package com.mythicalswords.client.model;

import com.mythicalswords.MythicalSwords;
import com.mythicalswords.entity.IzanagiEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class IzanagiModel extends GeoModel<IzanagiEntity> {
    @Override
    public Identifier getModelResource(IzanagiEntity animatable) {
        return new Identifier(MythicalSwords.MOD_ID, "geo/entity/izanagi.geo.json");
    }

    @Override
    public Identifier getTextureResource(IzanagiEntity animatable) {
        return new Identifier(MythicalSwords.MOD_ID, "textures/entity/izanagi.png");
    }

    @Override
    public Identifier getAnimationResource(IzanagiEntity animatable) {
        return new Identifier(MythicalSwords.MOD_ID, "animations/entity/izanagi.animation.json");
    }
}
