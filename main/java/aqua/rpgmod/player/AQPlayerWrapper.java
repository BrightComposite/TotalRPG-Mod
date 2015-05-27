package aqua.rpgmod.player;

import io.netty.buffer.ByteBuf;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

import org.apache.logging.log4j.Level;

import aqua.rpgmod.AquaMod;
import aqua.rpgmod.character.AQRace;
import aqua.rpgmod.client.render.AQTextureMap;
import aqua.rpgmod.geometry.AQPoint3D;
import aqua.rpgmod.geometry.AQPoint3Dd;
import aqua.rpgmod.player.chat.AQChatMessage;
import aqua.rpgmod.player.chat.AQChatMessageBase;
import aqua.rpgmod.player.params.AQFoodStats;
import aqua.rpgmod.player.params.AQHunger;
import aqua.rpgmod.player.params.AQLifeParameter;
import aqua.rpgmod.player.params.AQStamina;
import aqua.rpgmod.player.params.AQThirst;
import aqua.rpgmod.player.update.AQUpdateList;
import aqua.rpgmod.player.update.AQUpdateTimer;
import aqua.rpgmod.player.update.AQUpdater;
import aqua.rpgmod.service.AQLogger;
import aqua.rpgmod.service.AQStringTranslator;
import aqua.rpgmod.service.network.AQMessage;
import aqua.rpgmod.service.network.AQMessage.PlayerMessage;
import aqua.rpgmod.service.network.AQTranslator;
import aqua.rpgmod.service.network.AQWorldTranslator;
import aqua.rpgmod.world.region.AQRegionSynchronizer;

public class AQPlayerWrapper<T extends EntityPlayer> implements IExtendedEntityProperties, AQUpdater
{
	public final T player;
	protected AQRace race = null;
	
	public float yOffset = 0.0f;
	public double safeY = 0.0;
	
	public static enum SkillModifier
	{
		BREAK,
		ARCHER_ACCURACY,
		ARCHER_TENSION,
		ARCHER_RELOAD_TIME;
	}
	
	HashMap<SkillModifier, Float> skillModifiers = new HashMap<SkillModifier, Float>();

	void addSkillModifier(SkillModifier type)
	{
		this.skillModifiers.put(type, new Float(1.0f));
	}

	public float getSkillModifier(SkillModifier type)
	{
		return this.skillModifiers.get(type).floatValue();
	}

	public void setSkillModifier(SkillModifier type, float value)
	{
		this.skillModifiers.put(type, new Float(value));
	}
	
	public class SkillModifierTranslator implements AQTranslator
	{
		final SkillModifier type;
		
		public SkillModifierTranslator(SkillModifier type)
		{
			this.type = type;
		}

		public SkillModifierTranslator(ByteBuf buf)
		{
			this.type = SkillModifier.values()[buf.readInt()];
			AQPlayerWrapper.this.skillModifiers.put(this.type, new Float(buf.readFloat()));
		}
		
		@Override
		public ByteBuf write(ByteBuf buf)
		{
			buf.writeInt(this.type.ordinal());
			buf.writeFloat(AQPlayerWrapper.this.skillModifiers.get(this.type).floatValue());
			
			return buf;
		}
	}
	
	public boolean isSwimming = false;
	public float lastAngle = Float.MAX_VALUE;
	public float swimAngle = 0.0f;
	public boolean canBreathe = true;
	
	protected Random rand = new Random();
	
	public AQUpdateList updaters = new AQUpdateList();
	
	public final ArrayList<AQLifeParameter> params = new ArrayList<AQLifeParameter>();
	
	public AQLifeParameter mana = new AQLifeParameter("mana", this, AQLifeParameter.Side.CLIENT, 0, 20)
	{
		@Override
		public void update()
		{
			changeBy(2);
		}
	};
	
	public AQStamina stamina = new AQStamina(this);
	public AQThirst thirst = new AQThirst(this);
	public AQLifeParameter airLevel = new AQLifeParameter("air", this, AQLifeParameter.Side.CLIENT, 400, 400)
	{
		@Override
		public void update()
		{
			AQPlayerWrapper.this.player.setAir(300);
		}
	};
	
