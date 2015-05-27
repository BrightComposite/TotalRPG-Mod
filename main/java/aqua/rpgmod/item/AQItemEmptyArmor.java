package aqua.rpgmod.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.EnumHelper;

public class AQItemEmptyArmor extends ItemArmor
{
	public static final ArmorMaterial material = EnumHelper.addArmorMaterial("Empty", 5, new int[]
	{
		0, 0, 0, 0
	}, 0);
	
	public AQItemEmptyArmor()
	{
		super(material, 0, 0);
		setUnlocalizedName("disabled_armor");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister p_94581_1_)
	{}
	
	public static IIcon func_94602_b(int p_94602_0_)
	{
		return null;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamageForRenderPass(int p_77618_1_, int p_77618_2_)
	{
		return null;
	}
}
