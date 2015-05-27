package aqua.rpgmod.player.params;

import io.netty.buffer.ByteBuf;

public class AQPlayerParameter implements AQReadWritable
{
	protected float value;
	
	public AQPlayerParameter()
	{
		this.value = 1;
	}
	
	public AQPlayerParameter(int value)
	{
		this.value = value;
	}
	
	public float get()
	{
		return this.value;
	}
	
	public void set(float value)
	{
		this.value = value;
	}
	
	@Override
	public ByteBuf write(ByteBuf buffer)
	{
		return buffer.writeFloat(this.value);
	}
	
	@Override
	public ByteBuf read(ByteBuf buffer)
	{
		this.value = buffer.readFloat();
		return buffer;
	}
}
