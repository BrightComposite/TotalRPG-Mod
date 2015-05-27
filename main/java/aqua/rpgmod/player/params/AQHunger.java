package aqua.rpgmod.player.params;

import aqua.rpgmod.player.AQPlayerWrapper;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.EnumDifficulty;

public class AQHunger extends AQLifeParameter
{
	public AQLifeParameter saturation;
	
	protected float prevFoodLevel = 40;
	protected float foodExhaustionLevel;
	
	public AQHunger(AQPlayerWrapper wrapper)
	{
		super("hunger", wrapper, AQLifeParameter.Side.SERVER, 40, 40);
		this.saturation = new AQLifeParameter("ts", wrapper, AQLifeParameter.Side.SERVER, 20, 40);
	}
	
	public boolean needFood()
	{
		return this.value < this.max;
	}
	
	public void addExhaustion(float p_75113_1_)
	{
		this.foodExhaustionLevel = Math.min(this.foodExhaustionLevel + p_75113_1_, this.max);
	}
	
	public int getFoodLevel()
	{
		return Math.round(20 * getPercent());
	}
	
	public int getPrevFoodLevel()
	{
		return Math.round(20 * this.prevFoodLevel / this.max);
	}
	
	@Override
	public void update()
	{
		if(this.wrapper.player == null || this.wrapper.player.worldObj.isRemote)
			return;
		
		EnumDifficulty enumdifficulty = this.wrapper.player.worldObj.difficultySetting;
		
		if(this.wrapper.player.worldObj.getGameRules().getGameRuleBooleanValue("naturalRegeneration") && getFoodLevel() >= 18
			&& this.wrapper.player.shouldHeal())
		{
			this.wrapper.player.heal(1.0f);
			addExhaustion(3.0F);
		}
		else if(this.value == 0)
		{
			if(this.wrapper.player.getHealth() > 10.0F || enumdifficulty == EnumDifficulty.HARD || this.wrapper.player.getHealth() > 1.0F
				&& enumdifficulty == EnumDifficulty.NORMAL)
			{
				this.wrapper.player.attackEntityFrom(DamageSource.starve, 1.0F);
			}
		}
	}
	
	@Override
	public void saveNBTData(NBTTagCompound compound)
	{
		super.saveNBTData(compound);
		this.saturation.saveNBTData(compound);
		compound.setFloat("fel", this.foodExhaustionLevel);
	}
	
	@Override
	public void loadNBTData(NBTTagCompound compound)
	{
		super.loadNBTData(compound);
		this.saturation.loadNBTData(compound);
		this.foodExhaustionLevel = compound.getFloat("fel");
	}
	
	@Override
	public ByteBuf write(ByteBuf buffer)
	{
		return this.saturation.write(super.write(buffer)).writeFloat(this.foodExhaustionLevel);
	}
	
	@Override
	public ByteBuf read(ByteBuf buffer)
	{
		this.saturation.read(super.read(buffer));
		this.foodExhaustionLevel = buffer.readFloat();
		return buffer;
	}
}
