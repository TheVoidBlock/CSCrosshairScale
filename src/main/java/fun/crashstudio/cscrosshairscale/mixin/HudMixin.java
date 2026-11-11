package fun.crashstudio.cscrosshairscale.mixin;

import fun.crashstudio.cscrosshairscale.config.ModConfig;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import org.joml.Matrix3x2fStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Hud.class)
public abstract class HudMixin {

    @Unique
    private boolean cscrosshairscale$scaled = false;

    @Inject(
            method = "extractCrosshair",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;nextStratum()V",
                    shift = At.Shift.AFTER
            )
    )
    private void cscrosshairscale$beforeCrosshair(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (this.cscrosshairscale$scaled) {
            graphics.pose().popMatrix();
            this.cscrosshairscale$scaled = false;
        }

        float scale = ModConfig.get().scale;
        if (Math.abs(scale - 1.0f) > 0.001f) {
            float cx = graphics.guiWidth() / 2.0f;
            float cy = graphics.guiHeight() / 2.0f;
            Matrix3x2fStack matrices = graphics.pose();
            matrices.pushMatrix();
            matrices.translate(cx, cy);
            matrices.scale(scale, scale);
            matrices.translate(-cx, -cy);
            this.cscrosshairscale$scaled = true;
        }
    }

    @Inject(
            method = "extractCrosshair",
            at = @At("RETURN")
    )
    private void cscrosshairscale$afterRenderCrosshair(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (this.cscrosshairscale$scaled) {
            graphics.pose().popMatrix();
            this.cscrosshairscale$scaled = false;
        }
    }
}
