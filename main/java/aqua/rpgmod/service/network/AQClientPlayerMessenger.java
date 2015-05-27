package aqua.rpgmod.service.network;

import net.minecraft.world.World;
import aqua.rpgmod.AquaMod;
import aqua.rpgmod.player.AQPlayerWrapper;
import aqua.rpgmod.player.AQPlayerWrapper.SyncCause;
import aqua.rpgmod.player.rpg.AQAbilityGroup;
import aqua.rpgmod.service.AQStringTranslator;
import aqua.rpgmod.service.config.AQMapProperty;
import aqua.rpgmod.service.network.AQMessage.PlayerMessage;

public class AQClientPlayerMessenger extends AQMessenger<AQMessage>
{
	static class Update implements AQMessageReader<AQMessage>
	{
		@Override
		public void read(AQMessage message)
		{
			AQPlayerWrapper wrapper = AquaMod.proxy.getWrapper(message.player);
			
			if(message.data == null)
			{
				wrapper.synchronize(SyncCause.CLIENT_RESPONSE);
				return;
			}
			
			wrapper.new Translator(message.data);
		}
	}
	
	static class Death implements AQMessageReader<AQMessage>
	{
		@Override
		public void read(AQMessage message)
		{
			AQPlayerWrapper wrapper = AquaMod.proxy.getWrapper(message.player);
			wrapper.reset();
		}
	}
	
	static class Render implements AQMessageReader<AQMessage>
	{
		@Override
		public void read(AQMessage message)
		{
			AQPlayerWrapper.PlayerTranslator trans = new AQPlayerWrapper.PlayerTranslator(message.data, message.player.worldObj);
			
			if(trans.player == message.player)
				return;
			
			AQPlayerWrapper wrapper = AquaMod.proxy.getWrapper(trans.player);
			wrapper.new RenderTranslator(message.data);
		}
	}
	
	static class GetRace implements AQMessageReader<AQMessage>
	{
		@Override
		public void read(AQMessage message)
		{
			AQPlayerWrapper wrapper = AquaMod.proxy.getWrapper(message.player);
			String raceName = wrapper.getRace() == null ? "" : wrapper.getRace().name;
			
			AquaMod.playerNetwork.sendToServer(new AQMessage(PlayerMessage.SET_RACE, new AQStringTranslator(raceName)));
		}
	}
	
	static class SetRace implements AQMessageReader<AQMessage>
	{
		@Override
		public void read(AQMessage message)
		{
			World world = new AQWorldTranslator(message.data, false).world;
			
			if(world == null)
				return;
			
			AQPlayerWrapper wrapper = AquaMod.proxy.getWrapper(new AQPlayerWrapper.PlayerTranslator(message.data, world).player);
			
			wrapper.setRace(new AQStringTranslator(message.data).string);
		}
	}

	static class SetAbilityGroup implements AQMessageReader<AQMessage>
	{
		@Override
		public void read(AQMessage message)
		{
			AQPlayerWrapper wrapper = AquaMod.proxy.getWrapper(message.player);

			if(!wrapper.isThis())
				return;
			
			AQAbilityGroup group = AQAbilityGroup.map.get(new AQStringTranslator(message.data).string);
			
			if(group != null)
				group.read(new AQMapProperty(new AQStringTranslator(message.data).string));
		}
	}
	
	public AQClientPlayerMessenger()
	{
		put(PlayerMessage.UPDATE, new Update());
		put(PlayerMessage.DEATH, new Death());
		put(PlayerMessage.RENDER, new Render());
		put(PlayerMessage.GET_RACE, new GetRace());
		put(PlayerMessage.SET_RACE, new SetRace());
		put(PlayerMessage.SET_ABILITY_GROUP, new SetAbilityGroup());
	}
}
