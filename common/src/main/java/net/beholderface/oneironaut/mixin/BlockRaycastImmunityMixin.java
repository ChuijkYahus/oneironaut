package net.beholderface.oneironaut.mixin;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.NullIota;
import at.petrak.hexcasting.api.casting.iota.Vec3Iota;
import at.petrak.hexcasting.common.casting.actions.raycast.OpBlockAxisRaycast;
import at.petrak.hexcasting.common.casting.actions.raycast.OpBlockRaycast;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import kotlin.collections.CollectionsKt;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.RaycastContext;
import net.beholderface.oneironaut.MiscAPIKt;
import net.beholderface.oneironaut.Oneironaut;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(value = {OpBlockRaycast.class, OpBlockAxisRaycast.class})
public abstract class BlockRaycastImmunityMixin {
    @ModifyReturnValue(method = "execute", at = @At(value = "RETURN", remap = false), remap = false)
    private List<Iota> nullIfImmune(List<Iota> original, @Local CastingEnvironment env, @Local BlockHitResult hit){
        if (original.get(0) instanceof Vec3Iota vec3){
            BlockPos pos = hit.getBlockPos();
            //BlockPos pos = new BlockPos(vec3.getVec3());
            if (env.getWorld().getBlockState(pos).isIn(MiscAPIKt.getBlockTagKey(new Identifier(Oneironaut.MOD_ID, "blocksraycast")))){
                return CollectionsKt.listOf(new NullIota());
            }
        }
        return original;
    }
}
