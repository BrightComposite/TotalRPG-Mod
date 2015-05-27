package aqua.rpgmod.world.region;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import aqua.rpgmod.geometry.AQBox;
import aqua.rpgmod.geometry.AQPoint3D;
import aqua.rpgmod.geometry.AQPoint3Dd;
import aqua.rpgmod.geometry.AQQuad;
import aqua.rpgmod.geometry.AQShape3D;
import aqua.rpgmod.geometry.AQSide3D;
import aqua.rpgmod.service.AQStringTranslator;
import aqua.rpgmod.service.network.AQTranslator;

abstract public class AQRegion implements Comparable<AQRegion>
{
	public static enum RegionType
	{
		NONE,
		ROOT,
		SELECTION,
		PROTECTION;
	}
	
	public AQRegionProvider provider;
	public AQShape3D shape;
	
	public String name;
	public final RegionType type;
	
	private final HashMap<String, AQRegion> children = new HashMap<String, AQRegion>();
	protected AQRegion parent = null;
	
	public AQRegion(String name, RegionType type, AQRegion parent, AQShape3D shape)
	{
		if(name == null || name.isEmpty())
			name = "region";
		
		this.name = name;
		this.type = type;
		this.shape = shape;
		
		if(parent == null)
		{
			this.provider = null;
			return;
		}
		
		parent.addChild(this);
	}
	
	AQRegion(String name, RegionType type)
	{
		this.name = name;
		this.type = type;
	}
	
	public AQRegion(String name, RegionType type, AQRegion parent, AQPoint3D first, AQPoint3D second)
	{
		this(name, type, parent, new AQBox(first, second));
	}
	
	public static AQRegion create(String name, RegionType type, AQRegionProvider provider)
	{
		switch(type)
		{
			case ROOT:
				return new AQWorldRegion(provider);
				
			case SELECTION:
				return new AQSelection(name);
				
			case PROTECTION:
				return new AQProtectionRegion(name);
				
			default:
				return null;
		}
	}

	public void add()
	{
		this.provider.manager.addRegion(this);
	}
	
	public void update()
	{
		this.provider.manager.addRegion(this);
	}
	
	public void free()
	{
		if(this.parent != null)
			this.parent.removeChild(this);
		
		this.provider.removeRegion(this);
		this.provider.manager.removeRegion(this);
	}
	
	@Override
	public int compareTo(AQRegion region)
	{
		return this.name.compareTo(region.name);
	}

	synchronized public ArrayList<AQRegion> getChildren()
	{
		return new ArrayList<AQRegion>(this.children.values());
	}

	public AQRegion getParent()
	{
		return this.parent;
	}
	
	public void addChild(AQRegion child)
	{
		this.shape.extendBy(child.shape);
		
		child.parent = this;
		child.provider = this.provider;

		synchronized(this)
		{
			this.children.put(child.name, child);
		}
		
		this.provider.registerRegion(child);
		
		update();
	}
	
	public void removeChild(AQRegion child)
	{
		child.parent = this.parent;
		
		synchronized(this)
		{
			this.children.remove(child.name);
		}
	}
	
	public void clear()
	{
		for(Iterator<AQRegion> i = getChildren().iterator(); i.hasNext();)
			i.next().parent = this.parent;
		
		synchronized(this)
		{
			this.children.clear();
		}
	}
	
	public List<AQRegion> findAllRegionsAt(AQPoint3D point)
	{
		ArrayList<AQRegion> list = new ArrayList<AQRegion>();
		
		if(!this.shape.contains(point))
			return list;
		
		list.add(this);

		for(Iterator<AQRegion> i = getChildren().iterator(); i.hasNext();)
			list.addAll(i.next().findAllRegionsAt(point));
		
		return list;
	}
	
	public List<AQRegion> findAllRegionsAt(AQPoint3Dd point)
	{
		return findAllRegionsAt(new AQPoint3D(point));
	}
	
	public List<AQRegion> findAllRegionsAt(int x, int y, int z)
	{
		return findAllRegionsAt(new AQPoint3D(x, y, z));
	}
	
	public String getPath()
	{
		if(this.parent != null)
			return this.parent.getPath() + "/" + this.name;
		
		return this.name;
	}
	
