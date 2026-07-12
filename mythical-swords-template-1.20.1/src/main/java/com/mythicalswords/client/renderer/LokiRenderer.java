package com.mythicalswords.client.renderer;

import com.mythicalswords.client.model.LokiModel;
import com.mythicalswords.entity.LokiEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class LokiRenderer extends GeoEntityRenderer<LokiEntity> {
    public LokiRenderer(EntityRendererFactory.Context context) {
        super(context, new LokiModel());
    }
}
