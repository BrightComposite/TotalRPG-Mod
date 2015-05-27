package aqua.rpgmod.service.handlers;

import net.minecraft.client.Minecraft;
import aqua.rpgmod.client.gui.menu.AQModMenu;
import aqua.rpgmod.player.AQPlayerControllerMP;
import aqua.rpgmod.service.AQKeyBinding;
import aqua.rpgmod.service.proxy.AQClientProxy;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class AQFMLClientHandler
{
	@SideOnly(Side.CLIENT)
	@SuppressWarnings("static-method")
	@SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
	public void onEvent(ClientTickEvent event)
	{
		Minecraft mc = Minecraft.getMinecraft();
		AQPlayerControllerMP ctrl = (AQPlayerControllerMP) mc.playerController;
		
		if(ctrl == null)
			return;
		
		ctrl.updateRayTrace();
		
		if(event.phase == TickEvent.Phase.END)
		{
			if(mc.gameSettings.keyBindInventory.isPressed())
			{
				AQModMenu.instance.selectPage(1);
			}
			else
			{
				for(AQKeyBinding bind : AQClientProxy.keyBindings)
				{
					if(bind.isPressed())
						bind.onPress();
				}
			}
		}
	}
}
