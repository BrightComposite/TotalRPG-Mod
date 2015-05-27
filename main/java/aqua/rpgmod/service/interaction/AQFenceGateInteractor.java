package aqua.rpgmod.service.interaction;

import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;

public class AQFenceGateInteractor extends AQInteractor
{
	public AQFenceGateInteractor()
	{
		super(BlockFenceGate.class, "");
	}
	
	@Override
	public String getDesc(AQRayTraceInfo info, int id)
	{
		if(id == 0)
			return "interact.close";
		
		return "interact.open";
	}
	
	@Override
	public int getBlockInteractionId(AQRayTraceInfo rayTrace, ItemStack stack)
	{
		if(super.getBlockInteractionId(rayTrace, stack) == -1 || rayTrace.getBlock().getMaterial() == Material.iron)
			return -1;
				
		return BlockFenceGate.isFenceGateOpen(rayTrace.mc.theWorld.getBlockMetadata(rayTrace.x, rayTrace.y, rayTrace.z)) ? 0 : 1;
	}
}
