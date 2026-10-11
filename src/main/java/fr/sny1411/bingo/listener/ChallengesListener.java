package fr.sny1411.bingo.listener;

import com.destroystokyo.paper.event.entity.TurtleGoHomeEvent;
import fr.sny1411.bingo.Bingo;
import fr.sny1411.bingo.utils.*;
import fr.sny1411.bingo.utils.items.collections.Candle;
import fr.sny1411.bingo.utils.items.collections.Shulker;
import fr.sny1411.bingo.utils.items.collections.Terracota;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Levelled;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.CauldronLevelChangeEvent;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.event.raid.RaidTriggerEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

public class ChallengesListener implements Listener {
    private static boolean verifSetOfItems(Inventory inventory, ItemStack... items) {
        for (ItemStack item : items) {
            if (!inventory.containsAtLeast(new ItemStack(item.getType()), item.getAmount())) return false;

        }
        return true;
    }

    private static void realizeChallenge(Player player, ChallengeId challengeId) {
        Challenge challenge = Grid.getChallenge(Team.getTeam(player), challengeId);
        if (challenge != null && !challenge.getRealized()) {
            challenge.setRealized(true);
        }
    }

    private static void valideChallenge(Player player, ChallengeId challengeId) {
        Challenge challenge = Grid.getChallenge(Team.getTeam(player), challengeId);
        if (challenge != null && !challenge.getValidated()) {
            challenge.setValidated(true);
            Team teamPlayer = Team.getTeam(player);
            Text.validMessage(teamPlayer, challenge.getName());
            Bingo.getGame().getTeamsScore().get(teamPlayer).addChallenge(challenge);
        }
    }

    private static boolean verifValideChallenge(Player player, ChallengeId challengeId) {
        if (Boolean.TRUE.equals(Objects.requireNonNull(Grid.getChallenge(Team.getTeam(player), challengeId)).getRealized())) {
            valideChallenge(player, challengeId);
            return true;
        }
        return false;
    }

    public static void valideAndRealizeChallenge(Player player, ChallengeId challengeId) {
        valideAndRealizeChallenge(Team.getTeam(player), challengeId);
    }

