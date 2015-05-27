package aqua.rpgmod.client.editor;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ListIterator;

import javax.imageio.ImageIO;

public class SSLayer
{
	public String name;
	protected List<SSNode> nodes;
	public BufferedImage image;
	
	public SSLayer(String name, List<SSNode> nodes)
	{
		this.name = name;
		this.nodes = nodes;
		
		if(nodes.isEmpty())
		{
			File F = new File(AQSkinManager.instance.currentSkinGroup.resource.getFile(), buildPath() + ".png");
			
			if(!F.exists())
			{
				Exception e = new Exception("Can't find image at" + F.getAbsolutePath() + "!");
				e.printStackTrace();
				System.exit(1);
				return;
			}
			
			try
			{
				this.image = ImageIO.read(F);
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public String buildPath()
	{
		if(this.nodes.isEmpty())
		{
			return this.name;
		}
		
		String Path = this.name + File.separator;
		
		for(ListIterator<SSNode> i = this.nodes.listIterator(); i.hasNext();)
			Path += i.next().getBranch(File.separator);
		
		Path = Path.substring(0, Path.length() - 1);
		
		return Path;
	}
	
	public boolean isDependent(SSNode Node)
	{
		for(ListIterator<SSNode> i = this.nodes.listIterator(); i.hasNext();)
			if(i.next() == Node)
				return true;
		
		return false;
	}
	
	public void update(SSNode Node)
	{
		if(!isDependent(Node))
			return;
		
		File F = new File(AQSkinManager.instance.currentSkinGroup.resource.getFile(), buildPath() + ".png");
		
		if(!F.exists())
		{
			Exception e = new Exception("Can't find image at" + F.getAbsolutePath() + "!");
			e.printStackTrace();
			System.exit(1);
			return;
		}
		
		try
		{
			this.image = ImageIO.read(F);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void drawTo(Graphics g)
	{
		g.drawImage(this.image, 0, 0, null);
	}
}
