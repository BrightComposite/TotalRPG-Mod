package aqua.rpgmod.player.params;

import aqua.rpgmod.player.AQPlayerWrapper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.FoodStats;
import net.minecraft.world.EnumDifficulty;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class AQFoodStats extends FoodStats
{
	protected final AQPlayerWrapper wrapper;
	
	public AQFoodStats(AQPlayerWrapper wrapper)
	{
		this.wrapper = wrapper;
	}
	
	@Override
	public void addStats(int p_75122_1_, float p_75122_2_)
	{
		this.wrapper.hunger.changeBy(p_75122_1_);
		this.wrapper.hunger.saturation.changeBy(p_75122_1_ * p_75122_2_ * 2.0F);
	}
	
	@Override
	public void func_151686_a(ItemFood p_151686_1_, ItemStack p_151686_2_)
	{
		this.addStats(p_151686_1_.func_150905_g(p_151686_2_), p_151686_1_.func_150906_h(p_151686_2_));
	}
	
	@Override
	public void onUpdate(EntityPlayer player)
	{
		EnumDifficulty enumdifficulty = player.worldObj.difficultySetting;
		
		this.wrapper.hunger.prevFoodLevel = this.wrapper.hunger.value;
		
		if(this.wrapper.hunger.foodExhaustionLevel > 4.0F)
		{
			this.wrapper.hunger.foodExhaustionLevel -= 4.0F;
			
			if(this.wrapper.hunger.saturation.value > 0.0F)
			{
				this.wrapper.hunger.saturation.changeBy(-1);
			}
			else if(enumdifficulty != EnumDifficulty.PEACEFUL)
			{
				this.wrapper.hunger.changeBy(-1);
			}
		}
	}
	
	@Override
	public void readNBT(NBTTagCompound tag)
	{}
	
	@Override
	public void writeNBT(NBTTagCompound tag)
	{}
	
	@Override
	public int getFoodLevel()
	{
		return this.wrapper.hunger.getFoodLevel();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getPrevFoodLevel()
	{
		return this.wrapper.hunger.getPrevFoodLevel();
	}
	
	@Override
	public boolean needFood()
	{
		return this.wrapper.hunger.needFood();
	}
	
	@Override
	public void addExhaustion(float p_75113_1_)
	{
		this.wrapper.hunger.addExhaustion(p_75113_1_);
	}
	
	@Override
	public float getSaturationLevel()
	{
		return this.wrapper.hunger.saturation.value;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void setFoodLevel(int p_75114_1_)
	{
		this.wrapper.hunger.value = (p_75114_1_ * this.wrapper.hunger.max) / 20;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void setFoodSaturationLevel(float p_75119_1_)
	{
		this.wrapper.hunger.saturation.value = p_75119_1_;
	}
}
