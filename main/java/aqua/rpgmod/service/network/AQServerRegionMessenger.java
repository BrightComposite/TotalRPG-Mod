package aqua.rpgmod.service.network;

import java.util.Iterator;

import net.minecraft.entity.player.EntityPlayer;
import aqua.rpgmod.AquaMod;
import aqua.rpgmod.player.AQPlayerWrapper;
import aqua.rpgmod.player.AQPlayerWrapper.PlayerTranslator;
import aqua.rpgmod.service.AQStringTranslator;
import aqua.rpgmod.service.network.AQMessage.RegionMessage;
import aqua.rpgmod.world.region.AQRegion;
import aqua.rpgmod.world.region.AQRegionManager;
import aqua.rpgmod.world.region.AQRegionProvider;
import aqua.rpgmod.world.region.AQRegionSynchronizer;

public class AQServerRegionMessenger extends AQMessenger<AQMessage>
{
	static class Add implements AQMessageReader<AQMessage>
	{
		@Override
		public void read(AQMessage msg)
		{
			final AQRegionProvider provider = AQRegionManager.getProviderForPlayer(msg.player);
			final AQMessage message = msg;
			
			AQRegion.deserialiaze(message.data, provider);
			
			for(Iterator i = provider.world.playerEntities.iterator(); i.hasNext();)
			{
				AQPlayerWrapper wrapper = AquaMod.proxy.getWrapper((EntityPlayer) i.next());
				
				synchronized(wrapper.regionSyncronizer.thread)
				{
					wrapper.regionSyncronizer.thread.notify();
				}
			}
		}
	}
	
	static class Remove implements AQMessageReader<AQMessage>
	{
		@Override
		public void read(AQMessage msg)
		{
			AQRegionProvider provider = AQRegionManager.getProviderForPlayer(msg.player);
			AQRegion region = provider.findRegion(AQStringTranslator.deserialize(msg.data));
			
			if(region == null)
				return;
			
			region.free();
			
			for(Iterator i = provider.world.playerEntities.iterator(); i.hasNext();)
			{
				AQPlayerWrapper wrapper = AquaMod.proxy.getWrapper((EntityPlayer) i.next());
				
				synchronized(wrapper.regionSyncronizer.thread)
				{
					wrapper.regionSyncronizer.thread.notify();
				}
			}
		}
	}

	static class SyncResponse implements AQMessageReader<AQMessage>
	{
		@Override
		public void read(AQMessage msg)
		{
			AQWorldTranslator wt = new AQWorldTranslator(msg.data, true);
			PlayerTranslator pt = new PlayerTranslator(msg.data, wt.world);
			
			if(pt.player == null)
				return;
			
			AQPlayerWrapper wrapper = AquaMod.proxy.getWrapper(pt.player);
			AQRegionSynchronizer sync = wrapper.regionSyncronizer;
			
			synchronized(sync.thread)
			{
				sync.thread.notify();
			}
		}
	}
	
	public AQServerRegionMessenger()
	{
		put(RegionMessage.ADD, new Add());
		put(RegionMessage.REMOVE, new Remove());
		put(RegionMessage.SYNC_RESPONSE, new SyncResponse());
	}
}
