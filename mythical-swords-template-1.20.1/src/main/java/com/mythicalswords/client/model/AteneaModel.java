package com.mythicalswords.client.model;

import com.mythicalswords.MythicalSwords;
import com.mythicalswords.entity.AteneaEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class AteneaModel extends GeoModel<AteneaEntity> {
    @Override
    public Identifier getModelResource(AteneaEntity animatable) {
        return new Identifier(MythicalSwords.MOD_ID, "geo/entity/atenea.geo.json");
    }

    @Override
    public Identifier getTextureResource(AteneaEntity animatable) {
        return new Identifier(MythicalSwords.MOD_ID, "textures/entity/atenea.png");
    }

    @Override
    public Identifier getAnimationResource(AteneaEntity animatable) {
        return new Identifier(MythicalSwords.MOD_ID, "animations/entity/atenea.animation.json");
    }
}
