package aqua.rpgmod.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import aqua.rpgmod.craft.AQCraft;
import aqua.rpgmod.craft.AQCraftResult;
import aqua.rpgmod.craft.AQPlayerCraft;
import aqua.rpgmod.item.AQItems;
import aqua.rpgmod.player.AQThisPlayerWrapper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class AQContainerPlayer extends ContainerPlayer
{
	protected EntityPlayer thePlayer;
	protected int disabledArmor = 0;
	
	public AQCraft craft = null;
	public AQCraftResult result = null;
	
	protected static String[] parts = new String[]
	{
		"helmet", "chest", "pants", "boots"
	};
	
	public AQContainerPlayer(final InventoryPlayer invPlayer, boolean b, EntityPlayer entityPlayer)
	{
		super(invPlayer, b, entityPlayer);
		this.thePlayer = entityPlayer;
		this.craft = AQPlayerCraft.instance;
		this.inventorySlots.set(0, new AQSlotPlayerCrafting(this, 0, 144, 36));
	}
	
	public static class DisabledArmorSlot extends Slot
	{
		final ItemStack stack = new ItemStack(AQItems.emptyArmor);
		
		public DisabledArmorSlot(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_)
		{
			super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
		}
		
		@Override
		public ItemStack getStack()
		{
			return this.stack;
		}
		
		@Override
		public void putStack(ItemStack p_75215_1_)
		{}
		
		@Override
		public boolean canTakeStack(EntityPlayer p_82869_1_)
		{
			return false;
		}
		
		@Override
		public int getSlotStackLimit()
		{
			return 0;
		}
		
		@Override
		public boolean isItemValid(ItemStack s)
		{
			return false;
		}
	}
	
	public void updateSlots(AQThisPlayerWrapper info)
	{
		this.disabledArmor = info.getRace().disabledArmor();
		
		if(this.disabledArmor == 0)
			return;
		
		for(int i = 0; i < 4; i++)
		{
			if(isArmorDisabled(i))
			{
				armorMessage(i);
				DisabledArmorSlot slot = new DisabledArmorSlot(info.player.inventory, info.player.inventory.getSizeInventory() - 1 - i, 8, 8 + i * 18);
				
				this.inventorySlots.set(5 + i, slot);
				this.inventoryItemStacks.set(5 + i, slot.stack);
			}
		}
	}
	
	@Override
	public void onCraftMatrixChanged(IInventory p_75130_1_)
	{
		if(this.craft == null)
			return;
		
		this.result = this.craft.getResult(this.craftMatrix, this.thePlayer, 2, 2);
		
		if(this.result != null)
			this.craftResult.setInventorySlotContents(0, this.result.get());
		else
			this.craftResult.setInventorySlotContents(0, null);
	}
	
	public boolean isArmorDisabled(int armorType)
	{
		int armor = 1 << armorType;
		return (this.disabledArmor & armor) == armor;
	}
	
	@SideOnly(Side.CLIENT)
	public static void armorMessage(int armorType)
	{
		System.out.println("Change " + parts[armorType] + " slot");
	}
}
