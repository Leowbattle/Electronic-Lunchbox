package electroniclunchbox.network;

import com.ibm.icu.impl.Assert;

import electroniclunchbox.items.ModItems;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SetNBTMessageHandler implements IMessageHandler<SetNBTMessage, IMessage> {
	@Override
	public IMessage onMessage(SetNBTMessage message, MessageContext ctx) {
		EntityPlayerMP player = ctx.getServerHandler().player;
		ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);
		
		Assert.assrt(stack.getItem() == ModItems.lunchbox);
		
		NBTTagCompound nbt = stack.getTagCompound();
		nbt.setInteger("Hunger", message.hunger);
		nbt.setInteger("Saturation", message.saturation);
		
		return null;
	}

}
