package fr.sny1411.bingo.listener.challenges;

import fr.sny1411.bingo.utils.*;
import fr.sny1411.bingo.utils.items.collections.Candle;
import org.bukkit.*;
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
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.event.raid.RaidTriggerEvent;

import java.util.List;
import java.util.Objects;

import static fr.sny1411.bingo.listener.challenges.ChallengeProgress.realizeChallenge;

/**
 * Challenges done in the world: blocks, explosions, weather, raids.
 */
public class WorldChallengesListener implements Listener {
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
    private void cauldronExtinguish(CauldronLevelChangeEvent e) {
        if (e.getReason() == CauldronLevelChangeEvent.ChangeReason.EXTINGUISH && Objects.requireNonNull(e.getEntity()).getWorld() == Bukkit.getWorlds().get(1)) {
            realizeChallenge((Player) e.getEntity(), ChallengeId.SEANCE_JACUZZI);
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
    private void blockFormEvent(EntityBlockFormEvent e) {
        if (e.getBlock().getType() == Material.WATER && e.getEntity() instanceof Player) {
            realizeChallenge((Player) e.getEntity(), ChallengeId.APPELLE_MOI_MOISE);
        }
    }

    @EventHandler
    private void raidTrigger(RaidTriggerEvent e) {
        realizeChallenge(e.getPlayer(), ChallengeId.NOUS_SOMMES_EN_GUERRE);
    }

    @EventHandler
    private void lightningStrike(EntityDamageEvent e) {
        if (e.getCause() == EntityDamageEvent.DamageCause.LIGHTNING && e.getEntity() instanceof Player) {
            realizeChallenge((Player) e.getEntity(), ChallengeId.COUP_DE_FOUDRE);
        }
    }
}
