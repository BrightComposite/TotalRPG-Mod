package aqua.rpgmod.item;

import java.text.DecimalFormat;
import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import aqua.rpgmod.AquaMod;
import aqua.rpgmod.client.render.item.AQSpearRenderer;
import aqua.rpgmod.client.render.item.AQSpecialRenderItem;
import aqua.rpgmod.fx.AQFxPlacement;
import aqua.rpgmod.fx.AQRingFxSystem;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class AQItemSpear extends ItemSword implements AQSpecialRenderItem
{
	float mountedExtraDamage = 4;
	float baseDamage;
	
	@SideOnly(Side.CLIENT)
	public IIcon fullModel;
	
	private IItemRenderer renderer;
	private float[] color = new float[]
	{
		0.5f, 2.0f, 2.0f
	};
	
	public AQItemSpear(Item.ToolMaterial material)
	{
		super(material);
		
		this.renderer = new AQSpearRenderer();
		
		Object headComponent = Items.stick;
		
		if(material == ToolMaterial.WOOD)
		{
			headComponent = Items.stick;
			setUnlocalizedName("wooden_spear");
		}
		else if(material == ToolMaterial.STONE)
		{
			headComponent = Blocks.cobblestone;
			setUnlocalizedName("stone_spear");
		}
		else if(material == ToolMaterial.IRON)
		{
			headComponent = Items.iron_ingot;
			setUnlocalizedName("iron_spear");
		}
		else if(material == ToolMaterial.GOLD)
		{
			headComponent = Items.gold_ingot;
			setUnlocalizedName("golden_spear");
		}
		else if(material == ToolMaterial.EMERALD)
		{
			headComponent = Items.diamond;
			setUnlocalizedName("diamond_spear");
		}
		
		Object Recipe[] = new Object[]
		{
			"00A", "0B0", "B00", Character.valueOf('A'), headComponent, Character.valueOf('B'), (Item) Item.itemRegistry.getObject("stick")
		};
		
		GameRegistry.addRecipe(new ItemStack(this, 1), Recipe);
		this.baseDamage = material.getDamageVsEntity() + 3;
		
		AQItems.addItem(this);
		AQItems.addSpecialItem(this);
	}
	
	@Override
	public IItemRenderer getRenderer()
	{
		return this.renderer;
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer)
	{
		entityPlayer.setItemInUse(itemStack, this.getMaxItemUseDuration(itemStack));
		
		return itemStack;
	}
	
	/**
	 * Return the enchantability factor of the item, most of the time is based
	 * on material.
	 */
	@Override
	public int getItemEnchantability()
	{
		return 10;
	}
	
	@Override
	public ItemStack onEaten(ItemStack itemStack, World world, EntityPlayer entityPlayer)
	{
		return itemStack;
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack)
	{
		return 72000;
	}
	
	@Override
	public void registerIcons(IIconRegister IconRegister)
	{
		super.registerIcons(IconRegister);
		
		this.fullModel = IconRegister.registerIcon(this.getIconString() + "_big");
	}
	
	@Override
	public void addInformation(ItemStack ItemStack, EntityPlayer EntityPlayer, List List, boolean b)
	{
		super.addInformation(ItemStack, EntityPlayer, List, b);
		
		List.add(EnumChatFormatting.DARK_GREEN
			+ StatCollector.translateToLocalFormatted(
				"attribute.modifier.plus." + 0,
				new DecimalFormat("#.###").format(this.mountedExtraDamage),
				StatCollector.translateToLocal("attribute.weapon.mountedBonus")));
	}
	
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
	{
		if(super.onLeftClickEntity(stack, player, entity))
			return true;
		
		if(entity instanceof EntityLivingBase)
		{
			EntityLivingBase ent = (EntityLivingBase) entity;
			
			if(!AquaMod.proxy.isDedicated())
				AQRingFxSystem.instance.spawnFx(player.getEntityWorld(), new AQFxPlacement.EntityPlacement(ent), this.color);
			
			ent.attackEntityFrom(
				DamageSource.causePlayerDamage(player),
				(player.isRiding() || player.isSprinting()) ? (this.baseDamage + this.mountedExtraDamage) : this.baseDamage);
			
			return true;
		}
		
		return false;
	}
	
}
