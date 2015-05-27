package aqua.rpgmod.client.gui.menu;

import java.util.List;

import aqua.rpgmod.client.gui.AQGuiButton;
import aqua.rpgmod.client.gui.AQImageButton;
import aqua.rpgmod.client.gui.menu.skills.AQAbilityGroupPage;
import aqua.rpgmod.player.rpg.AQArchery;
import aqua.rpgmod.player.rpg.AQHarvesting;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;

public class AQSkillsMenu extends AQMenuPage
{
	public AQSkillsMenu()
	{
		super("skills", 16);
		
		addPage(new AQAbilityGroupPage(AQHarvesting.instance));
		addPage(new AQAbilityGroupPage(AQArchery.instance));
	}
	
	@Override
	protected void onAddPage(AQMenuPage page, int index)
	{
		if(index == 1)
		{
			AQImageButton prev = new AQImageButton(this, 0, 0, 0, 20, 20);
			
			prev.setHint(I18n.format("gui.skills.previous"));
			// prev.image = new ResourceLocation(AQModInfo.MODID + ":" +
			// "textures/gui/buttons/previous.png");
			prev.imageWidth = 16;
			prev.imageHeight = 16;
			
			AQImageButton next = new AQImageButton(this, 1, 0, 0, 20, 20);
			
			next.setHint(I18n.format("gui.skills.next"));
			// next.image = new ResourceLocation(AQModInfo.MODID + ":" +
			// "textures/gui/buttons/next.png");
			next.imageWidth = 16;
			next.imageHeight = 16;
			
			this.buttons.add(prev);
			this.buttons.add(next);
		}
	}
	
	@Override
	protected void setButtons(int x, int y, List buttonList)
	{
		if(!this.buttons.isEmpty())
		{
			AQGuiButton prev = this.buttons.get(0);
			AQGuiButton next = this.buttons.get(1);
			
			prev.xPosition = x + 20;
			prev.yPosition = y + 20;
			next.xPosition = x + this.xSize - 40;
			next.yPosition = y + 20;
		}
		
		super.setButtons(x, y, buttonList);
	}
	
	@Override
	public void actionPerformed(GuiButton button)
	{
		while(button instanceof AQGuiButton)
		{
			AQGuiButton b = (AQGuiButton) button;
			
			if(b.owner != this)
				break;
			
			if(b.id == 1)
				selectPage((this.currentChild + 1) % this.children.size());
			else
				selectPage((this.currentChild - 1 < 0) ? (this.children.size() - 1) : (this.currentChild - 1));
			
			return;
		}
		
		super.actionPerformed(button);
	}
}