	public AQQuad getSideQuad(AQSide3D side, double offset)
	{
		return this.shape.getSideQuad(side, offset);
	}
	
	abstract public boolean shouldDraw();
	
	abstract public boolean permanentView();
	
	abstract public float[] color();
	
	public static class Translator implements AQTranslator
	{
		AQRegion region;
		
		public Translator(final ByteBuf buffer, final AQRegionProvider provider)
		{
			Translator.this.region = deserialiaze(buffer, provider);
		}
		
		public Translator(AQRegion region)
		{
			this.region = region;
		}
		
		@Override
		public ByteBuf write(ByteBuf buf)
		{
			return serialiaze(buf, this.region);
		}
	}
	
	@SuppressWarnings("static-method")
	public ByteBuf writeInfo(ByteBuf buf)
	{
		return buf;
	}
	
	@SuppressWarnings("static-method")
	public ByteBuf readInfo(ByteBuf buf)
	{
		return buf;
	}
	
	public static ByteBuf serialiaze(ByteBuf buffer, AQRegion region)
	{
		buffer.writeInt(region.type.ordinal());
		AQStringTranslator.serialize(buffer, region.name);
		AQStringTranslator.serialize(buffer, region.parent != null && region.parent != region.provider.root ? region.parent.name : ".");
		
		if(region.type != RegionType.ROOT)
			AQShape3D.serialiaze(buffer, region.shape);
		
		region.writeInfo(buffer);

		buffer.writeByte('\n');
		return buffer;
	}
	
	public static AQRegion deserialiaze(ByteBuf buffer, AQRegionProvider provider)
	{
		RegionType type = RegionType.values()[buffer.readInt()];
		String name = AQStringTranslator.deserialize(buffer);
		String parentName = AQStringTranslator.deserialize(buffer);
		AQRegion parent = provider.findRegion(parentName);
		
		if(parent == null)
			parent = provider.root;
		
		AQRegion region = create(name, type, provider);
		
		if(region != null && region.type != RegionType.ROOT)
		{
			region.shape = AQShape3D.deserialiaze(buffer);
			parent.addChild(region);
			region.readInfo(buffer);
		}
		
		buffer.readByte();
		return region;
	}
	
	public static class Filter
	{
		Set<RegionType> ignoringTypes = new HashSet<AQRegion.RegionType>();
		
		public Filter(RegionType ... types)
		{
			for(RegionType type : types)
				this.ignoringTypes.add(type);
		}
		
		public boolean allow(AQRegion region)
		{
			if(this.ignoringTypes.contains(region.type))
				return false;
			
			return filter(region);
		}
		
		@SuppressWarnings(
		{
			"unused", "static-method"
		})
		protected boolean filter(AQRegion region)
		{
			return true;
		}
	}
	
	protected static void addRegion(final ArrayList<AQRegion> list, final AQRegion region, final Filter filter)
	{
		if(filter.allow(region))
		{
			if(region.type == RegionType.SELECTION)
			{
				AQSelection selection = (AQSelection) region;
				
				if(selection.player != null && !selection.player.isEntityAlive())
				{
					selection.player = null;
					return;
				}
			}
			
			list.add(region);
		}
		
		for(Iterator<AQRegion> i = region.getChildren().iterator(); i.hasNext();)
			addRegion(list, i.next(), filter);
	}
	
	public static ByteBuf writeRegions(final AQRegionProvider provider, final ByteBuf buf, final Filter filter)
	{
		ArrayList<AQRegion> list = new ArrayList<AQRegion>();
		addRegion(list, provider.root, filter);
		
		buf.writeInt(list.size());
		
		for(Iterator<AQRegion> i = list.iterator(); i.hasNext();)
			AQRegion.serialiaze(buf, i.next());
		
		return buf;
	}
	
	public static ByteBuf readRegions(final ByteBuf buf, final AQRegionProvider provider)
	{
		provider.synchronize(new Runnable()
		{
			@Override
			public void run()
			{
				provider.root.clear();
				provider.registry.clear();
				
				int count = buf.readInt();
				
				for(int i = 0; i < count; ++i)
					AQRegion.deserialiaze(buf, provider);
			}
		});

		return buf;
	}
}
