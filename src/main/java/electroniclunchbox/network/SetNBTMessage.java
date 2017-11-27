package electroniclunchbox.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SetNBTMessage implements IMessage {
	public int hunger;
	public int saturation;
	
	public SetNBTMessage() {}
	
	public SetNBTMessage(int hunger, int saturation) {
		this.hunger = hunger;
		this.saturation = saturation;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		hunger = buf.readInt();
		saturation = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(hunger);
		buf.writeInt(saturation);
	}

}
