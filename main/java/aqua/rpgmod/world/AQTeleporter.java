package aqua.rpgmod.world;

import org.apache.logging.log4j.Level;

import aqua.rpgmod.service.AQLogger;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class AQTeleporter extends Teleporter
{
	int[] coords;
	
	public static boolean teleportTo(EntityPlayer player, int dim, int coords[])
	{
		if(dim != player.dimension)
		{
			MinecraftServer.getServer().worldServerForDimension(player.dimension);
			WorldServer w = MinecraftServer.getServer().worldServerForDimension(dim);
			
			if(w == null)
			{
				AQLogger.log(Level.ERROR, "Can't teleport user to the dimension " + dim + ", it doesn't exits!");
				return false;
			}
			
			if(player instanceof EntityPlayerMP)
			{
				EntityPlayerMP mpPlayer = (EntityPlayerMP) player;
				mpPlayer.mcServer.getConfigurationManager().transferPlayerToDimension(
					mpPlayer,
					dim,
					new AQTeleporter(mpPlayer.mcServer.worldServerForDimension(mpPlayer.dimension), coords));
			}
		}
		else
		{
			player.setPosition(coords[0], coords[1], coords[2]);
			player.motionX = player.motionY = player.motionZ = 0.0D;
		}
		
		return true;
	}
	
	public AQTeleporter(WorldServer ws, int[] coords)
	{
		super(ws);
		
		this.coords = coords;
	}
	
	public static AQTeleporter get(WorldServer ws, int[] coords)
	{
		return new AQTeleporter(ws, coords);
	}
	
	@Override
	public void placeInPortal(Entity e, double d1, double d2, double d3, float f)
	{
		e.setPosition(this.coords[0], this.coords[1], this.coords[2]);
		e.motionX = e.motionY = e.motionZ = 0.0D;
	}
	
	@Override
	public void removeStalePortalLocations(long par1)
	{}
	
	@Override
	public boolean placeInExistingPortal(Entity par1Entity, double par2, double par4, double par6, float par8)
	{
		return false;
	}
}