	public AQHunger hunger = new AQHunger(this);
	
	public static final String propKey = "wrap";
	public AQTextureMap textures = new AQTextureMap();
	
	public static final int disableHelmet = 1;
	public static final int disableChest = 2;
	public static final int disablePants = 4;
	public static final int disableBoots = 8;
	public static final int disableLegs = disablePants | disableBoots;
	public static final int disableAll = disableHelmet | disableChest | disablePants | disableBoots;
	
	public AQRegionSynchronizer regionSyncronizer = null;
	public boolean isFlying = false;
	
	public AQPlayerWrapper(T player)
	{
		this.player = player;
		
		player.registerExtendedProperties(AQPlayerWrapper.propKey, this);
		
		for(SkillModifier mod : SkillModifier.values())
			addSkillModifier(mod);
	}
	
	public static boolean cantBeThis(EntityPlayer player)
	{
		return(player instanceof EntityPlayerMP || player instanceof EntityOtherPlayerMP);
	}
	
	public static AQPoint3Dd getPlayerPos(EntityPlayer player, float ticks)
	{
		double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * ticks;
		double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * ticks;
		double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * ticks;
		
		return new AQPoint3Dd(x, y, z);
	}
	
	public static AQPoint3Dd getPlayerPos(EntityPlayer player)
	{
		return new AQPoint3Dd(player.posX, player.posY, player.posZ);
	}
	
	public static AQPoint3D getPlayerServerPos(EntityPlayer player)
	{
		return new AQPoint3D(player.posX, player.posY, player.posZ);
	}
	
	public void initialize()
	{
		AQLogger.log(Level.INFO, "Create info for player " + getPlayerName());
		
		if(this.player instanceof EntityPlayerMP)
		{
			this.updaters.add(new AQUpdateTimer(new AQUpdater()
			{
				@Override
				public void update()
				{
					if(AQPlayerWrapper.this.race != null)
					{
						AQPlayerWrapper.this.updaters.remove(this);
						return;
					}
					
					AquaMod.playerNetwork.sendTo(new AQMessage(PlayerMessage.GET_RACE), (EntityPlayerMP) AQPlayerWrapper.this.player);
				}
			}, 20));

			this.updaters.add(new AQUpdater()
			{
				@Override
				public void update()
				{
					synchronize(SyncCause.SERVER_UPDATE);
				}
			});
		}
		
		if(this.player.worldObj.isRemote)
		{
			if(this.player instanceof EntityOtherPlayerMP)
			{
				AQThisPlayerWrapper.getWrapper().updaters.add(new AQUpdateTimer(new AQUpdater()
				{
					@Override
					public void update()
					{
						if(AQPlayerWrapper.this.race != null)
						{
							AQPlayerWrapper.this.updaters.remove(this);
							return;
						}
						
						AquaMod.playerNetwork.sendToServer(new AQMessage(
							PlayerMessage.GET_RACE,
							new AQWorldTranslator(AQPlayerWrapper.this.player.worldObj),
							new PlayerTranslator(AQPlayerWrapper.this.player)));
					}
				}, 20));
			}
		}
		
		this.updaters.add(new AQUpdateTimer(new AQUpdater()
		{
			@Override
			public void update()
			{
				if(AQPlayerWrapper.this.thirst.get() == 0.0f)
				{
					int source = AQDamageSource.indexOf(AQDamageSource.thirst);
					hurtPlayer(source, 1.0f);
				}
			}
			
		}, 80));
		
		this.updaters.add(new AQUpdateTimer(new AQUpdater()
		{
			@Override
			public void update()
			{
				AQPlayerWrapper.this.player.setAir(30);
				
				if(AQPlayerWrapper.this.airLevel.get() == 0)
				{
					AQPlayerWrapper.this.player.attackEntityFrom(DamageSource.drown, 2.0F);
				}
			}
		}, 32));
		
		this.updaters.add(new AQUpdateTimer(this.hunger, 80));
		this.updaters.add(this.airLevel);
		
		patchFood();
		
		this.regionSyncronizer = new AQRegionSynchronizer((AQPlayerWrapper) this);
	}
	
