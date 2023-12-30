package net.oneironaut.mixin;

import at.petrak.hexcasting.api.spell.Action;
import at.petrak.hexcasting.api.spell.casting.CastingContext;
import at.petrak.hexcasting.api.spell.casting.CastingHarness;
import at.petrak.hexcasting.common.casting.operators.OpEntityRaycast;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.RaycastContext;
import net.oneironaut.Oneironaut;
import net.oneironaut.registry.OneironautThingRegistry;
import net.oneironaut.MiscAPIKt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Predicate;

@Mixin(value = OpEntityRaycast.class)
public abstract class EntityRaycastImmunityMixin {
    @ModifyVariable(method = "execute", at = @At(value = "STORE"), remap = false)
    private EntityHitResult skipImmune(EntityHitResult value){
        if (value != null){
            Entity entity = value.getEntity();
            if (entity instanceof LivingEntity livingEntity){
                if (livingEntity.hasStatusEffect(OneironautThingRegistry.DETECTION_RESISTANCE.get())){
                    return null;
                } else {
                    return value;
                }
            } else {
                return value;
            }
        } else {
            return value;
        }
    }
    @Redirect(method = "execute", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/entity/projectile/ProjectileUtil;raycast(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;D)Lnet/minecraft/util/hit/EntityHitResult;"
    ))
    private EntityHitResult blockableRaycast(Entity entity, Vec3d origin, Vec3d max, Box box, Predicate<Entity> predicate, double d){
        EntityHitResult unmodifiedResult = ProjectileUtil.raycast(entity, origin, max, box, predicate, d);
        if (unmodifiedResult != null){
            CastingHarness mainharness = IXplatAbstractions.INSTANCE.getHarness((ServerPlayerEntity) entity, Hand.MAIN_HAND);
            CastingHarness offharness = IXplatAbstractions.INSTANCE.getHarness((ServerPlayerEntity) entity, Hand.OFF_HAND);
            CastingHarness harness = mainharness != null ? mainharness : offharness;
            CastingContext ctx = harness.getCtx();
            Vec3d entityHitPos = unmodifiedResult.getPos();
            //double stepResolution = 32.0;
            //Vec3d stepDirection = entityHitPos.subtract(origin).normalize().multiply(1.0/stepResolution);
            ServerWorld world = ctx.getWorld();
            //TagKey<Block> raycastblocking = TagKey.of(Registry.BLOCK_KEY, new Identifier(Oneironaut.MOD_ID, "blocksraycast"));
            BlockHitResult blockcast = world.raycast(new RaycastContext(origin, Action.Companion.raycastEnd(origin, max.subtract(origin)), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, (Entity)ctx.getCaster()));
            Vec3d blockHitPos = blockcast.getPos();
            if (blockcast.getType().equals(HitResult.Type.MISS)){
                return unmodifiedResult;
            }
            if (origin.distanceTo(entityHitPos) >= origin.distanceTo(blockHitPos)){
                if(world.getBlockState(new BlockPos(blockHitPos)).isIn(MiscAPIKt.getBlockTagKey(new Identifier(Oneironaut.MOD_ID, "blocksraycast")))){
                    return null;
                }
            }
            /*for (int i = 0; i < (Math.floor(origin.distanceTo(entityHitPos))) * stepResolution; i++){
                if (world.getBlockState(new BlockPos(origin.add(stepDirection.multiply(i)))).isIn(MiscAPIKt.getBlockTagKey(new Identifier(Oneironaut.MOD_ID, "blocksraycast")))){
                    return null;
                }
            }*/
        }
        return unmodifiedResult;
    }
}
