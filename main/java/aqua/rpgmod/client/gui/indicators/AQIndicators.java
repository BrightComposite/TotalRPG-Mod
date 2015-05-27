package aqua.rpgmod.client.gui.indicators;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;

import org.lwjgl.opengl.GL11;

import aqua.rpgmod.client.gui.AQGuiUtils;
import aqua.rpgmod.player.AQThisPlayerWrapper;

public class AQIndicators
{
	public static final AQIndicators instance = new AQIndicators();
	protected final ArrayList<AQGuiIndicator> indicators = new ArrayList<AQGuiIndicator>();
	public AQThisPlayerWrapper wrapper = null;
	
	static
	{
		addIndicator(new AQHealthIndicator());
		addIndicator(new AQAirIndicator());
		addIndicator(new AQStaminaIndicator());
		addIndicator(new AQHungerIndicator());
		addIndicator(new AQThirstIndicator());
	}
	
	public static void addIndicator(AQGuiIndicator indicator)
	{
		instance.indicators.add(indicator);
	}
	
	public static void render(int width, int height)
	{
		int size = 32;
		int x = width - size - 4;
		int y = (height - instance.indicators.size() * size) / 2;
		
		Minecraft mc = Minecraft.getMinecraft();
		
		GL11.glEnable(GL11.GL_BLEND);
		
		for(int i = 0; i < instance.indicators.size(); ++i)
		{
			AQGuiIndicator indicator = instance.indicators.get(i);
			indicator.draw(mc, x, y);
			GL11.glColor3d(1.0f, 1.0f, 1.0f);
			mc.getTextureManager().bindTexture(AQGuiIndicator.front);
			
			float[] v;
			
			if(instance.indicators.size() == 1)
				v = new float[]
				{
					0.0f, 0.25f
				};
			else if(i == 0)
				v = new float[]
				{
					0.25f, 0.5f
				};
			else if(i == instance.indicators.size() - 1)
				v = new float[]
				{
					0.75f, 1.0f
				};
			else
				v = new float[]
				{
					0.5f, 0.75f
				};
			
			AQGuiUtils.renderImage(x, y + 1, size, size, 0.0f, v[0], 1.0f, v[1], 0.0f);
			y += size;
		}
		
		GL11.glDisable(GL11.GL_BLEND);
	}
}
