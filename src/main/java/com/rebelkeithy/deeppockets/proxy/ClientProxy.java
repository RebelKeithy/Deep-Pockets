package com.rebelkeithy.deeppockets.proxy;

import com.rebelkeithy.deeppockets.Items.DeepPocketsItems;

public class ClientProxy extends CommonProxy
{
	public void registerRenderers()
	{
		DeepPocketsItems.registeryRenderers();
	}
}
