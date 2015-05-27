package aqua.rpgmod.client.editor;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.common.config.Property.Type;
import aqua.common.web.WebTarget;
import aqua.rpgmod.AquaMod;
import aqua.rpgmod.character.AQCharacterManager;
import aqua.rpgmod.character.AQRace;
import aqua.rpgmod.client.gui.dialog.AQAcceptDialog;
import aqua.rpgmod.client.gui.dialog.AQEditListDialog;
import aqua.rpgmod.client.gui.dialog.AQOkDialog;
import aqua.rpgmod.client.gui.dialog.AQOkDialog.Result;
import aqua.rpgmod.client.gui.dialog.AQTextDialog;
import aqua.rpgmod.service.network.AQWebSettings;
import aqua.rpgmod.service.resources.AQFileResource;

public class AQSkinManager
{
	public static AQSkinManager instance = null;
	public static boolean cycleSelectors = true;
	
	public AQSkin currentSkin = null;
	public AQRaceSkinGroup currentSkinGroup = null;
	protected AQFileResource savesDir = new AQFileResource("skinsaves");
	
	public boolean error = false;
	protected BufferedImage skinImage;
	
	protected static void load()
	{
		try
		{
			instance = new AQSkinManager();
		}
		catch(Exception e)
		{
			instance = null;
			e.printStackTrace();
		}
	}
	
	public static void unload()
	{
		if(instance == null)
			return;
		
		ConfigCategory cat = AquaMod.config.getCategory("skin");
		
		if(instance.currentSkin == null)
			cat.remove("name");
		else
			cat.put("name", new Property("name", instance.currentSkin.name, Type.STRING));
		
		AquaMod.config.save();
		instance = null;
	}
	
	public static void reload()
	{
		unload();
		load();
	}
	
	protected AQSkinManager() throws Exception
	{
		updateSkinGroup();
		
		if(!this.savesDir.getFile().exists())
		{
			this.savesDir.getFile().mkdirs();
			return;
		}
		
		ConfigCategory cat = AquaMod.config.getCategory("skin");
		Property last = cat.get("name");
		
		if(last != null)
			this.currentSkin = new AQSkin(last.getString());
	}
	
