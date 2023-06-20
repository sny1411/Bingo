package fr.sny1411.bingo.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public class BlocksFill {
    public static void changeArea(int smallX, int smallY, int smallZ, int bigX, int bigY, int bigZ, Material typeBlock, World world) {
        for (int i = smallX; i <= bigX; i++) {
            for (int j = smallY; j <= bigY; j++) {
                for (int k = smallZ; k <= bigZ; k++) {
                    new Location(world, i, j, k).getBlock().setType(typeBlock);
                }
            }
        }
    }
}
