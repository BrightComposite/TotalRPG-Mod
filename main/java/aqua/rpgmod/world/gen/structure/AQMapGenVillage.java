package aqua.rpgmod.world.gen.structure;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraft.world.gen.structure.StructureVillagePieces;

public class AQMapGenVillage extends MapGenVillage
{
	@Override
	protected boolean canSpawnStructureAtCoords(int p_75047_1_, int p_75047_2_)
	{
		return false;
	}
	
	@Override
	protected StructureStart getStructureStart(int p_75049_1_, int p_75049_2_)
	{
		return new MapGenVillage.Start(this.worldObj, this.rand, p_75049_1_, p_75049_2_, 0);
	}
	
	public static class Start extends StructureStart
	{
		private boolean hasMoreThanTwoComponents;
		
		public Start()
		{}
		
		public Start(World world, Random p_i2092_2_, int x, int z, int terrainType)
		{
			super(x, z);
			
			List list = StructureVillagePieces.getStructureVillageWeightedPieceList(p_i2092_2_, terrainType);
			StructureVillagePieces.Start start = new StructureVillagePieces.Start(
				world.getWorldChunkManager(),
				0,
				p_i2092_2_,
				(x << 4) + 2,
				(z << 4) + 2,
				list,
				terrainType);
			this.components.add(start);
			start.buildComponent(start, this.components, p_i2092_2_);
			List list1 = start.field_74930_j;
			List list2 = start.field_74932_i;
			int l;
			
			while(!list1.isEmpty() || !list2.isEmpty())
			{
				StructureComponent structurecomponent;
				
				if(list1.isEmpty())
				{
					l = p_i2092_2_.nextInt(list2.size());
					structurecomponent = (StructureComponent) list2.remove(l);
					structurecomponent.buildComponent(start, this.components, p_i2092_2_);
				}
				else
				{
					l = p_i2092_2_.nextInt(list1.size());
					structurecomponent = (StructureComponent) list1.remove(l);
					structurecomponent.buildComponent(start, this.components, p_i2092_2_);
				}
			}
			
			this.updateBoundingBox();
			l = 0;
			Iterator iterator = this.components.iterator();
			
			while(iterator.hasNext())
			{
				StructureComponent structurecomponent1 = (StructureComponent) iterator.next();
				
				if(!(structurecomponent1 instanceof StructureVillagePieces.Road))
				{
					++l;
				}
			}
			
			this.hasMoreThanTwoComponents = l > 2;
		}
		
		/**
		 * currently only defined for Villages, returns true if Village has more
		 * than 2 non-road components
		 */
		@Override
		public boolean isSizeableStructure()
		{
			return this.hasMoreThanTwoComponents;
		}
		
		@Override
		public void func_143022_a(NBTTagCompound p_143022_1_)
		{
			super.func_143022_a(p_143022_1_);
			p_143022_1_.setBoolean("Valid", this.hasMoreThanTwoComponents);
		}
		
		@Override
		public void func_143017_b(NBTTagCompound p_143017_1_)
		{
			super.func_143017_b(p_143017_1_);
			this.hasMoreThanTwoComponents = p_143017_1_.getBoolean("Valid");
		}
	}
}
