package com.slyvr.chat;

import com.google.common.base.Preconditions;
import com.slyvr.chat.style.TextColor;
import com.slyvr.chat.style.TextStyle;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The ChatTextBuilder class allows for the combination of multiple chat-texts by providing a simple and convenient way to append them together.
 *
 * @since 1.0.0
 */
public final class ChatTextBuilder implements Cloneable {

    private final List<ChatText> parts = new ArrayList<>();
    private int pointer = -1;

    private String text;

    /**
     * Constructs a new text-builder.
     *
     * @param initial The initial chat-texts.
     */
    public ChatTextBuilder(@NotNull ChatText... initial) {
        Preconditions.checkNotNull(initial, "The initial chat-texts collection cannot be null!");

        this.append(initial);
    }

    /**
     * Gets a copy of this text-builder's chat-text list.
     *
     * @return A copy of this text-builder's chat-text list
     */
    @NotNull
    public List<ChatText> getParts() {
        return new ArrayList<>(parts);
    }

    /**
     * Gets the chat-text at the given index.
     *
     * @param index The index to get.
     *
     * @return The chat-text present in the index, or null if none
     */
    @Nullable
    public ChatText getText(int index) {
        return isValidIndex(index) ? parts.get(index) : null;
    }

    /**
     * Gets the unformatted text representing all the appended chat-texts.
     *
     * @return The unformatted text
     */
    @NotNull
    public String getRawText() {
        return ChatColor.stripColor(text);
    }

    /**
     * Gets the text representing all the append
     */
    @NotNull
    public String getTextWithFormatting() {
        return text;
    }

    /**
     * Appends a chat-text to this text-builder. This sets the pointer to the last appended part.
     *
     * @param texts The chat-texts to append.
     *
     * @return This text-builder's instance
     */
    @NotNull
    public ChatTextBuilder append(@NotNull ChatText... texts) {
        for (ChatText text : texts) {
            if (text == null)
                continue;

            this.parts.add(text);
            this.text += text.getText();
        }

        this.pointer = parts.size() - 1;
        return this;
    }

    /**
     * Appends the given text with the giving color and style to this text-builder. This sets the pointer to the appended part.
     *
     * @param text   The content of the text.
     * @param color  The color to display the text with or null for none.
     * @param styles The styles to format the text with or null for none.
     *
     * @return This text-builder's instance
     */
    @NotNull
    public ChatTextBuilder append(@NotNull String text, @Nullable TextColor color, @Nullable TextStyle... styles) {
        Preconditions.checkNotNull(text, "Cannot append a null text!");

        this.parts.add(new ChatText(text, color, styles));
        this.text += text;

        this.pointer = parts.size() - 1;
        return this;
    }

    /**
     * Appends the given text with the giving color to this text-builder.
     *
     * @param text  The content of the text.
     * @param color The color to display the text with or null for none.
     *
     * @return This text-builder's instance
     */
    @NotNull
    public ChatTextBuilder append(@NotNull String text, @Nullable TextColor color) {
        return append(text, color, (TextStyle) null);
    }

    /**
     * Appends the given text to this text-builder.
     *
     * @param text The content of the text.
     *
     * @return This text-builder's instance
     */
    @NotNull
    public ChatTextBuilder append(@NotNull String text) {
        return append(text, null, (TextStyle) null);
    }

    /**
     * Selects the chat-text at the given index to directly modify.
     *
     * @param index The index to select.
     *
     * @return This text-builder's instance
     *
     * @throws IndexOutOfBoundsException If the given index is invalid.
     */
    @NotNull
    public ChatTextBuilder select(int index) {
        if (!isValidIndex(index))
            throw new IndexOutOfBoundsException("The index must be between 0 and " + (parts.size() - 1) + '!');

        this.pointer = index;
        return this;
    }

    /**
     * Colors the selected chat-text part of this text-builder with the given color.
     *
     * @param color The color to use.
     *
     * @return This text-builder's instance
     *
     * @throws NullPointerException  If the given color is null.
     * @throws IllegalStateException If there isn't any part selected.
     * @see #select(int)
     */
    @NotNull
    public ChatTextBuilder color(@Nullable TextColor color) {
        Preconditions.checkNotNull(color, "Color cannot be null!");
        Preconditions.checkArgument(pointer != -1, "There's no selected part to modify!");

        this.parts.get(pointer).color(color);
        return this;
    }

    /**
     * the selected chat-text part of this text-builder with the given styles.
     *
     * @param styles The styles to set.
     *
     * @return This text-builder's instance
     *
     * @throws NullPointerException  If the given styles collection is null.
     * @throws IllegalStateException If there isn't any part selected.
     * @see #select(int)
     */
    @NotNull
    public ChatTextBuilder style(@Nullable TextStyle... styles) {
        Preconditions.checkNotNull(styles, "Styles collection cannot be null!");
        Preconditions.checkArgument(pointer != -1, "There's no selected part to modify!");

        this.parts.get(pointer).style(styles);
        return this;
    }

    /**
     * Gets the number of independent chat-texts this text-builder contains.
     *
     * @return The number of independent chat-texts this text-builder contains
     */
    public int size() {
        return parts.size();
    }

    /**
     * Gets an array of base-components representing the combined parts of this text-builder.
     *
     * @return The array of base-components representing the combined parts of this text-builder
     */
    @NotNull
    public TextComponent[] toTextComponent() {
        TextComponent[] result = new TextComponent[parts.size()];

        for (int i = 0; i < result.length; i++)
            result[i] = parts.get(i).toTextComponent();

        return result;
    }

    /**
     * Sends and display this text to the given player.
     *
     * @param player The player to display the text for.
     */
    public void sendText(@NotNull Player player) {
        if (player != null)
            player.spigot().sendMessage(toTextComponent());
    }

    @Override
    public ChatTextBuilder clone() {
        try {
            ChatTextBuilder result = (ChatTextBuilder) super.clone();
            result.text = text;

            for (int i = 0; i < result.size(); i++)
                result.parts.set(i, new ChatText(parts.get(i)));

            return result;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        ChatTextBuilder other = (ChatTextBuilder) o;
        return pointer == other.pointer && parts.equals(other.parts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parts, pointer);
    }

    @Override
    public String toString() {
        return "ChatTextBuilder{" +
                "parts=" + parts +
                ", pointer=" + pointer +
                ", text='" + text + '\'' +
                '}';
    }

    /**
     * Checks if the index is a valid part-index.
     *
     * @param index The index to check.
     *
     * @return True if the index is valid, otherwise false
     */
    private boolean isValidIndex(int index) {
        return !parts.isEmpty() && index >= 0 && index <= parts.size();
    }

}