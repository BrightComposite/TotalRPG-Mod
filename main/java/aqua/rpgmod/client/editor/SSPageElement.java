package aqua.rpgmod.client.editor;

import java.util.ArrayList;
import java.util.List;

public class SSPageElement implements AQSelector
{
	public List<SSNode> nodes = new ArrayList<SSNode>();
	// public Selector Interface = new Selector(this);
	public SSPages parent;
	protected int index = 0;
	
	public SSPageElement(SSPages Parent)
	{
		this.parent = Parent;
		
		// Interface.label.setForeground(Color.getHSBColor(0.2f, 1.0f, 0.6f));
		// Interface.label.setFont(BaseUtils.getFont("bold", 16F));
	}
	
	public void addNode(SSNode Node)
	{
		this.nodes.add(Node);
		// Interface.updateButtons();
	}
	
	public void update()
	{
		SSNode node = this.nodes.get(this.index);
		
		// Interface.label.setText(Node.Name);
		// parent.selectors.addSelector(Interface);
		node = node.currentChild;
		
		while(node != null)
		{
			/*
			 * Selector s = new Selector(Node.Parent);
			 * 
			 * s.label.setText(Node.Name);
			 * s.label.setForeground(Color.getHSBColor(0.2f, 1.0f, 0.7f));
			 * s.label.setFont(BaseUtils.getFont("regular", 16F));
			 * 
			 * parent.Selectors.addSelector(s);
			 */
			node = node.currentChild;
		}
	}
	
	@Override
	public void next()
	{
		if(AQSkinManager.loopedSelection())
		{
			if(this.index + 1 > this.nodes.size() - 1)
				this.index = -1;
		}
		else if(!hasNext())
			return;
		
		this.index++;
		this.parent.update();
	}
	
	@Override
	public void previous()
	{
		if(AQSkinManager.loopedSelection())
		{
			if(this.index - 1 < 0)
				this.index = this.nodes.size();
		}
		else if(!hasPrevious())
			return;
		
		this.index--;
		this.parent.update();
	}
	
	@Override
	public boolean hasNext()
	{
		return AQSkinManager.loopedSelection() || this.index < this.nodes.size() - 1;
	}
	
	@Override
	public boolean hasPrevious()
	{
		return AQSkinManager.loopedSelection() || this.index > 0;
	}
	
	@Override
	public boolean hasVariants()
	{
		return this.nodes.size() > 1;
	}
	
	@Override
	public int getType()
	{
		return 1;
	}
}
