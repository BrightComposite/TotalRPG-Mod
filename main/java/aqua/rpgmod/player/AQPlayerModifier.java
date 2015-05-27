package aqua.rpgmod.player;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class AQPlayerModifier
{
	public float value;
	float def;
	
	AQPlayerModifier(float def)
	{
		setDefault(def);
	}
	
	public void setDefault(float def)
	{
		this.def = def;
		reset();
	}
	
	public float getDefault()
	{
		return this.def;
	}
	
	public void reset()
	{
		this.value = this.def;
	}
}
