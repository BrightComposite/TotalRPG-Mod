package aqua.rpgmod.player.rpg;

import java.util.HashMap;

import aqua.rpgmod.player.AQThisPlayerWrapper;
import aqua.rpgmod.player.AQPlayerWrapper.SkillModifier;
import net.minecraft.block.Block;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraftforge.common.ForgeHooks;

import static aqua.rpgmod.player.rpg.AQAbility.*;

public class AQHarvesting extends AQAbilityGroup
{
	public final static AQHarvesting instance = new AQHarvesting();
	
	public static HashMap<ToolMaterial, Integer> minLevels = new HashMap<ToolMaterial, Integer>();
	
	static
	{
		setToolMaterialLevel(ToolMaterial.WOOD, 0);
		setToolMaterialLevel(ToolMaterial.STONE, 1);
		setToolMaterialLevel(ToolMaterial.IRON, 1);
		setToolMaterialLevel(ToolMaterial.GOLD, 2);
		setToolMaterialLevel(ToolMaterial.EMERALD, 3);
	}
	
	public static void setToolMaterialLevel(ToolMaterial material, int level)
	{
		minLevels.put(material, Integer.valueOf(level));
	}
	
	public static int getToolMaterialLevel(ToolMaterial material)
	{
		return minLevels.get(material).intValue();
	}
	
	protected static class ToolAbility extends AQDependantAbility
	{
		public ToolAbility(AQAbilityGroup group, String name, int experienceTable[], AQAbility dependence, int displayX, int displayY)
		{
			super(group, name, experienceTable, dependence, displayX, displayY);
		}
		
		@Override
		public int getDependenceLevelNeeded()
		{
			if(this.level >= 3)
				return 3;
			
			if(this.level >= 1)
				return 2;
			
			return 1;
		}
	}
	
	public AQHarvesting()
	{
		super("harvesting");
	}
	
	public static void updateToolModifier(ItemStack stack, Block block, int metadata)
	{
		ItemTool tool = getTool(stack);
		
		if(tool == null || !ForgeHooks.isToolEffective(stack, block, metadata))
		{
			AQThisPlayerWrapper.getWrapper().setSkillModifier(SkillModifier.BREAK, defaultModifier());
			return;
		}
		
		ToolMaterial material = ToolMaterial.valueOf(tool.getToolMaterialName());
		
		if(tool instanceof ItemAxe)
		{
			if(woodcutter.active())
				use(woodcutter, material);
			
			return;
		}
		
		if(tool instanceof ItemPickaxe)
		{
			if(miner.active())
				use(miner, material);
			
			return;
		}
	}

	static float defaultModifier()
	{
		return 0.05f * (harvester.level + 2);
	}
	
	static ItemTool getTool(ItemStack stack)
	{
		return (stack != null && stack.getItem() instanceof ItemTool) ? (ItemTool) stack.getItem() : null;
	}
	
	static void use(AQAbility ability, ToolMaterial toolMaterial)
	{
		if(ability.level < getToolMaterialLevel(toolMaterial))
		{
			AQThisPlayerWrapper.getWrapper().setSkillModifier(SkillModifier.BREAK, defaultModifier());
			return;
		}
		
		float modifier = 0.0625f * (harvester.level + ability.level + 2);
		AQThisPlayerWrapper.getWrapper().setSkillModifier(SkillModifier.BREAK, modifier);
	}
	
}
