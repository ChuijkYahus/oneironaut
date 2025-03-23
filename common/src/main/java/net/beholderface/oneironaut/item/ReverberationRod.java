package net.beholderface.oneironaut.item;

import at.petrak.hexcasting.api.casting.eval.ResolvedPatternType;
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.common.items.magic.ItemPackagedHex;
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds;
import net.beholderface.oneironaut.Oneironaut;
import net.beholderface.oneironaut.casting.environments.ReverbRodCastEnv;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ReverberationRod extends ItemPackagedHex  {

    public static final Identifier CASTING_PREDICATE = new Identifier(Oneironaut.MOD_ID, "is_casting");
    private static final Map<UUID, ReverbRodCastEnv> ROD_ENV_MAP = new HashMap<>();
    public ReverberationRod(Settings settings){
        super(settings);
    }

    @Override
    public boolean breakAfterDepletion() {
        return false;
    }

    @Override
    public int cooldown() {
        return 1;
    }

    @Override
    public boolean canDrawMediaFromInventory(ItemStack stack) {
        return false;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.NONE;
    }


    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand usedHand) {
        var stack = player.getStackInHand(usedHand);
        if (!hasHex(stack)) {
            return TypedActionResult.fail(stack);
        }
        Stat<?> stat = Stats.USED.getOrCreateStat(this);
        player.incrementStat(stat);
        if (!world.isClient && player instanceof ServerPlayerEntity serverPlayer){
            ROD_ENV_MAP.put(serverPlayer.getUuid(), new ReverbRodCastEnv(serverPlayer, usedHand, true));
        }
        player.setCurrentHand(usedHand);
        //cast immediately on use rather than waiting for the next tick
        stack.usageTick(world, player, stack.getMaxUseTime());
        return TypedActionResult.consume(stack);
    }
    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (user.isPlayer() && !world.isClient){
            ServerPlayerEntity sPlayer = (ServerPlayerEntity) user; //world.getPlayerByUuid(user.getUuid()).getServer().getPlayerManager().getPlayer(user.getUuid());
            Hand usedHand;
            if(sPlayer.getStackInHand(Hand.MAIN_HAND) == stack){
                usedHand = Hand.MAIN_HAND;
            } else {
                usedHand = Hand.OFF_HAND;
            }
            ReverbRodCastEnv env = ROD_ENV_MAP.get(sPlayer.getUuid());
            env.setCastInProgress(true);
            try {
                if(!castHex(stack, (ServerWorld) world, sPlayer, usedHand)){
                    sPlayer.stopUsingItem();
                }
            } finally {
                env.setCastInProgress(false);
            }
        }
    }

    private boolean castHex(ItemStack stack, ServerWorld world, ServerPlayerEntity sPlayer, Hand usedHand){
        List<Iota> instrs = getHex(stack, world);
        assert instrs != null;
        ReverbRodCastEnv env = ROD_ENV_MAP.get(sPlayer.getUuid());
        int delay = env.getDelay();
        if (delay <= 0){
            if (delay < 0){
                env.setDelay(0);
            }
            var ctx = ROD_ENV_MAP.get(sPlayer.getUuid());
            var harness = CastingVM.empty(ctx);
            var info = harness.queueExecuteAndWrapIotas(instrs, ctx.getWorld());
            var sound = ctx.getSound().sound();
            if (sound != null) {
                var soundPos = sPlayer.getPos();
                if (world.getTime() >= ctx.lastSoundTimestamp + 30 || ctx.getSound() == HexEvalSounds.MISHAP){
                    sPlayer.getWorld().playSound(null, soundPos.x, soundPos.y, soundPos.z, sound, SoundCategory.PLAYERS, 1f, 1f);
                    ctx.updateLastSoundtimestamp();
                } else {
                    sPlayer.getWorld().playSound(null, soundPos.x, soundPos.y, soundPos.z, SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME,
                            SoundCategory.PLAYERS, 1f, 1f);
                }
            }
            if (info.getResolutionType().equals(ResolvedPatternType.ERRORED)){
                env.setResetCooldown(20);
                return false;
            }
            return env.getCurrentlyCasting();
        } else {
            env.adjustDelay(-1);
        }
        return true;
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user.isPlayer() && !world.isClient){
            ServerPlayerEntity sPlayer = (ServerPlayerEntity) user;//world.getPlayerByUuid(user.getUuid()).getServer().getPlayerManager().getPlayer(user.getUuid());
            //assert sPlayer != null;
            //assert stack.getNbt() != null;
            ReverbRodCastEnv env = ROD_ENV_MAP.get(user.getUuid());
            sPlayer.getItemCooldownManager().set(this, env.getResetCooldown());
            env.setCurrentlyCasting(false);
            ROD_ENV_MAP.remove(user.getUuid());
            //Oneironaut.LOGGER.info("Stopped casting from rod.");
        }
    }

    public static ReverbRodCastEnv getEnv(Entity user){
        return ROD_ENV_MAP.getOrDefault(user.getUuid(), null);
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 20 * 60 * 60;
    }
}
