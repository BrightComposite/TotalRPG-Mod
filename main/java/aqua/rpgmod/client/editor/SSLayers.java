package aqua.rpgmod.client.editor;

import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import aqua.rpgmod.service.AQLogger;
import aqua.rpgmod.service.resources.AQFileResource;

public class SSLayers
{
	public List<SSLayer> layers = new ArrayList<SSLayer>();
	
	public SSLayers(AQFileResource resource) throws Exception
	{
		if(!resource.canRead())
			throw new Exception("Can't read layers from " + resource.getFile().getAbsolutePath() + "!");
		
		BufferedReader br = new BufferedReader(new FileReader(resource.getFile()));
		br.skip(1);
		
		String line;
		int lineIndex = 0;
		
		while((line = br.readLine()) != null)
		{
			line = line.trim();
			lineIndex++;
			
			if((line.isEmpty()) || (line.startsWith("#")))
				continue;
			
			String[] s = line.split(":");
			String name = s[0].trim();
			
			String[] nodeNames = s[1].split(",");
			String nodeName = null;
			
			List<SSNode> nodes = new ArrayList<SSNode>();
			SSNode node = null;
			
			for(int i = 0; i < nodeNames.length; i++)
			{
				nodeName = nodeNames[i].trim();
				node = AQSkinManager.instance.currentSkinGroup.tree.find(nodeName);
				
				if(node == null)
					break;
				
				nodes.add(node);
			}
			
			if(node == null)
			{
				br.close();
				throw new Exception("Invalid layers file! Can't find node: " + nodeName + ", line " + String.valueOf(lineIndex));
			}
			
			this.layers.add(new SSLayer(name, nodes));
		}
		
		br.close();
		
		AQLogger.log("Layers has been loaded!");
	}
	
	public void update(SSNode Node)
	{
		for(ListIterator<SSLayer> i = this.layers.listIterator(); i.hasNext();)
			i.next().update(Node);
	}
	
	public void drawTo(Graphics g)
	{
		for(ListIterator<SSLayer> i = this.layers.listIterator(); i.hasNext();)
			i.next().drawTo(g);
	}
}
