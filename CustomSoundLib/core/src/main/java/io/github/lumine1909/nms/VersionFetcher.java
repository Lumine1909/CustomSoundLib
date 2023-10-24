package io.github.lumine1909.nms;

import org.bukkit.Bukkit;

public class VersionFetcher {
    static int[] ver = {0, 601, 900, 1100, 1300, 1500, 1602, 1700, 1800};
    public static int[] getServerVersion() {
        int[] version = new int[3];
        String versions = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        version[0] = Integer.parseInt(versions.split("_")[0].substring(1));
        version[1] = Integer.parseInt(versions.split("_")[1]);
        version[2] = Integer.parseInt(versions.split("_")[2].substring(1));
        return version;
    }
    public static int getPackVersion() {
        String var0 = Bukkit.getVersion().split(":")[1];
        String[] var1 = var0.substring(0, var0.length()-1).split("\\.");
        int pack = Integer.parseInt(var1[1]) * 100 + Integer.parseInt(var1[2]);
        if (pack < 1903) {
            for (int i=0; i<=8; i++) {
                if (ver[i] == pack) {
                    return i;
                }
            }
            return 9;
        } else if (pack == 1903) {
            return 12;
        } else if (pack == 1904) {
            return 13;
        } else if (pack == 2000 || pack == 2100) {
            return 15;
        } else if (pack == 2002) {
            return 18;
        } else {
            return 19;
        }
    }
}
