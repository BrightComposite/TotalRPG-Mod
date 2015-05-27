package aqua.rpgmod.world.region;

import io.netty.buffer.ByteBuf;
import aqua.rpgmod.AquaMod;
import aqua.rpgmod.geometry.AQPoint3D;
import aqua.rpgmod.player.AQPlayerWrapper;
import aqua.rpgmod.service.network.AQMessage;
import aqua.rpgmod.service.network.AQWorldTranslator;
import aqua.rpgmod.service.network.AQMessage.RegionMessage;
import aqua.rpgmod.service.network.AQTranslator;
import aqua.rpgmod.world.region.AQRegion.RegionType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class AQRegionSynchronizer
{
	AQPlayerWrapper<EntityPlayer> wrapper;
	AQRegionProvider provider;
	public Thread thread;
	
	public static float MAX_DISTANCE = 100;
	
	public AQRegionSynchronizer(AQPlayerWrapper<EntityPlayer> wrapper)
	{
		this.wrapper = wrapper;
		this.provider = AQRegionManager.getProviderForPlayer(wrapper.player);
	}
	
	public void start(boolean server)
	{
		this.thread = server ? new Writer() : new Reader();
		this.thread.start();
	}
	
	public Reader getReader()
	{
		return this.thread instanceof Reader ? (Reader) this.thread : null;
	}
	
	public class Reader extends Thread
	{
		ByteBuf data = null;
		
		public Reader()
		{
			setName("Region reader of " + AQRegionSynchronizer.this.wrapper.getPlayerName());
		}
		
		@Override
		public void run()
		{
			synchronized(this)
			{
				try
				{
					while(AQRegionSynchronizer.this.wrapper.player.isEntityAlive())
					{
						if(this.data != null)
						{
							AQRegion.readRegions(Reader.this.data, AQRegionSynchronizer.this.provider);
							
							AquaMod.regionNetwork.sendToServer(new AQMessage(RegionMessage.SYNC_RESPONSE, new AQWorldTranslator(
								AQRegionSynchronizer.this.provider.world), new AQPlayerWrapper.PlayerTranslator(AQRegionSynchronizer.this.wrapper.player)));
							
							this.data = null;
						}
						
						wait();
					}
				}
				catch(InterruptedException e)
				{
					e.printStackTrace();
				}
				
				AQRegionSynchronizer.this.wrapper = null;
			}
		}
	}
	
	class Writer extends Thread
	{
		public Writer()
		{
			setName("Region writer of " + AQRegionSynchronizer.this.wrapper.getPlayerName());
		}
		
		@Override
		public void run()
		{
			synchronized(this)
			{
				try
				{
					while(AQRegionSynchronizer.this.wrapper.player.isEntityAlive())
					{
						wait(2000);
						
						AquaMod.regionNetwork.sendTo(
							new AQMessage(RegionMessage.SYNC, new Translator(AQRegionSynchronizer.this)),
							(EntityPlayerMP) AQRegionSynchronizer.this.wrapper.player);
						
						wait();
					}
				}
				catch(InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	public static class Translator implements AQTranslator
	{
		final AQRegionSynchronizer sync;
		
		public Translator(AQRegionSynchronizer sync)
		{
			this.sync = sync;
		}
		
		public Translator(ByteBuf buf, AQRegionSynchronizer sync)
		{
			this.sync = sync;
			
			Reader reader = sync.getReader();
			
			if(reader != null)
			{
				synchronized(reader)
				{
					reader.data = buf;
					reader.notify();
				}
			}
		}
		
		@Override
		public ByteBuf write(ByteBuf buf)
		{
			AQRegion.Filter filter = new AQRegion.Filter(RegionType.ROOT)
			{
				@Override
				protected boolean filter(AQRegion region)
				{
					EntityPlayer player = Translator.this.sync.wrapper.player;
					AQPoint3D pos = new AQPoint3D(player.posX, player.posY, player.posZ);
					
					if(region.shape.contains(pos))
						return true;
					
					if(region.shape.approximateDistanceTo(pos) > MAX_DISTANCE)
						return false;
					
					if(region.shape.distanceTo(pos) > MAX_DISTANCE)
						return false;
					
					return true;
				}
			};
			
			return AQRegion.writeRegions(this.sync.provider, buf, filter);
		}
	}
}
