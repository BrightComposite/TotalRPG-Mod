package aqua.rpgmod.service.interaction;

import java.util.ArrayList;
import java.util.Iterator;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockBrewingStand;
import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockCommandBlock;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockDropper;
import net.minecraft.block.BlockEnchantmentTable;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockRedstoneComparator;
import net.minecraft.block.BlockRedstoneRepeater;
import net.minecraft.block.BlockWorkbench;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import aqua.rpgmod.item.AQItemRegionWand;
import aqua.rpgmod.player.AQPlayerControllerMP;

public class AQInteractManager
{
	public static final ArrayList<AQInteractor> itemInteractors = new ArrayList<AQInteractor>();
	public static final ArrayList<AQInteractor> blockInteractors = new ArrayList<AQInteractor>();
	public static final ArrayList<AQInteractor> entityInteractors = new ArrayList<AQInteractor>();
	
	static
	{
		itemInteractors.add(new AQInteractor(AQItemRegionWand.class, ""));
		
		blockInteractors.add(new AQInteractor(BlockAnvil.class, "interact.use"));
		blockInteractors.add(new AQInteractor(BlockWorkbench.class, "interact.use"));
		blockInteractors.add(new AQInteractor(BlockBrewingStand.class, "interact.use"));
		blockInteractors.add(new AQInteractor(BlockEnchantmentTable.class, "interact.use"));
		blockInteractors.add(new AQInteractor(BlockLever.class, "interact.use"));
		blockInteractors.add(new AQInteractor(BlockButton.class, "interact.use"));
		blockInteractors.add(new AQInteractor(BlockFurnace.class, "interact.open"));
		blockInteractors.add(new AQInteractor(BlockCommandBlock.class, "interact.open"));
		blockInteractors.add(new AQInteractor(BlockChest.class, "interact.open"));
		blockInteractors.add(new AQInteractor(BlockEnderChest.class, "interact.open"));
		blockInteractors.add(new AQInteractor(BlockDispenser.class, "interact.open"));
		blockInteractors.add(new AQInteractor(BlockDropper.class, "interact.open"));
		blockInteractors.add(new AQInteractor(BlockRedstoneRepeater.class, "interact.switch"));
		blockInteractors.add(new AQInteractor(BlockRedstoneComparator.class, "interact.switch"));
		blockInteractors.add(new AQInteractor(BlockBed.class, "interact.sleep"));
		blockInteractors.add(new AQFlowerPotInteractor(BlockFlowerPot.class));
		blockInteractors.add(new AQDoorInteractor());
		blockInteractors.add(new AQFenceGateInteractor());
		
		entityInteractors.add(new AQInteractor(EntityVillager.class, "interact.trade"));
		entityInteractors.add(new AQInteractor(IInventory.class, "interact.open"));
		entityInteractors.add(new AQFeedInteractor());
		entityInteractors.add(new AQPigInteractor());
		entityInteractors.add(new AQHorseInteractor());
		entityInteractors.add(new AQTameInteractor());
		entityInteractors.add(new AQTransportInteractor(EntityBoat.class));
		entityInteractors.add(new AQTransportInteractor(EntityMinecartEmpty.class));
	}
	
	public static AQInteractionInfo getInteractionInfo(AQRayTraceInfo rayTrace)
	{
		ItemStack stack = rayTrace.getEquippedItem();
		
		if(rayTrace.type == MovingObjectType.BLOCK)
		{
			for(Iterator<AQInteractor> i = blockInteractors.iterator(); i.hasNext();)
			{
				AQInteractor interactor = i.next();
				int id = interactor.getBlockInteractionId(rayTrace, stack);
				
				if(id != -1)
					return new AQInteractionInfo(rayTrace, interactor, id);
			}
		}
		else
		{
			for(Iterator<AQInteractor> i = entityInteractors.iterator(); i.hasNext();)
			{
				AQInteractor interactor = i.next();
				int id = interactor.getEntityInteractionId(rayTrace, stack);

				if(id != -1)
					return new AQInteractionInfo(rayTrace, interactor, id);
			}
		}
		
		return null;
	}
	
	@SideOnly(Side.CLIENT)
	public static String getInteractionDescription(Minecraft mc)
	{
		if(mc == null || mc.objectMouseOver == null)
			return "";
		
		AQPlayerControllerMP ctrl = (AQPlayerControllerMP) mc.playerController;
		AQRayTraceInfo rayTrace = ctrl.rayTrace;
		
		if(rayTrace.interactionInfo == null)
			return "";
		
		return rayTrace.interactionInfo.getDesc();
	}
}
