package aqua.rpgmod.client.gui.bars;

import java.util.ArrayList;
import java.util.HashMap;
import net.minecraft.client.Minecraft;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
abstract public class AQBarRenderer
{
	protected float animSpeed = 0.01f;
	protected Minecraft mc;
	protected AQBarArranger arranger = null;
	protected int barWidth = 88;
	protected int barHeight = 5;
	
	protected HashMap<Integer, AQBarController> bars = new HashMap<Integer, AQBarController>();
	protected static ArrayList<Integer> types = new ArrayList<Integer>();
	
	public static enum Type
	{
		HEALTH,
		HEALTHMOUNT,
		MANA,
		ARMOR,
		STAMINA,
		JUMP,
		HUNGER,
		THIRST,
		AIR
	}
	
	protected static final int defaultTypes;
	protected static int lastType;
	
	static
	{
		for(Type type : Type.values())
			types.add(Integer.valueOf(type.ordinal()));
		
		lastType = defaultTypes = types.size();
	}
	
	public AQBarRenderer(Minecraft minecraft)
	{
		this.mc = minecraft;
		
		this.bars.put(Integer.valueOf(Type.HEALTH.ordinal()), new AQHealthBar());
		this.bars.put(Integer.valueOf(Type.HEALTHMOUNT.ordinal()), new AQHealthMountBar());
		this.bars.put(Integer.valueOf(Type.MANA.ordinal()), new AQManaBar());
		this.bars.put(Integer.valueOf(Type.ARMOR.ordinal()), new AQArmorBar());
		this.bars.put(Integer.valueOf(Type.STAMINA.ordinal()), new AQStaminaBar());
		this.bars.put(Integer.valueOf(Type.JUMP.ordinal()), new AQJumpBar());
		this.bars.put(Integer.valueOf(Type.HUNGER.ordinal()), new AQHungerBar());
		this.bars.put(Integer.valueOf(Type.THIRST.ordinal()), new AQThirstBar());
		this.bars.put(Integer.valueOf(Type.AIR.ordinal()), new AQAirBar());
	}
	
	abstract public void drawBar(int x, int y, int width, int height, float percent, int dir, int number, int type);
	
	public void setArranger(AQBarArranger arranger)
	{
		this.arranger = arranger;
	}
	
	public void render(int x, int y, int width, int height, int number)
	{
		if(this.arranger == null)
			return;
		
		Integer type = types.get(number);
		AQBarController bar = this.bars.get(type);
		
		if(bar == null)
			return;
		
		float value = bar.getPercent(this, this.animSpeed);
		
		if(!bar.visible)
			return;
		
		if(!this.arranger.place(x, y, width, height, this.barWidth, this.barHeight, number, type.intValue()))
			return;
		
		float modifier = 1.0f;
		
		if(bar.metaController != null)
		{
			modifier = bar.metaController.getPercent(this, this.animSpeed);
		}
		
		drawBar(this.arranger.x, this.arranger.y, Math.round(this.barWidth * modifier), this.barHeight, value, this.arranger.dir, number, type.intValue());
	}
	
	public void renderAll(int x, int y, int width, int height)
	{
		for(int i = 0; i < types.size(); i++)
			render(x, y, width, height, i);
	}
	
	public static int registerNewType()
	{
		int type = lastType++;
		types.add(new Integer(type));
		
		return type;
	}
	
	public static int registerNewType(int number)
	{
		int type = lastType++;
		types.add(number, new Integer(type));
		
		return type;
	}
}
