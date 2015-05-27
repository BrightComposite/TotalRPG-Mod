package aqua.rpgmod.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.client.MinecraftForgeClient;

import aqua.rpgmod.AQCreativeTabs;
import aqua.rpgmod.AQModInfo;
import aqua.rpgmod.client.render.item.AQSpecialRenderItem;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class AQItems
{
	public static final List<Item> items = new ArrayList<Item>();
	public static final List<AQSpecialRenderItem> specialItems = new ArrayList<AQSpecialRenderItem>();
	
	public static final ToolMaterial materials[] = new ToolMaterial[]
	{
		ToolMaterial.WOOD, ToolMaterial.STONE, ToolMaterial.IRON, ToolMaterial.GOLD, ToolMaterial.EMERALD
	};
	
	public static AQItemMagicSphere magicSphere;
	public static AQItemSpell spell;
	public static AQItemRegionWand regionWand;
	public static AQItemTotem totem;
	
	public static AQItemEmptyArmor emptyArmor;
	
	public static final HashMap<ToolMaterial, AQItemSpear> spears = new HashMap<ToolMaterial, AQItemSpear>();
	public static final HashMap<ToolMaterial, AQItemMace> maces = new HashMap<ToolMaterial, AQItemMace>();
	
	public static void init()
	{
		magicSphere = new AQItemMagicSphere();
		spell = new AQItemSpell();
		regionWand = new AQItemRegionWand();
		totem = new AQItemTotem();
		
		for(ToolMaterial material : materials)
		{
			spears.put(material, new AQItemSpear(material));
			maces.put(material, new AQItemMace(material));
		}
		
		emptyArmor = new AQItemEmptyArmor();
		
		for(ListIterator<Item> it = items.listIterator(); it.hasNext();)
		{
			Item I = it.next();
			String Name = I.getUnlocalizedName();
			
			I.setCreativeTab(AQCreativeTabs.instance);
			I.setTextureName(AQModInfo.MODID + ":" + Name);
			GameRegistry.registerItem(I, Name);
		}
	}
	
	public static void addItem(Item item)
	{
		items.add(item);
	}
	
	public static void addSpecialItem(AQSpecialRenderItem item)
	{
		specialItems.add(item);
	}
	
	@SideOnly(Side.CLIENT)
	public static void registerRenderers()
	{
		for(Iterator<AQSpecialRenderItem> it = specialItems.iterator(); it.hasNext();)
		{
			AQSpecialRenderItem itemRender = it.next();
			Item item = (Item) itemRender;
			MinecraftForgeClient.registerItemRenderer(item, itemRender.getRenderer());
			item.setFull3D();
		}
	}
}
