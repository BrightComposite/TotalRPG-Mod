package aqua.rpgmod.world.schematics;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.Callable;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import aqua.rpgmod.block.AQBlocks;
import aqua.rpgmod.geometry.AQAxis3D;
import aqua.rpgmod.geometry.AQDimensions;
import aqua.rpgmod.geometry.AQPoint3D;
import aqua.rpgmod.service.resources.AQNamedResource;
import aqua.rpgmod.service.resources.AQResource;
import aqua.rpgmod.world.schematics.AQSchematics.AQBlockInfo;
import aqua.rpgmod.world.schematics.AQSchematics.AQTileInfo;

public class AQSchematicsResource extends AQSchematicsBase
{
	protected ArrayList<Block> blockList = new ArrayList<Block>();
	protected HashMap<String, Integer> blockMap = new HashMap<String, Integer>();
	protected AQResource resource;
	public AQSchematics schx;
	public boolean success = true;
	
	public AQSchematicsResource(AQResource resource)
	{
		this.resource = resource;
		
		if(resource instanceof AQNamedResource)
			((AQNamedResource) resource).domain = "schematics";
		
		this.schx = null;
	}
	
	public AQSchematicsResource(AQSchematics schx, AQResource resource)
	{
		this(resource);
		this.schx = schx;
	}
	
	protected int lookUp(Block block, String name)
	{
		Integer i = this.blockMap.get(name);
		
		if(i == null)
		{
			i = new Integer(this.blockList.size());
			this.blockMap.put(name, i);
			this.blockList.add(block);
		}
		
		return i.intValue();
	}
	
	public void load()
	{
		load(null);
	}
	
