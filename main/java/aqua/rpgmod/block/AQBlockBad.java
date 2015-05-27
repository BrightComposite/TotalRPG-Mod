package aqua.rpgmod.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class AQBlockBad extends Block
{
	protected AQBlockBad()
	{
		super(Material.snow);
		this.setBlockName("bad");
		AQBlocks.addBlock(this);
	}
	
}
