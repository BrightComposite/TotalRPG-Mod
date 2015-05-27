package aqua.rpgmod.world.region.actions;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumChatFormatting;
import aqua.rpgmod.geometry.AQPoint3D;
import aqua.rpgmod.world.region.AQSelection;
import aqua.rpgmod.world.region.actions.AQRegionActionManager.RegionAction;
import aqua.rpgmod.world.schematics.AQPasteUnit;
import aqua.rpgmod.world.schematics.AQSchematics;
import aqua.rpgmod.world.schematics.AQSchematics.BoxAlignment;
import aqua.rpgmod.world.schematics.AQSchematicsFillAdapter;

public class AQFill extends RegionAction<AQFill>
{
	public AQPasteUnit unit = null;
	Block block;
	int meta;
	
	public AQFill(AQRegionActionManager manager)
	{
		super(manager);
		
		this.block = Blocks.air;
		this.meta = 0;
	}
	
	public AQFill(AQRegionActionManager manager, EntityPlayer player, List<String> params)
	{
		super(manager, player, params);
		
		if(params.size() > 0)
			this.block = Block.getBlockFromName(params.get(0));
		
		if(params.size() > 1)
			this.meta = getInt(1, 0);
	}
	
	@Override
	public String getName()
	{
		return "fill";
	}
	
	@Override
	protected AQFill clone(AQRegionActionManager manager, EntityPlayer player, List<String> params)
	{
		return new AQFill(manager, player, params);
	}
	
	@Override
	protected boolean execute()
	{
		if(this.manager.selection == null)
		{
			AQRegionActionManager.message(this.player, AQRegionActionManager.chatComponent(EnumChatFormatting.RED, "region.noselect"));
			return false;
		}
		
		AQSelection selection = this.manager.selection;
		AQPoint3D pos = selection.shape.pos();

		AQSchematics schx = new AQSchematics();
		schx.load(new AQSchematicsFillAdapter(selection.shape, this.block, this.meta));
		
		this.unit = new AQPasteUnit(selection.provider.world, schx.getBox(pos, BoxAlignment.START));
		this.unit.paste(schx);
		
		AQRegionActionManager.message(this.player, AQRegionActionManager.chatComponent(EnumChatFormatting.GREEN, "region.filled"));
		
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