package aqua.rpgmod.world.schematics;

import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import aqua.rpgmod.AquaMod;
import aqua.rpgmod.block.AQBlocks;
import aqua.rpgmod.geometry.AQBox;
import aqua.rpgmod.geometry.AQDimensions;
import aqua.rpgmod.geometry.AQPoint3D;
import aqua.rpgmod.geometry.AQValue3D;
import aqua.rpgmod.service.network.AQBlockTranslator;
import aqua.rpgmod.service.network.AQCoordTranslator;
import aqua.rpgmod.service.network.AQMessage;
import aqua.rpgmod.service.network.AQMessage.ServiceMessage;
import aqua.rpgmod.service.network.AQTileTranslator;
import aqua.rpgmod.world.region.AQSelection;
import aqua.rpgmod.world.schematics.AQSchematics.AQBlockInfo;
import aqua.rpgmod.world.schematics.AQSchematics.AQTileInfo;

public class AQSchematicsWorldAdapter extends AQSchematicsBase implements AQSchematicsAdapter
{
	public static int tileEntitiesToSave = 0;
	public static AQSchematics savingSchematics = null;
	protected AQPoint3D pos;
	protected World world;
	
	public AQSchematicsWorldAdapter(World world, int x1, int y1, int z1, int x2, int y2, int z2)
	{
		this.world = world;
		this.pos = new AQPoint3D(Math.min(x1, x2), Math.min(y1, y2), Math.min(z1, z2));
		this.dim = new AQDimensions(Math.abs(x1 - x2) + 1, Math.abs(y1 - y2) + 1, Math.abs(z1 - z2) + 1);
	}
	
	public AQSchematicsWorldAdapter(World world, AQBox box)
	{
		this.world = world;
		this.pos = new AQPoint3D(box.pos());
		this.dim = new AQDimensions(box.dimensions());
	}
	
	public AQSchematicsWorldAdapter(AQSelection region)
	{
		this.world = region.provider.world;
		this.pos = new AQPoint3D(region.shape.pos());
		this.dim = new AQDimensions(region.shape.dimensions());
	}
	
	public AQSchematicsWorldAdapter(AQSchematics schx, World world, int x, int y, int z)
	{
		this.world = world;
		this.pos = new AQPoint3D(x, y, z);
		this.dim = new AQDimensions(schx.dim);
		schx.save(this);
	}
	
	public AQSchematicsWorldAdapter(AQSchematics schx, World world, AQPoint3D pos)
	{
		this(schx, world, pos.x, pos.y, pos.z);
	}
	
	@Override
	public AQBlockInfo createInfo(AQSchematics schx, int x, int y, int z)
	{
		AQValue3D pt = this.pos.plus(new AQValue3D(x, y, z));
		
		int meta = this.world.getBlockMetadata(pt.x, pt.y, pt.z);
		Block block = this.world.getBlock(pt.x, pt.y, pt.z);
		Block realBlock = Block.getBlockFromName(Block.blockRegistry.getNameForObject(block));
		
		AQBlockInfo info = new AQBlockInfo(realBlock, meta);
		TileEntity tile = this.world.getTileEntity(pt.x, pt.y, pt.z);
		
		if(tile == null)
			return info;
		
		if(this.world.isRemote)
		{
			++tileEntitiesToSave;
			savingSchematics = schx;
			
			AquaMod.serviceNetwork.sendToServer(new AQMessage(ServiceMessage.LOAD_TILE, new AQCoordTranslator(this.world, pt.x, pt.y, pt.z), new AQCoordTranslator(
				this.world,
				x,
				y,
				z)));
		}
		else
			schx.tileData.add(new AQTileInfo(tile, new AQPoint3D(pt)));
			
		return info;
	}
	
	@Override
	public void applyInfo(AQSchematics schx, AQBlockInfo info, int x, int y, int z)
	{
		if(info.block == AQBlocks.bad)
			return;
		
		AQValue3D pt = this.pos.plus(new AQValue3D(x, y, z));

		this.world.setBlock(pt.x, pt.y, pt.z, info.block, info.meta, 0x2);
		this.world.setBlockMetadataWithNotify(pt.x, pt.y, pt.z, info.meta, 0x2);
		
		if(this.world.isRemote)
			AquaMod.serviceNetwork.sendToServer(new AQMessage(ServiceMessage.SET_BLOCK, new AQBlockTranslator(this.world, info.block, pt.x, pt.y, pt.z, info.meta)));
	}
	
	@Override
	public void afterLoad(AQSchematics schx)
	{	
		
	}
	
	@Override
	public void afterSave(AQSchematics schx)
	{
		for(Iterator<AQTileInfo> i = schx.tileData.iterator(); i.hasNext();)
		{
			AQTileInfo tileInfo = i.next();
			TileEntity tile = tileInfo.tile;
			
			int x = this.pos.x + tileInfo.pos.x;
			int y = this.pos.y + tileInfo.pos.y;
			int z = this.pos.z + tileInfo.pos.z;
			
			if(this.world.isRemote)
				AquaMod.serviceNetwork.sendToServer(new AQMessage(ServiceMessage.SET_TILE, new AQCoordTranslator(this.world, x, y, z), new AQTileTranslator(tile)));
			else
				this.world.setTileEntity(x, y, z, tile);
		}
	}
}
