package electroniclunchbox;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = ElectronicLunchbox.MODID)
public class ModConfig {	

	@Config.Comment("")
	public static int baseCost = 2000;
	
	@Config.Comment("")
	public static int costPerHalfShank = 500;
	
	@Mod.EventBusSubscriber(modid = ElectronicLunchbox.MODID)
	private static class EventHandler {
		@SubscribeEvent
		public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID().equals(ElectronicLunchbox.MODID)) {
				ConfigManager.sync(ElectronicLunchbox.MODID, Config.Type.INSTANCE);
			}
		}
	}
}
