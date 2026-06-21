package io.github.skulli73.mauriceSMP.skills;

public final class SkillUtils {

    private SkillUtils() {}

    public static int xpToLevel(double xp) {
        int level = 1;
        int baseXP = 10;
        int currentXP = 0;

        while (currentXP <= xp) {
            currentXP += baseXP;
            if (currentXP > xp) {
                break;
            }
            level++;
            baseXP *= 2;
        }
        return level;
    }

    public static double levelToXP(int level) {
        if (level <= 1) {
            return 0;
        }
        int xp = 0;
        int baseXP = 10;
        for (int i = 1; i < level; i++) {
            xp += baseXP;
            baseXP *= 2;
        }
        return xp;
    }

    private static double log2(double input) {
        return Math.log10(input)/Math.log10(2);
    }

}
