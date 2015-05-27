package aqua.rpgmod.world.region.actions;

import static aqua.rpgmod.service.AQChatManager.chatComponent;
import static aqua.rpgmod.service.AQChatManager.message;

import java.util.List;
import java.util.concurrent.Callable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import aqua.rpgmod.geometry.AQShape3D;
import aqua.rpgmod.service.resources.AQFileResource;
import aqua.rpgmod.world.region.actions.AQRegionActionManager.RegionAction;
import aqua.rpgmod.world.schematics.AQSchematicsResource;

public class AQLoad extends RegionAction<AQLoad>
{
	public final String fileName;
	AQShape3D old = null;
	
	public AQLoad(AQRegionActionManager manager)
	{
		super(manager);
		this.fileName = "";
	}
	
	public AQLoad(AQRegionActionManager manager, EntityPlayer player, List<String> params)
	{
		super(manager, player, params);
		
		if(this.params.size() < 1)
		{
			incorrectUsage();
			this.fileName = null;
			params.add("null");
			return;
		}
		
		this.fileName = this.params.get(0);
	}
	
	@Override
	public String getName()
	{
		return "load";
	}
	
	@Override
	protected AQLoad clone(AQRegionActionManager manager, EntityPlayer player, List<String> params)
	{
		return new AQLoad(manager, player, params);
	}
	
	@Override
	protected boolean execute()
	{
		final AQSchematicsResource resource = new AQSchematicsResource(new AQFileResource(this.fileName));
		
		resource.load(new Callable()
		{
			@Override
			public Object call() throws Exception
			{
				if(resource.success)
				{
					message(
						AQLoad.this.player,
						chatComponent(EnumChatFormatting.GREEN, "region.loaded", AQLoad.this.fileName),
						chatComponent(EnumChatFormatting.GOLD, "command.region.paste.usage"));
					
					if(AQLoad.this.manager.selection == null)
					{
						AQLoad.this.manager.createSelectionFor(resource.schx);
						AQLoad.this.old = AQLoad.this.manager.selection.shape;
					}
					else
					{
						AQLoad.this.old = AQLoad.this.manager.selection.shape;
						AQLoad.this.manager.createSelectionFor(resource.schx);
					}
					
					AQLoad.this.manager.buffer = resource.schx;
				}
				else
					message(AQLoad.this.player, chatComponent(EnumChatFormatting.RED, "region.notloaded", AQLoad.this.fileName));
				
				return null;
			}
		});
		
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
		this.manager.buffer = null;
		this.manager.selection.shape = this.old;
		this.manager.selection.update();
		return true;
	}
}
