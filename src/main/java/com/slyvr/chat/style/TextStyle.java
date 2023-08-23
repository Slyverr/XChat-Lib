package com.slyvr.chat.style;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

/**
 * Represents different styles of chat texts.
 *
 * @since 1.0
 */
public enum TextStyle {

    /**
     * Applies random formatting styles to the text.
     */
    OBFUSCATED(ChatColor.MAGIC) {
        @Override
        public void apply(@NotNull BaseComponent component) {
            if (component != null)
                component.setObfuscated(true);
        }
    },

    /**
     * Applies a bold formatting style to the text.
     */
    BOLD(ChatColor.BOLD) {
        @Override
        public void apply(@NotNull BaseComponent component) {
            if (component != null)
                component.setBold(true);
        }
    },

    /**
     * Applies a bold formatting style to the text.
     */
    STRIKETHROUGH(ChatColor.STRIKETHROUGH) {
        @Override
        public void apply(@NotNull BaseComponent component) {
            if (component != null)
                component.setStrikethrough(true);
        }
    },

    /**
     * Applies an underline formatting style to the text.
     */
    UNDERLINE(ChatColor.UNDERLINE) {
        @Override
        public void apply(@NotNull BaseComponent component) {
            if (component != null)
                component.setUnderlined(true);
        }
    },

    /**
     * Applies an italic formatting style to the text.
     */
    ITALIC(ChatColor.ITALIC) {
        @Override
        public void apply(@NotNull BaseComponent component) {
            if (component != null)
                component.setItalic(true);
        }
    },

    /**
     * Removes all the formatting styles from the text.
     */
    NONE(ChatColor.RESET) {
        @Override
        public void apply(@NotNull BaseComponent component) {
            if (component == null)
                return;

            component.setStrikethrough(false);
            component.setObfuscated(false);
            component.setUnderlined(false);
            component.setItalic(false);
            component.setBold(false);
        }
    };

    private final ChatColor color;

    TextStyle(@NotNull ChatColor color) {
        this.color = color;
    }

    /**
     * Gets the bukkit's chat-color corresponding this text-style.
     *
     * @return The bukkit's chat-color corresponding this text-style
     */
    @NotNull
    public ChatColor toChatColor() {
        return color;
    }

    /**
     * Gets the bungee's chat-color corresponding this text-style.
     *
     * @return The bungee's chat-color corresponding this text-style
     */
    @NotNull
    public net.md_5.bungee.api.ChatColor toBungeeChatColor() {
        return color.asBungee();
    }

    /**
     * Applies the given style to the given component.
     *
     * @param component The component to apply the style to.
     */
    public abstract void apply(@NotNull BaseComponent component);

}