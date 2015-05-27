package aqua.rpgmod.client.render.tile;

import org.lwjgl.opengl.GL11;

import aqua.rpgmod.client.model.tile.AQModelBonfire;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;

public class AQBonfireRenderer extends TileEntitySpecialRenderer
{
	public static final AQModelBonfire model = new AQModelBonfire();
	
	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float ticks)
	{
	    GL11.glPushMatrix();
	    GL11.glTranslated(x + 0.5, y + 0.25, z + 0.5);
        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
		model.render((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
	    GL11.glPopMatrix();
	}
}
