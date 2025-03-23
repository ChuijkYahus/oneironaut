package net.beholderface.oneironaut.casting.environments;

import at.petrak.hexcasting.api.casting.eval.env.PlayerBasedCastEnv;
import at.petrak.hexcasting.api.pigment.FrozenPigment;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import net.beholderface.oneironaut.casting.lichdom.LichData;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;

public class LichPassiveHexEnv extends PlayerBasedCastEnv {
    public final LichData lichData;
    public LichPassiveHexEnv(ServerPlayerEntity caster, LichData lichData) {
        super(caster, null);
        this.lichData = lichData;
    }

    @Override
    protected long extractMediaEnvironment(long cost, boolean simulate) {
        return this.lichData.withdrawMedia(cost, simulate);
    }

    @Override
    public Hand getCastingHand() {
        return this.castingHand;
    }

    @Override
    public FrozenPigment getPigment() {
        return IXplatAbstractions.INSTANCE.getPigment(this.caster);
    }
}
