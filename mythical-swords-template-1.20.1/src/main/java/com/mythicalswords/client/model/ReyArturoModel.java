package com.mythicalswords.client.model;

import com.mythicalswords.MythicalSwords;
import com.mythicalswords.entity.ReyArturoEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

/**
 * GeckoLib model bindings for the Rey Arturo boss.
 */
public class ReyArturoModel extends GeoModel<ReyArturoEntity> {

    @Override
    public Identifier getModelResource(ReyArturoEntity animatable) {
        return new Identifier(MythicalSwords.MOD_ID, "geo/entity/rey_arturo.geo.json");
    }

    @Override
    public Identifier getTextureResource(ReyArturoEntity animatable) {
        return new Identifier(MythicalSwords.MOD_ID, "textures/entity/rey_arturo.png");
    }

    @Override
    public Identifier getAnimationResource(ReyArturoEntity animatable) {
        return new Identifier(MythicalSwords.MOD_ID, "animations/entity/rey_arturo.animation.json");
    }
}
