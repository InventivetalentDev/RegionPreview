package org.inventivetalent.regionpreview.helper;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class MaterialMapColorParser {

	static Map<String, Integer> mapped = new HashMap<>();

	public static void main(String[] args) throws IOException {
		try (BufferedReader in = new BufferedReader(new InputStreamReader(MaterialMapColorParser.class.getResourceAsStream("/MaterialMapColor_class.txt")))) {
			String line;
			while ((line = in.readLine()) != null) {
				String[] splitA = line.split("=");
				String name = splitA[0].substring("public static final MaterialMapColor ".length()).trim();

				String[] splitB = splitA[1].split(",");
				String valueString = splitB[1].replace(");", "").trim();
				int value = Integer.parseInt(valueString);

				mapped.put(name, value);
				System.out.println("put(\""+name+"\","+value+");");
			}
		}
		System.out.println(mapped);
		System.out.println(new Gson().toJson(mapped));
	}

}
