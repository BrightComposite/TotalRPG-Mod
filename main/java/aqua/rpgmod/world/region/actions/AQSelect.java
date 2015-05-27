package aqua.rpgmod.world.region.actions;

import static aqua.rpgmod.service.AQChatManager.chatComponent;
import static aqua.rpgmod.service.AQChatManager.message;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import aqua.rpgmod.world.region.actions.AQRegionActionManager.RegionAction;

public class AQSelect extends RegionAction<AQSelect>
{
	public AQSelect(AQRegionActionManager manager)
	{
		super(manager);
	}
	
	public AQSelect(AQRegionActionManager manager, EntityPlayer player, List<String> params)
	{
		super(manager, player, params);
	}
	
	@Override
	public String getName()
	{
		return "select";
	}
	
	@Override
	protected AQSelect clone(AQRegionActionManager manager, EntityPlayer player, List<String> params)
	{
		return new AQSelect(manager, player, params);
	}
	
	@Override
	protected boolean execute()
	{
		if(this.manager.buffer != null || this.manager.selection != null)
			return false;
		
		if(this.manager.points[0] == null || this.manager.points[1] == null)
		{
			message(this.player, chatComponent(EnumChatFormatting.RED, "region.nopoint"));
			return false;
		}
		
		this.manager.createSelection();
		message(this.player, chatComponent(EnumChatFormatting.GREEN, "region.select"));
		
		return true;
	}
	
	@Override
	protected boolean isUndoable()
	{
		return true;
	}
	
	@Override
	protected boolean undo()
	{
		this.manager.freeSelection();
		this.manager.buffer = null;
		
		return true;
	}
}