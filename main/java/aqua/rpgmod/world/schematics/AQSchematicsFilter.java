package aqua.rpgmod.world.schematics;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

public class AQSchematicsFilter
{	
	protected ArrayList<Block> blocks = new ArrayList<Block>();
	protected ArrayList<TileEntity> tiles = new ArrayList<TileEntity>();
	protected boolean deny;

	public AQSchematicsFilter()
	{
		this.deny = true;
	}
	
	public AQSchematicsFilter(boolean deny)
	{
		this.deny = deny;
	}

	public void add(Block ... blocks)
	{
		this.blocks.addAll(Arrays.asList(blocks));
	}

	public void add(TileEntity ... tiles)
	{
		this.tiles.addAll(Arrays.asList(tiles));
	}
	
	public boolean allow(Block block)
	{
		return this.deny != this.blocks.contains(block);
	}
	
	public boolean allow(TileEntity tile)
	{
		return this.deny != this.tiles.contains(tile);
	}
}
