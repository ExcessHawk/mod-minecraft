package com.mythicalswords.compat.rei;

import com.mythicalswords.core.ModBlocks;
import com.mythicalswords.systems.MythicalForgeSystem;
import com.mythicalswords.weapons.MythicalWeaponItem;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;

import java.util.List;

/**
 * REI plugin (loaded only when REI is installed — compile-only dependency).
 * Shows the Mythical Forge operations: repair, ability upgrade and rune
 * engraving, generated from the same tables the forge itself uses.
 */
public class MythicalSwordsREIPlugin implements REIClientPlugin {

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new MythicalForgeCategory());
        registry.addWorkstations(MythicalForgeCategory.ID, EntryStacks.of(ModBlocks.MYTHICAL_FORGE));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        List<MythicalWeaponItem> weapons = Registries.ITEM.stream()
                .filter(item -> item instanceof MythicalWeaponItem)
                .map(item -> (MythicalWeaponItem) item)
                .toList();

        // Per-weapon repair and upgrade recipes
        for (MythicalWeaponItem weapon : weapons) {
            Item repairMaterial = MythicalForgeSystem.getRepairMaterial(weapon.getMythology());
            if (repairMaterial == null) continue;

            registry.add(MythicalForgeDisplay.of(weapon, List.of(repairMaterial), "repair"));

            Item catalyst = MythicalForgeSystem.getTierCatalyst(weapon.getTier());
            if (catalyst != null) {
                registry.add(MythicalForgeDisplay.of(weapon, List.of(repairMaterial, catalyst), "upgrade"));
            }
        }

        // Rune engravings: any mythical weapon + rune material
        EntryIngredient anyWeapon = EntryIngredients.ofItemStacks(
                weapons.stream().map(ItemStack::new).toList());
        MythicalForgeSystem.getRuneEnchants().forEach((material, enchantment) ->
                registry.add(new MythicalForgeDisplay(
                        List.of(anyWeapon, EntryIngredients.of(material)),
                        anyWeapon,
                        Text.translatable("rei.mythicalswords.op.rune", enchantment.getName(1)))));
    }
}
