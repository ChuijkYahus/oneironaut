package net.beholderface.oneironaut.item;

import at.petrak.hexcasting.api.item.MediaHolderItem;
import at.petrak.hexcasting.api.misc.MediaConstants;
import at.petrak.hexcasting.api.utils.HexUtils;
import net.beholderface.oneironaut.Oneironaut;
import net.beholderface.oneironaut.casting.lichdom.LichData;
import net.beholderface.oneironaut.casting.lichdom.LichdomManager;
import net.beholderface.oneironaut.registry.OneironautMiscRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class MonkfruitItem extends AliasedBlockItem {
    public MonkfruitItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (user instanceof PlayerEntity player){
            var effects = player.getActiveStatusEffects();
            StatusEffect rumination = OneironautMiscRegistry.RUMINATION.get();
            if (effects.containsKey(rumination)){
                StatusEffectInstance instance = effects.get(rumination);
                effects.put(rumination, new StatusEffectInstance(rumination, instance.getDuration() + 100, 0, false, false, true));
            } else {
                player.addStatusEffect(new StatusEffectInstance(rumination, 100, 0, false, false, true));
            }
        }
        return user.eatFood(world, stack);
    }
}
