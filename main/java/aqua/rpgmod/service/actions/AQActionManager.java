package aqua.rpgmod.service.actions;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.command.CommandNotFoundException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import aqua.rpgmod.AquaMod;
import aqua.rpgmod.player.AQPlayerWrapper;
import aqua.rpgmod.service.AQChatManager;
import aqua.rpgmod.service.AQStringTranslator;
import aqua.rpgmod.service.network.AQMessage;
import aqua.rpgmod.service.network.AQMessage.ServiceMessage;
import aqua.rpgmod.service.network.AQTranslator;

abstract public class AQActionManager extends AQChatManager
{
	abstract public static class BasicAction<A extends BasicAction> extends AQAction<AQActionManager, A>
	{
		public BasicAction(AQActionManager manager)
		{
			super(manager);
		}
		
		public BasicAction(AQActionManager manager, EntityPlayer player, List<String> params)
		{
			super(manager, player, params);
		}
		
		@Override
		public boolean needPermission()
		{
			return false;
		}
		
		@Override
		public String getFullName()
		{
			return getName();
		}
	}
	
	public static class Help extends BasicAction<Help>
	{
		final String parameter;
		
		public Help(AQActionManager manager)
		{
			super(manager);
			this.parameter = "1";
		}
		
		public Help(AQActionManager manager, EntityPlayer player, List<String> params)
		{
			super(manager, player, params);
			
			if(this.params.size() == 0)
				this.parameter = "1";
			else
				this.parameter = this.params.get(0);
		}
		
		@Override
		public boolean execute()
		{
			if(isInteger(this.parameter))
				this.manager.printActions(this.player, Integer.valueOf(this.parameter).intValue());
			else
				this.manager.actionHelp(this.player, this.parameter);
			
			return true;
		}
		
		@Override
		public String getName()
		{
			return "help";
		}
		
		@Override
		public Help clone(AQActionManager manager, EntityPlayer player, List<String> params)
		{
			return new Help(manager, player, params);
		}
		
		@Override
		public boolean needPermission()
		{
			return false;
		}
	}
	
	public static class History extends BasicAction<History>
	{
		final int page;
		
		public History(AQActionManager manager)
		{
			super(manager);
			this.page = 1;
		}
		
		public History(AQActionManager manager, EntityPlayer player, List<String> params)
		{
			super(manager, player, params);
			
			if(this.params.size() == 0 || !isInteger(this.params.get(0)))
				this.page = 1;
			else
				this.page = getInt(0, 1);
		}
		
		@Override
		public boolean execute()
		{
			this.manager.printHistory(this.player, this.page);
			
			return true;
		}
		
		@Override
		public String getName()
		{
			return "history";
		}
		
		@Override
		public History clone(AQActionManager manager, EntityPlayer player, List<String> params)
		{
			return new History(manager, player, params);
		}
		
		@Override
		public boolean needPermission()
		{
			return false;
		}
	}
	
	public static class Undo extends BasicAction<Undo>
	{
		public Undo(AQActionManager manager)
		{
			super(manager);
		}
		
		public Undo(AQActionManager manager, EntityPlayer player, List<String> params)
		{
			super(manager, player, params);
		}
		
		@Override
		public String getName()
		{
			return "undo";
		}
		
		@Override
		public boolean needPermission()
		{
			return true;
		}
		
		@Override
		protected Undo clone(AQActionManager manager, EntityPlayer player, List<String> params)
		{
			return new Undo(manager, player, params);
		}
		
		@Override
		protected boolean execute()
		{
			boolean result = this.manager.undo();
			
			if(result)
				message(this.player, chatComponent(EnumChatFormatting.GREEN, "action.undo"));
			
			return result;
		}
	}
	
	public static class Redo extends BasicAction<Redo>
	{
		public Redo(AQActionManager manager)
		{
			super(manager);
		}
		
		public Redo(AQActionManager manager, EntityPlayer player, List<String> params)
		{
			super(manager, player, params);
		}
		
		@Override
		public String getName()
		{
			return "redo";
		}
		
		@Override
		public boolean needPermission()
		{
			return true;
		}
		
		@Override
		protected Redo clone(AQActionManager manager, EntityPlayer player, List<String> params)
		{
			return new Redo(manager, player, params);
		}
		
