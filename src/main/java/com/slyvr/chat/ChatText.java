package com.slyvr.chat;

import com.google.common.base.Preconditions;
import com.slyvr.chat.style.TextColor;
import com.slyvr.chat.style.TextStyle;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * Represents a single chat-text.
 *
 * @since 1.0.0
 */
public final class ChatText {

    private final TextComponent component;

    /**
     * Constructs a new chat-text.
     *
     * @param text   The text content of this chat-text.
     * @param color  The color to display the text with or null for none.
     * @param styles The styles to format the text with or null for none.
     *
     * @throws NullPointerException If the given text is null.
     */
    public ChatText(@NotNull String text, @Nullable TextColor color, @Nullable TextStyle... styles) {
        Preconditions.checkNotNull(text, "The text content cannot be null!");

        this.component = new TextComponent(text);

        this.color(color);
        this.style(styles);
    }

    /**
     * Constructs a new chat-text.
     *
     * @param comp The chat-component representing the text.
     *
     * @throws NullPointerException If the given text-component is null.
     */
    public ChatText(@NotNull TextComponent comp) {
        Preconditions.checkNotNull(comp, "The text-component cannot be null!");

        this.component = new TextComponent(comp);
    }

    /**
     * Constructs a copy of the provided text.
     *
     * @param text The text to copy from.
     *
     * @throws NullPointerException If the given text is null.
     */
    public ChatText(@NotNull ChatText text) {
        Preconditions.checkNotNull(text, "Cannot copy a null chat text!");

        this.component = new TextComponent(text.component);
    }

    /**
     * Constructs a new chat-text.
     *
     * @param text  The text content of the chat-text.
     * @param color The color to display the text with or null for white.
     *
     * @throws NullPointerException If the given text is null.
     */
    public ChatText(@NotNull String text, @Nullable TextColor color) {
        this(text, color, (TextStyle) null);
    }

    /**
     * Constructs a new chat-text.
     *
     * @param text The text content of the chat-text.
     *
     * @throws NullPointerException If the given text is null.
     */
    public ChatText(@NotNull String text) {
        this(text, null);
    }

    /**
     * Gets the string with the text and all its formatting codes ({@link net.md_5.bungee.api.ChatColor}).
     *
     * @return A string with the text and all its formatting codes
     */
    @NotNull
    public String getTextWithFormatting() {
        return component.toLegacyText();
    }

    /**
     * Gets the text content of this chat-text.
     *
     * @return The text content of this chat-text
     */
    @NotNull
    public String getText() {
        return component.getText();
    }

    /**
     * Gets the color to display this chat-text with.
     *
     * @return The color to display this chat-text with
     */
    @NotNull
    public TextColor getColor() {
        return TextColor.valueOf(component.getColor().getName());
    }

    /**
     * Gets all the styles applied to this text.
     *
     * @return All the styles applied to this text
     */
    @NotNull
    public TextStyle[] getStyles() {
        ArrayList<TextStyle> result = new ArrayList<>(5);

        if (component.isBold())
            result.add(TextStyle.BOLD);
        if (component.isItalic())
            result.add(TextStyle.ITALIC);
        if (component.isUnderlined())
            result.add(TextStyle.UNDERLINE);
        if (component.isObfuscated())
            result.add(TextStyle.OBFUSCATED);
        if (component.isStrikethrough())
            result.add(TextStyle.STRIKETHROUGH);

        return result.toArray(new TextStyle[result.size()]);
    }

    /**
     * Colors this text with the given color.
     *
     * @param color The color to set.
     *
     * @return This chat-text's instance
     */
    @NotNull
    public ChatText color(@Nullable TextColor color) {
        if (color != null)
            this.component.setColor(color.toBungeeChatColor());

        return this;
    }

    /**
     * Applies the given styles to this text.
     *
     * @param styles The styles to set.
     *
     * @return This chat-text's instance
     */
    @NotNull
    public ChatText style(@Nullable TextStyle... styles) {
        if (styles == null)
            return this;

        for (TextStyle style : styles) {
            if (style != null)
                style.apply(component);
        }

        return this;
    }

    /**
     * Sets the click-event to call when clicking this text.
     *
     * @param event The click-event to set or null for none.
     *
     * @return This chat-text's instance
     */
    @NotNull
    public ChatText setClickEvent(@Nullable ClickEvent event) {
        this.component.setClickEvent(event);
        return this;
    }

    /**
     * Sets the hover-event to call when hovering over this text.
     *
     * @param event The hover-event to set or null for none.
     *
     * @return This chat-text's instance
     */
    @NotNull
    public ChatText setHoverEvent(@Nullable HoverEvent event) {
        this.component.setHoverEvent(event);
        return this;
    }

    /**
     * Builds and combine all the parts of this builder together into one text-component.
     *
     * @return The created text-component instance with all the parts combined
     */
    @NotNull
    public TextComponent toTextComponent() {
        return component;
    }

    /**
     * Sends and display all the texts of this builder as one text to the player.
     *
     * @param player The player to display the text for.
     */
    public void sendText(@NotNull Player player) {
        if (player != null)
            player.spigot().sendMessage(component);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null || getClass() != obj.getClass())
            return false;

        ChatText other = (ChatText) obj;
        return component.equals(other.component);
    }

    @Override
    public int hashCode() {
        return Objects.hash(component);
    }

    @Override
    public String toString() {
        return "ChatText{Text: " + component.getText() + ",Text-Color: " + component.getColor() + ",Text-Styles: " + Arrays.toString(getStyles()) + "}";
    }

}