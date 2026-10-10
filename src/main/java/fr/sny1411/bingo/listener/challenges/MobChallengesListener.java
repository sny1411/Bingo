package fr.sny1411.bingo.listener.challenges;

import com.destroystokyo.paper.event.entity.TurtleGoHomeEvent;
import fr.sny1411.bingo.Bingo;
import fr.sny1411.bingo.utils.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

import static fr.sny1411.bingo.listener.challenges.ChallengeProgress.realizeChallenge;

/**
 * Challenges done with mobs: killing, taming, riding, interacting with them.
 */
public class MobChallengesListener implements Listener {
    private static final Hashtable<Player, Integer> nbWolfTame = new Hashtable<>();

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
    private void sheepShear(PlayerShearEntityEvent e) {
        if (e.getEntity() instanceof Sheep) {
            Sheep sheep = (Sheep) e.getEntity();
            if (sheep.getColor() == DyeColor.PURPLE) {
                realizeChallenge(e.getPlayer(), ChallengeId.TRICOT);
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
    private void piglinTrade(PiglinBarterEvent e) {
        List<Entity> nears = e.getEntity().getNearbyEntities(50, 50, 50);
        for (Entity entity : nears) {
            if (entity instanceof Player) {
                realizeChallenge((Player) entity, ChallengeId.UNE_AFFAIRE_EN_OR);
            }
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
}
