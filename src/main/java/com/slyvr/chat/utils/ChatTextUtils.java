package com.slyvr.chat.utils;

import org.bukkit.ChatColor;
import org.bukkit.map.MapFont;
import org.bukkit.map.MinecraftFont;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utilities for chat-texts.
 *
 * @since 1.0.0
 */
public final class ChatTextUtils {

    public static final int DEFAULT_CHAT_WIDTH = 320;

    private static final Map<Character, int[]> CUSTOM_CHARS = new HashMap<>();

    private ChatTextUtils() {
    }

    /**
     * Gets the width of a character.
     *
     * @param character The character to get its width.
     * @param bold      True if the character is bold, otherwise false.
     *
     * @return The width of the character
     */
    public static int getCharacterWidth(char character, boolean bold) {
        if (character == ' ')
            return 3;

        MapFont.CharacterSprite sprite = MinecraftFont.Font.getChar(character);
        if (sprite != null)
            return bold ? sprite.getWidth() + 1 : sprite.getWidth();

        int[] data = CUSTOM_CHARS.get(character);
        if (data != null)
            return bold ? data[1] : data[0];

        return 5;
    }

    /**
     * Gets the width of a character considering it's not bold.
     *
     * @param character The character to get its width.
     *
     * @return The width of the character
     */
    public static int getCharacterWidth(char character) {
        return getCharacterWidth(character, false);
    }

    /**
     * Sets the width of a custom unregistered character.
     *
     * @param character The character to register.
     * @param width     The width of the character when normal.
     * @param bold      The width of the character when bold.
     */
    public static void setCharacterWidth(char character, int width, int bold) {
        if (width <= 0 || bold <= 0)
            return;

        MapFont.CharacterSprite sprite = MinecraftFont.Font.getChar(character);
        if (sprite != null)
            return;

        ChatTextUtils.CUSTOM_CHARS.put(character, new int[]{width, bold});
    }

    /**
     * Gets the width of a text with formatting codes.
     *
     * @param text The text to get its width.
     *
     * @return The width of the text
     */
    public static int getTextWidth(@NotNull String text) {
        int maximum = text.length() - 1;
        int result = 0;

        boolean isColor = false;
        boolean isBold = false;

        for (int i = 0; i < text.length(); i++) {
            char character = text.charAt(i);

            // Checking if the current character is a COLOR_CHAR for later use.
            if (character == ChatColor.COLOR_CHAR) {
                isColor = true;
                continue;
            }

            // Checking if bold.
            if (isColor) {
                isColor = false;
                isBold = (character == 'l' || character == 'L');
                continue;
            }

            result += getCharacterWidth(character, isBold);
            if (character != ' ' && i != maximum)
                result++;
        }

        return result;
    }

    /**
     * Gets the number spaces required to center a text based on the provided width.
     *
     * @param width The width of the text to center.
     *
     * @return The number of spaces required to center the text, or 0 if none
     */
    public static int getSpacesToCenter(int width) {
        int difference = DEFAULT_CHAT_WIDTH - width;
        return difference > 6 ? difference / 6 : 0;
    }

    /**
     * Gets the number spaces required to align a text to the right based on the provided width.
     *
     * @param width The width of the text to align to the right.
     *
     * @return The number of spaces required to align to the right, or 0 if none
     */
    public static int getSpacesToRight(int width) {
        int difference = DEFAULT_CHAT_WIDTH - width;
        return difference > 3 ? difference / 3 : 0;
    }

    /**
     * Aligns the given text to the center of the chat's box.
     *
     * @param text The text to align.
     *
     * @return The aligned text
     */
    @NotNull
    public static String alignToCenter(@NotNull String text) {
        return getEmptyLine(getSpacesToCenter(getTextWidth(text))) + text;
    }

    /**
     * Aligns the given text to the right of the chat's box.
     *
     * @param text The text to align.
     *
     * @return The aligned text
     */
    @NotNull
    public static String alignToRight(@NotNull String text) {
        return getEmptyLine(getSpacesToRight(getTextWidth(text))) + text;
    }

