package aqua.rpgmod.client.gui.menu;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import aqua.rpgmod.client.gui.bars.AQBarArranger;
import aqua.rpgmod.client.gui.bars.AQBarRenderer;
import aqua.rpgmod.client.gui.bars.AQSimpleBarRenderer;
import aqua.rpgmod.player.AQThisPlayerWrapper;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;

public class AQPlayerMenu extends AQMenuPage
{
	AQBarRenderer barRenderer = null;
	
	public AQPlayerMenu()
	{
		super("player");
	}
	
	protected void drawPlayer(EntityPlayer player, int x, int y, float scale, float mouseX, float mouseY)
	{
		final ScaledResolution scaledresolution = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
		int i = scaledresolution.getScaledWidth();
		int j = scaledresolution.getScaledHeight();
		
		int cx = this.xSize - 180;
		int cy = this.ySize - 40;
		
		int left = (this.width - cx) / 2 - 73;
		int top = (this.height - cy) / 2;
		
		final int l = left * this.mc.displayWidth / i;
		final int t = top * this.mc.displayHeight / j;
		final int w = cx * this.mc.displayWidth / i;
		final int h = cy * this.mc.displayHeight / j;
		
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		GL11.glScissor(l, t, w, h);
		// GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, 50.0F);
		GL11.glScalef(-scale, scale, scale);
		GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
		float f2 = player.renderYawOffset;
		float f3 = player.rotationYaw;
		float f4 = player.rotationPitch;
		float f5 = player.prevRotationYawHead;
		float f6 = player.rotationYawHead;
		GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-((float) Math.atan(mouseY / 40.0F)) * 20.0F, 1.0F, 0.0F, 0.0F);
		player.renderYawOffset = (float) Math.atan(mouseX / 40.0F) * 20.0F;
		player.rotationYaw = (float) Math.atan(mouseX / 40.0F) * 40.0F;
		player.rotationPitch = -((float) Math.atan(mouseY / 40.0F)) * 20.0F;
		player.rotationYawHead = player.rotationYaw;
		player.prevRotationYawHead = player.rotationYaw;
		
		AQThisPlayerWrapper wrapper = AQThisPlayerWrapper.getWrapper();
		wrapper.renderInMenu = true;
		GL11.glTranslatef(0.0F, player.yOffset - wrapper.yOffset, 0.0F);
		RenderManager.instance.playerViewY = 180.0F;
		RenderManager.instance.renderEntityWithPosYaw(player, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
		wrapper.renderInMenu = false;
		player.renderYawOffset = f2;
		player.rotationYaw = f3;
		player.rotationPitch = f4;
		player.prevRotationYawHead = f5;
		player.rotationYawHead = f6;
		GL11.glPopMatrix();
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
	}
	
	@Override
	public void drawBackground(int p_73863_1_, int p_73863_2_, float p_73863_3_)
	{
		super.drawBackground(p_73863_1_, p_73863_2_, p_73863_3_);
		
		drawPlayer(this.mc.thePlayer, this.guiLeft + 51, this.guiTop + 115, 40, this.guiLeft + 51 - p_73863_1_, this.guiTop + 63 - p_73863_2_);
		
		if(this.barRenderer == null)
		{
			this.barRenderer = new AQSimpleBarRenderer(this.mc);
			this.barRenderer.setArranger(AQBarArranger.menuArranger);
		}
		
		this.barRenderer.renderAll(this.guiLeft + 51, this.guiTop + 20, this.xSize, this.ySize);
	}
}
