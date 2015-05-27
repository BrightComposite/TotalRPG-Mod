package aqua.rpgmod.entity;

import aqua.rpgmod.client.render.AQItemRendererLeftHand;
import aqua.rpgmod.player.AQThisPlayerWrapper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.ItemRenderer;

public class AQEntityRenderer extends EntityRenderer
{
	public final ItemRenderer itemRendererLeft;
	public Minecraft mc;
	
	public AQEntityRenderer(Minecraft mc)
	{
		super(mc, mc.getResourceManager());
		
		this.mc = mc;
		this.itemRendererLeft = new AQItemRendererLeftHand(mc);
	}
	
	/**
	 * Will update any inputs that effect the camera angle (mouse) and then
	 * render the world and GUI
	 */
	@Override
	public void updateCameraAndRender(float p_78480_1_)
	{
		if(this.mc.thePlayer == null || this.mc.thePlayer.isPlayerSleeping())
		{
			super.updateCameraAndRender(p_78480_1_);
			return;
		}
		
		this.mc.thePlayer.yOffset += AQThisPlayerWrapper.getWrapper().yOffset;
		super.updateCameraAndRender(p_78480_1_);
		this.mc.thePlayer.yOffset -= AQThisPlayerWrapper.getWrapper().yOffset;
	}
}
