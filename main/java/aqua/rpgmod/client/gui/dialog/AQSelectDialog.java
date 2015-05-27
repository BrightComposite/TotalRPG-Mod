package aqua.rpgmod.client.gui.dialog;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class AQSelectDialog<T> extends AQListDialog<T>
{
	public GuiButton select;
	
	public AQSelectDialog(GuiScreen owner, T ... elements)
	{
		super(owner, elements);
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		
		this.select = new GuiButton(1, this.guiLeft + (this.xSize - 100) / 2, this.guiTop + this.ySize - 40, 100, 20, I18n.format("menu.select"));
		this.buttonList.add(this.select);
	}
	
	public boolean onDelete(int index)
	{
		return isInRange(index);
	}
	
	@Override
	protected void actionPerformed(GuiButton button)
	{
		super.actionPerformed(button);
		
		switch(button.id)
		{
			case 1:
				if(this.elements.size() > 0 && (this.onUse(this.selectedIndex)))
				{
					this.result = Result.USE;
					close();
				}
				
				break;
		}
	}
}
