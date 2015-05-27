package aqua.rpgmod.inventory;

import aqua.rpgmod.craft.AQCraft;
import aqua.rpgmod.craft.AQCraftResult;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.World;

public class AQCraftContainer extends ContainerWorkbench
{
	public final EntityPlayer player;
	
	protected World worldObj;
	protected int posX;
	protected int posY;
	protected int posZ;
	public AQCraft craft = null;
	public AQCraftResult result = null;
	public final Block targetBlock;
	
	public AQCraftContainer(InventoryPlayer p_i1808_1_, World p_i1808_2_, int p_i1808_3_, int p_i1808_4_, int p_i1808_5_, AQCraft craft, Block targetBlock)
	{
		super(p_i1808_1_, p_i1808_2_, p_i1808_3_, p_i1808_4_, p_i1808_5_);
		
		this.player = p_i1808_1_.player;
		this.worldObj = p_i1808_2_;
		this.posX = p_i1808_3_;
		this.posY = p_i1808_4_;
		this.posZ = p_i1808_5_;
		this.craft = craft;
		this.targetBlock = targetBlock;
		
		this.inventorySlots.set(0, new AQSlotCrafting(this, this.craftMatrix, this.craftResult, 0, 124, 35));
	}
	
	@Override
	public void onCraftMatrixChanged(IInventory p_75130_1_)
	{
		if(this.craft == null)
			return;
		
		this.result = this.craft.getResult(this.craftMatrix, this.player, 3, 3);
		
		if(this.result != null)
			this.craftResult.setInventorySlotContents(0, this.result.get());
		else
			this.craftResult.setInventorySlotContents(0, null);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_)
	{
		return this.worldObj.getBlock(this.posX, this.posY, this.posZ) != this.targetBlock ? false : p_75145_1_.getDistanceSq(
			this.posX + 0.5D,
			this.posY + 0.5D,
			this.posZ + 0.5D) <= 64.0D;
	}
}
