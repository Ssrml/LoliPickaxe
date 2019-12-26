package com.anotherstar.client.render;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.anotherstar.common.config.ConfigLoader;
import com.anotherstar.common.entity.EntityLoli;
import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.layers.LayerMaidArrow;
import com.github.tartaricacid.touhoulittlemaid.proxy.ClientProxy;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.Optional.Interface;

@Optional.InterfaceList(value = {
		@Interface(iface = "com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid", modid = TouhouLittleMaid.MOD_ID),
		@Interface(iface = "com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.layers.LayerMaidArrow", modid = TouhouLittleMaid.MOD_ID),
		@Interface(iface = "com.github.tartaricacid.touhoulittlemaid.proxy.ClientProxy", modid = TouhouLittleMaid.MOD_ID) })
public class RenderRemiliaLoli extends RenderLiving<EntityLoli> {

	public static final Factory FACTORY = new Factory();
	private static final String DEFAULT_MODEL_ID = "touhou_little_maid:hakurei_reimu";
	private static final ResourceLocation DEFAULT_MODEL_TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID,
			"textures/entity/hakurei_reimu.png");
	private ResourceLocation modelRes;

	public RenderRemiliaLoli(RenderManager renderManager, ModelBase modelBase, float shadowSize) {
		super(renderManager, modelBase, shadowSize);
		this.modelRes = DEFAULT_MODEL_TEXTURE;
		this.addLayer(new LayerMaidArrow(this));
		this.addLayer(new LayerMaidHeldItem(this));
	}

	@Override
	public void doRender(@Nonnull EntityLoli entity, double x, double y, double z, float entityYaw,
			float partialTicks) {
		mainModel = ClientProxy.MAID_MODEL.getModel(DEFAULT_MODEL_ID).orElseThrow(NullPointerException::new);
		ClientProxy.MAID_MODEL.getModel(ConfigLoader.loliModelId).ifPresent(model -> this.mainModel = model);
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}

	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(@Nonnull EntityLoli entity) {
		this.modelRes = DEFAULT_MODEL_TEXTURE;
		ClientProxy.MAID_MODEL.getInfo(ConfigLoader.loliModelId)
				.ifPresent(modelItem -> modelRes = modelItem.getTexture());
		return modelRes;
	}

	public static class Factory implements IRenderFactory<EntityLoli> {
		@Override
		public Render<? super EntityLoli> createRenderFor(RenderManager manager) {
			return new RenderRemiliaLoli(manager,
					ClientProxy.MAID_MODEL.getModel(DEFAULT_MODEL_ID).orElseThrow(NullPointerException::new), 0.5f);
		}
	}

}
