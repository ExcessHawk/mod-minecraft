package com.mythicalswords.client.renderer;

import com.mythicalswords.client.model.RaModel;
import com.mythicalswords.entity.RaEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

/**
 * GeckoLib renderer for the Ra boss.
 */
public class RaRenderer extends GeoEntityRenderer<RaEntity> {

    public RaRenderer(EntityRendererFactory.Context context) {
        super(context, new RaModel());
    }
}
