package com.mythicalswords.client.model;

import com.mythicalswords.MythicalSwords;
import com.mythicalswords.entity.OniOscuroEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class OniOscuroModel extends GeoModel<OniOscuroEntity> {
    @Override
    public Identifier getModelResource(OniOscuroEntity animatable) {
        return new Identifier(MythicalSwords.MOD_ID, "geo/entity/oni_oscuro.geo.json");
    }

    @Override
    public Identifier getTextureResource(OniOscuroEntity animatable) {
        return new Identifier(MythicalSwords.MOD_ID, "textures/entity/oni_oscuro.png");
    }

    @Override
    public Identifier getAnimationResource(OniOscuroEntity animatable) {
        return new Identifier(MythicalSwords.MOD_ID, "animations/entity/oni_oscuro.animation.json");
    }
}
