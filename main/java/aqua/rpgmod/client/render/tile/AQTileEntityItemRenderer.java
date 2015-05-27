package aqua.rpgmod.client.render.tile;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class AQTileEntityItemRenderer implements IItemRenderer
{
	final TileEntitySpecialRenderer renderer;
	final TileEntity tile;
	
	AQTileEntityItemRenderer(TileEntitySpecialRenderer renderer, TileEntity tile)
	{
		this.renderer = renderer;
		this.tile = tile;
	}
	
	public static void register(Class<? extends TileEntity> cl, Block block, TileEntitySpecialRenderer renderer)
	{
		try
		{
			AQTileEntityItemRenderer itemRenderer = new AQTileEntityItemRenderer(renderer, cl.newInstance());
			MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(block), itemRenderer);
	        ClientRegistry.bindTileEntitySpecialRenderer(cl, renderer);
		}
		catch(InstantiationException e)
		{
			e.printStackTrace();
		}
		catch(IllegalAccessException e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type)
	{
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
	{
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object ... data)
	{
		GL11.glTranslatef(-0.5F, 0.0F, -0.5F);
		GL11.glScaled(1.5, 1.5, 1.5);
		this.renderer.renderTileEntityAt(this.tile, 0.0D, 0.0D, 0.0D, 0.0F);
	}	
	
}
