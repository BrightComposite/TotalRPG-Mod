package aqua.rpgmod.client.gui.dialog;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.Tessellator;

public class AQListDialog<T> extends AQGuiDialog
{
	public static enum Result
	{
		USE,
		CLOSE;
	}
	
	ArrayList<T> elements = new ArrayList<T>();
	int selectedIndex = 0;
	
	protected Result result = Result.CLOSE;
	
	protected Contents contents;
	
	public AQListDialog(GuiScreen owner, T ... elements)
	{
		super(owner);
		
		this.elements.addAll(Arrays.asList(elements));
	}
	
	@Override
	public void initGui()
	{
		this.xSize = this.width;
		this.ySize = this.height;
		
		super.initGui();
		this.buttonList.add(new GuiButton(0, this.guiLeft + this.xSize - 25, this.guiTop + 5, 20, 20, "X"));
		
		this.contents = new Contents();
		this.contents.registerScrollButtons(3, 4);
	}
	
	@Override
	protected void actionPerformed(GuiButton button)
	{
		switch(button.id)
		{
			case 0:
				this.result = Result.CLOSE;
				close();
				break;
			
			default:
				this.contents.actionPerformed(button);
		}
	}
	
	public String getElementDisplayName(int index)
	{
		return this.elements.get(index).toString();
	}
	
	@Override
	public void drawForegroundLayer(int p_73863_1_, int p_73863_2_, float p_73863_3_)
	{
		this.contents.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}
	
	public int selectedIndex()
	{
		return this.selectedIndex;
	}
	
	public boolean isInRange(int index)
	{
		return index >= 0 && index < this.elements.size();
	}
	
	public T selectedElement()
	{
		return isInRange(this.selectedIndex) ? this.elements.get(this.selectedIndex) : null;
	}
	
	public void select(T element)
	{
		int index = this.elements.indexOf(element);
		
		if(index > 0 && onSelect(index))
		{
			AQListDialog.this.selectedIndex = index;
		}
	}
	
	public Result getResult()
	{
		return this.result;
	}
	
	public boolean onSelect(int index)
	{
		return isInRange(index);
	}
	
	public boolean onUse(int index)
	{
		return isInRange(index);
	}
	
	class Contents extends GuiSlot
	{
		public Contents()
		{
			super(AQListDialog.this.mc, AQListDialog.this.width, AQListDialog.this.height, 32, AQListDialog.this.height - 64, 20);
		}
		
		@Override
		protected int getSize()
		{
			return AQListDialog.this.elements.size();
		}
		
		@Override
		protected void elementClicked(int index, boolean doubleClick, int p_148144_3_, int p_148144_4_)
		{
			if(AQListDialog.this.selectedIndex == index)
			{
				if(doubleClick)
				{
					if(onUse(AQListDialog.this.selectedIndex))
					{
						AQListDialog.this.result = Result.USE;
						close();
					}
				}
				
				return;
			}
			
			if(onSelect(index))
			{
				AQListDialog.this.selectedIndex = index;
			}
		}
		
		@Override
		protected boolean isSelected(int index)
		{
			return index == AQListDialog.this.selectedIndex;
		}
		
		@Override
		protected void drawBackground()
		{	
			
		}
		
		@Override
		protected void drawSlot(int index, int x, int y, int p_148126_4_, Tessellator p_148126_5_, int p_148126_6_, int p_148126_7_)
		{
			drawString(AQListDialog.this.fontRendererObj, getElementDisplayName(index), x + 2, y + 2, isSelected(index) ? 0xffffffff : 0xaaaaaaff);
		}
		
	}
}
