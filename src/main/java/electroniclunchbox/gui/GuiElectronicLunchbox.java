package electroniclunchbox.gui;

import java.io.IOException;

import com.ibm.icu.impl.Assert;

import electroniclunchbox.ElectronicLunchbox;
import electroniclunchbox.ModConfig;
import electroniclunchbox.items.ModItems;
import electroniclunchbox.network.SetNBTMessage;
import net.darkhax.bookshelf.client.gui.GuiSlider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

public class GuiElectronicLunchbox extends GuiContainer {	
	//These represent how much hunger and saturation will be applied when you eat from the lunchbox
	public GuiSlider hungerSlider;
	public GuiSlider saturationSlider;
	public GuiButton saveSettings;
	
	public EntityPlayer player;
	
	public GuiElectronicLunchbox(Container inventorySlotsIn, EntityPlayer player) {
		super(inventorySlotsIn);
		
		this.player = player;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		
		hungerSlider = new GuiSlider(0, "Hunger", 0, i + xSize / 8, j + ySize / 8, true, 20);
		saturationSlider = new GuiSlider(1, "Saturation", 0, i + (int)(xSize * 0.75), j + ySize / 8, true, 20);
		saturationSlider.x -= saturationSlider.width / 2;
		
		saveSettings = new GuiButton(2, i + xSize / 2, j + (int)(ySize * 0.75), 100, 20, "Save Setings");
		saveSettings.x -= saveSettings.width / 2;
		
		this.buttonList.add(hungerSlider);
		this.buttonList.add(saturationSlider);
		this.buttonList.add(saveSettings);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		this.drawDefaultBackground();
		GlStateManager.color(1, 1, 1, 1);
		GlStateManager.disableLighting();
		
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		
		//This texture has all of the overlays in it too
		Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("electroniclunchbox:textures/gui/electroniclunchbox.png"));
		
		//Draw GUI background
		this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
		
		//Here I convert from a percentage to a value from 1 to 20
		int hunger = (int)(hungerSlider.getSliderValue() * 20);
		int saturation = (int)(saturationSlider.getSliderValue() * 20);
		
		//Base cost of eating from the lunchbox is 2000 RF by default
		//The cost of a single hunger or saturation point is 20 RF
		//The hunger and saturation are multiplied together and added to the base cost
		int cost = ModConfig.baseCost;
		cost += (hunger * saturation) * ModConfig.costPerHalfShank;
		
		this.drawCenteredString(Minecraft.getMinecraft().fontRenderer, String.format("Cost = %s", cost), this.width / 2, this.height / 2, 0xFFFFFF);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 2) {
			//If "Save Changes" was pressed
			ItemStack stack = player.getHeldItemMainhand();
			
			//If the item is not the electronic lunchbox this Gui shouldn't be open anyway
			//Therefore it is responsible to crash the game before writing NBT to items I shoudn't be
			//If this does happen it is because of someone mucking around with Gui IDs they shoudn't be
			Assert.assrt(stack.getItem() == ModItems.lunchbox);
			
			ElectronicLunchbox.instance.network.sendToAllAround(new SetNBTMessage((int)(hungerSlider.getSliderValue() * 20), (int)(saturationSlider.getSliderValue() * 20)), new TargetPoint(this.player.dimension, this.player.posX, this.player.posY, this.player.posZ, 1));
			
			NBTTagCompound nbt = stack.getTagCompound();
		
			nbt.setInteger("Hunger", (int)(hungerSlider.getSliderValue() * 20));
			nbt.setInteger("Saturation", (int)(saturationSlider.getSliderValue() * 20));
		}
	}

}
