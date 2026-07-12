package com.mythicalswords.client.renderer;

import com.mythicalswords.client.model.ReyArturoModel;
import com.mythicalswords.entity.ReyArturoEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

/**
 * GeckoLib renderer for the Rey Arturo boss.
 */
public class ReyArturoRenderer extends GeoEntityRenderer<ReyArturoEntity> {

    public ReyArturoRenderer(EntityRendererFactory.Context context) {
        super(context, new ReyArturoModel());
    }
}
