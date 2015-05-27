package aqua.rpgmod.world.region.actions;

import static aqua.rpgmod.service.AQChatManager.chatComponent;
import static aqua.rpgmod.service.AQChatManager.message;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import aqua.rpgmod.geometry.AQPoint3D;
import aqua.rpgmod.world.region.actions.AQRegionActionManager.RegionAction;

public class AQPoint extends RegionAction<AQPoint>
{
	int index;
	final AQPoint3D point;
	AQPoint3D old;
	
	public AQPoint(AQRegionActionManager manager)
	{
		super(manager);
		
		this.index = 0;
		this.point = null;
	}
	
	public AQPoint(AQRegionActionManager manager, EntityPlayer player, List<String> params)
	{
		super(manager, player, params);
		
		if(params.size() > 0)
		{
			this.index = getInt(0, 0);
		}
		else
		{
			this.index = 0;
			params.add("0");
		}
		
		AQPoint3D coords = getCoords(1);
		
		if(coords == null)
		{
			coords = new AQPoint3D(player.posX, player.posY, player.posZ);
			
			params.add(String.valueOf(coords.x));
			params.add(String.valueOf(coords.y));
			params.add(String.valueOf(coords.z));
		}
		
		this.point = coords;
	}
	
	@Override
	public String getName()
	{
		return "point";
	}
	
	@Override
	protected AQPoint clone(AQRegionActionManager manager, EntityPlayer player, List<String> params)
	{
		return new AQPoint(manager, player, params);
	}
	
	@Override
	protected boolean execute()
	{
		if(this.manager.buffer != null)
		{
			if(this.index != 0)
				return false;
			
			this.index = 2;
			
			this.old = this.manager.getPoint(2);
			this.manager.setPoint(2, this.point);
			
			message(
				this.player,
				chatComponent(
					EnumChatFormatting.GREEN,
					"region.select.pos",
					Integer.valueOf(this.point.x),
					Integer.valueOf(this.point.y),
					Integer.valueOf(this.point.z)));
			
			return true;
		}
		
		this.old = this.manager.getPoint(this.index);
		this.manager.setPoint(this.index, this.point);
		
		message(
			this.player,
			chatComponent(
				EnumChatFormatting.GREEN,
				(this.index == 1) ? "region.select.second" : "region.select.first",
				Integer.valueOf(this.point.x),
				Integer.valueOf(this.point.y),
				Integer.valueOf(this.point.z)));
		
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
		this.manager.setPoint(this.index, this.old);
		
		return true;
	}
}
