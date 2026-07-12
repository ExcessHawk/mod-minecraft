package com.mythicalswords.client.model;

import com.mythicalswords.MythicalSwords;
import com.mythicalswords.entity.RaEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

/**
 * GeckoLib model bindings for the Ra boss.
 */
public class RaModel extends GeoModel<RaEntity> {

    @Override
    public Identifier getModelResource(RaEntity animatable) {
        return new Identifier(MythicalSwords.MOD_ID, "geo/entity/ra.geo.json");
    }

    @Override
    public Identifier getTextureResource(RaEntity animatable) {
        return new Identifier(MythicalSwords.MOD_ID, "textures/entity/ra.png");
    }

    @Override
    public Identifier getAnimationResource(RaEntity animatable) {
        return new Identifier(MythicalSwords.MOD_ID, "animations/entity/ra.animation.json");
    }
}
