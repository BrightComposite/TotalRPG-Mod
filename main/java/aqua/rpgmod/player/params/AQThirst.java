package aqua.rpgmod.player.params;

import aqua.rpgmod.player.AQPlayerWrapper;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

public class AQThirst extends AQLifeParameter
{
	public AQLifeParameter saturation;
	
	public AQThirst(AQPlayerWrapper wrapper)
	{
		super("thirst", wrapper, AQLifeParameter.Side.CLIENT, 50000, 50000);
		this.saturation = new AQLifeParameter("ts", wrapper, AQLifeParameter.Side.CLIENT, 20000, 20000);
	}
	
	@Override
	public boolean changeBy(float amount)
	{
		if(this.wrapper.player.capabilities.isCreativeMode)
			return false;
		
		this.saturation.changeBy(amount * 2);
		
		if(amount < 0 && this.saturation.get() > 0)
			return true;
		
		this.value = Math.max(this.value + amount, 0);
		
		if(this.value > this.max)
		{
			this.saturation.changeBy((this.value - this.max) / 2);
			this.value = this.max;
		}
		
		return true;
	}
	
	public boolean needWater()
	{
		return this.value < this.max;
	}
	
	@Override
	public void update()
	{
		int modifier = 1;
		
		if(this.value > 0.0f && this.wrapper.stamina.getPercent() < 1.0f)
			modifier += 5 * (1.0f - this.wrapper.stamina.getPercent());
		
		changeBy(-modifier);
	}
	
	@Override
	public void saveNBTData(NBTTagCompound compound)
	{
		super.saveNBTData(compound);
		this.saturation.saveNBTData(compound);
	}
	
	@Override
	public void loadNBTData(NBTTagCompound compound)
	{
		super.loadNBTData(compound);
		this.saturation.loadNBTData(compound);
	}
	
	@Override
	public ByteBuf write(ByteBuf buffer)
	{
		return this.saturation.write(super.write(buffer));
	}
	
	@Override
	public ByteBuf read(ByteBuf buffer)
	{
		return this.saturation.read(super.read(buffer));
	}
}
