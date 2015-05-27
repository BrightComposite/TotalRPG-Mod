package aqua.rpgmod.world.region.actions;

import static aqua.rpgmod.service.AQChatManager.chatComponent;
import static aqua.rpgmod.service.AQChatManager.message;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import aqua.rpgmod.world.region.actions.AQRegionActionManager.RegionAction;

public class AQDeselect extends RegionAction<AQDeselect>
{
	public AQDeselect(AQRegionActionManager manager)
	{
		super(manager);
	}
	
	public AQDeselect(AQRegionActionManager manager, EntityPlayer player, List<String> params)
	{
		super(manager, player, params);
	}
	
	@Override
	public String getName()
	{
		return "deselect";
	}
	
	@Override
	protected AQDeselect clone(AQRegionActionManager manager, EntityPlayer player, List<String> params)
	{
		return new AQDeselect(manager, player, params);
	}
	
	@Override
	protected boolean execute()
	{
		if(this.manager.selection == null)
			return false;
		
		this.manager.freeSelection();
		this.manager.buffer = null;
		
		message(this.player, chatComponent(EnumChatFormatting.GREEN, "region.deselect"));
		return true;
	}
}
