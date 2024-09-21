package com.slyvr.chat;

import com.google.common.base.Preconditions;
import com.slyvr.chat.style.TextAlignment;
import com.slyvr.chat.utils.ChatTextUtils;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a text-section composed of a combination of multiple texts.
 *
 * @since 1.0.0
 */
public final class ChatTextSection {

    private final List<SectionLine<?>> section_parts = new ArrayList<>();

    /**
     * Constructs a new text-section.
     */
    public ChatTextSection() {
    }

    /**
     * Appends a new line to this section.
     *
     * @param text      The text to append.
     * @param alignment The alignment to apply.
     *
     * @return This text-section's instance
     *
     * @throws NullPointerException If the given text or alignment type is null.
     */
    @NotNull
    public ChatTextSection append(@NotNull ChatText text, @NotNull TextAlignment alignment) {
        Preconditions.checkNotNull(text, "Text to append cannot be null!");
        Preconditions.checkNotNull(alignment, "Alignment type cannot be null!");

        this.section_parts.add(new TextLine(text, alignment));
        return this;
    }

    /**
     * Appends a new line to this section.
     *
     * @param text   The text to append.
     * @param spaces The number of spaces to align by.
     *
     * @return This text-section's instance
     *
     * @throws NullPointerException     If the given text is null.
     * @throws IllegalArgumentException If the number of spaces is negative.
     */
    @NotNull
    public ChatTextSection append(@NotNull ChatText text, int spaces) {
        Preconditions.checkNotNull(text, "Text to append cannot be null!");
        Preconditions.checkArgument(spaces >= 0, "Number of spaces to append by cannot be negative!");

        this.section_parts.add(new TextLine(text, spaces));
        return this;
    }

    /**
     * Appends a new line to this section.
     *
     * @param text The text to append.
     *
     * @return This text-section's instance
     *
     * @throws NullPointerException If the given text is null.
     */
    @NotNull
    public ChatTextSection append(@NotNull ChatText text) {
        return append(text, TextAlignment.LEFT);
    }

    /**
     * Appends a new line to this section.
     *
     * @param text      The text to append.
     * @param alignment The alignment to apply.
     *
     * @return This text-section's instance
     *
     * @throws NullPointerException If the given text or alignment type is null.
     */
    @NotNull
    public ChatTextSection append(@NotNull String text, @NotNull TextAlignment alignment) {
        Preconditions.checkNotNull(text, "Text to append cannot be null!");
        Preconditions.checkNotNull(alignment, "TextAlignment's type cannot be null!");

        this.section_parts.add(new StringLine(text, alignment));
        return this;
    }

    /**
     * Appends a new line to this section.
     *
     * @param text   The text to append.
     * @param spaces The number of spaces to align by.
     *
     * @return This text-section's instance
     *
     * @throws NullPointerException     If the given text is null.
     * @throws IllegalArgumentException If the number of spaces is negative.
     */
    @NotNull
    public ChatTextSection append(@NotNull String text, int spaces) {
        Preconditions.checkNotNull(text, "Text to append cannot be null!");
        Preconditions.checkArgument(spaces >= 0, "Number of spaces to append by cannot be negative!");

        this.section_parts.add(new StringLine(text, spaces));
        return this;
    }

    /**
     * Appends a new line to this text-section.
     *
     * @param text The text to append.
     *
     * @return This text-section's instance
     *
     * @throws NullPointerException If the given text is null.
     */
    @NotNull
    public ChatTextSection append(@NotNull String text) {
        return append(text, TextAlignment.LEFT);
    }

    /**
     * Sends and display this section to the player.
     *
     * @param player The player to display this section for
     */
    public void sendSection(@NotNull Player player) {
        if (player == null)
            return;

        for (SectionLine<?> line : section_parts)
            line.sendText(player);
    }
    
    /**
     * Represents a line for a text-section.
     *
     * @since 1.0
     */
    private interface SectionLine<T> {

        /**
         * Aligns the text according to the given alignment type.
         *
         * @param obj       The obj to align.
         * @param alignment The type of alignment.
         **/
        void align(@NotNull T obj, @NotNull TextAlignment alignment);

        /**
         * Aligns the text by the specified number of spaces.
         *
         * @param obj    The object to align.
         * @param spaces The number of spaces to align by.
         */
        void align(@NotNull T obj, int spaces);

        /**
         * Sends and displays this line to the given player.
         *
         * @param player The player to display this line for.
         */
        void sendText(@NotNull Player player);

    }

    private static final class StringLine implements SectionLine<String> {

        private String[] aligned;

        public StringLine(@NotNull String text, @NotNull TextAlignment alignment) {
            this.align(text, alignment);
        }

        public StringLine(@NotNull String text, int spaces) {
            this.align(text, spaces);
        }

        @Override
        public void align(@NotNull String text, @NotNull TextAlignment alignment) {
            switch (alignment) {
                case LEFT:
                    this.aligned = new String[]{text};
                    break;
                case CENTER:
                    this.aligned = ChatTextUtils.align(text, 6).toArray(new String[0]);
                    break;
                case RIGHT:
                    this.aligned = ChatTextUtils.align(text, 3).toArray(new String[0]);
                    break;
            }

        }

        @Override
        public void align(@NotNull String text, int spaces) {
            if (spaces <= 0) {
                this.align(text, TextAlignment.LEFT);
                return;
            }

            this.aligned = ChatTextUtils.split(text, spaces).toArray(new String[0]);
        }

        @Override
        public void sendText(@NotNull Player player) {
            player.sendMessage(aligned);
        }

    }

    private static final class TextLine implements SectionLine<ChatText> {

        private TextComponent[] aligned;

        public TextLine(@NotNull ChatText text, @NotNull TextAlignment alignment) {
            this.align(text, alignment);
        }

        public TextLine(@NotNull ChatText text, int spaces) {
            this.align(text, spaces);
        }

        @Override
        public void align(@NotNull ChatText text, @NotNull TextAlignment alignment) {
            switch (alignment) {
                case LEFT:
                    this.aligned = new TextComponent[]{text.toTextComponent()};
                    break;
                case CENTER:
                    this.aligned = align(text.toTextComponent(), 6);
                    break;
                case RIGHT:
                    this.aligned = align(text.toTextComponent(), 3);
                    break;
            }

        }

        @Override
        public void align(@NotNull ChatText text, int spaces) {
            if (spaces <= 0) {
                this.align(text, TextAlignment.LEFT);
                return;
            }

            this.aligned = copy(ChatTextUtils.split(text.getTextWithFormatting(), spaces), text.toTextComponent());
        }

        @Override
        public void sendText(@NotNull Player player) {
            for (TextComponent comp : aligned)
                player.spigot().sendMessage(comp);
        }

        @NotNull
        private TextComponent[] align(@NotNull TextComponent comp, int factor) {
            return copy(ChatTextUtils.align(comp.toLegacyText(), factor), comp);
        }

        @NotNull
        private TextComponent[] copy(@NotNull List<String> lines, @NotNull TextComponent def) {
            TextComponent[] result = new TextComponent[lines.size()];

            for (int i = 0; i < lines.size(); i++) {
                TextComponent comp = new TextComponent(def);
                comp.setText(lines.get(i));

                result[i] = comp;
            }

            return result;
        }


    }

}