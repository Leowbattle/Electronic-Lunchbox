package electroniclunchbox.items;

import electroniclunchbox.ElectronicLunchbox;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber
@ObjectHolder(ElectronicLunchbox.MODID)
public class ModItems {
	public static Item lunchbox = new ItemElectronicLunchbox(0, 0, false).setRegistryName("electroniclunchbox").setUnlocalizedName("electroniclunchbox");
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();
		
		registry.register(lunchbox);
	}
	
	public static void registerRenderers() {
		registerRenderer(lunchbox);
	}
	
	private static void registerRenderer(Item item) {
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
}
