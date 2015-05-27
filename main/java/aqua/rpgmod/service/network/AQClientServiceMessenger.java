package aqua.rpgmod.service.network;

import aqua.rpgmod.command.AQActionCommand;
import aqua.rpgmod.command.AQCommandManager;
import aqua.rpgmod.service.AQStringTranslator;
import aqua.rpgmod.service.actions.AQAction;
import aqua.rpgmod.service.network.AQMessage.ServiceMessage;
import aqua.rpgmod.world.schematics.AQSchematicsWorldAdapter;

public class AQClientServiceMessenger extends AQMessenger<AQMessage>
{
	static class Action implements AQMessageReader<AQMessage>
	{
		@Override
		public void read(AQMessage message)
		{
			AQActionCommand command = AQCommandManager.get(AQStringTranslator.deserialize(message.data));
			
			if(command == null)
				return;
			
			AQAction action = command.manager.getAction(AQStringTranslator.deserialize(message.data));
			
			if(action == null)
				return;
			
			command.manager.execute(action.read(command.manager, message.player, message.data));
		}
	}
	
	static class SetBlock implements AQMessageReader<AQMessage>
	{
		@Override
		public void read(AQMessage msg)
		{
			AQBlockTranslator t = new AQBlockTranslator(msg.data, false);
			t.world.setBlock(t.x, t.y, t.z, t.block, t.meta, 0x1 | 0x2);
		}
	}
	
	static class SaveTile implements AQMessageReader<AQMessage>
	{
		@Override
		public void read(AQMessage msg)
		{
			AQCoordTranslator coord = new AQCoordTranslator(msg.data, false);
			AQTileTranslator t = new AQTileTranslator(msg.data);
			
			AQSchematicsWorldAdapter.savingSchematics.addTileInfo(t.readTile(), coord.getPos());
			
			if(--AQSchematicsWorldAdapter.tileEntitiesToSave == 0)
				AQSchematicsWorldAdapter.savingSchematics = null;
		}
	}
	
	public AQClientServiceMessenger()
	{
		put(ServiceMessage.ACTION, new Action());
		put(ServiceMessage.SET_BLOCK, new SetBlock());
		put(ServiceMessage.SAVE_TILE, new SaveTile());
	}
}
