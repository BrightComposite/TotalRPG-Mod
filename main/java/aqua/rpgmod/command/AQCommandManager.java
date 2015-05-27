package aqua.rpgmod.command;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;

public class AQCommandManager
{
	protected static final HashMap<String, AQActionCommand> commands = new HashMap<String, AQActionCommand>();
	
	public static void register(AQActionCommand command)
	{
		commands.put(command.getCommandName(), command);
	}
	
	public static void registerOnServer(MinecraftServer server)
	{
		ServerCommandManager manager = (ServerCommandManager) server.getCommandManager();
		
		for(Iterator<Entry<String, AQActionCommand>> i = commands.entrySet().iterator(); i.hasNext();)
			manager.registerCommand(i.next().getValue());
	}
	
	public static AQActionCommand get(String name)
	{
		return commands.get(name);
	}
}
