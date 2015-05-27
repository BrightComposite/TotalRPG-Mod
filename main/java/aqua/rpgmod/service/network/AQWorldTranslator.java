package aqua.rpgmod.service.network;

import aqua.rpgmod.AquaMod;
import io.netty.buffer.ByteBuf;
import net.minecraft.world.World;

public class AQWorldTranslator implements AQTranslator
{
	World world;
	
	public AQWorldTranslator(ByteBuf buf, boolean server)
	{
		this.world = AquaMod.proxy.getWorld(buf.readInt(), server);
	}
	
	public AQWorldTranslator(World world)
	{
		this.world = world;
	}
	
	@Override
	public ByteBuf write(ByteBuf buf)
	{
		buf.writeInt(this.world.provider.dimensionId);
		
		return buf;
	}
}
