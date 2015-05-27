package aqua.rpgmod.client.gui;

import aqua.rpgmod.client.gui.craft.AQAnvilGui;
import aqua.rpgmod.client.gui.craft.AQWorkbenchGui;
import aqua.rpgmod.client.gui.menu.AQMenuPage;
import aqua.rpgmod.client.gui.menu.AQModMenu;
import aqua.rpgmod.inventory.AQAnvilContainer;
import aqua.rpgmod.inventory.AQWorkbenchContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class AQGuiHandler implements IGuiHandler
{
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if(ID == AQAnvilContainer.ID)
			return new AQAnvilContainer(player.inventory, world, x, y, z);
		
		if(ID == AQWorkbenchContainer.ID)
			return new AQWorkbenchContainer(player.inventory, world, x, y, z);
		
		return null;
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if(ID == AQAnvilContainer.ID)
			return new AQAnvilGui(player.inventory, world, x, y, z);
		
		if(ID == AQWorkbenchContainer.ID)
			return new AQWorkbenchGui(player.inventory, world, x, y, z);
		
		AQMenuPage page = AQModMenu.instance.getById(ID);
		
		if(page != null)
			return page;
		
		return null;
	}
}
