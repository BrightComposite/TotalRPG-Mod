package aqua.rpgmod.client.editor;

import java.util.ArrayList;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.common.config.Property.Type;
import aqua.common.DownloadThread;
import aqua.common.data.GuardUtils;
import aqua.common.data.ZipUtils;
import aqua.rpgmod.AquaMod;
import aqua.rpgmod.character.AQCharacterManager;
import aqua.rpgmod.client.gui.dialog.AQOkDialog;
import aqua.rpgmod.client.gui.editor.AQGuiCharacter;
import aqua.rpgmod.service.network.AQWebSettings;
import aqua.rpgmod.service.resources.AQFileResource;

public class SSUpdaterThread extends DownloadThread
{
	public SSUpdaterThread(String answer)
	{
		super(new ArrayList<String>(), answer);
	}
	
	public void run()
	{
		AQGuiCharacter gui = AQCharacterManager.instance.getCurrentGui();
		
		try
		{
			AQFileResource destination = new AQFileResource("");
			AQFileResource skins = new AQFileResource("skins/");
			
			this.files.add("skinsystem.zip");
			
			download(AQWebSettings.getFileURL(""), destination.getFile().getAbsolutePath());
			
			ConfigCategory cat = AquaMod.config.getCategory("skin");
			
			cat.put("md5", new Property("md5", GuardUtils.getMD5(destination.getInput(), destination.path), Type.STRING));
			
			if(skins.getFile().exists())
				GuardUtils.delete(skins.getFile());
			
			if(!ZipUtils.unzip(destination.getFile().getAbsolutePath(), skins.getFile().getAbsolutePath(), false))
			{
				new AQOkDialog(gui, "Не удалось распаковать файл скинпака!\nОбратитесь к администраторам!");
				return;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			this.error = true;
		}
		
		new AQOkDialog(gui, "Скинпак был успешно обновлен!");
		interrupt();
	}
	
}
