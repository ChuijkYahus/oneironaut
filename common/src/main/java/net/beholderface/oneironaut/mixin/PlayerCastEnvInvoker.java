package net.beholderface.oneironaut.mixin;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.env.PlayerBasedCastEnv;
import at.petrak.hexcasting.api.casting.eval.sideeffects.OperatorSideEffect;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(PlayerBasedCastEnv.class)
public interface PlayerCastEnvInvoker {
    @Invoker("extractMediaFromInventory")
    long extract(long costLeft, boolean allowOvercast, boolean simulate);
    //naming it just canOvercast results in an infinite loop
    @Invoker("canOvercast")
    boolean canItOvercast();
    @Invoker("sendMishapMsgToPlayer")
    void sendMishapMsgPlayer(OperatorSideEffect.DoMishap mishap);
    @Invoker("isCreativeMode")
    boolean isCreative();

}
