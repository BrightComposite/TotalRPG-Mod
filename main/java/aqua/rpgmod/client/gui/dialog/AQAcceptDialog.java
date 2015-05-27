package aqua.rpgmod.client.gui.dialog;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class AQAcceptDialog extends AQInfoDialog
{
	public static enum Result
	{
		YES,
		NO,
		CLOSE;
	}
	
	protected Result result = Result.CLOSE;
	
	public AQAcceptDialog(GuiScreen owner, String text, Object ... objects)
	{
		super(owner, text, objects);
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		this.buttonList.add(new GuiButton(1, this.guiLeft + (this.xSize - 110) / 2, this.guiTop + this.ySize - 40, 50, 20, I18n.format("menu.yes")));
		this.buttonList.add(new GuiButton(2, this.guiLeft + (this.xSize - 110) / 2 + 60, this.guiTop + this.ySize - 40, 50, 20, I18n.format("menu.no")));
	}
	
	@Override
	protected void actionPerformed(GuiButton button)
	{
		if(button.id == 0)
		{
			this.result = Result.CLOSE;
			close();
		}
		else if(button.id == 1)
		{
			this.result = Result.YES;
			close();
		}
		else if(button.id == 2)
		{
			this.result = Result.NO;
			close();
		}
	}
	
	public Result getResult()
	{
		return this.result;
	}
}
