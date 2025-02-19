package net.beholderface.oneironaut.fabric.mixin;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.beholderface.oneironaut.item.ItemLibraryCard;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = {
        "at/petrak/hexcasting/common/casting/actions/akashic/OpAkashicRead",
        "ram/talia/hexal/common/casting/actions/everbook/OpEverbookWrite"
})
public class MixinHonorLibraryCardReads {
    @WrapOperation(method = {
            "execute"
    },
            at = @At(value = "INVOKE", target="at/petrak/hexcasting/api/casting/eval/CastingEnvironment.getWorld ()Lnet/minecraft/server/world/ServerWorld;"))
    public ServerWorld getAlternateLibraryDim(CastingEnvironment ctx, Operation<ServerWorld> original){
        ServerWorld originalWorld = original.call(ctx);
        if(!(ctx.getCastingEntity() instanceof ServerPlayerEntity)) return originalWorld;
        PlayerInventory pInv = ctx.getCaster().getInventory();
        for(int i = 0; i < pInv.size(); i++){
            ItemStack stack = pInv.getStack(i);
            if(stack.getItem() instanceof ItemLibraryCard libCard){
                RegistryKey<World> dim = libCard.getDimension(stack);
                if(dim != null){
                    ServerWorld newWorld = originalWorld.getServer().getWorld(dim);
                    if(newWorld != null) return newWorld;
                }
            }
        }
        return originalWorld;
    }
}