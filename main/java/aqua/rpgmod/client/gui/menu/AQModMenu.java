package aqua.rpgmod.client.gui.menu;

import java.util.Iterator;
import java.util.List;

import aqua.rpgmod.AQModInfo;
import aqua.rpgmod.client.gui.AQGuiButton;
import aqua.rpgmod.client.gui.AQGuiContainerCreative;
import aqua.rpgmod.client.gui.AQImageButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class AQModMenu extends AQMenuPage
{
	public static AQModMenu instance = new AQModMenu(16);
	public static final int buttonId = 1000;
	
	public AQModMenu(int maxPages)
	{
		super("main", maxPages);
	}
	
	@Override
	protected void setButtons(int x, int y, List buttonList)
	{
		int yPos = y + 5;
		
		for(Iterator<AQGuiButton> i = this.buttons.iterator(); i.hasNext();)
		{
			AQGuiButton button = i.next();
			
			if(button instanceof AQImageButton)
			{
				AQMenuPage p = this.children.get(button.id - buttonId);
				
				if(p != null && p instanceof AQGuiContainerCreative && !Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode)
					continue;
			}
			
			button.xPosition = x - 5 - button.width;
			button.yPosition = yPos;
			yPos += button.height + 5;
			
			buttonList.add(button);
		}
	}
	
	@Override
	protected void onAddPage(AQMenuPage page, int index)
	{
		AQImageButton selector = new AQImageButton(this, buttonId + index, 0, 0, 20, 20);
		
		selector.setHint(I18n.format("gui." + page.name));
		selector.image = new ResourceLocation(AQModInfo.MODID + ":" + "textures/gui/buttons/" + page.name + ".png");
		selector.imageWidth = 16;
		selector.imageHeight = 16;
		
		this.buttons.add(selector);
	}
	
	@Override
	public void actionPerformed(GuiButton button)
	{
		int index = button.id - buttonId;
		
		if(index >= 0)
			selectPage(index);
	}
}
