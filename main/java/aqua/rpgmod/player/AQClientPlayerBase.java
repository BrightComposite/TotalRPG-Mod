package aqua.rpgmod.player;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.stats.StatList;
import net.minecraft.util.MathHelper;
import api.player.client.ClientPlayerAPI;
import api.player.client.ClientPlayerBase;

public class AQClientPlayerBase extends ClientPlayerBase
{
	boolean isClimbing = false;
	
	boolean sprintPressed = false;
	boolean sprinting = false;
	/*
	 * boolean sneakPressed = false; boolean sneaking = false;
	 */
	int swimCounter = 0;
	int topLevel = 0;
	
	float swimmingLimit;
	float breathLimit;
	
	AQThisPlayerWrapper wrapper;
	Minecraft mc;
	
	public AQClientPlayerBase(ClientPlayerAPI playerAPI)
	{
		super(playerAPI);
		
		this.wrapper = AQThisPlayerWrapper.getWrapper();
		this.mc = Minecraft.getMinecraft();
	}
	
	@Override
	public float getAIMoveSpeed()
	{
		if(this.player.capabilities.isCreativeMode)
			return super.getAIMoveSpeed();
		
		return super.getAIMoveSpeed() * this.wrapper.SPEED.value;
	}
	
	@Override
	public boolean isOnLadder()
	{
		if(super.isOnLadder())
			return true;
		
		return this.wrapper.race.canClimpUpTheWall() && this.wrapper.nearTheWall(-1.6f);
	}
	
	static void setSize(EntityPlayer player, float width, float height)
	{
		player.posX = (player.boundingBox.minX + player.boundingBox.maxX) / 2;
		player.posZ = (player.boundingBox.minZ + player.boundingBox.maxZ) / 2;
		
		player.width = width;
		player.height = height;
		
		float f = player.width / 2.0F;
		
		player.boundingBox.minX = player.posX - f;
		player.boundingBox.minZ = player.posZ - f;
		player.boundingBox.maxX = player.posX + f;
		player.boundingBox.maxZ = player.posZ + f;
	}
	
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		
		if(this.wrapper.player.isPlayerSleeping() || this.wrapper.player.isDead)
		{
			setSize(this.wrapper.player, 0.2f, 0.2f);
			return;
		}
		
		if(this.wrapper.race != null)
		{
			setSize(this.wrapper.player, this.wrapper.race.width(), this.wrapper.race.height());
			return;
		}
		
