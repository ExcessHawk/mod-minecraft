package com.mythicalswords.client.renderer;

import com.mythicalswords.client.model.CelestialGuardianModel;
import com.mythicalswords.entity.CelestialGuardianEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CelestialGuardianRenderer extends GeoEntityRenderer<CelestialGuardianEntity> {
    public CelestialGuardianRenderer(EntityRendererFactory.Context context) {
        super(context, new CelestialGuardianModel());
        this.shadowRadius = 1.2f;
    }
}
