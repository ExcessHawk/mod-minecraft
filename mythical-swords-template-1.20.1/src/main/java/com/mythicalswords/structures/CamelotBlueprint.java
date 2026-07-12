package com.mythicalswords.structures;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mythicalswords.MythicalSwords;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Builds the great castle of Camelot from the data-driven blueprint
 * (resources/camelot_blueprint.json). Each district is rendered by a generic
 * primitive (wall, tower, building, flat, hollow, feature) chosen from its type,
 * offset from the structure origin (the keep center at ground level).
 *
 * All loops are CLIPPED to the builder's writable region ({@code writeMin/Max*}),
 * so every chunk only iterates the slice of each district that falls inside it —
 * essential because Camelot is ~500 blocks wide and is built across hundreds of
 * chunks by the worldgen pipeline.
 */
public final class CamelotBlueprint {
    private CamelotBlueprint() {}

    private static final int BLUEPRINT_GROUND_Y = 64;
    private static final int FLAG = Block.NOTIFY_ALL;

    private record District(String type, int x, int y, int z, int w, int h, int l,
                            BlockState primary, BlockState roof, boolean hasWater) {}

    private static List<District> CACHE;

    // ===== Loading =====

    private static synchronized List<District> districts() {
        if (CACHE != null) return CACHE;
        List<District> list = new ArrayList<>();
        try (var is = CamelotBlueprint.class.getResourceAsStream("/camelot_blueprint.json")) {
            if (is == null) {
                MythicalSwords.LOGGER.error("[Camelot] blueprint resource not found");
                CACHE = list;
                return list;
            }
            JsonObject root = JsonParser.parseReader(new InputStreamReader(is)).getAsJsonObject();
            JsonArray arr = root.getAsJsonObject("castle").getAsJsonArray("districts");
            for (var el : arr) {
                JsonObject d = el.getAsJsonObject();
                JsonObject p = d.getAsJsonObject("position");
                JsonObject s = d.getAsJsonObject("size");
                JsonArray mats = d.getAsJsonArray("materials");
                BlockState primary = mats.size() > 0 ? block(mats.get(0).getAsString()) : Blocks.STONE_BRICKS.getDefaultState();
                list.add(new District(
                    d.get("type").getAsString(),
                    p.get("x").getAsInt(), p.get("y").getAsInt(), p.get("z").getAsInt(),
                    Math.max(1, s.get("width").getAsInt()),
                    Math.max(1, s.get("height").getAsInt()),
                    Math.max(1, s.get("length").getAsInt()),
                    primary, pickRoof(mats), containsWater(mats)));
            }
            MythicalSwords.LOGGER.info("[Camelot] loaded " + list.size() + " districts");
        } catch (Exception e) {
            MythicalSwords.LOGGER.error("[Camelot] failed to load blueprint", e);
        }
        CACHE = list;
        return list;
    }

    private static BlockState block(String name) {
        if (name == null || name.isEmpty()) return Blocks.STONE_BRICKS.getDefaultState();
        Identifier id = name.contains(":") ? new Identifier(name) : new Identifier("minecraft", name);
        Block b = Registries.BLOCK.get(id);
        return b == Blocks.AIR ? Blocks.STONE_BRICKS.getDefaultState() : b.getDefaultState();
    }

    private static BlockState pickRoof(JsonArray mats) {
        for (var m : mats) {
            String n = m.getAsString();
            if (n.equals("deepslate_tiles") || n.endsWith("_planks") || n.endsWith("_log")) return block(n);
        }
        return Blocks.DEEPSLATE_TILES.getDefaultState();
    }

    private static boolean containsWater(JsonArray mats) {
        for (var m : mats) if (m.getAsString().equals("water")) return true;
        return false;
    }

    // ===== Build =====

