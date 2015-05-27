package aqua.rpgmod.client.gui.editor;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import aqua.rpgmod.character.AQCharacterManager;
import aqua.rpgmod.character.AQRace;
import aqua.rpgmod.client.gui.AQGuiMainMenu;
import aqua.rpgmod.client.gui.dialog.AQOkDialog;
import aqua.rpgmod.client.gui.dialog.AQRaceDialog;
import aqua.rpgmod.client.gui.dialog.AQTextDialog;
import aqua.rpgmod.service.AQStringValidator;

public class AQGuiCharacter extends AQGuiMainMenu
{
	protected int xSize = 248;
	protected int ySize = 166;
	
	private float limbSwingAmount;
	private float prevLimbSwingAmount;
	private float limbSwing;
	
	protected int angle = 45;
	
	GuiButton changeRace;
	GuiButton changeName;
	GuiButton back;
	
	public AQGuiCharacter()
	{	
		
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		this.charManager.useGui(this);
		
		this.buttonList.clear();
		
		int id = 0;
		int y = 20;
		
		this.changeName = new GuiButton(++id, 20, y += 24, 100, 20, I18n.format("menu.changename"));
		this.changeRace = new GuiButton(++id, 20, y += 24, 100, 20, I18n.format("menu.changerace"));
		this.back = new GuiButton(++id, 20, y += 36, 100, 20, I18n.format("menu.back"));
		
		this.buttonList.add(this.changeName);
		this.buttonList.add(this.changeRace);
		this.buttonList.add(this.back);
	}
	
	@Override
	protected void actionPerformed(GuiButton button)
	{
		if(button.id == this.changeName.id)
		{
			AQTextDialog dialog = new AQTextDialog(this, "menu.changename")
			{
				@Override
				public void onClose()
				{
					if(this.result == Result.OK)
					{
						String name = this.getText().trim();
						String[] validation = AQStringValidator.validateUsername(name);
						
						if(validation != null)
						{
							this.setText(AQCharacterManager.instance.name);
							new AQOkDialog(this, I18n.format(validation[0], AQStringValidator.getVargs(validation))).show();
							return;
						}
						
						String result = AQCharacterManager.instance.changeName(this.getText());
						
						if(result.equals("ok"))
						{
							new AQOkDialog(AQGuiCharacter.this, "menu.namechanged").show();
							return;
						}
						
						if(result.equals("wrongname"))
						{
							new AQOkDialog(this, "menu.wrongname").show();
							return;
						}
						
						if(result.equals("samename"))
						{
							new AQOkDialog(this, "menu.samename").show();
							return;
						}
						
						if(result.equals("error"))
						{
							new AQOkDialog(AQGuiCharacter.this, "menu.servererror").show();
							return;
						}
					}
				}
			};
			
			dialog.setText(AQCharacterManager.instance.name);
			dialog.show();
		}
		else if(button.id == this.changeRace.id)
		{
			AQRaceDialog dialog = new AQRaceDialog(this)
			{
				@Override
				public boolean onUse(int index)
				{
					if(!super.onUse(index))
						return false;
					
					AQRace race = this.selectedElement();
					
					if(race == null)
						return false;
					
					switch(AQCharacterManager.instance.changeRace(race))
					{
						case TRUE:
							new AQOkDialog(AQGuiCharacter.this, "menu.racechanged").show();
							return true;
						case FALSE:
							new AQOkDialog(AQGuiCharacter.this, "menu.racenotchanged").show();
							return false;
						default:
							new AQOkDialog(AQGuiCharacter.this, "menu.servererror").show();
							return false;
					}
				}
			};
			
			dialog.select(AQCharacterManager.instance.race);
			dialog.show();
		}
		else if(button.id == this.back.id)
		{
			this.mc.displayGuiScreen(new AQGuiMainMenu());
		}
	}
	
