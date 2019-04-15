package org.inventivetalent.regionpreview.helper;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class IdParser {

	static Map<Integer, String> mapped = new HashMap<>();

	public static void main(String[] args) throws IOException {
		try (BufferedReader in = new BufferedReader(new InputStreamReader(MaterialMapColorParser.class.getResourceAsStream("/Block_old_class.txt")))) {
			String line;
			while ((line = in.readLine()) != null) {
				if(!line.startsWith("a("))continue;
				line = line.substring("a(".length());

				String[] splitA = line.split(",");
				String idString = splitA[0].trim();
				String name = splitA[1].replace("\"","").trim();
				Integer id = Integer.parseInt(idString);


				mapped.put(id,name);
				System.out.println("put("+id+",\""+name+"\");");
			}
		}
		System.out.println(mapped);
		System.out.println(new Gson().toJson(mapped));
	}


}
