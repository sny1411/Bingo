package fr.sny1411.bingo.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

public final class Items {
    private Items() {
        throw new IllegalStateException("Utility class");
    }
    private static ItemStack settings;
    private static ItemStack teamSelector;
    private static ItemStack glassForGui;
    private static ItemStack glassValidBingo;

    public abstract static class Challenge {
        private static ItemStack harming;
        private static ItemStack dolphin;
        private static ItemStack fox;
        private static ItemStack horse;
        private static ItemStack strider;
        private static ItemStack cavespider;
        private static ItemStack llama;
        private static ItemStack parrot;
        private static ItemStack pillager;
        private static ItemStack leatherBoots;
        private static ItemStack piglin;
        private static ItemStack ghast;
        private static ItemStack elderGuardian;
        private static ItemStack ironGolem;
        private static ItemStack regen;
        private static ItemStack diamondChestplate;
        private static ItemStack silverfish;
        private static ItemStack enderman;
        private static ItemStack bowser;
        private static ItemStack grenouille;
        private static ItemStack dromadaire;

        private static void init() {
            initHarming();
            initDolphin();
            initFox();
            initHorse();
            initStrider();
            initCavespider();
            initLlama();
            initParrot();
            initPillager();
            initLeatherBoots();
            initPiglin();
            initGhast();
            initElderGuardian();
            initIronGolem();
            initRegen();
            initDiamondChestplate();
            initSilverfish();
            initEnderman();
            initBowser();
            initGrenouille();
            initDromadaire();
        }

        private static void initHarming() {
            harming = new ItemStack(Material.POTION);
            PotionMeta harmingMeta = (PotionMeta) harming.getItemMeta();
            harmingMeta.setBasePotionData(new PotionData(PotionType.INSTANT_DAMAGE, false, false));
            harming.setItemMeta(harmingMeta);
        }

        private static void initDolphin() {
            dolphin = SkullCustom.getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGU5Njg4Yjk1MGQ4ODBiNTViN2FhMmNmY2Q3NmU1YTBmYTk0YWFjNmQxNmY3OGU4MzNmNzQ0M2VhMjlmZWQzIn19fQ==");
        }

        private static void initFox() {
            fox = SkullCustom.getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDg5NTRhNDJlNjllMDg4MWFlNmQyNGQ0MjgxNDU5YzE0NGEwZDVhOTY4YWVkMzVkNmQzZDczYTNjNjVkMjZhIn19fQ==");
        }

        private static void initHorse() {
            horse = SkullCustom.getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGM0OGZkYTYyZWMwZDA4MzVlMTIzZDA4MmI1Yzk2MDRkZGIyZjE4MjI0ZjExNTQ5NDllYzdhYWNhMzQ1Mjc2OCJ9fX0=");
        }

        private static void initStrider() {
            strider = SkullCustom.getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMThhOWFkZjc4MGVjN2RkNDYyNWM5YzA3NzkwNTJlNmExNWE0NTE4NjY2MjM1MTFlNGM4MmU5NjU1NzE0YjNjMSJ9fX0=");
        }

        private static void initCavespider() {
            cavespider = SkullCustom.getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2U4NTUzODBiNTQyZGQyMjI2NTk5NzhhOTY3Nzc2NGY5NTM3ZTc5YTZjNTA1MDU4MTEyMDFiODU3MDRlYjcwNSJ9fX0=");
        }

        private static void initLlama() {
            llama = SkullCustom.getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmFlMjVkZGMyZDI1MzljNTY1ZGZmMmFhNTAwNjAzM2YxNGNjMDYzNzlmZTI4YjA3MzFjN2JkYzY1YmEwZTAxNiJ9fX0=");
        }

        private static void initParrot() {
            parrot = SkullCustom.getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmM2NDcxZjIzNTQ3YjJkYmRmNjAzNDdlYTEyOGY4ZWIyYmFhNmE3OWIwNDAxNzI0ZjIzYmQ0ZTI1NjRhMmI2MSJ9fX0=");
        }

        private static void initPillager() {
            pillager = SkullCustom.getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmUzOWI0NmY4MWZkNmUxMjYxZTE0ODU4OGNkOWEzOWYxZjA5OWQzMDA1YTExODljN2JmN2IzZjc4MGNkMGU3NyJ9fX0=");
        }

        private static void initLeatherBoots() {
            leatherBoots = new ItemStack(Material.LEATHER_BOOTS);
            ItemMeta leatherBootsMeta = leatherBoots.getItemMeta();
            leatherBootsMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            leatherBoots.setItemMeta(leatherBootsMeta);
        }

        private static void initPiglin() {
            piglin = SkullCustom.getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTBiYzlkYmI0NDA0YjgwMGY4Y2YwMjU2MjIwZmY3NGIwYjcxZGJhOGI2NjYwMGI2NzM0ZjRkNjMzNjE2MThmNSJ9fX0=");
        }

        private static void initGhast() {
            ghast = SkullCustom.getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGI2YTcyMTM4ZDY5ZmJiZDJmZWEzZmEyNTFjYWJkODcxNTJlNGYxYzk3ZTVmOTg2YmY2ODU1NzFkYjNjYzAifX19");
        }

        private static void initElderGuardian() {
            elderGuardian = SkullCustom.getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWM3OTc0ODJhMTRiZmNiODc3MjU3Y2IyY2ZmMWI2ZTZhOGI4NDEzMzM2ZmZiNGMyOWE2MTM5Mjc4YjQzNmIifX19");
        }

