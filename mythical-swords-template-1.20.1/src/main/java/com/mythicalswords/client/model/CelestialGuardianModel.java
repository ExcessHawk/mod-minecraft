package com.mythicalswords.client.model;

import com.mythicalswords.MythicalSwords;
import com.mythicalswords.entity.CelestialGuardianEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class CelestialGuardianModel extends GeoModel<CelestialGuardianEntity> {
    @Override
    public Identifier getModelResource(CelestialGuardianEntity animatable) {
        return new Identifier(MythicalSwords.MOD_ID, "geo/entity/celestial_guardian.geo.json");
    }

    @Override
    public Identifier getTextureResource(CelestialGuardianEntity animatable) {
        return new Identifier(MythicalSwords.MOD_ID, "textures/entity/celestial_guardian.png");
    }

    @Override
    public Identifier getAnimationResource(CelestialGuardianEntity animatable) {
        return new Identifier(MythicalSwords.MOD_ID, "animations/entity/celestial_guardian.animation.json");
    }
}
