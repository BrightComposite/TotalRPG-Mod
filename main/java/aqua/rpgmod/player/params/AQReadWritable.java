package aqua.rpgmod.player.params;

import io.netty.buffer.ByteBuf;

public interface AQReadWritable
{
	public ByteBuf write(ByteBuf buffer);
	
	public ByteBuf read(ByteBuf buffer);
}
