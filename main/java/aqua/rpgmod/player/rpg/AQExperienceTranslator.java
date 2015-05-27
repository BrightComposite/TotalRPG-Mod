package aqua.rpgmod.player.rpg;

import io.netty.buffer.ByteBuf;
import aqua.rpgmod.service.network.AQTranslator;

public class AQExperienceTranslator implements AQTranslator
{
	public final int value;
	public final boolean level;
	
	public AQExperienceTranslator(ByteBuf buf)
	{
		this.level = buf.readBoolean();
		this.value = buf.readInt();
	}

	public AQExperienceTranslator(boolean level, int value)
	{
		this.level = level;
		this.value = value;
	}
	
	@Override
	public ByteBuf write(ByteBuf buf)
	{
		buf.writeBoolean(this.level);
		buf.writeInt(this.value);
		return buf;
	}
}
