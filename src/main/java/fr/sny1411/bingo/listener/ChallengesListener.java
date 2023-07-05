package fr.sny1411.bingo.listener;

import fr.sny1411.bingo.utils.Challenge;
import fr.sny1411.bingo.utils.Grid;
import fr.sny1411.bingo.utils.Team;
import fr.sny1411.bingo.utils.Terracota;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.CauldronLevelChangeEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.event.raid.RaidTriggerEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;
import org.spigotmc.event.entity.EntityDismountEvent;
import org.spigotmc.event.entity.EntityMountEvent;

import java.util.Hashtable;
import java.util.List;
import java.util.Objects;

public class ChallengesListener implements Listener {
    private static boolean verifSetOfItems(Inventory inventory, ItemStack... items) {
        for (ItemStack item : items) {
            if (!inventory.containsAtLeast(new ItemStack(item.getType()), item.getAmount())) return false;
        }
        return true;
    }

    private static void realizeChallenge(Player player, String challengeName) {
        Challenge challenge = Grid.getChallenge(Team.getTeam(player), challengeName);
        if (challenge != null) {
            challenge.setRealized(true);
        }
    }

    private static void valideChallenge(Player player, String challengeName) {
        Challenge challenge = Grid.getChallenge(Team.getTeam(player), challengeName);
        if (challenge != null) {
            challenge.setValidated(true);
        }
    }

    private static void verifValideChallenge(Player player, String challengeName) {
        if (Objects.requireNonNull(Grid.getChallenge(Team.getTeam(player), challengeName)).getRealized()) {
            valideChallenge(player, challengeName);
        }
    }

