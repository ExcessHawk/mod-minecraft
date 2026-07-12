package com.mythicalswords.client.renderer;

import com.mythicalswords.client.model.QuetzalcoatlModel;
import com.mythicalswords.entity.QuetzalcoatlEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class QuetzalcoatlRenderer extends GeoEntityRenderer<QuetzalcoatlEntity> {
    public QuetzalcoatlRenderer(EntityRendererFactory.Context context) {
        super(context, new QuetzalcoatlModel());
        this.shadowRadius = 1.0f;
    }
}
