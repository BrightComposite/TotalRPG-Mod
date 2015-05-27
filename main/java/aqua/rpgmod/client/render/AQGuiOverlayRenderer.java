package aqua.rpgmod.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import aqua.rpgmod.client.gui.bars.AQBarArranger;
import aqua.rpgmod.client.gui.bars.AQBarRenderer;
import aqua.rpgmod.client.gui.bars.AQSimpleBarRenderer;
import aqua.rpgmod.client.gui.indicators.AQIndicators;
import aqua.rpgmod.service.interaction.AQInteractManager;
import aqua.rpgmod.service.proxy.AQClientProxy;

public class AQGuiOverlayRenderer extends Gui
{
	public static final AQGuiOverlayRenderer instance = new AQGuiOverlayRenderer();
	
	public AQBarRenderer barRenderer = null;
	protected Minecraft mc;
	
	public void init(Minecraft mc)
	{
		this.barRenderer = new AQSimpleBarRenderer(mc);
		this.mc = mc;
	}
	
	public void render(int width, int height)
	{
		if(!this.mc.thePlayer.capabilities.isCreativeMode)
		{
			this.barRenderer.setArranger(AQBarArranger.gameArranger);
			this.barRenderer.renderAll(0, 0, width, height);
		}
		
		AQIndicators.render(width, height);
		
		String desc = AQInteractManager.getInteractionDescription(this.mc);
		
		if(!desc.isEmpty())
			this.drawCenteredString(
				this.mc.fontRenderer,
				I18n.format(desc) + " (" + GameSettings.getKeyDisplayString(AQClientProxy.keyBindInteract.getKeyCode()) + ")",
				width / 2,
				height / 2 + 5,
				14737632);
	}
}