    private static void valideAndRealizeChallenge(Player player, String challengeName) {
        Challenge challenge = Grid.getChallenge(Team.getTeam(player), challengeName);
        if (challenge != null) {
            challenge.setValidated(true);
            challenge.setRealized(true);
        }
    }

    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent e) {
        realizeChallenge(e.getPlayer(), "§d§lSuicide Squad");
    }

    @EventHandler
    private void mobKill(EntityDeathEvent e) {
        LivingEntity entity = e.getEntity();
        Player killer = entity.getKiller();

        if (killer == null) return;
        EntityType entityType = entity.getType();
        switch (entityType) {
            case WITCH:
                realizeChallenge(killer, "§d§lAurevoir Sabrina !");
                break;
            case SLIME:
                realizeChallenge(killer, "§d§lSlime Rancher");
                break;
            case DOLPHIN:
                realizeChallenge(killer, "§d§lFaut pas Flipper");
                break;
            case FOX:
                realizeChallenge(killer, "§d§lWhat does the fox say?");
                break;
            case STRIDER:
                realizeChallenge(killer, "§d§lDestrier des Enfers");
                break;
            case SPIDER:
                realizeChallenge(killer, "§d§lArachnophobe");
                break;
            case ELDER_GUARDIAN:
                realizeChallenge(killer, "§d§lIl est bon mon poisson");
                break;
            case IRON_GOLEM:
                realizeChallenge(killer, "§d§lOptimum prime");
                break;
        }
    }

    private static Hashtable<Player, Integer> nbWolfTame = new Hashtable<>();

    @EventHandler
    private void tameMob(EntityTameEvent e) {
        EntityType entityType = e.getEntityType();
        Player player = (Player) e.getOwner();
        switch (entityType) {
            case WOLF:
                if (nbWolfTame.contains(player)) {
                    realizeChallenge(player, "§d§lWolf gang");
                } else {
                    nbWolfTame.put(player, 1);
                }
                break;
            case CAT:
                realizeChallenge(player, "§d§lNyan Cat");
                break;
            case HORSE:
                realizeChallenge(player, "§d§lLe cheval c'est trop génial");
        }
    }

    @EventHandler
    private void playerConsume(PlayerItemConsumeEvent e) {
        ItemStack item = e.getItem();
        Player player = e.getPlayer();
        switch (e.getItem().getType()) {
            case POTION:
                PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
                if (potionMeta.getBasePotionData().getType() == PotionType.SPEED && (player.getInventory().getItemInOffHand().getType() == Material.BREAD)) {
                    valideAndRealizeChallenge(player, "§d§lTu es un sorcier Harry !");
                }
                break;
        }
    }

    @EventHandler
    private void tntExplode(EntityExplodeEvent e) {
        for (Block block : e.blockList()) {
            if (block.getType() == Material.CRAFTING_TABLE) {
                List<Entity> nears = e.getEntity().getNearbyEntities(50, 50, 50);
                for (Entity entity : nears) {
                    if (entity instanceof Player) {
                        realizeChallenge(((Player) entity).getPlayer(), "§d§lOh no, my table is broken!");
                    }
                }
            }
        }
    }

    @EventHandler
    private void advencementDone(PlayerAdvancementDoneEvent e) {
        Player player = e.getPlayer();
        switch (e.getAdvancement().getKey().getKey()) {
            case "story/enter_the_nether":
                realizeChallenge(player, "§d§lBienvenue en Enfer");
                break;
            case "nether/explore_nether":
                realizeChallenge(player, "§d§lVoyage au bout de l'Enfer");
                break;
            case "nether/obtain_ancient_debris":
                realizeChallenge(player, "§d§lAu fond des profondeurs");
                break;
            case "story/follow_ender_eye":
                realizeChallenge(player, "§d§lEn suivant les yeux...");
                break;
            case "end/root":
                realizeChallenge(player, "§d§lC'est la fin?");
                break;
            case "nether/find_bastion":
                realizeChallenge(player, "§d§lLes mystérieuses cités d'or");
                break;
            case "adventure/bullseye":
                realizeChallenge(player, "§d§lDans le mille");
                break;
            case "adventure/walk_on_powder_snow_with_leather_boots":
                realizeChallenge(player, "§d§lJésus des neiges");
                break;
            case "nether/charge_respawn_anchor":
                realizeChallenge(player, "§d§lChargé à bloc");
                break;
            case "nether/return_to_sender":
                realizeChallenge(player, "§d§lRetour à l'envoyeur");
                break;
            case "nether/trade":
                realizeChallenge(player, "§d§lStonks Industries");
                break;
        }
    }

    @EventHandler
    private void projectileHitMob(ProjectileHitEvent e) {
        if (e.getHitEntity() == null && e.getHitEntity() instanceof Player) return;
        switch (e.getEntity().getType()) {
            case LLAMA_SPIT:
                realizeChallenge((Player) e.getHitEntity(), "§d§lLa plus grosse racaille");
                break;
            case SNOWBALL:
                if (e.getHitEntity().getType() == EntityType.SNOWMAN) {
                    realizeChallenge((Player) e.getEntity().getShooter(), "§d§lCombat d'anthologie");
                }
                break;
        }
    }

    @EventHandler
    private void rideEvent(EntityMountEvent e) {
        Entity entity = e.getMount();
        if (entity.getType() == EntityType.PIG && (entity.getLocation().getY() >= 320 && e.getEntity() instanceof Player)) {
            realizeChallenge((Player) e.getEntity(), "§d§lRedBull donne des ailes");
        }
    }

    @EventHandler
    private void lightningStrike(EntityDamageEvent e) {
        if (e.getCause() == EntityDamageEvent.DamageCause.LIGHTNING && e.getEntity() instanceof Player) {
            realizeChallenge((Player) e.getEntity(), "§d§lCoup de foudre");
        }
    }

    @EventHandler
    private void parrotMount(EntityDismountEvent e) {
        if (e.getEntity() instanceof Parrot && e.getDismounted() instanceof Player) {
            realizeChallenge((Player) e.getDismounted(), "§d§lPirate des Caraïbes");
        }
    }

    @EventHandler
    private void raidTrigger(RaidTriggerEvent e) {
        realizeChallenge(e.getPlayer(), "§d§lNous sommes en guerre");
    }

    @EventHandler
    private void breakBlock(BlockBreakEvent e) {
        Biome biome = e.getBlock().getBiome();
        if (biome == Biome.ICE_SPIKES || biome == Biome.FROZEN_OCEAN || biome == Biome.DEEP_FROZEN_OCEAN && (e.getBlock().getType() == Material.CHAIN)) {
            realizeChallenge(e.getPlayer(), "§d§lLibérée, Délivrée");
        }
    }

    @EventHandler
    private void candleIgnite(BlockIgniteEvent e) {
        if (e.getBlock().getType() == Material.CANDLE) {
            realizeChallenge(e.getPlayer(), "§d§lT'es pas net Baptiste?");
        }
    }

    @EventHandler
    private void piglinTrade(PiglinBarterEvent e) {
        List<Entity> nears = e.getEntity().getNearbyEntities(50, 50, 50);
        for (Entity entity : nears) {
            if (entity instanceof Player) {
                realizeChallenge((Player) entity, "§d§lUne affaire en or");
            }
        }
    }

    @EventHandler
    private void cauldronExtinguish(CauldronLevelChangeEvent e) {
        if (e.getReason() == CauldronLevelChangeEvent.ChangeReason.EXTINGUISH && Objects.requireNonNull(e.getEntity()).getWorld() == Bukkit.getWorlds().get(1)) {
            realizeChallenge((Player) e.getEntity(), "§d§lSéance jacuzzi");
        }
    }

    @EventHandler
    private void exChange(PlayerLevelChangeEvent e) {
        if (e.getPlayer().getLevel() >= 30) {
            realizeChallenge(e.getPlayer(), "§d§lExpérimenté");
        }
    }

    @EventHandler
    private void turtleReproduce(EntityBreedEvent e) {
        if (e.getFather().getType() == EntityType.TURTLE) {
            realizeChallenge((Player) e.getEntity(), "§d§lMichelangelo?");
        }
    }

    @EventHandler
    private void sheepShear(PlayerShearEntityEvent e) {
        if (e.getEntity() instanceof Sheep) {
            Sheep sheep = (Sheep) e.getEntity();
            if (sheep.getColor() == DyeColor.PURPLE) {
                realizeChallenge(e.getPlayer(), "§d§lTricot");
            }
        }
    }

    @EventHandler
    private void interactEvent(PlayerInteractEvent e) {
        Block block = e.getClickedBlock();
        if (block != null && block.getType() == Material.COMPOSTER) {
            BlockData blockData = block.getBlockData();
            Levelled level = (Levelled) blockData;
            if (level.getLevel() == level.getMaximumLevel()) {
                realizeChallenge(e.getPlayer(), "§d§lRecyclage");
            }
        }
    }

    @EventHandler
    private void playerInteractMob(PlayerInteractEntityEvent e) {
        // TODO : FINIR BATMAN
    }

    public void verifChallenge(Player player, String challengeName) {
        PlayerInventory playerInventory = player.getInventory();
        switch (challengeName) {
            case "§d§lSuicide Squad":
            case "§d§lAurevoir Sabrina !":
            case "§d§lSlime Rancher":
            case "§d§lFaut pas Flipper":
            case "§d§lWolf gang":
            case "§d§lNyan Cat":
            case "§d§lWhat does the fox say?":
            case "§d§lDestrier des Enfers":
            case "§d§lTu es un sorcier Harry !":
            case "§d§lBienvenue en Enfer":
            case "§d§lArachnophobe":
            case "§d§lVoyage au bout de l'Enfer":
            case "§d§lAu fond des profondeurs":
            case "§d§lEn suivant les yeux...":
            case "§d§lC'est la fin?":
            case "§d§lLa plus grosse racaille":
            case "§d§lRedBull donne des ailes":
            case "§d§lLes mystérieuses cités d'or":
            case "§d§lDans le mille":
            case "§d§lCoup de foudre":
            case "§d§lNous sommes en guerre":
            case "§d§lLibérée, Délivrée":
            case "§d§lCombat d'anthologie":
            case "§d§lT'es pas net Baptiste?":
            case "§d§lJésus des neiges":
            case "§d§lUne affaire en or":
            case "§d§lSéance jacuzzi":
            case "§d§lOh no, my table is broken!":
            case "§d§lRetour à l'envoyeur":
            case "§d§lExpérimenté":
            case "§d§lMichelangelo?":
            case "§d§lStonks Industries":
            case "§d§lIl est bon mon poisson":
            case "§d§lTricot":
            case "§d§lOptimum prime":
            case "§d§lRecyclage":
                verifValideChallenge(player, challengeName);
                break;
            case "§d§lBoulets de canon":
                if (playerInventory.containsAtLeast(new ItemStack(Material.FIRE_CHARGE), 6)) {
                    valideAndRealizeChallenge(player, challengeName);
                }
                break;
            case "§d§lDéforestation":
                if (playerInventory.containsAtLeast(new ItemStack(Material.ACACIA_LOG), 64)) {
                    valideAndRealizeChallenge(player, challengeName);
                }
                break;
            case "§d§lEt ça fait bim bam boom":
                if (playerInventory.containsAtLeast(new ItemStack(Material.TNT), 5)) {
                    valideAndRealizeChallenge(player, challengeName);
                }
                break;
            case "§d§lTop Chef":
                if (playerInventory.containsAtLeast(new ItemStack(Material.CAKE), 1)) {
                    valideAndRealizeChallenge(player, challengeName);
                }
                break;
            case "§d§lForgeron":
                if (playerInventory.containsAtLeast(new ItemStack(Material.ANVIL), 1)) {
                    valideAndRealizeChallenge(player, challengeName);
                }
                break;
            case "§d§lLa dame du CDI":
                if (playerInventory.containsAtLeast(new ItemStack(Material.BOOK), 16)) {
                    valideAndRealizeChallenge(player, challengeName);
                }
                break;
            case "§d§lVers l'infini et au-delà":
                if (player.getLocation().getY() >= 320) {
                    valideAndRealizeChallenge(player, challengeName);
                }
                break;
            case "§d§lIngénieur informaticien":
                if (playerInventory.containsAtLeast(new ItemStack(Material.REDSTONE_BLOCK), 16)) {
                    valideAndRealizeChallenge(player, challengeName);
                }
                break;
            case "§d§lAlgoculteur":
                if (playerInventory.containsAtLeast(new ItemStack(Material.DRIED_KELP_BLOCK), 16)) {
                    valideAndRealizeChallenge(player, challengeName);
                }
                break;
            case "§d§lHalloween":
                if (playerInventory.containsAtLeast(new ItemStack(Material.JACK_O_LANTERN), 1)) {
                    valideAndRealizeChallenge(player, challengeName);
                }
                break;
            case "§d§lCa colle...":
                if (playerInventory.containsAtLeast(new ItemStack(Material.HONEY_BOTTLE), 1)) {
                    valideAndRealizeChallenge(player, challengeName);
                }
                break;
            case "§d§lEtrange pomme d'amour":
                if (playerInventory.containsAtLeast(new ItemStack(Material.GOLDEN_APPLE), 1)) {
                    valideAndRealizeChallenge(player, challengeName);
                }
                break;
            case "§d§lMcDonald's":
                if (playerInventory.containsAtLeast(new ItemStack(Material.POISONOUS_POTATO), 1)) {
                    valideAndRealizeChallenge(player, challengeName);
                }
                break;
            case "§d§lFarming Simulator":
                if (playerInventory.containsAtLeast(new ItemStack(Material.HAY_BLOCK), 32)) {
                    valideAndRealizeChallenge(player, challengeName);
                }
                break;
            case "§d§lCauchemar en cuisine":
                if (playerInventory.containsAtLeast(new ItemStack(Material.SUSPICIOUS_STEW), 1)) {
                    valideAndRealizeChallenge(player, challengeName);
                }
                break;
            case "§d§lFée clocharde":
                if (playerInventory.containsAtLeast(new ItemStack(Material.FEATHER), 31)) {
                    valideAndRealizeChallenge(player, challengeName);
                }
                break;
            case "§d§lAddict des seaux":
                if (verifSetOfItems(playerInventory, new ItemStack(Material.LAVA_BUCKET),
                        new ItemStack(Material.WATER_BUCKET),
                        new ItemStack(Material.MILK_BUCKET))) {
                    valideAndRealizeChallenge(player, challengeName);
                }
                break;
            case "§d§lTout est bon dans le cochon":
                if (playerInventory.containsAtLeast(new ItemStack(Material.PORKCHOP), 22)) {
                    valideAndRealizeChallenge(player, challengeName);
                }
                break;
            case "§d§lTu es grosse Mélissandre":
                if (playerInventory.containsAtLeast(new ItemStack(Material.PUMPKIN_PIE), 1)) {
                    valideAndRealizeChallenge(player, challengeName);
                }
                break;
            case "§d§lY'a du bambou là !":
                if (playerInventory.containsAtLeast(new ItemStack(Material.BAMBOO), 64)) {
                    valideAndRealizeChallenge(player, challengeName);
                }
                break;
            case "§d§lCollectionneur":
                if (verifSetOfItems(playerInventory, new ItemStack(Material.COAL_BLOCK),
                        new ItemStack(Material.REDSTONE_BLOCK),
                        new ItemStack(Material.LAPIS_BLOCK),
                        new ItemStack(Material.GOLD_BLOCK),
                        new ItemStack(Material.IRON_BLOCK),
                        new ItemStack(Material.DIAMOND_BLOCK),
                        new ItemStack(Material.COPPER_BLOCK),
                        new ItemStack(Material.EMERALD_BLOCK),
                        new ItemStack(Material.QUARTZ_BLOCK))) {
                    valideAndRealizeChallenge(player, challengeName);
                }
                break;
            case "§d§lCoffre du néant":
                if (playerInventory.containsAtLeast(new ItemStack(Material.ENDER_CHEST), 1)) {
                    valideAndRealizeChallenge(player, challengeName);
                }
                break;
            case "§d§lTrésor enfoui":
                if (playerInventory.containsAtLeast(new ItemStack(Material.HEART_OF_THE_SEA), 1)) {
                    valideAndRealizeChallenge(player, challengeName);
                }
                break;
            case "§d§lIndiana Jones":
                Biome biome = player.getLocation().getBlock().getBiome();
                if (biome == Biome.JUNGLE || biome == Biome.BAMBOO_JUNGLE || biome == Biome.SPARSE_JUNGLE) {
                    valideAndRealizeChallenge(player, challengeName);
                }
                break;
            case "§d§lMerlin l'enchanteur":
                if (playerInventory.containsAtLeast(new ItemStack(Material.ENCHANTING_TABLE), 1)) {
                    valideAndRealizeChallenge(player, challengeName);
                }
                break;
            case "§d§lJ'ai le bâton en feu !":
                if (playerInventory.containsAtLeast(new ItemStack(Material.BLAZE_ROD), 2)) {
                    valideAndRealizeChallenge(player, challengeName);
                }
                break;
            case "§d§lSortez les mouchoirs":
                if (playerInventory.containsAtLeast(new ItemStack(Material.CRYING_OBSIDIAN), 1)) {
                    valideAndRealizeChallenge(player, challengeName);
                }
                break;
            case "§d§lThe Walking Dead":
                if (player.getStatistic(Statistic.KILL_ENTITY, EntityType.ZOMBIE) >= 29) {
                    valideAndRealizeChallenge(player, challengeName);
                }
                break;
            case "§d§lSOS Fantômes":
                if (playerInventory.containsAtLeast(new ItemStack(Material.PHANTOM_MEMBRANE), 1)) {
                    valideAndRealizeChallenge(player, challengeName);
                }
                break;
            case "§d§lJe veux tes yeux":
                if (playerInventory.containsAtLeast(new ItemStack(Material.ENDER_EYE), 3)) {
                    valideAndRealizeChallenge(player, challengeName);
                }
                break;
            case "§d§lChâteau rouge":
                if (playerInventory.containsAtLeast(new ItemStack(Material.RED_NETHER_BRICKS), 17)) {
                    valideAndRealizeChallenge(player, challengeName);
                }
                break;
            case "§d§lOld Town Road":
                if (playerInventory.containsAtLeast(new ItemStack(Material.SADDLE), 1)) {
                    valideAndRealizeChallenge(player, challengeName);
                }
                break;
            case "§d§lTerre colorée":
                if (Terracota.getNbTerracota(playerInventory) >= 8) {
                    valideAndRealizeChallenge(player, challengeName);
                }
                break;
            case "§d§lBob l'éponge cubique":
                if (playerInventory.containsAtLeast(new ItemStack(Material.SPONGE), 1)) {
                    valideAndRealizeChallenge(player, challengeName);
                }
                break;
            case "§d§lMayo l'abeille":
                if (playerInventory.containsAtLeast(new ItemStack(Material.HONEY_BLOCK), 1)) {
                    valideAndRealizeChallenge(player, challengeName);
                }
                break;
            case "§d§lSous l'océan":
                if (playerInventory.containsAtLeast(new ItemStack(Material.TROPICAL_FISH_BUCKET), 1)) {
                    valideAndRealizeChallenge(player, challengeName);
                }
                break;
            case "§d§lAssurance vie":
                if (playerInventory.containsAtLeast(new ItemStack(Material.TOTEM_OF_UNDYING), 1)) {
                    valideAndRealizeChallenge(player, challengeName);
                }
                break;
            case "§d§lBienvenue au pays des Schtroumpfs":
                if (player.getLocation().getBlock().getBiome() == Biome.MUSHROOM_FIELDS) {
                    valideAndRealizeChallenge(player, challengeName);
                }
                break;
            case "§d§lQui dit mieux?":
                if (playerInventory.containsAtLeast(new ItemStack(Material.NETHERITE_INGOT), 1)) {
                    valideAndRealizeChallenge(player, challengeName);
                }
                break;
            case "§d§lMon précieux":
                if (playerInventory.containsAtLeast(new ItemStack(Material.AMETHYST_BLOCK), 16)) {
                    valideAndRealizeChallenge(player, challengeName);
                }
                break;
            case "§d§lUn bout de Cerbère":
                if (playerInventory.containsAtLeast(new ItemStack(Material.WITHER_SKELETON_SKULL), 1)) {
                    valideAndRealizeChallenge(player, challengeName);
                }
                break;
            case "§d§lDes paillettes dans ma vie Kévin":
                if (player.getStatistic(Statistic.KILL_ENTITY, EntityType.GLOW_SQUID) >= 3) {
                    valideAndRealizeChallenge(player, challengeName);
                }
                break;
            case "§d§lBoules scintillantes":
                if (playerInventory.containsAtLeast(new ItemStack(Material.GLOW_BERRIES), 5)) {
                    valideAndRealizeChallenge(player, challengeName);
                }
                break;
            case "§d§lCa pique...":
                if (playerInventory.containsAtLeast(new ItemStack(Material.DRIPSTONE_BLOCK), 20)) {
                    valideAndRealizeChallenge(player, challengeName);
                }
                break;
            case "§d§lPoséidon":
                for (ItemStack item : playerInventory.getStorageContents()) {
                    if (item != null && item.getType() == Material.TRIDENT) {
                        valideAndRealizeChallenge(player, challengeName);
                        break;
                    }
                }
                break;
            case "§d§lPlutôt Krokmou ou Spyro?":
                ItemStack helmet = playerInventory.getHelmet();
                if (helmet != null && helmet.getType() == Material.DRAGON_HEAD) {
                    valideAndRealizeChallenge(player, challengeName);
                }
                break;
            case "§d§lHallucinogènes":
                if (verifSetOfItems((playerInventory), new ItemStack(Material.BROWN_MUSHROOM),
                        new ItemStack(Material.RED_MUSHROOM),
                        new ItemStack(Material.CRIMSON_FUNGUS),
                        new ItemStack(Material.WARPED_FUNGUS))) {
                    valideAndRealizeChallenge(player, challengeName);
                }
                break;
            case "§d§lNouvelle énergie":
                if (playerInventory.containsAtLeast(new ItemStack(Material.DAYLIGHT_DETECTOR), 1)) {
                    valideAndRealizeChallenge(player, challengeName);
                }
                break;
            case "§d§lLady Gaga":
                helmet = playerInventory.getHelmet();
                ItemStack chestplate = playerInventory.getChestplate();
                ItemStack leggings = playerInventory.getLeggings();
                ItemStack boots = playerInventory.getBoots();

                if (helmet != null && helmet.getType() == Material.GOLDEN_HELMET &&
                        (chestplate != null && chestplate.getType() == Material.GOLDEN_CHESTPLATE &&
                                (leggings != null && leggings.getType() == Material.GOLDEN_LEGGINGS &&
                                        (boots != null && boots.getType() == Material.GOLDEN_BOOTS)))) {
                    valideAndRealizeChallenge(player, challengeName);
                }
                break;
            case "§d§lSac à dos, sac à dos":
                if (playerInventory.containsAtLeast(new ItemStack(Material.SHULKER_BOX), 1)) {
                    valideAndRealizeChallenge(player, challengeName);
                }
                break;
        }
    }
}
