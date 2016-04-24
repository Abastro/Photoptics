package abr.mod.photoptics;

import abr.mod.photoptics.item.PhotopticsItems;
import abr.mod.photoptics.render.RenderEventHandler;
import abr.mod.photoptics.render.item.ItemBinocularsRenderer;
import abr.mod.photoptics.render.item.ItemHandheldTelescopeRenderer;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {
	
	public void registerRenderers() {
		MinecraftForge.EVENT_BUS.register(new RenderEventHandler());
		FMLCommonHandler.instance().bus().register(new PhotopticsKeybindings());
		this.registerItemRenderers();
	}
	
	public void registerItemRenderers() {
		MinecraftForgeClient.registerItemRenderer(PhotopticsItems.basicBinoculars,
				new ItemBinocularsRenderer());
		MinecraftForgeClient.registerItemRenderer(PhotopticsItems.basicHandheldTelescope,
				new ItemHandheldTelescopeRenderer());
	}
	
	public void forcePerspective() {
		Minecraft.getMinecraft().gameSettings.hideGUI = false;
		Minecraft.getMinecraft().gameSettings.thirdPersonView = 0;
	}

}
