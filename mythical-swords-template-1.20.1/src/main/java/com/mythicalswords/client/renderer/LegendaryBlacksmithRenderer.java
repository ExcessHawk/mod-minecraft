package com.mythicalswords.client.renderer;

import com.mythicalswords.client.model.LegendaryBlacksmithModel;
import com.mythicalswords.entity.LegendaryBlacksmithEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class LegendaryBlacksmithRenderer extends GeoEntityRenderer<LegendaryBlacksmithEntity> {
    public LegendaryBlacksmithRenderer(EntityRendererFactory.Context context) {
        super(context, new LegendaryBlacksmithModel());
    }
}
