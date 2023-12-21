package net.oneironaut.registry;

import at.petrak.hexcasting.api.PatternRegistry;
import at.petrak.hexcasting.api.misc.MediaConstants;
import at.petrak.hexcasting.api.spell.Action;
import at.petrak.hexcasting.api.spell.math.HexDir;
import at.petrak.hexcasting.api.spell.math.HexPattern;
import at.petrak.hexcasting.api.utils.HexUtils;
import at.petrak.hexcasting.common.casting.operators.spells.OpMakePackagedSpell;
import at.petrak.hexcasting.common.items.magic.ItemPackagedHex;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import kotlin.Triple;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;
import net.oneironaut.Oneironaut;
import net.oneironaut.casting.patterns.OpFrameItem;
import net.oneironaut.casting.patterns.OpFrameRotation;
import net.oneironaut.casting.patterns.OpGetDim;
import net.minecraft.util.Identifier;
import net.oneironaut.casting.patterns.rod.OpDelayRod;
import net.oneironaut.casting.patterns.rod.OpGetInitialRodState;
import net.oneironaut.casting.patterns.rod.OpHaltRod;
import net.oneironaut.casting.patterns.spells.OpReadIdea;
import net.oneironaut.casting.patterns.spells.OpSplatoon;
import net.oneironaut.casting.patterns.spells.OpWriteIdea;
import net.oneironaut.casting.patterns.spells.great.OpDimTeleport;
import net.oneironaut.casting.patterns.spells.great.OpInfuseMedia;
import net.oneironaut.casting.patterns.spells.great.OpSwapSpace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.oneironaut.Oneironaut.id;

public class OneironautPatternRegistry {
    //public static final DeferredRegister<HexPattern> HEX_PATTERNS = DeferredRegister.create(Oneironaut.MOD_ID, Hex);
    public static List<Triple<HexPattern, Identifier, Action>> PATTERNS = new ArrayList<>();
    public static List<Triple<HexPattern, Identifier, Action>> PER_WORLD_PATTERNS = new ArrayList<>();
    public static HexPattern GETDIM_1 = register(HexPattern.fromAngles("wqwqwqwqwqwaeqqe", HexDir.WEST), "getdim1", new OpGetDim(false, MediaConstants.DUST_UNIT / 100));
    public static HexPattern GETDIM_2 = register(HexPattern.fromAngles("wqwqwqwqwqwaqeeq", HexDir.WEST), "getdim2", new OpGetDim(true, MediaConstants.DUST_UNIT / 10));
    public static HexPattern PAINT_CONJURED = register(HexPattern.fromAngles("eqdweeqdwweeqddqdwwwdeww", HexDir.WEST), "paintconjured", new OpSplatoon());
/*
    public static HexPattern CRAFT_ROD = register(HexPattern.fromAngles("eqqqqqawweqqqqqawweqqqqqawwdeqewwwwweqeeeqewwwwweqe", HexDir.EAST), "craftrod", new OpMakePackagedSpell<>((ItemPackagedHex) OneironautThingRegistry.REVERBERATION_ROD.get(), MediaConstants.CRYSTAL_UNIT * 10));
*/
    public static HexPattern ROD_LOOK = register(HexPattern.fromAngles("qwqqqwqawa", HexDir.SOUTH_EAST), "getrodlook", new OpGetInitialRodState(1));
    public static HexPattern ROD_POS = register(HexPattern.fromAngles("qwqqqwqawaa", HexDir.SOUTH_EAST), "getrodpos", new OpGetInitialRodState(2));
    public static HexPattern ROD_STAMP = register(HexPattern.fromAngles("qwqqqwqawaaw", HexDir.SOUTH_EAST), "getrodstamp", new OpGetInitialRodState(3));
    public static HexPattern DELAY_ROD = register(HexPattern.fromAngles("qwqqqwqaqddq", HexDir.SOUTH_EAST), "delayrod", new OpDelayRod());
    public static HexPattern HALT_ROD = register(HexPattern.fromAngles("aqdeeweeew", HexDir.SOUTH_WEST), "haltrod", new OpHaltRod(0));
    public static HexPattern RESET_ROD = register(HexPattern.fromAngles("deaqqwqqqw", HexDir.SOUTH_EAST), "resetrod", new OpHaltRod(1));
    public static HexPattern READ_FRAME_ROTATION = register(HexPattern.fromAngles("wwawwqwwawwaeae", HexDir.SOUTH_WEST), "readframerotation", new OpFrameRotation(0));
    public static HexPattern SET_FRAME_ROTATION = register(HexPattern.fromAngles("wwawwqwwawwaqdq", HexDir.SOUTH_WEST), "setframerotation", new OpFrameRotation(1));
    //turns out normal Sorter's Purification works on item frames
    //public static HexPattern READ_FRAME_ITEMTYPE = register(HexPattern.fromAngles("wqwqwqwqwqwawedeedqdq", HexDir.WEST), "readframeitem", new OpFrameItem());
    public static HexPattern WRITE_IDEA = register(HexPattern.fromAngles("eweweweweweeewqaqwe", HexDir.EAST), "writeidea", new OpWriteIdea());
    public static HexPattern READ_IDEA = register(HexPattern.fromAngles("qwqwqwqwqwqqqwedewq", HexDir.WEST), "readidea", new OpReadIdea());
    public static HexPattern DIM_TELEPORT = registerPerWorld(HexPattern.fromAngles("qeewwwweeqeqeewwwweeqdqqdwwwdqeqdwwwdqdadwwdqdwwddadaqadaawww", HexDir.NORTH_EAST), "dimteleport", new OpDimTeleport());
    public static HexPattern INFUSE_MEDIA = registerPerWorld(HexPattern.fromAngles("wwaqqqqqeqqqwwwqqeqqwwwqqweqadadadaqeqeqadadadaqe", HexDir.EAST), "infusemedia", new OpInfuseMedia());
    public static HexPattern SWAP_SPACE = registerPerWorld(HexPattern.fromAngles("wqqqwwwwwqqqwwwqdaqadwqqwdaqadweqeqqqqeqeqaqeqedeqeqa", HexDir.EAST), "swapspace", new OpSwapSpace());
    public static void init() {
        try {
            for (Triple<HexPattern, Identifier, Action> patternTriple : PATTERNS) {
                PatternRegistry.mapPattern(patternTriple.getFirst(), patternTriple.getSecond(), patternTriple.getThird());
            }
            for (Triple<HexPattern, Identifier, Action> patternTriple : PER_WORLD_PATTERNS) {
                PatternRegistry.mapPattern(patternTriple.getFirst(), patternTriple.getSecond(), patternTriple.getThird(), true);
            }
        } catch (PatternRegistry.RegisterPatternException e) {
            e.printStackTrace();
        }
        registerItemDependentPatterns();

    }
//stolen from gloop
    private static Map<RegistrySupplier<? extends Item>, UncheckedPatternRegister> itemDependentPatternRegisterers = new HashMap<>();

