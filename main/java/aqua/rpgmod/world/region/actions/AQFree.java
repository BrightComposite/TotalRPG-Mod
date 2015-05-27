package aqua.rpgmod.world.region.actions;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import aqua.rpgmod.geometry.AQShape3D;
import aqua.rpgmod.world.region.AQProtectionRegion;
import aqua.rpgmod.world.region.AQRegion;
import aqua.rpgmod.world.region.AQRegionManager;
import aqua.rpgmod.world.region.AQRegionProvider;
import aqua.rpgmod.world.region.actions.AQRegionActionManager.RegionAction;

public class AQFree extends RegionAction<AQFree>
{
	final String regionName;
	String parentName;
	AQShape3D regionShape;
	
	public AQFree(AQRegionActionManager manager)
	{
		super(manager);
		this.regionName = null;
	}
	
	public AQFree(AQRegionActionManager manager, EntityPlayer player, List<String> params)
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
	}
	
	@Override
	public String getName()
	{
		return "free";
	}
	
	@Override
	protected AQFree clone(AQRegionActionManager manager, EntityPlayer player, List<String> params)
	{
		return new AQFree(manager, player, params);
	}
	
	@Override
	protected boolean execute()
	{
		if(this.regionName == null)
			return false;
		
		AQRegionProvider provider = AQRegionManager.getProviderForPlayer(this.player);
		AQRegion region = provider.findRegion(this.regionName);
		
		if(region == null)
			return false;
		
		this.regionShape = region.shape.clone();
		
		AQRegion parent = region.getParent();
		this.parentName = parent != null ? parent.name : null;

		region.free();
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
		AQRegion parent = provider.findRegion(this.parentName);
		
		if(parent == null)
			parent = provider.root;
		
		new AQProtectionRegion(this.regionName, parent, this.regionShape, this.player).add();
		return true;
	}
}