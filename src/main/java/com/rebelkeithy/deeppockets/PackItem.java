package com.rebelkeithy.deeppockets;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.CallbackI;

import java.util.*;
import java.util.stream.Collectors;

public class PackItem extends Item {
    public static Map<Integer, String> prefix = Map.of(
            0, "No",
            1, "A Few ",
            5, "Several ",
            16, "Piles of ",
            32, "Lots of ",
            64, "Hoards of ",
            128, "Throngs of ",
            256, "Swarms of ",
            512, "Zounds of ",
            1024, "Legions of "
    );

    private int size;

    public PackItem(Settings settings, int size) {
        super(settings);
        this.size = size;
    }

    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        DefaultedList<ItemStack> inventory = getInventory(stack);
        tooltip.add(new LiteralText("Contains:").formatted(Formatting.GRAY));
        boolean empty = true;
        for (ItemStack inventoryStack : inventory) {
            if (!inventoryStack.isEmpty()) {
                MutableText amount = getPrefix(inventoryStack.getCount()).formatted(Formatting.GRAY);
                MutableText item = new TranslatableText(inventoryStack.getTranslationKey()).formatted(Formatting.DARK_AQUA);
                tooltip.add(amount.append(item));
                empty = false;
            }
        }
        if(empty) {
            tooltip.add(new LiteralText("Nothing").formatted(Formatting.GRAY));
        }
    }

    public ActionResult useOnBlock(ItemUsageContext context) {
        if (context.getWorld() != null) {
            BlockEntity entity = context.getWorld().getBlockEntity(context.getBlockPos());
            if (entity instanceof Inventory inventory) {
                DefaultedList<ItemStack> packInventory = getInventory(context.getStack());
                int total = packInventory.stream().mapToInt(ItemStack::getCount).sum();
                if (total == 0) return ActionResult.PASS;

                if (context.getWorld().isClient()) {
                    context.getWorld().playSound(context.getPlayer(), context.getBlockPos(), SoundEvents.BLOCK_GRAVEL_FALL, SoundCategory.PLAYERS, 0.5f, 0.6f);
                    return ActionResult.SUCCESS;
                }

                List<ItemStack> itemsToTransfer = packInventory.stream().filter(itemStack -> !itemStack.isEmpty()).collect(Collectors.toList());

                Random random = new Random();
                int emergencyStop = 1000;
                while (itemsToTransfer.size() > 0) {
                    int packIndex = random.nextInt(itemsToTransfer.size());
                    ItemStack stackToTransfer = itemsToTransfer.get(packIndex);
                    int amountToTransfer = Math.min(stackToTransfer.getCount(), random.nextInt(45));

                    List<Integer> possibleSlots = new ArrayList<>();
                    for (int i = 0; i < inventory.size(); i++) {
                         ItemStack invStack = inventory.getStack(i);
                         if (invStack.isEmpty()) {
                             possibleSlots.add(i);
                         } else if (ItemStack.canCombine(invStack, stackToTransfer) && invStack.getCount() < invStack.getMaxCount()) {
                             possibleSlots.add(i);
                         }
                    }

                    if (possibleSlots.isEmpty()) {
                        itemsToTransfer.remove(packIndex);
                        continue;
                    }

                    int slotToTransferInto = possibleSlots.get(random.nextInt(possibleSlots.size()));

                    ItemStack invStack = inventory.getStack(slotToTransferInto);
                    int amountToIncrement = Math.min(amountToTransfer, invStack.getMaxCount() - invStack.getCount());
                    if (invStack.isEmpty()) {
                        invStack = stackToTransfer.copy();
                        invStack.setCount(amountToTransfer);
                        if (invStack.getCount() > invStack.getMaxCount()) {
                            invStack.setCount(invStack.getMaxCount());
                        }
                        stackToTransfer.decrement(invStack.getCount());
                        inventory.setStack(slotToTransferInto, invStack);
                    } else {
                        invStack.increment(amountToIncrement);
                        stackToTransfer.decrement(amountToIncrement);
                        inventory.setStack(slotToTransferInto, invStack);
                    }

                    if (stackToTransfer.isEmpty()) {
                        itemsToTransfer.remove(packIndex);
                        continue;
                    }
                    if(emergencyStop-- < 0) {
                        break;
                    }
                }

//                for (ItemStack item : packInventory) {
//                    if (context.getWorld().isClient()) {
//                        context.getWorld().playSound(context.getPlayer(), context.getBlockPos(), SoundEvents.BLOCK_GRAVEL_FALL, SoundCategory.PLAYERS, 0.5f, 0.1f);
//                        return ActionResult.SUCCESS;
//                    }
//
//                    if (item.isEmpty()) continue;
//                    for (int i = 0; i < inventory.size(); i++) {
//                        if (inventory.isValid(i, item)) {
//                            ItemStack invStack = inventory.getStack(i);
//                            if (invStack.isEmpty()) {
//                                ItemStack transferStack = item.copy();
//                                if (transferStack.getCount() > transferStack.getMaxCount()) {
//                                    transferStack.setCount(transferStack.getMaxCount());
//                                }
//                                item.decrement(transferStack.getCount());
//                                inventory.setStack(i, transferStack);
//                            }
//                            if (ItemStack.canCombine(inventory.getStack(i), item)) {
////                                inventory.setStack(i, item);
////                                break;
//                            }
//                        }
//                    }
//                }
                this.setInventory(context.getStack(), packInventory);
            }
        }
        return ActionResult.PASS;
    }

    public boolean canAccept(ItemStack stack) {
        if (stack.isEmpty()) return false;
        return DeepPockets.BAGGABLE.contains(stack.getItem());
    }

    public LiteralText getPrefix(int amount) {
        List<Integer> keys = new ArrayList<>(prefix.keySet());
        keys.sort(Collections.reverseOrder());
        for (int i : keys) {
            if (amount >= i) {
                return new LiteralText(prefix.get(i));
            }
        }
        return new LiteralText("Error ");
    }

    public boolean addStack(ItemStack pack, ItemStack item) {
        DefaultedList<ItemStack> stacks = getInventory(pack);

        for (int i = 0; i < stacks.size(); i++) {
            ItemStack inventoryStack = stacks.get(i);
            if (ItemStack.canCombine(inventoryStack, item)) {
                inventoryStack.increment(item.getCount());
                item.setCount(0);
                this.setInventory(pack, stacks);
                return true;
            }
        }
        for (int i = 0; i < stacks.size(); i++) {
            ItemStack inventoryStack = stacks.get(i);
            if (inventoryStack.isEmpty()) {
                stacks.set(i, item.copy());
                item.setCount(0);
                this.setInventory(pack, stacks);
                return true;
            }
        }
        return false;
    }

    public void setInventory(ItemStack stack, DefaultedList<ItemStack> inventory) {
        stack.getOrCreateNbt().put("stored_items", inventoryToNbtList(inventory));
    }

    public DefaultedList<ItemStack> getInventory(ItemStack stack) {
        if(stack.hasNbt()) {
            return inventoryReadNbtList(stack.getNbt().getList("stored_items", 10));
        }
        return DefaultedList.ofSize(size, ItemStack.EMPTY);
    }


    public DefaultedList<ItemStack> inventoryReadNbtList(NbtList nbtList) {
        DefaultedList<ItemStack> stacks = DefaultedList.ofSize(size, ItemStack.EMPTY);
        for(int i = 0; i < nbtList.size(); ++i) {
            ItemStack itemStack = itemstackFromNbt(nbtList.getCompound(i));
            if (!itemStack.isEmpty()) {
                stacks.set(i, itemStack);
            }
        }
        return stacks;
    }

    public NbtList inventoryToNbtList(DefaultedList<ItemStack> inventory) {
        NbtList nbtList = new NbtList();
        for (ItemStack itemStack : inventory) {
            nbtList.add(itemstackWriteNbt(itemStack, new NbtCompound()));
        }
        return nbtList;
    }

    public static ItemStack itemstackFromNbt(NbtCompound nbt) {
        int count = nbt.getInt("Count");
        // If count > 128 then ItemStack.fromNbt will return a stack with count < 0
        ItemStack itemStack = ItemStack.fromNbt(nbt);
        if (count > 0) {
            itemStack.setCount(count);
        }
        return itemStack;
    }

    public NbtCompound itemstackWriteNbt(ItemStack stack, NbtCompound nbt) {
        stack.writeNbt(nbt);
        //Identifier identifier = Registry.ITEM.getId(stack.getItem());
        //nbt.putString("id", identifier == null ? "minecraft:air" : identifier.toString());
        nbt.putInt("Count", stack.getCount());
//        if (stack.getNbt() != null) {
//            nbt.put("tag", stack.getNbt().copy());
//        }
        return nbt;
    }

}
