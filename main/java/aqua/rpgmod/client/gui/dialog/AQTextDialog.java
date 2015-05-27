package aqua.rpgmod.client.gui.dialog;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class AQTextDialog extends AQOkDialog
{
	GuiTextField textField = null;
	String textContents = "";
	
	public AQTextDialog(GuiScreen owner, String text, Object ... objects)
	{
		super(owner, text, objects);
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		
		if(this.textField == null)
		{
			this.textField = new GuiTextField(this.mc.fontRenderer, this.guiLeft + (this.xSize - 100) / 2, this.guiTop + this.ySize - 64, 100, 20);
		}
		
		this.textField.setFocused(true);
		this.textField.setText(this.textContents);
	}
	
	public int getTextHeight()
	{
		return this.ySize - 64;
	}
	
	@Override
	protected void keyTyped(char p_73869_1_, int p_73869_2_)
	{
		super.keyTyped(p_73869_1_, p_73869_2_);
		this.textField.textboxKeyTyped(p_73869_1_, p_73869_2_);
		this.textContents = this.textField.getText();
	}
	
	@Override
	protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_)
	{
		super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
		this.textField.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
	}
	
	@Override
	public void drawForegroundLayer(int p_73863_1_, int p_73863_2_, float p_73863_3_)
	{
		super.drawForegroundLayer(p_73863_1_, p_73863_2_, p_73863_3_);
		this.textField.drawTextBox();
	}
	
	public void setText(String text)
	{
		this.textContents = text;
		
		if(this.textField != null)
			this.textField.setText(this.textContents);
	}
	
	public String getText()
	{
		return this.textContents;
	}
}
