package aqua.rpgmod.craft;

import aqua.rpgmod.inventory.AQCraftContainer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class AQRepairResult extends AQCraftResult
{
	public final AQHashItemStack target;
	public final int repairAmount;
	public final int expNeeded;
	protected float mult = 1.0f;
	
	public AQRepairResult(Item result, int repairAmount, int expNeeded)
	{
		this.target = new AQHashItemStack(new ItemStack(result));
		this.repairAmount = repairAmount;
		this.expNeeded = expNeeded;
	}
	
	@Override
	public ItemStack get()
	{
		return this.result;
	}
	
	@Override
	public AQCraftResult apply(AQRecipe recipe, EntityPlayer player)
	{
		this.result = null;
		
		for(int i = 0; i < recipe.contents.length; i++)
		{
			if(recipe.contents[i].id == this.target.id)
				this.result = recipe.contents[i].getStack();
		}
		
		if(this.result != null)
		{
			this.mult = Math.min(1.0f, (float) (this.result.getItemDamage()) / (float) (this.repairAmount));
			this.result.setItemDamage(this.result.getItemDamage() - (int) (this.repairAmount * this.mult));
			this.text = "Requires " + (int) (this.expNeeded * this.mult) + " XP";
			
			if(!player.capabilities.isCreativeMode && player.experienceTotal < this.expNeeded * this.mult)
			{
				this.availiable = false;
				this.text = "\u00a74" + this.text;
			}
			else
			{
				this.availiable = true;
				this.text = "\u00a72" + this.text;
			}
		}
		
		return this;
	}
	
	@Override
	public void onPickup(AQCraftContainer container, AQHashItemStack[] craftMap)
	{
		if(!container.player.capabilities.isCreativeMode)
			container.player.addExperience(-(int) (this.expNeeded * this.mult));
		
		container.result = null;
	}
}
