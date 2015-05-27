package aqua.rpgmod.client.gui.editor;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import aqua.rpgmod.character.AQCharacterManager;
import aqua.rpgmod.character.AQRace;
import aqua.rpgmod.client.gui.AQGuiMainMenu;
import aqua.rpgmod.client.gui.dialog.AQOkDialog;
import aqua.rpgmod.client.gui.dialog.AQRaceDialog;
import aqua.rpgmod.client.gui.dialog.AQTextDialog;
import aqua.rpgmod.service.AQStringValidator;

public class AQGuiCreateCharacter extends AQGuiMainMenu
{
	GuiButton setName = null;
	GuiButton selectRace;
	AQRace selectedRace = null;
	boolean nameSet = true;
	
	public AQGuiCreateCharacter()
	{	
		
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		
		this.buttonList.clear();
		
		int buttonx = (this.width - 200) / 2;
		
		int i = 48;
		
		if(this.charManager.hasCharacter)
		{
			this.setName = new GuiButton(0, buttonx, i, I18n.format("menu.setname"));
			i += 24;
			this.buttonList.add(this.setName);
			this.nameSet = false;
		}
		
		this.selectRace = new GuiButton(1, buttonx, i, I18n.format("menu.selectrace"));
		i += 24;
		
		GuiButton create = new GuiButton(2, buttonx, i, I18n.format("menu.create"));
		i += 36;
		GuiButton back = new GuiButton(3, buttonx, i, I18n.format("menu.back"));
		
		this.buttonList.add(this.selectRace);
		this.buttonList.add(create);
		this.buttonList.add(back);
	}
	
	@Override
	protected void actionPerformed(GuiButton button)
	{
		switch(button.id)
		{
			case 0:
				AQTextDialog dialog = new AQTextDialog(this, I18n.format("menu.setname"))
				{
					@Override
					public void onClose()
					{
						if(this.result == Result.OK)
						{
							String name = this.getText().trim();
							String[] validation = AQStringValidator.validateUsername(name);
							
							if(validation != null)
							{
								AQGuiCreateCharacter.this.nameSet = false;
								new AQOkDialog(this, I18n.format(validation[0], AQStringValidator.getVargs(validation))).show();
								return;
							}
							
							AQGuiCreateCharacter.this.nameSet = true;
							AQGuiCreateCharacter.this.setName.displayString = name;
						}
					}
				};
				
				dialog.setText(AQCharacterManager.instance.name);
				dialog.show();
				break;
			case 1:
				AQRaceDialog racedialog = new AQRaceDialog(this)
				{
					@Override
					public boolean onUse(int index)
					{
						if(!super.onUse(index))
							return false;
						
						if(this.selectedElement() == null)
							return false;
						
						AQGuiCreateCharacter.this.selectedRace = this.selectedElement();
						AQGuiCreateCharacter.this.selectRace.displayString = this.getElementDisplayName(index);
						
						return true;
					}
				};
				
				racedialog.select(AQGuiCreateCharacter.this.selectedRace);
				racedialog.show();
				
				break;
			case 2:
				String name = this.setName == null ? this.charManager.username : this.nameSet ? this.setName.displayString : null;
				
				if(this.selectedRace == null)
				{
					new AQOkDialog(this, "menu.mustselectrace").show();
					break;
				}
				
				if(name == null || name.isEmpty())
				{
					new AQOkDialog(this, "menu.mustsetname").show();
					break;
				}
				
				String result = this.charManager.createCharacter(name, this.selectedRace);
				
				if(result.equals("ok"))
				{
					GuiScreen screen = new AQGuiMainMenu();
					this.mc.displayGuiScreen(screen);
					new AQOkDialog(screen, "menu.charcreated").show();
					return;
				}
				
				if(result.equals("max"))
				{
					new AQOkDialog(this, "menu.charmax").show();
					return;
				}
				
				if(result.equals("wrongname"))
				{
					new AQOkDialog(this, "menu.wrongname").show();
					return;
				}
				
				if(result.equals("samename"))
				{
					new AQOkDialog(this, "menu.samename").show();
					return;
				}
				
				if(result.equals("error"))
				{
					new AQOkDialog(this, "menu.servererror").show();
					return;
				}
				
				break;
			case 3:
				this.mc.displayGuiScreen(new AQGuiMainMenu());
				break;
		}
	}
	
	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_)
	{
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}
}
