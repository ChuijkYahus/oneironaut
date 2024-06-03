package net.beholderface.oneironaut.fabric;

import at.petrak.hexcasting.fabric.FabricHexInitializer;
import dev.architectury.event.events.common.LifecycleEvent;
import net.fabricmc.api.ModInitializer;
import net.beholderface.oneironaut.Oneironaut;
import net.beholderface.oneironaut.casting.IdeaInscriptionManager;

/**
 * This is your loading entrypoint on fabric(-likes), in case you need to initialize
 * something platform-specific.
 * <br/>
 * Since quilt can load fabric mods, you develop for two platforms in one fell swoop.
 * Feel free to check out the <a href="https://github.com/architectury/architectury-templates">Architectury templates</a>
 * if you want to see how to add quilt-specific code.
 */
public class OneironautFabric implements ModInitializer {
    FabricOneironautConfig config = FabricOneironautConfig.setup();
    @Override
    public void onInitialize() {
        Oneironaut.init();
    }
}
