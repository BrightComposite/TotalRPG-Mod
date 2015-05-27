package aqua.rpgmod.client.gui.menu.skills;

import java.util.Iterator;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import aqua.rpgmod.client.gui.menu.AQMenuPage;
import aqua.rpgmod.player.AQThisPlayerWrapper;
import aqua.rpgmod.player.rpg.AQAbility;
import aqua.rpgmod.player.rpg.AQAbilityGroup;

public class AQAbilityGroupPage extends AQMenuPage
{
	public final AQAbilityGroup group;
	
	public AQAbilityGroupPage(AQAbilityGroup group)
	{
		super("skills");
		this.group = group;
	}
	
	protected void updateButtons()
	{
		int k = 0;
		this.buttons.clear();
		
		for(Iterator<AQAbility> i = this.group.abilityIterator(); i.hasNext();)
		{
			this.buttons.add(new AQAbilityIcon(this, k++, i.next()));
		}
	}
	
	@Override
	protected void setButtons(int x, int y, List buttonList)
	{
		updateButtons();
		super.setButtons(x, y, buttonList);
	}
	
	@Override
	public void drawForeground(int mouseX, int mouseY, float ticks)
	{
		super.drawForeground(mouseX, mouseY, ticks);
		drawCenteredString(this.mc.fontRenderer, I18n.format("skills." + this.group.name), this.guiLeft + this.xSize / 2, this.guiTop + 20, 14737632);
		drawCenteredString(
			this.mc.fontRenderer,
			EnumChatFormatting.GOLD + I18n.format("gui.skills.points") + String.valueOf(AQThisPlayerWrapper.getWrapper().player.experienceLevel),
			this.guiLeft + this.xSize / 2,
			this.guiTop + this.ySize - 20,
			14737632);
	}
	
	@Override
	public void actionPerformed(GuiButton button)
	{
		if(button instanceof AQAbilityIcon)
		{
			AQAbilityIcon icon = (AQAbilityIcon) button;
			icon.ability.increase();
			this.mc.displayGuiScreen(this);
		}
		else
			super.actionPerformed(button);
	}
}
