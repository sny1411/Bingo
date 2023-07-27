package fr.sny1411.bingo.listener.gui;

import fr.sny1411.bingo.Bingo;
import fr.sny1411.bingo.Game;
import fr.sny1411.bingo.utils.Challenge;
import fr.sny1411.bingo.utils.Items;
import fr.sny1411.bingo.utils.SkullCustom;
import fr.sny1411.bingo.utils.Team;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class SettingsGui implements Listener {
    private static final Inventory gui = Bukkit.createInventory(null, 27, Component.text("§3§lParamètres"));
    private static final Inventory guiDifficult = Bukkit.createInventory(null, 27, Component.text("§3§lParamètres grille"));
    private static final Inventory guiVictoire = Bukkit.createInventory(null, 27, Component.text("§3§lParamètres victoire"));
    private static final Inventory guiTeams = Bukkit.createInventory(null, 27, Component.text("§3§lParamètres teams"));

    private static void openGui(Player player) {
        for (int i = 0; i < 27; i++) {
            if (i < 10 || i > 16) {
                gui.setItem(i, Items.getGlassForGui());
            }
        }

        ItemStack item = new ItemStack(Material.WHITE_BANNER);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text("§b§lEquipes"));

        List<Component> itemLore = new ArrayList<>();
        itemLore.add(Component.text("§7>> Clique pour modifier"));
        meta.lore(itemLore);
        item.setItemMeta(meta);
        gui.setItem(10, item);

        itemLore = new ArrayList<>();
        if (Bingo.getGame().getModeAffichage() == Game.ModeAffichage.CHILL) {
            item = new ItemStack(Material.IRON_BLOCK);
            meta = item.getItemMeta();
            meta.displayName(Component.text("§b§lAffichage des défis"));
            itemLore.add(Component.text("§6§l>> §r§eChill"));
            itemLore.add(Component.text("§7Compétition"));
        } else {
            item = new ItemStack(Material.NETHERITE_BLOCK);
            meta = item.getItemMeta();
            meta.displayName(Component.text("§b§lAffichage des défis"));
            itemLore.add(Component.text("§7Chill"));
            itemLore.add(Component.text("§6§l>> §r§eCompétition"));
        }
        meta.lore(itemLore);
        item.setItemMeta(meta);
        gui.setItem(11, item);

        item = new ItemStack(Material.CAULDRON);
        meta = item.getItemMeta();
        meta.displayName(Component.text("§b§lDifficulté de la grille"));
        itemLore = new ArrayList<>();
        itemLore.add(Component.text("§7>> Clique pour modifier"));
        meta.lore(itemLore);
        item.setItemMeta(meta);
        gui.setItem(12, item);

        item = new ItemStack(Material.CRAFTING_TABLE);
        meta = item.getItemMeta();
        meta.displayName(Component.text("§b§lMode de jeu"));
        itemLore = new ArrayList<>();
        if (Bingo.getGame().getModeJeu() == Game.ModeJeu.CLASSIC) {
            itemLore.add(Component.text("§6§l>> §r§eClassic"));
            itemLore.add(Component.text("§7Duel"));
            itemLore.add(Component.text("§7Handicap"));
        } else if (Bingo.getGame().getModeJeu() == Game.ModeJeu.DUEL) {
            Bingo.getGame().setModeVictoire(Game.ModeVictoire.DEFIS);
            itemLore.add(Component.text("§7Classic"));
            itemLore.add(Component.text("§6§l>> §r§eDuel"));
            itemLore.add(Component.text("§7Handicap"));
        } else {
            Bingo.getGame().setModeVictoire(Game.ModeVictoire.BINGO);
            itemLore.add(Component.text("§7Classic"));
            itemLore.add(Component.text("§7Duel"));
            itemLore.add(Component.text("§6§l>> §r§eHandicap"));
        }
        meta.lore(itemLore);
        item.setItemMeta(meta);
        gui.setItem(13, item);

        item = new ItemStack(Material.REDSTONE);
        meta = item.getItemMeta();
        meta.displayName(Component.text("§b§lCondition de victoire"));
        itemLore = new ArrayList<>();
        itemLore.add(Component.text("§7>> Clique pour modifier"));
        meta.lore(itemLore);
        item.setItemMeta(meta);
        gui.setItem(14, item);

        if (Bingo.getGame().isDefiBonus()) {
            item = new ItemStack(Material.GLOWSTONE);
            meta = item.getItemMeta();
            itemLore = new ArrayList<>();
            itemLore.add(Component.text("§e§lOn §r§7/ Off"));
        } else {
            item = new ItemStack(Material.REDSTONE_LAMP);
            meta = item.getItemMeta();
            itemLore = new ArrayList<>();
            itemLore.add(Component.text("§7On /§e§l Off"));
        }
        meta.displayName(Component.text("§b§lEvent défis bonus"));
        meta.lore(itemLore);
        item.setItemMeta(meta);
        gui.setItem(15, item);

        ItemStack resetBlock = new ItemStack(Material.REDSTONE_BLOCK);
        ItemMeta resetMeta = resetBlock.getItemMeta();
        resetMeta.displayName(Component.text("§c§lReset"));
        resetBlock.setItemMeta(resetMeta);
        gui.setItem(16, resetBlock);

        player.openInventory(gui);
    }

    private void openGuiTeams(Player player) {
        ItemStack plus = SkullCustom.getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjA1NmJjMTI0NGZjZmY5OTM0NGYxMmFiYTQyYWMyM2ZlZTZlZjZlMzM1MWQyN2QyNzNjMTU3MjUzMWYifX19");
        ItemMeta plusMeta = plus.getItemMeta();
        plusMeta.displayName(Component.text("§a+"));
        plus.setItemMeta(plusMeta);
        guiTeams.setItem(3, plus);
        guiTeams.setItem(5, plus);
        ItemStack moins = SkullCustom.getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGU0YjhiOGQyMzYyYzg2NGUwNjIzMDE0ODdkOTRkMzI3MmE2YjU3MGFmYmY4MGMyYzViMTQ4Yzk1NDU3OWQ0NiJ9fX0=");
        ItemMeta moinsMeta = moins.getItemMeta();
        moinsMeta.displayName(Component.text("§c-"));
        moins.setItemMeta(moinsMeta);
        guiTeams.setItem(21, moins);
        guiTeams.setItem(23, moins);
        ItemStack nombreTeams = new ItemStack(Material.DIAMOND_HORSE_ARMOR, Team.getNbTeams());

        ItemMeta nombreTeamsMeta = nombreTeams.getItemMeta();
        nombreTeamsMeta.displayName(Component.text("§bNombre de teams"));
        nombreTeams.setItemMeta(nombreTeamsMeta);
        guiTeams.setItem(12, nombreTeams);
        ItemStack nombreJoueursTeams = new ItemStack(Material.PUFFERFISH, Team.getNbPlayerTeams());
        ItemMeta nombreJoueursTeamsMeta = nombreJoueursTeams.getItemMeta();
        nombreJoueursTeamsMeta.displayName(Component.text("§bNombre de joueurs"));
        nombreJoueursTeams.setItemMeta(nombreJoueursTeamsMeta);
        guiTeams.setItem(14, nombreJoueursTeams);
        ItemStack back = new ItemStack(Material.BARRIER);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.displayName(Component.text("§cRetour"));
        back.setItemMeta(backMeta);
        guiTeams.setItem(26, back);
        player.openInventory(guiTeams);
    }

    private void openGuiDifficult(Player player) {
        ItemStack plus = SkullCustom.getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjA1NmJjMTI0NGZjZmY5OTM0NGYxMmFiYTQyYWMyM2ZlZTZlZjZlMzM1MWQyN2QyNzNjMTU3MjUzMWYifX19");
        ItemMeta plusMeta = plus.getItemMeta();
        plusMeta.displayName(Component.text("§a+"));
        plus.setItemMeta(plusMeta);
        guiDifficult.setItem(1, plus);
        guiDifficult.setItem(3, plus);
        guiDifficult.setItem(5, plus);
        guiDifficult.setItem(7, plus);
        ItemStack moins = SkullCustom.getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGU0YjhiOGQyMzYyYzg2NGUwNjIzMDE0ODdkOTRkMzI3MmE2YjU3MGFmYmY4MGMyYzViMTQ4Yzk1NDU3OWQ0NiJ9fX0=");
        ItemMeta moinsMeta = moins.getItemMeta();
        moinsMeta.displayName(Component.text("§c-"));
        moins.setItemMeta(moinsMeta);
        guiDifficult.setItem(19, moins);
        guiDifficult.setItem(21, moins);
        guiDifficult.setItem(23, moins);
        guiDifficult.setItem(25, moins);
        ItemStack easy = new ItemStack(Material.STRUCTURE_VOID);
        ItemStack medium = new ItemStack(Material.STRUCTURE_VOID);
        ItemStack hard = new ItemStack(Material.STRUCTURE_VOID);
        ItemStack extreme = new ItemStack(Material.STRUCTURE_VOID);

        if (Challenge.getMaxEasy() > 0) {
            easy = new ItemStack(Material.COAL, Challenge.getMaxEasy());
        }
        if (Challenge.getMaxMedium() > 0) {
            medium = new ItemStack(Material.COPPER_INGOT, Challenge.getMaxMedium());
        }
        if (Challenge.getMaxHard() > 0) {
            hard = new ItemStack(Material.AMETHYST_SHARD, Challenge.getMaxHard());
        }
        if (Challenge.getMaxExtreme() > 0) {
            extreme = new ItemStack(Material.NETHERITE_SCRAP, Challenge.getMaxExtreme());
        }
        // TODO : REFAIRE APRES DEFIS FAIT

        ItemMeta easyMeta = easy.getItemMeta();
        easyMeta.displayName(Component.text("§8Défi(s) facile(s)"));
        ArrayList<Component> easyLore = new ArrayList<>();
        easyLore.add(Component.text("§7[ " + Challenge.getMaxEasy() + " / " + Challenge.getNbEasy() + " ]"));
        easyMeta.lore(easyLore);
        easy.setItemMeta(easyMeta);
        ItemMeta mediumMeta = medium.getItemMeta();
        mediumMeta.displayName(Component.text("§6Défi(s) moyen(s)"));
        ArrayList<Component> mediumLore = new ArrayList<>();
        mediumLore.add(Component.text("§7[ " + Challenge.getMaxMedium() + " / " + Challenge.getNbMedium() + " ]"));
        mediumMeta.lore(mediumLore);
        medium.setItemMeta(mediumMeta);
        ItemMeta hardMeta = hard.getItemMeta();
        hardMeta.displayName(Component.text("§dDéfi(s) dur(s)"));
        ArrayList<Component> hardLore = new ArrayList<>();
        hardLore.add(Component.text("§7[ " + Challenge.getMaxHard() + " / " + Challenge.getNbHard() + " ]"));
        hardMeta.lore(hardLore);
        hard.setItemMeta(hardMeta);
        ItemMeta extremeMeta = extreme.getItemMeta();
        extremeMeta.displayName(Component.text("§4Défi(s) extrême(s)"));
        ArrayList<Component> extremeLore = new ArrayList<>();
        extremeLore.add(Component.text("§7[ " + Challenge.getMaxExtreme() + " / " + Challenge.getNbExtreme() + " ]"));
        extremeMeta.lore(extremeLore);
        extreme.setItemMeta(extremeMeta);
        // TODO : PAREIL

        guiDifficult.setItem(10, easy);
        guiDifficult.setItem(12, medium);
        guiDifficult.setItem(14, hard);
        guiDifficult.setItem(16, extreme);
        ItemStack back = new ItemStack(Material.BARRIER);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.displayName(Component.text("§cRetour"));
        back.setItemMeta(backMeta);
        guiDifficult.setItem(26, back);
        player.openInventory(guiDifficult);
    }

    private void openGuiVictoire(Player player) {
        ItemStack plus = SkullCustom.getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjA1NmJjMTI0NGZjZmY5OTM0NGYxMmFiYTQyYWMyM2ZlZTZlZjZlMzM1MWQyN2QyNzNjMTU3MjUzMWYifX19");
        ItemMeta plusMeta = plus.getItemMeta();
        plusMeta.displayName(Component.text("§a+"));
        plus.setItemMeta(plusMeta);
        guiVictoire.setItem(3, plus);
        ItemStack moins = SkullCustom.getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGU0YjhiOGQyMzYyYzg2NGUwNjIzMDE0ODdkOTRkMzI3MmE2YjU3MGFmYmY4MGMyYzViMTQ4Yzk1NDU3OWQ0NiJ9fX0=");
        ItemMeta moinsMeta = moins.getItemMeta();
        moinsMeta.displayName(Component.text("§c-"));
        moins.setItemMeta(moinsMeta);
        guiVictoire.setItem(21, moins);

        ItemStack nombreBingos = new ItemStack(Material.SPECTRAL_ARROW, Bingo.getGame().getNbreBingoForWin());
        ItemMeta nombreBingosMeta = nombreBingos.getItemMeta();
        nombreBingosMeta.displayName(Component.text("§b§lNombre de bingos"));
        nombreBingos.setItemMeta(nombreBingosMeta);
        guiVictoire.setItem(12, nombreBingos);

        ItemStack winMode = new ItemStack(Material.TARGET);
        ItemMeta winMeta = winMode.getItemMeta();
        winMeta.displayName(Component.text("§b§lMode de victoire"));
        ArrayList<Component> winLore = new ArrayList<>();
        Game game = Bingo.getGame();
        if (game.getModeJeu() == Game.ModeJeu.DUEL) {
            winLore.add(Component.text("§7Bingos /§e Défis"));
            winLore.add(Component.text("§cLe mode de jeu empêche la"));
            winLore.add(Component.text("§cmodification du mode de victoire"));
        } else if (game.getModeJeu() == Game.ModeJeu.HANDICAP) {
            winLore.add(Component.text("§eBingos§7 / Défis"));
            winLore.add(Component.text("§cLe mode de jeu empêche la"));
            winLore.add(Component.text("§cmodification du mode de victoire"));
        } else {
            if (game.getModeVictoire() == Game.ModeVictoire.BINGO) {
                winLore.add(Component.text("§eBingos§7 / Défis"));
            } else {
                winLore.add(Component.text("§7Bingos /§e Défis"));
            }
        }
        winMeta.lore(winLore);
        winMode.setItemMeta(winMeta);
        guiVictoire.setItem(14, winMode);
        ItemStack back = new ItemStack(Material.BARRIER);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.displayName(Component.text("§cRetour"));
        back.setItemMeta(backMeta);
        guiVictoire.setItem(26, back);
        player.openInventory(guiVictoire);
    }

    @EventHandler
    public void clickItems(PlayerInteractEvent e) {
        if (Bingo.getGame().getEtat() == Game.Etat.SETUP) {
            if (e.getMaterial() == Material.COMPARATOR) {
                openGui(e.getPlayer());
            }
        }
    }

    // TODO : retirer couleur dans le tab quand change nbTeams ou nbJoueurTeam
    @EventHandler
    private void inventoryClick(InventoryClickEvent e) {
        if (Bingo.getGame().getEtat() == Game.Etat.SETUP) {
            Inventory clickedInventory = e.getClickedInventory();
            if (clickedInventory == gui) {
                Material currentItem = e.getCurrentItem().getType();
                Player player = (Player) e.getWhoClicked();
                Game game = Bingo.getGame();
                if (currentItem == Material.IRON_BLOCK) {
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_WORK_LIBRARIAN, 500.0f, 1.0f);
                    game.setModeAffichage(Game.ModeAffichage.COMPETITION);
                    openGui(player);
                } else if (currentItem == Material.CRAFTING_TABLE) {
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_WORK_LIBRARIAN, 500.0f, 1.0f);
                    if (game.getModeJeu() == Game.ModeJeu.CLASSIC) {
                        game.setModeJeu(Game.ModeJeu.DUEL);
                        openGui(player);
                    } else if (game.getModeJeu() == Game.ModeJeu.DUEL) {
                        game.setModeJeu(Game.ModeJeu.HANDICAP);
                        openGui(player);
                    } else {
                        game.setModeJeu(Game.ModeJeu.CLASSIC);
                        openGui(player);
                    }
                } else if (currentItem == Material.WHITE_BANNER) {
                    openGuiTeams((Player) e.getWhoClicked());
                } else if (currentItem == Material.REDSTONE_LAMP) {
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_WORK_LIBRARIAN, 500.0f, 1.0f);
                    game.setDefiBonus(true);
                    openGui(player);

                } else if (currentItem == Material.NETHERITE_BLOCK) {
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_WORK_LIBRARIAN, 500.0f, 1.0f);
                    game.setModeAffichage(Game.ModeAffichage.CHILL);
                    openGui(player);
                } else if (currentItem == Material.GLOWSTONE) {
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_WORK_LIBRARIAN, 500.0f, 1.0f);
                    game.setDefiBonus(false);
                    openGui(player);
                } else if (currentItem == Material.CAULDRON) {
                    openGuiDifficult(player);
                } else if (currentItem == Material.REDSTONE) {
                    openGuiVictoire(player);
                } else if (currentItem == Material.REDSTONE_BLOCK) {
                    //game.resetSettings();
                    // TODO : faire méthode reset settings
                    player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_BREAK, 500.0f, 1.0f);
                    openGui(player);
                }
            } else if (clickedInventory == guiDifficult) {
                Material currentItem = e.getCurrentItem().getType();
                Player player = (Player) e.getWhoClicked();
                int cursor = e.getSlot();
                if (currentItem == Material.BARRIER) {
                    openGui(player);
                }

                else if (cursor == 1) {
                    if (Challenge.verifSettingsToHigh()) {
                        Challenge.setMaxEasy(Challenge.getMaxEasy() + 1);
                        openGuiDifficult(player);
                    }
                } else if (cursor == 3) {
                    if (Challenge.verifSettingsToHigh()) {
                        Challenge.setMaxMedium(Challenge.getMaxMedium() + 1);
                        openGuiDifficult(player);
                    }
                } else if (cursor == 5) {
                    if (Challenge.verifSettingsToHigh()) {
                        if (Challenge.getNbHard() < Challenge.getMaxHard()) {
                            Challenge.setMaxHard(Challenge.getMaxHard() + 1);
                            openGuiDifficult(player);
                        }
                    }
                } else if (cursor == 7) {
                    if (Challenge.verifSettingsToHigh()) {
                        if (Challenge.getNbExtreme() < Challenge.getMaxExtreme()) {
                            Challenge.setMaxExtreme(Challenge.getMaxExtreme() + 1);
                            openGuiDifficult(player);
                        }

                    }
                } else if (cursor == 19) {
                    if (Challenge.getMaxEasy() > 0) {
                        Challenge.setMaxEasy(Challenge.getMaxEasy() - 1);
                        openGuiDifficult(player);
                    }
                } else if (cursor == 21) {
                    if (Challenge.getMaxMedium() > 0) {
                        Challenge.setMaxMedium(Challenge.getMaxMedium() - 1);
                        openGuiDifficult(player);
                    }
                } else if (cursor == 23) {
                    if (Challenge.getMaxHard() > 0) {
                        Challenge.setMaxHard(Challenge.getMaxHard() - 1);
                        openGuiDifficult(player);
                    }
                } else if (cursor == 25) {
                    if (Challenge.getMaxExtreme() > 0) {
                        Challenge.setMaxExtreme(Challenge.getMaxExtreme() - 1);
                        openGuiDifficult(player);
                    }
                }
                // TODO : refaire après défis

            } else if (clickedInventory == guiVictoire) {
                Material currentItem = e.getCurrentItem().getType();
                Player player = (Player) e.getWhoClicked();
                int cursor = e.getSlot();
                if (currentItem == Material.BARRIER) {
                    openGui(player);
                } else if (currentItem == Material.TARGET) {
                    Game game = Bingo.getGame();
                    if (!(game.getModeJeu() == Game.ModeJeu.DUEL) && !(game.getModeJeu() == Game.ModeJeu.HANDICAP)) {
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_WORK_LIBRARIAN, 500.0f, 1.0f);
                        if (game.getModeVictoire() == Game.ModeVictoire.BINGO) {
                            game.setModeVictoire(Game.ModeVictoire.DEFIS);
                            openGuiVictoire(player);
                        } else {
                            game.setModeVictoire(Game.ModeVictoire.BINGO);
                            openGuiVictoire(player);
                        }
                    }
                } else if (cursor == 3) {
                    if (Bingo.getGame().getNbreBingoForWin() < 10) {
                        Bingo.getGame().setNbreBingoForWin(Bingo.getGame().getNbreBingoForWin() + 1);
                        openGuiVictoire(player);
                    }
                } else if (cursor == 21) {
                    if (Bingo.getGame().getNbreBingoForWin() > 1) {
                        Bingo.getGame().setNbreBingoForWin(Bingo.getGame().getNbreBingoForWin() - 1);
                        openGuiVictoire(player);
                    }
                }
                // TODO : refaire après défis (nbBingos)

            } else if (clickedInventory == guiTeams) {
                Material currentItem = e.getCurrentItem().getType();
                Player player = (Player) e.getWhoClicked();
                if (currentItem == Material.BARRIER) {
                    openGui(player);
                } else if (e.getSlot() == 3) {
                    if (Team.getNbTeams() < 6) {
                        Team.setNbTeams(Team.getNbTeams() + 1);
                        openGuiTeams(player);
                    }
                } else if (e.getSlot() == 5) {
                    if (Team.getNbPlayerTeams() < 10) {
                        Team.setNbPlayerTeams(Team.getNbPlayerTeams() + 1);
                        setChallengePreset(player);
                    }
                } else if (e.getSlot() == 21) {
                    if (Team.getNbTeams() > 2) {
                        Team.setNbTeams(Team.getNbTeams() - 1);
                        openGuiTeams(player);
                    }
                } else if (e.getSlot() == 23) {
                    if (Team.getNbPlayerTeams() > 1) {
                        Team.setNbPlayerTeams(Team.getNbPlayerTeams() - 1);
                        setChallengePreset(player);
                    }
                }
            }
            e.setCancelled(true);
        }
    }

    private void setChallengePreset(Player player) {
        resetColorTab();
        if (Team.getNbPlayerTeams() == 1) {
            Challenge.presetOnePlayer();
        } else if (Team.getNbPlayerTeams() == 2) {
            Challenge.presetTwoPlayers();
        } else if (Team.getNbPlayerTeams() == 3) {
            Challenge.presetThreePlayers();
        } else if (Team.getNbPlayerTeams() == 4) {
            Challenge.presetFourPlayers();
        }
        openGuiTeams(player);
    }

    private void resetColorTab() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.displayName(Component.text(player.getName()));
        }
    }
}
