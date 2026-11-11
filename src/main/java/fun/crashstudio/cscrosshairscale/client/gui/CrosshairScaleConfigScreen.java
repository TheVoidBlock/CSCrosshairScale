package fun.crashstudio.cscrosshairscale.client.gui;

import fun.crashstudio.cscrosshairscale.config.ModConfig;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.joml.Matrix3x2fStack;
import org.jspecify.annotations.NonNull;

public class CrosshairScaleConfigScreen extends Screen {
    private static final Identifier CROSSHAIR_TEXTURE = Identifier.withDefaultNamespace("hud/crosshair");

    private final Screen parent;
    private CrosshairScaleSliderWidget slider;

    public CrosshairScaleConfigScreen(Screen parent) {
        super(Component.translatable("cscrosshairscale.screen.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int previewBoxHeight = 76;
        int previewBoxY = 32;
        int startY = previewBoxY + previewBoxHeight + 14;
        int buttonWidth = 200;
        int buttonHeight = 20;
        int buttonX = this.width / 2 - buttonWidth / 2;

        this.slider = new CrosshairScaleSliderWidget(buttonX, startY, buttonWidth, buttonHeight);
        this.addRenderableWidget(this.slider);

        Button resetButton = Button.builder(
                Component.translatable("cscrosshairscale.config.reset"),
                _ -> {
                    ModConfig.get().reset();
                    this.slider.syncFromConfig();
                }
        ).bounds(buttonX, startY + 24, buttonWidth, buttonHeight).build();
        this.addRenderableWidget(resetButton);

        int doneY = Math.max(startY + 50, this.height - 28);
        Button doneButton = Button.builder(
                CommonComponents.GUI_DONE,
                _ -> this.onClose()
        ).bounds(buttonX, doneY, buttonWidth, buttonHeight).build();
        this.addRenderableWidget(doneButton);
    }

    @Override
    public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        super.extractRenderState(graphics, mouseX, mouseY, delta);

        graphics.centeredText(this.font, this.title, this.width / 2, 14, 0xFFFFFF);

        int previewBoxWidth = 90;
        int previewBoxHeight = 76;
        int previewBoxX = this.width / 2 - previewBoxWidth / 2;
        int previewBoxY = 32;

        graphics.fill(previewBoxX, previewBoxY, previewBoxX + previewBoxWidth, previewBoxY + previewBoxHeight, 0x80000000);
        graphics.outline(previewBoxX, previewBoxY, previewBoxWidth, previewBoxHeight, 0xFF555555);

        graphics.centeredText(this.font, Component.translatable("cscrosshairscale.config.preview"), this.width / 2, previewBoxY - 11, 0xAAAAAA);

        int previewCenterX = this.width / 2;
        int previewCenterY = previewBoxY + previewBoxHeight / 2;

        float scale = ModConfig.get().scale;
        Matrix3x2fStack matrices = graphics.pose();

        graphics.enableScissor(previewBoxX + 1, previewBoxY + 1, previewBoxX + previewBoxWidth - 1, previewBoxY + previewBoxHeight - 1);
        matrices.pushMatrix();
        matrices.translate(previewCenterX, previewCenterY);
        matrices.scale(scale, scale);
        matrices.translate(-previewCenterX, -previewCenterY);

        graphics.blitSprite(RenderPipelines.CROSSHAIR, CROSSHAIR_TEXTURE, previewCenterX - 7, previewCenterY - 7, 15, 15);
        matrices.popMatrix();
        graphics.disableScissor();
    }

    @Override
    public void onClose() {
        ModConfig.save();
        this.minecraft.gui.setScreen(this.parent);
    }
}
