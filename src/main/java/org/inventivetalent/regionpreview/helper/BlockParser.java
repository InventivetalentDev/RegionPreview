package org.inventivetalent.regionpreview.helper;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class BlockParser {

	// Generated by MaterialMapColorParser
	static final Map<String, Integer> materialMapColor = new HashMap<String, Integer>() {{
		put("b", 0);
		put("c", 8368696);
		put("d", 16247203);
		put("e", 13092807);
		put("f", 16711680);
		put("g", 10526975);
		put("h", 10987431);
		put("i", 31744);
		put("j", 16777215);
		put("k", 10791096);
		put("l", 9923917);
		put("m", 7368816);
		put("n", 4210943);
		put("o", 9402184);
		put("p", 16776437);
		put("q", 14188339);
		put("r", 11685080);
		put("s", 6724056);
		put("t", 15066419);
		put("u", 8375321);
		put("v", 15892389);
		put("w", 5000268);
		put("x", 10066329);
		put("y", 5013401);
		put("z", 8339378);
		put("A", 3361970);
		put("B", 6704179);
		put("C", 6717235);
		put("D", 10040115);
		put("E", 1644825);
		put("F", 16445005);
		put("G", 6085589);
		put("H", 4882687);
		put("I", 55610);
		put("J", 8476209);
		put("K", 7340544);
		put("L", 13742497);
		put("M", 10441252);
		put("N", 9787244);
		put("O", 7367818);
		put("P", 12223780);
		put("Q", 6780213);
		put("R", 10505550);
		put("S", 3746083);
		put("T", 8874850);
		put("U", 5725276);
		put("V", 8014168);
		put("W", 4996700);
		put("X", 4993571);
		put("Y", 5001770);
		put("Z", 9321518);
		put("aa", 2430480);
	}};

	// Generated by MaterialParser
	static final Map<String, String> material = new HashMap<String, String>() {{
		put("AIR", "b");
		put("STRUCTURE_VOID", "b");
		put("PORTAL", "b");
		put("WOOL", "e");
		put("PLANT", "i");
		put("WATER_PLANT", "n");
		put("REPLACEABLE_PLANT", "i");
		put("REPLACEABLE_WATER_PLANT", "n");
		put("WATER", "n");
		put("BUBBLE_COLUMN", "n");
		put("LAVA", "f");
		put("PACKED_ICE", "j");
		put("FIRE", "b");
		put("ORIENTABLE", "b");
		put("WEB", "e");
		put("BUILDABLE_GLASS", "b");
		put("CLAY", "k");
		put("EARTH", "l");
		put("GRASS", "c");
		put("SNOW_LAYER", "g");
		put("SAND", "d");
		put("SPONGE", "t");
		put("WOOD", "o");
		put("CLOTH", "e");
		put("TNT", "f");
		put("LEAVES", "i");
		put("SHATTERABLE", "b");
		put("ICE", "g");
		put("CACTUS", "i");
		put("STONE", "m");
		put("ORE", "h");
		put("SNOW_BLOCK", "j");
		put("HEAVY", "h");
		put("BANNER", "b");
		put("PISTON", "m");
		put("CORAL", "i");
		put("PUMPKIN", "i");
		put("DRAGON_EGG", "i");
		put("CAKE", "b");
	}};

	// Generated by my fingers
	static final Map<String, String> enumColor = new HashMap<String, String>() {{
		put("WHITE", "j");
		put("ORANGE", "q");
		put("MAGENTA", "r");
		put("LIGHT_BLUE", "s");
		put("YELLOW", "t");
		put("LIME", "u");
		put("PINK", "v");
		put("GRAY", "w");
		put("LIGHT_GRAY", "x");
		put("CYAN", "y");
		put("PURPLE", "z");
		put("BLUE", "A");
		put("BROWN", "B");
		put("GREEN", "C");
		put("RED", "D");
		put("BLACK", "E");
	}};

	static Map<String, Integer> mapped = new HashMap<>();

	public static void main(String[] args) throws IOException {
		try (BufferedReader in = new BufferedReader(new InputStreamReader(MaterialMapColorParser.class.getResourceAsStream("/Block_class.txt")))) {
			String line;
			while ((line = in.readLine()) != null) {
				int rgb = 0;
				if (line.contains("Block.Info")) {// other lines contain e.g. EnumColor aswell, but don't actually have a color on maps
					if (line.contains("MaterialMapColor")) {
						String[] splitA = line.split("Block.Info.");// curse you, logs with multiple colors!
						String[] splitB = splitA[1].split("MaterialMapColor.");
						String[] splitC = splitB[1].split("\\).");
						String mapColor = splitC[0];

						rgb = rgbForMapColor(mapColor);
					} else if (line.contains("EnumColor")) {
						String[] splitA = line.split("Block.Info.");
						String[] splitB = splitA[1].split(", EnumColor.");// important occurrences are the second argument
						if (splitB.length < 2) { continue; }
						String[] splitC = splitB[1].split("\\).");
						String enumColor = splitC[0];

						rgb = rgbForEnumColor(enumColor);
					} else if (line.contains("Material.")) {
						String[] splitA = line.split("Block.Info.");
						String[] splitB = splitA[1].split("Material.");
						String[] splitC = splitB[1].split("\\)");
						String[] splitD = splitC[0].split(",");

						String material = splitD[0];

						rgb = rgbForMaterial(material);
					} else {
						continue;
					}

					String[] splitA = line.substring("a(\"".length()).split("\",");
					String name = splitA[0];

					mapped.put(name, rgb);
					System.out.println("put(\"" + name + "\"," + rgb + ");");
				}
			}
		}
		System.out.println(mapped);
		System.out.println(new Gson().toJson(mapped));
	}

	static int rgbForMapColor(String mapColor) {
		return materialMapColor.get(mapColor);
	}

	static int rgbForEnumColor(String enumC) {
		return rgbForMapColor(mapColorForEnumColor(enumC));
	}

	static int rgbForMaterial(String mat) {
		return rgbForMapColor(mapColorForMaterial(mat));
	}

	static String mapColorForEnumColor(String enumC) {
		return enumColor.get(enumC);
	}

	static String mapColorForMaterial(String mat) {
		return material.get(mat);
	}

}
