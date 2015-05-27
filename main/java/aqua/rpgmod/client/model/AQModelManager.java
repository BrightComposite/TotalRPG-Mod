package aqua.rpgmod.client.model;

import java.util.HashMap;
import java.util.Iterator;

import aqua.rpgmod.character.AQRace;
import aqua.rpgmod.character.AQRace.Arachnoid;
import aqua.rpgmod.character.AQRace.Centaurus;
import aqua.rpgmod.character.AQRace.Dwarf;
import aqua.rpgmod.character.AQRace.Elf;
import aqua.rpgmod.character.AQRace.Human;

public class AQModelManager
{
	protected static HashMap<AQRace, Class<? extends AQModelPlayer>> models = new HashMap<AQRace, Class<? extends AQModelPlayer>>();
	
	static
	{
		models.put(Human.instance(), AQModelHuman.class);
		models.put(Elf.instance(), AQModelElf.class);
		models.put(Dwarf.instance(), AQModelDwarf.class);
		models.put(Arachnoid.instance(), AQModelArachnoid.class);
		models.put(Centaurus.instance(), AQModelCentaurus.class);
	}
	
	public static AQModelPlayer create(AQRace race)
	{
		Class<? extends AQModelPlayer> cl = models.get(race);
		
		if(cl == null)
			return null;
		
		try
		{
			return cl.newInstance();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static HashMap<AQRace, AQModelPlayer> createMap()
	{
		HashMap<AQRace, AQModelPlayer> map = new HashMap<AQRace, AQModelPlayer>();
		
		for(Iterator<AQRace> i = models.keySet().iterator(); i.hasNext();)
		{
			AQRace race = i.next();
			AQModelPlayer value = create(race);
			
			if(value != null)
				map.put(race, value);
		}
		
		return map;
	}
}
