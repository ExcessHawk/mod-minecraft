package com.mythicalswords.client.model;

import com.mythicalswords.MythicalSwords;
import com.mythicalswords.entity.LokiEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class LokiModel extends GeoModel<LokiEntity> {
    @Override
    public Identifier getModelResource(LokiEntity animatable) {
        return new Identifier(MythicalSwords.MOD_ID, "geo/entity/loki.geo.json");
    }

    @Override
    public Identifier getTextureResource(LokiEntity animatable) {
        return new Identifier(MythicalSwords.MOD_ID, "textures/entity/loki.png");
    }

    @Override
    public Identifier getAnimationResource(LokiEntity animatable) {
        return new Identifier(MythicalSwords.MOD_ID, "animations/entity/loki.animation.json");
    }
}
