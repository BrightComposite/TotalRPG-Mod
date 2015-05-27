package aqua.rpgmod.world.region.actions;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import aqua.rpgmod.geometry.AQBox;
import aqua.rpgmod.geometry.AQPoint3D;
import aqua.rpgmod.geometry.AQPoint3Dd;
import aqua.rpgmod.player.AQPlayerWrapper;
import aqua.rpgmod.player.AQThisPlayerWrapper;
import aqua.rpgmod.service.actions.AQAction;
import aqua.rpgmod.service.actions.AQActionManager;
import aqua.rpgmod.world.region.AQRegionManager;
import aqua.rpgmod.world.region.AQRegionProvider;
import aqua.rpgmod.world.region.AQSelection;
import aqua.rpgmod.world.schematics.AQSchematics;
import aqua.rpgmod.world.schematics.AQSchematics.BoxAlignment;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class AQRegionActionManager extends AQActionManager
{
	abstract public static class RegionAction<A extends RegionAction> extends AQAction<AQRegionActionManager, A>
	{
		public RegionAction(AQRegionActionManager manager)
		{
			super(manager);
		}
		
		public RegionAction(AQRegionActionManager manager, EntityPlayer player, List<String> params)
		{
			super(manager, player, params);
		}
		
		@Override
		public boolean needPermission()
		{
			return false;
		}
	}
	
	public static final AQRegionActionManager instance = new AQRegionActionManager();
	
	AQSchematics buffer = null;
	AQSelection selection = null;
	
	public static final String selectionName = "selection";
	
	public AQPoint3D[] points = new AQPoint3D[3];
	
	public AQRegionActionManager()
	{
		addAction(new AQPaste(this));
		addAction(new AQCopy(this));
		addAction(new AQSave(this));
		addAction(new AQLoad(this));
		addAction(new AQPoint(this));
		addAction(new AQSelect(this));
		addAction(new AQDeselect(this));
		addAction(new AQProtect(this));
		addAction(new AQFree(this));
		addAction(new AQFlags(this));
		addAction(new AQPlayers(this));
		addAction(new AQFill(this));
		addAction(new AQList(this));
	}
	
	@Override
	public String getName()
	{
		return "region";
	}
	
	public AQPoint3D getPoint(int index)
	{
		return this.points[index];
	}
	
	public AQSelection getSelection()
	{
		return this.selection;
	}
	
	public void freeSelection()
	{
		this.selection.free();
		this.selection = null;
	}
	
	public AQSchematics getBuffer()
	{
		return this.buffer;
	}
	
	@SideOnly(Side.CLIENT)
	public void setPoint(int index, AQPoint3D point)
	{
		EntityPlayer player = AQThisPlayerWrapper.getWrapper().player;
		
		if(index == 2)
		{
			this.points[2] = point;
			
			AQBox box = this.buffer.getBox(this.points[2], BoxAlignment.START);
			
			if(this.selection != null)
			{
				this.selection.shape = box;
				this.selection.update();
				return;
			}
			
			AQRegionProvider provider = AQRegionManager.getProviderForPlayer(player);
			this.selection = new AQSelection(selectionName, provider.root, box, player);
			this.selection.add();
			
			return;
		}
		
		this.points[index] = point;
		
		if(this.selection != null)
		{
			this.selection.shape.set(this.points[0], this.points[1]);
			this.selection.update();
		}
	}

	@SideOnly(Side.CLIENT)
	public boolean createSelection()
	{
		if(this.points[0] == null || this.points[1] == null)
			return false;

		EntityPlayer player = AQThisPlayerWrapper.getWrapper().player;
		
		AQRegionProvider provider = AQRegionManager.getProviderForPlayer(player);
		
		if(this.selection != null)
			this.selection.free();
			
		this.selection = new AQSelection(selectionName, provider.root, this.points[0], this.points[1], player);
		this.selection.add();
		
		return true;
	}

	@SideOnly(Side.CLIENT)
	public boolean createSelectionFor(AQSchematics schx)
	{
		EntityPlayer player = AQThisPlayerWrapper.getWrapper().player;

		AQRegionProvider provider = AQRegionManager.getProviderForPlayer(player);
		
		if(this.points[2] == null)
			this.points[2] = this.points[0] != null ? this.points[0] : AQPlayerWrapper.getPlayerServerPos(player);
		
		if(this.selection != null)
			this.selection.free();
			
		this.selection = new AQSelection(selectionName, provider.root, schx.getBox(this.points[2], BoxAlignment.START), player);
		this.selection.add();
		
		return true;
	}
	
	public void checkWorld(World world)
	{
		if(this.selection != null && this.selection.provider.world != world)
		{
			freeSelection();
			
			this.buffer = null;
			this.points[0] = null;
			this.points[1] = null;
			this.points[2] = null;
		}
	}
	
	public void updatePos(AQPoint3Dd playerPos)
	{
		AQPoint3D pos;
		
		if(this.points[0] != null)
			pos = new AQPoint3D(this.points[0]);
		else
			pos = new AQPoint3D(playerPos);
		
		setPoint(2, pos);
	}
}
