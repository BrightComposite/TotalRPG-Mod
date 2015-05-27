package aqua.rpgmod.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import aqua.rpgmod.world.schematics.AQPasteUnit;

public class AQTotemTileEntity extends TileEntity
{
	public AQPasteUnit schemeUnit = null;
	public static final String key = "schx";
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		
		if(!compound.hasKey(key))
			return;
		
		NBTTagCompound tag = compound.getCompoundTag(key);
		this.schemeUnit = new AQPasteUnit(tag);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		
		if(this.schemeUnit == null)
			return;
		
		NBTTagCompound tag = new NBTTagCompound();
		this.schemeUnit.writeToNBT(tag);
		compound.setTag(key, tag);
	}
}
