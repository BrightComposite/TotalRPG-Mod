package aqua.rpgmod.block;

import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import aqua.rpgmod.AQModInfo;
import aqua.rpgmod.tileentity.AQBonfireTileEntity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class AQBlockBonfire extends BlockContainer
{
	public static final ResourceLocation stoneTex = new ResourceLocation(AQModInfo.MODID + ":" + "textures/blocks/stone.png");
	public static final ResourceLocation stickTex = new ResourceLocation(AQModInfo.MODID + ":" + "textures/blocks/log.png");
	
	protected AQBlockBonfire()
	{
		super(Material.rock);
		this.setBlockName("bonfire");
		AQBlocks.addBlock(this);
		this.setHardness(3.0f);
        this.setBlockBounds(0.1F, 0.0F, 0.1F, 0.9F, 0.3F, 0.9F);
	}
	
	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z)
	{
		AQBonfireTileEntity tile = (AQBonfireTileEntity)world.getTileEntity(x, y, z);
		return (tile != null && tile.isLit) ? 14 : 0;
	}
	 
	@Override
	public int getRenderType()
	{
		return -1;
	}
	 
    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }
    
    @Override
    public boolean renderAsNormalBlock()
    {
    	return false;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
    	this.blockIcon = iconRegister.registerIcon(AQModInfo.MODID + ":" + "stone");
    }
    
	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
	{
		return new AQBonfireTileEntity();
	}

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random)
    {
    	AQBonfireTileEntity tile = (AQBonfireTileEntity)world.getTileEntity(x, y, z);
		
		if(tile == null || !tile.isLit)
			return;
		
        float f  = x + 0.5f;
        float f1 = y + 0.25f + random.nextFloat() * 3.0f / 16.0f;
        float f2 = z + 0.5f;
        float f3 = random.nextFloat() * 0.2F - 0.1F;
        float f4 = random.nextFloat() * 0.2F - 0.1F;

    	world.spawnParticle("smoke", f + f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
    	world.spawnParticle("flame", f + f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
    }

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer p_149727_5_, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
	{
		if(world.isRemote)
			return true;
		
		AQBonfireTileEntity tile = (AQBonfireTileEntity)world.getTileEntity(x, y, z);
		
		if(tile != null)
		{
			tile.isLit = !tile.isLit;
			world.markBlockForUpdate(x, y, z);
			world.updateLightByType(EnumSkyBlock.Block, x, y, z);
		}

		return true;
	}
}
