package aqua.rpgmod.client.gui.dialog;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class AQEditListDialog<T> extends AQListDialog<T>
{
	public GuiButton use;
	public GuiButton delete;
	
	public AQEditListDialog(GuiScreen owner, T ... elements)
	{
		super(owner, elements);
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		
		this.use = new GuiButton(1, this.guiLeft + (this.xSize - 220) / 2, this.guiTop + this.ySize - 40, 100, 20, I18n.format("menu.use"));
		this.buttonList.add(this.use);
		this.delete = new GuiButton(2, this.guiLeft + (this.xSize - 220) / 2 + 120, this.guiTop + this.ySize - 40, 100, 20, I18n.format("menu.delete"));
		this.buttonList.add(this.delete);
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
			case 2:
				if(onDelete(this.selectedIndex))
				{
					this.elements.remove(this.selectedIndex);
					this.selectedIndex = Math.min(this.selectedIndex, this.elements.size() - 1);
				}
				
				break;
		}
	}
}
