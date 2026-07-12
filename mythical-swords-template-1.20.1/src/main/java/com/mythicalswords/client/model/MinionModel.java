package com.mythicalswords.client.model;

import com.mythicalswords.MythicalSwords;
import com.mythicalswords.entity.MythicalMinionEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

/** Shared model for all mythology minions: one geo/anim set, per-minion texture. */
public class MinionModel<T extends MythicalMinionEntity> extends GeoModel<T> {

    @Override
    public Identifier getModelResource(T animatable) {
        return new Identifier(MythicalSwords.MOD_ID, "geo/entity/minion.geo.json");
    }

    @Override
    public Identifier getTextureResource(T animatable) {
        return new Identifier(MythicalSwords.MOD_ID,
                "textures/entity/minion_" + animatable.getMinionId() + ".png");
    }

    @Override
    public Identifier getAnimationResource(T animatable) {
        return new Identifier(MythicalSwords.MOD_ID, "animations/entity/minion.animation.json");
    }
}
