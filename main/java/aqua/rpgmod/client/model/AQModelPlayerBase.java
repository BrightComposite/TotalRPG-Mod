package aqua.rpgmod.client.model;

import java.util.HashMap;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import api.player.model.ModelPlayerAPI;
import api.player.model.ModelPlayerBase;
import aqua.rpgmod.AquaMod;
import aqua.rpgmod.character.AQRace;
import aqua.rpgmod.player.AQPlayerWrapper;

public class AQModelPlayerBase extends ModelPlayerBase
{
	protected HashMap<AQRace, AQModelPlayer> models;
	
	private EntityPlayer currentPlayer = null;
	private AQModelPlayer currentModel = null;
	private int type;
	private static int typeInit = 0;
	
	public AQModelPlayerBase(ModelPlayerAPI modelplayerapi)
	{
		super(modelplayerapi);
		
		this.type = typeInit++;
		this.models = AQModelManager.createMap();
	}
	
	@Override
	public void render(Entity entity, float f1, float f2, float f3, float f4, float f5, float f6)
	{
		if(entity instanceof EntityPlayer)
		{
			AQPlayerWrapper wrapper = AquaMod.proxy.getWrapper((EntityPlayer) entity);
			
			if(wrapper != null)
			{
				updateModel(wrapper);
				this.currentModel.setRotationAngles(this.modelPlayer, f1, f2, f3, f4, f5, f6, entity, this.type);
				this.currentModel.render(wrapper.player, this.modelPlayer, f6, wrapper.textures, this.type);
				return;
			}
		}
		
		super.render(entity, f1, f2, f3, f4, f5, f6);
	}
	
	protected void updateModel(AQPlayerWrapper wrapper)
	{
		if(this.currentPlayer == wrapper.player && this.currentModel != null && this.currentModel.race == wrapper.getRace())
			return;
		
		this.currentPlayer = wrapper.player;
		this.currentModel = this.models.get(wrapper.getRace());
		
		if(this.currentModel == null)
			this.currentModel = new AQModelHuman();
		
		this.currentModel.setModel(this.modelPlayer);
	}
}
