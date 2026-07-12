package com.mythicalswords.client.model;

import com.mythicalswords.MythicalSwords;
import com.mythicalswords.entity.LegendaryBlacksmithEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class LegendaryBlacksmithModel extends GeoModel<LegendaryBlacksmithEntity> {
    @Override
    public Identifier getModelResource(LegendaryBlacksmithEntity animatable) {
        return new Identifier(MythicalSwords.MOD_ID, "geo/entity/legendary_blacksmith.geo.json");
    }

    @Override
    public Identifier getTextureResource(LegendaryBlacksmithEntity animatable) {
        return new Identifier(MythicalSwords.MOD_ID, "textures/entity/legendary_blacksmith.png");
    }

    @Override
    public Identifier getAnimationResource(LegendaryBlacksmithEntity animatable) {
        return new Identifier(MythicalSwords.MOD_ID, "animations/entity/legendary_blacksmith.animation.json");
    }
}
