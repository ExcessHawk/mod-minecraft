package com.mythicalswords.client.renderer;

import com.mythicalswords.client.model.MinionModel;
import com.mythicalswords.entity.MythicalMinionEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

/** Shared renderer for all mythology minions. */
public class MinionRenderer<T extends MythicalMinionEntity> extends GeoEntityRenderer<T> {
    public MinionRenderer(EntityRendererFactory.Context context) {
        super(context, new MinionModel<>());
    }
}
