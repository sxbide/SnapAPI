package com.github.sxbi.world;

import com.grinderwolf.swm.api.world.SlimeWorld;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Represents a loaded or cloned SlimeWorld instance with metadata.
 */
@Getter
@Setter
@Builder
@Accessors(fluent = true)
public class SnapWorld {

    /**
     * The name of the world instance.
     * <p>
     * This is typically the name used to register or reference the world in the server.
     */
    private String worldName;

    /**
     * Properties associated with the SlimeWorld instance.
     * <p>
     * These include configuration details such as world difficulty, spawn coordinates,
     * PVP settings, and other world-specific flags.
     */
    private SlimeWorld.SlimeProperties slimeProperties;
}
