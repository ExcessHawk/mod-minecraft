package com.mythicalswords.client.renderer;

import com.mythicalswords.client.model.IzanagiModel;
import com.mythicalswords.entity.IzanagiEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class IzanagiRenderer extends GeoEntityRenderer<IzanagiEntity> {
    public IzanagiRenderer(EntityRendererFactory.Context context) {
        super(context, new IzanagiModel());
    }
}
