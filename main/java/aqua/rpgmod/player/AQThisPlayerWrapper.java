package aqua.rpgmod.player;

import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import aqua.rpgmod.AquaMod;
import aqua.rpgmod.character.AQCharacterManager;
import aqua.rpgmod.client.render.AQRegionRenderer;
import aqua.rpgmod.inventory.AQContainerPlayer;
import aqua.rpgmod.inventory.AQInventoryPlayer;
import aqua.rpgmod.player.rpg.AQAbility;
import aqua.rpgmod.player.update.AQUpdateTimer;
import aqua.rpgmod.player.update.AQUpdater;
import aqua.rpgmod.service.actions.AQPermissionManager;
import aqua.rpgmod.service.network.AQMessage;
import aqua.rpgmod.service.network.AQMessage.PlayerMessage;
import aqua.rpgmod.world.region.AQClientRegionManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class AQThisPlayerWrapper extends AQPlayerWrapper<EntityClientPlayerMP>
{
	static AQThisPlayerWrapper instance = null;
	
	protected int jumpTicks = 0;
	protected int maxJumpTicks = 3;
	
	public boolean renderInMenu = false;
	
	public AQPlayerModifier STEP_HEIGHT = new AQPlayerModifier(0.5f);
	public AQPlayerModifier SPEED = new AQPlayerModifier(1.0f);
	public AQPlayerModifier JUMP = new AQPlayerModifier(1.0f);
	public AQPlayerModifier WATER_SPEED = new AQPlayerModifier(0.75f);
	public AQPlayerModifier Y_OFFSET = new AQPlayerModifier(0.0f);
	
	public AQThisPlayerWrapper(AQPlayerWrapper<EntityClientPlayerMP> wrapper)
	{
		super(wrapper.player);
	}
	
	public AQThisPlayerWrapper(final EntityClientPlayerMP player)
	{
		super(player);
		instance = this;
		
		AQClientRegionManager.instance.createProvider(player.worldObj);
	}

	public void setSkillModifier(SkillModifier type, float value)
	{
		super.setSkillModifier(type, value);
		
		AquaMod.playerNetwork.sendToServer(new AQMessage(PlayerMessage.SET_SKILL_MODIFIER, this.new SkillModifierTranslator(type)));
	}
	
	public static AQThisPlayerWrapper getWrapper()
	{
		return instance;
	}
	
	@Override
	public boolean isThis()
	{
		return true;
	}
	
	@Override
	public boolean renderInMenu()
	{
		return this.renderInMenu;
	}
	
	@Override
	public void initialize()
	{
		super.initialize();
		
		this.race = AQCharacterManager.instance.race;
		this.race.setModifiers(this);
		
		this.player.stepHeight = this.STEP_HEIGHT.value;
		this.yOffset = this.Y_OFFSET.def;
		this.player.eyeHeight = this.player.getDefaultEyeHeight() - this.yOffset;
		
		this.player.width = this.race.width();
		this.player.height = this.race.height();
		
		if(AQPermissionManager.commandsAllowed(this.player))
			AQRegionRenderer.instance.shouldRender = true;
		
		synchronize(SyncCause.CLIENT_QUERY);
		
		if(this.player.inventoryContainer instanceof AQContainerPlayer)
		{
			AQContainerPlayer container = (AQContainerPlayer) this.player.inventoryContainer;
			container.updateSlots(this);
		}
		
		this.updaters.add(new AQUpdater()
		{
			@Override
			public void update()
			{
				synchronize(SyncCause.CLIENT_UPDATE);
			}
		});
		
		this.updaters.add(new AQUpdater()
		{
			@Override
			public void update()
			{
				if(AQThisPlayerWrapper.this.isJumping())
				{
					AQThisPlayerWrapper.this.jumpTicks--;
					
					if(AQThisPlayerWrapper.this.JUMP.value > 0)
					{
						AQThisPlayerWrapper.this.player.motionY += isInLiquid() ? 0.02f : 0.03;
					}
				}
				
				AQThisPlayerWrapper.this.player.setJumping(false);
			}
		});
		
		this.updaters.add(new AQUpdateTimer(new AQUpdater()
		{
			@Override
			public void update()
			{
				AQThisPlayerWrapper.this.yOffset = AQThisPlayerWrapper.this.Y_OFFSET.value;
				AquaMod.playerNetwork.sendToServer(new AQMessage(PlayerMessage.RENDER, new RenderTranslator()));
			}
		}, 4));
		
		this.updaters.add(new AQUpdater()
		{
			@Override
			public void update()
			{
				if(!AQThisPlayerWrapper.this.canBreathe)
				{
					if(!AQThisPlayerWrapper.this.player.canBreatheUnderwater() && !AQThisPlayerWrapper.this.player.isPotionActive(Potion.waterBreathing.id))
					{
						if(AQThisPlayerWrapper.this.airLevel.get() == 0)
						{
							for(int i = 0; i < 8; ++i)
							{
								float f = AQThisPlayerWrapper.this.rand.nextFloat() - AQThisPlayerWrapper.this.rand.nextFloat();
								float f1 = AQThisPlayerWrapper.this.rand.nextFloat() - AQThisPlayerWrapper.this.rand.nextFloat();
								float f2 = AQThisPlayerWrapper.this.rand.nextFloat() - AQThisPlayerWrapper.this.rand.nextFloat();
								AQThisPlayerWrapper.this.player.worldObj.spawnParticle(
									"bubble",
									AQThisPlayerWrapper.this.player.posX + f,
									AQThisPlayerWrapper.this.player.posY + f1,
									AQThisPlayerWrapper.this.player.posZ + f2,
									AQThisPlayerWrapper.this.player.motionX,
									AQThisPlayerWrapper.this.player.motionY,
									AQThisPlayerWrapper.this.player.motionZ);
							}
						}
						else
						{
							decreaseAirSupply();
						}
					}
				}
				else
				{
					AQThisPlayerWrapper.this.airLevel.restore();
				}
			}
		});
		
		this.updaters.add(this.stamina);
		this.updaters.add(this.thirst);
		this.updaters.add(new AQUpdateTimer(this.mana, 80));
		
		AQAbility.updateFromServer();
	}
	
	public void setMaxJumpTicks(int maxJumpTicks)
	{
		this.maxJumpTicks = maxJumpTicks;
	}
	
	public void jump()
	{
		this.jumpTicks = this.maxJumpTicks;
	}
	
	public boolean isJumping()
	{
		return this.jumpTicks > 0;
	}
	
	public boolean canClimbBlock(int yOffset)
	{
		if(!this.player.isCollidedHorizontally || this.player.movementInput.moveForward <= 0.0f)
			return false;
		
		Vec3 v = this.player.getLookVec();
		
		int x = MathHelper.floor_double(this.player.posX + v.xCoord);
		int y = MathHelper.floor_double(this.player.boundingBox.minY + yOffset);
		int yh = MathHelper.floor_double(this.player.boundingBox.maxY + yOffset);
		int z = MathHelper.floor_double(this.player.posZ + v.zCoord);
		
		return canCollideWithBlock(x, y, z) && !canCollideWithBlock(x, yh, z);
	}
	
	public void decreaseAirSupply()
	{
		int j = EnchantmentHelper.getRespiration(this.player);
		
		if(j <= 0 || this.rand.nextInt(j + 1) == 0)
			this.airLevel.changeBy(-1);
	}
	
	@SideOnly(Side.CLIENT)
	public static boolean isArmorDisabled(int armorType)
	{
		int armor = 1 << armorType;
		return (instance.race.disabledArmor() & armor) == armor;
	}
	
	@SuppressWarnings("unused")
	@SideOnly(Side.CLIENT)
	public void onCurrentItemChange(AQInventoryPlayer inventory, ItemStack currentItem)
	{
		// AQLogger.log(Level.INFO, "Change current item");
	}
	
	@Override
	public void reset()
	{
		super.reset();
		synchronize(SyncCause.CLIENT_UPDATE);
	}
}
