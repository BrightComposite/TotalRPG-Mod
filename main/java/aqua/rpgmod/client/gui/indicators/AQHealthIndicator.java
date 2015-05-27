package aqua.rpgmod.client.gui.indicators;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import aqua.rpgmod.AQModInfo;
import aqua.rpgmod.client.gui.AQGuiUtils;
import aqua.rpgmod.player.AQThisPlayerWrapper;

public class AQHealthIndicator extends AQGuiIndicator
{
	static final ResourceLocation image = new ResourceLocation(AQModInfo.MODID + ":" + "textures/gui/indicators/health.png");
	
	public static float getPercent()
	{
		return AQThisPlayerWrapper.getWrapper().player.getHealth() / AQThisPlayerWrapper.getWrapper().player.getMaxHealth();
	}
	
	@Override
	public void draw(Minecraft mc, int x, int y)
	{
		float[] color = getColor(getPercent());
		GL11.glColor3f(color[0], color[1], color[2]);
		mc.getTextureManager().bindTexture(back);
		AQGuiUtils.renderImage(x, y + 1, 32, 32, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f);
		
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		mc.getTextureManager().bindTexture(image);
		AQGuiUtils.renderImage(x, y + 1, 32, 32, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f);
	}
	
	public String getDesc(int level)
	{
		return I18n.format("indicator.health" + level);
	}
}
