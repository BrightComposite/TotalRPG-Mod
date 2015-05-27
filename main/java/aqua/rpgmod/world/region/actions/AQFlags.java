package aqua.rpgmod.world.region.actions;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import aqua.rpgmod.service.AQChatManager;
import aqua.rpgmod.world.region.AQProtectionRegion;
import aqua.rpgmod.world.region.AQProtectionUnit.Flag;
import aqua.rpgmod.world.region.AQProtectionUnit.FlagAction;
import aqua.rpgmod.world.region.AQRegion;
import aqua.rpgmod.world.region.AQRegion.RegionType;
import aqua.rpgmod.world.region.AQRegionManager;
import aqua.rpgmod.world.region.AQRegionProvider;
import aqua.rpgmod.world.region.actions.AQRegionActionManager.RegionAction;

public class AQFlags extends RegionAction<AQFlags>
{
	final FlagAction action;
	final String regionName;
	final Flag flags[];
	
	public AQFlags(AQRegionActionManager manager)
	{
		super(manager);
		this.action = null;
		this.regionName = null;
		this.flags = new Flag[0];
	}

	public AQFlags(AQRegionActionManager manager, EntityPlayer player, List<String> params)
	{
		super(manager, player, params);
		
		while(true)
		{
			if(this.params.size() < 1)
				break;
			
			FlagAction action = FlagAction.get(this.params.get(0));
			
			if(action == null || (action != FlagAction.LIST && this.params.size() < 2))
				break;
			
			String regionName = this.params.size() >= 2 ? this.params.get(1) : null;
			
			if(action == FlagAction.LIST)
			{
				this.action = action;
				this.regionName = regionName;
				this.flags = new Flag[0];
				return;
			}
			
			Flag flags[] = new Flag[this.params.size() - 2];
			
			L : for(int i = 0; i < flags.length; ++i)
			{
				String flag = this.params.get(2 + i);
				flags[i] = Flag.get(flag);
				
				if(flags[i] == null)
				{
					incorrectFlag(flag);
					break L;
				}
			}

			this.action = action;
			this.regionName = regionName;
			this.flags = flags;
			return;
		}

		incorrectUsage();
		this.action = null;
		this.regionName = null;
		this.flags = new Flag[0];
		params.add("add");
		params.add("null");
	}
	
	public void incorrectFlag(String flag)
	{
		if(this.player == null)
			return;
		
		AQChatManager.message(this.player, AQChatManager.chatComponent(EnumChatFormatting.RED, "command.region.flags.unknownflag", AQChatManager.chatComponent(EnumChatFormatting.YELLOW, flag).getFormattedText()));
	}

	@Override
	public String getName()
	{
		return "flags";
	}

	@Override
	protected AQFlags clone(AQRegionActionManager manager, EntityPlayer player, List<String> params)
	{
		return new AQFlags(manager, player, params);
	}

	@Override
	protected boolean execute()
	{
		if(this.action == null)
			return false;
		
		AQRegionProvider provider = AQRegionManager.getProviderForPlayer(this.player);
		AQRegion r = this.regionName != null ? provider.findRegion(this.regionName) : null;
		
		AQProtectionRegion region = r != null && r.type == RegionType.PROTECTION ? (AQProtectionRegion)r : null;

		if(region == null && this.regionName != null)
		{
			AQChatManager.message(this.player, AQChatManager.chatComponent(EnumChatFormatting.GREEN, "region.cantfind", this.regionName));
			return false;
		}
		
		switch(this.action)
		{
			case ADD:
				if(region == null)
				{
					incorrectUsage();
					return false;
				}

				if(this.flags == null)
				{
					incorrectUsage();
					return false;
				}

				region.addFlags(this.flags);
				AQChatManager.message(this.player, AQChatManager.chatComponent(EnumChatFormatting.GREEN, "command.region.flags.added", this.regionName));
				return true;

			case SET:
				if(region == null)
				{
					incorrectUsage();
					return false;
				}

				if(this.flags == null)
				{
					incorrectUsage();
					return false;
				}

				region.setFlags(this.flags);
				AQChatManager.message(this.player, AQChatManager.chatComponent(EnumChatFormatting.GREEN, "command.region.flags.set", this.regionName));
				return true;

			case CLEAR:
				if(region == null)
				{
					incorrectUsage();
					return false;
				}

				region.clearFlags(this.flags);
				AQChatManager.message(this.player, AQChatManager.chatComponent(EnumChatFormatting.GREEN, "command.region.flags.cleared", this.regionName));
				return true;

			case LIST:
				if(region == null)
				{
					AQChatManager.message(this.player, AQChatManager.chatComponent(EnumChatFormatting.GREEN, "command.region.flags.all"));
					AQChatManager.message(this.player, AQChatManager.list(EnumChatFormatting.GREEN, "- ", Flag.getNames(Flag.values())));
					return true;
				}
				
				Flag[] flags = region.getFlags();
				
				if(flags.length != 0)
				{
					AQChatManager.message(this.player, AQChatManager.chatComponent(EnumChatFormatting.GREEN, "command.region.flags.list", this.regionName));
					AQChatManager.message(this.player, AQChatManager.list(EnumChatFormatting.GREEN, "- ", Flag.getNames(flags)));
					return true;
				}
				
				AQChatManager.message(this.player, AQChatManager.chatComponent(EnumChatFormatting.GREEN, "command.region.flags.no", this.regionName));
				return true;
			
			default:
				break;
		}
		
		return false;
	}
}