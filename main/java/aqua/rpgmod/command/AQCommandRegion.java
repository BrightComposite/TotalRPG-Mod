package aqua.rpgmod.command;

import java.util.Arrays;
import java.util.List;

import aqua.rpgmod.world.region.actions.AQRegionActionManager;

public class AQCommandRegion extends AQActionCommand
{
	public AQCommandRegion()
	{
		super(AQRegionActionManager.instance);
	}
	
	@Override
	public List getCommandAliases()
	{
		return Arrays.asList(new String[]
		{
			"rg", "reg",
		});
	}
}
