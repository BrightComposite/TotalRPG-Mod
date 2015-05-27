package aqua.rpgmod.service.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.world.World;

public class AQBlockTranslator extends AQCoordTranslator
{
	Block block;
	int meta;
	
	public AQBlockTranslator(ByteBuf buf, boolean server)
	{
		super(buf, server);
		
		this.block = Block.getBlockById(buf.readInt());
		this.meta = buf.readInt();
	}
	
	public AQBlockTranslator(World world, Block block, int x, int y, int z, int meta)
	{
		super(world, x, y, z);
		
		this.block = block;
		this.meta = meta;
	}
	
	@Override
	public ByteBuf write(ByteBuf buf)
	{
		super.write(buf);
		
		buf.writeInt(Block.getIdFromBlock(this.block));
		buf.writeInt(this.meta);
		
		return buf;
	}
}
