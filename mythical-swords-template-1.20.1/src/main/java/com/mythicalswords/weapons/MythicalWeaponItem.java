package com.mythicalswords.weapons;

import com.mythicalswords.abilities.CooldownManager;
import com.mythicalswords.abilities.LifeStealAbility;
import com.mythicalswords.abilities.SwiftStrikesAbility;
import com.mythicalswords.abilities.WeaponAbility;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;

/**
 * Base class for all mythical weapons in the mod.
 * Extends SwordItem and adds custom properties like tier, affinity, and
 * mythology.
 * Implements weapon leveling system through NBT data.
 */
public abstract class MythicalWeaponItem extends SwordItem {

    // NBT keys for weapon data
    private static final String NBT_LEVEL = "WeaponLevel";
    private static final String NBT_XP = "WeaponXP";

    // Weapon properties
    private final WeaponTier tier;
    private final ElementalAffinity affinity;
    private final String mythology;
    private final float attackSpeed; // Stored because SwordItem keeps it private
    private WeaponAbility ability; // Optional special ability

    /**
     * Constructor for MythicalWeaponItem
     * 
     * @param material     Tool material for the weapon
     * @param attackDamage Base attack damage
     * @param attackSpeed  Attack speed modifier
     * @param settings     Item settings
     * @param tier         Weapon tier (COMMON, RARE, EPIC, LEGENDARY)
     * @param affinity     Elemental affinity
     * @param mythology    Mythology origin (e.g., "norse", "greek", "arthurian")
     */
    public MythicalWeaponItem(ToolMaterial material, int attackDamage, float attackSpeed,
            Settings settings, WeaponTier tier, ElementalAffinity affinity,
            String mythology) {
        super(material, attackDamage, attackSpeed, settings);
        this.tier = tier;
        this.affinity = affinity;
        this.mythology = mythology;
        this.attackSpeed = attackSpeed;
    }

    // ===== NBT Helper Methods =====

    /**
     * Get the current level of the weapon
     * 
     * @param stack ItemStack to read from
     * @return weapon level (default: 0)
     */
    public int getLevel(ItemStack stack) {
        NbtCompound nbt = stack.getNbt();
        if (nbt != null && nbt.contains(NBT_LEVEL)) {
            return nbt.getInt(NBT_LEVEL);
        }
        return 0;
    }

    /**
     * Set the level of the weapon
     * 
     * @param stack ItemStack to write to
     * @param level New level value
     */
    public void setLevel(ItemStack stack, int level) {
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putInt(NBT_LEVEL, level);
    }

    /**
     * Get the current XP of the weapon
     * 
     * @param stack ItemStack to read from
     * @return weapon XP (default: 0)
     */
    public int getXP(ItemStack stack) {
        NbtCompound nbt = stack.getNbt();
        if (nbt != null && nbt.contains(NBT_XP)) {
            return nbt.getInt(NBT_XP);
        }
        return 0;
    }

    /**
     * Set the XP of the weapon
     * 
     * @param stack ItemStack to write to
     * @param xp    New XP value
     */
    public void setXP(ItemStack stack, int xp) {
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putInt(NBT_XP, xp);
    }

    // ===== Getters =====

    public WeaponTier getTier() {
        return tier;
    }

    public ElementalAffinity getAffinity() {
        return affinity;
    }

    public String getMythology() {
        return mythology;
    }

    // ===== Tooltip =====

    /**
     * Add custom tooltip information to the weapon
     */
    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        // Do NOT call super.appendTooltip to avoid duplicate/messy vanilla tooltips if
        // we want full control
        // But usually we want the enchantments etc. So we keep it.
        super.appendTooltip(stack, world, tooltip, context);

        int level = getLevel(stack);
        int xp = getXP(stack);
        int xpNext = com.mythicalswords.systems.WeaponLevelingSystem.getXpForNextLevel(level);
        float damageBonus = com.mythicalswords.systems.WeaponLevelingSystem.getDamageBonus(this, level);

        // 1. Level Line with Stars (color-coded by tier)
        String stars = getStarsForTier(tier);
        tooltip.add(Text
                .literal("Level " + level + "/" + com.mythicalswords.systems.WeaponLevelingSystem.MAX_LEVEL + " "
                        + stars)
                .formatted(tierColor(tier)));

