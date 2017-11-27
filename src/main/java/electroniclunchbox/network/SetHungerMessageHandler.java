package electroniclunchbox.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.FoodStats;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SetHungerMessageHandler implements IMessageHandler<SetHungerMessage, IMessage> {
	@Override
	public IMessage onMessage(SetHungerMessage message, MessageContext ctx) {
		EntityPlayerMP player = ctx.getServerHandler().player;
		FoodStats food = player.getFoodStats();
		
		food.addStats(message.hunger, message.saturation);
		
		return null;
	}

}
