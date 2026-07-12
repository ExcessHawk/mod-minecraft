package com.mythicalswords.client.renderer;

import com.mythicalswords.client.model.AteneaModel;
import com.mythicalswords.entity.AteneaEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class AteneaRenderer extends GeoEntityRenderer<AteneaEntity> {
    public AteneaRenderer(EntityRendererFactory.Context context) {
        super(context, new AteneaModel());
    }
}
