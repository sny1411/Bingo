package fr.sny1411.bingo.utils.bonus;

import fr.sny1411.bingo.Bingo;
import fr.sny1411.bingo.utils.Challenge;
import fr.sny1411.bingo.utils.Random;
import fr.sny1411.bingo.utils.Team;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
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

    private static final List<PotionEffectType> potionEffectTypes = new ArrayList<>(Arrays.asList(PotionEffectType.SPEED,
                                                                                            PotionEffectType.HASTE,
                                                                                            PotionEffectType.RESISTANCE));
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
                setBonusII(potionEffectType, player, true);
                break;
            case HARD:
                setBonusIII(potionEffectType, player);
                break;
            default:
                throw new IllegalStateException("A bonus challenge can't be above HARD");
        }
    }

    private static void setBonusI(PotionEffectType potionEffectType, Player player) {
        player.sendMessage(bonusMessage("bingo.reward.you", potionEffectType, "I"));
        PotionEffect potion = new PotionEffect(potionEffectType, 144000, 0);
        player.addPotionEffect(potion);
    }

    private static void setBonusII(PotionEffectType potionEffectType, Player player, boolean setOnPlayerRealized) {
        player.sendMessage(bonusMessage("bingo.reward.team", potionEffectType, "I"));
        Team team = Bingo.getGame().getTeams().getTeam(player);
        assert team != null;
        for (Player playerTeam : team.getOnlinePlayers()) {
            if (player.equals(playerTeam)) {
                if (setOnPlayerRealized) {
                    setBonusI(potionEffectType, playerTeam);
                }
            } else {
                setBonusI(potionEffectType, playerTeam);
            }
        }
    }

    private static void setBonusIII(PotionEffectType potionEffectType, Player player) {
        setBonusII(potionEffectType, player, false);

        player.sendMessage(bonusMessage("bingo.reward.you", potionEffectType, "II"));
        PotionEffect potion = new PotionEffect(potionEffectType, 144000, 1);
        player.addPotionEffect(potion);
    }

    // The effect name is translated by the client, in the player's language
    private static Component bonusMessage(String key, PotionEffectType potionEffectType, String level) {
        return Component.textOfChildren(
                Component.text("≫ ", NamedTextColor.DARK_GRAY, TextDecoration.BOLD),
                Component.translatable(key, NamedTextColor.GRAY,
                        Component.translatable(potionEffectType, NamedTextColor.AQUA),
                        Component.text(level, NamedTextColor.AQUA)));
    }
}
