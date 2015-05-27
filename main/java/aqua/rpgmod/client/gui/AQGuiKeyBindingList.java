package aqua.rpgmod.client.gui;

import java.lang.reflect.Field;
import java.util.ArrayList;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiControls;
import net.minecraft.client.gui.GuiKeyBindingList;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;

@SideOnly(Side.CLIENT)
public class AQGuiKeyBindingList extends GuiKeyBindingList
{
	protected final ArrayList<GuiListExtended.IGuiListEntry> list;
	protected boolean shouldPatch = true;
	
	public AQGuiKeyBindingList(GuiControls p_i45031_1_, Minecraft p_i45031_2_)
	{
		super(p_i45031_1_, p_i45031_2_);
		
		this.list = new ArrayList<GuiListExtended.IGuiListEntry>(super.getSize());
		
		for(int i = 0; i < super.getSize(); i++)
		{
			GuiListExtended.IGuiListEntry entry = super.getListEntry(i);
			
			try
			{
				if(this.shouldPatch && entry instanceof CategoryEntry)
				{
					CategoryEntry category = (CategoryEntry) entry;
					
					Field f = GuiKeyBindingList.CategoryEntry.class.getDeclaredField("field_148285_b");
					String s = (String) f.get(category);
					
					if(s.equals(I18n.format("key.categories.stream")))
						continue;
				}
				
				if(this.shouldPatch && entry instanceof KeyEntry)
				{
					KeyEntry key = (KeyEntry) entry;
					
					Field f = GuiKeyBindingList.KeyEntry.class.getDeclaredField("field_148282_b");
					KeyBinding binding = (KeyBinding) f.get(key);
					
					if(binding.getKeyCategory().equals("key.categories.stream"))
						continue;
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				this.shouldPatch = false;
			}
			
			this.list.add(entry);
		}
	}
	
	@Override
	protected int getSize()
	{
		return this.list.size();
	}
	
	/**
	 * Gets the IGuiListEntry object for the given index
	 */
	@Override
	public GuiListExtended.IGuiListEntry getListEntry(int p_148180_1_)
	{
		return this.list.get(p_148180_1_);
	}
	
}
