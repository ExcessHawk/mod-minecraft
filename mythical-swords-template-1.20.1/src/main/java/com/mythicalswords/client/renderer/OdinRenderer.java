package com.mythicalswords.client.renderer;

import com.mythicalswords.client.model.OdinModel;
import com.mythicalswords.entity.OdinEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class OdinRenderer extends GeoEntityRenderer<OdinEntity> {
    public OdinRenderer(EntityRendererFactory.Context context) {
        super(context, new OdinModel());
    }
}