        private static void initIronGolem() {
            ironGolem = SkullCustom.getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODkwOTFkNzllYTBmNTllZjdlZjk0ZDdiYmE2ZTVmMTdmMmY3ZDQ1NzJjNDRmOTBmNzZjNDgxOWE3MTQifX19");
        }

        private static void initRegen() {
            regen = new ItemStack(Material.POTION);
            PotionMeta regenMeta = (PotionMeta) regen.getItemMeta();
            ;
            regenMeta.setBasePotionData(new PotionData(PotionType.REGEN, false, true));
            regen.setItemMeta(regenMeta);
        }

        private static void initDiamondChestplate() {
            diamondChestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
            ItemMeta metaChestplate = diamondChestplate.getItemMeta();
            metaChestplate.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            diamondChestplate.setItemMeta(metaChestplate);
        }

        private static void initSilverfish() {
            silverfish = SkullCustom.getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGE5MWRhYjgzOTFhZjVmZGE1NGFjZDJjMGIxOGZiZDgxOWI4NjVlMWE4ZjFkNjIzODEzZmE3NjFlOTI0NTQwIn19fQ==");
        }

        private static void initEnderman() {
            enderman = SkullCustom.getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2E1OWJiMGE3YTMyOTY1YjNkOTBkOGVhZmE4OTlkMTgzNWY0MjQ1MDllYWRkNGU2YjcwOWFkYTUwYjljZiJ9fX0=");
        }

        private static void initBowser() {
            bowser = SkullCustom.getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTg2MDYxMGVkMzA3ODVlNjIyOWU4MmUyODk3YjQyZmRhYmIxZGY2Mjk2ZDM3MzFmYWMyNzQ0ZTU2YTllYjkifX19");
        }

        private static void initGrenouille() {
            grenouille = SkullCustom.getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2U2MmU4YTA0OGQwNDBlYjA1MzNiYTI2YTg2NmNkOWMyZDA5MjhjOTMxYzUwYjQ0ODJhYzNhMzI2MWZhYjZmMCJ9fX0=");
        }

        private static void initDromadaire() {
            dromadaire = SkullCustom.getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzY0MmM5ZjcxMTMxYjVkZjRhOGMyMWM4YzZmMTA2ODRmMjJhYmFmYjhjZDY4YTFkNTVhYzRiZjI2M2E1M2EzMSJ9fX0=");
        }

        public static ItemStack getHarming() {
            return harming;
        }

        public static ItemStack getDolphin() {
            return dolphin;
        }

        public static ItemStack getFox() {
            return fox;
        }

        public static ItemStack getHorse() {
            return horse;
        }

        public static ItemStack getStrider() {
            return strider;
        }

        public static ItemStack getCavespider() {
            return cavespider;
        }

        public static ItemStack getLlama() {
            return llama;
        }

        public static ItemStack getParrot() {
            return parrot;
        }

        public static ItemStack getPillager() {
            return pillager;
        }

        public static ItemStack getLeatherBoots() {
            return leatherBoots;
        }

        public static ItemStack getPiglin() {
            return piglin;
        }

        public static ItemStack getGhast() {
            return ghast;
        }

        public static ItemStack getElderGuardian() {
            return elderGuardian;
        }

        public static ItemStack getIronGolem() {
            return ironGolem;
        }

        public static ItemStack getRegen() {
            return regen;
        }

        public static ItemStack getDiamondChestplate() {
            return diamondChestplate;
        }

        public static ItemStack getSilverfish() {
            return silverfish;
        }

        public static ItemStack getEnderman() {
            return enderman;
        }

        public static ItemStack getBowser() {
            return bowser;
        }

        public static ItemStack getGrenouille() {
            return grenouille;
        }

        public static ItemStack getDromadaire() {
            return dromadaire;
        }
    }

    public static void init() {
        initSettings();
        initTeamSelector();
        initGlassForGui();
        initGlassValidBingo();

        Challenge.init();
    }

    private static void initGlassForGui() {
        glassForGui = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        ItemMeta metaGlassGui = glassForGui.getItemMeta();
        metaGlassGui.displayName(Component.text(" "));
        glassForGui.setItemMeta(metaGlassGui);
    }

    private static void initSettings() {
        settings = new ItemStack(Material.COMPARATOR);
        ItemMeta metaSettings = settings.getItemMeta();
        metaSettings.displayName(Component.text("§2◈ §a§lParamètres §r§2◈"));
        settings.setItemMeta(metaSettings);
    }

    private static void initTeamSelector() {
        teamSelector = new ItemStack(Material.COMPASS);
        ItemMeta metaTeamsSelector = teamSelector.getItemMeta();
        metaTeamsSelector.displayName(Component.text("§2◈ §a§lTeams §r§2◈"));
        teamSelector.setItemMeta(metaTeamsSelector);
    }

    private static void initGlassValidBingo() {
        glassValidBingo = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        ItemMeta metaGlassValidBingo = glassValidBingo.getItemMeta();
        metaGlassValidBingo.displayName(Component.text(" "));
        glassValidBingo.setItemMeta(metaGlassValidBingo);
    }

    public static ItemStack getSettings() {
        return settings;
    }

    public static ItemStack getTeamSelector() {
        return teamSelector;
    }

    public static ItemStack getGlassForGui() {
        return glassForGui;
    }

    public static ItemStack getGlassValidBingo() {
        return glassValidBingo;
    }
}
