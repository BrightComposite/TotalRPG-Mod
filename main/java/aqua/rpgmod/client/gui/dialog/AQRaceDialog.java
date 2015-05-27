package aqua.rpgmod.client.gui.dialog;

import aqua.rpgmod.character.AQRace;
import aqua.rpgmod.character.AQRace.Arachnoid;
import aqua.rpgmod.character.AQRace.Centaurus;
import aqua.rpgmod.character.AQRace.Dwarf;
import aqua.rpgmod.character.AQRace.Elf;
import aqua.rpgmod.character.AQRace.Human;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class AQRaceDialog extends AQSelectDialog<AQRace>
{
	public AQRaceDialog(GuiScreen owner)
	{
		super(owner, Human.instance(), Elf.instance(), Dwarf.instance(), Arachnoid.instance(), Centaurus.instance());
	}
	
	@Override
	public String getElementDisplayName(int index)
	{
		return I18n.format("race." + this.elements.get(index).name);
	}
}
