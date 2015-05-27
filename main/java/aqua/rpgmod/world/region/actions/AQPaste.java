package aqua.rpgmod.world.region.actions;

import static aqua.rpgmod.service.AQChatManager.chatComponent;
import static aqua.rpgmod.service.AQChatManager.message;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import aqua.rpgmod.geometry.AQPoint3D;
import aqua.rpgmod.player.AQPlayerWrapper;
import aqua.rpgmod.world.region.AQRegionManager;
import aqua.rpgmod.world.region.AQRegionProvider;
import aqua.rpgmod.world.region.actions.AQRegionActionManager.RegionAction;
import aqua.rpgmod.world.schematics.AQPasteUnit;
import aqua.rpgmod.world.schematics.AQSchematics.BoxAlignment;

public class AQPaste extends RegionAction<AQPaste>
{
	public final AQPoint3D pos;
	public AQPasteUnit unit = null;
	
	public AQPaste(AQRegionActionManager manager)
	{
		super(manager);
		this.pos = null;
	}
	
	public AQPaste(AQRegionActionManager manager, EntityPlayer player, List<String> params)
	{
		super(manager, player, params);
		AQPoint3D coords = getCoords(0);
		
		if(coords == null)
		{
			if(manager.getPoint(2) == null)
				manager.updatePos(AQPlayerWrapper.getPlayerPos(player));
			
			coords = manager.getPoint(2);
			
			params.add(String.valueOf(coords.x));
			params.add(String.valueOf(coords.y));
			params.add(String.valueOf(coords.z));
		}
		
		this.pos = coords;
	}
	
	@Override
	public String getName()
	{
		return "paste";
	}
	
	@Override
	protected AQPaste clone(AQRegionActionManager manager, EntityPlayer player, List<String> params)
	{
		return new AQPaste(manager, player, params);
	}
	
	@Override
	public boolean needPermission()
	{
		return true;
	}
	
	@Override
	protected boolean execute()
	{
		if(this.manager.buffer == null)
		{
			message(this.player, chatComponent(EnumChatFormatting.RED, "region.noload"));
			return false;
		}
		
		AQRegionProvider provider = AQRegionManager.getProviderForPlayer(this.player);
		this.unit = new AQPasteUnit(provider.world, this.manager.buffer.getBox(this.pos, BoxAlignment.START));
		this.unit.paste(this.manager.buffer);
		
		message(this.player, chatComponent(EnumChatFormatting.GREEN, "region.pasted"));
		
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
		if(this.unit == null)
			return false;
		
		return this.unit.restore();
	}
}
