package aqua.rpgmod.client.gui.indicators;

import aqua.rpgmod.AQModInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

abstract public class AQGuiIndicator extends Gui
{
	public static final ResourceLocation back = new ResourceLocation(AQModInfo.MODID + ":" + "textures/gui/indicators/back.png");
	public static final ResourceLocation front = new ResourceLocation(AQModInfo.MODID + ":" + "textures/gui/indicators/front.png");
	
	public static float[] getColor(float percent)
	{
		return new float[]
		{
			Math.min(0.6f, (1.0f - percent) * 1.2f), Math.min(0.6f, percent * 1.2f), 0.0f
		};
	}
	
	abstract public void draw(Minecraft mc, int x, int y);
	
	abstract public String getDesc(int level);
}
