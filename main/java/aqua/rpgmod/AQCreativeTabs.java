package aqua.rpgmod;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class AQCreativeTabs extends CreativeTabs
{
	public static final AQCreativeTabs instance = new AQCreativeTabs(CreativeTabs.getNextID(), "AquaCreativeTab");
	
	protected AQCreativeTabs(int position, String tabID)
	{
		super(position, tabID);
	}
	
	@Override
	public String getTranslatedTabLabel()
	{
		return "Aqua";
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Item getTabIconItem()
	{
		return Items.water_bucket;
	}
}