	public void updateSkinGroup()
	{
		AQRace race = AQCharacterManager.instance.race;
		
		if(race == null || this.currentSkinGroup.race == race)
			return;
		
		try
		{
			this.currentSkinGroup = new AQRaceSkinGroup(race);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public BufferedImage drawSkin()
	{
		this.skinImage = new BufferedImage(128, 64, 2);
		this.currentSkinGroup.layers.drawTo(this.skinImage.getGraphics());
		return this.skinImage;
	}
	
	public AQFileResource getSkinSavesDir()
	{
		return this.savesDir.append(AQCharacterManager.instance.race.name);
	}
	
	public SSTree getTree()
	{
		return this.currentSkinGroup == null ? null : this.currentSkinGroup.tree;
	}
	
	public SSLayers getLayers()
	{
		return this.currentSkinGroup == null ? null : this.currentSkinGroup.layers;
	}
	
	public SSPages getPages()
	{
		return this.currentSkinGroup == null ? null : this.currentSkinGroup.pages;
	}
	
	public static boolean loopedSelection()
	{
		return true;
	}
	
	public static void upload(final File skin, final String part)
	{
		Thread t = new Thread()
		{
			public void run()
			{
				try
				{
					WebTarget target = new WebTarget(new URL(AQWebSettings.domain + "skin.php"));
					
					target.post(new Object[]
					{
						"username", AQCharacterManager.instance.username, "token", AQCharacterManager.instance.token, "part", part, "ufile", skin
					});
					
					String response = target.getResponse();
					
					boolean error = !response.contains("ok");
					
					if(error)
						new AQOkDialog(AQCharacterManager.instance.getCurrentGui(), "Ошибка сервера\nПопробуйте отправить скин в другой раз");
					else
						new AQOkDialog(AQCharacterManager.instance.getCurrentGui(), "Скин успешно отправлен на сервер");
					
					skin.delete();
					interrupt();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		};
		
		t.setName("Upload thread");
		t.start();
	}
	
	public void uploadSkin()
	{
		try
		{
			upload(this.currentSkin.saveImage().getFile(), "skin");
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void saveSkin()
	{
		if(this.currentSkin == null)
		{
			saveSkinAs();
			return;
		}
		
		try
		{
			this.currentSkin.save();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void saveSkinAs()
	{
		while(true)
		{
			String s = this.currentSkin != null ? this.currentSkin.name : "";
			AQTextDialog dialog1 = new AQTextDialog(AQCharacterManager.instance.getCurrentGui(), "Сохранить как...", s);
			dialog1.show();
			
			if(dialog1.getResult() == Result.CLOSE)
				return;
			
			String newName = dialog1.getText();
			
			if(!testName(newName))
				continue;
			
			if(this.currentSkin != null && newName.equals(this.currentSkin.name))
			{
				AQAcceptDialog dialog2 = new AQAcceptDialog(
					AQCharacterManager.instance.getCurrentGui(),
					"Новое имя совпадает с именем текущего скина! Перезаписать?");
				dialog2.show();
				
				if(dialog2.getResult() != AQAcceptDialog.Result.YES)
					continue;
			}
			else
			{
				AQFileResource path = this.savesDir.append(newName);
				
				if(path.getFile().exists())
				{
					AQAcceptDialog dialog2 = new AQAcceptDialog(AQCharacterManager.instance.getCurrentGui(), "Сохранение уже существует.\nПерезаписать?");
					dialog2.show();
					
					if(dialog2.getResult() != AQAcceptDialog.Result.YES)
						continue;
					
					path.delete();
				}
				
				path.getFile().mkdir();
			}
			
			this.currentSkin = new AQSkin(newName);
			break;
		}
		
		saveSkin();
	}
	
	public void renameSkin()
	{
		if(this.currentSkin == null)
		{
			new AQOkDialog(AQCharacterManager.instance.getCurrentGui(), "Текущий скин не сохранен!");
			return;
		}
		
		while(true)
		{
			AQTextDialog dialog1 = new AQTextDialog(AQCharacterManager.instance.getCurrentGui(), "Переименовать в...", this.currentSkin.name);
			dialog1.show();
			
			if(dialog1.getResult() == Result.CLOSE)
				return;
			
			String newName = dialog1.getText();
			
			if(!testName(newName))
				continue;
			
			if(newName.equals(this.currentSkin.name))
			{
				new AQOkDialog(AQCharacterManager.instance.getCurrentGui(), "Новое имя совпадает с именем текущего скина!");
				continue;
			}
			
			AQFileResource path = this.savesDir.append(newName);
			
			if(path.getFile().exists())
			{
				AQAcceptDialog dialog2 = new AQAcceptDialog(AQCharacterManager.instance.getCurrentGui(), "Сохранение уже существует.\nПерезаписать?");
				dialog2.show();
				
				if(dialog2.getResult() != AQAcceptDialog.Result.YES)
					continue;
				
				path.delete();
			}
			
			this.currentSkin.rename(newName);
			break;
		}
	}
	
	public static boolean testName(String name)
	{
		if(name.isEmpty())
		{
			new AQOkDialog(AQCharacterManager.instance.getCurrentGui(), "Имя сохранения не должно быть пустым!");
			return false;
		}
		else if(name.length() >= 20)
		{
			new AQOkDialog(AQCharacterManager.instance.getCurrentGui(), "Имя сохранения не должно быть длиннее 20 символов!");
			return false;
		}
		else if(!name.matches("[0-9A-Za-z]*"))
		{
			new AQOkDialog(AQCharacterManager.instance.getCurrentGui(), "Имя скина должно содержать только\nсимволы латинского алфавита или цифры!");
			return false;
		}
		
		return true;
	}
	
	public String[] getSkinSaves()
	{
		File[] fileList = this.savesDir.getFile().listFiles();
		String[] skins = new String[fileList.length];
		
		for(int i = 0; i < skins.length; i++)
		{
			skins[i] = fileList[i].getName();
		}
		
		return skins;
	}
	
	public void listSkins()
	{
		String[] skins = getSkinSaves();
		
		AQEditListDialog<String> dialog = new AQEditListDialog<String>(AQCharacterManager.instance.getCurrentGui(), skins)
		{
			@Override
			public boolean onUse(int index)
			{
				if(!super.onUse(index))
					return false;
				
				AQSkinManager.this.currentSkin = new AQSkin(this.selectedElement());
				
				try
				{
					AQSkinManager.this.currentSkin.load();
					return true;
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
				
				return false;
			}
			
			@Override
			public boolean onDelete(int index)
			{
				if(!super.onDelete(index))
					return false;
				
				String s = "Вы уверены, что хотите удалить выбранный скин?";
				
				AQAcceptDialog dialog1 = new AQAcceptDialog(AQCharacterManager.instance.getCurrentGui(), s);
				dialog1.show();
				
				if(dialog1.getResult() != AQAcceptDialog.Result.YES)
					return false;
				
				new AQSkin(this.selectedElement()).delete();
				return true;
			}
		};
		
		dialog.use.displayString = "Загрузить";
		
		if(this.currentSkin != null)
			dialog.select(this.currentSkin.name);
		
		dialog.show();
	}
}
