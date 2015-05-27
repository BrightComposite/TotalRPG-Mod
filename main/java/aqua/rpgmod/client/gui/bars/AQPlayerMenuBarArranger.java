package aqua.rpgmod.client.gui.bars;

public class AQPlayerMenuBarArranger extends AQBarArranger
{
	@Override
	public boolean place(int displayX, int displayY, int displayWidth, int displayHeight, int barWidth, int barHeight, int number, int type)
	{
		if(number == 0)
			this.y = displayY + 5;
		
		this.y += barHeight + 2;
		this.x = displayX + 60;
		
		return true;
	}
	
}
