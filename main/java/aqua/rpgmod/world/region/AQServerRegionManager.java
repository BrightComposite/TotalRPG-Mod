package aqua.rpgmod.world.region;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import net.minecraft.world.World;

import org.apache.logging.log4j.Level;

import aqua.rpgmod.service.AQLogger;
import aqua.rpgmod.service.resources.AQFileResource;
import aqua.rpgmod.world.region.AQRegion.Filter;
import aqua.rpgmod.world.region.AQRegion.RegionType;

public class AQServerRegionManager extends AQRegionManager
{
	public static AQServerRegionManager instance = new AQServerRegionManager();
	HashMap<Integer, AQRegionProvider> providers = new HashMap<Integer, AQRegionProvider>();
	
	@Override
	public AQRegionProvider getProvider(World world)
	{
		Integer id = new Integer(world.provider.dimensionId);
		AQRegionProvider provider = this.providers.get(id);
		
		if(provider == null)
		{
			provider = new AQRegionProvider(world, this);
			this.providers.put(id, provider);
		}
		
		return provider;
	}
	
	@Override
	public AQRegionProvider getProvider(Integer id)
	{
		return this.providers.get(id);
	}
	
	public void loadRegions(World world)
	{
		String name = world.provider.getDimensionName();
		final AQFileResource resource = new AQFileResource("regions", name);
		final AQRegionProvider provider = getProvider(world);
		
		if(!resource.canRead())
		{
			AQLogger.log(Level.INFO, "Can't load regions!");
			return;
		}
		
		new Thread("Region loader of world " + name)
		{
			@Override
			public void run()
			{
				byte data[];
				
				try
				{
					ByteArrayOutputStream bytes = new ByteArrayOutputStream();
					InputStream input = resource.getInput();
					
					byte dataPart[] = new byte[64];
					
					while(true)
					{
						int readed = input.read(dataPart);
						bytes.write(dataPart, 0, readed);
						
						if(readed < 64)
							break;
					}
					
					data = bytes.toByteArray();
					
					input.close();
				}
				catch(IOException e)
				{
					e.printStackTrace();
					AQLogger.log(Level.INFO, "Can't load regions!");
					return;
				}
				
				ByteBuf buffer = Unpooled.buffer().writeBytes(data);
				AQRegion.readRegions(buffer, provider);
			}
		}.start();
	}
	
	public void saveRegions(World world)
	{
		final String name = world.provider.getDimensionName();
		final AQFileResource resource = new AQFileResource("regions", name + "-temp");
		final AQRegionProvider provider = getProvider(world);
		
		if(!resource.canWrite())
		{
			AQLogger.log(Level.INFO, "Can't save regions!");
			return;
		}
		
		new Thread("Region saver of world " + name)
		{
			@Override
			public void run()
			{
				ByteBuf buffer = AQRegion.writeRegions(provider, Unpooled.buffer(), new Filter(RegionType.ROOT, RegionType.SELECTION));
				
				try
				{
					File file = resource.getFile();
					
					if(file.exists())
						file.delete();
					
					OutputStream output = resource.getOutput();
					
					byte data[] = new byte[buffer.readableBytes()];
					buffer.readBytes(data);
					
					output.write(data);
					output.close();
					
					File newFile = new AQFileResource("regions", name).getFile();
					
					if(newFile.exists())
						newFile.delete();
					
					file.renameTo(newFile);
					
					// AQLogger.log(Level.INFO,
					// "Regions have been saved successfully");
				}
				catch(IOException e)
				{
					e.printStackTrace();
					AQLogger.log(Level.INFO, "Can't save regions!");
				}
			}
		}.start();
	}
}
