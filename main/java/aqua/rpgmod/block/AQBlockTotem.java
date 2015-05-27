package aqua.rpgmod.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import aqua.rpgmod.service.resources.AQInternalResource;
import aqua.rpgmod.tileentity.AQTotemTileEntity;
import aqua.rpgmod.world.schematics.AQPasteUnit;
import aqua.rpgmod.world.schematics.AQSchematics;
import aqua.rpgmod.world.schematics.AQSchematics.BoxAlignment;
import aqua.rpgmod.world.schematics.AQSchematicsFilter;
import aqua.rpgmod.world.schematics.AQSchematicsResource;

public class AQBlockTotem extends BlockContainer
{
	public static AQSchematics schematics = null;
	
	public AQBlockTotem()
	{
		super(Material.wood);
		this.setBlockName("totem");
		AQBlocks.addBlock(this);
	}
	
	public static void init()
	{
		AQSchematicsResource file = new AQSchematicsResource(new AQInternalResource("totem"));
		file.load();
		schematics = file.schx;
	}
	
	@Override
	public void onPostBlockPlaced(World world, int x, int y, int z, int meta)
	{
		if(world.isRemote || schematics == null)
			return;
		
		TileEntity tile = world.getTileEntity(x, y, z);
		
		if(tile != null && tile instanceof AQTotemTileEntity)
		{
			AQTotemTileEntity totemTile = (AQTotemTileEntity) tile;
			
			totemTile.schemeUnit = new AQPasteUnit(world, schematics.getBox(x, y - 1, z, BoxAlignment.CENTER));
			totemTile.schemeUnit.old.filter = new AQSchematicsFilter();
			totemTile.schemeUnit.old.filter.add(totemTile);
			totemTile.schemeUnit.paste(schematics);
			totemTile.markDirty();
		}
	}
	
	@Override
	public void onBlockPreDestroy(World world, int x, int y, int z, int meta)
	{
		TileEntity tile = world.getTileEntity(x, y, z);
		
		if(tile != null && tile instanceof AQTotemTileEntity)
		{
			AQTotemTileEntity totemTile = (AQTotemTileEntity) tile;
			
			if(totemTile.schemeUnit != null)
			{	
				totemTile.schemeUnit.restore();
				totemTile.schemeUnit = null;
			}
		}
	}

	@Override
	public TileEntity createNewTileEntity(World world, int p_149915_2_)
	{
		return new AQTotemTileEntity();
	}
}
