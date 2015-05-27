package aqua.rpgmod.player.params;

import aqua.rpgmod.player.AQPlayerWrapper;
import aqua.rpgmod.player.AQThisPlayerWrapper;
import aqua.rpgmod.player.update.AQUpdateTimer;
import aqua.rpgmod.player.update.AQUpdater;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class AQStamina extends AQLifeParameter
{
	public float standRestore = 2.5f;
	public float walkRestore = 1;
	public float walkSpend = 1.5f;
	public float sprintSpend = 2.5f;
	public float jumpSpend = 4.5f;
	
	public class Restorer implements AQUpdater
	{
		protected int disableRestoringTicks = 0;
		
		@Override
		public void update()
		{
			if(this.disableRestoringTicks > 0)
			{
				this.disableRestoringTicks--;
				return;
			}
			
			float modifier = (AQStamina.this.wrapper.player.moveForward != 0.0f || AQStamina.this.wrapper.player.moveStrafing != 0.0f) ? AQStamina.this.walkRestore
				: AQStamina.this.standRestore;
			
			changeBy(2.0f * modifier);
		}
	}
	
	Restorer restorer = new Restorer();
	
	AQUpdateTimer periodicRestore = new AQUpdateTimer(new AQUpdater()
	{
		@Override
		public void update()
		{
			changeBy(1);
		}
	}, 4);
	
	public AQStamina(AQPlayerWrapper wrapper)
	{
		super("stamina", wrapper, AQLifeParameter.Side.CLIENT, 1500, 1500);
	}
	
	abstract public class FatigueLevel
	{
		public final float limit;
		public final boolean pass;
		
		public FatigueLevel(float limit)
		{
			this(limit, false);
		}
		
		public FatigueLevel(float limit, boolean pass)
		{
			this.limit = limit;
			this.pass = pass;
		}
		
		abstract public void update();
	}
	
	public final FatigueLevel fatigueLevels[] = new FatigueLevel[]
	{
		new FatigueLevel(500)
		{
			@Override
			public void update()
			{
				AQThisPlayerWrapper wrapper = getThis();
				
				if(AQStamina.this.fatigueLevel > 0)
					wrapper.SPEED.reset();
				
				if(AQStamina.this.fatigueLevel > 1)
					wrapper.JUMP.reset();
			}
		}, new FatigueLevel(200)
		{
			@Override
			public void update()
			{
				AQThisPlayerWrapper wrapper = getThis();
				
				wrapper.SPEED.value = wrapper.SPEED.getDefault() * speedModifier();
				
				if(AQStamina.this.fatigueLevel > 1)
					wrapper.JUMP.reset();
			}
		}, new FatigueLevel(50, true)
		{
			@Override
			public void update()
			{
				AQThisPlayerWrapper wrapper = getThis();
				
				wrapper.SPEED.value = wrapper.SPEED.getDefault() * speedModifier();
				wrapper.JUMP.value = wrapper.JUMP.getDefault() * jumpModifier();
			}
		}, new FatigueLevel(0)
		{
			@Override
			public void update()
			{
				AQThisPlayerWrapper wrapper = getThis();
				
				wrapper.SPEED.value = wrapper.SPEED.getDefault() * speedModifier();
				wrapper.JUMP.value = 0;
			}
		},
	};
	
	public float fatigueModifier(int level, float mul)
	{
		return 1.0f + (this.value / this.fatigueLevels[level].limit - 1.0f) * mul;
	}
	
	public float speedModifier()
	{
		return fatigueModifier(0, 0.25f);
	}
	
	public float jumpModifier()
	{
		return fatigueModifier(1, 0.25f);
	}
	
	public int fatigueLevel = 0;
	
	public void disableRestoring(int ticks)
	{
		this.restorer.disableRestoringTicks = ticks;
	}
	
	public AQThisPlayerWrapper getThis()
	{
		return (AQThisPlayerWrapper) this.wrapper;
	}
	
	@Override
	public void update()
	{
		EntityPlayer player = this.wrapper.player;
		
		if(player.capabilities.isCreativeMode)
		{
			if(getPercent() < 1.0f)
			{
				this.restore();
				setFatigueLevel(calculateFatigue());
			}
			
			return;
		}
		
		AQThisPlayerWrapper thisWrapper = getThis();
		this.restorer.update();
		this.periodicRestore.update();
		
		this.max = this.wrapper.getHunger() * 25;
		
		float modifier = 0;
		
		if(this.wrapper.isMoving())
		{
			modifier -= this.walkSpend;
			
			if(player.isSprinting())
				modifier -= this.sprintSpend;
		}
		
		if(thisWrapper.isJumping())
		{
			modifier -= this.jumpSpend * thisWrapper.JUMP.value + 1.5f;
		}
		
		changeBy(modifier);
		setFatigueLevel(calculateFatigue());
	}
	
	protected void setFatigueLevel(int fatigueLevel)
	{
		this.fatigueLevels[fatigueLevel].update();
		this.fatigueLevel = fatigueLevel;
	}
	
	public int calculateFatigue()
	{
		for(int i = 0; i < this.fatigueLevels.length; i++)
		{
			if(this.value >= this.fatigueLevels[i].limit)
			{
				if(this.fatigueLevels[i].pass && this.fatigueLevel > i)
					continue;
				
				return i;
			}
		}
		
		return this.fatigueLevels.length - 1;
	}
	
	@Override
	public void saveNBTData(NBTTagCompound compound)
	{
		super.saveNBTData(compound);
		
		compound.setInteger("fatigue", this.fatigueLevel);
	}
	
	@Override
	public void loadNBTData(NBTTagCompound compound)
	{
		super.loadNBTData(compound);
		
		if(compound.hasKey("fatigue"))
			this.fatigueLevel = compound.getInteger("fatigue");
		else
			this.fatigueLevel = 0;
	}
	
	@Override
	public ByteBuf write(ByteBuf buffer)
	{
		return super.write(buffer).writeInt(this.fatigueLevel);
	}
	
	@Override
	public ByteBuf read(ByteBuf buffer)
	{
		this.fatigueLevel = super.read(buffer).readInt();
		return buffer;
	}
}