        // 2. Damage Bonus
        if (damageBonus > 0) {
            tooltip.add(Text.literal("Bonus Damage: +" + String.format("%.1f", damageBonus))
                    .formatted(Formatting.BLUE));
        }

        // 3. Affinity & Mythology
        tooltip.add(Text.literal("Affinity: " + affinity.name()).formatted(affinityColor(affinity)));
        tooltip.add(Text.literal("Mythology: " + mythology).formatted(Formatting.LIGHT_PURPLE));

        // 4. Ability info
        if (ability != null) {
            tooltip.add(Text.literal("Ability: " + ability.getName())
                .formatted(tierColor(tier)));
            int cd = ability.getCooldownTicks();
            tooltip.add(Text.literal("Cooldown: " + (cd / 20) + "s").formatted(Formatting.GRAY));
        }

        // 5. XP Progress Bar
        if (level < com.mythicalswords.systems.WeaponLevelingSystem.MAX_LEVEL) {
            tooltip.add(Text.literal("XP: " + xp + " / " + xpNext).formatted(Formatting.GRAY));
            tooltip.add(Text.literal(getProgressBar(xp, xpNext)).formatted(Formatting.GREEN));
        } else {
            tooltip.add(Text.literal("MAX LEVEL").formatted(Formatting.RED, Formatting.BOLD));
        }
    }

    private Formatting tierColor(WeaponTier tier) {
        return switch (tier) {
            case COMMON    -> Formatting.WHITE;
            case RARE      -> Formatting.BLUE;
            case EPIC      -> Formatting.DARK_PURPLE;
            case LEGENDARY -> Formatting.GOLD;
        };
    }

    private Formatting affinityColor(ElementalAffinity affinity) {
        return switch (affinity) {
            case FIRE      -> Formatting.RED;
            case ICE       -> Formatting.AQUA;
            case LIGHTNING -> Formatting.YELLOW;
            case DIVINE    -> Formatting.WHITE;
            case DARK      -> Formatting.DARK_PURPLE;
            case NATURE    -> Formatting.GREEN;
        };
    }

    private String getStarsForTier(WeaponTier tier) {
        switch (tier) {
            case COMMON:
                return "⭐";
            case RARE:
                return "⭐⭐";
            case EPIC:
                return "⭐⭐⭐";
            case LEGENDARY:
                return "⭐⭐⭐⭐";
            default:
                return "";
        }
    }

    private String getProgressBar(int current, int max) {
        int totalBars = 20;
        float percent = (float) current / max;
        int filledBars = (int) (percent * totalBars);

        StringBuilder bar = new StringBuilder("[");
        for (int i = 0; i < totalBars; i++) {
            if (i < filledBars)
                bar.append("|");
            else
                bar.append(" ");
        }
        bar.append("]");
        return bar.toString();
    }

    // ===== Dynamic Attributes (Damage Bonus) =====

    @Override
    public com.google.common.collect.Multimap<net.minecraft.entity.attribute.EntityAttribute, net.minecraft.entity.attribute.EntityAttributeModifier> getAttributeModifiers(
            ItemStack stack, net.minecraft.entity.EquipmentSlot slot) {
        com.google.common.collect.Multimap<net.minecraft.entity.attribute.EntityAttribute, net.minecraft.entity.attribute.EntityAttributeModifier> modifiers = com.google.common.collect.ArrayListMultimap
                .create();

        if (slot == net.minecraft.entity.EquipmentSlot.MAINHAND) {
            // Base damage from SwordItem logic (material + base)
            // We need to replicate vanilla behavior + our bonus
            // Vanilla SwordItem adds: Attack Damage and Attack Speed

            float baseDamage = this.getAttackDamage(); // This is material damage + 3 (from SwordItem constructor)
            float damageBonus = com.mythicalswords.systems.WeaponLevelingSystem.getDamageBonus(this, getLevel(stack));
            // Forge upgrades scale total damage (AbilityDamageBonus NBT, +15% per upgrade)
            float forgeBonus = com.mythicalswords.systems.MythicalForgeSystem.getAbilityDamageBonus(stack);
            float totalDamage = (baseDamage + damageBonus) * (1.0f + forgeBonus);

            modifiers.put(net.minecraft.entity.attribute.EntityAttributes.GENERIC_ATTACK_DAMAGE,
                    new net.minecraft.entity.attribute.EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID,
                            "Weapon modifier",
                            totalDamage,
                            net.minecraft.entity.attribute.EntityAttributeModifier.Operation.ADDITION));

            modifiers.put(net.minecraft.entity.attribute.EntityAttributes.GENERIC_ATTACK_SPEED,
                    new net.minecraft.entity.attribute.EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID,
                            "Weapon modifier",
                            this.attackSpeed, // Per-weapon speed (stored from constructor)
                            net.minecraft.entity.attribute.EntityAttributeModifier.Operation.ADDITION));
        }

        return modifiers;
    }
    
    // ===== Passive Ability Hooks =====

    /**
     * Wire passive on-hit abilities. Currently powers Khopesh's Life Steal,
     * which heals the attacker for a percentage of damage dealt.
     */
    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (ability instanceof LifeStealAbility lifeSteal
                && attacker instanceof PlayerEntity player
                && !attacker.getWorld().isClient) {
            float damageDealt = this.getAttackDamage()
                    + com.mythicalswords.systems.WeaponLevelingSystem.getDamageBonus(this, getLevel(stack));
            lifeSteal.applyLifeSteal(player, target, damageDealt, attacker.getWorld());
        }
        return super.postHit(stack, target, attacker);
    }

    /**
     * Wire passive while-held abilities. Currently powers Jian's Swift Strikes
     * passive movement speed.
     */
    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (world.isClient || !selected || !(entity instanceof PlayerEntity player)) {
            return;
        }
        // Passive while-held ability (Jian speed)
        if (ability instanceof SwiftStrikesAbility swift) {
            swift.applyPassiveSpeed(player);
        }
    }

    // ===== Ability System =====

    /**
     * Set the special ability for this weapon
     *
     * @param ability The weapon ability
     */
    public void setAbility(WeaponAbility ability) {
        this.ability = ability;
    }
    
    /**
     * Get the special ability for this weapon
     * 
     * @return The weapon ability, or null if none
     */
    public WeaponAbility getAbility() {
        return ability;
    }
    
    /**
     * Handle right-click to activate weapon ability
     */
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        
        // Check if weapon has an ability
        if (ability == null) {
            return TypedActionResult.pass(stack);
        }
        
        // Check if ability can be used
        if (!ability.canUse(world, player, stack)) {
            return TypedActionResult.fail(stack);
        }
        
        // Check cooldown manager
        if (CooldownManager.getInstance().isOnCooldown(player.getUuid(), ability.getName())) {
            if (!world.isClient) {
                int remaining = CooldownManager.getInstance().getRemainingCooldown(player.getUuid(), ability.getName());
                player.sendMessage(
                    Text.literal(ability.getName() + " on cooldown: " + (remaining / 20) + "s")
                        .formatted(Formatting.RED),
                    true
                );
            }
            return TypedActionResult.fail(stack);
        }
        
        // Activate ability
        if (ability.activate(world, player, stack)) {
            // Set cooldown, reduced by forge upgrades (CooldownReduction NBT)
            // and scaled by the global config multiplier
            int baseCd = ability.getCooldownTicks();
            float cdReduction = com.mythicalswords.systems.MythicalForgeSystem.getCooldownReduction(stack);
            float cdMultiplier = com.mythicalswords.config.ModConfig.get().abilityCooldownMultiplier;
            int effectiveCd = Math.max(1, Math.round(baseCd * (1.0f - cdReduction) * cdMultiplier));
            CooldownManager.getInstance().setCooldown(player.getUuid(), ability.getName(), effectiveCd);
            player.getItemCooldownManager().set(this, effectiveCd);
            
            if (!world.isClient) {
                player.sendMessage(
                    Text.literal(ability.getName() + " activated!")
                        .formatted(Formatting.GOLD),
                    true
                );
            }
            
            return TypedActionResult.success(stack, world.isClient());
        }
        
        return TypedActionResult.fail(stack);
    }
}
