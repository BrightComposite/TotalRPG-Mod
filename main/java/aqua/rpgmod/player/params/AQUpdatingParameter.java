package aqua.rpgmod.player.params;

import aqua.rpgmod.player.update.AQUpdater;

public class AQUpdatingParameter extends AQRangedParameter implements AQUpdater
{
	public AQUpdatingParameter(float value, float max)
	{
		super(value, max);
	}
	
	@Override
	public void update()
	{}
}
