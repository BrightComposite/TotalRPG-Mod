package aqua.rpgmod.client.gui.bars;

import java.awt.Color;
import java.util.HashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class AQSimpleBarRenderer extends AQBarRenderer
{
	public static class ColorSet
	{
		protected float components[];
		
		public ColorSet(float components[])
		{
			this.components = components;
		}
		
		@SuppressWarnings("unused")
		public void apply(float brightness, float percent)
		{}
		
		public int get(int index)
		{
			return Color.HSBtoRGB(this.components[index * 3], this.components[index * 3 + 1], this.components[index * 3 + 2]);
		}
	}
	
	public static class VariableColorSet extends ColorSet
	{
		public VariableColorSet(float[] components)
		{
			super(components);
		}
		
		@Override
		public void apply(float brightness, float percent)
		{
			this.components[2] = brightness;
			this.components[4] = percent;
		}
	}
	
	protected HashMap<Integer, ColorSet> colors = new HashMap<Integer, ColorSet>();
	
	public AQSimpleBarRenderer(Minecraft minecraft)
	{
		super(minecraft);
		
		setColorSet(Type.HEALTH.ordinal(), new VariableColorSet(new float[]
		{
			0.0f, 1.0f, -1.0f, 0.0f, -1.0f, 0.3f, 0.1f, 1.0f, 0.1f
		}));
		
		setColorSet(Type.HEALTHMOUNT.ordinal(), new VariableColorSet(new float[]
		{
			0.05f, 1.0f, -1.0f, 0.0f, -1.0f, 0.3f, 0.1f, 1.0f, 0.1f
		}));
		
		setColorSet(Type.MANA.ordinal(), new VariableColorSet(new float[]
		{
			0.7f, 1.0f, -1.0f, 0.7f, -1.0f, 0.3f, 0.7f, 1.0f, 0.1f
		}));
		
		setColorSet(Type.ARMOR.ordinal(), new ColorSet(new float[]
		{
			0.0f, 0.0f, 0.6f, 0.0f, 0.0f, 0.3f, 0.0f, 1.0f, 0.1f
		}));
		
		setColorSet(Type.STAMINA.ordinal(), new VariableColorSet(new float[]
		{
			0.1f, 1.0f, -1.0f, 0.1f, -1.0f, 0.3f, 0.1f, 1.0f, 0.1f
		}));
		
		setColorSet(Type.JUMP.ordinal(), new ColorSet(new float[]
		{
			0.7f, 0.4f, 1.0f, 0.1f, 0.4f, 0.3f, 0.1f, 1.0f, 0.1f
		}));
		
		setColorSet(Type.HUNGER.ordinal(), new VariableColorSet(new float[]
		{
			0.3f, 1.0f, -1.0f, 0.3f, -1.0f, 0.3f, 0.3f, 1.0f, 0.1f
		}));
		
		setColorSet(Type.THIRST.ordinal(), new VariableColorSet(new float[]
		{
			0.5f, 1.0f, -1.0f, 0.5f, -1.0f, 0.3f, 0.5f, 1.0f, 0.1f
		}));
		
		setColorSet(Type.AIR.ordinal(), new ColorSet(new float[]
		{
			0.6f, 0.2f, 1.0f, 0.6f, 0.2f, 0.3f, 0.6f, 0.2f, 0.1f
		}));
		
		this.barHeight = 4;
	}
	
	public void setColorSet(int type, ColorSet colorSet)
	{
		this.colors.put(Integer.valueOf(type), colorSet);
	}
	
	@Override
	public void drawBar(int x, int y, int width, int height, float percent, int dir, int number, int type)
	{
		ColorSet colorSet = this.colors.get(Integer.valueOf(type));
		
		if(colorSet == null)
			return;
		
		float barBrightness = (percent + 1.0f) * 0.5f;
		colorSet.apply(barBrightness, percent);
		
		int value = Math.round(width * percent);
		
		Gui.drawRect(x + dir * 1, y + 0, x + dir * (3 + width), y + height - 0, colorSet.get(2));
		Gui.drawRect(x + dir * 2, y + 1, x + dir * (2 + width), y + height - 1, colorSet.get(1));
		Gui.drawRect(x + dir * 2, y + 1, x + dir * (2 + value), y + height - 1, colorSet.get(0));
	}
}
