package com.rebelkeithy.deeppockets.proxy.compatability;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class Compatability {
	
	private static int version = -1;
	
	public static void init()
	{
		Method[] methods = ItemStack.class.getMethods();
		for(Method m : methods)
		{
			if(m.getName().equals("isEmpty") || m.getName().equals("func_190926_b"))
			{
				version = 11;
				return;
			}
		}
		
		version = 10;
	}
	
	public static ItemStack[] getInventory(InventoryPlayer inventory)
	{
		if(version == 11)
		{
			return inventory.mainInventory.toArray(new ItemStack[inventory.mainInventory.size()]);
		}
		
		if(version == 10)
		{
			Field f = ReflectionHelper.findField(InventoryPlayer.class, "field_70462_a", "mainInventory");
			try {
				return (ItemStack[]) f.get(inventory);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	public static void setCount(ItemStack stack, int count)
	{
		if(version == 11)
		{
			Method m = ReflectionHelper.findMethod(ItemStack.class, stack, new String[] {"func_190920_e", "setCount"}, int.class);
			try {
				m.invoke(stack, count);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}

		if(version == 10)
		{
			Field f = ReflectionHelper.findField(ItemStack.class, new String[] {"field_77994_a", "stackSize" });
			try {
				f.set(stack, count);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static int getStackCount(ItemStack stack)
	{
		if(version == 11)
		{
			Method m = ReflectionHelper.findMethod(ItemStack.class, stack, new String[] {"func_190916_E", "getCount"});
			try {
				return (Integer) m.invoke(stack);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		
		if(version == 10)
		{
			Field f = ReflectionHelper.findField(ItemStack.class, new String[] {"field_77994_a", "stackSize" });
			try {
				return f.getInt(stack);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
		return -1;
	}
	
	public static boolean isItemStackNull(ItemStack stack)
	{
		if(version == 11)
		{
			try {
				Method m = ReflectionHelper.findMethod(ItemStack.class, stack, new String[] {"b", "func_190926_b", "isEmpty"});
				return (Boolean) m.invoke(stack);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (Exception e) {
				
			}
		}

		if(version == 10)
			return stack == null;
		
		return true;
	}
}
