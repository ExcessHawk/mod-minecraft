package com.mythicalswords.client.model;

import com.mythicalswords.MythicalSwords;
import com.mythicalswords.entity.AnubisEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

/**
 * GeckoLib model bindings for the Anubis boss.
 */
public class AnubisModel extends GeoModel<AnubisEntity> {

    @Override
    public Identifier getModelResource(AnubisEntity animatable) {
        return new Identifier(MythicalSwords.MOD_ID, "geo/entity/anubis.geo.json");
    }

    @Override
    public Identifier getTextureResource(AnubisEntity animatable) {
        return new Identifier(MythicalSwords.MOD_ID, "textures/entity/anubis.png");
    }

    @Override
    public Identifier getAnimationResource(AnubisEntity animatable) {
        return new Identifier(MythicalSwords.MOD_ID, "animations/entity/anubis.animation.json");
    }
}
