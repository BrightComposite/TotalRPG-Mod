package aqua.rpgmod.character;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayerMP;
import aqua.rpgmod.service.config.AQConfiguration;
import aqua.rpgmod.service.config.AQProperty;
import aqua.rpgmod.service.resources.AQFileResource;

public class AQServerCharacterManager
{	
	public static AQFileResource profilesPath = new AQFileResource("profiles");
	
	public static class Profile extends AQConfiguration
	{
		public final EntityPlayerMP player;
		
		public Profile(EntityPlayerMP player)
		{
			super(profilesPath.append(player.getCommandSenderName()));
			this.player = player;
		}
	}
	
	public static void getProperty(EntityPlayerMP player, String property, AQProperty value)
	{
		try
		{
			Profile profile = new Profile(player);
			profile.load();
			profile.getProperty(property, value);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void setProperty(EntityPlayerMP player, String property, AQProperty value)
	{
		try
		{
			Profile profile = new Profile(player);
			profile.load();
			profile.setProperty(property, value);
			profile.save();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void deleteProperty(EntityPlayerMP player, String property)
	{
		try
		{
			Profile profile = new Profile(player);
			profile.load();
			profile.deleteProperty(property);
			profile.save();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