    public static void valideAndRealizeChallenge(Team team, ChallengeId challengeId) {
        Challenge challenge = Grid.getChallenge(team, challengeId);
        if (challenge != null && !challenge.getValidated()) {
            challenge.setValidated(true);
            challenge.setRealized(true);
            Text.validMessage(team, challenge.getName());
            Bingo.getGame().getTeamsScore().get(team).addChallenge(challenge);
        }
    }

    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent e) {
        realizeChallenge(e.getPlayer(), ChallengeId.SUICIDE_SQUAD);
    }

    @EventHandler
    private void mobKill(EntityDeathEvent e) {
        LivingEntity entity = e.getEntity();
        Player killer = entity.getKiller();

        if (killer == null) return;
        EntityType entityType = entity.getType();
        switch (entityType) {
            case WITCH:
                realizeChallenge(killer, ChallengeId.AUREVOIR_SABRINA);
                break;
            case SLIME:
                realizeChallenge(killer, ChallengeId.SLIME_RANCHER);
                break;
            case DOLPHIN:
                realizeChallenge(killer, ChallengeId.FAUT_PAS_FLIPPER);
                break;
            case FOX:
                realizeChallenge(killer, ChallengeId.WHAT_DOES_THE_FOX_SAY);
                break;
            case STRIDER:
                realizeChallenge(killer, ChallengeId.DESTRIER_DES_ENFERS);
                break;
            case CAVE_SPIDER:
                realizeChallenge(killer, ChallengeId.ARACHNOPHOBE);
                break;
            case ELDER_GUARDIAN:
                realizeChallenge(killer, ChallengeId.IL_EST_BON_MON_POISSON);
                break;
            case IRON_GOLEM:
                realizeChallenge(killer, ChallengeId.OPTIMUM_PRIME);
                break;
            case SILVERFISH:
                realizeChallenge(killer, ChallengeId.TEMA_LA_TAILLE_DU_RAT);
                break;
            case TURTLE:
                if (e.getEntity().getWorld() == Bukkit.getWorlds().get(1)) {
                    realizeChallenge(killer, ChallengeId.MARIO_CONTRE_BOWSER);
                }
                break;
        }
    }

    private static final Hashtable<Player, Integer> nbWolfTame = new Hashtable<>();

    @EventHandler
    private void tameMob(EntityTameEvent e) {
        EntityType entityType = e.getEntityType();
        Player player = (Player) e.getOwner();
        switch (entityType) {
            case WOLF:
                if (nbWolfTame.containsKey(player)) {
                    realizeChallenge(player, ChallengeId.WOLF_GANG);
                } else {
                    nbWolfTame.put(player, 1);
                }
                break;
            case CAT:
                realizeChallenge(player, ChallengeId.NYAN_CAT);
                break;
            case HORSE:
                realizeChallenge(player, ChallengeId.LE_CHEVAL_C_EST_TROP_GENIAL);
        }
    }

    @EventHandler
    private void playerConsume(PlayerItemConsumeEvent e) {
        ItemStack item = e.getItem();
        Player player = e.getPlayer();
        switch (e.getItem().getType()) {
            case POTION:
                PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
                if (EnumSet.of(PotionType.SWIFTNESS, PotionType.LONG_SWIFTNESS, PotionType.STRONG_SWIFTNESS).contains(potionMeta.getBasePotionType()) && (player.getInventory().getItemInOffHand().getType() == Material.BREAD)) {
                    realizeChallenge(player, ChallengeId.TU_ES_UN_SORCIER_HARRY);
                }
                break;
            case COOKIE:
                realizeChallenge(player, ChallengeId.COOKIE_MONSTER);
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
                        realizeChallenge(((Player) entity).getPlayer(), ChallengeId.OH_NO_MY_TABLE_IS_BROKEN);
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
                realizeChallenge(player, ChallengeId.BIENVENUE_EN_ENFER);
                break;
            case "nether/explore_nether":
                realizeChallenge(player, ChallengeId.VOYAGE_AU_BOUT_DE_L_ENFER);
                break;
            case "nether/obtain_ancient_debris":
                realizeChallenge(player, ChallengeId.AU_FOND_DES_PROFONDEURS);
                break;
            case "story/follow_ender_eye":
                realizeChallenge(player, ChallengeId.EN_SUIVANT_LES_YEUX);
                break;
            case "end/root":
                realizeChallenge(player, ChallengeId.C_EST_LA_FIN);
                break;
            case "nether/find_bastion":
                realizeChallenge(player, ChallengeId.LES_MYSTERIEUSES_CITES_D_OR);
                break;
            case "adventure/bullseye":
                realizeChallenge(player, ChallengeId.DANS_LE_MILLE);
                break;
            case "adventure/walk_on_powder_snow_with_leather_boots":
                realizeChallenge(player, ChallengeId.JESUS_DES_NEIGES);
                break;
            case "nether/charge_respawn_anchor":
                realizeChallenge(player, ChallengeId.CHARGE_A_BLOC);
                break;
            case "nether/return_to_sender":
                realizeChallenge(player, ChallengeId.RETOUR_A_L_ENVOYEUR);
                break;
            case "adventure/trade":
                realizeChallenge(player, ChallengeId.STONKS_INDUSTRIES);
                break;
            case "story/cure_zombie_villager":
                realizeChallenge(player, ChallengeId.DOCTOR_STRANGE);
                break;
            case "adventure/salvage_sherd":
                realizeChallenge(player, ChallengeId.ARCHEOLOGUE);
                break;
            case "adventure/sleep_in_bed":
                realizeChallenge(player, ChallengeId.BONNE_NUIT_LES_PETITS);
                break;
        }
    }

    @EventHandler
    private void projectileHitMob(ProjectileHitEvent e) {
        Bukkit.getLogger().log(Level.INFO, e.getEntity().getType().toString());
        if (e.getHitEntity() == null) return;
        switch (e.getEntity().getType()) {
            case LLAMA_SPIT:
                realizeChallenge((Player) e.getHitEntity(), ChallengeId.LA_PLUS_GROSSE_RACAILLE);
                break;
            case SNOWBALL:
                if (e.getHitEntity().getType() == EntityType.SNOW_GOLEM) {
                    realizeChallenge((Player) e.getEntity().getShooter(), ChallengeId.COMBAT_D_ANTHOLOGIE);
                }
                break;
            case FIREWORK_ROCKET:
                if (e.getHitEntity().getType() == EntityType.PIG) {
                    realizeChallenge((Player) e.getEntity().getShooter(), ChallengeId.C_EST_LA_FETE_DE_TROP);
                }
        }
    }

    @EventHandler
    private void rideEvent(EntityMountEvent e) {
        Entity entity = e.getMount();
        if (entity.getType() == EntityType.PIG && (entity.getLocation().getY() >= 320 && e.getEntity() instanceof Player)) {
            realizeChallenge((Player) e.getEntity(), ChallengeId.REDBULL_DONNE_DES_AILES);
        }
    }

    @EventHandler
    private void lightningStrike(EntityDamageEvent e) {
        if (e.getCause() == EntityDamageEvent.DamageCause.LIGHTNING && e.getEntity() instanceof Player) {
            realizeChallenge((Player) e.getEntity(), ChallengeId.COUP_DE_FOUDRE);
        }
    }

    @EventHandler
    private void raidTrigger(RaidTriggerEvent e) {
        realizeChallenge(e.getPlayer(), ChallengeId.NOUS_SOMMES_EN_GUERRE);
    }

    @EventHandler
    private void breakBlock(BlockBreakEvent e) {
        if (e.getBlock().getType() == Material.IRON_CHAIN) {
            Biome biome = e.getBlock().getBiome();
            if (biome == Biome.ICE_SPIKES || biome == Biome.FROZEN_OCEAN || biome == Biome.DEEP_FROZEN_OCEAN) {
                realizeChallenge(e.getPlayer(), ChallengeId.LIBEREE_DELIVREE);
            }
        } else if (e.getBlock().getType() == Material.SPAWNER) {
            realizeChallenge(e.getPlayer(), ChallengeId.MONSTER_HUNTER);
        }

    }

    @EventHandler
    private void candleIgnite(BlockIgniteEvent e) {
        if (Candle.isCandleItem(e.getBlock().getType())) {
            realizeChallenge(e.getPlayer(), ChallengeId.T_ES_PAS_NET_BAPTISTE);
        }
    }

    @EventHandler
    private void piglinTrade(PiglinBarterEvent e) {
        List<Entity> nears = e.getEntity().getNearbyEntities(50, 50, 50);
        for (Entity entity : nears) {
            if (entity instanceof Player) {
                realizeChallenge((Player) entity, ChallengeId.UNE_AFFAIRE_EN_OR);
            }
        }
    }

    @EventHandler
    private void cauldronExtinguish(CauldronLevelChangeEvent e) {
        if (e.getReason() == CauldronLevelChangeEvent.ChangeReason.EXTINGUISH && Objects.requireNonNull(e.getEntity()).getWorld() == Bukkit.getWorlds().get(1)) {
            realizeChallenge((Player) e.getEntity(), ChallengeId.SEANCE_JACUZZI);
        }
    }

    @EventHandler
    private void exChange(PlayerLevelChangeEvent e) {
        if (e.getPlayer().getLevel() >= 30) {
            realizeChallenge(e.getPlayer(), ChallengeId.EXPERIMENTE);
        }
    }

    @EventHandler
    private void turtleEvent(TurtleGoHomeEvent e) {
        List<Entity> entities = e.getEntity().getNearbyEntities(10,10,10);
        for (Entity entity : entities) {
            if (entity instanceof Player) {
                realizeChallenge((Player) entity,ChallengeId.MICHELANGELO);
            }
        }
    }

    @EventHandler
    private void sheepShear(PlayerShearEntityEvent e) {
        if (e.getEntity() instanceof Sheep) {
            Sheep sheep = (Sheep) e.getEntity();
            if (sheep.getColor() == DyeColor.PURPLE) {
                realizeChallenge(e.getPlayer(), ChallengeId.TRICOT);
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
                realizeChallenge(e.getPlayer(), ChallengeId.RECYCLAGE);
            }
        }
    }

    @EventHandler
    private void playerInteractMob(PlayerInteractEntityEvent e) {
        if (e.getRightClicked().getType() == EntityType.BAT) {
            Bukkit.getLogger().log(Level.INFO, "chauve souris");
            PlayerInventory inv = e.getPlayer().getInventory();
            ItemStack mainHand = inv.getItemInMainHand();
            ItemStack offHand = inv.getItemInOffHand();
            if ((mainHand.getType() == Material.NAME_TAG && Bingo.getPlainSerializer().serialize(mainHand.displayName()).equals("[Batman]")) ||
            (offHand.getType() == Material.NAME_TAG && Bingo.getPlainSerializer().serialize(offHand.displayName()).equals("[Batman]"))) {
                realizeChallenge(e.getPlayer(), ChallengeId.BATMAN);
            }
        }
    }

    @EventHandler
    private void endermanLook(EntityTargetLivingEntityEvent e) {
        if (e.getEntityType() == EntityType.ENDERMAN &&
                e.getTarget() instanceof Player &&
                e.getReason() == EntityTargetEvent.TargetReason.CLOSEST_PLAYER) {
            realizeChallenge(((Player) e.getTarget()), ChallengeId.DUEL_DE_REGARD);
        }
    }

    @EventHandler
    private void mobSpawn(CreatureSpawnEvent e) {
        LivingEntity entity = e.getEntity();
        if (entity.getType() == EntityType.ENDER_DRAGON) {
            World end = Bukkit.getWorlds().get(2);
            Collection<Entity> nears = Objects.requireNonNull(Bukkit.getWorld(end.getName())).getNearbyEntities(new Location(end, 0, 65, 0), 150, 50, 150);
            for (Entity entityNear : nears) {
                if (entityNear instanceof Player) {
                    realizeChallenge(((Player) entityNear), ChallengeId.VIENS_A_MOI_SHENRON);
                }
            }
        } else if (entity.getType() == EntityType.PARROT && e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SHOULDER_ENTITY) {
            List<Entity> proches = e.getEntity().getNearbyEntities(5, 5, 5);
            for (Entity entityNear : proches) {
                if (entityNear instanceof Player) {
                    realizeChallenge((Player) entityNear, ChallengeId.PIRATE_DES_CARAIBES);
                }
            }
        }
    }

    @EventHandler
    private void blockFormEvent(EntityBlockFormEvent e) {
        if (e.getBlock().getType() == Material.WATER && e.getEntity() instanceof Player) {
            realizeChallenge((Player) e.getEntity(), ChallengeId.APPELLE_MOI_MOISE);
        }
    }

    public static boolean verifChallenge(Player player, ChallengeId challengeId) {
        Bukkit.getLogger().log(Level.INFO, "test");
        PlayerInventory playerInventory = player.getInventory();
        Bukkit.getLogger().log(Level.INFO, challengeId.name());
        switch (challengeId) {
            case SUICIDE_SQUAD:
            case AUREVOIR_SABRINA:
            case SLIME_RANCHER:
            case FAUT_PAS_FLIPPER:
            case WOLF_GANG:
            case NYAN_CAT:
            case WHAT_DOES_THE_FOX_SAY:
            case DESTRIER_DES_ENFERS:
            case TU_ES_UN_SORCIER_HARRY:
            case BIENVENUE_EN_ENFER:
            case ARACHNOPHOBE:
            case VOYAGE_AU_BOUT_DE_L_ENFER:
            case AU_FOND_DES_PROFONDEURS:
            case EN_SUIVANT_LES_YEUX:
            case C_EST_LA_FIN:
            case LA_PLUS_GROSSE_RACAILLE:
            case REDBULL_DONNE_DES_AILES:
            case LES_MYSTERIEUSES_CITES_D_OR:
            case DANS_LE_MILLE:
            case COUP_DE_FOUDRE:
            case NOUS_SOMMES_EN_GUERRE:
            case LIBEREE_DELIVREE:
            case COMBAT_D_ANTHOLOGIE:
            case T_ES_PAS_NET_BAPTISTE:
            case JESUS_DES_NEIGES:
            case UNE_AFFAIRE_EN_OR:
            case SEANCE_JACUZZI:
            case OH_NO_MY_TABLE_IS_BROKEN:
            case RETOUR_A_L_ENVOYEUR:
            case EXPERIMENTE:
            case MICHELANGELO:
            case STONKS_INDUSTRIES:
            case IL_EST_BON_MON_POISSON:
            case TRICOT:
            case OPTIMUM_PRIME:
            case RECYCLAGE:
            case BATMAN:
            case C_EST_LA_FETE_DE_TROP:
            case TEMA_LA_TAILLE_DU_RAT:
            case COOKIE_MONSTER:
            case DUEL_DE_REGARD:
            case MONSTER_HUNTER:
            case DOCTOR_STRANGE:
            case VIENS_A_MOI_SHENRON:
            case APPELLE_MOI_MOISE:
            case MARIO_CONTRE_BOWSER:
            case ARCHEOLOGUE:
            case BONNE_NUIT_LES_PETITS:
            case LE_CHEVAL_C_EST_TROP_GENIAL:
            case CHARGE_A_BLOC:
            case PIRATE_DES_CARAIBES:
                return verifValideChallenge(player, challengeId);
            case BOULETS_DE_CANON:
                if (playerInventory.containsAtLeast(new ItemStack(Material.FIRE_CHARGE), 6)) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case DEFORESTATION:
                if (playerInventory.containsAtLeast(new ItemStack(Material.ACACIA_LOG), 64)) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case ET_CA_FAIT_BIM_BAM_BOOM:
                if (playerInventory.containsAtLeast(new ItemStack(Material.TNT), 5)) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case TOP_CHEF:
                if (playerInventory.containsAtLeast(new ItemStack(Material.CAKE), 1)) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case FORGERON:
                if (playerInventory.containsAtLeast(new ItemStack(Material.ANVIL), 1)) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case LA_DAME_DU_CDI:
                if (playerInventory.containsAtLeast(new ItemStack(Material.BOOK), 16)) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case VERS_L_INFINI_ET_AU_DELA:
                if (player.getLocation().getY() >= 320) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case INGENIEUR_INFORMATICIEN:
                if (playerInventory.containsAtLeast(new ItemStack(Material.REDSTONE_BLOCK), 16)) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case ALGOCULTEUR:
                if (playerInventory.containsAtLeast(new ItemStack(Material.DRIED_KELP_BLOCK), 16)) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case HALLOWEEN:
                if (playerInventory.containsAtLeast(new ItemStack(Material.JACK_O_LANTERN), 1)) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case CA_COLLE:
                if (playerInventory.containsAtLeast(new ItemStack(Material.HONEY_BOTTLE), 1)) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case ETRANGE_POMME_D_AMOUR:
                if (playerInventory.containsAtLeast(new ItemStack(Material.GOLDEN_APPLE), 1)) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case MCDONALDS:
                if (playerInventory.containsAtLeast(new ItemStack(Material.POISONOUS_POTATO), 1)) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case FARMING_SIMULATOR:
                if (playerInventory.containsAtLeast(new ItemStack(Material.HAY_BLOCK), 32)) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case CAUCHEMAR_EN_CUISINE:
                if (playerInventory.containsAtLeast(new ItemStack(Material.SUSPICIOUS_STEW), 1)) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case FEE_CLOCHARDE:
                if (playerInventory.containsAtLeast(new ItemStack(Material.FEATHER), 31)) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case ADDICT_DES_SEAUX:
                if (verifSetOfItems(playerInventory, new ItemStack(Material.LAVA_BUCKET),
                        new ItemStack(Material.WATER_BUCKET),
                        new ItemStack(Material.MILK_BUCKET))) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case TOUT_EST_BON_DANS_LE_COCHON:
                if (playerInventory.containsAtLeast(new ItemStack(Material.PORKCHOP), 22)) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case TU_ES_GROSSE_MELISSANDRE:
                if (playerInventory.containsAtLeast(new ItemStack(Material.PUMPKIN_PIE), 1)) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case Y_A_DU_BAMBOU_LA:
                if (playerInventory.containsAtLeast(new ItemStack(Material.BAMBOO), 64)) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case COLLECTIONNEUR:
                if (verifSetOfItems(playerInventory, new ItemStack(Material.COAL_BLOCK),
                        new ItemStack(Material.REDSTONE_BLOCK),
                        new ItemStack(Material.LAPIS_BLOCK),
                        new ItemStack(Material.GOLD_BLOCK),
                        new ItemStack(Material.IRON_BLOCK),
                        new ItemStack(Material.DIAMOND_BLOCK),
                        new ItemStack(Material.COPPER_BLOCK),
                        new ItemStack(Material.EMERALD_BLOCK),
                        new ItemStack(Material.QUARTZ_BLOCK))) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case COFFRE_DU_NEANT:
                if (playerInventory.containsAtLeast(new ItemStack(Material.ENDER_CHEST), 1)) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case TRESOR_ENFOUI:
                if (playerInventory.containsAtLeast(new ItemStack(Material.HEART_OF_THE_SEA), 1)) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case INDIANA_JONES:
                Biome biome = player.getLocation().getBlock().getBiome();
                if (biome == Biome.JUNGLE || biome == Biome.BAMBOO_JUNGLE || biome == Biome.SPARSE_JUNGLE) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case MERLIN_L_ENCHANTEUR:
                if (playerInventory.containsAtLeast(new ItemStack(Material.ENCHANTING_TABLE), 1)) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case J_AI_LE_BATON_EN_FEU:
                if (playerInventory.containsAtLeast(new ItemStack(Material.BLAZE_ROD), 2)) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case SORTEZ_LES_MOUCHOIRS:
                if (playerInventory.containsAtLeast(new ItemStack(Material.CRYING_OBSIDIAN), 1)) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case THE_WALKING_DEAD:
                if (player.getStatistic(Statistic.KILL_ENTITY, EntityType.ZOMBIE) >= 29) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case SOS_FANTOMES:
                if (playerInventory.containsAtLeast(new ItemStack(Material.PHANTOM_MEMBRANE), 1)) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case JE_VEUX_TES_YEUX:
                if (playerInventory.containsAtLeast(new ItemStack(Material.ENDER_PEARL), 3)) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case CHATEAU_ROUGE:
                if (playerInventory.containsAtLeast(new ItemStack(Material.RED_NETHER_BRICKS), 17)) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case OLD_TOWN_ROAD:
                if (playerInventory.containsAtLeast(new ItemStack(Material.SADDLE), 1)) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case TERRE_COLOREE:
                if (Terracota.getNbTerracota(playerInventory) >= 8) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case BOB_L_EPONGE_CUBIQUE:
                if (playerInventory.containsAtLeast(new ItemStack(Material.SPONGE), 1)) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case MAYO_L_ABEILLE:
                if (playerInventory.containsAtLeast(new ItemStack(Material.HONEY_BLOCK), 1)) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case SOUS_L_OCEAN:
                if (playerInventory.containsAtLeast(new ItemStack(Material.TROPICAL_FISH_BUCKET), 1)) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case ASSURANCE_VIE:
                if (playerInventory.containsAtLeast(new ItemStack(Material.TOTEM_OF_UNDYING), 1)) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case BIENVENUE_AU_PAYS_DES_SCHTROUMPFS:
                if (player.getLocation().getBlock().getBiome() == Biome.MUSHROOM_FIELDS) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case QUI_DIT_MIEUX:
                if (playerInventory.containsAtLeast(new ItemStack(Material.NETHERITE_INGOT), 1)) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case MON_PRECIEUX:
                if (playerInventory.containsAtLeast(new ItemStack(Material.AMETHYST_BLOCK), 16)) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case UN_BOUT_DE_CERBERE:
                if (playerInventory.containsAtLeast(new ItemStack(Material.WITHER_SKELETON_SKULL), 1)) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case DES_PAILLETTES_DANS_MA_VIE_KEVIN:
                if (player.getStatistic(Statistic.KILL_ENTITY, EntityType.GLOW_SQUID) >= 3) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case BOULES_SCINTILLANTES:
                if (playerInventory.containsAtLeast(new ItemStack(Material.GLOW_BERRIES), 5)) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case CA_PIQUE:
                if (playerInventory.containsAtLeast(new ItemStack(Material.POINTED_DRIPSTONE), 20)) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case POSEIDON:
                for (ItemStack item : playerInventory.getStorageContents()) {
                    if (item != null && item.getType() == Material.TRIDENT) {
                        valideAndRealizeChallenge(player, challengeId);
                        return true;
                    }
                }
                break;
            case PLUTOT_KROKMOU_OU_SPYRO:
                ItemStack helmet = playerInventory.getHelmet();
                if (helmet != null && helmet.getType() == Material.DRAGON_HEAD) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case HALLUCINOGENES:
                if (verifSetOfItems((playerInventory), new ItemStack(Material.BROWN_MUSHROOM),
                        new ItemStack(Material.RED_MUSHROOM),
                        new ItemStack(Material.CRIMSON_FUNGUS),
                        new ItemStack(Material.WARPED_FUNGUS))) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case NOUVELLE_ENERGIE:
                if (playerInventory.containsAtLeast(new ItemStack(Material.DAYLIGHT_DETECTOR), 1)) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case LADY_GAGA:
                helmet = playerInventory.getHelmet();
                ItemStack chestplate = playerInventory.getChestplate();
                ItemStack leggings = playerInventory.getLeggings();
                ItemStack boots = playerInventory.getBoots();

                if (helmet != null && helmet.getType() == Material.GOLDEN_HELMET &&
                        (chestplate != null && chestplate.getType() == Material.GOLDEN_CHESTPLATE &&
                                (leggings != null && leggings.getType() == Material.GOLDEN_LEGGINGS &&
                                        (boots != null && boots.getType() == Material.GOLDEN_BOOTS)))) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case SAC_A_DOS_SAC_A_DOS:
                if (Shulker.isInInventory(playerInventory)) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case REMEDE_MAGIQUE:
                PotionEffect effect = player.getPotionEffect(PotionEffectType.REGENERATION);
                if (effect != null && effect.getAmplifier() == 1) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case ARMURE_ETINCELANTE:
                helmet = playerInventory.getHelmet();
                chestplate = playerInventory.getChestplate();
                leggings = playerInventory.getLeggings();
                boots = playerInventory.getBoots();

                if (helmet != null && helmet.getType() == Material.DIAMOND_HELMET &&
                        (chestplate != null && chestplate.getType() == Material.DIAMOND_CHESTPLATE &&
                                (leggings != null && leggings.getType() == Material.DIAMOND_LEGGINGS &&
                                        (boots != null && boots.getType() == Material.DIAMOND_BOOTS)))) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case JUSQU_AUX_CIEUX:
                if (playerInventory.containsAtLeast(new ItemStack(Material.BEACON), 1)) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case RAILS_DE_COKE:
                if (verifSetOfItems(playerInventory, new ItemStack(Material.RAIL, 32), new ItemStack(Material.SUGAR, 32))) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case DROLE_DE_PORTE_BONHEUR:
                if (playerInventory.containsAtLeast(new ItemStack(Material.RABBIT_FOOT), 1)) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case REPARATION_EXPRESS:
                helmet = playerInventory.getHelmet();
                if (helmet != null && helmet.getType() == Material.IRON_HELMET &&
                        helmet.getEnchantments().containsKey(Enchantment.MENDING)) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case FISHING_PLANET:
                if (verifSetOfItems(playerInventory, new ItemStack(Material.SALMON),
                        new ItemStack(Material.COD),
                        new ItemStack(Material.PUFFERFISH),
                        new ItemStack(Material.TROPICAL_FISH))) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case AFFAME:
                effect = player.getPotionEffect(PotionEffectType.HUNGER);
                if (effect != null) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case JACK_JE_VOLE:
                if (playerInventory.containsAtLeast(new ItemStack(Material.ELYTRA), 1)) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case ECRIS_L_HISTOIRE:
                for (ItemStack item : playerInventory.getContents()) {
                    if (item != null && item.getType() == Material.WRITTEN_BOOK &&
                            Objects.equals(((BookMeta) item.getItemMeta()).getAuthor(), player.getName())) {
                        valideAndRealizeChallenge(player, challengeId);
                        return true;
                    }
                }
                break;
            case BIENVENUE_AU_JAPON:
                if (playerInventory.containsAtLeast(new ItemStack(Material.CHERRY_SAPLING), 1)) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
            case LA_PRINCESSE_ET_LA_GRENOUILLE:
                helmet = playerInventory.getHelmet();
                if (helmet != null && helmet.getType() == Material.GOLDEN_HELMET) {
                    Collection<Entity> nears = player.getNearbyEntities(5, 5, 5);
                    for (Entity entity : nears) {
                        if (entity instanceof Frog) {
                            valideAndRealizeChallenge(player, challengeId);
                            return true;
                        }
                    }
                }
                break;
            case CORNE_DE_BRUME:
              for (ItemStack item : playerInventory) {
                  if (item != null && item.getType() == Material.GOAT_HORN) {
                      valideAndRealizeChallenge(player, challengeId);
                      return true;
                  }
              }
                break;
            case BELLE_BOSSE:
                if (player.getVehicle() instanceof Camel) {
                    valideAndRealizeChallenge(player, challengeId);
                    return true;
                }
                break;
        }
        return false;
    }
}
