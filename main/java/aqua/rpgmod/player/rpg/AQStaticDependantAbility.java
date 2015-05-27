package aqua.rpgmod.player.rpg;

public class AQStaticDependantAbility extends AQDependantAbility
{
	public final int dependenceLevel;
	
	public AQStaticDependantAbility(AQAbilityGroup group, String name, int experienceTable[], AQAbility dependence, int dependenceLevel, int displayX,
		int displayY)
	{
		super(group, name, experienceTable, dependence, displayX, displayY);
		
		this.dependenceLevel = dependenceLevel;
	}
	
	@Override
	public int getDependenceLevelNeeded()
	{
		if(this.dependence == null)
			return 0;
		
		return this.dependenceLevel;
	}
}
