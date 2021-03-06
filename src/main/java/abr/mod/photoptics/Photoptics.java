package abr.mod.photoptics;

import java.io.IOException;

import org.apache.logging.log4j.Logger;

import abr.mod.photoptics.command.CommandObserve;
import abr.mod.photoptics.item.PhotopticsItems;
import abr.mod.photoptics.network.PhotopticsNetworkHandler;
import abr.mod.photoptics.processing.PossibleObservations;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import stellarapi.api.SAPIReferences;
import stellarapi.api.lib.config.ConfigManager;

// TODO Total Regeneration
@Mod(modid=Photoptics.modid, version=Photoptics.version,
acceptedMinecraftVersions="[1.12.0, 1.13.0)",
dependencies="required-after:stellarapi@[1.12.2-0.4.2.1, 1.12.2-0.4.3.0)", guiFactory="abr.mod.photoptics.config.POConfigGuiFactory")
public class Photoptics {
	public static final String modid = "photoptics";
	public static final String resourceid = "photoptics";
	public static final String version = "@VERSION@";

	// The instance of Photoptics
	@Mod.Instance(Photoptics.modid)
	public static Photoptics instance;

	@SidedProxy(clientSide="abr.mod.photoptics.ClientProxy", serverSide="abr.mod.photoptics.CommonProxy")
	public static CommonProxy proxy;

	public static Logger logger;

	private PhotopticsNetworkHandler networkHandler = new PhotopticsNetworkHandler();
	private ConfigManager manager;

	public PhotopticsNetworkHandler getNetworkHandler() {
		return this.networkHandler;
	}

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) { 
		logger = event.getModLog();

		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		this.manager = new ConfigManager(config);

		SAPIReferences.getEventBus().register(new StellarAPIEventHandler());

		MinecraftForge.EVENT_BUS.register(new TickEventHandler());
		MinecraftForge.EVENT_BUS.register(new PhotopticsForgeEventHandler());
		MinecraftForge.EVENT_BUS.register(new PhotopticsFMLEventHandler());
		MinecraftForge.EVENT_BUS.register(PhotopticsItems.INSTANCE);
		networkHandler.register();

		manager.register(PossibleObservations.categoryName, PossibleObservations.instance());
	}
	

	@Mod.EventHandler
	public void load(FMLInitializationEvent event) throws IOException {
		manager.syncFromFile();
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
	}

	@Mod.EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandObserve());
	}

	public ConfigManager getConfigManager() {
		return this.manager;
	}
}
