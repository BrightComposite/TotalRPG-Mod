package aqua.rpgmod.player;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import api.player.server.ServerPlayerAPI;
import api.player.server.ServerPlayerBase;
import aqua.rpgmod.AquaMod;
import aqua.rpgmod.character.AQRace;

public class AQServerPlayerBase extends ServerPlayerBase
{
	public AQServerPlayerBase(ServerPlayerAPI paramServerPlayerAPI)
	{
		super(paramServerPlayerAPI);
	}
	
	@Override
	public void addExperienceLevel(int level)
	{
		super.addExperienceLevel(level);
		// AquaMod.playerNetwork.sendTo(new AQMessage(player, true,
		// PlayerMessage.ADDXP, Unpooled.buffer().writeInt(level)), player);
	}
	
	@Override
	public boolean isOnLadder()
	{
		if(super.isOnLadder())
			return true;
		
		AQPlayerWrapper wrapper = AquaMod.proxy.getWrapper(this.player);
		
		if(wrapper.getRace() == null)
			return false;
		
		return wrapper.getRace().canClimpUpTheWall() && wrapper.nearTheWall(-1.6f);
	}
	
	@Override
	public void mountEntity(Entity paramEntity)
	{
		AQPlayerWrapper wrapper = AquaMod.proxy.getWrapper(this.player);
		AQRace race = wrapper.getRace();
		
		if(race != null && race.canMountEntity())
			super.mountEntity(paramEntity);
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
		
		AQPlayerWrapper wrapper = AquaMod.proxy.getWrapper(this.player);
		
		if(wrapper == null)
			return;
		
		if(wrapper.player.isPlayerSleeping() || wrapper.player.isDead)
		{
			setSize(wrapper.player, 0.2f, 0.2f);
			return;
		}
		
		if(wrapper.race != null)
		{
			setSize(wrapper.player, wrapper.race.width(), wrapper.race.height());
			return;
		}
		
		setSize(wrapper.player, 0.6f, 1.8f);
	}
}
