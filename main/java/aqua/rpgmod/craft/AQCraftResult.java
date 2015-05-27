package aqua.rpgmod.craft;

import aqua.rpgmod.inventory.AQContainerPlayer;
import aqua.rpgmod.inventory.AQCraftContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

abstract public class AQCraftResult
{
	protected ItemStack result = null;
	public String text = null;
	public boolean availiable = true;
	public AQRecipe ownRecipe;
	
	public void setRecipe(AQRecipe recipe)
	{
		this.ownRecipe = recipe;
	}
	
	@SuppressWarnings("unused")
	public AQCraftResult apply(AQRecipe recipe, EntityPlayer player)
	{
		return this;
	}
	
	public ItemStack get()
	{
		return this.result.copy();
	}
	
	@SuppressWarnings("unused")
	public void onPickup(AQCraftContainer container, AQHashItemStack[] craftMap)
	{}
	
	@SuppressWarnings("unused")
	public void onPickup(AQContainerPlayer container, AQHashItemStack[] craftMap)
	{}
}
