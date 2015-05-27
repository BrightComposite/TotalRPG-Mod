package aqua.rpgmod.client.editor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import aqua.rpgmod.service.AQLogger;
import aqua.rpgmod.service.resources.AQFileResource;

public class SSPages implements AQSelector
{
	// public SelectorsField Selectors;
	public ArrayList<SSPage> pages = new ArrayList<SSPage>();
	public SSPage currentPage = null;
	public int index = 0;
	
	public SSPages(AQFileResource resource) throws Exception
	{
		// Selectors = SkinEditor.getSelectors();
		
		if(!resource.canRead())
		{
			throw new Exception("Can't read pages from " + resource.getFile().getAbsolutePath() + "!");
		}
		
		BufferedReader br = new BufferedReader(new FileReader(resource.getFile()));
		br.skip(1);
		
		SSPage page = null;
		int lineIndex = 0;
		
		while(true)
		{
			String line = br.readLine();
			
			if(line == null)
				break;
			
			line = line.trim();
			lineIndex++;
			
			if(line.isEmpty() || line.startsWith("#"))
				continue;
			
			if(line.startsWith(">"))
			{
				if(page == null)
				{
					br.close();
					throw new Exception("Invalid pages file! End of unknown section found, line " + String.valueOf(lineIndex));
				}
				
				page = null;
				continue;
			}
			
			if(line.endsWith("<"))
			{
				line = line.substring(0, line.length() - 1).trim();
				page = new SSPage(line, this);
				this.pages.add(page);
				continue;
			}
			
			if(page == null)
			{
				br.close();
				throw new Exception("Invalid pages file! Error on token: " + line + ",line " + String.valueOf(lineIndex));
			}
			
			String[] nodes = line.split(",");
			SSPageElement element = page.addElement();
			
			for(String name : nodes)
			{
				name = name.trim();
				
				if(name.isEmpty())
					continue;
				
				SSNode node = AQSkinManager.instance.getTree().root.find(name);
				
				if(node == null)
				{
					br.close();
					throw new Exception("Invalid pages file! Can't find node: " + line + ",line " + String.valueOf(lineIndex));
				}
				
				element.addNode(node);
			}
		}
		
		br.close();
		
		update();
		AQLogger.log("Pages has been loaded!");
	}
	
	@Override
	public void next()
	{
		if(AQSkinManager.loopedSelection())
		{
			if(this.index + 1 > this.pages.size() - 1)
				this.index = -1;
		}
		else if(!hasNext())
			return;
		
		this.index++;
		this.currentPage = this.pages.get(this.index);
		update();
	}
	
	@Override
	public void previous()
	{
		if(AQSkinManager.loopedSelection())
		{
			if(this.index - 1 < 0)
				this.index = this.pages.size();
		}
		else if(!hasPrevious())
			return;
		
		this.index--;
		this.currentPage = this.pages.get(this.index);
		update();
	}
	
	@Override
	public boolean hasNext()
	{
		return AQSkinManager.loopedSelection() || this.index < this.pages.size() - 1;
	}
	
	@Override
	public boolean hasPrevious()
	{
		return AQSkinManager.loopedSelection() || this.index > 0;
	}
	
	@Override
	public boolean hasVariants()
	{
		return this.pages.size() > 1;
	}
	
	@Override
	public int getType()
	{
		return 2;
	}
	
	public void update()
	{
		/*
		 * Selectors.reset();
		 * 
		 * Selector PageSelector = new Selector(this);
		 * 
		 * PageSelector.label.setText(CurrentPage.Name);
		 * PageSelector.label.setForeground(Color.getHSBColor(0.1f, 1.0f,
		 * 0.9f)); PageSelector.label.setFont(BaseUtils.getFont("bold", 16F));
		 * 
		 * Selectors.addSelector(PageSelector); Selectors.Y += 10;
		 * 
		 * CurrentPage.update();
		 */
	}
}
