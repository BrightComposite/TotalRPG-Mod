package aqua.rpgmod.world.schematics;

import aqua.rpgmod.geometry.AQDimensions;
import aqua.rpgmod.world.schematics.AQSchematics.AQBlockInfo;

public interface AQSchematicsAdapter
{
	public AQDimensions dimensions();
	
	public AQBlockInfo createInfo(AQSchematics schx, int x, int y, int z);
	
	public void applyInfo(AQSchematics schx, AQBlockInfo info, int x, int y, int z);
	
	public void afterLoad(AQSchematics schx);
	
	public void afterSave(AQSchematics schx);
}
