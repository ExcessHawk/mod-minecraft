package com.mythicalswords.systems;

import com.mythicalswords.blocks.MythicalForgeBlockEntity;
import com.mythicalswords.core.ModEnchantments;
import com.mythicalswords.core.ModItems;
import com.mythicalswords.core.ModSounds;
import com.mythicalswords.weapons.MythicalWeaponItem;
import com.mythicalswords.weapons.WeaponTier;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MythicalForgeSystem {

    public static final int MAX_UPGRADES = 5;

    // Mythology -> material required to repair weapons of that origin
    private static final Map<String, Item> REPAIR_MATERIALS = new HashMap<>();
    // Tier -> catalyst required to upgrade weapons of that tier
    private static final Map<WeaponTier, Item> TIER_CATALYSTS = new HashMap<>();
    // Rune material -> forge-exclusive enchantment it applies (one level per rune)
    private static final Map<Item, Enchantment> RUNE_ENCHANTS = new HashMap<>();

    static {
        REPAIR_MATERIALS.put("arthurian", ModItems.MYTHRIL_INGOT);
        REPAIR_MATERIALS.put("norse", ModItems.NORTHSTEEL_INGOT);
        REPAIR_MATERIALS.put("greek", ModItems.SACRED_IRON_INGOT);
        REPAIR_MATERIALS.put("japanese", ModItems.TAMAHAGANE_INGOT);
        REPAIR_MATERIALS.put("chinese", ModItems.JADE_IMPERIAL_INGOT);
        REPAIR_MATERIALS.put("mesoamerican", ModItems.OBSIDIANA_RITUAL_SHARD);
        REPAIR_MATERIALS.put("egyptian", ModItems.BRONCE_BENDITO);
        REPAIR_MATERIALS.put("atlantean", ModItems.ORICHALCUM_INGOT);
        REPAIR_MATERIALS.put("frost", ModItems.FROSTSTEEL_INGOT);
        REPAIR_MATERIALS.put("void", ModItems.VOIDSTEEL_INGOT);

        TIER_CATALYSTS.put(WeaponTier.COMMON, ModItems.SOUL_FRAGMENT);
        TIER_CATALYSTS.put(WeaponTier.RARE, ModItems.DUST_OF_LONGEVITY);
        TIER_CATALYSTS.put(WeaponTier.EPIC, ModItems.ESSENCE_OF_RIGHTEOUSNESS);
        TIER_CATALYSTS.put(WeaponTier.LEGENDARY, ModItems.SHARD_OF_DIVINITY);

        RUNE_ENCHANTS.put(ModItems.FROZEN_SOUL_CRYSTAL, ModEnchantments.ODIN_RUNE);
        RUNE_ENCHANTS.put(ModItems.GEM_OF_BISHAMON, ModEnchantments.IAIJUTSU);
        RUNE_ENCHANTS.put(ModItems.FEATHER_OF_VICTORY, ModEnchantments.AEGIS_WARD);
        RUNE_ENCHANTS.put(ModItems.MOONSTONE_SHARD, ModEnchantments.ANUBIS_CURSE);
        RUNE_ENCHANTS.put(ModItems.SUN_BLESSED_ALLOY, ModEnchantments.SOLAR_WRATH);
        RUNE_ENCHANTS.put(ModItems.FILO_DE_PLUMA_DE_QUETZAL, ModEnchantments.BLOOD_THIRST);
        RUNE_ENCHANTS.put(ModItems.BAMBOO_REINFORCED_SHAFT, ModEnchantments.MONKEY_STEP);
    }

    /**
     * Process a forge operation: repair, ability upgrade, or enchantment.
     * Slot 0 = weapon, Slot 1 = material1, Slot 2 = material2, Slot 3 = output.
     *
     * Repair:  weapon + its mythology material (1 consumed, +25% durability)
     * Upgrade: weapon + mythology material + tier catalyst (1 of each consumed)
     * Enchant: weapon + enchanted book (anvil rules: no duplicates/conflicts)
     */
    public static void process(Inventory inventory, PlayerEntity player) {
        ItemStack weaponStack = inventory.getStack(0);
        ItemStack material1 = inventory.getStack(1);
        ItemStack material2 = inventory.getStack(2);

        if (!(weaponStack.getItem() instanceof MythicalWeaponItem weapon)) return;

        // Never overwrite a pending result
        if (!inventory.getStack(3).isEmpty()) {
            fail(player, "message.mythicalswords.forge.output_full");
            return;
        }

        boolean book1 = material1.getItem() instanceof EnchantedBookItem;
        boolean book2 = material2.getItem() instanceof EnchantedBookItem;

        if ((book1 && material2.isEmpty()) || (book2 && material1.isEmpty())) {
            doEnchant(inventory, weaponStack, book1 ? 1 : 2, player);
        } else if (!material1.isEmpty() && !material2.isEmpty()) {
            doAbilityUpgrade(inventory, weaponStack, weapon, player);
        } else if (!material1.isEmpty() || !material2.isEmpty()) {
            int matSlot = material1.isEmpty() ? 2 : 1;
            if (RUNE_ENCHANTS.containsKey(inventory.getStack(matSlot).getItem())) {
                doRuneEnchant(inventory, weaponStack, matSlot, player);
            } else {
                doRepair(inventory, weaponStack, weapon, matSlot, player);
            }
        }
    }

    // ===== Repair =====

    private static void doRepair(Inventory inventory, ItemStack weaponStack,
                                  MythicalWeaponItem weapon, int matSlot, PlayerEntity player) {
        Item required = REPAIR_MATERIALS.get(weapon.getMythology());
        ItemStack material = inventory.getStack(matSlot);

        if (required == null || material.getItem() != required) {
            fail(player, "message.mythicalswords.forge.repair_wrong_material",
                    required == null ? Text.literal("?") : required.getName());
            return;
        }
        if (!weaponStack.isDamaged()) {
            fail(player, "message.mythicalswords.forge.not_damaged");
            return;
        }

        ItemStack repaired = weaponStack.copy();
        // Restore 25% durability per material
        int repairAmount = repaired.getMaxDamage() / 4;
        repaired.setDamage(Math.max(0, repaired.getDamage() - repairAmount));

        inventory.setStack(3, repaired);
        inventory.setStack(0, ItemStack.EMPTY);
        consumeOne(inventory, matSlot);

        forgeEffects(inventory, player);
        player.sendMessage(Text.translatable("message.mythicalswords.forge.repaired")
                .formatted(Formatting.GREEN), true);
    }

    // ===== Ability upgrade =====

    private static void doAbilityUpgrade(Inventory inventory, ItemStack weaponStack,
                                          MythicalWeaponItem weapon, PlayerEntity player) {
        Item ingot = REPAIR_MATERIALS.get(weapon.getMythology());
        Item catalyst = TIER_CATALYSTS.get(weapon.getTier());

        ItemStack mat1 = inventory.getStack(1);
        ItemStack mat2 = inventory.getStack(2);

        // Accept the pair in either order
        boolean valid = (mat1.getItem() == ingot && mat2.getItem() == catalyst)
                || (mat1.getItem() == catalyst && mat2.getItem() == ingot);
        if (ingot == null || catalyst == null || !valid) {
            fail(player, "message.mythicalswords.forge.upgrade_wrong_materials",
                    ingot == null ? Text.literal("?") : ingot.getName(),
                    catalyst == null ? Text.literal("?") : catalyst.getName());
            return;
        }

        ItemStack upgraded = weaponStack.copy();
        var nbt = upgraded.getOrCreateNbt();

        var config = com.mythicalswords.config.ModConfig.get();
        int maxUpgrades = config.forgeMaxUpgrades;
        int upgrades = nbt.getInt("ForgeUpgrades");
        if (upgrades >= maxUpgrades) {
            fail(player, "message.mythicalswords.forge.max_upgrades");
            return;
        }

        // Store upgrade modifier: per-upgrade bonuses come from the config
        nbt.putInt("ForgeUpgrades", upgrades + 1);
        nbt.putFloat("CooldownReduction", (upgrades + 1) * config.forgeCooldownReductionPerUpgrade);
        nbt.putFloat("AbilityDamageBonus", (upgrades + 1) * config.forgeDamageBonusPerUpgrade);

        inventory.setStack(3, upgraded);
        inventory.setStack(0, ItemStack.EMPTY);
        consumeOne(inventory, 1);
        consumeOne(inventory, 2);

        forgeEffects(inventory, player);
        player.sendMessage(Text.translatable("message.mythicalswords.forge.upgraded",
                upgrades + 1, maxUpgrades).formatted(Formatting.GOLD), true);
    }

    // ===== Rune enchant (forge-exclusive mythology enchantments) =====

    private static void doRuneEnchant(Inventory inventory, ItemStack weaponStack,
                                       int matSlot, PlayerEntity player) {
        Enchantment enchantment = RUNE_ENCHANTS.get(inventory.getStack(matSlot).getItem());
        Map<Enchantment, Integer> current =
                new LinkedHashMap<>(EnchantmentHelper.get(weaponStack));

        int existing = current.getOrDefault(enchantment, 0);
        if (existing >= enchantment.getMaxLevel()) {
            fail(player, "message.mythicalswords.forge.rune_max");
            return;
        }

        current.put(enchantment, existing + 1);
        ItemStack enchanted = weaponStack.copy();
        EnchantmentHelper.set(current, enchanted);

        inventory.setStack(3, enchanted);
        inventory.setStack(0, ItemStack.EMPTY);
        consumeOne(inventory, matSlot);

        forgeEffects(inventory, player);
        player.sendMessage(Text.translatable("message.mythicalswords.forge.rune_applied",
                enchantment.getName(existing + 1)).formatted(Formatting.LIGHT_PURPLE), true);
    }

    // ===== Enchant =====

    private static void doEnchant(Inventory inventory, ItemStack weaponStack,
                                   int bookSlot, PlayerEntity player) {
        ItemStack book = inventory.getStack(bookSlot);
        Map<Enchantment, Integer> bookEnchants =
                EnchantmentHelper.fromNbt(EnchantedBookItem.getEnchantmentNbt(book));
        Map<Enchantment, Integer> current =
                new LinkedHashMap<>(EnchantmentHelper.get(weaponStack));

        boolean changed = false;
        for (var entry : bookEnchants.entrySet()) {
            Enchantment enchantment = entry.getKey();
            int bookLevel = entry.getValue();

            if (!enchantment.isAcceptableItem(weaponStack)) continue;

            // Reject anything that conflicts with a different enchant already present
            boolean conflicts = current.keySet().stream()
                    .anyMatch(e -> e != enchantment && !enchantment.canCombine(e));
            if (conflicts) continue;

            int existing = current.getOrDefault(enchantment, 0);
            // Anvil rules: equal levels combine to +1 (capped), otherwise keep the higher
            int result = existing == bookLevel
                    ? Math.min(existing + 1, enchantment.getMaxLevel())
                    : Math.max(existing, bookLevel);
            if (result > existing) {
                current.put(enchantment, result);
                changed = true;
            }
        }

        if (!changed) {
            fail(player, "message.mythicalswords.forge.enchant_incompatible");
            return;
        }

        ItemStack enchanted = weaponStack.copy();
        EnchantmentHelper.set(current, enchanted);

        inventory.setStack(3, enchanted);
        inventory.setStack(0, ItemStack.EMPTY);
        consumeOne(inventory, bookSlot);

        forgeEffects(inventory, player);
        player.sendMessage(Text.translatable("message.mythicalswords.forge.enchanted")
                .formatted(Formatting.LIGHT_PURPLE), true);
    }

    // ===== Helpers =====

    private static void consumeOne(Inventory inventory, int slot) {
        ItemStack stack = inventory.getStack(slot);
        stack.decrement(1);
        inventory.setStack(slot, stack.isEmpty() ? ItemStack.EMPTY : stack);
    }

    private static void fail(PlayerEntity player, String key, Object... args) {
        player.sendMessage(Text.translatable(key, args).formatted(Formatting.RED), true);
    }

    private static void playForgeSound(PlayerEntity player) {
        player.getWorld().playSound(null,
            player.getX(), player.getY(), player.getZ(),
            ModSounds.FORGE_OPERATE, SoundCategory.BLOCKS, 1.0f, 1.0f);
    }

    /** Sound + spark burst at the forge block (falls back to the player's position). */
    private static void forgeEffects(Inventory inventory, PlayerEntity player) {
        playForgeSound(player);
        if (!(player.getWorld() instanceof ServerWorld world)) return;
        double x = player.getX(), y = player.getY() + 1.0, z = player.getZ();
        if (inventory instanceof MythicalForgeBlockEntity forge) {
            x = forge.getPos().getX() + 0.5;
            y = forge.getPos().getY() + 1.1;
            z = forge.getPos().getZ() + 0.5;
        }
        world.spawnParticles(ParticleTypes.LAVA, x, y, z, 6, 0.25, 0.1, 0.25, 0.0);
        world.spawnParticles(ParticleTypes.CRIT, x, y, z, 14, 0.3, 0.3, 0.3, 0.15);
        world.spawnParticles(ParticleTypes.FLAME, x, y, z, 8, 0.2, 0.2, 0.2, 0.02);
    }

    /**
     * Get cooldown reduction multiplier from NBT (0.0 = none, 0.5 = 50% reduction)
     */
    public static float getCooldownReduction(ItemStack stack) {
        var nbt = stack.getNbt();
        if (nbt == null) return 0f;
        return nbt.getFloat("CooldownReduction");
    }

    /**
     * Get ability damage bonus multiplier from NBT
     */
    public static float getAbilityDamageBonus(ItemStack stack) {
        var nbt = stack.getNbt();
        if (nbt == null) return 0f;
        return nbt.getFloat("AbilityDamageBonus");
    }

    /** Material required to repair a weapon of the given mythology, or null. */
    public static Item getRepairMaterial(String mythology) {
        return REPAIR_MATERIALS.get(mythology);
    }

    /** Catalyst required to upgrade a weapon of the given tier. */
    public static Item getTierCatalyst(WeaponTier tier) {
        return TIER_CATALYSTS.get(tier);
    }

    /** Rune material -> forge-exclusive enchantment (read-only, for recipe viewers). */
    public static Map<Item, Enchantment> getRuneEnchants() {
        return java.util.Collections.unmodifiableMap(RUNE_ENCHANTS);
    }
}
