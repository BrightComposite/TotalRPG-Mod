package aqua.rpgmod.client.editor;

import java.util.ArrayList;
import java.util.Iterator;

public class SSNode implements AQSelector
{
	public String name = null;
	public String key = null;
	public SSNode parent = null;
	public SSTree tree = null;
	
	public SSNode currentChild = null;
	protected int index = 0;
	public ArrayList<SSNode> children = new ArrayList<SSNode>();
	
	public SSNode(SSTree tree)
	{
		this.tree = tree;
	}
	
	public SSNode(String key, String name, SSNode parent, SSTree tree)
	{
		this.key = key;
		this.name = name;
		this.parent = parent;
		this.tree = tree;
		
		parent.add(this);
	}
	
	public boolean isChild()
	{
		return this.children.isEmpty();
	}
	
	public boolean isParent()
	{
		return !this.children.isEmpty();
	}
	
	public boolean isTop()
	{
		return this.parent == this.tree.root;
	}
	
	public void add(SSNode Part)
	{
		this.children.add(Part);
		
		if(this.currentChild == null)
			this.currentChild = Part;
	}
	
	public SSNode find(String Key)
	{
		if(this.children.isEmpty())
			return null;
		
		for(Iterator<SSNode> i = this.children.iterator(); i.hasNext();)
		{
			SSNode node = i.next();
			if(node.key.equals(Key))
				return node;
		}
		
		return null;
	}
	
	public void sendUpdate()
	{
		if(this.parent == null)
			return;
		
		if(isTop())
			this.tree.update(this);
		else
			this.parent.sendUpdate();
	}
	
	public void setIndex(int index)
	{
		if(this.index == index)
			return;
		
		this.index = index;
		this.currentChild = this.children.get(index);
		sendUpdate();
	}
	
	@Override
	public void next()
	{
		if(AQSkinManager.loopedSelection())
		{
			if(this.index + 1 > this.children.size() - 1)
				this.index = -1;
		}
		else if(!hasNext())
			return;
		
		this.index++;
		this.currentChild = this.children.get(this.index);
		sendUpdate();
	}
	
	@Override
	public void previous()
	{
		if(AQSkinManager.loopedSelection())
		{
			if(this.index - 1 < 0)
				this.index = this.children.size();
		}
		else if(!hasPrevious())
			return;
		
		this.index--;
		this.currentChild = this.children.get(this.index);
		sendUpdate();
	}
	
	@Override
	public boolean hasNext()
	{
		return this != this.tree.root && (AQSkinManager.loopedSelection() || this.index < this.children.size() - 1);
	}
	
	@Override
	public boolean hasPrevious()
	{
		return this != this.tree.root && (AQSkinManager.loopedSelection() || this.index > 0);
	}
	
	@Override
	public boolean hasVariants()
	{
		return this.children.size() > 1;
	}
	
	@Override
	public int getType()
	{
		return this.currentChild.isTop() ? 1 : 0;
	}
	
	public String getBranch(String separator)
	{
		String path = "";
		SSNode node = this;
		
		while(node.isParent())
		{
			node = node.currentChild;
			path += node.key + separator;
		}
		
		return path;
	}
	
	public void print(String offset)
	{
		if(this != this.tree.root)
		{
			System.out.println(offset + this.key);
			offset += "  ";
		}
		
		for(Iterator<SSNode> i = this.children.iterator(); i.hasNext();)
			i.next().print(offset);
	}
	
	public String loadIndices(String Indices)
	{
		if(!isParent())
			return Indices;
		
		if(this == this.tree.root)
		{
			for(Iterator<SSNode> i = this.children.iterator(); i.hasNext();)
				Indices = i.next().loadIndices(Indices);
			
			return Indices;
		}
		
		String I = Indices.substring(0, Indices.indexOf(" "));
		Indices = Indices.substring(Indices.indexOf(" ") + 1, Indices.length());
		
		setIndex(Integer.valueOf(I).intValue());
		Indices = this.currentChild.loadIndices(Indices);
		return Indices;
	}
	
	public String printIndices(String Indices)
	{
		if(this == this.tree.root)
		{
			String s = "";
			for(Iterator<SSNode> i = this.children.iterator(); i.hasNext();)
				s += i.next().printIndices(Indices);
			
			return Indices + s;
		}
		if(isTop())
			return this.currentChild.printIndices(Indices);
		
		if(isParent())
			return String.valueOf(getId()) + " " + this.currentChild.printIndices(Indices);
		
		return String.valueOf(getId()) + " ";
	}
	
	public int getId()
	{
		return this == this.tree.root ? 0 : this.parent.index;
	}
}
