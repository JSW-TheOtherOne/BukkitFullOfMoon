package com.dinnerbone.bukkit.moon;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.util.NumberConversions;

public class MoonUtils {
	// Send to msg to Bukkit Logger
	public static void log(ChatColor colour, String msg) {
		if (colour == null) {
			colour = ChatColor.WHITE;
		}
        Bukkit.getConsoleSender().sendMessage(colour + msg);// + ChatColor.WHITE);
	}

    public static double distanceArray(List<Integer> n,List<Integer> o) {
		Double x = NumberConversions.toDouble(n.get(0));
		Double y = NumberConversions.toDouble(n.get(1));
		Double z = NumberConversions.toDouble(n.get(2));
		Double xx = NumberConversions.toDouble(o.get(0));
		Double yy = NumberConversions.toDouble(o.get(1));
		Double zz = NumberConversions.toDouble(o.get(2));
		
		return Math.sqrt(NumberConversions.square(x - xx) + NumberConversions.square(y - yy) + NumberConversions.square(z - zz));
    }
}