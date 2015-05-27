package aqua.rpgmod.world.region.actions;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import aqua.rpgmod.world.region.AQProtectionRegion;
import aqua.rpgmod.world.region.AQRegion;
import aqua.rpgmod.world.region.AQRegionManager;
import aqua.rpgmod.world.region.AQRegionProvider;
import aqua.rpgmod.world.region.AQSelection;
import aqua.rpgmod.world.region.actions.AQRegionActionManager.RegionAction;

public class AQProtect extends RegionAction<AQProtect>
{
	final String regionName;
	String parentName;
	
	public AQProtect(AQRegionActionManager manager)
	{
		super(manager);
		
		this.regionName = null;
	}
	
	public AQProtect(AQRegionActionManager manager, EntityPlayer player, List<String> params)
	{
		super(manager, player, params);
		
		if(this.params.size() < 1)
		{
			incorrectUsage();
			this.regionName = null;
			params.add("null");
			return;
		}
		
		this.regionName = params.get(0);
		
		if(params.size() > 1)
			this.parentName = params.get(1);
	}
	
	@Override
	public String getName()
	{
		return "protect";
	}
	
	@Override
	protected AQProtect clone(AQRegionActionManager manager, EntityPlayer player, List<String> params)
	{
		return new AQProtect(manager, player, params);
	}
	
	@Override
	protected boolean execute()
	{
		if(this.regionName == null)
			return false;
		
		if(this.manager.selection == null)
		{
			AQRegionActionManager.message(this.player, AQRegionActionManager.chatComponent(EnumChatFormatting.RED, "region.noselect"));
			return false;
		}
		
		AQRegionProvider provider = AQRegionManager.getProviderForPlayer(this.player);
		AQRegion parent = provider.findRegion(this.parentName);
		
		if(parent == null)
			parent = provider.root;
		
		new AQProtectionRegion(this.regionName, parent, this.manager.selection.shape.clone(), this.player).add();
		
		this.manager.freeSelection();
		this.manager.buffer = null;
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
		AQRegionProvider provider = AQRegionManager.getProviderForPlayer(this.player);
		AQRegion region = provider.findRegion(this.regionName);
		
		if(region == null)
			return false;
		
		region.free();
		this.manager.selection = new AQSelection(AQRegionActionManager.selectionName, provider.root, region.shape.clone(), this.player);
		this.manager.selection.add();
		
		return true;
	}
}