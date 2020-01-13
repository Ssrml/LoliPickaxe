package com.anotherstar.client.gui;

import java.io.IOException;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class GUILoliCard extends GuiScreen {

	private final ResourceLocation resource;
	private final int imageWidth;
	private final int imageHeight;
	private final double ratio;

	private int dcx;
	private int dcy;
	private double ds;
	private boolean clicked;
	private int clickX;
	private int clickY;
	private int odcx;
	private int odcy;

	public GUILoliCard(ResourceLocation resource, int imageWidth, int imageHeight) {
		this.resource = resource;
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		this.ratio = (double) imageWidth / (double) imageHeight;
		this.dcx = 0;
		this.dcy = 0;
		this.ds = 1;
		this.clicked = false;
		this.clickX = 0;
		this.clickY = 0;
	}

	@Override
	public void initGui() {
		addButton(new GuiButton(0, (this.width - 200) / 2, this.height - 20, 200, 20, I18n.format("gui.back")));
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.enabled) {
			if (button.id == 0) {
				mc.displayGuiScreen(null);
			}
		}
	}

	public void handleMouseInput() throws IOException {
		int d = Mouse.getEventDWheel();
		if (d == 0) {
			super.handleMouseInput();
		} else {
			int x = Mouse.getEventX() * width / mc.displayWidth;
			int y = height - Mouse.getEventY() * height / mc.displayHeight - 1;
			if (ds > 0.1 || d > 0) {
				ds *= d > 0 ? 1.28 : 0.78125;
				dcx += d > 0 ? (width / 2 + dcx - x) * 0.28 : (x - width / 2 - dcx) * 0.21875;
				dcy += d > 0 ? (height / 2 + dcy - y) * 0.28 : (y - height / 2 - dcy) * 0.21875;
			}
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		clicked = true;
		clickX = mouseX;
		clickY = mouseY;
		odcx = dcx;
		odcy = dcy;
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);
		clicked = false;
	}

	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
		if (clicked) {
			dcx = odcx + mouseX - clickX;
			dcy = odcy + mouseY - clickY;
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		mc.getTextureManager().bindTexture(resource);
		int cx = width / 2 + dcx;
		int cy = height / 2 + dcy;
		double proportion;
		if (ratio < (double) width / (double) height) {
			proportion = (double) height / (double) imageHeight;
		} else {
			proportion = (double) width / (double) imageWidth;
		}
		int x = (int) (imageWidth * proportion / 2 * ds);
		int y = (int) (imageHeight * proportion / 2 * ds);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos(cx - x, cy + y, zLevel).tex(0, 1).endVertex();
		bufferbuilder.pos(cx + x, cy + y, zLevel).tex(1, 1).endVertex();
		bufferbuilder.pos(cx + x, cy - y, zLevel).tex(1, 0).endVertex();
		bufferbuilder.pos(cx - x, cy - y, zLevel).tex(0, 0).endVertex();
		tessellator.draw();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

}
