/*
 * Copyright 2021 Siphalor
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied.
 * See the License for the specific language governing
 * permissions and limitations under the License.
 */

package de.siphalor.nmuk.api;

import de.siphalor.nmuk.impl.NMUKKeyBinding;
import de.siphalor.nmuk.impl.NMUKKeyBindingHelper;
import de.siphalor.nmuk.impl.mixin.KeyBindingAccessor;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Main API class of NMUK (No More Useless Keys).<br />
 * The methods defined in this class allow you to define alternatives to existing keybindings.
 */
public class NMUKAlternatives {
	/**
	 * Create an alternative keybinding with the given code and {@link InputUtil.Type#KEYSYM}.
	 *
	 * @param base The base keybinding to create an alternative for
	 * @param code The keycode to use as default for the alternative
	 */
	public static void create(KeyBinding base, int code) {
		create(base, InputUtil.Type.KEYSYM, code);
	}

	/**
	 * Create an alternative keybinding with the given code and input type.
	 *
	 * @param base      The base keybinding to create an alternative for
	 * @param inputType The {@link InputUtil.Type} that defines the type of the code
	 * @param code      The input code
	 */
	public static void create(KeyBinding base, InputUtil.Type inputType, int code) {
		KeyBinding alternative = NMUKKeyBindingHelper.createAlternativeKeyBinding(base, inputType, code);
		NMUKKeyBindingHelper.registerKeyBinding(alternative);
		NMUKKeyBindingHelper.defaultAlternatives.put(base, alternative);
	}

	/**
	 * Register and add the latter keybinding to the former.<br />
	 * This is useful when using more complex keybinding trigger, e.g. in use with Amces.<br />
	 * The translation key and the category of the alternative keybinding will be rewritten
	 * and as such it must not be registered yet.
	 *
	 * @param base        The base keybinding to create an alternative for
	 * @param alternative The alternative keybinding. This keybinding MUST NOT be registered yet
	 */
	public static void create(KeyBinding base, KeyBinding alternative) {
		((KeyBindingAccessor) alternative).setTranslationKey(base.getTranslationKey() + "%" + ((NMUKKeyBinding) base).nmuk_getNextChildId());
		((KeyBindingAccessor) alternative).setCategory(base.getCategory());
		((NMUKKeyBinding) base).nmuk_addAlternative(alternative);
		((NMUKKeyBinding) alternative).nmuk_setParent(base);
		NMUKKeyBindingHelper.registerKeyBinding(alternative);
		NMUKKeyBindingHelper.defaultAlternatives.put(base, alternative);
	}

	/**
	 * Returns whether the given keybinding is an alternative.
	 *
	 * @param binding A keybinding
	 * @return Whether the given keybinding is an alternative
	 */
	public static boolean isAlternative(KeyBinding binding) {
		return ((NMUKKeyBinding) binding).nmuk_isAlternative();
	}

	/**
	 * Gets all alternatives that are registered for a keybinding.
	 *
	 * @param binding A keyinding
	 * @return A list of alternatives or <code>null</code>
	 */
	@Nullable
	public static List<KeyBinding> getAlternatives(KeyBinding binding) {
		return ((NMUKKeyBinding) binding).nmuk_getAlternatives();
	}

	/**
	 * Gets the base keybinding for an alternative keybinding.
	 *
	 * @param binding An alternative keybinding
	 * @return The base keyinding or <code>null</code> if the given keybinding is no alternative
	 */
	public static KeyBinding getBase(KeyBinding binding) {
		return ((NMUKKeyBinding) binding).nmuk_getParent();
	}
}
