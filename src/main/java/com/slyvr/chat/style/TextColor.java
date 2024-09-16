package com.slyvr.chat.style;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

/**
 * Represents different colors for chat texts.
 *
 * @since 1.0.0
 */
public enum TextColor {

    BLACK(ChatColor.BLACK),
    DARK_BLUE(ChatColor.DARK_BLUE),
    DARK_GREEN(ChatColor.DARK_GREEN),
    DARK_AQUA(ChatColor.DARK_AQUA),
    DARK_RED(ChatColor.DARK_RED),
    DARK_PURPLE(ChatColor.DARK_PURPLE),
    GOLD(ChatColor.GOLD),
    GRAY(ChatColor.GRAY),
    DARK_GRAY(ChatColor.DARK_GRAY),
    BLUE(ChatColor.BLUE),
    GREEN(ChatColor.GREEN),
    AQUA(ChatColor.AQUA),
    RED(ChatColor.RED),
    LIGHT_PURPLE(ChatColor.LIGHT_PURPLE),
    YELLOW(ChatColor.YELLOW),
    WHITE(ChatColor.WHITE);

    private final ChatColor color;

    TextColor(@NotNull ChatColor color) {
        this.color = color;
    }

    /**
     * Gets the bukkit's chat-color corresponding this text-color.
     *
     * @return The bukkit's chat-color corresponding this text-color
     */
    @NotNull
    public ChatColor toChatColor() {
        return color;
    }

    /**
     * Gets the bungee's chat-color corresponding this text-color.
     *
     * @return The bungee's chat-color corresponding this text-color
     */
    @NotNull
    public net.md_5.bungee.api.ChatColor toBungeeChatColor() {
        return color.asBungee();
    }

}