package aqua.rpgmod.service.network;

import io.netty.buffer.ByteBuf;

public interface AQTranslator
{
	public ByteBuf write(ByteBuf buf);
}
