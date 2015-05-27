package aqua.rpgmod.geometry;

import java.util.ArrayList;

import net.minecraft.util.MathHelper;

public class AQBox extends AQShape3D
{
	protected AQPoint3D first;
	protected AQPoint3D second;
	
	public AQBox(AQPoint3D first, AQPoint3D second)
	{
		super(Type.BOX);
		set(first, second);
	}
	
	public AQBox(AQPoint3D pos, AQDimensions dim)
	{
		super(Type.BOX);
		set(pos, dim);
	}
	
	@Override
	public AQShape3D clone()
	{
		return new AQBox(this.first, this.second);
	}
	
	@Override
	public AQPoint3D pos()
	{
		return new AQPoint3D(this.first);
	}
	
	@Override
	public AQPoint3D getCenter()
	{
		return new AQPoint3D(this.first.plus(this.second).mul(0.5));
	}
	
	@Override
	public double radius()
	{
		AQDimensions dim = dimensions();
		return Math.sqrt(dim.x * dim.x + dim.y * dim.y + dim.z * dim.z);
	}
	
	@Override
	public double distanceTo(AQPoint3D p)
	{
		ArrayList<AQSide3D> list = new ArrayList<AQSide3D>();
		
		for(AQSide3D side : AQSide3D.values())
		{
			if(side.compare(getSide(side), p.get(side.axis)) >= 0)
				list.add(side);
		}
		
		double result = 0;
		
		for(AQSide3D side : list)
		{
			int value = p.get(side.axis) - getSide(side);
			result += value * value;
		}
		
		return Math.sqrt(result);
	}
	
	@Override
	public int getSide(AQSide3D side)
	{
		if(side.sign < 0)
			return this.first.get(side.axis);
		
		return this.second.get(side.axis);
	}
	
	@Override
	public AQQuad getSideQuad(AQSide3D side, double offset)
	{
		return new AQQuad(new AQPoint3Dd(this.first).plus(-offset), new AQPoint3Dd(this.second).plus(1.0 + offset), side);
	}
	
	public void setFirst(AQPoint3D value)
	{
		this.first = (AQPoint3D) value.clone();
		
		for(AQAxis3D axis : AQAxis3D.values())
			updateAxis(axis);
	}
	
	public void setSecond(AQPoint3D value)
	{
		this.second = (AQPoint3D) value.clone();
		
		for(AQAxis3D axis : AQAxis3D.values())
			updateAxis(axis);
	}
	
	@Override
	public void set(AQPoint3D pos, AQDimensions dim)
	{
		this.first = new AQPoint3D(pos);
		this.second = new AQPoint3D(pos.plus(dim).plus(-1));
	}
	
	@Override
	public void set(AQPoint3D first, AQPoint3D second)
	{
		this.first = new AQPoint3D(AQValue3D.generateMin(first, second));
		this.second = new AQPoint3D(AQValue3D.generateMax(first, second));
	}
	
	public void setSide(AQSide3D side, int value)
	{
		switch(side)
		{
			case LEFT:
				this.first.x = value;
				updateAxis(AQAxis3D.X);
				break;
			case RIGHT:
				this.second.x = value;
				updateAxis(AQAxis3D.X);
				break;
			case BOTTOM:
				this.first.y = value;
				updateAxis(AQAxis3D.Y);
				break;
			case TOP:
				this.second.y = value;
				updateAxis(AQAxis3D.Y);
				break;
			case BACK:
				this.first.z = value;
				updateAxis(AQAxis3D.Z);
				break;
			/* FRONT */default:
				this.second.z = value;
				updateAxis(AQAxis3D.Z);
				break;
		}
	}
	
	public void updateAxis(AQAxis3D axis)
	{
		int f = this.first.get(axis);
		int s = this.second.get(axis);
		
		if(f > s)
		{
			this.first.set(axis, s);
			this.second.set(axis, f);
		}
	}
	
	@Override
	public AQInterval<Integer> getInterval(AQAxis3D axis)
	{
		return new AQInterval<Integer>(Integer.valueOf(this.first.get(axis)), Integer.valueOf(this.second.get(axis)));
	}
	
	@Override
	public void setInterval(AQAxis3D axis, AQInterval<Integer> value)
	{
		this.first.set(axis, value.start.intValue());
		this.second.set(axis, value.end.intValue());
	}
	
	@Override
	public AQDimensions dimensions()
	{
		return new AQDimensions(this.second.x - this.first.x + 1, this.second.y - this.first.y + 1, this.second.z - this.first.z + 1);
	}
	
	@Override
	public int dimension(AQAxis3D axis)
	{
		return this.second.get(axis) - this.first.get(axis) + 1;
	}
	
	@Override
	public void extendBy(AQShape3D shape)
	{
		for(AQSide3D side : AQSide3D.values())
		{
			if(side.compare(shape.getSide(side), getSide(side)) > 0)
				setSide(side, shape.getSide(side));
		}
	}
	
	@Override
	public boolean contains(double x, double y, double z)
	{
		AQPoint3D pt = new AQPoint3D(MathHelper.floor_double(x), MathHelper.floor_double(y), MathHelper.floor_double(z));
		
		for(AQAxis3D axis : AQAxis3D.values())
		{
			if(!getInterval(axis).contains(Integer.valueOf(pt.get(axis))))
				return false;
		}
		
		return true;
	}
}
