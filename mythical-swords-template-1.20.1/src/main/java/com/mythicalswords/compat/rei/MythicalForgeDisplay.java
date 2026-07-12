package com.mythicalswords.compat.rei;

import com.mythicalswords.weapons.MythicalWeaponItem;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.item.Item;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * One Mythical Forge operation shown in REI: inputs (weapon + materials) and
 * the resulting weapon, with a label describing the operation.
 */
public class MythicalForgeDisplay extends BasicDisplay {

    private final Text operation;

    public MythicalForgeDisplay(List<EntryIngredient> inputs, EntryIngredient output, Text operation) {
        super(inputs, List.of(output));
        this.operation = operation;
    }

    public Text getOperation() {
        return operation;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return MythicalForgeCategory.ID;
    }

    /** Display for a specific weapon: weapon + materials -> same weapon. */
    public static MythicalForgeDisplay of(MythicalWeaponItem weapon, List<Item> materials, String opKey) {
        List<EntryIngredient> inputs = new ArrayList<>();
        inputs.add(EntryIngredients.of(weapon));
        for (Item material : materials) {
            inputs.add(EntryIngredients.of(material));
        }
        return new MythicalForgeDisplay(inputs, EntryIngredients.of(weapon),
                Text.translatable("rei.mythicalswords.op." + opKey));
    }
}
