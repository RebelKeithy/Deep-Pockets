package com.rebelkeithy.deeppockets.Items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.rebelkeithy.deeppockets.DeepPocketsConfig;
import com.rebelkeithy.deeppockets.proxy.compatability.Compatability;

import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class ItemMiningPack extends Item {
	
	ItemMiningPack(){
		this.setCreativeTab(CreativeTabs.TOOLS);
		this.setHasSubtypes(true);
	}

	public static Map<Integer, String> prefix = new HashMap<Integer, String>();
	static
	{
		prefix.put(0, "No ");
		prefix.put(1, "A Few ");
		prefix.put(5, "Several ");
		prefix.put(16, "Piles of ");
		prefix.put(32, "Lots of ");
		prefix.put(64, "Hoards of ");
		prefix.put(128, "Throngs of ");
		prefix.put(256, "Swarms of ");
		prefix.put(512, "Zounds of ");
		prefix.put(1024, "Legions of ");
	}
	
	private int maxSlots;
	
	public ItemMiningPack(int maxSlots)
	{
		this.maxSlots = maxSlots;
		this.setMaxStackSize(1);
	}
	
	public int getMaxSlots()
	{
		return maxSlots;
	}
	
	public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List<String> list, boolean bool) 
	{
		NBTTagCompound tag = itemStack.getTagCompound();
		if(tag == null)
			return;

		NBTTagCompound ores = tag.getCompoundTag("ores");

		if(ores.getKeySet().size() > 0)
			list.add(TextFormatting.GRAY + "Slots Used: " + ores.getSize() + "/" + maxSlots);
		
		for(String ore : ores.getKeySet())
		{
			int amount = ores.getCompoundTag(ore).getInteger("amount");
			String name = ores.getCompoundTag(ore).getString("name");
			String p = "error";
			int max = -1;
			//System.out.println(prefix.keySet());
			for(Integer i : prefix.keySet())
			{
				if(i > max && i <= amount)
				{
					p = prefix.get(i);
					max = i;
				}
			}
			String lock = "";
			if(ores.getCompoundTag(ore).getBoolean("lock"))
				lock = TextFormatting.DARK_GRAY + " *";
			
			list.add(TextFormatting.GRAY + p + TextFormatting.DARK_AQUA + name.replace(" Ore", "") + lock);
			//list.add(p + name + " (" + amount + ")");
		}
		
	}
	
    public EnumActionResult func_180614_a(ItemStack stack, EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
    	System.out.println("*** On Item Use Works ***");
    	return this.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }
	
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
    	System.out.println("*** On Item Use Works ***");
    	return this.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }
	

    /**
     * Called when a Block is right-clicked with this Item
     */
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
    	TileEntity te = worldIn.getTileEntity(pos);
    	System.out.println(te);
    	if(te instanceof IInventory)
    	{
    		IInventory inventory = (IInventory)te;
    		ItemStack itemStack = player.getHeldItemMainhand();
    		
    		if(worldIn.isRemote)
    		{
    			if(!isEmpty(itemStack))
    			worldIn.playSound(player, pos, SoundEvents.BLOCK_GRAVEL_FALL, SoundCategory.PLAYERS, 0.5f, 0.6f);
    			return EnumActionResult.PASS;
    		}
        	//System.out.println("test " + itemStack);
    		return this.depositOresRandomly(inventory, itemStack);
    		
    	}
    	

    	IBlockState block = worldIn.getBlockState(pos);
    	ItemStack clickedBlockStack = new ItemStack(block.getBlock(), 1, block.getBlock().getMetaFromState(block));
    	if(block.getBlock() == Blocks.LIT_REDSTONE_ORE)
    		clickedBlockStack = new ItemStack(Blocks.REDSTONE_ORE, 1);
    	
    	if(Compatability.isItemStackNull(clickedBlockStack))
    		return EnumActionResult.PASS;
    	
    	int[] oreIDs = OreDictionary.getOreIDs(clickedBlockStack);
    	if(oreIDs.length > 0)
    	{
    		String oreName = OreDictionary.getOreName(oreIDs[0]);
    		if(oreName.contains("ore"))
    		{
	    		ItemStack itemStack = player.getHeldItemMainhand();
	    		NBTTagCompound tag = itemStack.getTagCompound();
	    		if(tag == null)
	    			tag = new NBTTagCompound();
	    		
	    		NBTTagCompound ores = tag.getCompoundTag("ores");
	    		NBTTagCompound thisOre = ores.getCompoundTag(oreName);
	    		if(thisOre.getBoolean("lock"))
	    		{
	    			thisOre.setBoolean("lock", false);
	    			worldIn.playSound(player, pos, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.3f, 0.5f);
	    		}
	    		else if(ores.getSize() < maxSlots)
	    		{
	    			thisOre.setBoolean("lock", true);
	    			thisOre.setString("name", clickedBlockStack.getDisplayName());
	    			thisOre.setString("registry", clickedBlockStack.getItem().getRegistryName().toString());
	    			thisOre.setInteger("meta", clickedBlockStack.getItemDamage());
		    		if(!thisOre.hasKey("amount"))
		    			thisOre.setInteger("amount", 0);
	    			worldIn.playSound(player, pos, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.3f, 0.8f);
	    		}
	    		
	    		
	    		ores.setTag(oreName, thisOre);
				if(thisOre.getInteger("amount") == 0 && !thisOre.getBoolean("lock"))
				{
					ores.removeTag(oreName);
				}
	    		tag.setTag("ores", ores);
	    		itemStack.setTagCompound(tag);
	    		
	        	System.out.println(tag);
    		}
    	}
    	
        return EnumActionResult.PASS;
    }
    
    public EnumActionResult depositOresRandomly(IInventory inventory, ItemStack itemStack)
    {
    	
    	System.out.println("Depositing ore");
    	NBTTagCompound tag = itemStack.getTagCompound();
		if(tag == null)
			return EnumActionResult.PASS;
		
		Random rand = new Random();
		List<StoredOre> ores = new ArrayList<StoredOre>();
		
		NBTTagCompound oresTag = tag.getCompoundTag("ores");		
		for(String ore : oresTag.getKeySet())
		{
			ores.add(this.getOre(itemStack, ore));
		}
		
		if(ores.isEmpty())
			return EnumActionResult.PASS;
		
		boolean done = false;
		while(!done)
		{
			StoredOre ore = ores.get(rand.nextInt(ores.size()));
			int amount = Math.min(ore.amount, rand.nextInt(45));
			
			ItemStack stack = DeepPocketsConfig.getStack(ore, amount);

			List<Integer> possibleSlots = new ArrayList<Integer>();
			for(int i = 0; i < inventory.getSizeInventory(); i++)
			{
				if(inventory.isItemValidForSlot(i, stack))
				{
    				if(Compatability.isItemStackNull(inventory.getStackInSlot(i)) || inventory.getStackInSlot(i).getItem() == null)
    				{
    					possibleSlots.add(i);
    				}
    				else if(inventory.getStackInSlot(i).getItem() == stack.getItem())
    				{
    					if(Compatability.getStackCount(inventory.getStackInSlot(i)) < inventory.getStackInSlot(i).getMaxStackSize())
							possibleSlots.add(i);
    				}
				}
			}
			
			if(possibleSlots.isEmpty())
			{
				ores.remove(ore);
				this.setOre(itemStack, ore.oreDictName, ore);
				
				if(ores.isEmpty())
				{
					System.out.println("Inventory full for all ores");
					done = true;
				}
				
				continue;
			}
			
			int index = possibleSlots.get(rand.nextInt(possibleSlots.size()));
			

			if(Compatability.isItemStackNull(inventory.getStackInSlot(index)))
			{
				inventory.setInventorySlotContents(index, stack.copy());
				ore.amount -= amount;
			}
			else if(inventory.getStackInSlot(index).getItem() == stack.getItem())
			{
				int amountToTransfer = Math.min(amount, 64 - Compatability.getStackCount(inventory.getStackInSlot(index)));
				ItemStack toTransfer = stack.splitStack(amountToTransfer);
				
				int fullAmount = Compatability.getStackCount(toTransfer) + Compatability.getStackCount(inventory.getStackInSlot(index));
				Compatability.setCount(inventory.getStackInSlot(index), fullAmount);
				ore.amount -= Compatability.getStackCount(toTransfer);
			}
			
			if(ore.amount == 0)
			{
				ores.remove(ore);
				this.setOre(itemStack, ore.oreDictName, ore);
				
				if(ores.isEmpty())
					done = true;
			}
			
		}
    	
    	
		return EnumActionResult.PASS;
    }
    
    public boolean containsOre(ItemStack pack, String oreName)
    {
    	if(pack.getTagCompound() == null)
    		return false;
    	
    	return pack.getTagCompound().getCompoundTag("ores").hasKey(oreName);
    }

    public int getOreAmount(ItemStack pack, String oreName)
    {
    	if(pack.getTagCompound() == null || !containsOre(pack, oreName))
    		return 0;
    	
    	return pack.getTagCompound().getCompoundTag("ores").getCompoundTag(oreName).getInteger("amount");
    }
    
    public int numStoredOres(ItemStack pack)
    {
    	if(pack.getTagCompound() == null)
    		return 0;
    	
    	return pack.getTagCompound().getCompoundTag("ores").getSize();
    }
    
    public StoredOre getOre(ItemStack pack, String oreName)
    {
    	if(pack.getTagCompound() == null || !pack.getTagCompound().getCompoundTag("ores").hasKey(oreName))
    	{
    		return new StoredOre(oreName, 0, "");
    	}
    	
    	return new StoredOre(pack.getTagCompound().getCompoundTag("ores").getCompoundTag(oreName), oreName);
    }
    
    public void setOre(ItemStack pack, String oreName, StoredOre ore)
    {
    	if(pack.getTagCompound() == null)
    		pack.setTagCompound(new NBTTagCompound());
    	
    	NBTTagCompound oreTag = pack.getTagCompound().getCompoundTag("ores");    	
    	ore.save(oreTag);
    	pack.getTagCompound().setTag("ores", oreTag);
    }
    
    public boolean isEmpty(ItemStack pack)
    {
    	NBTTagCompound tag = pack.getTagCompound();
		if(tag == null)
			return true;
		
		NBTTagCompound oresTag = tag.getCompoundTag("ores");		
		for(String ore : oresTag.getKeySet())
		{
			if(tag.getCompoundTag("ores").getCompoundTag(ore).getInteger("amount") > 0)
				return false;
		}
		
		return true;
    }

	public int getTotalItems(ItemStack pack) 
	{
    	NBTTagCompound tag = pack.getTagCompound();
		if(tag == null)
			return 0;
		
		int items = 0;
		NBTTagCompound oresTag = tag.getCompoundTag("ores");		
		for(String ore : oresTag.getKeySet())
		{
			items += tag.getCompoundTag("ores").getCompoundTag(ore).getInteger("amount");
		}
		
		return items;
	}
    
    public class StoredOre
    {
    	public String oreDictName;
    	public String registryName;
    	public int meta;
    	public int amount;
    	public String displayName;
    	public boolean locked;
    	
    	public StoredOre(String oreDictName, int amount, String displayName, boolean locked)
    	{
    		this.oreDictName = oreDictName;
    		this.amount = amount;
    		this.displayName = displayName;
    		this.locked = locked;
    	}
    	
    	public StoredOre(String oreDictName, int amount, String displayName) { this(oreDictName, amount, displayName, false); }
    	
    	public StoredOre(NBTTagCompound tag, String oreDictName)
    	{
    		this.oreDictName = oreDictName;
    		this.registryName = tag.getString("registry");
    		this.meta = tag.getInteger("meta");
    		this.amount = tag.getInteger("amount");
    		this.displayName = tag.getString("name");
    		this.locked = tag.getBoolean("lock");
    	}
    	
    	public void save(NBTTagCompound tag)
    	{
    		if(amount > 0 || locked)
    		{
        		NBTTagCompound oreTag = new NBTTagCompound();
        		oreTag.setString("registry", registryName);
        		oreTag.setInteger("meta", meta);
	    		oreTag.setInteger("amount", amount);
	    		oreTag.setString("name", displayName);
	    		oreTag.setBoolean("lock", locked);
	    		tag.setTag(oreDictName, oreTag);
    		}
    		else
    		{
    			tag.removeTag(oreDictName);
    		}
    	}
    }  
}