	public void load(final Callable callback)
	{
		this.success = false;
		
		if(!this.resource.canRead())
			return;
		
		Thread thread = new Thread("Load schematics")
		{
			@Override
			public void run()
			{
				try
				{
					final InputStream in = AQSchematicsResource.this.resource.getInput();
					
					if(in == null)
						return;
					
					final DataInputStream input = new DataInputStream(in);
					
					try
					{
						AQSchematicsResource.this.schx = new AQSchematics();
						readHeader(input);
						
						AQSchematicsResource.this.schx.load(new AQSchematicsAdapter()
						{
							@Override
							public AQDimensions dimensions()
							{
								return AQSchematicsResource.this.dim;
							}
							
							@Override
							public AQBlockInfo createInfo(AQSchematics schx, int x, int y, int z)
							{
								try
								{
									int index = input.readInt();
									int meta = input.readByte();
									
									Block block = AQSchematicsResource.this.blockList.get(index);
									
									if(block == null)
										block = AQBlocks.bad;
									
									return new AQBlockInfo(block, meta);
								}
								catch(IOException e)
								{
									e.printStackTrace();
									return new AQBlockInfo(Blocks.air, 0);
								}
							}
							
							@Override
							public void applyInfo(AQSchematics schx, AQBlockInfo info, int x, int y, int z)
							{}
							
							@Override
							public void afterLoad(AQSchematics schx)
							{}
							
							@Override
							public void afterSave(AQSchematics schx)
							{}
						});
						
						int count = input.readInt();
						
						for(int i = 0; i < count; ++i)
						{
							AQPoint3D pos = new AQPoint3D(input.readInt(), input.readInt(), input.readInt());
							NBTTagCompound compound = AQNbtFileManager.read("tileData", input);
							
							TileEntity tile = TileEntity.createAndLoadEntity(compound.getCompoundTag(String.valueOf(i)));
							AQSchematicsResource.this.schx.tileData.add(new AQTileInfo(tile, pos));
						}
						
						AQSchematicsResource.this.success = true;
						
						if(callback != null)
							callback.call();
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
					finally
					{
						AQSchematicsResource.this.resource.onClose(in);
						input.close();
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		};
		
		if(callback != null)
			thread.start();
		else
			thread.run();
	}
	
	public void save()
	{
		save(null);
	}
	
	public void save(final Callable callback)
	{
		this.dim = this.schx.dim;
		
		this.success = false;
		
		Thread thread = new Thread("Save schematics")
		{
			@Override
			public void run()
			{
				try
				{
					if(!AQSchematicsResource.this.resource.canWrite())
						return;
					
					final OutputStream out = AQSchematicsResource.this.resource.getOutput();
					
					if(out == null)
						return;
					
					final DataOutputStream output = new DataOutputStream(out);
					final ArrayList<AQBlockInfo> infoList = new ArrayList<AQBlockInfo>();
					
					try
					{
						AQSchematicsResource.this.schx.save(new AQSchematicsAdapter()
						{
							@Override
							public AQDimensions dimensions()
							{
								return AQSchematicsResource.this.dim;
							}
							
							@Override
							public AQBlockInfo createInfo(AQSchematics schx, int x, int y, int z)
							{
								return new AQBlockInfo(Blocks.air, 0);
							}
							
							@Override
							public void applyInfo(AQSchematics schx, AQBlockInfo info, int x, int y, int z)
							{
								lookUp(info.block, Block.blockRegistry.getNameForObject(info.block));
								infoList.add(info);
							}
							
							@Override
							public void afterLoad(AQSchematics schx)
							{	
								
							}
							
							@Override
							public void afterSave(AQSchematics schx)
							{	
								
							}
						});
						
						writeHeader(output);
						
						for(Iterator<AQBlockInfo> i = infoList.iterator(); i.hasNext();)
						{
							AQBlockInfo info = i.next();
							int index = lookUp(info.block, Block.blockRegistry.getNameForObject(info.block));
							output.writeInt(index);
							output.writeByte(info.meta);
						}
						
						while(AQSchematicsWorldAdapter.savingSchematics != null)
							this.wait(10);
						
						int count = AQSchematicsResource.this.schx.tileData.size();
						output.writeInt(count);
						
						for(int i = 0; i < count; ++i)
						{
							AQTileInfo info = AQSchematicsResource.this.schx.getTileInfo(i);
							
							if(info == null)
								continue;
							
							output.writeInt(info.pos.x);
							output.writeInt(info.pos.y);
							output.writeInt(info.pos.z);
							
							NBTTagCompound compound = new NBTTagCompound();
							NBTTagCompound tag = new NBTTagCompound();
							info.tile.writeToNBT(tag);
							compound.setTag(String.valueOf(i), tag);
							
							AQNbtFileManager.write("tileData", compound, output);
						}
						
						AQSchematicsResource.this.success = true;
						
						if(callback != null)
							callback.call();
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
					finally
					{
						AQSchematicsResource.this.resource.onClose(out);
						output.close();
					}
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		};
		
		if(callback != null)
			thread.start();
		else
			thread.run();
	}
	
	public void readHeader(DataInputStream input) throws IOException
	{
		while(true)
		{
			byte length = input.readByte();
			
			if(length == 0)
				break;
			
			byte[] s = new byte[length];
			input.readFully(s);
			
			String name = new String(s, Charset.defaultCharset());
			lookUp(Block.getBlockFromName(name), name);
		}
		
		for(AQAxis3D axis : AQAxis3D.values())
		{
			this.dim.set(axis, input.readInt());
		}
		
		input.readByte();
	}
	
	public void writeHeader(DataOutputStream output) throws IOException
	{
		for(Iterator<Block> i = this.blockList.iterator(); i.hasNext();)
		{
			String name = Block.blockRegistry.getNameForObject(i.next());
			int length = name.length();
			output.writeByte(length);
			output.write(name.getBytes(), 0, length);
		}
		
		output.writeByte(0);
		
		for(AQAxis3D axis : AQAxis3D.values())
		{
			output.writeInt(this.dim.get(axis));
		}
		
		output.writeByte('\n');
	}
}