    public static void build(StructureBuilder world, BlockPos origin) {
        int minY = world.getBottomY() + 1;
        int maxY = world.getTopY() - 1;
        for (District d : districts()) {
            int cx = origin.getX() + d.x();
            int cz = origin.getZ() + d.z();
            int baseY = origin.getY() + (d.y() - BLUEPRINT_GROUND_Y);
            int hw = d.w() / 2, hl = d.l() / 2;
            int x0 = cx - hw, x1 = cx + hw;
            int z0 = cz - hl, z1 = cz + hl;
            int y0 = Math.max(minY, baseY);
            int y1 = Math.min(maxY, baseY + d.h() - 1);
            if (y1 <= y0) continue;

            switch (category(d.type())) {
                case WALL    -> buildWall(world, x0, y0, z0, x1, y1, z1, d.primary());
                case TOWER   -> buildTower(world, x0, y0, z0, x1, y1, z1, d.primary());
                case FLAT    -> buildFlat(world, x0, y0, z0, x1, z1, d.primary());
                case HOLLOW  -> buildHollow(world, x0, y0, z0, x1, y1, z1, d.primary(), d.hasWater());
                case FEATURE -> buildFeature(world, x0, y0, z0, x1, y1, z1, d.primary(), d.hasWater());
                case SKIP    -> { /* moat / decorative volume left to natural terrain */ }
                default      -> buildBuilding(world, x0, y0, z0, x1, y1, z1, d.primary(), d.roof());
            }
        }
    }

    private enum Cat { WALL, TOWER, BUILDING, FLAT, HOLLOW, FEATURE, SKIP }

    private static Cat category(String type) {
        return switch (type) {
            case "defensive_wall", "rampart", "battlements" -> Cat.WALL;
            case "corner_tower", "defense_tower", "archer_tower", "watchtower",
                 "tower", "signal_tower" -> Cat.TOWER;
            case "road", "plaza", "market", "garden", "orchard", "training_ground",
                 "maze", "cemetery", "arena" -> Cat.FLAT;
            case "dungeon", "prison", "cellar", "crypt", "tomb", "tunnel_hub",
                 "secret_tunnel", "secret_room", "secret_passage", "emergency_exit",
                 "cistern" -> Cat.HOLLOW;
            case "fountain", "statue", "well", "drawbridge", "portcullis",
                 "siege_engine_decorative", "balcony", "throne" -> Cat.FEATURE;
            case "water_defense" -> Cat.SKIP;
            default -> Cat.BUILDING;
        };
    }

    // ===== Clipped primitives =====

    /** Fill a box, clipped to the builder's writable (chunk) region. */
    private static void fill(StructureBuilder w, int x0, int y0, int z0, int x1, int y1, int z1, BlockState s) {
        int ax0 = Math.max(x0, w.writeMinX()), ax1 = Math.min(x1, w.writeMaxX());
        if (ax0 > ax1) return;
        int ay0 = Math.max(y0, w.writeMinY()), ay1 = Math.min(y1, w.writeMaxY());
        if (ay0 > ay1) return;
        int az0 = Math.max(z0, w.writeMinZ()), az1 = Math.min(z1, w.writeMaxZ());
        if (az0 > az1) return;
        for (int x = ax0; x <= ax1; x++)
            for (int y = ay0; y <= ay1; y++)
                for (int z = az0; z <= az1; z++)
                    w.setBlockState(new BlockPos(x, y, z), s, FLAG);
    }

    private static void clear(StructureBuilder w, int x0, int y0, int z0, int x1, int y1, int z1) {
        fill(w, x0, y0, z0, x1, y1, z1, Blocks.AIR.getDefaultState());
    }

    /** Single block, bounds-checked. */
    private static void put(StructureBuilder w, int x, int y, int z, BlockState s) {
        if (x < w.writeMinX() || x > w.writeMaxX() || y < w.writeMinY() || y > w.writeMaxY()
                || z < w.writeMinZ() || z > w.writeMaxZ()) return;
        w.setBlockState(new BlockPos(x, y, z), s, FLAG);
    }

    private static void buildWall(StructureBuilder w, int x0, int y0, int z0, int x1, int y1, int z1, BlockState s) {
        fill(w, x0, y0, z0, x1, y1 - 1, z1, s);
        // crenellated top row (merlons only)
        int ax0 = Math.max(x0, w.writeMinX()), ax1 = Math.min(x1, w.writeMaxX());
        int az0 = Math.max(z0, w.writeMinZ()), az1 = Math.min(z1, w.writeMaxZ());
        if (y1 >= w.writeMinY() && y1 <= w.writeMaxY())
            for (int x = ax0; x <= ax1; x++)
                for (int z = az0; z <= az1; z++)
                    if (((x + z) & 1) == 0) w.setBlockState(new BlockPos(x, y1, z), s, FLAG);
    }

