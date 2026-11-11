package fun.crashstudio.cscrosshairscale.client;

import com.mojang.blaze3d.platform.InputConstants;
import fun.crashstudio.cscrosshairscale.client.gui.CrosshairScaleConfigScreen;
import fun.crashstudio.cscrosshairscale.config.ModConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

public class CscrosshairscaleKeybinds {
    public static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(Identifier.fromNamespaceAndPath("cscrosshairscale", "cscrosshairscale"));

    public static final KeyMapping OPEN_MENU_KEY = new KeyMapping(
            "key.cscrosshairscale.open_menu",
            InputConstants.Type.KEYBOARD,
            InputConstants.UNKNOWN.getValue(),
            CATEGORY
    );

    public static final KeyMapping INCREASE_KEY = new KeyMapping(
            "key.cscrosshairscale.increase",
            InputConstants.Type.KEYBOARD,
            InputConstants.UNKNOWN.getValue(),
            CATEGORY
    );

    public static final KeyMapping DECREASE_KEY = new KeyMapping(
            "key.cscrosshairscale.decrease",
            InputConstants.Type.KEYBOARD,
            InputConstants.UNKNOWN.getValue(),
            CATEGORY
    );

    public static void register() {
        KeyMappingHelper.registerKeyMapping(OPEN_MENU_KEY);
        KeyMappingHelper.registerKeyMapping(INCREASE_KEY);
        KeyMappingHelper.registerKeyMapping(DECREASE_KEY);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.gui.screen() != null) {
                return;
            }

            while (OPEN_MENU_KEY.consumeClick()) {
                client.gui.setScreen(new CrosshairScaleConfigScreen(null));
            }

            while (INCREASE_KEY.consumeClick()) {
                ModConfig.get().changeScale(ModConfig.SCALE_STEP);
            }

            while (DECREASE_KEY.consumeClick()) {
                ModConfig.get().changeScale(-ModConfig.SCALE_STEP);
            }
        });
    }
}
