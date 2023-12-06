package net.oneironaut.registry;

import at.petrak.hexcasting.common.lib.HexItems;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.*;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;
import net.minecraft.sound.BlockSoundGroup;
import net.oneironaut.Oneironaut;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

public class OneironautThingRegistry {
    // Register items through this
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Oneironaut.MOD_ID, Registry.ITEM_KEY);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Oneironaut.MOD_ID, Registry.BLOCK_KEY);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Oneironaut.MOD_ID, Registry.FLUID_KEY);
    public static void init() {
        FLUIDS.register();
        BLOCKS.register();
        ITEMS.register();
    }


    //public static final RegistrySupplier<Block> STUPID_BLOCK = BLOCKS.register("stupid_block", () -> new Block(AbstractBlock.Settings.of(Material.AMETHYST)));
    //public static final RegistrySupplier<Block> SMART_BLOCK = BLOCKS.register("smart_block", () -> new Block(AbstractBlock.Settings.of(Material.AMETHYST)));
    public static final RegistrySupplier<Block> PSUEDOAMETHYST_BLOCK = BLOCKS.register("pseudoamethyst_block", () -> new Block(AbstractBlock.Settings.of(Material.AMETHYST)
            .hardness(1.5f)
            .sounds(BlockSoundGroup.AMETHYST_BLOCK)
            .resistance(5)
            .luminance(state -> 7)
            ));
    public static final RegistrySupplier<Item> PSUEDOAMETHYST_BLOCK_ITEM = ITEMS.register("pseudoamethyst_block", () -> new BlockItem(PSUEDOAMETHYST_BLOCK.get(), HexItems.props()));
    public static final RegistrySupplier<Item> PSUEDOAMETHYST_SHARD = ITEMS.register("pseudoamethyst_shard", () -> new Item(HexItems.props()));


    //why architectury documentation no worky

    /*public static final Supplier<? extends FlowableFluid> THOUGHT_SLURRY = FLUIDS.register("thought_slurry", () ->new ArchitecturyFlowingFluid.Source(OneironautItemRegistry.THOUGHT_SLURRY_ATTRIBUTES));
    public static final RegistrySupplier<FlowableFluid> THOUGHT_SLURRY_FLOWING = FLUIDS.register("thought_slurry_flowing", () -> new ArchitecturyFlowingFluid.Flowing(OneironautItemRegistry.THOUGHT_SLURRY_ATTRIBUTES));
    //why level no exist
    public static final RegistrySupplier<FluidBlock> THOUGHT_SLURRY_BLOCK = BLOCKS.register("thought_slurry", () -> new ArchitecturyLiquidBlock(THOUGHT_SLURRY, AbstractBlock.Settings.copy(Blocks.LAVA)));
    public static final RegistrySupplier<Item> THOUGHT_SLURRY_BUCKET = ITEMS.register("thought_slurry_bucket", () -> new ArchitecturyBucketItem(THOUGHT_SLURRY, HexItems.unstackable()));
    public static final ArchitecturyFluidAttributes THOUGHT_SLURRY_ATTRIBUTES =
            SimpleArchitecturyFluidAttributes.ofSupplier(() -> OneironautItemRegistry.THOUGHT_SLURRY, () -> OneironautItemRegistry.THOUGHT_SLURRY_FLOWING)
            .blockSupplier(() -> OneironautItemRegistry.THOUGHT_SLURRY_BLOCK)
            .bucketItemSupplier(() -> OneironautItemRegistry.THOUGHT_SLURRY_BUCKET);*/


    // A new creative tab. Notice how it is one of the few things that are not deferred
    //public static final ItemGroup DUMMY_GROUP = CreativeTabRegistry.create(id("dummy_group"), () -> new ItemStack(OneironautItemRegistry.DUMMY_ITEM.get()));

    // During the loading phase, refrain from accessing suppliers' items (e.g. EXAMPLE_ITEM.get()), they will not be available
    //public static final RegistrySupplier<Item> DUMMY_ITEM = ITEMS.register("dummy_item", () -> new Item(new Item.Settings().group(DUMMY_GROUP)));


}