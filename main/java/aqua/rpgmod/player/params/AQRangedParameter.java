package aqua.rpgmod.player.params;

import io.netty.buffer.ByteBuf;

public class AQRangedParameter extends AQPlayerParameter
{
	protected float max = 20;
	
	public AQRangedParameter(float value, float max)
	{
		this.max = max;
		this.value = Math.max(Math.min(value, max), 0);
	}
	
	public boolean changeBy(float amount)
	{
		this.value = Math.max(Math.min(this.value + amount, this.max), 0);
		
		return true;
	}
	
	public void restore()
	{
		this.value = this.max;
	}
	
	public void exhaust()
	{
		this.value = 0;
	}
	
	public float getPercent()
	{
		return (this.max > 0) ? this.value / this.max : 1.0f;
	}
	
	@Override
	public void set(float value)
	{
		this.value = Math.max(Math.min(value, this.max), 0);
	}
	
	public void setMax(float max)
	{
		this.max = max;
	}
	
	@Override
	public ByteBuf write(ByteBuf buffer)
	{
		return super.write(buffer).writeFloat(this.max);
	}
	
	@Override
	public ByteBuf read(ByteBuf buffer)
	{
		super.read(buffer);
		this.max = buffer.readFloat();
		
		return buffer;
	}
}
