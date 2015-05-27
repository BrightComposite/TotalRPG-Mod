package aqua.rpgmod.service.network;

import aqua.rpgmod.geometry.AQPoint3D;
import io.netty.buffer.ByteBuf;
import net.minecraft.world.World;

public class AQCoordTranslator extends AQWorldTranslator
{
	int x, y, z;
	
	public AQCoordTranslator(ByteBuf buf, boolean server)
	{
		super(buf, server);
		
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
	}
	
	public AQCoordTranslator(World world, int x, int y, int z)
	{
		super(world);
		
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public ByteBuf write(ByteBuf buf)
	{
		super.write(buf);
		
		buf.writeInt(this.x);
		buf.writeInt(this.y);
		buf.writeInt(this.z);
		
		return buf;
	}
	
	public AQPoint3D getPos()
	{
		return new AQPoint3D(this.x, this.y, this.z);
	}
}
