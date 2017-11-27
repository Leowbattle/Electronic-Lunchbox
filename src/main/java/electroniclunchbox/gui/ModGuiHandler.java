package electroniclunchbox.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class ModGuiHandler implements IGuiHandler {
	private static int maxID = 0;
	
	public static final int electronicLunchboxGuiID = maxID++;
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID > maxID) {
			return null;
		}
		
		switch (ID) {
		case 0:
			return new ContainerElectronicLunchbox();
			
		default:
			return null;
		}
		
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID > maxID) {
			return null;
		}
	
		switch (ID) {
		case 0:			
			return new GuiElectronicLunchbox(new ContainerElectronicLunchbox(), player);
		
		default:
			return null;
		}
	}
	
	
}
