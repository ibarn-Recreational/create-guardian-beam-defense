package com.ibarnstormer.gbd.client.keybinds;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {

    public static final String KEYS_CATEGORY = "key.category.creategbd.main";
    public static final String KEY_BEAM_REACTOR_TOGGLE = "key.creategbd.beam_reactor_toggle";

    public static final KeyMapping BEAM_REACTOR_TOGGLE = new KeyMapping(KEY_BEAM_REACTOR_TOGGLE, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_R, KEYS_CATEGORY);

}
