package aqua.rpgmod.client.gui.bars;

import net.minecraft.client.Minecraft;

abstract public class AQBarController
{
	public boolean visible = true;
	public boolean alwaysVisible = false;
	public float percent = -1.0f;
	public AQBarController metaController = null;
	
	abstract public float calculate(Minecraft mc);
	
	public void checkVisibility(float value)
	{
		this.visible = value > 0.0f;
	}
	
	public float getPercent(AQBarRenderer renderer, float animSpeed)
	{
		float value = animate(calculate(renderer.mc), animSpeed);
		
		if(!this.alwaysVisible)
			checkVisibility(value);
		
		return value;
	}
	
	protected float animate(float level, float animSpeed)
	{
		if(this.percent == -1.0f)
			return level;
		
		if(this.percent < level - animSpeed)
			return this.percent + animSpeed;
		
		if(this.percent > level + animSpeed)
			return this.percent - animSpeed;
		
		return level;
	}
}
