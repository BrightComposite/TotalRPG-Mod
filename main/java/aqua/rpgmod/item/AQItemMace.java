package aqua.rpgmod.item;

import java.text.DecimalFormat;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.common.registry.GameRegistry;

public class AQItemMace extends ItemSword
{
	float skeletonExtraDamage = 4;
	float baseDamage;
	
	public AQItemMace(Item.ToolMaterial material)
	{
		super(material);
		
		Object headComponent = Blocks.planks;
		
		if(material == ToolMaterial.WOOD)
		{
			headComponent = Blocks.planks;
			setUnlocalizedName("wooden_mace");
		}
		else if(material == ToolMaterial.STONE)
		{
			headComponent = Blocks.cobblestone;
			setUnlocalizedName("stone_mace");
		}
		else if(material == ToolMaterial.IRON)
		{
			headComponent = Items.iron_ingot;
			setUnlocalizedName("iron_mace");
		}
		else if(material == ToolMaterial.GOLD)
		{
			headComponent = Items.gold_ingot;
			setUnlocalizedName("golden_mace");
		}
		else if(material == ToolMaterial.EMERALD)
		{
			headComponent = Items.diamond;
			setUnlocalizedName("diamond_mace");
		}
		
		Object Recipe[] = new Object[]
		{
			"00A", "0B0", "B00", Character.valueOf('A'), headComponent, Character.valueOf('B'), Items.stick
		};
		
		GameRegistry.addRecipe(new ItemStack(this, 1), Recipe);
		this.baseDamage = material.getDamageVsEntity() + 4;
		
		AQItems.addItem(this);
	}
	
	@Override
	public void addInformation(ItemStack ItemStack, EntityPlayer EntityPlayer, List List, boolean b)
	{
		super.addInformation(ItemStack, EntityPlayer, List, b);
		
		List.add(EnumChatFormatting.DARK_GREEN
			+ StatCollector.translateToLocalFormatted(
				"attribute.modifier.plus." + 0,
				new DecimalFormat("#.###").format(this.skeletonExtraDamage),
				StatCollector.translateToLocal("attribute.weapon.skeletonBonus")));
	}
	
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
	{
		if(super.onLeftClickEntity(stack, player, entity))
			return true;
		
		if(entity instanceof EntityLivingBase)
		{
			((EntityLivingBase) entity).attackEntityFrom(
				DamageSource.causePlayerDamage(player),
				(entity instanceof EntitySkeleton) ? (this.baseDamage + this.skeletonExtraDamage) : this.baseDamage);
			
			return true;
		}
		
		return false;
	}
}
