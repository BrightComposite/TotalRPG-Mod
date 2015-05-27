package aqua.rpgmod.player.params;

import net.minecraft.nbt.NBTTagCompound;
import aqua.rpgmod.player.AQPlayerWrapper;

public class AQLifeParameter extends AQUpdatingParameter
{
	public enum Side
	{
		CLIENT,
		SERVER,
		BOTH;
	}
	
	public final String name;
	public final AQPlayerWrapper wrapper;
	public final Side side;
	
	public AQLifeParameter(String name, AQPlayerWrapper wrapper, Side side, int value, int max)
	{
		super(value, max);
		this.name = name;
		this.wrapper = wrapper;
		this.side = side;
		
		wrapper.params.add(this);
	}
	
	@Override
	public boolean changeBy(float amount)
	{
		if(this.wrapper.player.capabilities.isCreativeMode)
			return false;
		
		super.changeBy(amount);
		return true;
	}
	
	public void saveNBTData(NBTTagCompound compound)
	{
		compound.setFloat(this.name, this.value);
	}
	
	public void loadNBTData(NBTTagCompound compound)
	{
		if(compound.hasKey(this.name))
			this.value = compound.getFloat(this.name);
		else
			restore();
	}
}
