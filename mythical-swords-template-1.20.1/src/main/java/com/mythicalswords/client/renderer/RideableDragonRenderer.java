package com.mythicalswords.client.renderer;

import com.mythicalswords.client.model.RideableDragonModel;
import com.mythicalswords.entity.RideableDragonEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RideableDragonRenderer extends GeoEntityRenderer<RideableDragonEntity> {

    public RideableDragonRenderer(EntityRendererFactory.Context context) {
        super(context, new RideableDragonModel());
        this.shadowRadius = 1.2f;
    }
}
