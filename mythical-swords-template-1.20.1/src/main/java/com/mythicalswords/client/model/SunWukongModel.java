package com.mythicalswords.client.model;

import com.mythicalswords.MythicalSwords;
import com.mythicalswords.entity.SunWukongEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class SunWukongModel extends GeoModel<SunWukongEntity> {
    @Override
    public Identifier getModelResource(SunWukongEntity animatable) {
        return new Identifier(MythicalSwords.MOD_ID, "geo/entity/sun_wukong.geo.json");
    }

    @Override
    public Identifier getTextureResource(SunWukongEntity animatable) {
        return new Identifier(MythicalSwords.MOD_ID, "textures/entity/sun_wukong.png");
    }

    @Override
    public Identifier getAnimationResource(SunWukongEntity animatable) {
        return new Identifier(MythicalSwords.MOD_ID, "animations/entity/sun_wukong.animation.json");
    }
}
