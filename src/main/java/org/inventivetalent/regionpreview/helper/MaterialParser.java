package org.inventivetalent.regionpreview.helper;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class MaterialParser {

	static Map<String, String> mapped = new HashMap<>();

	public static void main(String[] args) throws IOException {
		try (BufferedReader in = new BufferedReader(new InputStreamReader(MaterialParser.class.getResourceAsStream("/Material_class.txt")))) {
			String line;
			while ((line = in.readLine()) != null) {
				String[] splitA = line.split("=");
				String name = splitA[0].trim();

				String[] splitB = line.split("MaterialMapColor.");
				String[] splitC = splitB[1].split("\\)\\)");
				String mapColor = splitC[0].trim();

				mapped.put(name, mapColor);
				System.out.println("put(\"" + name + "\",\"" + mapColor + "\");");
			}
		}
		System.out.println(mapped);
		System.out.println(new Gson().toJson(mapped));
	}

}
