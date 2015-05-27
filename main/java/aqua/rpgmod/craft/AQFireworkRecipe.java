package aqua.rpgmod.craft;

import java.util.ArrayList;

import net.minecraft.init.Items;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class AQFireworkRecipe extends AQMorphingRecipe
{
	@Override
	public boolean equals(AQMorphingRecipe recipe)
	{
		recipe.output = null;
		int i = 0;
		int j = 0;
		int k = 0;
		int l = 0;
		int i1 = 0;
		int j1 = 0;
		
		for(int k1 = 0; k1 < recipe.contents.length; ++k1)
		{
			ItemStack itemstack = recipe.contents[k1].getStack();
			
			if(itemstack != null)
			{
				if(itemstack.getItem() == Items.gunpowder)
				{
					++j;
				}
				else if(itemstack.getItem() == Items.firework_charge)
				{
					++l;
				}
				else if(itemstack.getItem() == Items.dye)
				{
					++k;
				}
				else if(itemstack.getItem() == Items.paper)
				{
					++i;
				}
				else if(itemstack.getItem() == Items.glowstone_dust)
				{
					++i1;
				}
				else if(itemstack.getItem() == Items.diamond)
				{
					++i1;
				}
				else if(itemstack.getItem() == Items.fire_charge)
				{
					++j1;
				}
				else if(itemstack.getItem() == Items.feather)
				{
					++j1;
				}
				else if(itemstack.getItem() == Items.gold_nugget)
				{
					++j1;
				}
				else
				{
					if(itemstack.getItem() != Items.skull)
					{
						return false;
					}
					
					++j1;
				}
			}
		}
		
		i1 += k + j1;
		
		if(j <= 3 && i <= 1)
		{
			NBTTagCompound nbttagcompound;
			NBTTagCompound nbttagcompound1;
			
			if(j >= 1 && i == 1 && i1 == 0)
			{
				recipe.output = new ItemStack(Items.fireworks);
				
				nbttagcompound = new NBTTagCompound();
				if(l > 0)
				{
					nbttagcompound1 = new NBTTagCompound();
					NBTTagList nbttaglist = new NBTTagList();
					
					for(int k2 = 0; k2 < recipe.contents.length; ++k2)
					{
						ItemStack itemstack3 = recipe.contents[k2].getStack();
						
						if(itemstack3 != null && itemstack3.getItem() == Items.firework_charge && itemstack3.hasTagCompound()
							&& itemstack3.getTagCompound().hasKey("Explosion", 10))
						{
							nbttaglist.appendTag(itemstack3.getTagCompound().getCompoundTag("Explosion"));
						}
					}
					
					nbttagcompound1.setTag("Explosions", nbttaglist);
					nbttagcompound1.setByte("Flight", (byte) j);
					nbttagcompound.setTag("Fireworks", nbttagcompound1);
				}
				recipe.output.setTagCompound(nbttagcompound); // Forge BugFix:
																// NPE
																// Protection
				
				return true;
			}
			else if(j == 1 && i == 0 && l == 0 && k > 0 && j1 <= 1)
			{
				recipe.output = new ItemStack(Items.firework_charge);
				nbttagcompound = new NBTTagCompound();
				nbttagcompound1 = new NBTTagCompound();
				byte b0 = 0;
				ArrayList arraylist = new ArrayList();
				
				for(int l1 = 0; l1 < recipe.contents.length; ++l1)
				{
					ItemStack itemstack2 = recipe.contents[l1].getStack();
					
					if(itemstack2 != null)
					{
						if(itemstack2.getItem() == Items.dye)
						{
							arraylist.add(Integer.valueOf(ItemDye.field_150922_c[itemstack2.getItemDamage()]));
						}
						else if(itemstack2.getItem() == Items.glowstone_dust)
						{
							nbttagcompound1.setBoolean("Flicker", true);
						}
						else if(itemstack2.getItem() == Items.diamond)
						{
							nbttagcompound1.setBoolean("Trail", true);
						}
						else if(itemstack2.getItem() == Items.fire_charge)
						{
							b0 = 1;
						}
						else if(itemstack2.getItem() == Items.feather)
						{
							b0 = 4;
						}
						else if(itemstack2.getItem() == Items.gold_nugget)
						{
							b0 = 2;
						}
						else if(itemstack2.getItem() == Items.skull)
						{
							b0 = 3;
						}
					}
				}
				
				int[] aint1 = new int[arraylist.size()];
				
				for(int l2 = 0; l2 < aint1.length; ++l2)
				{
					aint1[l2] = ((Integer) arraylist.get(l2)).intValue();
				}
				
				nbttagcompound1.setIntArray("Colors", aint1);
				nbttagcompound1.setByte("Type", b0);
				nbttagcompound.setTag("Explosion", nbttagcompound1);
				recipe.output.setTagCompound(nbttagcompound);
				return true;
			}
			else if(j == 0 && i == 0 && l == 1 && k > 0 && k == i1)
			{
				ArrayList arraylist1 = new ArrayList();
				
				for(int i2 = 0; i2 < recipe.contents.length; ++i2)
				{
					ItemStack itemstack1 = recipe.contents[i2].getStack();
					
					if(itemstack1 != null)
					{
						if(itemstack1.getItem() == Items.dye)
						{
							arraylist1.add(Integer.valueOf(ItemDye.field_150922_c[itemstack1.getItemDamage()]));
						}
						else if(itemstack1.getItem() == Items.firework_charge)
						{
							recipe.output = itemstack1.copy();
							recipe.output.stackSize = 1;
						}
					}
				}
				
				int[] aint = new int[arraylist1.size()];
				
				for(int j2 = 0; j2 < aint.length; ++j2)
				{
					aint[j2] = ((Integer) arraylist1.get(j2)).intValue();
				}
				
				if(recipe.output != null && recipe.output.hasTagCompound())
				{
					NBTTagCompound nbttagcompound2 = recipe.output.getTagCompound().getCompoundTag("Explosion");
					
					if(nbttagcompound2 == null)
					{
						return false;
					}
					
					nbttagcompound2.setIntArray("FadeColors", aint);
					return true;
				}
				
				return false;
			}
			else
			{
				return false;
			}
		}
		
		return false;
	}
	
	@Override
	public void normalize()
	{}
}
