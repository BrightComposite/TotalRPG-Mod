package aqua.rpgmod.player.update;

public class AQUpdateTimer implements AQUpdater
{
	protected int tick = 0;
	protected int period;
	protected AQUpdater target = null;
	
	public AQUpdateTimer(int period)
	{
		this.period = period;
	}
	
	public AQUpdateTimer(AQUpdater target, int period)
	{
		this.target = target;
		this.period = period;
	}
	
	public void setTarget(AQUpdater target)
	{
		this.target = target;
	}
	
	public void setPeriod(int period)
	{
		this.period = period;
		this.tick %= period;
	}
	
	public int getTick()
	{
		return this.tick;
	}
	
	public int getPeriod()
	{
		return this.period;
	}
	
	@Override
	public void update()
	{
		if(this.tick++ == 0)
		{
			if(this.target != null)
				this.target.update();
		}
		
		this.tick %= this.period;
	}
}
