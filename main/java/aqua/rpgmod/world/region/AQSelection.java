package aqua.rpgmod.world.region;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import aqua.rpgmod.geometry.AQPoint3D;
import aqua.rpgmod.geometry.AQQuad;
import aqua.rpgmod.geometry.AQShape3D;
import aqua.rpgmod.geometry.AQSide3D;
import aqua.rpgmod.player.AQPlayerWrapper;

public class AQSelection extends AQRegion
{
	public EntityPlayer player;
	public final float[] color;
	public float offset;
	
	AQSelection(String name)
	{
		super(name, RegionType.SELECTION);

		this.player = null;
		this.color = new float[]
		{
			0.0f, 1.0f, 0.0f
		};
		
		this.offset = 0.0f;
	}
	
	public AQSelection(String name, AQWorldRegion root, AQShape3D shape, EntityPlayer player)
	{
		super(name + " of " + player.getCommandSenderName(), RegionType.SELECTION, root, shape);

		this.player = player;
		this.color = new float[]
		{
			0.0f, 1.0f, 0.0f
		};

		this.offset = 0.0f;
	}
	
	public AQSelection(String name, AQWorldRegion root, AQPoint3D first, AQPoint3D second, EntityPlayer player)
	{
		super(name + " of " + player.getCommandSenderName(), RegionType.SELECTION, root, first, second);

		this.player = player;
		this.color = new float[]
		{
			0.0f, 1.0f, 0.0f
		};

		this.offset = 0.0f;
	}
	
	public AQSelection(String name, AQWorldRegion root, AQPoint3D first, AQPoint3D second, EntityPlayer player, float[] color, float offset)
	{
		super(name + " of " + player.getCommandSenderName(), RegionType.SELECTION, root, first, second);

		this.player = player;
		this.color = color;
		this.offset = offset;
	}
	
	@Override
	public AQQuad getSideQuad(AQSide3D side, double offset)
	{
		return super.getSideQuad(side, this.offset + offset);
	}
	
	@Override
	public boolean permanentView()
	{
		return true;
	}
	
	@Override
	public boolean shouldDraw()
	{
		return true;
	}
	
	@Override
	public float[] color()
	{
		return this.color;
	}
	
	@Override
	public ByteBuf writeInfo(ByteBuf buf)
	{
		buf.writeFloat(this.color[0]);
		buf.writeFloat(this.color[1]);
		buf.writeFloat(this.color[2]);
		buf.writeFloat(this.offset);
		
		new AQPlayerWrapper.PlayerTranslator(this.player).write(buf);
		
		return buf;
	}
	
	@Override
	public ByteBuf readInfo(ByteBuf buf)
	{
		this.color[0] = buf.readFloat();
		this.color[1] = buf.readFloat();
		this.color[2] = buf.readFloat();
		this.offset = buf.readFloat();
		
		this.player = new AQPlayerWrapper.PlayerTranslator(buf, this.provider.world).player;
		
		if(this.player == null)
			free();
		
		return buf;
	}
	
}
