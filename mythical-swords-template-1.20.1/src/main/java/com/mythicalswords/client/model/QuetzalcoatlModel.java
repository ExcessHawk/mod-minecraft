package com.mythicalswords.client.model;

import com.mythicalswords.MythicalSwords;
import com.mythicalswords.entity.QuetzalcoatlEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class QuetzalcoatlModel extends GeoModel<QuetzalcoatlEntity> {
    @Override
    public Identifier getModelResource(QuetzalcoatlEntity animatable) {
        return new Identifier(MythicalSwords.MOD_ID, "geo/entity/quetzalcoatl.geo.json");
    }

    @Override
    public Identifier getTextureResource(QuetzalcoatlEntity animatable) {
        return new Identifier(MythicalSwords.MOD_ID, "textures/entity/quetzalcoatl.png");
    }

    @Override
    public Identifier getAnimationResource(QuetzalcoatlEntity animatable) {
        return new Identifier(MythicalSwords.MOD_ID, "animations/entity/quetzalcoatl.animation.json");
    }
}
