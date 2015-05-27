package aqua.rpgmod.client.editor;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class SSPage
{
	public String name;
	public List<SSPageElement> elements = new ArrayList<SSPageElement>();
	public SSPages parent;
	
	public SSPage(String name, SSPages parent)
	{
		if(parent.currentPage == null)
			parent.currentPage = this;
		
		this.name = name;
		this.parent = parent;
	}
	
	public SSPageElement addElement()
	{
		SSPageElement Element = new SSPageElement(this.parent);
		this.elements.add(Element);
		
		return Element;
	}
	
	public void update()
	{
		for(ListIterator<SSPageElement> i = this.elements.listIterator(); i.hasNext();)
			i.next().update();
		
	}
}
