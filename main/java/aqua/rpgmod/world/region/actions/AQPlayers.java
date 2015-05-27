package aqua.rpgmod.world.region.actions;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import aqua.rpgmod.service.AQChatManager;
import aqua.rpgmod.world.region.AQPlayerProtectionUnit.PlayerAction;
import aqua.rpgmod.world.region.AQPlayerProtectionUnit.Status;
import aqua.rpgmod.world.region.AQProtectionRegion;
import aqua.rpgmod.world.region.AQRegion;
import aqua.rpgmod.world.region.AQRegion.RegionType;
import aqua.rpgmod.world.region.AQRegionManager;
import aqua.rpgmod.world.region.AQRegionProvider;
import aqua.rpgmod.world.region.actions.AQRegionActionManager.RegionAction;

public class AQPlayers extends RegionAction<AQPlayers>
{
	final PlayerAction action;
	final String regionName;
	final String playerName;
	final Status status;
	
	public AQPlayers(AQRegionActionManager manager)
	{
		super(manager);
		this.action = null;
		this.regionName = null;
		this.playerName = null;
		this.status = Status.MEMBER;
	}

	public AQPlayers(AQRegionActionManager manager, EntityPlayer player, List<String> params)
	{
		super(manager, player, params);
		
		while(true)
		{
			if(this.params.size() < 1)
				break;
			
			PlayerAction action = PlayerAction.get(this.params.get(0));
			
			if(action == null || this.params.size() < 2)
				break;
			
			String regionName = this.params.get(1);
			
			if(action == PlayerAction.LIST || action == PlayerAction.CLEAR)
			{
				this.action = action;
				this.regionName = regionName;
				this.playerName = null;
				this.status = Status.MEMBER;
				return;
			}

			if(this.params.size() < 3)
				break;
			
			this.action = action;
			this.regionName = regionName;
			this.playerName = this.params.get(2);
			this.status = this.params.size() >= 4 ? Status.get(this.params.get(3)) : Status.MEMBER;
			return;
		}

		incorrectUsage();
		this.action = null;
		this.regionName = null;
		this.playerName = null;
		this.status = Status.MEMBER;
		params.add("add");
		params.add("null");
	}
	
	@Override
	public String getName()
	{
		return "players";
	}

	@Override
	protected AQPlayers clone(AQRegionActionManager manager, EntityPlayer player, List<String> params)
	{
		return new AQPlayers(manager, player, params);
	}

	@Override
	protected boolean execute()
	{
		if(this.action == null)
			return false;
		
		AQRegionProvider provider = AQRegionManager.getProviderForPlayer(this.player);
		AQRegion r = provider.findRegion(this.regionName);
		
		AQProtectionRegion region = r != null && r.type == RegionType.PROTECTION ? (AQProtectionRegion)r : null;
		
		if(region == null)
		{
			AQChatManager.message(this.player, AQChatManager.chatComponent(EnumChatFormatting.GREEN, "region.cantfind", this.regionName));
			return false;
		}
		
		switch(this.action)
		{
			case ADD:
				if(this.playerName == null)
				{
					incorrectUsage();
					return false;
				}

				region.addPlayer(this.playerName, this.status);
				AQChatManager.message(this.player, AQChatManager.chatComponent(EnumChatFormatting.GREEN, "command.region.players.added", this.playerName, this.regionName));
				return true;

			case REMOVE:
				if(this.playerName == null)
				{
					incorrectUsage();
					return false;
				}

				region.removePlayer(this.playerName);
				AQChatManager.message(this.player, AQChatManager.chatComponent(EnumChatFormatting.GREEN, "command.region.players.removed", this.playerName, this.regionName));
				return true;

			case CLEAR:
				region.clearPlayers();
				AQChatManager.message(this.player, AQChatManager.chatComponent(EnumChatFormatting.GREEN, "command.region.players.cleared", this.regionName));
				return true;

			case LIST:
				String[] players = region.getPlayers();
				
				if(players.length != 0)
				{
					AQChatManager.message(this.player, AQChatManager.chatComponent(EnumChatFormatting.GREEN, "command.region.players.list", this.regionName));
					AQChatManager.message(this.player, AQChatManager.list(EnumChatFormatting.GREEN, "* ", players));
					return true;
				}
				
				AQChatManager.message(this.player, AQChatManager.chatComponent(EnumChatFormatting.GREEN, "command.region.players.no", this.regionName));
				return true;
			
			default:
				break;
		}
		
		return false;
	}
}
