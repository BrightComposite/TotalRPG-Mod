package aqua.rpgmod.service.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import aqua.rpgmod.AquaMod;
import aqua.rpgmod.service.network.AQMessage.ServiceMessage;

public class AQServerServiceMessenger extends AQMessenger<AQMessage>
{
	static class SetBlock implements AQMessageReader<AQMessage>
	{
		@Override
		public void read(AQMessage msg)
		{
			AQBlockTranslator t = new AQBlockTranslator(msg.data, true);
			t.world.setBlock(t.x, t.y, t.z, t.block, t.meta, 0x1 | 0x2);
			t.world.setBlockMetadataWithNotify(t.x, t.y, t.z, t.meta, 0x2);
		}
	}
	
	static class SetTile implements AQMessageReader<AQMessage>
	{
		@Override
		public void read(AQMessage msg)
		{
			AQCoordTranslator coord = new AQCoordTranslator(msg.data, true);
			AQTileTranslator t = new AQTileTranslator(msg.data);
			
			TileEntity tile = t.readTile();
			coord.world.setTileEntity(coord.x, coord.y, coord.z, tile);
		}
	}
	
	static class UpdateTile implements AQMessageReader<AQMessage>
	{
		@Override
		public void read(AQMessage msg)
		{
			AQCoordTranslator coord = new AQCoordTranslator(msg.data, true);
			TileEntity tile = coord.world.getTileEntity(coord.x, coord.y, coord.z);
			
			AQTileTranslator t = new AQTileTranslator(msg.data);
			t.setTileNBT(tile);
		}
	}
	
	static class LoadTile implements AQMessageReader<AQMessage>
	{
		@Override
		public void read(AQMessage msg)
		{
			AQCoordTranslator t = new AQCoordTranslator(msg.data, true);
			AQCoordTranslator d = new AQCoordTranslator(msg.data, true);
			TileEntity tile = t.world.getTileEntity(t.x, t.y, t.z);
			
			AquaMod.serviceNetwork.sendTo(new AQMessage(ServiceMessage.SAVE_TILE, d, new AQTileTranslator(tile)), (EntityPlayerMP) msg.player);
		}
	}
	
	public AQServerServiceMessenger()
	{
		put(ServiceMessage.SET_BLOCK, new SetBlock());
		put(ServiceMessage.SET_TILE, new SetTile());
		put(ServiceMessage.UPDATE_TILE, new UpdateTile());
		put(ServiceMessage.LOAD_TILE, new LoadTile());
	}
}
