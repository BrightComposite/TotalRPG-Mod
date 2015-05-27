package aqua.rpgmod.world.region.actions;

import static aqua.rpgmod.service.AQChatManager.chatComponent;
import static aqua.rpgmod.service.AQChatManager.message;

import java.util.List;
import java.util.concurrent.Callable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import aqua.rpgmod.service.resources.AQFileResource;
import aqua.rpgmod.world.region.actions.AQRegionActionManager.RegionAction;
import aqua.rpgmod.world.schematics.AQSchematicsResource;

public class AQSave extends RegionAction<AQSave>
{
	public final String fileName;
	
	public AQSave(AQRegionActionManager manager)
	{
		super(manager);
		this.fileName = "";
	}
	
	public AQSave(AQRegionActionManager manager, EntityPlayer player, List<String> params)
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
		return "save";
	}
	
	@Override
	protected AQSave clone(AQRegionActionManager manager, EntityPlayer player, List<String> params)
	{
		return new AQSave(manager, player, params);
	}
	
	@Override
	protected boolean execute()
	{
		if(this.fileName == null)
			return false;
		
		if(this.manager.buffer == null)
		{
			message(this.player, chatComponent(EnumChatFormatting.RED, "region.noload"));
			return false;
		}
		
		final AQSchematicsResource resource = new AQSchematicsResource(this.manager.buffer, new AQFileResource(this.fileName));
		
		resource.save(new Callable()
		{
			@Override
			public Object call() throws Exception
			{
				if(resource.success)
					message(AQSave.this.player, chatComponent(EnumChatFormatting.GREEN, "region.saved", AQSave.this.fileName));
				else
					message(AQSave.this.player, chatComponent(EnumChatFormatting.RED, "region.notsaved", AQSave.this.fileName));
				
				return null;
			}
		});
		
		return true;
	}
}
