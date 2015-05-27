package aqua.rpgmod.client.editor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ListIterator;

import aqua.rpgmod.service.AQLogger;
import aqua.rpgmod.service.resources.AQFileResource;

public class SSTree
{
	public SSNode root = new SSNode(this);
	
	public SSTree(AQFileResource resource) throws Exception
	{
		if(!resource.canRead())
		{
			throw new Exception("Can't read pages from " + resource.getFile().getAbsolutePath() + "!");
		}
		
		BufferedReader br = new BufferedReader(new FileReader(resource.getFile()));
		br.skip(1);
		
		SSNode node = this.root;
		int lineIndex = 0;
		
		while(true)
		{
			String line = br.readLine();
			
			if(line == null)
				break;
			
			line = line.trim();
			lineIndex++;
			
			if((line.isEmpty()) || (line.startsWith("#")))
				continue;
			
			if(line.startsWith(">"))
			{
				if(node == this.root)
				{
					br.close();
					throw new Exception("Invalid tree file! End of unknown section found, line " + String.valueOf(lineIndex));
				}
				
				node = node.parent;
				continue;
			}
			
			if(!line.contains("="))
			{
				br.close();
				throw new Exception("Invalid tree file! Error on token: " + line + ",line " + String.valueOf(lineIndex));
			}
			
			String[] info = line.split("=");
			
			String key = info[0];
			String name = info[1];
			
			if(name.endsWith("<"))
			{
				name = name.substring(0, name.length() - 1).trim();
				node = new SSNode(key, name, node, this);
				continue;
			}
			
			if(node == this.root)
			{
				br.close();
				throw new Exception("Invalid tree file! Error on token: " + line + ",line " + String.valueOf(lineIndex));
			}
			
			new SSNode(key, name, node, this);
		}
		
		br.close();
		AQLogger.log("Tree has been loaded!");
	}
	
	public void print()
	{
		this.root.print("");
	}
	
	public SSNode find(String Key)
	{
		return this.root.find(Key);
	}
	
	public void init(String Indices)
	{
		this.root.loadIndices(Indices);
	}
	
	public String save()
	{
		return this.root.printIndices("");
	}
	
	public void update()
	{
		AQSkinManager.instance.getPages().update();
		
		for(ListIterator<SSNode> i = this.root.children.listIterator(); i.hasNext();)
			AQSkinManager.instance.getLayers().update(i.next());
		
		// SkinRenderer.regenTexture();
	}
	
	@SuppressWarnings("static-method")
	public void update(SSNode Sender)
	{
		AQSkinManager.instance.getPages().update();
		AQSkinManager.instance.getLayers().update(Sender);
		// SkinRenderer.regenTexture();
	}
}
