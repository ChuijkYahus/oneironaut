package net.beholderface.oneironaut.casting.lichdom;

import at.petrak.hexcasting.api.casting.eval.ResolvedPatternType;
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM;
import at.petrak.hexcasting.api.utils.NBTHelper;
import net.beholderface.oneironaut.Oneironaut;
import net.beholderface.oneironaut.casting.environments.LichPassiveHexEnv;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class LichdomManager extends PersistentState {
    public LichdomManager(){

    }
    private static Map<UUID, LichData> LICH_DATA_MAP = new HashMap<>();
    public static LichData getLichData(ServerPlayerEntity player){
        return LICH_DATA_MAP.getOrDefault(player.getUuid(), null);
    }
    public static boolean isPlayerLich(@Nullable ServerPlayerEntity player){
        return player != null && LICH_DATA_MAP.containsKey(player.getUuid());
    }

    public static boolean lichifyPlayer(ServerPlayerEntity player){
        if (!isPlayerLich(player)){
            LICH_DATA_MAP.put(player.getUuid(), new LichData());
            return true;
        } else {
            return false;
        }
    }
    public static boolean unlichPlayer(ServerPlayerEntity player){
        if (isPlayerLich(player)){
            LICH_DATA_MAP.remove(player.getUuid());
            return true;
        }
        return false;
    }

    public static void tick(MinecraftServer server){
        List<UUID> uuidsToRemove = new ArrayList<>();
        for (UUID uuid : LICH_DATA_MAP.keySet()){
            //cast the hex
            ServerPlayerEntity player = server.getPlayerManager().getPlayer(uuid);
            if (!isPlayerLich(player)){
                uuidsToRemove.add(uuid);
                continue;
            }
            LichData data = LICH_DATA_MAP.get(uuid);
            if (server.getOverworld().getTime() % 20 == 0 && data.getPassiveHex() != null && !data.getPassiveHex().isEmpty() && !data.isOnCooldown()){
                LichPassiveHexEnv env = new LichPassiveHexEnv(player, data);
                var harness = CastingVM.empty(env);
                var info = harness.queueExecuteAndWrapIotas(data.getPassiveHex(), env.getWorld());
                if (info.getResolutionType() == ResolvedPatternType.ERRORED){
                    data.adjustAbilityCooldown(100L);
                }
            }
            if (data.isOnCooldown()){
                data.adjustAbilityCooldown(-1L);
            }
        }
        if (!uuidsToRemove.isEmpty()){
            for (UUID uuid : uuidsToRemove){
                LICH_DATA_MAP.remove(uuid);
            }
        }
    }

    public static LichdomManager createFromNbt(NbtCompound data){
        LichdomManager lichManager = new LichdomManager();
        ServerWorld world = Oneironaut.getNoosphere();
        Map<UUID, LichData> newMap = new HashMap<>();
        for (String uuidString : data.getKeys()){
            UUID uuid = UUID.fromString(uuidString);
            LichData data2 = LichData.deserialize(data.getCompound(uuidString), world);
            if (data2 != null){
                newMap.put(uuid, data2);
            }
        }
        LICH_DATA_MAP = newMap;
        return lichManager;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        for (UUID uuid : LICH_DATA_MAP.keySet()){
            NBTHelper.putCompound(nbt, uuid.toString(), LICH_DATA_MAP.get(uuid).serialize());
        }
        return nbt;
    }

    public static LichdomManager getServerState(MinecraftServer server){
        PersistentStateManager stateManager = Oneironaut.getNoosphere().getPersistentStateManager();
        LichdomManager ideas = stateManager.getOrCreate(LichdomManager::createFromNbt, LichdomManager::new, Oneironaut.MOD_ID);
        ideas.markDirty();
        return ideas;
    }
}
