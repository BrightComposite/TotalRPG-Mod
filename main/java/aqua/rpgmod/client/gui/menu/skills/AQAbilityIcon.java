package aqua.rpgmod.client.gui.menu.skills;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.util.ResourceLocation;
import aqua.rpgmod.AQModInfo;
import aqua.rpgmod.client.gui.AQImageButton;
import aqua.rpgmod.player.rpg.AQAbility;

public class AQAbilityIcon extends AQImageButton
{
	public final AQAbility ability;
	
	public static final ResourceLocation dark_back = new ResourceLocation(AQModInfo.MODID + ":" + "textures/gui/misc/dark_back.png");
	public static final ResourceLocation gray_back = new ResourceLocation(AQModInfo.MODID + ":" + "textures/gui/misc/gray_back.png");
	public static final ResourceLocation lite_back = new ResourceLocation(AQModInfo.MODID + ":" + "textures/gui/misc/lite_back.png");
	
	public AQAbilityIcon(AQAbilityGroupPage owner, int id, AQAbility ability)
	{
		super(owner, id, 0, 0, 20, 20);
		
		this.image = new ResourceLocation(AQModInfo.MODID + ":" + "textures/skills/" + owner.group.name + "/" + ability.name + ".png");
		this.imageWidth = 16;
		this.imageHeight = 16;
		
		this.ability = ability;
		
		this.xPosition = this.owner.getLeft() + this.ability.displayX;
		this.yPosition = this.owner.getTop() + this.ability.displayY;
		
		String desc = this.ability.desc();
		
		if(this.ability.active() && this.ability.experienceTable.length > 1)
			desc += AQAbility.levelString(this.ability.getLevel());
		
		setHint(this.ability.addDescription(this.ability.update(), desc));
	}
	
	@Override
	protected void drawSurface(Minecraft mc, int mouseX, int mouseY)
	{
		if(this.ability.available)
			this.drawImage(mc, lite_back, -6, -6, 32, 32);
		else
			this.drawImage(mc, this.ability.active() ? gray_back : dark_back, -6, -6, 32, 32);
	}
	
	@Override
	protected void drawForeground(Minecraft mc)
	{
		if(!this.ability.available)
			GL11.glColor3f(0.5f, 0.5f, 0.5f);
		
		super.drawForeground(mc);
	}
	
	@Override
	public void func_146113_a(SoundHandler p_146113_1_)
	{	
		
	}
}
