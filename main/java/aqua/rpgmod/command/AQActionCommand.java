package aqua.rpgmod.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import aqua.rpgmod.service.AQChatManager;
import aqua.rpgmod.service.actions.AQAction;
import aqua.rpgmod.service.actions.AQActionManager;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayerMP;

abstract public class AQActionCommand extends CommandBase
{
	public final AQActionManager manager;
	
	public AQActionCommand(AQActionManager manager)
	{
		this.manager = manager;
	}
	
	@Override
	public String getCommandName()
	{
		return this.manager.getName();
	}
	
	@Override
	public String getCommandUsage(ICommandSender sender)
	{
		return this.manager.getUsage().getFormattedText();
	}
	
	@Override
	public void processCommand(ICommandSender sender, String[] params)
	{
		if(params.length < 1)
		{
			AQChatManager.message(sender, this.manager.getUsage());
			return;
		}
		
		if(sender instanceof EntityPlayerMP == false)
			throw new PlayerNotFoundException("command.invalidsender");
		
		LinkedList<String> list = new LinkedList<String>(Arrays.asList(params));
		list.removeFirst();
		
		this.manager.execute(params[0], getCommandSenderAsPlayer(sender), list);
	}
	
	@Override
	public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_)
	{
		return p_71516_2_.length == 1 ? getListOfStringsFromIterableMatchingLastWord(p_71516_2_) : null;
	}
	
	public List getListOfStringsFromIterableMatchingLastWord(String[] p_71531_0_)
	{
		String s = p_71531_0_[p_71531_0_.length - 1];
		ArrayList arraylist = new ArrayList();
		
		for(Iterator<AQAction> i = this.manager.getActions().iterator(); i.hasNext();)
		{
			String s1 = i.next().getName();
			
			if(doesStringStartWith(s, s1))
			{
				arraylist.add(s1);
			}
		}
		
		return arraylist;
	}
	
}