	public void render(float f1, float f2, float f3, float f4, float f5, float f6, float f)
	{
		this.charManager.currentModel.setRotationAngles(this.charManager.rootModel, f1, f2, f3, f4, f5, f6, null, 0);
		this.charManager.currentModel.render(null, this.charManager.rootModel, f, this.charManager.textures, 0);
	}
	
	public void setViewport(int left, int top, int width, int height)
	{
		final int l = left * this.mc.displayWidth / this.width;
		final int t = top * this.mc.displayHeight / this.height;
		final int w = width * this.mc.displayWidth / this.width;
		final int h = height * this.mc.displayHeight / this.height;
		
		GL11.glViewport(l, t, w, h);
	}
	
	@Override
	public void handleMouseInput()
	{
		super.handleMouseInput();
		
		int data = Mouse.getDWheel();
		
		if(data != 0)
		{
			this.angle += data / 120;
			
			for(; this.angle > 180; this.angle -= 360)
			{}
			for(; this.angle < -180; this.angle += 360)
			{}
		}
	}
	
	float headAngleX = -360.0f;
	float headAngleY = -360.0f;
	
	float delta = 6.0f;
	
	public void rotateHeadX(float value)
	{
		if(this.headAngleX == -360.0f)
		{
			this.headAngleX = value;
			return;
		}
		
		if(value > this.headAngleX + this.delta)
		{
			this.headAngleX += this.delta;
		}
		else if(value < this.headAngleX - this.delta)
		{
			this.headAngleX -= this.delta;
		}
		else
		{
			this.headAngleX = value;
		}
	}
	
	public void rotateHeadY(float value)
	{
		if(this.headAngleY == -360.0f)
		{
			this.headAngleY = value;
			return;
		}
		
		if(value > this.headAngleY + this.delta)
		{
			this.headAngleY += this.delta;
		}
		else if(value < this.headAngleY - this.delta)
		{
			this.headAngleY -= this.delta;
		}
		else
		{
			this.headAngleY = value;
		}
	}
	
	@Override
	public void drawScreen(int x, int y, float f)
	{
		super.drawScreen(x, y, f);
		
		if(this.charManager.currentModel == null)
			return;
		
		int cx = this.width / 2;
		int cy = this.height * 3 / 2;
		
		int left = this.width - cx;
		int top = -cy / 8;
		
		setViewport(left, top, cx, cy);
		
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(90, (float) cx / (float) cy, 20.0f, 100.0f);
		
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LESS);
		
		GL11.glPushMatrix();
		
		GL11.glTranslatef(0.0f, this.charManager.race.height() * 4, -45.0f);
		GL11.glRotatef(180.0f, 1.0f, 0.0f, 0.0f);
		GL11.glRotatef(this.angle, 0.0f, 1.0f, 0.0f);
		
		float f6 = this.prevLimbSwingAmount + (this.limbSwingAmount - this.prevLimbSwingAmount) * f;
		float f7 = this.limbSwing - this.limbSwingAmount * (1.0F - f);
		
		this.prevLimbSwingAmount = this.limbSwingAmount;
		this.limbSwingAmount += (0.2f - this.limbSwingAmount) * 0.4f;
		this.limbSwing += this.limbSwingAmount;
		
		double tx = x - (left + cx / 2);
		double ty = y - (top + cy / 2 - this.charManager.currentModel.race.height() * cy / 7);
		
		tx /= Math.sqrt(tx * tx + 40000);
		ty /= Math.sqrt(ty * ty + 40000);
		
		rotateHeadX(Math.min(Math.max((float) (Math.acos(tx) / Math.PI * 180.0f) - 90.0f - this.angle, -80.0f), 80.0f));
		rotateHeadY((float) (Math.asin(ty) / Math.PI * 180.0f));
		
		render(f7, f6, 0.5f, this.headAngleX, this.headAngleY, 0.0f, 1.0f);
		GL11.glPopMatrix();
		
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
	}
	
	@Override
	public void onGuiClosed()
	{
		this.charManager.closeGui(this);
	}
}
