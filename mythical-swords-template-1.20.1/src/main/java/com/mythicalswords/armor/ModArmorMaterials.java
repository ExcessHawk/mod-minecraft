package com.mythicalswords.armor;

import com.mythicalswords.MythicalSwords;
import com.mythicalswords.core.ModItems;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

import java.util.function.Supplier;

/**
 * Custom armor materials for the new minerals. One per affinity.
 */
public enum ModArmorMaterials implements ArmorMaterial {
    ORICHALCUM("orichalcum", 37, new int[]{3, 6, 8, 3}, 12, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE, 3.0f, 0.1f,
            () -> Ingredient.ofItems(ModItems.ORICHALCUM_INGOT)),
    URU("uru", 35, new int[]{3, 6, 7, 3}, 14, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE, 2.5f, 0.1f,
            () -> Ingredient.ofItems(ModItems.URU_INGOT)),
    VOIDSTEEL("voidsteel", 40, new int[]{3, 6, 8, 3}, 15, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE, 3.5f, 0.15f,
            () -> Ingredient.ofItems(ModItems.VOIDSTEEL_INGOT)),
    FROSTSTEEL("froststeel", 28, new int[]{2, 5, 6, 2}, 12, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 1.5f, 0.0f,
            () -> Ingredient.ofItems(ModItems.FROSTSTEEL_INGOT)),
    // Endgame tier forged from multi-boss materials (above netherite)
    CELESTIAL("celestial", 42, new int[]{4, 7, 9, 4}, 20, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE, 4.0f, 0.15f,
            () -> Ingredient.ofItems(ModItems.CELESTIAL_INGOT));

    // HELMET, CHESTPLATE, LEGGINGS, BOOTS (by ArmorItem.Type ordinal)
    private static final int[] BASE_DURABILITY = {11, 16, 15, 13};

    private final String name;
    private final int durabilityMultiplier;
    private final int[] protectionAmounts;
    private final int enchantability;
    private final SoundEvent equipSound;
    private final float toughness;
    private final float knockbackResistance;
    private final Supplier<Ingredient> repairIngredient;

    ModArmorMaterials(String name, int durabilityMultiplier, int[] protectionAmounts, int enchantability,
            SoundEvent equipSound, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredient) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.protectionAmounts = protectionAmounts;
        this.enchantability = enchantability;
        this.equipSound = equipSound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredient = repairIngredient;
    }

    @Override
    public int getDurability(ArmorItem.Type type) {
        return BASE_DURABILITY[type.ordinal()] * this.durabilityMultiplier;
    }

    @Override
    public int getProtection(ArmorItem.Type type) {
        return this.protectionAmounts[type.ordinal()];
    }

    @Override
    public int getEnchantability() {
        return this.enchantability;
    }

    @Override
    public SoundEvent getEquipSound() {
        return this.equipSound;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    @Override
    public String getName() {
        return MythicalSwords.MOD_ID + ":" + this.name;
    }

    @Override
    public float getToughness() {
        return this.toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }
}
