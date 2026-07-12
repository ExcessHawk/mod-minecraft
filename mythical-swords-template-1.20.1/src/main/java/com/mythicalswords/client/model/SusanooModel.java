package com.mythicalswords.client.model;

import com.mythicalswords.MythicalSwords;
import com.mythicalswords.entity.SusanooEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

/**
 * GeckoLib model bindings for the Susanoo boss.
 */
public class SusanooModel extends GeoModel<SusanooEntity> {

    @Override
    public Identifier getModelResource(SusanooEntity animatable) {
        return new Identifier(MythicalSwords.MOD_ID, "geo/entity/susanoo.geo.json");
    }

    @Override
    public Identifier getTextureResource(SusanooEntity animatable) {
        return new Identifier(MythicalSwords.MOD_ID, "textures/entity/susanoo.png");
    }

    @Override
    public Identifier getAnimationResource(SusanooEntity animatable) {
        return new Identifier(MythicalSwords.MOD_ID, "animations/entity/susanoo.animation.json");
    }
}
