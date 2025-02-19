package net.beholderface.oneironaut.block.blockentity;

import at.petrak.hexcasting.common.particles.ConjureParticleOptions;
import net.beholderface.oneironaut.MiscAPIKt;
import net.beholderface.oneironaut.Oneironaut;
import net.beholderface.oneironaut.registry.OneironautBlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.*;
import static net.beholderface.oneironaut.MiscAPIKt.playerUUIDtoServerPlayer;

public class NoosphereGateEntity extends BlockEntity {
    public static Map<RegistryKey<World>, Map<BlockPos, Vec3d>> gateLocationMap = new HashMap<>();
    public NoosphereGateEntity(BlockPos pos, BlockState state) {
        super(OneironautBlockRegistry.NOOSPHERE_GATE_ENTITY.get(), pos, state);
        //Oneironaut.LOGGER.info("super Creating blockentity.");
    }
    public void tick(World world, BlockPos pos, BlockState state){
        //Oneironaut.LOGGER.info("Spam.");
        Vec3d doublePos = new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        if (!world.isClient){
            PlayerEntity possiblePassenger = world.getClosestPlayer(doublePos.x, doublePos.y, doublePos.z, 2.0, false);
            if (possiblePassenger != null){
                MinecraftServer server = possiblePassenger.getServer();
                ServerPlayerEntity player = (ServerPlayerEntity) possiblePassenger;
                //Vec3d passengerMidpoint = new Vec3d(possiblePassenger.getX(), possiblePassenger.getY() + (possiblePassenger.getHeight() / 2), possiblePassenger.getZ());
                if (player.getBoundingBox().intersects(new Box(pos))){
                    //possiblePassenger.sendMessage(Text.of("teleporting"));
                    ServerWorld noosphere = null;
                    ServerWorld origin = (ServerWorld) player.getWorld();
                    ServerWorld current = origin;
                    //Oneironaut.LOGGER.info("Current: " + current.getRegistryKey().getValue().toString());
                    if (!(current.getRegistryKey().getValue().toString().equals("oneironaut:noosphere"))){
                        //Oneironaut.LOGGER.info("Iterating.");
                        for (ServerWorld serverWorld : server.getWorlds()) {
                            current = serverWorld;
                            //Oneironaut.LOGGER.info("Current: " + current.getRegistryKey().getValue().toString());
                            if (current.getRegistryKey().getValue().toString().equals("oneironaut:noosphere")) {
                                noosphere = current;
                                //Oneironaut.LOGGER.info("Noosphere REALLY shouldn't be null.");
                                break;
                            }
                        }
                    } else {
                        noosphere = current;
                    }
                    /*if (noosphere != null){
                        Oneironaut.LOGGER.info("Noosphere: " + noosphere.getRegistryKey().getValue().toString());
                    } else {
                        Oneironaut.LOGGER.info("Noosphere is null.");
                    }*/
                    //Oneironaut.LOGGER.info("Noosphere: " + noosphere.toString());
                    double compressionFactor;
                    if (noosphere != null){
                        if (noosphere == origin){
                            ServerWorld homeDim = server.getWorld(player.getSpawnPointDimension());
                            boolean antiSoftlock = false;
                            if (homeDim == null || homeDim == noosphere){
                                homeDim = server.getOverworld();
                                antiSoftlock = true;
                            }
                            compressionFactor = 1 / homeDim.getDimension().coordinateScale();
                            BlockPos spawnpoint = player.getSpawnPointPosition();
                            if (spawnpoint == null || antiSoftlock){
                                spawnpoint = homeDim.getSpawnPos();
                                //Oneironaut.LOGGER.info("Spawnpoint was null, using world spawn");
                            }
                            player.teleport(homeDim, spawnpoint.getX() + 0.5, spawnpoint.getY(), spawnpoint.getZ() + 0.5, player.getYaw(), player.getPitch());
                            player.sendAbilitiesUpdate();
                        } else {
                            //iterate to find the ground
                            double altitude = 321;
                            while (noosphere.getBlockState(new BlockPos(pos.getX(), (int) altitude, pos.getZ())).isAir()){
                                altitude -= 1;
                                //don't go into the void
                                if (altitude < -64){
                                    altitude = 321;
                                    break;
                                }
                            }
                            compressionFactor = origin.getDimension().coordinateScale();
                            Vec3d destPos = new Vec3d(floor(pos.getX() * compressionFactor) + 0.5, altitude + 1, floor(pos.getZ() * compressionFactor) + 0.5);
                            WorldBorder border = noosphere.getWorldBorder();
                            //make sure you don't end up outside the world border
                            if (destPos.x > border.getBoundEast()){
                                destPos = new Vec3d((border.getBoundEast() - 2), destPos.y, destPos.z);
                            } else if (destPos.x < border.getBoundWest()){
                                destPos = new Vec3d((border.getBoundWest() + 2), destPos.y, destPos.z);
                            }
                            if (destPos.z > border.getBoundSouth()){
                                destPos = new Vec3d(destPos.x, destPos.y, (border.getBoundSouth() - 2));
                            } else if (destPos.z < border.getBoundNorth()){
                                destPos = new Vec3d(destPos.x, destPos.y, (border.getBoundNorth() + 2));
                            }
                            Vec3i destPosI = MiscAPIKt.toVec3i(destPos);
                            if (noosphere.getBlockState(new BlockPos(destPosI).down()).getBlock().equals(OneironautBlockRegistry.NOOSPHERE_GATE.get())){
                                //Oneironaut.LOGGER.info("found a portal at the destination OwO " + new BlockPos(destPos).down().toString());
                                if (!(noosphere.getBlockState(new BlockPos(destPosI).east().down()).getBlock().equals(OneironautBlockRegistry.NOOSPHERE_GATE.get()))){
                                    //Oneironaut.LOGGER.info("found a portal at the east OwO " + new BlockPos(destPos).down().east().toString());
                                    destPos = destPos.add(1, 0, 0);
                                } else if (!(noosphere.getBlockState(new BlockPos(destPosI).west().down()).getBlock().equals(OneironautBlockRegistry.NOOSPHERE_GATE.get()))){
                                    //Oneironaut.LOGGER.info("found a portal at the west OwO " + new BlockPos(destPos).down().west().toString());
                                    destPos = destPos.add(-1, 0, 0);
                                } else if (!(noosphere.getBlockState(new BlockPos(destPosI).south().down()).getBlock().equals(OneironautBlockRegistry.NOOSPHERE_GATE.get()))){
                                    //Oneironaut.LOGGER.info("found a portal at the south OwO " + new BlockPos(destPos).down().south().toString());
                                    destPos = destPos.add(0, 0, 1);
                                } else if (!(noosphere.getBlockState(new BlockPos(destPosI).north().down()).getBlock().equals(OneironautBlockRegistry.NOOSPHERE_GATE.get()))){
                                    //Oneironaut.LOGGER.info("found a portal at the north OwO " + new BlockPos(destPos).down().north().toString());
                                    destPos = destPos.add(0, 0, -1);
                                }
                            }/* else {
                                Oneironaut.LOGGER.info("Couldn't find an orthogonally adjacent spot without a portal. >:(");
                            }*/
                            //Oneironaut.LOGGER.info("Teleporting to " + new BlockPos(destPos).toString());
                            player.teleport(noosphere, destPos.x, destPos.y, destPos.z, player.getYaw(), player.getPitch());
                            player.sendAbilitiesUpdate();
                            if (altitude >= 321){
                                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 1200));
                            }
                        }
                    }
                }
            }
            //maintain wisps
            RegistryKey<World> worldKey = world.getRegistryKey();
            //don't do that if the current world is the noosphere, because the wisp will never check the map if it's in the noosphere and it'd be pointless lag
            if (!(gateLocationMap.containsKey(worldKey)) && !worldKey.getValue().toString().equals("oneironaut:noosphere")){
                Map<BlockPos, Vec3d> newMap = new HashMap<BlockPos, Vec3d>();
                newMap.put(pos, new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5));
                gateLocationMap.put(worldKey, newMap);
            } else if (gateLocationMap.containsKey(worldKey)){
                Map<BlockPos, Vec3d> existingMap = gateLocationMap.get(worldKey);
                if (!(existingMap.containsKey(pos))){
                    existingMap.put(pos, new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5));
                }
            }
        } else {
            //purple slipway thing
            Random rand = world.random;
            int i = 0;
            while (i < 8){
                i++;
                double speedMultiplier = 0.025;
                double gaussX = rand.nextGaussian();
                double gaussY = rand.nextGaussian();
                double gaussZ = rand.nextGaussian();
                while (gaussX == 0){
                    gaussX = rand.nextGaussian();
                }
                double gaussNormalize = 1 / (pow((pow(gaussX, 2) + pow(gaussY, 2) + pow(gaussZ, 2)), 0.5));
                gaussX = gaussX * gaussNormalize;
                gaussY = gaussY * gaussNormalize;
                gaussZ = gaussZ * gaussNormalize;
                if(!world.getRegistryKey().getValue().toString().equals("oneironaut:noosphere")){
                    double particlePosX = doublePos.x + gaussX * rand.nextDouble();
                    double particlePosY = doublePos.y + gaussY * rand.nextDouble();
                    double particlePosZ = doublePos.z + gaussZ * rand.nextDouble();
                    double particleVelX = (signum(particlePosX - doublePos.x) * speedMultiplier) * rand.nextDouble();
                    double particleVelY = (signum(particlePosY - doublePos.y) * speedMultiplier) * rand.nextDouble();
                    double particleVelZ = (signum(particlePosZ - doublePos.z) * speedMultiplier) * rand.nextDouble();
                    world.addParticle(
                            new ConjureParticleOptions(0x6a31d2),
                            particlePosX, particlePosY, particlePosZ,
                            particleVelX, particleVelY, particleVelZ
                    );
                } else {
                    double particlePosX = doublePos.x + gaussX * (rand.nextDouble() + 1); /*(((rand.nextInt(9) + 1) - 5) / noosphereBoxDivisor);*/
                    double particlePosY = doublePos.y + gaussY * (rand.nextDouble() + 1);
                    double particlePosZ = doublePos.z + gaussZ * (rand.nextDouble() + 1);
                    double particleVelX = (signum(particlePosX - doublePos.x) * speedMultiplier)/* * rand.nextDouble()*/;
                    double particleVelY = (signum(particlePosY - doublePos.y) * speedMultiplier)/* * rand.nextDouble()*/;
                    double particleVelZ = (signum(particlePosZ - doublePos.z) * speedMultiplier)/* * rand.nextDouble()*/;
                    world.addParticle(
                            new ConjureParticleOptions(0x6a31d2),
                            particlePosX, particlePosY, particlePosZ,
                            particleVelX * -1, particleVelY * -1, particleVelZ * -1
                    );
                }
            }
        }

        //world.setBlockState(pos.add(0, 1, 0), Blocks.ACACIA_FENCE.getDefaultState());
    }
}