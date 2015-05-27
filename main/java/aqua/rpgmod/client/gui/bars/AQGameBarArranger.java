package aqua.rpgmod.client.gui.bars;

import java.util.HashMap;

import aqua.rpgmod.client.gui.bars.AQBarRenderer.Type;

public class AQGameBarArranger extends AQBarArranger
{
	int rightY, leftY;
	
	protected HashMap<Integer, Byte> directions = new HashMap<Integer, Byte>();
	
	public AQGameBarArranger()
	{
		setDirection(Type.HEALTH.ordinal(), (byte) -1);
		setDirection(Type.HEALTHMOUNT.ordinal(), (byte) -1);
		setDirection(Type.MANA.ordinal(), (byte) 1);
		setDirection(Type.ARMOR.ordinal(), (byte) -1);
		setDirection(Type.STAMINA.ordinal(), (byte) 1);
		setDirection(Type.JUMP.ordinal(), (byte) 1);
		setDirection(Type.AIR.ordinal(), (byte) 1);
	}
	
	public void setDirection(int barType, byte dir)
	{
		this.directions.put(Integer.valueOf(barType), Byte.valueOf(dir));
	}
	
	@Override
	public boolean place(int displayX, int displayY, int displayWidth, int displayHeight, int barWidth, int barHeight, int number, int type)
	{
		if(number == 0)
			this.rightY = this.leftY = displayHeight - 30;
		
		Byte d = this.directions.get(Integer.valueOf(type));
		
		if(d == null)
			return false;
		
		this.dir = d.byteValue();
		this.x = displayWidth / 2;
		
		if(this.dir < 0)
			this.y = (this.rightY -= barHeight + 1);
		else
			this.y = (this.leftY -= barHeight + 1);
		
		return true;
	}
}
