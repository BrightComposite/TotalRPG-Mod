package aqua.rpgmod.service.interaction;

import net.minecraft.block.Block;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class AQHorseInteractor extends AQInteractor
{
	public AQHorseInteractor()
	{
		super(EntityHorse.class, "");
	}
	
	@Override
	public String getDesc(AQRayTraceInfo info, int id)
	{
		switch(id)
		{
			case 0:
				return "interact.wear";
			case 1:
				return "interact.feed";
			default:
				return "interact.mount";
		}
	}
	
	@Override
	public int getEntityInteractionId(AQRayTraceInfo rayTrace, ItemStack stack)
	{
		if(super.getEntityInteractionId(rayTrace, stack) == -1)
			return -1;
		
		if(stack != null)
		{
			if(stack.getItem() == Items.saddle)
				return 0;
			
			if(stack.getItem() == Items.wheat || stack.getItem() == Items.sugar || stack.getItem() == Items.bread
				|| Block.getBlockFromItem(stack.getItem()) == Blocks.hay_block || stack.getItem() == Items.apple || stack.getItem() == Items.golden_carrot
				|| stack.getItem() == Items.golden_apple)
				return 1;
		}
		
		return rayTrace.getPlayerWrapper().getRace().canMountEntity() ? 2 : -1;
	}
}
