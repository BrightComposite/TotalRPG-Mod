package aqua.rpgmod.world.gen;

import aqua.rpgmod.world.gen.structure.AQMapGenVillage;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import net.minecraftforge.event.terraingen.InitMapGenEvent.EventType;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class AQTerrainGenHandler
{
	@SuppressWarnings("static-method")
	@SubscribeEvent
	public void onEvent(InitMapGenEvent event)
	{
		if(event.type == EventType.VILLAGE)
		{
			event.newGen = new AQMapGenVillage();
		}
	}
	
}
