package aqua.rpgmod.player.rpg;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;

abstract public class AQDependantAbility extends AQAbility
{
	public final AQAbility dependence;
	
	public AQDependantAbility(AQAbilityGroup group, String name, int experienceTable[], AQAbility dependence, int displayX, int displayY)
	{
		super(group, name, experienceTable, displayX, displayY);
		
		this.dependence = dependence;
	}
	
	abstract public int getDependenceLevelNeeded();
	
	@Override
	public String addDescription(int code, String desc)
	{
		desc = super.addDescription(code, desc);
		
		switch(code)
		{
			case 3:
				desc += "\n" + EnumChatFormatting.GRAY + I18n.format("skills.unavailable");
				desc += "\n" + EnumChatFormatting.DARK_RED + I18n.format("skills.dependence") + this.dependence.desc()
					+ AQAbility.levelString(getDependenceLevelNeeded());
				break;
		}
		
		return desc;
	}
}
