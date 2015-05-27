package aqua.rpgmod.block;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import aqua.rpgmod.AQCreativeTabs;
import aqua.rpgmod.AQModInfo;
import aqua.rpgmod.tileentity.AQBonfireTileEntity;
import aqua.rpgmod.tileentity.AQTotemTileEntity;
import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraft.block.Block;

public class AQBlocks
{
	public static final List<Block> blocks = new ArrayList<Block>();
	
	public static AQBlockTotem totem;
	public static AQBlockBad bad;
	public static AQBlockBonfire bonfire;
	
	public static void init()
	{
		totem = new AQBlockTotem();
		bad = new AQBlockBad();
		bonfire = new AQBlockBonfire();
		
		for(Iterator<Block> it = blocks.iterator(); it.hasNext();)
		{
			Block block = it.next();
			String name = block.getUnlocalizedName();
			
			block.setCreativeTab(AQCreativeTabs.instance);
			block.setBlockTextureName(AQModInfo.MODID + ":" + name);
			GameRegistry.registerBlock(block, name);
		}
		
		GameRegistry.registerTileEntity(AQTotemTileEntity.class, "total:totem");
		GameRegistry.registerTileEntity(AQBonfireTileEntity.class, "total:bonfire");
		AQBlockTotem.init();
	}
	
	public static void addBlock(Block block)
	{
		blocks.add(block);
	}
}
