package com.mythicalswords.client.model;

import com.mythicalswords.MythicalSwords;
import com.mythicalswords.entity.OdinEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class OdinModel extends GeoModel<OdinEntity> {
    @Override
    public Identifier getModelResource(OdinEntity animatable) {
        return new Identifier(MythicalSwords.MOD_ID, "geo/entity/odin.geo.json");
    }

    @Override
    public Identifier getTextureResource(OdinEntity animatable) {
        return new Identifier(MythicalSwords.MOD_ID, "textures/entity/odin.png");
    }

    @Override
    public Identifier getAnimationResource(OdinEntity animatable) {
        return new Identifier(MythicalSwords.MOD_ID, "animations/entity/odin.animation.json");
    }
}
