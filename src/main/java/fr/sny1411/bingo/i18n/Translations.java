package fr.sny1411.bingo.i18n;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.translation.MiniMessageTranslationStore;
import net.kyori.adventure.translation.GlobalTranslator;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Loads the language files of the plugin (lang/&lt;language&gt;.properties, written in MiniMessage) and registers them
 * with Adventure, so that the translatable components of the plugin are shown in the language of each player.
 */
public final class Translations {
    public static final Locale DEFAULT_LOCALE = Locale.ENGLISH;
    private static final String FOLDER = "lang/";
    private static final String EXTENSION = ".properties";

    private static MiniMessageTranslationStore store;

    private Translations() {
        throw new IllegalStateException("Utility class");
    }

    public static void load(JarFile jar, Logger logger) {
        store = MiniMessageTranslationStore.create(Key.key("bingo", "translations"));
        store.defaultLocale(DEFAULT_LOCALE);
        Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
            String name = entries.nextElement().getName();
            if (name.startsWith(FOLDER) && name.endsWith(EXTENSION)) {
                Locale locale = Locale.forLanguageTag(name.substring(FOLDER.length(), name.length() - EXTENSION.length()).replace('_', '-'));
                try (InputStream input = jar.getInputStream(jar.getEntry(name));
                     Reader reader = new InputStreamReader(input, StandardCharsets.UTF_8)) {
                    store.registerAll(locale, new PropertyResourceBundle(reader), false);
                    logger.log(Level.INFO, "Loaded the {0} translations", locale.getDisplayLanguage(Locale.ENGLISH));
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "Could not load " + name, e);
                }
            }
        }
        GlobalTranslator.translator().addSource(store);
    }

    public static void unload() {
        if (store != null) {
            GlobalTranslator.translator().removeSource(store);
            store = null;
        }
    }

    /**
     * Renders the translatable components in the player's language, for the texts that Paper doesn't translate
     * itself, such as item names and lores.
     */
    public static Component render(Component component, Player player) {
        return render(component, player.locale());
    }

    public static Component render(Component component, Locale locale) {
        return GlobalTranslator.render(component, locale);
    }
}
