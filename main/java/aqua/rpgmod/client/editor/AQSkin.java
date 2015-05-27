package aqua.rpgmod.client.editor;

import static aqua.rpgmod.service.AQLogger.log;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.Level;

import aqua.common.data.GuardUtils;
import aqua.rpgmod.character.AQCharacterManager;
import aqua.rpgmod.client.gui.dialog.AQOkDialog;
import aqua.rpgmod.service.resources.AQFileResource;
import aqua.rpgmod.service.resources.AQResource;

public class AQSkin
{
	public String name;
	
	public AQSkin(String name)
	{
		this.name = name;
	}
	
	AQFileResource getResource()
	{
		return AQSkinManager.instance.getSkinSavesDir().append(this.name);
	}
	
	public boolean load() throws IOException
	{
		AQFileResource resource = getResource().append("save");
		
		if(!resource.canRead())
		{
			new AQOkDialog(AQCharacterManager.instance.getCurrentGui(), "Не удалось найти файл конфигурации!\nСкин будет удален!");
			
			log("Can't load skin! Can't find config file!");
			delete();
			return false;
		}
		
		DataInputStream input = new DataInputStream(resource.getInput());
		
		String md5 = AQResource.readString(input);
		AQFileResource treeResource = AQSkinManager.instance.currentSkinGroup.treeResource();
		
		if(!md5.equals(GuardUtils.getMD5(treeResource.getInput(), treeResource.path)))
		{
			new AQOkDialog(
				AQCharacterManager.instance.getCurrentGui(),
				"Не удалось корректно загрузить скин!\nНесоответствие контрольной суммы!\nСкин будет удален!");
			
			log("Can't load skin configuration! Hash - " + md5);
			input.close();
			delete();
			return false;
		}
		
		String tree = AQResource.readString(input);
		
		AQSkinManager.instance.getTree().init(tree);
		AQSkinManager.instance.getTree().update();
		input.close();
		
		log("Skin has been loaded!");
		return true;
	}
	
	public AQFileResource saveImage() throws IOException
	{
		AQSkinManager.instance.drawSkin();
		
		BufferedImage image = new BufferedImage(96, 96, 2);
		
		image.getGraphics().drawImage(AQSkinManager.instance.skinImage.getSubimage(8, 8, 8, 8), 0, 0, 96, 96, null);
		image.getGraphics().drawImage(AQSkinManager.instance.skinImage.getSubimage(40, 8, 8, 8), 0, 0, 96, 96, null);
		
		AQFileResource save = getResource().append("skin.png");
		
		ImageIO.write(AQSkinManager.instance.skinImage, "png", save.getOutput());
		
		return save;
	}
	
	public void save() throws IOException
	{
		AQFileResource resource = getResource().append("save");
		DataOutputStream output = new DataOutputStream(resource.getOutput());
		
		AQFileResource treeResource = AQSkinManager.instance.currentSkinGroup.treeResource();
		AQResource.writeString(output, GuardUtils.getMD5(treeResource.getInput(), treeResource.path));
		
		SSTree tree = AQSkinManager.instance.getTree();
		
		if(tree == null)
		{
			new AQOkDialog(AQCharacterManager.instance.getCurrentGui(), "Не удалось сохранить скин, внутренняя ошибка лаунчера!");
			log(Level.WARN, "Can't save skin! Tree is lost!");
			output.close();
			return;
		}
		
		AQResource.writeString(output, tree.save());
		output.close();
		
		saveImage();
		new AQOkDialog(AQCharacterManager.instance.getCurrentGui(), "Скин сохранен!");
		
		log("Skin has been saved!");
	}
	
	public void rename(String name)
	{
		AQFileResource old = getResource();
		this.name = name;
		
		old.getFile().renameTo(getResource().getFile());
	}
	
	public void delete()
	{
		if(AQSkinManager.instance.currentSkin != null && this.name.equals(AQSkinManager.instance.currentSkin.name))
			AQSkinManager.instance.currentSkin = null;
		
		AQFileResource resource = new AQFileResource("skinsaves", this.name);
		log("Delete skin: " + this.name);
		resource.delete();
	}
}
