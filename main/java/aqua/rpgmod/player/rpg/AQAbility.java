package aqua.rpgmod.player.rpg;

import java.util.Iterator;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import aqua.rpgmod.AquaMod;
import aqua.rpgmod.player.AQThisPlayerWrapper;
import aqua.rpgmod.player.rpg.AQHarvesting.ToolAbility;
import aqua.rpgmod.service.AQStringTranslator;
import aqua.rpgmod.service.config.AQMapProperty;
import aqua.rpgmod.service.network.AQMessage;
import aqua.rpgmod.service.network.AQMessage.PlayerMessage;

public class AQAbility
{
	protected final static AQAbility harvester = new AQAbility(AQHarvesting.instance, "harvester", new int[]{1, 3, 5}, 114, 85);
	protected final static AQDependantAbility miner = new ToolAbility(AQHarvesting.instance, "miner", new int[]{1, 3, 5, 7}, harvester, 114, 45);
	protected final static AQDependantAbility digger = new ToolAbility(AQHarvesting.instance, "digger", new int[]{1, 3, 5, 7}, harvester, 77, 105);
	protected final static AQDependantAbility woodcutter = new ToolAbility(AQHarvesting.instance, "woodcutter", new int[]{1, 3, 5, 7}, harvester, 151, 105);

	protected final static AQAbility accuracy = new AQAbility(AQArchery.instance, "accuracy", new int[]{1, 3, 5, 7}, 114, 45);
	protected final static AQAbility tension = new AQAbility(AQArchery.instance, "tension", new int[]{1, 3, 5, 7}, 77, 105);
	protected final static AQAbility fastHand = new AQAbility(AQArchery.instance, "fastHand", new int[]{1, 3, 5}, 151, 105);
	
	static
	{
		harvester.level = 1;
	}
	
	public static void updateFromServer()
	{
		for(Iterator<AQAbilityGroup> i = AQAbilityGroup.map.values().iterator(); i.hasNext();)
		{
			AQAbilityGroup group = i.next();
			AquaMod.playerNetwork.sendToServer(new AQMessage(PlayerMessage.LOAD_ABILITY_GROUP, new AQStringTranslator(group.name)));
		}
	}
	
	public final AQAbilityGroup group;
	public final String name;

	public final int experienceTable[];
	protected int level = 0;
	public boolean available = false;
	
	public int displayX = 0;
	public int displayY = 0;
	
	public AQAbility(AQAbilityGroup group, String name, int experienceTable[], int displayX, int displayY)
	{
		this.group = group;
		this.name = name;
		
		this.experienceTable = experienceTable;
		
		this.displayX = displayX;
		this.displayY = displayY;
		
		group.abilities.put(name, this);
	}
	
	public int getLevel()
	{
		return this.level;
	}
	
	public boolean active()
	{
		return this.level > 0;
	}
	
	public boolean closed()
	{
		return this.level == this.experienceTable.length;
	}

	public int getExperienceToIncrease()
	{
		return closed() ? 0 : this.experienceTable[this.level] - AQThisPlayerWrapper.getWrapper().player.experienceLevel;
	}
	
	public String desc()
	{
		return I18n.format("skills." + this.group.name + "." + this.name);
	}
	
	static String roman[] = new String[]
	{
		"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"
	};
	
	public static String levelString(int level)
	{
		if(level <= 0 || level >= roman.length)
			return "";
		
		return " " + roman[level - 1];
	}
	
	public String addDescription(int code, String desc)
	{
		switch(code)
		{
			case 0:
				desc += "\n" + EnumChatFormatting.GREEN + I18n.format("skills.available");
				break;
			case 1:
				desc += "\n" + EnumChatFormatting.GRAY + I18n.format("skills.unavailable") + EnumChatFormatting.WHITE + String.valueOf(getExperienceToIncrease());
				break;
			case 2:
				desc += "\n" + EnumChatFormatting.GOLD + I18n.format("skills.maximum");
				break;
		}
		
		return desc;
	}
	
	@SuppressWarnings("static-method")
	public boolean dependant()
	{
		return false;
	}
	
	public int update()
	{
		AQThisPlayerWrapper wrapper = AQThisPlayerWrapper.getWrapper();
		
		this.available = false;

		if(closed())
			return 2;
		
		if(wrapper.player.experienceLevel < this.experienceTable[this.level])
			return 1;
		
		if(dependant())
			return 3;
		
		this.available = true;
		
		return 0;
	}
	
	public void increase()
	{
		if(!this.available)
			return;

		this.level++;
		
		AQThisPlayerWrapper wrapper = AQThisPlayerWrapper.getWrapper();
		wrapper.player.addExperienceLevel(-this.experienceTable[this.level - 1]);
		AquaMod.playerNetwork.sendToServer(new AQMessage(PlayerMessage.SET_ABILITY_GROUP, new AQStringTranslator(this.group.name), new AQStringTranslator(this.group.write(new AQMapProperty()).toString())));
		AquaMod.playerNetwork.sendToServer(new AQMessage(PlayerMessage.CHANGE_EXPERIENCE, new AQExperienceTranslator(true, -this.experienceTable[this.level - 1])));
		
	}
}
