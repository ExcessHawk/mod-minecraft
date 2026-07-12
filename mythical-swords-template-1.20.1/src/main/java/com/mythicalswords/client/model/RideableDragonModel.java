package com.mythicalswords.client.model;

import com.mythicalswords.MythicalSwords;
import com.mythicalswords.entity.RideableDragonEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class RideableDragonModel extends GeoModel<RideableDragonEntity> {

    @Override
    public Identifier getModelResource(RideableDragonEntity animatable) {
        return new Identifier(MythicalSwords.MOD_ID, "geo/entity/rideable_dragon.geo.json");
    }

    @Override
    public Identifier getTextureResource(RideableDragonEntity animatable) {
        return new Identifier(MythicalSwords.MOD_ID, "textures/entity/rideable_dragon.png");
    }

    @Override
    public Identifier getAnimationResource(RideableDragonEntity animatable) {
        return new Identifier(MythicalSwords.MOD_ID, "animations/entity/rideable_dragon.animation.json");
    }
}
