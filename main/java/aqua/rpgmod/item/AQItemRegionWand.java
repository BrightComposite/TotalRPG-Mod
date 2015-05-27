package aqua.rpgmod.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class AQItemRegionWand extends Item
{
	public AQItemRegionWand()
	{
		setUnlocalizedName("region_wand");
		this.maxStackSize = 1;
		AQItems.addItem(this);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer)
	{
		return super.onItemRightClick(itemStack, world, entityPlayer);
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack itemStack)
	{
		return EnumAction.block;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean isFull3D()
	{
		return true;
	}
	
	@Override
	public boolean func_150897_b(Block par1Block)
	{
		return false;
	}
	
	@Override
	public boolean canHarvestBlock(Block par1Block, ItemStack itemStack)
	{
		return false;
	}
	
	@Override
	public int getHarvestLevel(ItemStack stack, String toolClass)
	{
		return -1;
	}
	
	@Override
	public boolean onBlockStartBreak(ItemStack itemstack, int X, int Y, int Z, EntityPlayer player)
	{
		return super.onBlockStartBreak(itemstack, X, Y, Z, player);
	}
}
