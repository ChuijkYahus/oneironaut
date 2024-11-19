package net.beholderface.oneironaut.registry;

import at.petrak.hexcasting.common.lib.HexBlocks;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.beholderface.oneironaut.block.*;
import net.beholderface.oneironaut.block.blockentity.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.registry.Registry;
import net.beholderface.oneironaut.Oneironaut;

import java.util.function.ToIntFunction;

public class OneironautBlockRegistry {
    //public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Oneironaut.MOD_ID, Registry.ITEM_KEY);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Oneironaut.MOD_ID, Registry.BLOCK_KEY);
    //public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Oneironaut.MOD_ID, Registry.FLUID_KEY);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Oneironaut.MOD_ID, Registry.BLOCK_ENTITY_TYPE_KEY);
    /*public static final DeferredRegister<StatusEffect> EFFECTS = DeferredRegister.create(Oneironaut.MOD_ID, Registry.MOB_EFFECT_KEY);
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(Oneironaut.MOD_ID, Registry.ENCHANTMENT_KEY);*/

    //I will not scream at my computer over this

    public static void init() {
        BLOCKS.register();
        BLOCK_ENTITIES.register();
    }
    public static final RegistrySupplier<Block> PSUEDOAMETHYST_BLOCK = BLOCKS.register("pseudoamethyst_block", () -> new Block(AbstractBlock.Settings.of(Material.AMETHYST)
            .hardness(1.5f)
            .sounds(BlockSoundGroup.AMETHYST_BLOCK)
            .resistance(5)
            .luminance(state -> 7)
            ));
    public static final RegistrySupplier<Block> NOOSPHERE_BASALT = BLOCKS.register("noosphere_basalt", () -> new Block(AbstractBlock.Settings.of(Material.STONE)
            .hardness(1f)
            .sounds(BlockSoundGroup.BASALT)
            .resistance(4)
    ));
    public static final RegistrySupplier<NoosphereGateway> NOOSPHERE_GATE = BLOCKS.register("noosphere_gate", () -> new NoosphereGateway(AbstractBlock.Settings.of(Material.PORTAL).luminance(state -> 15).noCollision().hardness(-1)));
    public static final RegistrySupplier<BlockEntityType<NoosphereGateEntity>> NOOSPHERE_GATE_ENTITY = BLOCK_ENTITIES.register("noosphere_gate_entity", () -> BlockEntityType.Builder.create(NoosphereGateEntity::new, NOOSPHERE_GATE.get()).build(null));
    public static final RegistrySupplier<WispLantern> WISP_LANTERN = BLOCKS.register("wisp_lantern", () -> new WispLantern(AbstractBlock.Settings.of(Material.GLASS).luminance(state -> 15).sounds(BlockSoundGroup.GLASS)));
    public static final RegistrySupplier<WispLanternTinted> WISP_LANTERN_TINTED = BLOCKS.register("wisp_lantern_tinted", () -> new WispLanternTinted(AbstractBlock.Settings.of(Material.GLASS).sounds(BlockSoundGroup.GLASS)));
    public static final RegistrySupplier<BlockEntityType<WispLanternEntity>> WISP_LANTERN_ENTITY = BLOCK_ENTITIES.register("wisp_lantern_entity", () -> BlockEntityType.Builder.create(WispLanternEntity::new, WISP_LANTERN.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<WispLanternEntityTinted>> WISP_LANTERN_ENTITY_TINTED = BLOCK_ENTITIES.register("wisp_lantern_entity_tinted", () -> BlockEntityType.Builder.create(WispLanternEntityTinted::new, WISP_LANTERN_TINTED.get()).build(null));
    public static final RegistrySupplier<ThoughtSlurryBlock> THOUGHT_SLURRY_BLOCK = BLOCKS.register("thought_slurry", () -> ThoughtSlurryBlock.INSTANCE /*new ThoughtSlurryBlock(ThoughtSlurry.STILL_FLUID, AbstractBlock.Settings.copy(Blocks.LAVA))*/);
    public static final RegistrySupplier<SuperBuddingBlock> SUPER_BUDDING = BLOCKS.register("super_budding", () -> new SuperBuddingBlock(AbstractBlock.Settings.of(Material.AMETHYST)));
    public static final RegistrySupplier<SentinelTrapImpetus> SENTINEL_TRAP = BLOCKS.register("sentinel_trap", () -> new SentinelTrapImpetus(AbstractBlock.Settings.of(HexBlocks.SLATE_BLOCK.getDefaultState().getMaterial()).hardness(2f)));
    public static final RegistrySupplier<BlockEntityType<SentinelTrapImpetusEntity>> SENTINEL_TRAP_ENTITY = BLOCK_ENTITIES.register("sentinel_trap_entity", () -> BlockEntityType.Builder.create(SentinelTrapImpetusEntity::new, SENTINEL_TRAP.get()).build(null));
    public static final RegistrySupplier<SentinelSensor> SENTINEL_SENSOR = BLOCKS.register("sentinel_sensor", () -> new SentinelSensor(AbstractBlock.Settings.of(HexBlocks.SLATE_BLOCK.getDefaultState().getMaterial())));
    public static final RegistrySupplier<BlockEntityType<SentinelSensorEntity>> SENTINEL_SENSOR_ENTITY = BLOCK_ENTITIES.register("sentinel_sensor_entity", () -> BlockEntityType.Builder.create(SentinelSensorEntity::new, SENTINEL_SENSOR.get()).build(null));
    public static final RegistrySupplier<Block> RAYCAST_BLOCKER = BLOCKS.register("raycast_blocker", () -> new Block(AbstractBlock.Settings.copy(Blocks.DEEPSLATE)));
    public static final RegistrySupplier<Block> RAYCAST_BLOCKER_GLASS = BLOCKS.register("raycast_blocker_glass", () -> new RaycastBlockerGlass(AbstractBlock.Settings.copy(Blocks.TINTED_GLASS)));
    public static final RegistrySupplier<Block> HEX_RESISTANT_BLOCK = BLOCKS.register("hex_resistant_block", () -> new Block(AbstractBlock.Settings.copy(Blocks.OBSIDIAN).hardness(1.5f)));
    public static final RegistrySupplier<Block> CIRCLE = BLOCKS.register("circle", () -> new CircleBlock(AbstractBlock.Settings.copy(Blocks.BLACK_CONCRETE)
            .nonOpaque().breakInstantly()));
    public static final RegistrySupplier<Block> MEDIA_ICE = BLOCKS.register("media_ice", ()-> new Block(AbstractBlock.Settings.copy(Blocks.PACKED_ICE)
            .slipperiness(1.1f).mapColor(MapColor.PALE_PURPLE)
    ));
    //produced by frost walker on thought slurry
    public static final RegistrySupplier<Block> MEDIA_ICE_FROSTED = BLOCKS.register("media_ice_frosted", ()-> new FrostedMediaIceBlock(AbstractBlock.Settings.copy(Blocks.PACKED_ICE)
            .slipperiness(1.08f).mapColor(MapColor.PALE_PURPLE).ticksRandomly().strength(0.5f).sounds(BlockSoundGroup.GLASS)
    ));
    public static final RegistrySupplier<MediaGelBlock> MEDIA_GEL = BLOCKS.register("media_gel", ()-> new MediaGelBlock(AbstractBlock.Settings.copy(Blocks.SLIME_BLOCK)
            .velocityMultiplier(0.05f).jumpVelocityMultiplier(0.25f).mapColor(MapColor.PALE_PURPLE).sounds(BlockSoundGroup.SLIME).nonOpaque().hardness(Blocks.SOUL_SAND.getHardness())
    ));
    //will eventually do something related to cellular automata, and be related to the media gel
    public static final RegistrySupplier<CellBlock> CELL = BLOCKS.register("cell", ()-> new CellBlock(AbstractBlock.Settings.copy(Blocks.SLIME_BLOCK)
            .velocityMultiplier(0.6f).jumpVelocityMultiplier(0.75f).mapColor(MapColor.PALE_PURPLE).sounds(BlockSoundGroup.SLIME).nonOpaque().hardness(Blocks.SOUL_SAND.getHardness())
    ));
    public static final RegistrySupplier<BlockEntityType<CellEntity>> CELL_ENTITY = BLOCK_ENTITIES.register("cell_entity", () -> BlockEntityType.Builder.create(CellEntity::new, CELL.get()).build(null));

    public static final RegistrySupplier<WispBattery> WISP_BATTERY = BLOCKS.register("wisp_battery", ()-> new WispBattery(AbstractBlock.Settings.copy(HexBlocks.SLATE_BLOCK).luminance(createLightLevelFromBoolBlockState(WispBattery.REDSTONE_POWERED, 15))));
    public static final RegistrySupplier<BlockEntityType<WispBatteryEntity>> WISP_BATTERY_ENTITY = BLOCK_ENTITIES.register("wisp_battery_entity", ()-> BlockEntityType.Builder.create(WispBatteryEntity::new, WISP_BATTERY.get()).build(null));
    public static final RegistrySupplier<WispBatteryFake> WISP_BATTERY_DECORATIVE = BLOCKS.register("decorative_wisp_battery", ()-> new WispBatteryFake(AbstractBlock.Settings.copy(HexBlocks.SLATE_BLOCK).luminance(createLightLevelFromBoolBlockState(WispBatteryFake.REDSTONE_POWERED, 15))));
    public static final RegistrySupplier<BlockEntityType<WispBatteryEntityFake>> WISP_BATTERY_ENTITY_DECORATIVE = BLOCK_ENTITIES.register("decorative_wisp_battery_entity", ()-> BlockEntityType.Builder.create(WispBatteryEntityFake::new, WISP_BATTERY_DECORATIVE.get()).build(null));

    public static RegistrySupplier<EdifiedTreeSpawnerBlock> EDIFIED_TREE_SPAWNER = BLOCKS.register("edified_tree_spawner", ()-> new EdifiedTreeSpawnerBlock(AbstractBlock.Settings.of(Material.AIR)));
    public static RegistrySupplier<BlockEntityType<EdifiedTreeSpawnerBlockEntity>> EDIFIED_TREE_SPAWNER_ENTITY = BLOCK_ENTITIES.register("edified_tree_spawner_entity", ()->BlockEntityType.Builder.create(EdifiedTreeSpawnerBlockEntity::new, EDIFIED_TREE_SPAWNER.get()).build(null));

    public static RegistrySupplier<HoverElevatorBlock> HOVER_ELEVATOR = BLOCKS.register("hover_elevator", ()-> new HoverElevatorBlock(AbstractBlock.Settings.copy(HexBlocks.SLATE_BLOCK).luminance(createLightLevelFromBoolBlockState(HoverElevatorBlock.POWERED, 15))));
    public static RegistrySupplier<BlockEntityType<HoverElevatorBlockEntity>> HOVER_ELEVATOR_ENTITY = BLOCK_ENTITIES.register("hover_elevator_entity", ()->BlockEntityType.Builder.create(HoverElevatorBlockEntity::new, HOVER_ELEVATOR.get()).build(null));
    public static RegistrySupplier<Block> HOVER_REPEATER = BLOCKS.register("hover_repeater", ()->new HoverRepeaterBlock(AbstractBlock.Settings.copy(HexBlocks.SLATE_BLOCK).noCollision().breakInstantly().nonOpaque()));

    public static RegistrySupplier<AmethystClusterBlock> PSEUDOAMETHYST_CLUSTER = BLOCKS.register("pseudoamethyst_cluster", ()-> new AmethystClusterBlock(7, 3, AbstractBlock.Settings.copy(Blocks.AMETHYST_CLUSTER)));
    public static RegistrySupplier<AmethystClusterBlock> PSEUDOAMETHYST_BUD_LARGE = BLOCKS.register("pseudoamethyst_bud_large", ()-> new AmethystClusterBlock(5, 3, AbstractBlock.Settings.copy(Blocks.LARGE_AMETHYST_BUD)));
    public static RegistrySupplier<AmethystClusterBlock> PSEUDOAMETHYST_BUD_MEDIUM = BLOCKS.register("pseudoamethyst_bud_medium", ()-> new AmethystClusterBlock(4, 3, AbstractBlock.Settings.copy(Blocks.MEDIUM_AMETHYST_BUD)));
    public static RegistrySupplier<AmethystClusterBlock> PSEUDOAMETHYST_BUD_SMALL = BLOCKS.register("pseudoamethyst_bud_small", ()-> new AmethystClusterBlock(3, 4, AbstractBlock.Settings.copy(Blocks.SMALL_AMETHYST_BUD)));

    //mostly just stolen from the vanilla class since it's private in there
    protected static ToIntFunction<BlockState> createLightLevelFromBoolBlockState(BooleanProperty property, int litLevel) {
        return state -> state.get(property) ? litLevel : 0;
    }


    //used for the eternal chorus mixin
    public static final BooleanProperty ETERNAL = BooleanProperty.of("eternal");
}
