package com.mythicalswords.client.renderer;

import com.mythicalswords.client.model.AnubisModel;
import com.mythicalswords.entity.AnubisEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

/**
 * GeckoLib renderer for the Anubis boss.
 */
public class AnubisRenderer extends GeoEntityRenderer<AnubisEntity> {

    public AnubisRenderer(EntityRendererFactory.Context context) {
        super(context, new AnubisModel());
    }
}
