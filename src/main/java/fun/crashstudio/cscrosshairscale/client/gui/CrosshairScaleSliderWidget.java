package fun.crashstudio.cscrosshairscale.client.gui;

import fun.crashstudio.cscrosshairscale.config.ModConfig;
import java.util.Locale;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

public class CrosshairScaleSliderWidget extends AbstractSliderButton {

    public CrosshairScaleSliderWidget(int x, int y, int width, int height) {
        super(x, y, width, height, Component.empty(), toSliderValue(ModConfig.get().scale));
        updateMessage();
    }

    private static double toSliderValue(float scale) {
        return Math.clamp((scale - ModConfig.MIN_SCALE) / (ModConfig.MAX_SCALE - ModConfig.MIN_SCALE), 0.0, 1.0);
    }

    private static float toScale(double sliderValue) {
        float raw = ModConfig.MIN_SCALE + (float) sliderValue * (ModConfig.MAX_SCALE - ModConfig.MIN_SCALE);
        float stepped = Math.round(raw / ModConfig.SCALE_STEP) * ModConfig.SCALE_STEP;
        stepped = Math.round(stepped * 100.0f) / 100.0f;
        return Math.clamp(stepped, ModConfig.MIN_SCALE, ModConfig.MAX_SCALE);
    }

    @Override
    protected void updateMessage() {
        float currentScale = toScale(this.value);
        this.setMessage(Component.translatable("cscrosshairscale.config.scale", String.format(Locale.ROOT, "%.2fx", currentScale)));
    }

    @Override
    protected void applyValue() {
        ModConfig.get().scale = toScale(this.value);
    }

    @Override
    public void onRelease(@NonNull MouseButtonEvent click) {
        super.onRelease(click);
        ModConfig.save();
    }

    public void syncFromConfig() {
        this.value = toSliderValue(ModConfig.get().scale);
        updateMessage();
    }
}
