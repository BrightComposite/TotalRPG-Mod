package aqua.rpgmod.world.schematics;

import net.minecraft.block.Block;
import aqua.rpgmod.geometry.AQDimensions;
import aqua.rpgmod.geometry.AQShape3D;
import aqua.rpgmod.world.schematics.AQSchematics.AQBlockInfo;

public class AQSchematicsFillAdapter implements AQSchematicsAdapter
{
	AQShape3D shape;
	Block block;
	int meta;
	
	public AQSchematicsFillAdapter(AQShape3D shape, Block block, int meta)
	{
		this.shape = shape;
		this.block = block;
		this.meta = meta;
	}
	
	@Override
	public AQDimensions dimensions()
	{
		return this.shape.dimensions();
	}
	
	@Override
	public AQBlockInfo createInfo(AQSchematics schx, int x, int y, int z)
	{
		return new AQBlockInfo(this.block, this.meta);
	}
	
	@Override
	public void applyInfo(AQSchematics schx, AQBlockInfo info, int x, int y, int z)
	{	
		
	}
	
	@Override
	public void afterLoad(AQSchematics schx)
	{	
		
	}
	
	@Override
	public void afterSave(AQSchematics schx)
	{	
		
	}
	
}
