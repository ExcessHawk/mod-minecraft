package com.mythicalswords.client.renderer;

import com.mythicalswords.client.model.SusanooModel;
import com.mythicalswords.entity.SusanooEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

/**
 * GeckoLib renderer for the Susanoo boss.
 */
public class SusanooRenderer extends GeoEntityRenderer<SusanooEntity> {

    public SusanooRenderer(EntityRendererFactory.Context context) {
        super(context, new SusanooModel());
    }
}
