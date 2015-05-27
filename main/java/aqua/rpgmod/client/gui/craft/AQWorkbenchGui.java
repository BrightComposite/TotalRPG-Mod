package aqua.rpgmod.client.gui.craft;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import aqua.rpgmod.inventory.AQWorkbenchContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class AQWorkbenchGui extends AQCraftGui
{
	private static final ResourceLocation ct = new ResourceLocation("textures/gui/container/crafting_table.png");
	
	public AQWorkbenchGui(InventoryPlayer inventory, World world, int x, int y, int z)
	{
		super("aqua.workbench", new AQWorkbenchContainer(inventory, world, x, y, z));
		setBackground(ct);
	}
}
