package electroniclunchbox.items;

import java.util.List;

import cofh.redstoneflux.api.IEnergyContainerItem;
import electroniclunchbox.ElectronicLunchbox;
import electroniclunchbox.ModConfig;
import electroniclunchbox.gui.ModGuiHandler;
import electroniclunchbox.network.SetHungerMessage;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

public class ItemElectronicLunchbox extends ItemFood implements IEnergyContainerItem {
	public static final int capacity = 1000000;
	public static final int maxReceive = 10000;
	
	public ItemElectronicLunchbox(int amount, float saturation, boolean isWolfFood) {
		super(amount, saturation, isWolfFood);
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {	
		NBTTagCompound nbt = stack.getTagCompound();
		int hunger = nbt.getInteger("Hunger");
		int saturation = nbt.getInteger("Saturation");
		int cost = ModConfig.baseCost;
		cost += (hunger * saturation) * ModConfig.costPerHalfShank;
		if (cost > nbt.getInteger("Energy")) {
			return stack;
		}
		
		if (entityLiving instanceof EntityPlayer) {
            EntityPlayer entityplayer = (EntityPlayer)entityLiving;
            entityplayer.getFoodStats().addStats(this, stack);
            worldIn.playSound((EntityPlayer)null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
            this.onFoodEaten(stack, worldIn, entityplayer);
            entityplayer.addStat(StatList.getObjectUseStats(this));

            if (entityplayer instanceof EntityPlayerMP) {
                CriteriaTriggers.CONSUME_ITEM.trigger((EntityPlayerMP)entityplayer, stack);
            }
        }

		nbt.setInteger("Energy", nbt.getInteger("Energy") - cost);
		
		ElectronicLunchbox.instance.network.sendToAllAround(new SetHungerMessage(hunger, saturation), new TargetPoint(entityLiving.dimension, entityLiving.posY, entityLiving.posZ, entityLiving.posZ, 1));
		
        return stack;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return super.getMaxItemUseDuration(stack);
	}

	@Override
	public int getHealAmount(ItemStack stack) {
		if (!stack.hasTagCompound()) {
			return 0;
		}
		
		NBTTagCompound nbt = stack.getTagCompound();
		
		int cost = ModConfig.baseCost + (nbt.getInteger("Hunger") * nbt.getInteger("Saturation")) * ModConfig.costPerHalfShank;
		
		if (nbt.getInteger("Energy") < cost) {
			return 0;
		}
		
		return nbt.getInteger("Hunger");
	}

	@Override
	public float getSaturationModifier(ItemStack stack) {
		if (!stack.hasTagCompound()) {
			return 0;
		}
		NBTTagCompound nbt = stack.getTagCompound();
		
		int cost = ModConfig.baseCost + (nbt.getInteger("Hunger") * nbt.getInteger("Saturation")) * ModConfig.costPerHalfShank;
		
		if (nbt.getInteger("Energy") < cost) {
			return 0;
		}
		
		return nbt.getInteger("Saturation") / 10;
	}

	@Override
	public boolean isWolfsFavoriteMeat() {
		return super.isWolfsFavoriteMeat();
	}

	@Override
	public ItemFood setPotionEffect(PotionEffect effect, float probability) {
		return super.setPotionEffect(effect, probability);
	}

	@Override
	public ItemFood setAlwaysEdible() {
		return super.setAlwaysEdible();
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		
		if (!stack.hasTagCompound()) {
			return;
		}
		
		NBTTagCompound nbt = stack.getTagCompound();
		
		tooltip.add("Shift-Right Click Me To Edit My Settings");
		tooltip.add("Hunger: " + nbt.getInteger("Hunger"));
		tooltip.add("Saturation: " + nbt.getInteger("Saturation"));
		tooltip.add("RF Cost: " + (ModConfig.baseCost + (nbt.getInteger("Hunger") * nbt.getInteger("Saturation")) * ModConfig.costPerHalfShank));
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		if (!stack.hasTagCompound()) {
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setInteger("Energy", 0);
			nbt.setInteger("Hunger", 0);
			nbt.setInteger("Saturation", 0);
			stack.setTagCompound(nbt);
		}
		
		NBTTagCompound nbt = stack.getTagCompound();
		
		return (double)nbt.getInteger("Energy") / (double)capacity;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if (playerIn.isSneaking()) {
			playerIn.openGui(ElectronicLunchbox.instance, ModGuiHandler.electronicLunchboxGuiID, worldIn, (int)playerIn.posX, (int)playerIn.posY, (int)playerIn.posZ);
			return new ActionResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
		}
		
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		//I will turn this back on when I figure out how to make it not backwards
		return false;
	}

	@Override
	public int extractEnergy(ItemStack arg0, int arg1, boolean arg2) {
		return arg1;
	}

	@Override
	public int getEnergyStored(ItemStack arg0) {
		return 0;
	}

	@Override
	public int getMaxEnergyStored(ItemStack arg0) {
		return 1000000;
	}

	@Override
	public int receiveEnergy(ItemStack arg0, int arg1, boolean arg2) {
		if (!arg0.hasTagCompound()) {
			arg0.setTagCompound(new NBTTagCompound());
		}
		int energy = arg0.getTagCompound().getInteger("Energy");
		int energyReceived = Math.min(capacity - energy, Math.min(maxReceive, maxReceive));

		if (!arg2) {
			energy += energyReceived;
			arg0.getTagCompound().setInteger("Energy", energy);
		}
		return energyReceived;
	}	
}
