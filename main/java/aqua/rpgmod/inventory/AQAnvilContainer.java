package aqua.rpgmod.inventory;

import aqua.rpgmod.craft.AQAnvilCraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class AQAnvilContainer extends AQCraftContainer
{
	public static final int ID = 300;
	
	public AQAnvilContainer(InventoryPlayer p_i1808_1_, World p_i1808_2_, int p_i1808_3_, int p_i1808_4_, int p_i1808_5_)
	{
		super(p_i1808_1_, p_i1808_2_, p_i1808_3_, p_i1808_4_, p_i1808_5_, AQAnvilCraft.instance, Blocks.anvil);
	}
}