		@Override
		protected boolean execute()
		{
			boolean result = this.manager.redo();
			
			if(result)
				message(this.player, chatComponent(EnumChatFormatting.GREEN, "action.redo"));
			
			return result;
		}
	}
	
	public static boolean isInteger(String value)
	{
		try
		{
			Integer.parseInt(value);
			return true;
		}
		catch(NumberFormatException ex)
		{
			return false;
		}
	}
	
	protected LinkedList<AQAction> undo = new LinkedList<AQAction>();
	protected LinkedList<AQAction> redo = new LinkedList<AQAction>();
	
	public final HashMap<String, AQAction> actionMap = new HashMap<String, AQAction>();
	public final List<AQAction> actions = new ArrayList<AQAction>();
	
	public static int MAX_UNDO = 32;
	
	public AQActionManager()
	{
		addAction(new Help(this));
		addAction(new History(this));
		addAction(new Undo(this));
		addAction(new Redo(this));
	}
	
	abstract public String getName();
	
	public AQAction getAction(String name)
	{
		return this.actionMap.get(name);
	}
	
	public List<AQAction> getActions()
	{
		return this.actions;
	}
	
	public void addAction(AQAction action)
	{
		this.actions.add(action);
		this.actionMap.put(action.getName(), action);
	}
	
	public IChatComponent getUsage()
	{
		return unite(
			chatComponent(EnumChatFormatting.GREEN, "command.usage"),
			chatComponent(EnumChatFormatting.GREEN, "`"),
			chatComponent(EnumChatFormatting.GOLD, "command." + getName() + ".usage"));
	}
	
	public boolean execute(String name, EntityPlayer player, String ... params)
	{
		return this.execute(name, player, Arrays.asList(params));
	}
	
	public boolean execute(String name, EntityPlayer player, List<String> params)
	{
		AQAction action = getAction(name);
		
		if(action == null)
			throw new CommandNotFoundException();
		
		if(AQPlayerWrapper.cantBeThis(player))
		{
			if(action.needPermission() && !AQPermissionManager.hasPermission(player, action.getPermission()))
			{
				message(player, chatComponent(EnumChatFormatting.RED, "commands.generic.permission"));
				return false;
			}
			
			sendToPlayer(player, action.getName(), params);
			return true;
		}
		
		return execute(action.clone(this, player, params));
	}
	
	public boolean execute(AQAction action)
	{
		if(action.execute())
		{
			if(action.isUndoable())
				addUndo(action);
			
			return true;
		}
		
		return false;
	}
	
	public void actionHelp(ICommandSender sender, String actionName)
	{
		AQAction action = getAction(actionName);
		
		if(action == null)
		{
			message(
				sender,
				chatComponent(
					EnumChatFormatting.RED,
					"action.unknown",
					chatComponent(EnumChatFormatting.GOLD, "command.help.usage", getName()).getFormattedText()));
			
			return;
		}
		
		message(
			sender,
			chatComponent(EnumChatFormatting.GREEN, action + " - "),
			chatComponent(EnumChatFormatting.GREEN, "command." + action.getFullName() + ".desc"),
			new ChatComponentText("."));
		
		message(
			sender,
			chatComponent(EnumChatFormatting.WHITE, "command.usage"),
			new ChatComponentText(" "),
			chatComponent(EnumChatFormatting.GOLD, "command." + action.getFullName() + ".usage", getName()));
	}
	
	class HistoryPrinter implements PagePrinter
	{
		@Override
		public void printBefore(ICommandSender sender, int page, int maxPages)
		{
			message(
				sender,
				chatComponent(EnumChatFormatting.DARK_GREEN, "=== "),
				chatComponent(EnumChatFormatting.DARK_GREEN, "command.history"),
				chatComponent(EnumChatFormatting.DARK_GREEN, " ("),
				chatComponent(EnumChatFormatting.GREEN, "command.page"),
				chatComponent(EnumChatFormatting.GREEN, " [%1$s/%2$s]", Integer.valueOf(page), Integer.valueOf(maxPages)),
				chatComponent(EnumChatFormatting.DARK_GREEN, ", "),
				chatComponent(EnumChatFormatting.GOLD, "command.history.usage", getName()),
				chatComponent(EnumChatFormatting.DARK_GREEN, ") ==="));
		}
		
		@Override
		public void printAfter(ICommandSender sender, int page, int maxPages)
		{}
		
