package electroniclunchbox.proxies;

import electroniclunchbox.ElectronicLunchbox;
import electroniclunchbox.gui.ModGuiHandler;
import electroniclunchbox.items.ModItems;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy implements CommonProxy {
	@Override
	public void init() {
		NetworkRegistry.INSTANCE.registerGuiHandler(ElectronicLunchbox.instance, new ModGuiHandler());
	}
	
	@SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        ModItems.registerRenderers();
    }
}
