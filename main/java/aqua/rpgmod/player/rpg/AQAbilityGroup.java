package aqua.rpgmod.player.rpg;

import java.util.HashMap;
import java.util.Iterator;

import aqua.rpgmod.service.config.AQIntProperty;
import aqua.rpgmod.service.config.AQMapProperty;

public class AQAbilityGroup
{
	public static final HashMap<String, AQAbilityGroup> map = new HashMap<String, AQAbilityGroup>();
	
	public final String name;
	final HashMap<String, AQAbility> abilities = new HashMap<String, AQAbility>();
	
	public AQAbilityGroup(String name)
	{
		this.name = name;
		map.put(this.name, this);
	}

	public Iterator<AQAbility> abilityIterator()
	{
		return this.abilities.values().iterator();
	}
	
	public AQAbilityGroup read(AQMapProperty mapProperty)
	{
		AQIntProperty property = new AQIntProperty();
		
		for(Iterator<AQAbility> i = abilityIterator(); i.hasNext();)
		{
			AQAbility ability = i.next();
			
			if(mapProperty.getProperty(ability.name, property))
				ability.level = property.value;
		}
		
		return this;
	}
	
	public AQMapProperty write(AQMapProperty mapProperty)
	{
		for(Iterator<AQAbility> i = abilityIterator(); i.hasNext();)
		{
			AQAbility ability = i.next();
			mapProperty.setProperty(ability.name, new AQIntProperty(ability.level));
		}
		
		return mapProperty;
	}
}
