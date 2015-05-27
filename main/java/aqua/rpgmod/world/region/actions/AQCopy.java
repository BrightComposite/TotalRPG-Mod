package aqua.rpgmod.world.region.actions;

import static aqua.rpgmod.service.AQChatManager.chatComponent;
import static aqua.rpgmod.service.AQChatManager.message;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import aqua.rpgmod.world.region.actions.AQRegionActionManager.RegionAction;
import aqua.rpgmod.world.schematics.AQSchematics;
import aqua.rpgmod.world.schematics.AQSchematicsWorldAdapter;

public class AQCopy extends RegionAction<AQCopy>
{
	public AQCopy(AQRegionActionManager manager)
	{
		super(manager);
	}
	
	public AQCopy(AQRegionActionManager manager, EntityPlayer player, List<String> params)
	{
		super(manager, player, params);
	}
	
	@Override
	public String getName()
	{
		return "copy";
	}
	
	@Override
	protected AQCopy clone(AQRegionActionManager manager, EntityPlayer player, List<String> params)
	{
		return new AQCopy(manager, player, params);
	}
	
	@Override
	protected boolean execute()
	{
		if(this.manager.selection == null)
		{
			message(this.player, chatComponent(EnumChatFormatting.RED, "region.noselect"));
			return false;
		}
		
		this.manager.buffer = new AQSchematics();
		this.manager.buffer.load(new AQSchematicsWorldAdapter(this.manager.selection));
		
		message(
			this.player,
			chatComponent(EnumChatFormatting.GREEN, "region.copied1"),
			new ChatComponentText(" "),
			new AQSave(this.manager).usage(),
			chatComponent(EnumChatFormatting.GREEN, "region.copied2"),
			new ChatComponentText(" "),
			new AQPaste(this.manager).usage());
		
		return true;
	}
}