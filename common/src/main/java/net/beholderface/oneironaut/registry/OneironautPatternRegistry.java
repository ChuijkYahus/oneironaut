package net.beholderface.oneironaut.registry;

import at.petrak.hexcasting.api.PatternRegistry;
import at.petrak.hexcasting.api.misc.MediaConstants;
import at.petrak.hexcasting.api.spell.Action;
import at.petrak.hexcasting.api.spell.math.HexDir;
import at.petrak.hexcasting.api.spell.math.HexPattern;
import at.petrak.hexcasting.common.casting.operators.spells.OpMakePackagedSpell;
import dev.architectury.registry.registries.RegistrySupplier;
import kotlin.Triple;
import net.beholderface.oneironaut.Oneironaut;
import net.beholderface.oneironaut.casting.patterns.*;
import net.beholderface.oneironaut.casting.patterns.rod.*;
import net.beholderface.oneironaut.casting.patterns.spells.OpAdvanceAutomaton;
import net.beholderface.oneironaut.casting.patterns.spells.OpCircle;
import net.beholderface.oneironaut.casting.patterns.spells.OpSignItem;
import net.beholderface.oneironaut.casting.patterns.spells.great.*;
import net.beholderface.oneironaut.casting.patterns.spells.idea.OpGetIdeaTimestamp;
import net.beholderface.oneironaut.casting.patterns.spells.idea.OpGetIdeaWriter;
import net.beholderface.oneironaut.casting.patterns.spells.idea.OpReadIdea;
import net.beholderface.oneironaut.casting.patterns.spells.idea.OpWriteIdea;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OneironautPatternRegistry {
    public static List<Triple<HexPattern, Identifier, Action>> PATTERNS = new ArrayList<>();
    public static List<Triple<HexPattern, Identifier, Action>> PER_WORLD_PATTERNS = new ArrayList<>();
    //operators/other actions
    public static HexPattern GETDIM_1 = register(HexPattern.fromAngles("wqwqwqwqwqwaeqqe", HexDir.WEST), "getdim1", new OpGetDim(false, MediaConstants.DUST_UNIT / 100));
    public static HexPattern GETDIM_2 = register(HexPattern.fromAngles("wqwqwqwqwqwaqeeq", HexDir.WEST), "getdim2", new OpGetDim(true, MediaConstants.DUST_UNIT / 10));
    public static HexPattern ROD_LOOK = register(HexPattern.fromAngles("qwqqqwqawa", HexDir.SOUTH_EAST), "getrodlook", new OpGetInitialRodState(1));
    public static HexPattern ROD_POS = register(HexPattern.fromAngles("qwqqqwqawaa", HexDir.SOUTH_EAST), "getrodpos", new OpGetInitialRodState(2));
    public static HexPattern ROD_STAMP = register(HexPattern.fromAngles("qwqqqwqawaaw", HexDir.SOUTH_EAST), "getrodstamp", new OpGetInitialRodState(3));
    public static HexPattern ROD_RAM_READ = register(HexPattern.fromAngles("qeeweeewddw", HexDir.NORTH_EAST), "readrodram", new OpAccessRAM(0));
    public static HexPattern ROD_RAM_WRITE = register(HexPattern.fromAngles("eqqwqqqwaaw", HexDir.NORTH_WEST), "writerodram", new OpAccessRAM(1));
    public static HexPattern READ_IDEA = register(HexPattern.fromAngles("qwqwqwqwqwqqqwedewq", HexDir.WEST), "readidea", new OpReadIdea());
    public static HexPattern READ_IDEA_TIME = register(HexPattern.fromAngles("qwqwqwqwqwqqqeqaqeq", HexDir.WEST), "readideatime", new OpGetIdeaTimestamp());
    public static HexPattern COMPARE_IDEA_WRITER = register(HexPattern.fromAngles("qwqwqwqwqwqaeqedeqe", HexDir.WEST), "readideawriter", new OpGetIdeaWriter());
    public static HexPattern READ_SENTINEL = register(HexPattern.fromAngles("waeawaeddwwd", HexDir.EAST), "readsentinel", new OpReadSentinel());
    public static HexPattern DETECT_SHROUDED = register(HexPattern.fromAngles("qqqqqwwaawewaawdww", HexDir.SOUTH_EAST), "detectshroud", new OpDetectShrouded());
    //normal spells
    public static HexPattern DELAY_ROD = register(HexPattern.fromAngles("qwqqqwqaqddq", HexDir.SOUTH_EAST), "delayrod", new OpDelayRod());
    public static HexPattern HALT_ROD = register(HexPattern.fromAngles("aqdeeweeew", HexDir.SOUTH_WEST), "haltrod", new OpHaltRod(0));
    public static HexPattern RESET_ROD = register(HexPattern.fromAngles("deaqqwqqqw", HexDir.SOUTH_EAST), "resetrod", new OpHaltRod(1));
    public static HexPattern QUERY_ROD = register(HexPattern.fromAngles("qwqqqwqaeaqa", HexDir.SOUTH_EAST), "queryrod", new OpCheckForRod());
    public static HexPattern WRITE_IDEA = register(HexPattern.fromAngles("eweweweweweeewqaqwe", HexDir.EAST), "writeidea", new OpWriteIdea());
    public static HexPattern GET_SOULPRINT = register(HexPattern.fromAngles("qqaqwedee", HexDir.EAST), "getsoulprint", new OpGetSoulprint());
    public static HexPattern SIGN_ITEM = register(HexPattern.fromAngles("qqaqwedeea", HexDir.EAST), "signitem", new OpSignItem());
    public static HexPattern CHECK_SIGNATURE = register(HexPattern.fromAngles("qqaqwedeed", HexDir.EAST), "checksignature", new OpCompareSignature());
    public static HexPattern CIRCLE = register(HexPattern.fromAngles("wwwwwwqwwwwwwqwwwwwwqwwwwwwqwwwwwwqwwwwww", HexDir.SOUTH_EAST), "circle", new OpCircle());
    //it's supposed to look like a classic game of life glider
    public static HexPattern ADVANCE_AUTOMATON = register(HexPattern.fromAngles("qqwqwqwaqeee", HexDir.SOUTH_WEST), "advanceautomaton", new OpAdvanceAutomaton());
    //public static HexPattern TRIGGER_AUTOMATON = regi(not actually, hexdoc regex, this is commented out)ster(HexPattern.fromAngles("eewewewdeqqq", HexDir.SOUTH_EAST), "triggerautomaton", new OpTriggerAutomaton());

    /*dang you hexdoc
    public static HexPattern CRAFT_ROD = register(HexPattern.fromAngles("eqqqqqawweqqqqqawweqqqqqawwdeqewwwwweqeeeqewwwwweqe", HexDir.EAST), "craftrod", new OpMakePackagedSpell<>((ItemPackagedHex) OneironautThingRegistry.REVERBERATION_ROD.get(), MediaConstants.CRYSTAL_UNIT*/
    //public static HexPattern MUFFLE_WISP = dontdoithexdocilleatyourknees(HexPattern.fromAngles("aaqdwaaqaweewaqawee", HexDir.WEST), "mufflewisp", new OpSetWispVolume());

    //great spells
    public static HexPattern DIM_TELEPORT = registerPerWorld(HexPattern.fromAngles("qeewwwweeqeqeewwwweeqdqqdwwwdqeqdwwwdqdadwwdqdwwddadaqadaawww", HexDir.NORTH_EAST), "dimteleport", new OpDimTeleport());
    public static HexPattern INFUSE_MEDIA = registerPerWorld(HexPattern.fromAngles("wwaqqqqqeqqqwwwqqeqqwwwqqweqadadadaqeqeqadadadaqe", HexDir.EAST), "infusemedia", new OpInfuseMedia());
    public static HexPattern SWAP_SPACE = registerPerWorld(HexPattern.fromAngles("wqqqwwwwwqqqwwwqdaqadwqqwdaqadweqeqqqqeqeqaqeqedeqeqa", HexDir.EAST), "swapspace", new OpSwapSpace());
    public static HexPattern RESIST_DETECTION = registerPerWorld(HexPattern.fromAngles("wawwwdwdwwaqqqqqe", HexDir.EAST), "resistdetection", new OpResistDetection());
    public static HexPattern APPLY_NOT_MISSING = registerPerWorld(HexPattern.fromAngles("qdaeqeawaeqeadqqdeed", HexDir.SOUTH_WEST), "applynotmissing", new OpMarkEntity());
    public static HexPattern APPLY_MIND_RENDER = registerPerWorld(HexPattern.fromAngles("qweqadeqadeqadqqqwdaqedaqedaqeqaqdwawdwawdwaqawdwawdwawddwwwwwqdeddw", HexDir.EAST), "applymindrender", new OpApplyOvercastDamage());
    public static HexPattern REVIVE_FLAYED = registerPerWorld(HexPattern.fromAngles("qeqwqqedeeeeeaqwqeqaqedqde", HexDir.NORTH_EAST), "reviveflayed", new OpReviveFlayed());

    //cell spells
    //public static List<Triple<String[][], Identifier, ICellSpell>> CELL_PATTERNS = new ArrayList<>();

    //public static String[][] CELL_EXPLOSION = registerCellSpell(OpCellExplosion.explosionPattern, "explosion", new OpCellExplosion(OpCellExplosion.explosionPattern, "oneironaut.cellspell.explosion"));
    //public static String[][] CELL_HEAL = registerCellSpell(OpCellHeal.line, "heal", new OpCellHeal(OpCellHeal.line, "oneironaut.cellspell.heal"));
    //public static String[][] CELL_UNIFY = registerCellSpell(OpCellUnify.unifyPattern, "unify", new OpCellUnify(OpCellUnify.unifyPattern, "oneironaut.cellspell.unify"));
    //public static String[][] CELL_COPY_EFFECTS = registerCellSpell(OpCellCopyEffects.copyPattern, "copyeffects", new OpCellCopyEffects(OpCellCopyEffects.copyPattern, "oneironaut.cellspell.copyeffects"));

    public static void init() {
        try {
            for (Triple<HexPattern, Identifier, Action> patternTriple : PATTERNS) {
                PatternRegistry.mapPattern(patternTriple.getFirst(), patternTriple.getSecond(), patternTriple.getThird());
            }
            for (Triple<HexPattern, Identifier, Action> patternTriple : PER_WORLD_PATTERNS) {
                PatternRegistry.mapPattern(patternTriple.getFirst(), patternTriple.getSecond(), patternTriple.getThird(), true);
            }
            /*for (Triple<String[][], Identifier, ICellSpell> cellTriple : CELL_PATTERNS){
                CellSpellManager.registerCellSpell(cellTriple.getFirst(), cellTriple.getSecond(), cellTriple.getThird());
            }*/
        } catch (PatternRegistry.RegisterPatternException e) {
            e.printStackTrace();
        }        registerItemDependentPatterns();

    }
//stolen from gloop
    private static Map<RegistrySupplier<? extends Item>, UncheckedPatternRegister> itemDependentPatternRegisterers = new HashMap<>();

    static {
        itemDependentPatternRegisterers.put(OneironautItemRegistry.REVERBERATION_ROD, () -> {
            PatternRegistry.mapPattern(HexPattern.fromAngles("eqqqqqawweqqqqqawweqqqqqawwdeqewwwwweqeeeqewwwwweqe", HexDir.EAST),
                    new Identifier(Oneironaut.MOD_ID, "craftrod"),
                    new OpMakePackagedSpell<>(OneironautItemRegistry.REVERBERATION_ROD.get(), MediaConstants.CRYSTAL_UNIT * 10));
        });
        /*itemDependentPatternRegisterers.put(OneironautItemRegistry.INSULATED_TRINKET, () -> {
            Pattern!Registry.map!Pattern(HexPattern!from!Angles("wwaqqqqqeaeaqdadqaeqqeaeq", HexDir.EAST),
                    new Identifier(Oneironaut.MOD_ID, "craftinsulatedtrinket"),
                    new OpMakePackagedSpell<>(OneironautItemRegistry.INSULATED_TRINKET.get(), MediaConstants.SHARD_UNIT * 10));
        });*/
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
        Triple<HexPattern, Identifier, Action> triple = new Triple<>(pattern, Oneironaut.id(name), action);
        PATTERNS.add(triple);
        return pattern;
    }

    private static HexPattern registerPerWorld(HexPattern pattern, String name, Action action) {
        Triple<HexPattern, Identifier, Action> triple = new Triple<>(pattern, Oneironaut.id(name), action);
        PER_WORLD_PATTERNS.add(triple);
        return pattern;
    }

    @FunctionalInterface
    public static interface UncheckedPatternRegister{
        public void register() throws PatternRegistry.RegisterPatternException;
    }

    /*private static String[][] registerCellSpell(String[][] pattern, String name, ICellSpell spell){
        Triple<String[][], Identifier, ICellSpell> triple = new Triple<>(pattern, id(name), spell);
        CELL_PATTERNS.add(triple);
        return pattern;
    }*/
}
