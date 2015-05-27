package aqua.rpgmod.service.interaction;

import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;

public class AQDoorInteractor extends AQInteractor
{
	public AQDoorInteractor()
	{
		super(BlockDoor.class, "");
	}
	
	@Override
	public String getDesc(AQRayTraceInfo info, int id)
	{
		if(id == 0)
			return "interact.open";
		
		return "interact.close";
	}
	
	@Override
	public int getBlockInteractionId(AQRayTraceInfo rayTrace, ItemStack stack)
	{
		if(super.getBlockInteractionId(rayTrace, stack) == -1 || rayTrace.getBlock().getMaterial() == Material.iron)
			return -1;
		
		return (((BlockDoor) rayTrace.getBlock()).func_150012_g(rayTrace.mc.theWorld, rayTrace.x, rayTrace.y, rayTrace.z) & 4) == 0 ? 0 : 1;
	}
}
