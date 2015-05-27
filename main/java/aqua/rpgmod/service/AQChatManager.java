package aqua.rpgmod.service;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;

public class AQChatManager
{
	public static IChatComponent chatComponent(EnumChatFormatting formatting, String text, Object ... components)
	{
		return list(formatting, "", format(formatting, new ChatComponentTranslation(text, components)).getFormattedText().split("`"));
	}

	public static IChatComponent format(EnumChatFormatting formatting, IChatComponent component)
	{
		return component.setChatStyle(new ChatStyle().setColor(formatting));
	}
	
	public static IChatComponent unite(IChatComponent first, IChatComponent ... components)
	{
		IChatComponent comp = first;
		
		for(int i = 0; i < components.length; ++i)
			comp = comp.appendSibling(components[i]);
		
		return comp;
	}
	
	public static void message(ICommandSender sender, IChatComponent first, IChatComponent ... components)
	{
		String messages[] = unite(first, components).getFormattedText().split("`");
		
		for(String str : messages)
			sender.addChatMessage(new ChatComponentTranslation(str));
	}

	public static IChatComponent list(EnumChatFormatting formatting, String prefix, String ... lines)
	{
		if(lines.length == 0)
			return new ChatComponentText("");
		
		IChatComponent comp = format(formatting, new ChatComponentTranslation(prefix + lines[0]));

		for(int i = 1; i < lines.length; ++i)
		{
			comp = comp.appendSibling(new ChatComponentText("`"));
			comp = comp.appendSibling(format(formatting, new ChatComponentTranslation(prefix + lines[i])));
		}
		
		return comp;
	}
	
	public static interface PagePrinter
	{
		public void printBefore(ICommandSender sender, int page, int maxPages);
		
		public void printAfter(ICommandSender sender, int page, int maxPages);
		
		public void printItem(int index, ICommandSender sender, int page, int maxPages);
	}
	
	protected static int itemsOnPage = 6;
	
	public static void printByPage(ICommandSender sender, int page, int itemsCount, PagePrinter printer)
	{
		int realPage = page - 1;
		int maxPages = MathHelper.floor_float(((float) itemsCount - 1) / itemsOnPage + 1.0f);
		
		if(realPage < 0 || realPage >= maxPages)
		{
			AQChatManager.message(sender, AQChatManager.chatComponent(EnumChatFormatting.RED, "commands.generic.num.invalid", Integer.valueOf(page)));
			return;
		}
		
		int startIndex = realPage * itemsOnPage;
		int endIndex = Math.min(startIndex + itemsOnPage, itemsCount);
		
		printer.printBefore(sender, page, maxPages);
		
		for(int i = startIndex; i < endIndex; ++i)
		{
			printer.printItem(i, sender, page, maxPages);
		}
		
		printer.printAfter(sender, page, maxPages);
	}
}
