package aqua.rpgmod.service.actions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.logging.log4j.Level;

import aqua.rpgmod.service.AQLogger;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class AQPermissionManager
{
	public static AQPermissionManager instance = new AQPermissionManager();
	private Class bukkit;
	private Method getPlayer;
	private Method hasPermission;
	
	public AQPermissionManager()
	{
		try
		{
			this.bukkit = Class.forName("org.bukkit.Bukkit");
			this.getPlayer = this.bukkit.getMethod("getPlayer", new Class[]
			{
				String.class
			});
			this.hasPermission = Class.forName("org.bukkit.entity.Player").getMethod("hasPermission", new Class[]
			{
				String.class
			});
			
			AQLogger.log(Level.INFO, "Bukkit permissions enabled");
		}
		catch(ClassNotFoundException var2)
		{
			AQLogger.log(Level.WARN, "Bukkit permissions are not enabled");
		}
		catch(NoSuchMethodException var3)
		{
			var3.printStackTrace();
		}
		catch(SecurityException var4)
		{
			var4.printStackTrace();
		}
	}
	
	public static boolean commandsAllowed(EntityPlayer player)
	{
		return player instanceof EntityPlayerMP && ((EntityPlayerMP) player).mcServer.getConfigurationManager().func_152596_g(player.getGameProfile());
	}
	
	public static boolean hasPermission(EntityPlayer player, String permission)
	{
		return (instance.bukkit != null && instance.bukkitPermission(player.getCommandSenderName(), permission))
			|| (instance.bukkit == null && commandsAllowed(player));
	}
	
	private boolean bukkitPermission(String username, String permission)
	{
		try
		{
			Object e = this.getPlayer.invoke(null, username);
			return ((Boolean) this.hasPermission.invoke(e, permission)).booleanValue();
		}
		catch(IllegalAccessException var4)
		{
			var4.printStackTrace();
		}
		catch(IllegalArgumentException var5)
		{
			var5.printStackTrace();
		}
		catch(InvocationTargetException var6)
		{
			var6.printStackTrace();
		}
		
		return false;
	}
}
