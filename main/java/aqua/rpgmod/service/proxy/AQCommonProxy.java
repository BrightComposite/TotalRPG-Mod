package aqua.rpgmod.service.proxy;

import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import api.player.server.ServerPlayerAPI;
import aqua.rpgmod.AQModInfo;
import aqua.rpgmod.AquaMod;
import aqua.rpgmod.player.AQPlayerWrapper;
import aqua.rpgmod.player.AQServerPlayerBase;
import aqua.rpgmod.service.handlers.AQServerEventHandler;
import aqua.rpgmod.service.network.AQMessage;
import aqua.rpgmod.service.network.AQServerPlayerMessenger;
import aqua.rpgmod.service.network.AQServerRegionMessenger;
import aqua.rpgmod.service.network.AQServerServiceMessenger;

public class AQCommonProxy
{
	@SuppressWarnings("static-method")
	public boolean isDedicated()
	{
		return true;
	}
	
	@SuppressWarnings("static-method")
	public void preInit()
	{
		AquaMod.playerNetwork.registerMessage(AQServerPlayerMessenger.class, AQMessage.class, 0, Side.SERVER);
		AquaMod.serviceNetwork.registerMessage(AQServerServiceMessenger.class, AQMessage.class, 1, Side.SERVER);
		AquaMod.regionNetwork.registerMessage(AQServerRegionMessenger.class, AQMessage.class, 2, Side.SERVER);
		
		ServerPlayerAPI.register(AQModInfo.MODID, AQServerPlayerBase.class);
		MinecraftForge.EVENT_BUS.register(new AQServerEventHandler());
	}
	
	public void postInit()
	{}
	
	@SuppressWarnings("static-method")
	public AQPlayerWrapper createWrapper(EntityPlayer player)
	{
		AQPlayerWrapper wrapper = new AQPlayerWrapper(player);
		return wrapper;
	}
	
	@SuppressWarnings("static-method")
	public AQPlayerWrapper getWrapper(EntityPlayer player)
	{
		return (AQPlayerWrapper) player.getExtendedProperties(AQPlayerWrapper.propKey);
	}
	
	@SuppressWarnings(
	{
		"static-method", "unused"
	})
	public World getWorld(int dimension, boolean server)
	{
		return MinecraftServer.getServer().worldServerForDimension(dimension);
	}
	
	@SuppressWarnings(
	{
		"static-method", "unused"
	})
	public World getWorld(String name, boolean server)
	{
		if(MinecraftServer.getServer() != null)
		{
			for(WorldServer world : MinecraftServer.getServer().worldServers)
			{
				if(world.provider.getDimensionName().toLowerCase().equals(name.toLowerCase()))
					return world;
			}
		}
		
		return null;
	}
	
	@SuppressWarnings("static-method")
	public EntityPlayer getPlayer(MessageContext ctx)
	{
		return ctx.getServerHandler().playerEntity;
	}
}
