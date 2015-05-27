package aqua.rpgmod.world.region.actions;

import java.util.Arrays;
import java.util.List;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import aqua.rpgmod.service.AQChatManager.PagePrinter;
import aqua.rpgmod.world.region.AQRegion;
import aqua.rpgmod.world.region.AQRegionManager;
import aqua.rpgmod.world.region.AQRegionProvider;
import aqua.rpgmod.world.region.actions.AQRegionActionManager.RegionAction;

public class AQList extends RegionAction<AQList>
{
	final int page;
	
	public AQList(AQRegionActionManager manager)
	{
		super(manager);
		this.page = 1;
	}
	
	public AQList(AQRegionActionManager manager, EntityPlayer player, List<String> params)
	{
		super(manager, player, params);
		
		if(this.params.size() == 0 || !AQRegionActionManager.isInteger(this.params.get(0)))
			this.page = 1;
		else
			this.page = getInt(0, 1);
	}
	
	@Override
	public String getName()
	{
		return "list";
	}
	
	@Override
	protected AQList clone(AQRegionActionManager manager, EntityPlayer player, List<String> params)
	{
		return new AQList(manager, player, params);
	}
	
	@Override
	protected boolean execute()
	{
		final AQRegionProvider provider = AQRegionManager.getProviderForPlayer(this.player);
		final AQRegion[] regions = provider.getRegions();
		
		if(regions.length == 0)
		{
			AQRegionActionManager.message(this.player, AQRegionActionManager.chatComponent(EnumChatFormatting.DARK_GREEN, "command.region.list.empty"));
			return true;
		}
		
		Arrays.sort(regions);
		
		PagePrinter printer = new PagePrinter()
		{
			@Override
			public void printItem(int index, ICommandSender sender, int page, int maxPages)
			{
				AQRegionActionManager.message(sender, AQRegionActionManager.chatComponent(EnumChatFormatting.GREEN, "- " + regions[index].name));
			}
			
			@Override
			public void printBefore(ICommandSender sender, int page, int maxPages)
			{
				AQRegionActionManager.message(
					sender,
					AQRegionActionManager.chatComponent(EnumChatFormatting.DARK_GREEN, "=== "),
					AQRegionActionManager.chatComponent(EnumChatFormatting.DARK_GREEN, "command.region.list"),
					AQRegionActionManager.chatComponent(EnumChatFormatting.DARK_GREEN, " ("),
					AQRegionActionManager.chatComponent(EnumChatFormatting.GREEN, "command.page"),
					AQRegionActionManager.chatComponent(EnumChatFormatting.GREEN, " [%1$s/%2$s]", Integer.valueOf(page), Integer.valueOf(maxPages)),
					AQRegionActionManager.chatComponent(EnumChatFormatting.DARK_GREEN, ", "),
					AQRegionActionManager.chatComponent(EnumChatFormatting.GOLD, "command.region.list.usage"),
					AQRegionActionManager.chatComponent(EnumChatFormatting.DARK_GREEN, ") ==="));
			}
			
			@Override
			public void printAfter(ICommandSender sender, int page, int maxPages)
			{}
		};
		
		AQRegionActionManager.printByPage(this.player, this.page, regions.length, printer);
		
		return true;
	}
}