    /**
     * Splits the given text into lines based on the specified initial spaces.
     *
     * @param text   The text to be split into lines.
     * @param spaces The number of initial spaces to count.
     *
     * @return A list of lines representing different segments of the input text
     */
    @NotNull
    public static List<String> split(@NotNull String text, int spaces) {
        List<String> lines = new ArrayList<>();

        int text_width = spaces * 3;
        int last_index = 0;

        boolean isColor = false;
        boolean isBold = false;

        String empty = ChatColor.RESET + getEmptyLine(spaces);
        for (int i = 0; i < text.length(); i++) {
            char character = text.charAt(i);

            // Checking if the current character is a COLOR_CHAR for later use.
            if (character == ChatColor.COLOR_CHAR) {
                isColor = true;
                continue;
            }

            // Checking if bold.
            if (isColor) {
                isColor = false;
                isBold = (character == 'l' || character == 'L');
                continue;
            }

            int char_width = getCharacterWidth(character, isBold);
            if (character != ' ' && i != text.length() - 1)
                char_width++;

            if (text_width + char_width <= ChatTextUtils.DEFAULT_CHAT_WIDTH) {
                text_width += char_width;
                continue;
            }

            if (!lines.isEmpty())
                lines.add(empty + ChatColor.getLastColors(lines.get(lines.size() - 1)) + text.substring(last_index, last_index = i));
            else
                lines.add(empty + text.substring(last_index, last_index = i));

            text_width = spaces * 3;
        }

        if (!lines.isEmpty())
            lines.add(empty + ChatColor.getLastColors(lines.get(lines.size() - 1)) + text.substring(last_index));
        else
            lines.add(empty + text.substring(last_index));

        return lines;
    }

    /**
     * Splits the given text into lines with no initial spaces.
     *
     * @param text The text to be split into lines.
     *
     * @return A list of lines representing different segments of the input text
     */
    @NotNull
    public static List<String> split(@NotNull String text) {
        return split(text, 0);
    }

    /**
     * Aligns the given text into multiple lines based on the specified factor.
     *
     * @param text   The text to be aligned and split into lines.
     * @param factor The factor by which the text width will be divided for alignment.
     *
     * @return A list of aligned lines, each representing a segment of the input text
     */
    @NotNull
    public static List<String> align(@NotNull String text, int factor) {
        List<String> lines = new ArrayList<>();

        int text_width = 0;
        int last_index = 0;

        boolean isColor = false;
        boolean isBold = false;

        for (int i = 0; i < text.length(); i++) {
            char character = text.charAt(i);

            // Checking if the current character is a COLOR_CHAR for later use.
            if (character == ChatColor.COLOR_CHAR) {
                isColor = true;
                continue;
            }

            // Checking if bold.
            if (isColor) {
                isColor = false;
                isBold = (character == 'l' || character == 'L');
                continue;
            }

            int char_width = getCharacterWidth(character, isBold);
            if (character != ' ' && i != text.length() - 1)
                char_width++;

            if (text_width + char_width <= ChatTextUtils.DEFAULT_CHAT_WIDTH) {
                text_width += char_width;
                continue;
            }

            lines.add(text.substring(last_index, (last_index = i)));
            text_width = 0;
        }

        String empty = ChatColor.RESET + getEmptyLine((DEFAULT_CHAT_WIDTH - text_width) / factor);
        if (!lines.isEmpty())
            lines.add(empty + ChatColor.getLastColors(lines.get(lines.size() - 1)) + text.substring(last_index));
        else
            lines.add(empty + text.substring(last_index));

        return lines;
    }

    /**
     * Gets a string filled with the given number of whitespaces.
     *
     * @param spaces The number of whitespaces.
     *
     * @return The string filled with whitespaces
     */
    @NotNull
    public static String getEmptyLine(int spaces) {
        if (spaces <= 0)
            return "";

        StringBuilder builder = new StringBuilder(spaces);

        for (int i = 0; i < spaces; i++)
            builder.append(' ');

        return builder.toString();
    }

}