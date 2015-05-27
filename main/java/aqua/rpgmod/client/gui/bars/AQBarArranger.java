package aqua.rpgmod.client.gui.bars;

abstract public class AQBarArranger
{
	public static final AQBarArranger gameArranger = new AQGameBarArranger();
	public static final AQBarArranger menuArranger = new AQPlayerMenuBarArranger();
	
	int x = 0;
	int y = 0;
	int dir = 1;
	
	abstract public boolean place(int displayX, int displayY, int displayWidth, int displayHeight, int barWidth, int barHeight, int number, int type);
}
