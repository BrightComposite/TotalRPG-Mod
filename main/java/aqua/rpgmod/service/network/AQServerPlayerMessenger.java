package aqua.rpgmod.service.network;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import aqua.rpgmod.AquaMod;
import aqua.rpgmod.character.AQServerCharacterManager;
import aqua.rpgmod.player.AQPlayerWrapper;
import aqua.rpgmod.player.AQPlayerWrapper.PlayerTranslator;
import aqua.rpgmod.player.AQPlayerWrapper.SyncCause;
import aqua.rpgmod.player.rpg.AQExperienceTranslator;
import aqua.rpgmod.service.AQStringTranslator;
import aqua.rpgmod.service.config.AQMapProperty;
import aqua.rpgmod.service.network.AQMessage.PlayerMessage;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;

public class AQServerPlayerMessenger extends AQMessenger<AQMessage>
{
	static class Update implements AQMessageReader<AQMessage>
	{
		@Override
		public void read(AQMessage message)
		{
			if(message.player == null)
				return;
			
			EntityPlayerMP player = (EntityPlayerMP) message.player;
			AQPlayerWrapper wrapper = AquaMod.proxy.getWrapper(player);
			
			if(message.data == null)
			{
				wrapper.synchronize(SyncCause.SERVER_RESPONSE);
				return;
			}
			
			wrapper.new Translator(message.data);
			wrapper.update();
		}
	}
	
	static class Render implements AQMessageReader<AQMessage>
	{
		@Override
		public void read(AQMessage message)
		{
			if(message.player == null)
				return;
			
			EntityPlayerMP player = (EntityPlayerMP) message.player;
			
			double x = player.posX;
			double y = player.posY;
			double z = player.posZ;
			double range = 32;
			
			AQPlayerWrapper wrapper = AquaMod.proxy.getWrapper(player);
			
			AquaMod.playerNetwork.sendToAllAround(new AQMessage(
				PlayerMessage.RENDER,
				new AQPlayerWrapper.PlayerTranslator(message.player),
				wrapper.new RenderTranslator(message.data)), new TargetPoint(player.dimension, x, y, z, range));
		}
	}
	
	static class Interact implements AQMessageReader<AQMessage>
	{
		@Override
		public void read(AQMessage message)
		{
			if(message.player == null)
				return;
			
			int x = message.data.readInt();
			int y = message.data.readInt();
			int z = message.data.readInt();
			int side = message.data.readInt();
			float xh = message.data.readFloat();
			float yh = message.data.readFloat();
			float zh = message.data.readFloat();
			
			EntityPlayerMP player = (EntityPlayerMP) message.player;
			Block block = player.worldObj.getBlock(x, y, z);
			block.onBlockActivated(player.worldObj, x, y, z, player, side, xh, yh, zh);
		}
	}
	
	static class GetRace implements AQMessageReader<AQMessage>
	{
		@Override
		public void read(AQMessage message)
		{
			EntityPlayerMP player = (EntityPlayerMP) message.player;
			
			AQWorldTranslator wt = new AQWorldTranslator(message.data, true);
			PlayerTranslator pt = new PlayerTranslator(message.data, wt.world);
			
			if(pt.player == null)
				return;
			
			AQPlayerWrapper wrapper = AquaMod.proxy.getWrapper(pt.player);
			
			if(wrapper.getRace() == null)
				return;
			
			AquaMod.playerNetwork.sendTo(new AQMessage(PlayerMessage.SET_RACE, wt, pt, new AQStringTranslator(wrapper.getRace().name)), player);
		}
	}
	
	static class SetRace implements AQMessageReader<AQMessage>
	{
		@Override
		public void read(AQMessage message)
		{
			EntityPlayerMP player = (EntityPlayerMP) message.player;
			AQPlayerWrapper wrapper = AquaMod.proxy.getWrapper(player);
			
			wrapper.setRace(new AQStringTranslator(message.data).string);
		}
	}

	static class LoadAbilityGroup implements AQMessageReader<AQMessage>
	{
		@Override
		public void read(AQMessage message)
		{
			EntityPlayerMP player = (EntityPlayerMP) message.player;
			String group = new AQStringTranslator(message.data).string;
			AQMapProperty value = new AQMapProperty();
			AQServerCharacterManager.getProperty(player, group, value);

			AquaMod.playerNetwork.sendTo(new AQMessage(PlayerMessage.SET_ABILITY_GROUP, new AQStringTranslator(group), new AQStringTranslator(value.toString())), player);
		}
	}

	static class SetAbilityGroup implements AQMessageReader<AQMessage>
	{
		@Override
		public void read(AQMessage message)
		{
			EntityPlayerMP player = (EntityPlayerMP) message.player;
			String group = new AQStringTranslator(message.data).string;

			AQServerCharacterManager.setProperty(player, group, new AQMapProperty(new AQStringTranslator(message.data).string));
		}
	}

	static class SetSkillModifier implements AQMessageReader<AQMessage>
	{
		@Override
		public void read(AQMessage message)
		{
			AQPlayerWrapper wrapper = AquaMod.proxy.getWrapper(message.player);
			wrapper.new SkillModifierTranslator(message.data);
		}
	}

	static class ChangeExperience implements AQMessageReader<AQMessage>
	{
		@Override
		public void read(AQMessage message)
		{
			EntityPlayerMP player = (EntityPlayerMP) message.player;
			AQExperienceTranslator tr = new AQExperienceTranslator(message.data);
			
			if(tr.level)
				player.addExperienceLevel(tr.value);
			else
				player.addExperience(tr.value);
		}
	}

	public AQServerPlayerMessenger()
	{
		put(PlayerMessage.UPDATE, new Update());
		put(PlayerMessage.RENDER, new Render());
		put(PlayerMessage.INTERACT, new Interact());
		put(PlayerMessage.GET_RACE, new GetRace());
		put(PlayerMessage.SET_RACE, new SetRace());
		put(PlayerMessage.LOAD_ABILITY_GROUP, new LoadAbilityGroup());
		put(PlayerMessage.SET_ABILITY_GROUP, new SetAbilityGroup());
		put(PlayerMessage.SET_SKILL_MODIFIER, new SetSkillModifier());
		put(PlayerMessage.CHANGE_EXPERIENCE, new ChangeExperience());
	}
}
