package com.kushals.collection;

import java.util.Map;

public class MapConsumer {

	public static void main(String[] args) {
		Map<String, String> myMap = new DiskBackedMap<>();
		myMap.put("kushal", "sethi");
		myMap.put("clg", "vjti");
		System.out.println("Size of map: " + myMap.size());
		String value = myMap.remove("kushal");
		System.out.println("Removed value : " + value);
		System.out.println("Value at key clg : " + myMap.get("clg"));
		System.out.println("Size of map after removal: " + myMap.size());
	}

}
