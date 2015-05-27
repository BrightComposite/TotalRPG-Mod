package aqua.rpgmod.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.EnumSkyBlock;

public class AQBonfireTileEntity extends TileEntity
{	
	public boolean isLit = false;
	public static final String key = "bnfr";
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);

		if(!compound.hasKey(key))
			return;
		
		NBTTagCompound tag = compound.getCompoundTag(key);
		
		this.isLit = tag.getBoolean("il");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);

		NBTTagCompound tag = new NBTTagCompound();
		tag.setBoolean("il", this.isLit);
		
		compound.setTag(key, tag);
	}
	
	public Packet getDescriptionPacket()
	{
	    NBTTagCompound tagCompound = new NBTTagCompound();
	    writeToNBT(tagCompound);
	    
	    return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, tagCompound);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet)
	{
		readFromNBT(packet.func_148857_g());
		this.worldObj.updateLightByType(EnumSkyBlock.Block, this.xCoord, this.yCoord, this.zCoord);
	}
}
