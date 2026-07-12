package com.mythicalswords.client.renderer;

import com.mythicalswords.client.model.SunWukongModel;
import com.mythicalswords.entity.SunWukongEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SunWukongRenderer extends GeoEntityRenderer<SunWukongEntity> {
    public SunWukongRenderer(EntityRendererFactory.Context context) {
        super(context, new SunWukongModel());
    }
}
