package aqua.rpgmod.service.network;

import aqua.rpgmod.player.AQThisPlayerWrapper;
import aqua.rpgmod.service.network.AQMessage.RegionMessage;
import aqua.rpgmod.world.region.AQRegionSynchronizer;

public class AQClientRegionMessenger extends AQMessenger<AQMessage>
{
	static class Sync implements AQMessageReader<AQMessage>
	{
		@Override
		public void read(AQMessage msg)
		{
			AQThisPlayerWrapper wrapper = AQThisPlayerWrapper.getWrapper();
			AQRegionSynchronizer sync = wrapper.regionSyncronizer;
			
			new AQRegionSynchronizer.Translator(msg.data, sync);
		}
	}
	
	public AQClientRegionMessenger()
	{
		put(RegionMessage.SYNC, new Sync());
	}
}