		@Override
		public void printItem(int index, ICommandSender sender, int page, int maxPages)
		{
			AQAction action = AQActionManager.this.undo.get(index);
			
			StringBuilder params = new StringBuilder("");
			
			if(action.params.size() > 0)
			{
				params.append(" (");
				
				for(int k = 0; k < action.params.size(); ++k)
				{
					if(k > 0)
						params.append(" ");
					
					params.append(action.params.get(k));
				}
				
				params.append(")");
			}
			
			message(
				sender,
				chatComponent(EnumChatFormatting.DARK_GREEN, "* "),
				chatComponent(EnumChatFormatting.GREEN, action.getName()),
				chatComponent(EnumChatFormatting.DARK_GREEN, params.toString()));
		}
	}
	
	class ActionsPrinter implements PagePrinter
	{
		List<AQAction> list;
		
		public ActionsPrinter(List<AQAction> list)
		{
			this.list = list;
		}
		
		@Override
		public void printBefore(ICommandSender sender, int page, int maxPages)
		{
			message(
				sender,
				chatComponent(EnumChatFormatting.DARK_GREEN, "=== "),
				chatComponent(EnumChatFormatting.DARK_GREEN, "command.help"),
				chatComponent(EnumChatFormatting.DARK_GREEN, " ("),
				chatComponent(EnumChatFormatting.GREEN, "command.page"),
				chatComponent(EnumChatFormatting.GREEN, " [%1$s/%2$s]", Integer.valueOf(page), Integer.valueOf(maxPages)),
				chatComponent(EnumChatFormatting.DARK_GREEN, ", "),
				chatComponent(EnumChatFormatting.GOLD, "command.help.usage.page", getName()),
				chatComponent(EnumChatFormatting.DARK_GREEN, ") ==="));
		}
		
		@Override
		public void printAfter(ICommandSender sender, int page, int maxPages)
		{
			message(
				sender,
				chatComponent(EnumChatFormatting.DARK_GREEN, "command.help.action"),
				chatComponent(EnumChatFormatting.DARK_GREEN, " "),
				chatComponent(EnumChatFormatting.GOLD, "command.help.usage.action", getName()));
		}
		
		@Override
		public void printItem(int index, ICommandSender sender, int page, int maxPages)
		{
			AQAction action = this.list.get(index);
			message(sender, AQChatManager.chatComponent(EnumChatFormatting.DARK_GREEN, "* "), chatComponent(EnumChatFormatting.GREEN, action.getName()));
		}
	}
	
	public void printHistory(ICommandSender sender, int page)
	{
		if(this.undo.size() == 0)
		{
			message(sender, chatComponent(EnumChatFormatting.DARK_GREEN, "command.history.empty"));
			return;
		}
		
		printByPage(sender, page, this.undo.size(), new HistoryPrinter());
	}
	
	public void printActions(ICommandSender sender, int page)
	{
		List<AQAction> list = getActions();
		printByPage(sender, page, list.size(), new ActionsPrinter(list));
	}
	
	private void addUndo(AQAction action)
	{
		this.undo.push(action);
		
		if(this.undo.size() >= MAX_UNDO)
			this.undo.removeLast();
		
		this.redo.clear();
	}
	
	public boolean undo()
	{
		if(this.undo.isEmpty())
			return false;
		
		AQAction action = this.undo.pop();
		
		if(action.undo())
		{
			this.redo.push(action);
			return true;
		}
		
		return false;
	}
	
	public boolean redo()
	{
		if(this.redo.isEmpty())
			return false;
		
		AQAction action = this.redo.pop();
		
		if(action.execute())
		{
			this.undo.push(action);
			return true;
		}
		
		return false;
	}
	
	public void sendToPlayer(EntityPlayer player, final String actionName, final List<String> params)
	{
		AQTranslator trans = new AQTranslator()
		{
			@Override
			public ByteBuf write(ByteBuf buf)
			{
				AQStringTranslator.serialize(buf, AQActionManager.this.getName());
				AQStringTranslator.serialize(buf, actionName);
				
				buf.writeInt(params.size());
				
				for(Iterator<String> i = params.iterator(); i.hasNext();)
				{
					AQStringTranslator.serialize(buf, i.next());
				}
				
				return buf;
			}
		};
		
		AquaMod.serviceNetwork.sendTo(new AQMessage(ServiceMessage.ACTION, trans), (EntityPlayerMP) player);
	}
}
