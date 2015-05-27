package aqua.rpgmod.service.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.input.Keyboard;

import api.player.client.ClientPlayerAPI;
import api.player.model.ModelPlayerAPI;
import api.player.render.RenderPlayerAPI;
import api.player.server.ServerPlayerAPI;
import aqua.rpgmod.AQModInfo;
import aqua.rpgmod.AquaMod;
import aqua.rpgmod.block.AQBlocks;
import aqua.rpgmod.client.gui.AQGuiContainerCreative;
import aqua.rpgmod.client.gui.menu.AQInventoryMenu;
import aqua.rpgmod.client.gui.menu.AQModMenu;
import aqua.rpgmod.client.gui.menu.AQPlayerMenu;
import aqua.rpgmod.client.gui.menu.AQSkillsMenu;
import aqua.rpgmod.client.model.AQModelPlayerBase;
import aqua.rpgmod.client.render.AQGuiOverlayRenderer;
import aqua.rpgmod.client.render.AQRegionRenderer;
import aqua.rpgmod.client.render.AQRenderPlayerBase;
import aqua.rpgmod.client.render.entity.AQRenderSpellCharge;
import aqua.rpgmod.client.render.tile.AQBonfireRenderer;
import aqua.rpgmod.client.render.tile.AQTileEntityItemRenderer;
import aqua.rpgmod.entity.AQEntityRenderer;
import aqua.rpgmod.entity.AQEntitySpellCharge;
import aqua.rpgmod.fx.AQRandomFxSystem;
import aqua.rpgmod.fx.AQRingFxSystem;
import aqua.rpgmod.fx.AQSphereFxSystem;
import aqua.rpgmod.fx.AQSpreadingFxSystem;
import aqua.rpgmod.item.AQItems;
import aqua.rpgmod.player.AQClientPlayerBase;
import aqua.rpgmod.player.AQPlayerControllerMP;
import aqua.rpgmod.player.AQPlayerWrapper;
import aqua.rpgmod.player.AQServerPlayerBase;
import aqua.rpgmod.player.AQThisPlayerWrapper;
import aqua.rpgmod.service.AQKeyBinding;
import aqua.rpgmod.service.handlers.AQClientEventHandler;
import aqua.rpgmod.service.handlers.AQFMLClientHandler;
import aqua.rpgmod.service.network.AQClientPlayerMessenger;
import aqua.rpgmod.service.network.AQClientRegionMessenger;
import aqua.rpgmod.service.network.AQClientServiceMessenger;
import aqua.rpgmod.service.network.AQMessage;
import aqua.rpgmod.service.network.AQServerPlayerMessenger;
import aqua.rpgmod.service.network.AQServerRegionMessenger;
import aqua.rpgmod.service.network.AQServerServiceMessenger;
import aqua.rpgmod.tileentity.AQBonfireTileEntity;
import aqua.rpgmod.world.region.actions.AQRegionActionManager;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class AQClientProxy extends AQCommonProxy
{
	Minecraft mc;
	
	public static AQClientProxy instance = null;
	public static AQKeyBinding keyBindings[];
	
	public final static AQKeyBinding keyBindInventoryCreative = new AQKeyBinding("key.creative", Keyboard.KEY_U, "key.categories.inventory")
	{
		@Override
		public void onPress()
		{
			if(AQClientProxy.instance.mc.thePlayer.capabilities.isCreativeMode)
				AQModMenu.instance.selectPage(2);
		}
	};
	
	public final static AQKeyBinding keyBindInteract = new AQKeyBinding("key.interact", Keyboard.KEY_E, "key.categories.movement")
	{
		@Override
		public void onPress()
		{
			AQPlayerControllerMP ctrl = (AQPlayerControllerMP) AQClientProxy.instance.mc.playerController;
			ctrl.interact();
		}
	};
	
	// public final static AQKeyBinding keyBindSneakToggle = new
	// AQKeyBinding("key.sneaktoggle", Keyboard.KEY_C,
	// "key.categories.movement");
	
	@Override
	public boolean isDedicated()
	{
		return false;
	}
	
	@Override
	public void preInit()
	{
		instance = this;
		
		AquaMod.playerNetwork.registerMessage(AQClientPlayerMessenger.class, AQMessage.class, 0, Side.CLIENT);
		AquaMod.playerNetwork.registerMessage(AQServerPlayerMessenger.class, AQMessage.class, 0, Side.SERVER);
		AquaMod.serviceNetwork.registerMessage(AQClientServiceMessenger.class, AQMessage.class, 1, Side.CLIENT);
		AquaMod.serviceNetwork.registerMessage(AQServerServiceMessenger.class, AQMessage.class, 1, Side.SERVER);
		AquaMod.regionNetwork.registerMessage(AQClientRegionMessenger.class, AQMessage.class, 2, Side.CLIENT);
		AquaMod.regionNetwork.registerMessage(AQServerRegionMessenger.class, AQMessage.class, 2, Side.SERVER);
		
		RenderPlayerAPI.register(AQModInfo.MODID, AQRenderPlayerBase.class);
		ModelPlayerAPI.register(AQModInfo.MODID, AQModelPlayerBase.class);
		ClientPlayerAPI.register(AQModInfo.MODID, AQClientPlayerBase.class);
		ServerPlayerAPI.register(AQModInfo.MODID, AQServerPlayerBase.class);
		
		MinecraftForge.EVENT_BUS.register(new AQClientEventHandler());
		FMLCommonHandler.instance().bus().register(new AQFMLClientHandler());
		
		this.mc = Minecraft.getMinecraft();
		
		AQGuiOverlayRenderer.instance.init(this.mc);
		
		keyBindings = new AQKeyBinding[]
		{
			keyBindInventoryCreative,
			keyBindInteract,
			// keyBindSneakToggle,
			new AQKeyBinding("key.fly", Keyboard.KEY_B, "key.categories.gameplay")
			{
				@Override
				public void onPress()
				{
					AQThisPlayerWrapper wrapper = AQThisPlayerWrapper.getWrapper();
					
					if(wrapper.player.capabilities.isCreativeMode)
					{
						wrapper.isFlying = !wrapper.isFlying;
					}
				}
			}, new AQKeyBinding("key.scheme.left", Keyboard.KEY_LEFT, "key.categories.scheme")
			{
				@Override
				public void onPress()
				{
					super.onPress();
				}
			}, new AQKeyBinding("key.scheme.right", Keyboard.KEY_RIGHT, "key.categories.scheme"),
			new AQKeyBinding("key.scheme.forward", Keyboard.KEY_UP, "key.categories.scheme"),
			new AQKeyBinding("key.scheme.back", Keyboard.KEY_DOWN, "key.categories.scheme"),
			new AQKeyBinding("key.scheme.down", Keyboard.KEY_SUBTRACT, "key.categories.scheme"),
			new AQKeyBinding("key.scheme.up", Keyboard.KEY_ADD, "key.categories.scheme"),
			new AQKeyBinding("key.scheme.disable", Keyboard.KEY_NUMPAD0, "key.categories.scheme")
			{
				@Override
				public void onPress()
				{
					AQRegionRenderer.instance.shouldRender = !AQRegionRenderer.instance.shouldRender;
				}
			}, new AQKeyBinding("key.scheme.rotate.left", Keyboard.KEY_NUMPAD4, "key.categories.scheme"),
			new AQKeyBinding("key.scheme.rotate.right", Keyboard.KEY_NUMPAD6, "key.categories.scheme"),
			new AQKeyBinding("key.scheme.rotate.forward", Keyboard.KEY_NUMPAD8, "key.categories.scheme"),
			new AQKeyBinding("key.scheme.rotate.back", Keyboard.KEY_NUMPAD2, "key.categories.scheme"),
			new AQKeyBinding("key.scheme.select", Keyboard.KEY_NUMPAD5, "key.categories.scheme")
			{
				@Override
				public void onPress()
				{
					if(AQRegionActionManager.instance.getSelection() == null)
						AQRegionActionManager.instance.execute("select", AQClientProxy.this.mc.thePlayer);
					else
						AQRegionActionManager.instance.execute("deselect", AQClientProxy.this.mc.thePlayer);
				}
			},
		};
		
		AQModMenu.instance.addPage(new AQPlayerMenu());
		AQModMenu.instance.addPage(new AQInventoryMenu());
		AQModMenu.instance.addPage(new AQGuiContainerCreative());
		AQModMenu.instance.addPage(new AQSkillsMenu());
	}
	
	@Override
	public void postInit()
	{
		this.mc.entityRenderer = new AQEntityRenderer(this.mc);
		
		AQItems.registerRenderers();
		AQTileEntityItemRenderer.register(AQBonfireTileEntity.class, AQBlocks.bonfire, new AQBonfireRenderer());
		RenderingRegistry.registerEntityRenderingHandler(AQEntitySpellCharge.class, new AQRenderSpellCharge());
		
		AQRingFxSystem.create();
		AQSphereFxSystem.create();
		AQRandomFxSystem.create();
		AQSpreadingFxSystem.create();
	}
	
	@Override
	public AQPlayerWrapper createWrapper(EntityPlayer player)
	{
		return AQPlayerWrapper.cantBeThis(player) ? super.createWrapper(player) : new AQThisPlayerWrapper((EntityClientPlayerMP) player);
	}
	
	@Override
	public AQPlayerWrapper getWrapper(EntityPlayer player)
	{
		return AQPlayerWrapper.cantBeThis(player) ? super.getWrapper(player) : AQThisPlayerWrapper.getWrapper();
	}
	
	@Override
	public World getWorld(int dimension, boolean server)
	{
		if(server)
		{
			if(MinecraftServer.getServer() != null)
				return super.getWorld(dimension, server);
			
			return null;
		}
		
		return Minecraft.getMinecraft().theWorld;
	}
	
	@Override
	public World getWorld(String name, boolean server)
	{
		if(server)
		{
			if(MinecraftServer.getServer() != null)
				return super.getWorld(name, server);
			
			return null;
		}
		
		return Minecraft.getMinecraft().theWorld;
	}
	
	@Override
	public EntityPlayer getPlayer(MessageContext ctx)
	{
		if(ctx.side == Side.SERVER)
			return super.getPlayer(ctx);
		
		return Minecraft.getMinecraft().thePlayer;
	}
}
