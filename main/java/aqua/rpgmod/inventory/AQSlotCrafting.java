package aqua.rpgmod.inventory;

import aqua.rpgmod.craft.AQCraft;
import aqua.rpgmod.craft.AQHashItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class AQSlotCrafting extends SlotCrafting
{
	public final AQCraftContainer container;
	protected final IInventory craftMatrix;
	
	public AQSlotCrafting(AQCraftContainer container, IInventory p_i1823_2_, IInventory p_i1823_3_, int p_i1823_4_, int p_i1823_5_, int p_i1823_6_)
	{
		super(container.player, p_i1823_2_, p_i1823_3_, p_i1823_4_, p_i1823_5_, p_i1823_6_);
		
		this.container = container;
		this.craftMatrix = p_i1823_2_;
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
			AQHashItemStack[] stacks = AQCraft.genBuffer(this.craftMatrix.getSizeInventory(), 1);
			
			for(int i = 0; i < stacks.length; i++)
				stacks[i] = new AQHashItemStack(this.craftMatrix.getStackInSlot(i));
			
			this.container.result.onPickup(this.container, stacks);
			
			World w = player.worldObj;
			
			if(!w.isRemote)
				w.playAuxSFX(1021, this.container.posX, this.container.posY, this.container.posZ, 0);
		}
		
		super.onPickupFromSlot(player, p_82870_2_);
	}
}
