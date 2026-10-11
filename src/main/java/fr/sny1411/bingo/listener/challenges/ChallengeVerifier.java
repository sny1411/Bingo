package fr.sny1411.bingo.listener.challenges;

import fr.sny1411.bingo.utils.*;
import fr.sny1411.bingo.utils.items.collections.Shulker;
import fr.sny1411.bingo.utils.items.collections.Terracota;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.Objects;
import java.util.logging.Level;

import static fr.sny1411.bingo.listener.challenges.ChallengeProgress.valideAndRealizeChallenge;
import static fr.sny1411.bingo.listener.challenges.ChallengeProgress.verifValideChallenge;

/**
 * Checks the challenges that are validated by clicking them in the grid GUI.
 */
public final class ChallengeVerifier {
    private ChallengeVerifier() {
        throw new IllegalStateException("Utility class");
    }

    private static boolean verifSetOfItems(Inventory inventory, ItemStack... items) {
        for (ItemStack item : items) {
            if (!inventory.containsAtLeast(new ItemStack(item.getType()), item.getAmount())) return false;

        }
        return true;
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
