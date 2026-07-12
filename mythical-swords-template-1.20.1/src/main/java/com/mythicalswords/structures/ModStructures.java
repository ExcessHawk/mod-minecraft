package com.mythicalswords.structures;

import com.mythicalswords.MythicalSwords;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.structure.Structure;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Facade over the worldgen structure system. Structures are now placed by
 * Minecraft's chunk generator (see {@link MythicalStructure} + the JSON under
 * {@code data/mythicalswords/worldgen/}). This class only:
 *   - registers the custom structure type/piece, and
 *   - locates a structure on demand for the compass items.
 */
public class ModStructures {

    // ===== Structure variant ids (match worldgen/structure/<id>.json) =====
    public static final String ARTHURIAN = "arthurian";
    public static final String VALHALLA  = "valhalla";
    public static final String TRICKSTER = "trickster";
    public static final String GREEK     = "greek";
    public static final String BAMBOO    = "bamboo";
    public static final String ONI       = "oni";
    public static final String AZTEC     = "aztec";
    public static final String DESERT    = "desert";
    public static final String CELESTIAL = "celestial";

    // Cache located positions per "<seed>:<variant>" so the compass does not
    // re-run the expensive chunk scan every right-click.
    private static final Map<String, BlockPos> LOCATE_CACHE = new ConcurrentHashMap<>();

    // Search radius in chunks for locateStructure.
    private static final int SEARCH_RADIUS = 160;

    public static void register() {
        MythicalSwords.LOGGER.info("Registering structures for " + MythicalSwords.MOD_ID);
        ModStructureTypes.register();
    }

    // ===== Compass API =====

    /** Nearest position of the given structure variant, or null if none found. */
    public static BlockPos getStructurePos(ServerWorld world, String variant) {
        return getStructurePos(world, BlockPos.ORIGIN, variant);
    }

    /**
     * Nearest position of the given structure variant to {@code center}.
     * Searching from the requesting player's position (instead of the world
     * origin) is essential for structures bound to rarer biomes — jungle,
     * desert, peaks — which may not exist anywhere near (0,0).
     */
    public static BlockPos getStructurePos(ServerWorld world, BlockPos center, String variant) {
        String key = world.getSeed() + ":" + variant;
        BlockPos cached = LOCATE_CACHE.get(key);
        if (cached != null) return cached;

        // Locate by structure tag (data/mythicalswords/tags/worldgen/structure/<variant>.json).
        TagKey<Structure> tag =
            TagKey.of(RegistryKeys.STRUCTURE, new Identifier(MythicalSwords.MOD_ID, variant));
        BlockPos pos = world.locateStructure(tag, center, SEARCH_RADIUS, false);
        if (pos == null) return null;

        LOCATE_CACHE.put(key, pos);
        return pos;
    }

    /** A structure is considered "built" once a position can be located. */
    public static boolean isStructureBuilt(ServerWorld world, String variant) {
        return getStructurePos(world, variant) != null;
    }
}
