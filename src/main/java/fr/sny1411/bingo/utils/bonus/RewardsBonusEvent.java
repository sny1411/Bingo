package fr.sny1411.bingo.utils.bonus;

import fr.sny1411.bingo.utils.Challenge;
import fr.sny1411.bingo.utils.Team;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class RewardsBonusEvent {

    public static void setBonus(PotionEffectType potionEffectType, Player player, Challenge.Difficult difficult) {
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
                break;
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