		setSize(this.wrapper.player, 0.6f, 1.8f);
	}
	
	@Override
	public void updateEntityActionState()
	{
		// calculateSneaking();
		calculateSprinting();
		
		while(true)
		{
			if(this.wrapper.isFlying || this.wrapper.player.isRiding())
			{
				this.wrapper.isSwimming = false;
				break;
			}
			
			boolean jump = this.player.movementInput.jump;
			this.player.movementInput.jump = false;
			
			this.breathLimit = -this.wrapper.yOffset + (this.player.movementInput.sneak ? 0.0f : 0.1f);
			this.swimmingLimit = this.breathLimit - (this.wrapper.race.canSwimLikeHuman() ? 0.2f : (this.wrapper.race.height() / 2 - 0.4f));
			
			if(this.wrapper.race.canSwim() && checkSwimming())
				break;

			this.wrapper.canBreathe = !this.wrapper.hasLiquidAt(0.0f, this.breathLimit + (float) this.player.motionY, 0.0f);
			
			if(this.wrapper.isSwimming)
			{
				if(this.wrapper.race.canSwimLikeHuman() && onSwimming())
					break;

				calculateTopLevel();
				
				if(this.topLevel == 2)
					this.player.motionY = -0.01f;
			}
			
			if(jump)
				handleJumpInput();
			
			break;
		}
		
		super.updateEntityActionState();
	}
	
	public void calculateSprinting()
	{
		if(/* this.sneaking || */this.wrapper.stamina.fatigueLevel == 3 || this.player.moveForward <= 0.0f)
		{
			this.sprinting = false;
		}
		else if(this.mc.gameSettings.keyBindSprint.getIsKeyPressed())
		{
			this.sprintPressed = true;
		}
		else if(this.sprintPressed)
		{
			this.sprinting = !this.sprinting;
			this.sprintPressed = false;
		}
		
		this.player.setSprinting(this.sprinting);
	}
	
	/*
	 * public void calculateSneaking() { boolean sneaking =
	 * this.mc.gameSettings.keyBindSneak.getIsKeyPressed();
	 * 
	 * if(AQClientProxy.keyBindSneakToggle.getIsKeyPressed()) {
	 * this.sneakPressed = true; } else if(this.sneakPressed) { this.sneaking =
	 * !this.sneaking; this.sneakPressed = false; }
	 * 
	 * KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindSneak.getKeyCode(),
	 * this.sneaking);
	 * 
	 * if(!sneaking) { this.player.movementInput.sneak = this.sneaking;
	 * this.player.setSneaking(this.sneaking); } }
	 */
	
	public boolean checkSwimming()
	{
		boolean onBlock = this.wrapper.canCollideWithBlockAt(0.0f, 0.5f, 0.0f);
		boolean isSwimming = false;
		
		if(this.wrapper.isInLiquid(this.swimmingLimit - 1.0f))
		{
			isSwimming = this.wrapper.isInLiquid(this.swimmingLimit) && (!onBlock || this.wrapper.isInLiquid(this.breathLimit + (float) this.player.motionY))
				&& !this.player.onGround;
			
			if(!this.wrapper.isJumping())
			{
				if((this.wrapper.isInLiquid() && this.player.isCollidedHorizontally) || this.wrapper.canClimbBlock(0))
				{
					this.player.motionY = 0.06f;
					this.wrapper.canBreathe = !this.wrapper.hasLiquidAt(0.0f, this.breathLimit + 0.66f, 0.0f);
					return true;
				}
			}
		}
		
		this.wrapper.isSwimming = isSwimming;
		
		return false;
	}
	
	public boolean onSwimming()
	{
		if(this.player.motionY < 0.0f)
		{
			this.player.motionY = Math.min(this.player.motionY + 0.32f, 0.0f);
		}
		
		if(this.player.movementInput.sneak)
		{
			if(this.player.motionY > -0.32f)
			{
				this.player.motionY -= 0.03f;
				
				if(this.player.motionY < -0.32f)
					this.player.motionY = 0.32f;
			}
			
			this.wrapper.stamina.changeBy(-3);
			return true;
		}
		
		return false;
	}
	
	public void calculateTopLevel()
	{
		if(this.wrapper.isInLiquid(this.swimmingLimit + 0.15f))
		{
			this.topLevel = 0;
		}
		else if(this.topLevel < 2 && this.wrapper.isInLiquid(this.swimmingLimit + 0.1f))
		{
			this.topLevel = 1;
		}
		else
		{
			this.topLevel = 2;
		}
	}
	
	public void handleJumpInput()
	{
		if(isOnLadder() || this.wrapper.canClimbBlock(0))
		{
			this.isClimbing = this.isClimbing || this.player.onGround && this.wrapper.stamina.fatigueLevel >= 3;
			
			if(this.isClimbing)
			{
				if(isOnLadder())
				{
					this.player.motionY = 0.12;
				}
				else
				{
					this.player.motionY = 0.06;
					this.wrapper.stamina.changeBy(-1);
				}
				
				return;
			}
		}
		
		this.isClimbing = false;
		
		if(this.player.onGround && this.wrapper.JUMP.value > 0)
		{
			jump();
			return;
		}
		
		if(!this.wrapper.isJumping() && this.wrapper.isSwimming)
		{
			float modifier = this.wrapper.stamina.fatigueLevel < 3 ? 1.0f : 0.5f;
			
			switch(this.topLevel)
			{
				case 0:
					this.player.motionY = 0.02f * modifier * (this.wrapper.JUMP.value + 0.2f);
					this.wrapper.stamina.changeBy(-(int) (modifier * 3));
					break;
				case 1:
					this.player.motionY = 0.008f * modifier * (this.wrapper.JUMP.value + 0.2f);
					this.wrapper.stamina.changeBy(-1);
					break;
			}
		}
	}
	
	public void jump(float speed)
	{
		this.player.motionY = speed;
		
		if(this.player.isPotionActive(Potion.jump))
		{
			this.player.motionY += (this.player.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F;
		}
		
		this.player.motionY *= this.wrapper.JUMP.value;
		
		if(this.isSprinting())
		{
			float f = this.player.rotationYaw * 0.018f;
			this.player.motionX -= MathHelper.sin(f) * 0.2F;
			this.player.motionZ += MathHelper.cos(f) * 0.2F;
		}
		
		this.player.isAirBorne = true;
		
		this.addStat(StatList.jumpStat, 1);
		
		if(this.isSprinting())
		{
			this.player.addExhaustion(0.8F);
		}
		else
		{
			this.player.addExhaustion(0.2F);
		}
		
		if(this.wrapper.isInLiquid())
			this.wrapper.setMaxJumpTicks(9);
		else
			this.wrapper.setMaxJumpTicks(3);
		
		this.wrapper.jump();
	}
	
	@Override
	public void jump()
	{
		jump(0.38f);
	}
}
