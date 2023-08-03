package fr.sny1411.bingo.utils.bonus;

import fr.sny1411.bingo.utils.Challenge;
import fr.sny1411.bingo.utils.Random;
import fr.sny1411.bingo.utils.Team;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class RewardsBonusEvent {
    private RewardsBonusEvent() {
        throw new IllegalStateException("Utility class");
    }

    private static List<PotionEffectType> potionEffectTypes = new ArrayList<>(Arrays.asList(PotionEffectType.SPEED,
                                                                                            PotionEffectType.FAST_DIGGING,
                                                                                            PotionEffectType.DAMAGE_RESISTANCE));
    public static void setBonus(Challenge challenge, Player player) {
        PotionEffectType potionEffect = potionEffectTypes.get(Random.choice(0, potionEffectTypes.size()-1));
        setBonus(potionEffect, player, challenge.getDifficult());
    }

    private static void setBonus(PotionEffectType potionEffectType, Player player, Challenge.Difficult difficult) {
        switch (difficult) {
            case EASY:
                setBonusI(potionEffectType, player);
                break;
            case MEDIUM:
                setBonusII(potionEffectType, player);
                break;
            case HARD:
                setBonusIII(potionEffectType, player);
                break;
            default:
                // TODO AJOUTER UNE ERREUR
                throw new IllegalStateException("Le défis bonus ne peut pas être au dessus de HARD");
        }
    }

    private static void setBonusI(PotionEffectType potionEffectType, Player player) {
        player.sendMessage(Component.text("§8§l≫ §r§7Vous recevez le bonus §b" + potionEffectType.getName() + " I"));
        PotionEffect potion = new PotionEffect(potionEffectType, 144000, 0);
        player.addPotionEffect(potion);
    }

    private static void setBonusII(PotionEffectType potionEffectType, Player player) {
        player.sendMessage(Component.text("§8§l≫ §r§7Votre équipe reçoit le bonus §b" + potionEffectType.getName() + " I"));
        Team team = Team.getTeam(player);
        assert team != null;
        for (Player playerTeam : team.getPlayers()) {
            if (playerTeam.isOnline()) {
                setBonusI(potionEffectType, playerTeam);
            }
        }
    }

    private static void setBonusIII(PotionEffectType potionEffectType, Player player) {
        setBonusII(potionEffectType, player);

        player.sendMessage(Component.text("§8§l≫ §r§7Vous recevez le bonus §b" + potionEffectType.getName() + " II"));
        PotionEffect potion = new PotionEffect(potionEffectType, 144000, 1);
        player.addPotionEffect(potion);
    }
}
