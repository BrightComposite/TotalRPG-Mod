package aqua.rpgmod.service.interaction;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class AQFlowerPotInteractor extends AQInteractor
{
	public AQFlowerPotInteractor(Class cl)
	{
		super(cl, "interact.plant");
	}
	
	private static boolean isPlant(Item item)
	{
		return item == Item.getItemFromBlock(Blocks.red_flower) || item == Item.getItemFromBlock(Blocks.yellow_flower)
			|| item == Item.getItemFromBlock(Blocks.sapling) || item == Item.getItemFromBlock(Blocks.red_mushroom)
			|| item == Item.getItemFromBlock(Blocks.brown_mushroom) || item == Item.getItemFromBlock(Blocks.cactus)
			|| item == Item.getItemFromBlock(Blocks.deadbush) || item == Item.getItemFromBlock(Blocks.tallgrass);
	}
	
	@Override
	public int getBlockInteractionId(AQRayTraceInfo rayTrace, ItemStack stack)
	{
		if(super.getBlockInteractionId(rayTrace, stack) == -1)
			return -1;
		
		return stack != null && isPlant(stack.getItem()) ? 0 : -1;
	}
}
