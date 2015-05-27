package aqua.rpgmod.player.update;

import java.util.Iterator;
import java.util.LinkedList;

public class AQUpdateList implements AQUpdater
{
	protected LinkedList<AQUpdater> list = new LinkedList<AQUpdater>();
	
	public void add(AQUpdater updater)
	{
		this.list.add(updater);
	}
	
	public void remove(AQUpdater updater)
	{
		this.list.remove(updater);
	}
	
	@Override
	public void update()
	{
		for(Iterator<AQUpdater> i = this.list.iterator(); i.hasNext();)
			i.next().update();
	}
}
