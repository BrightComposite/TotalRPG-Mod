package aqua.rpgmod.player.rpg;

import aqua.rpgmod.player.AQThisPlayerWrapper;
import aqua.rpgmod.player.AQPlayerWrapper.SkillModifier;

public class AQArchery extends AQAbilityGroup
{
	public final static AQArchery instance = new AQArchery();
	
	public AQArchery()
	{
		super("archery");
	}
	
	public static void updateModifiers()
	{
		AQThisPlayerWrapper wrapper = AQThisPlayerWrapper.getWrapper();
		
		wrapper.setSkillModifier(SkillModifier.ARCHER_RELOAD_TIME, 1.0f - (AQAbility.fastHand.level / 72.0f));
		wrapper.setSkillModifier(SkillModifier.ARCHER_ACCURACY, 1.0f - (AQAbility.accuracy.level / 4.0f));
		wrapper.setSkillModifier(SkillModifier.ARCHER_TENSION, (AQAbility.tension.level * 0.5f) + 1.0f);
	}
}
