package com.mythicalswords.client.renderer;

import com.mythicalswords.client.model.OniOscuroModel;
import com.mythicalswords.entity.OniOscuroEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class OniOscuroRenderer extends GeoEntityRenderer<OniOscuroEntity> {
    public OniOscuroRenderer(EntityRendererFactory.Context context) {
        super(context, new OniOscuroModel());
    }
}