    private static void buildTower(StructureBuilder w, int x0, int y0, int z0, int x1, int y1, int z1, BlockState s) {
        clear(w, x0, y0, z0, x1, y1, z1);
        fill(w, x0, y0 - 1, z0, x1, y0, z1, s);          // foundation + floor
        fill(w, x0, y0 + 1, z0, x1, y1 - 1, z0, s);      // wall north
        fill(w, x0, y0 + 1, z1, x1, y1 - 1, z1, s);      // wall south
        fill(w, x0, y0 + 1, z0 + 1, x0, y1 - 1, z1 - 1, s); // wall west
        fill(w, x1, y0 + 1, z0 + 1, x1, y1 - 1, z1 - 1, s); // wall east
        crenelRing(w, x0, z0, x1, z1, y1, s);
    }

    private static void crenelRing(StructureBuilder w, int x0, int z0, int x1, int z1, int y, BlockState s) {
        for (int x = x0; x <= x1; x++) {
            if (((x + z0) & 1) == 0) put(w, x, y, z0, s);
            if (((x + z1) & 1) == 0) put(w, x, y, z1, s);
        }
        for (int z = z0; z <= z1; z++) {
            if (((x0 + z) & 1) == 0) put(w, x0, y, z, s);
            if (((x1 + z) & 1) == 0) put(w, x1, y, z, s);
        }
    }

    private static void buildBuilding(StructureBuilder w, int x0, int y0, int z0, int x1, int y1, int z1,
                                      BlockState wall, BlockState roof) {
        clear(w, x0, y0, z0, x1, y1, z1);
        fill(w, x0, y0 - 1, z0, x1, y0, z1, wall);       // foundation + floor
        fill(w, x0, y0 + 1, z0, x1, y1 - 1, z0, wall);   // north
        fill(w, x0, y0 + 1, z1, x1, y1 - 1, z1, wall);   // south
        fill(w, x0, y0 + 1, z0 + 1, x0, y1 - 1, z1 - 1, wall); // west
        fill(w, x1, y0 + 1, z0 + 1, x1, y1 - 1, z1 - 1, wall); // east
        fill(w, x0, y1, z0, x1, y1, z1, roof);           // flat roof
        // south doorway
        int dcx = (x0 + x1) / 2;
        clear(w, dcx - 1, y0 + 1, z1, dcx + 1, Math.min(y1 - 1, y0 + 3), z1);
        // sparse windows
        for (int x = x0 + 2; x <= x1 - 2; x += 4) {
            put(w, x, y0 + 2, z0, Blocks.GLASS_PANE.getDefaultState());
            put(w, x, y0 + 2, z1, Blocks.GLASS_PANE.getDefaultState());
        }
    }

    private static void buildFlat(StructureBuilder w, int x0, int y0, int z0, int x1, int z1, BlockState s) {
        clear(w, x0, y0 + 1, z0, x1, y0 + 4, z1);
        fill(w, x0, y0, z0, x1, y0, z1, s);
    }

    private static void buildHollow(StructureBuilder w, int x0, int y0, int z0, int x1, int y1, int z1,
                                    BlockState s, boolean water) {
        fill(w, x0, y0 - 1, z0, x1, y1, z1, s);          // solid shell block
        if (x1 - x0 >= 2 && z1 - z0 >= 2 && y1 - y0 >= 2) {
            BlockState inner = water ? Blocks.WATER.getDefaultState() : Blocks.AIR.getDefaultState();
            fill(w, x0 + 1, y0, z0 + 1, x1 - 1, y1 - 1, z1 - 1, inner); // hollow interior
        }
    }

    private static void buildFeature(StructureBuilder w, int x0, int y0, int z0, int x1, int y1, int z1,
                                     BlockState s, boolean water) {
        fill(w, x0, y0, z0, x1, y0, z1, s);              // base
        fill(w, x0, y0 + 1, z0, x1, y0 + 1, z0, s);      // ring edges
        fill(w, x0, y0 + 1, z1, x1, y0 + 1, z1, s);
        fill(w, x0, y0 + 1, z0, x0, y0 + 1, z1, s);
        fill(w, x1, y0 + 1, z0, x1, y0 + 1, z1, s);
        int cx = (x0 + x1) / 2, cz = (z0 + z1) / 2;
        if (water) {
            if (x1 - x0 >= 2 && z1 - z0 >= 2)
                fill(w, x0 + 1, y0 + 1, z0 + 1, x1 - 1, y0 + 1, z1 - 1, Blocks.WATER.getDefaultState());
        } else {
            fill(w, cx, y0 + 1, cz, cx, y1, cz, s);      // central column
        }
    }
}
