package aqua.rpgmod.world.schematics;

import java.util.ArrayList;
import java.util.Iterator;

import aqua.rpgmod.block.AQBlocks;
import aqua.rpgmod.geometry.AQBox;
import aqua.rpgmod.geometry.AQPoint3D;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.ForgeDirection;

public class AQSchematics extends AQSchematicsBase implements IBlockAccess
{
	public static class AQBlockInfo
	{
		public final Block block;
		public final int meta;

		public AQBlockInfo()
		{
			this.block = AQBlocks.bad;
			this.meta = 0;
		}
		
		public AQBlockInfo(Block block, int meta)
		{
			this.block = block != null ? block : AQBlocks.bad;
			this.meta = meta;
		}
	}
	
	public static class AQTileInfo
	{
		public final TileEntity tile;
		public final AQPoint3D pos;
		
		public AQTileInfo(TileEntity tile, AQPoint3D pos)
		{
			this.tile = tile;
			this.pos = pos;
		}
	}
	
	protected AQBlockInfo data[][][] = null;
	protected final ArrayList<AQTileInfo> tileData = new ArrayList<AQTileInfo>();
	public AQSchematicsFilter filter = null;
	
	public void load(AQSchematicsAdapter adapter)
	{
		this.dim = adapter.dimensions();
		
		this.data = new AQBlockInfo[this.dim.z][][];
		
		for(int z = 0; z < this.dim.z; z++)
		{
			this.data[z] = new AQBlockInfo[this.dim.y][];
			
			for(int y = 0; y < this.dim.y; y++)
			{
				this.data[z][y] = new AQBlockInfo[this.dim.x];
				
				for(int x = 0; x < this.dim.x; x++)
				{
					AQBlockInfo info = adapter.createInfo(this, x, y, z);
					
					if(this.filter != null && !this.filter.allow(info.block))
						info = new AQBlockInfo();
					
					this.data[z][y][x] = info;
				}
			}
		}
		
		adapter.afterLoad(this);
	}
	
	public void save(AQSchematicsAdapter adapter)
	{
		for(int z = 0; z < this.dim.z; z++)
		{
			for(int y = 0; y < this.dim.y; y++)
			{
				for(int x = 0; x < this.dim.x; x++)
				{
					AQBlockInfo info = this.data[z][y][x];
					
					if(this.filter != null && !this.filter.allow(info.block))
						info = new AQBlockInfo();
					
					adapter.applyInfo(this, info, x, y, z);
				}
			}
		}
		
		adapter.afterSave(this);
	}
	
	public static enum BoxAlignment
	{
		START,
		CENTER,
		END;
	}
	
	public AQBox getBox(int x, int y, int z, BoxAlignment alignment)
	{
		AQPoint3D first;
		
		switch(alignment)
		{
			case START:
				first = new AQPoint3D(x, y, z);
				break;
			case CENTER:
				first = new AQPoint3D(x - MathHelper.floor_float((this.dim.x - 1) / 2.0f), y, z - MathHelper.floor_float((this.dim.z - 1) / 2.0f));
				break;
			default:
				first = new AQPoint3D(x - (this.dim.x - 1), y, z - (this.dim.z - 1));
		}
		
		AQPoint3D second = new AQPoint3D(first.x + this.dim.x - 1, first.y + this.dim.y - 1, first.z + this.dim.z - 1);
		
		return new AQBox(first, second);
	}
	
	public AQBox getBox(AQPoint3D pos, BoxAlignment alignment)
	{
		return getBox(pos.x, pos.y, pos.z, alignment);
	}
	
	public boolean outOfBounds(int x, int y, int z)
	{
		return x < 0 || x >= this.dim.x || y < 0 || y >= this.dim.y || z < 0 || z >= this.dim.z;
	}

	public AQTileInfo getTileInfo(int index)
	{
		AQTileInfo info = this.tileData.get(index);
		
		if(this.filter != null && !this.filter.allow(info.tile))
			return null;
		
		return info;
	}

	public int getTileInfoSize()
	{
		return this.tileData.size();
	}

	public void addTileInfo(TileEntity tile, AQPoint3D pos)
	{
		if(this.filter != null && !this.filter.allow(tile))
			return;
		
		this.tileData.add(new AQTileInfo(tile, pos));
	}
	
	@Override
	public Block getBlock(int x, int y, int z)
	{
		if(outOfBounds(x, y, z))
			return Blocks.air;
		
		return this.data[z][y][x].block;
	}

	@Override
	public TileEntity getTileEntity(int x, int y, int z)
	{
		for(Iterator<AQTileInfo> i = this.tileData.iterator(); i.hasNext();)
		{
			TileEntity tile = i.next().tile;
			
			if(tile.xCoord == x && tile.yCoord == y && tile.zCoord == z)
				return tile;
		}
		
		return null;
	}
	
	@SuppressWarnings(
	{
		"unused", "static-method"
	})
	@SideOnly(Side.CLIENT)
	public int getSkyBlockTypeBrightness(EnumSkyBlock p_72925_1_, int p_72925_2_, int p_72925_3_, int p_72925_4_)
	{
		return p_72925_1_.defaultLightValue;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getLightBrightnessForSkyBlocks(int p_72802_1_, int p_72802_2_, int p_72802_3_, int p_72802_4_)
	{
		int i1 = this.getSkyBlockTypeBrightness(EnumSkyBlock.Sky, p_72802_1_, p_72802_2_, p_72802_3_);
		int j1 = this.getSkyBlockTypeBrightness(EnumSkyBlock.Block, p_72802_1_, p_72802_2_, p_72802_3_);
		
		if(j1 < p_72802_4_)
		{
			j1 = p_72802_4_;
		}
		
		return i1 << 20 | j1 << 4;
	}
	
	@Override
	public int getBlockMetadata(int x, int y, int z)
	{
		if(outOfBounds(x, y, z))
			return 0;
		
		return this.data[z][y][x].meta;
	}
	
	@Override
	public int isBlockProvidingPowerTo(int p_72879_1_, int p_72879_2_, int p_72879_3_, int p_72879_4_)
	{
		return this.getBlock(p_72879_1_, p_72879_2_, p_72879_3_).isProvidingStrongPower(this, p_72879_1_, p_72879_2_, p_72879_3_, p_72879_4_);
	}
	
	@Override
	public boolean isAirBlock(int p_147437_1_, int p_147437_2_, int p_147437_3_)
	{
		Block block = this.getBlock(p_147437_1_, p_147437_2_, p_147437_3_);
		return block.isAir(this, p_147437_1_, p_147437_2_, p_147437_3_);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public BiomeGenBase getBiomeGenForCoords(int p_72807_1_, int p_72807_2_)
	{
		return BiomeGenBase.plains;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getHeight()
	{
		return this.dim.y;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean extendedLevelsInChunkCache()
	{
		return false;
	}
	
	@Override
	public boolean isSideSolid(int x, int y, int z, ForgeDirection side, boolean _default)
	{
		return getBlock(x, y, z).isSideSolid(this, x, y, z, side);
	}
}
