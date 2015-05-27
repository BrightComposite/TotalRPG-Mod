package aqua.rpgmod.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import aqua.rpgmod.craft.AQCraft;
import aqua.rpgmod.craft.AQHashItemStack;

public class AQSlotPlayerCrafting extends SlotCrafting
{
	public final AQContainerPlayer container;
	
	public AQSlotPlayerCrafting(AQContainerPlayer container, int p_i1823_4_, int p_i1823_5_, int p_i1823_6_)
	{
		super(container.thePlayer, container.craftMatrix, container.craftResult, p_i1823_4_, p_i1823_5_, p_i1823_6_);
		this.container = container;
	}
	
	@Override
	public boolean canTakeStack(EntityPlayer p_82869_1_)
	{
		return (this.container.result != null) ? this.container.result.availiable : false;
	}
	
	@Override
	public ItemStack getStack()
	{
		return (this.container.result == null) ? super.getStack() : (this.container.result.availiable) ? super.getStack() : null;
	}
	
	@Override
	public void onPickupFromSlot(EntityPlayer player, ItemStack p_82870_2_)
	{
		if(this.container.result != null)
		{
			AQHashItemStack[] stacks = AQCraft.genBuffer(this.container.craftMatrix.getSizeInventory(), 1);
			
			for(int i = 0; i < stacks.length; i++)
				stacks[i] = new AQHashItemStack(this.container.craftMatrix.getStackInSlot(i));
			
			this.container.result.onPickup(this.container, stacks);
		}
		
		super.onPickupFromSlot(player, p_82870_2_);
	}
}
