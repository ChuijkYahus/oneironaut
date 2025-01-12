package net.beholderface.oneironaut;

import at.petrak.hexcasting.api.pigment.FrozenPigment;
import at.petrak.hexcasting.common.items.ItemStaff;
import at.petrak.hexcasting.common.lib.HexItems;
import dev.architectury.event.CompoundEventResult;
import dev.architectury.event.events.common.InteractionEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.TickEvent;
import net.beholderface.oneironaut.block.blockentity.HoverElevatorBlockEntity;
import net.beholderface.oneironaut.casting.DepartureEntry;
import net.beholderface.oneironaut.casting.IdeaInscriptionManager;
import net.beholderface.oneironaut.item.BottomlessMediaItem;
import net.beholderface.oneironaut.recipe.OneironautRecipeSerializer;
import net.beholderface.oneironaut.recipe.OneironautRecipeTypes;
import net.beholderface.oneironaut.registry.*;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ram.talia.hexal.common.entities.WanderingWisp;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import static net.beholderface.oneironaut.MiscAPIKt.getItemTagKey;
import static net.beholderface.oneironaut.MiscAPIKt.stringToWorld;

/**
 * This is effectively the loading entrypoint for most of your code, at least
 * if you are using Architectury as intended.
 */
public class Oneironaut {
    public static final String MOD_ID = "oneironaut";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    private static final List<Item> randomWispPigments = new ArrayList<>();
    private static ServerWorld noosphere = null;


    public static void init() {
        LOGGER.info("why do they call it oven when you of in the cold food of out hot eat the food");
        OneironautMiscRegistry.init();
        OneironautBlockRegistry.init();
        OneironautItemRegistry.init();
        OneironautFeatureRegistry.init();
        OneironautIotaTypeRegistry.init();
        OneironautPatternRegistry.init();
        //Registry.register(Registry.RECIPE_SERIALIZER, OneironautRecipeSerializer.)
        OneironautRecipeSerializer.registerSerializers(OneironautRecipeTypes.Companion.bind(Registries.RECIPE_SERIALIZER));
        OneironautRecipeTypes.registerTypes(OneironautRecipeTypes.Companion.bind(Registries.RECIPE_TYPE));

        //Registry.register(Registry.CHUNK_GENERATOR, new Identifier(MOD_ID, "noosphere"))

        //LOGGER.info(OneironautAbstractions.getConfigDirectory().toAbsolutePath().normalize().toString());
        LifecycleEvent.SERVER_STARTED.register((startedserver) ->{
            noosphere = stringToWorld("oneironaut:noosphere", startedserver);
            IdeaInscriptionManager ideaState = IdeaInscriptionManager.getServerState(startedserver);
            IdeaInscriptionManager.cleanMap(startedserver, ideaState);
            ideaState.markDirty();
            randomWispPigments.addAll(HexItems.DYE_PIGMENTS.values());
            randomWispPigments.addAll(HexItems.PRIDE_PIGMENTS.values());
            randomWispPigments.add(HexItems.DEFAULT_PIGMENT);
            randomWispPigments.add(HexItems.UUID_PIGMENT);
            randomWispPigments.add(OneironautItemRegistry.PIGMENT_NOOSPHERE.get());
            randomWispPigments.add(OneironautItemRegistry.PIGMENT_FLAME.get());
        });

        TickEvent.SERVER_PRE.register((server) -> {
            BottomlessMediaItem.time = server.getOverworld().getTime();
        });

        LOGGER.info("Registering server-side hoverlift processor.");
        TickEvent.SERVER_POST.register((server)->{
            try {
                HoverElevatorBlockEntity.processHover(true, server.getOverworld().getTime());
            } catch (ConcurrentModificationException exception){
                LOGGER.error("Oopsie server-side hoverlift exception " + exception.getMessage());
            }
            DepartureEntry.clearMap();
            ServerPlayerEntity player = noosphere.getRandomAlivePlayer();
            Random rand = noosphere.random;
            if (player != null && rand.nextInt(1024) == 0){
                double gaussDistance = 16.0;
                WanderingWisp wisp = new WanderingWisp(noosphere, player.getPos().add(
                        rand.nextGaussian() * gaussDistance, rand.nextGaussian() * gaussDistance, rand.nextGaussian() * gaussDistance));
                ItemStack stack = randomWispPigments.get(rand.nextInt(randomWispPigments.size())).getDefaultStack();
                wisp.setPigment(new FrozenPigment(stack, ((Entity)wisp).getUuid()));
                noosphere.spawnEntity(wisp);
            }
        });

        ItemStack fakeStaffStack = HexItems.STAFF_OAK.getDefaultStack();
        TagKey<Item> realStaffTag = getItemTagKey(new Identifier("hexcasting:staves"));
        TagKey<Item> fakeStaffTag = getItemTagKey(new Identifier("oneironaut:datapack_staves"));
        InteractionEvent.RIGHT_CLICK_ITEM.register((player, hand) -> {
            ItemStack heldStack = player.getStackInHand(hand);
            if (heldStack.isIn(fakeStaffTag) && !(heldStack.getItem() instanceof ItemStaff)){
                if (heldStack.isIn(realStaffTag)){
                    fakeStaffStack.use(player.getWorld(), player, hand);
                    player.swingHand(hand);
                } else {
                    LOGGER.info(player.getName().getString() + " has right-clicked an item tagged as a datapacked staff, but that item does not have the normal staff tag, which is necessary for the datapack staff functionality to work.");
                }
            }
            return CompoundEventResult.pass();
        });
        //IdeaInscriptionManager ideaState = IdeaInscriptionManager.getServerState()
    }

    //for easily toggling whether several things should be logged without having to search through the whole file
    public static void boolLogger(String str, boolean bool){
        if (bool){
            LOGGER.info(str);
        }
    }

    /**
     * Shortcut for identifiers specific to this mod.
     */
    public static Identifier id(String string) {
        return new Identifier(MOD_ID, string);
    }
    public static ServerWorld getNoosphere(){
        if (noosphere == null){
            throw new IllegalStateException("getNoosphere method called before server start");
        }
        return noosphere;
    }
}
