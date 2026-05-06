package com.ibarnstormer.gbd.items;

import com.ibarnstormer.gbd.utils.Utils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class BeamReactorSwitch extends Item {


    public BeamReactorSwitch() {
        super(new Properties().stacksTo(1).rarity(Rarity.UNCOMMON).fireResistant());
    }


    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.getInventory().getArmor(2).getItem() instanceof BeamReactorHelmet) {
            Utils.toggleBeamLaser(player, Utils.isBeamLaserEnabled(player));
        }
        else {
            player.displayClientMessage(Component.literal("Unable to link to chest reactor, reactor not found..."), true);
        }
        return InteractionResultHolder.success(stack);
    }


}
