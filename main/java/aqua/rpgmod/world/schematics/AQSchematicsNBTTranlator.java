package aqua.rpgmod.world.schematics;

import net.minecraft.nbt.NBTTagCompound;
import aqua.rpgmod.service.resources.AQByteResource;

public class AQSchematicsNBTTranlator
{
	protected final NBTTagCompound compound;
	protected static final String key = "data";
	
	public AQSchematicsNBTTranlator(NBTTagCompound compound)
	{
		this.compound = compound;
	}
	
	public AQSchematicsNBTTranlator(AQSchematics schx, NBTTagCompound compound)
	{
		this.compound = compound;
		save(schx);
	}
	
	public void save(AQSchematics schx)
	{
		final AQByteResource resource = new AQByteResource();
		new AQSchematicsResource(schx, resource).save();
		
		if(resource.data != null)
			AQSchematicsNBTTranlator.this.compound.setByteArray(key, resource.data);
	}
	
	public AQSchematics load()
	{
		if(!this.compound.hasKey(key))
			return null;
		
		AQSchematicsResource resource = new AQSchematicsResource(new AQByteResource(this.compound.getByteArray(key)));
		resource.load();
		return resource.schx;
	}
}
