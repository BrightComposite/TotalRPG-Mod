package aqua.rpgmod.player;

import java.util.ArrayList;

import net.minecraft.util.DamageSource;

public class AQDamageSource extends DamageSource
{
	
	protected static ArrayList<AQDamageSource> list = new ArrayList<AQDamageSource>();
	public static DamageSource thirst = new AQDamageSource("thirst").setDamageBypassesArmor().setDamageIsAbsolute();
	
	public AQDamageSource(String p_i1566_1_)
	{
		super(p_i1566_1_);
		list.add(this);
	}
	
	public static AQDamageSource get(int index)
	{
		return list.get(index);
	}
	
	public static int indexOf(DamageSource source)
	{
		return list.indexOf(source);
	}
}