	protected void patchFood()
	{
		AQFoodStats stats = new AQFoodStats(this);
		Field field;
		
		try
		{
			field = EntityPlayer.class.getDeclaredField(AquaMod.inDevelopment() ? "foodStats" : "field_71100_bB");
			field.set(this.player, stats);
		}
		catch(NoSuchFieldException e)
		{
			e.printStackTrace();
		}
		catch(SecurityException e)
		{
			e.printStackTrace();
		}
		catch(IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch(IllegalAccessException e)
		{
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("static-method")
	public boolean isThis()
	{
		return false;
	}
	
	@SuppressWarnings("static-method")
	public boolean renderInMenu()
	{
		return false;
	}
	
	@Override
	public void saveNBTData(NBTTagCompound compound)
	{
		NBTTagCompound tag = new NBTTagCompound();
		
		AQLogger.log(Level.INFO, "Saving player info...");
		
		for(Iterator<AQLifeParameter> i = this.params.iterator(); i.hasNext();)
		{
			i.next().saveNBTData(tag);
		}

		tag.setBoolean("isf", this.isFlying);
		compound.setTag("i", tag);
	}
	
	@Override
	public void loadNBTData(NBTTagCompound compound)
	{
		NBTTagCompound tag = compound.getCompoundTag("i");
		
		if(tag == null)
		{
			reset();
			return;
		}
		
		AQLogger.log(Level.INFO, "Loading player info...");
		
		for(Iterator<AQLifeParameter> i = this.params.iterator(); i.hasNext();)
			i.next().loadNBTData(tag);
		
		this.isFlying = tag.getBoolean("isf");
	}
	
	@Override
	public void update()
	{
		this.updaters.update();
		this.player.eyeHeight = this.player.getDefaultEyeHeight() - this.yOffset / 1.2f;
		
		if(this.race == null)
			return;
		
		if(this.race.slowFalling() || this.player.isOnLadder() || this.player.onGround)
		{
			if(this.race.slowFalling() && this.player.motionY < -0.2 && !(this.player.isOnLadder() || this.player.onGround))
				this.player.motionY = -0.2;
			
			this.safeY = this.getPlayerMinY() + this.player.motionY;
		}
	}
	
	public class Translator implements AQTranslator
	{
		public final AQLifeParameter.Side side;
		
		public Translator(AQLifeParameter.Side side)
		{
			this.side = side;
		}
		
		public Translator(ByteBuf buf)
		{
			this.side = AQLifeParameter.Side.values()[buf.readInt()];
			
			for(Iterator<AQLifeParameter> i = AQPlayerWrapper.this.params.iterator(); i.hasNext();)
			{
				AQLifeParameter param = i.next();
				
				if(this.side == AQLifeParameter.Side.BOTH || param.side == this.side)
					param.read(buf);
			}
		}
		
		@Override
		public ByteBuf write(ByteBuf buf)
		{
			buf.writeInt(this.side.ordinal());
			
			for(Iterator<AQLifeParameter> i = AQPlayerWrapper.this.params.iterator(); i.hasNext();)
			{
				AQLifeParameter param = i.next();
				
				if(this.side == AQLifeParameter.Side.BOTH || param.side == this.side)
					param.write(buf);
			}
			
			return buf;
		}
	}
	
	public class RenderTranslator implements AQTranslator
	{
		public RenderTranslator()
		{}
		
		public RenderTranslator(ByteBuf buf)
		{
			AQPlayerWrapper.this.yOffset = buf.readFloat();
			AQPlayerWrapper.this.isSwimming = buf.readBoolean();
			AQPlayerWrapper.this.swimAngle = buf.readFloat();
			AQPlayerWrapper.this.lastAngle = buf.readFloat();
		}
		
		@Override
		public ByteBuf write(ByteBuf buf)
		{
			buf.writeFloat(AQPlayerWrapper.this.yOffset);
			buf.writeBoolean(AQPlayerWrapper.this.isSwimming);
			buf.writeFloat(AQPlayerWrapper.this.swimAngle);
			buf.writeFloat(AQPlayerWrapper.this.lastAngle);
			
			return buf;
		}
	}
	
	public static class PlayerTranslator implements AQTranslator
	{
		public final EntityPlayer player;
		
		public PlayerTranslator(EntityPlayer player)
		{
			this.player = player;
		}
		
		public PlayerTranslator(ByteBuf buf, World world)
		{
			this.player = world.getPlayerEntityByName(AQStringTranslator.deserialize(buf));
		}
		
		@Override
		public ByteBuf write(ByteBuf buf)
		{
			AQStringTranslator.serialize(buf, this.player != null ? this.player.getCommandSenderName() : "");
			return buf;
		}
	}
	
	public static enum SyncCause
	{
		CLIENT_RESPONSE,
		SERVER_RESPONSE,
		CLIENT_UPDATE,
		SERVER_UPDATE,
		CLIENT_QUERY,
		SERVER_QUERY;
	}
	
	public void synchronize(SyncCause cause)
	{
		switch(cause)
		{
			case CLIENT_QUERY:
				AquaMod.playerNetwork.sendToServer(new AQMessage(PlayerMessage.UPDATE));
				break;
			case CLIENT_RESPONSE:
				AquaMod.playerNetwork.sendToServer(new AQMessage(PlayerMessage.UPDATE, new Translator(AQLifeParameter.Side.BOTH)));
				break;
			case CLIENT_UPDATE:
				AquaMod.playerNetwork.sendToServer(new AQMessage(PlayerMessage.UPDATE, new Translator(AQLifeParameter.Side.CLIENT)));
				break;
			case SERVER_QUERY:
				AquaMod.playerNetwork.sendTo(new AQMessage(PlayerMessage.UPDATE), (EntityPlayerMP) this.player);
				break;
			case SERVER_RESPONSE:
				AquaMod.playerNetwork.sendTo(new AQMessage(PlayerMessage.UPDATE, new Translator(AQLifeParameter.Side.BOTH)), (EntityPlayerMP) this.player);
				break;
			case SERVER_UPDATE:
				AquaMod.playerNetwork.sendTo(new AQMessage(PlayerMessage.UPDATE, new Translator(AQLifeParameter.Side.SERVER)), (EntityPlayerMP) this.player);
				break;
			default:
		}
	}
	
	public void hurtPlayer(int damageSource, float amount)
	{
		this.player.attackEntityFrom(AQDamageSource.get(damageSource), amount);
	}
	
	public void reset()
	{
		AQLogger.log(Level.INFO, "Reset player parameters");
		
		for(Iterator<AQLifeParameter> i = this.params.iterator(); i.hasNext();)
			i.next().restore();
	}
	
	public String getPlayerName()
	{
		return this.player.getCommandSenderName();
	}
	
	public boolean isHungerModded()
	{
		return this.player.getFoodStats() instanceof AQFoodStats;
	}
	
	public int getHunger()
	{
		return MathHelper.floor_double(this.hunger.get());
	}
	
	public float getMana()
	{
		return this.player.capabilities.isCreativeMode ? Float.MAX_VALUE : this.mana.get();
	}
	
	public AQRace getRace()
	{
		return this.race;
	}
	
	public void setRace(String name)
	{
		if(name == null || name.isEmpty())
			return;
		
		this.race = AQRace.map.get(name);
		
		if(this.race == null)
			this.race = AQRace.defaultRace;
	}
	
	protected static float[][] getPosOffsets()
	{
		float w = 1.0f;// this.race.width() * 0.5f;
		
		return new float[][]
		{
			new float[]
			{
				-w, 0
			}, new float[]
			{
				-w, -w
			}, new float[]
			{
				-w, w
			}, new float[]
			{
				w, 0
			}, new float[]
			{
				w, -w
			}, new float[]
			{
				w, w
			}, new float[]
			{
				0, -w
			}, new float[]
			{
				0, w
			},
		};
	}
	
	public Block getBlockAt(float xOffset, float yOffset, float zOffset)
	{
		int x = MathHelper.floor_double(this.player.posX + xOffset);
		int y = MathHelper.floor_double(this.player.boundingBox.minY + this.player.yOffset - this.player.ySize + yOffset);
		int z = MathHelper.floor_double(this.player.posZ + zOffset);
		
		return this.player.worldObj.getBlock(x, y, z);
	}
	
	public boolean canCollideWithBlock(int x, int y, int z)
	{
		Block block = this.player.worldObj.getBlock(x, y, z);
		
		if(!block.getMaterial().blocksMovement())
			return false;
		
		AxisAlignedBB bb = block.getCollisionBoundingBoxFromPool(this.player.worldObj, x, y, z);
		return bb != null && bb.intersectsWith(this.player.boundingBox.expand(0.01, 0.01, 0.01));
	}
	
	public boolean hasAirAt(float xOffset, float yOffset, float zOffset)
	{
		return getBlockAt(xOffset, yOffset, zOffset).getMaterial() == Material.air;
	}
	
	public boolean hasLiquidAt(float xOffset, float yOffset, float zOffset)
	{
		return getBlockAt(xOffset, yOffset, zOffset) instanceof BlockLiquid;
	}
	
	public boolean hasStaticLiquidAt(float xOffset, float yOffset, float zOffset)
	{
		return getBlockAt(xOffset, yOffset, zOffset) instanceof BlockStaticLiquid;
	}
	
	public boolean isInLiquid()
	{
		return this.player.isInWater() || this.player.handleLavaMovement();
	}
	
	public boolean isInLiquid(float yOffset)
	{
		if(!isInLiquid())
			return false;
		
		double x = this.player.posX;
		double y = this.player.boundingBox.minY + this.player.yOffset - this.player.ySize + yOffset;
		double z = this.player.posZ;
		
		float[][] posOffsets = getPosOffsets();
		
		for(int j = 0; j < posOffsets.length; ++j)
		{
			if(this.player.worldObj.getBlock(
				MathHelper.floor_double(x + posOffsets[j][0]),
				MathHelper.floor_double(y),
				MathHelper.floor_double(z + posOffsets[j][1])).getMaterial() == Material.air)
				return false;
		}
		
		return true;
	}
	
	public boolean canCollideWithBlockAt(float xOffset, float yOffset, float zOffset)
	{
		int x = MathHelper.floor_double(this.player.posX + xOffset);
		int y = MathHelper.floor_double(this.player.boundingBox.minY + this.player.yOffset - this.player.ySize + yOffset);
		int z = MathHelper.floor_double(this.player.posZ + zOffset);
		
		return canCollideWithBlock(x, y, z);
	}
	
	public double getPlayerMinY()
	{
		return this.player.boundingBox.minY + this.player.yOffset - this.player.ySize;
	}
	
	public boolean nearTheWall(float yOffset)
	{
		double x = this.player.posX;
		double y = getPlayerMinY() + yOffset;
		double z = this.player.posZ;
		
		float[][] posOffsets = getPosOffsets();
		
		for(int j = 0; j < posOffsets.length; ++j)
		{
			if(canCollideWithBlock(MathHelper.floor_double(x + posOffsets[j][0]), MathHelper.floor_double(y), MathHelper.floor_double(z + posOffsets[j][1])))
				return true;
		}
		
		return false;
	}
	
	public boolean isMoving()
	{
		return this.player.motionX != 0.0f || this.player.motionY != 0.0f;
	}
	
	public void chatMessage(String msg)
	{
		new AQChatMessage(msg).send(this.player);
	}
	
	public void chatMessage(AQChatMessageBase msg)
	{
		msg.send(this.player);
	}
	
	@Override
	public void init(Entity entity, World world)
	{}
}
