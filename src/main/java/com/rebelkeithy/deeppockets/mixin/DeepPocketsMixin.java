package com.rebelkeithy.deeppockets.mixin;

import com.rebelkeithy.deeppockets.DeepPockets;
import com.rebelkeithy.deeppockets.PackItem;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public class DeepPocketsMixin {

	private PlayerEntity player;

	@Inject(at = @At("HEAD"), method = "onPlayerCollision(Lnet/minecraft/entity/player/PlayerEntity;)V")
	private void init(PlayerEntity player, CallbackInfo ci) {
		this.player = player;
	}

	@ModifyArg(method = "onPlayerCollision(Lnet/minecraft/entity/player/PlayerEntity;)V", at = @At(value = "INVOKE", target = "net/minecraft/entity/player/PlayerInventory.insertStack(Lnet/minecraft/item/ItemStack;)Z"), index = 0)
	private ItemStack init(ItemStack stack) {
		for (int i = 0; i < player.getInventory().size(); i++) {
			ItemStack inventoryStack = player.getInventory().getStack(i);
			if (!inventoryStack.isEmpty() && inventoryStack.getItem() instanceof PackItem packItem) {
				int count = stack.getCount();
				if (packItem.canAccept(stack) && packItem.addStack(inventoryStack, stack)) {
					player.sendPickup((ItemEntity)(Object)this, count);
					return stack;
				}
			}
		}
		return stack;
	}
}
