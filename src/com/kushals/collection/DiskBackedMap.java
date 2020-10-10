package com.kushals.collection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class DiskBackedMap<K extends Serializable, V extends Serializable> implements Map<String, String> {

	private File mapFile;

	public DiskBackedMap() {
		mapFile = new File("DiskMap~" + UUID.randomUUID() + ".txt");
		// mapFile.mkdirs();
		try {
			if (mapFile.createNewFile()) {
				System.out.println("File is created!");
			} else {
				System.out.println("File already exists!");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.mapFile.deleteOnExit();
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsKey(Object key) {
		String fileContents = null;
		try {
			fileContents = getFileContents();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (fileContents != null) {
			String[] lines = fileContents.split("\n");
			for (String line : lines) {
				String[] keyValuePair = line.split(":::");
				if (key.equals(keyValuePair[0])) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		String fileContents = null;
		try {
			fileContents = getFileContents();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (fileContents != null) {
			String[] lines = fileContents.split("\n");
			for (String line : lines) {
				String[] keyValuePair = line.split(":::");
				if (value.equals(keyValuePair[1])) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public Set<java.util.Map.Entry<String, String>> entrySet() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String get(Object key) {
		if (key == null)
			throw new NullPointerException();
		if (containsKey(key)) {
			String fileContents = null;
			try {
				fileContents = getFileContents();
				if (fileContents != null) {
					String[] lines = fileContents.split("\n");
					for (String line : lines) {
						String[] keyValuePair = line.split(":::");
						if (key.equals(keyValuePair[0])) {
							return keyValuePair[1];
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public boolean isEmpty() {
		return (size() > 0) ? true : false;
	}

	@Override
	public Set<String> keySet() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String put(String key, String value) {
		if (key == null && value == null)
			throw new NullPointerException();
		try {
			if (!containsKey(key)) {
				addEntryToMapFile(key, value);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void addEntryToMapFile(String key, String value) throws IOException {
		PrintWriter writer = new PrintWriter(new FileWriter(mapFile, true));
		writer.println(key + ":::" + value);
		writer.flush();
		writer.close();
	}

	@Override
	public void putAll(Map<? extends String, ? extends String> m) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String remove(Object key) {
		if (key == null)
			throw new NullPointerException();
		if (containsKey(key)) {
			String fileContents = null;
			PrintWriter writer = null;
			try {
				String valueToReturn = get(key);
				fileContents = getFileContents();
				if (fileContents != null) {
					File tempFile = new File("tempFile.txt");
					tempFile.createNewFile();
					writer = new PrintWriter(new FileWriter(tempFile, true));
					String[] lines = fileContents.split("\n");
					for (String line : lines) {
						String[] keyValuePair = line.split(":::");
						if (keyValuePair[0].contains((String) key))
							continue;
						writer.println(keyValuePair[0] + ":::" + keyValuePair[1]);
					}
					writer.close();
					Path path = Files.move(Paths.get(tempFile.toURI()), Paths.get(this.mapFile.toURI()),
							StandardCopyOption.REPLACE_EXISTING);
					System.out.println("File replaced at : " + path.getFileName());
				}
				return valueToReturn;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			throw new RuntimeException("Invalid Key!");
		}
		return null;
	}

	@Override
	public int size() {
		String fileContents = null;
		String[] lines = null;
		try {
			fileContents = getFileContents();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (fileContents != null) {
			lines = fileContents.split(System.getProperty("line.separator"));
		}
		return (lines != null && lines.length > 0) ? lines.length : 0;
	}

	@Override
	public Collection<String> values() {
		throw new UnsupportedOperationException();
	}

	private String getFileContents() throws IOException {
		StringBuffer fileContents = new StringBuffer();
		BufferedReader reader = new BufferedReader(new FileReader(mapFile));
		String line;
		while ((line = reader.readLine()) != null) {
			fileContents.append(line + "\n");
		}
		reader.close();
		return fileContents.toString();
	}
}