    static {
        itemDependentPatternRegisterers.put(OneironautThingRegistry.REVERBERATION_ROD, () -> {
            PatternRegistry.mapPattern(HexPattern.fromAngles("eqqqqqawweqqqqqawweqqqqqawwdeqewwwwweqeeeqewwwwweqe", HexDir.EAST),
                    new Identifier(Oneironaut.MOD_ID, "craftrod"),
                    new OpMakePackagedSpell<>(OneironautThingRegistry.REVERBERATION_ROD.get(), MediaConstants.CRYSTAL_UNIT * 10));
        });
    }

    private static void registerItemDependentPatterns(){
        for(Map.Entry<RegistrySupplier<? extends Item>, UncheckedPatternRegister> entry : itemDependentPatternRegisterers.entrySet()){
            entry.getKey().listen(item -> {
                try{
                    entry.getValue().register();
                } catch (PatternRegistry.RegisterPatternException exn) {
                    exn.printStackTrace();
                }
            });
        }
    }

    private static HexPattern register(HexPattern pattern, String name, Action action) {
        Triple<HexPattern, Identifier, Action> triple = new Triple<>(pattern, id(name), action);
        PATTERNS.add(triple);
        return pattern;
    }

    private static HexPattern registerPerWorld(HexPattern pattern, String name, Action action) {
        Triple<HexPattern, Identifier, Action> triple = new Triple<>(pattern, id(name), action);
        PER_WORLD_PATTERNS.add(triple);
        return pattern;
    }

    @FunctionalInterface
    public static interface UncheckedPatternRegister{
        public void register() throws PatternRegistry.RegisterPatternException;
    }
}