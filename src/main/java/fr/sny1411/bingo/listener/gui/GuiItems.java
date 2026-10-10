package fr.sny1411.bingo.listener.gui;

import fr.sny1411.bingo.i18n.Translations;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Builds the items of the GUIs in the language of the player who opens them.
 */
final class GuiItems {
    private GuiItems() {
        throw new IllegalStateException("Utility class");
    }

    static ItemStack item(Material material, int amount, Component name, List<Component> lore, Player player) {
        return named(new ItemStack(material, amount), name, lore, player);
    }

    static ItemStack item(Material material, Component name, Player player) {
        return item(material, 1, name, List.of(), player);
    }

    static ItemStack named(ItemStack item, Component name, List<Component> lore, Player player) {
        ItemMeta meta = item.getItemMeta();
        meta.displayName(render(name, player));
        List<Component> renderedLore = new ArrayList<>();
        for (Component line : lore) {
            renderedLore.add(render(line, player));
        }
        meta.lore(renderedLore);
        item.setItemMeta(meta);
        return item;
    }

    // Item names and lores are italic by default, the GUI texts are not
    private static Component render(Component text, Player player) {
        return Translations.render(text, player).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }

    // ">> Selected" in yellow, or the option in gray
    static Component option(Component label, boolean selected) {
        if (selected) {
            return Component.textOfChildren(Component.text(">> ", NamedTextColor.GOLD, TextDecoration.BOLD), label.color(NamedTextColor.YELLOW));
        }
        return label.color(NamedTextColor.GRAY);
    }

    static Component title(String key, Player player) {
        return Translations.render(Component.translatable(key, NamedTextColor.DARK_AQUA, TextDecoration.BOLD), player);
    }
}
