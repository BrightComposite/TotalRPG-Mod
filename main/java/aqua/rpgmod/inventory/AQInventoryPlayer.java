package aqua.rpgmod.inventory;

import aqua.rpgmod.AquaMod;
import aqua.rpgmod.player.AQThisPlayerWrapper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class AQInventoryPlayer extends InventoryPlayer
{
	public boolean currentItemDisabled = false;
	protected ItemStack lastItem = null;
	
	public AQInventoryPlayer(EntityPlayer p_i1750_1_)
	{
		super(p_i1750_1_);
	}
	
	@Override
	public ItemStack getCurrentItem()
	{
		ItemStack stack = super.getCurrentItem();
		
		if(!AquaMod.proxy.isDedicated())
		{
			if(this.lastItem != stack && AQThisPlayerWrapper.getWrapper() != null)
			{
				AQThisPlayerWrapper.getWrapper().onCurrentItemChange(this, stack);
				this.lastItem = stack;
			}
		}
		
		return this.currentItemDisabled ? null : stack;
	}
}
