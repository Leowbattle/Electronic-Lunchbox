package electroniclunchbox;

import electroniclunchbox.network.SetHungerMessage;
import electroniclunchbox.network.SetHungerMessageHandler;
import electroniclunchbox.network.SetNBTMessage;
import electroniclunchbox.network.SetNBTMessageHandler;
import electroniclunchbox.proxies.ClientProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = ElectronicLunchbox.MODID, version = ElectronicLunchbox.VERSION, dependencies = "required-after:redstoneflux;required-after:bookshelf;")
public class ElectronicLunchbox {
    public static final String MODID = "electroniclunchbox";
    public static final String name = "Electronic Lunchbox";
    public static final String VERSION = "1.0";
    
    @SidedProxy(serverSide = "electroniclunchbox.proxies.ServerProxy", clientSide = "electroniclunchbox.proxies.ClientProxy")
	public static ClientProxy proxy;
	
	@Mod.Instance(MODID)
	public static ElectronicLunchbox instance;
    
	public SimpleNetworkWrapper network;
	
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {    	
    	network = NetworkRegistry.INSTANCE.newSimpleChannel("electroniclunchbox");
    	
    	network.registerMessage(SetNBTMessageHandler.class, SetNBTMessage.class, 0, Side.SERVER);
    	network.registerMessage(SetHungerMessageHandler.class, SetHungerMessage.class, 1, Side.SERVER);
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event) { 	
    	proxy.init();
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    	
    }
}
