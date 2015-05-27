package aqua.rpgmod.geometry;

public class AQInterval<T extends Comparable<T>>
{
	public T start, end;
	
	public AQInterval(T start, T end)
	{
		this.start = start;
		this.end = end;
	}
	
	public boolean contains(T value)
	{
		return value.compareTo(this.start) >= 0 && value.compareTo(this.end) <= 0;
	}
}
