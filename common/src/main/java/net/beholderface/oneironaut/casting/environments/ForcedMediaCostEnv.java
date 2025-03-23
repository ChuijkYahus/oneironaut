package net.beholderface.oneironaut.casting.environments;

import at.petrak.hexcasting.api.casting.eval.env.PlayerBasedCastEnv;
import at.petrak.hexcasting.api.pigment.FrozenPigment;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;

public class ForcedMediaCostEnv extends PlayerBasedCastEnv {

    public ForcedMediaCostEnv(ServerPlayerEntity caster, Hand castingHand) {
        super(caster, castingHand);
    }

    @Override
    public long extractMediaEnvironment(long cost, boolean simulate) {
        if (this.caster.isCreative())
            return 0;

        var canOvercast = this.canOvercast();
        return this.extractMediaFromInventory(cost, canOvercast, simulate);
    }

    @Override
    public Hand getCastingHand() {
        return Hand.MAIN_HAND;
    }

    @Override
    public FrozenPigment getPigment() {
        return null;
    }
    @Override
    protected boolean canOvercast() {
        return true;
    }
}
