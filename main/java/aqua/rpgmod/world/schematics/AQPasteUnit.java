package aqua.rpgmod.world.schematics;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import aqua.rpgmod.AquaMod;
import aqua.rpgmod.geometry.AQBox;
import aqua.rpgmod.geometry.AQPoint3D;

public class AQPasteUnit
{
	public AQSchematics old;
	public AQSchematicsWorldAdapter adapter;
	public final AQPoint3D pos;
	public final World world;

	public AQPasteUnit(NBTTagCompound compound)
	{
		this.pos = new AQPoint3D(compound.getInteger("x"), compound.getInteger("y"), compound.getInteger("z"));
		this.world = AquaMod.proxy.getWorld(compound.getInteger("w"), true);
		this.old = new AQSchematicsNBTTranlator(compound).load();
		this.adapter = null;
	}
	
	public AQPasteUnit(World world, AQBox box)
	{
		this.pos = box.pos();
		this.world = world;
		this.old = new AQSchematics();
		this.adapter = new AQSchematicsWorldAdapter(world, box);
	}

	public void paste(AQSchematics schx)
	{
		if(this.adapter != null)
			this.old.load(this.adapter);
		
		new AQSchematicsWorldAdapter(schx, this.world, this.pos);
	}
	
	public boolean restore()
	{
		if(this.old == null)
			return false;
		
		new AQSchematicsWorldAdapter(this.old, this.world, this.pos);
		this.old = null;
		
		return true;
	}
	
    public void writeToNBT(NBTTagCompound compound)
    {
    	compound.setInteger("x", this.pos.x);
    	compound.setInteger("y", this.pos.y);
    	compound.setInteger("z", this.pos.z);
    	compound.setInteger("w", this.world.provider.dimensionId);
		new AQSchematicsNBTTranlator(this.old, compound);
    }
}
