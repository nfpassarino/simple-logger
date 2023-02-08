package com.nfpassarino.simplelogger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class LoggerFile {
	private String path;
	private boolean append = true;

	public LoggerFile(String path) {
		this.path = path;
	}

	public void cleanLog() {
		this.append = false;
		addLine("");
		this.append = true;
	}

	public void addLine(String line) {
		try (FileWriter fileWriter = new FileWriter(this.path, append);
				BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);) {
			if (!line.isBlank()) {
				line = sanitizeLine(line);
			}
			bufferedWriter.write(line);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<String> readFile() throws FileNotFoundException {
		List<String> linesFile = new ArrayList<>();
		try (FileReader fileReader = new FileReader(this.path);
				BufferedReader bufferedReader = new BufferedReader(fileReader)) {
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				linesFile.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return linesFile;
	}

	private String sanitizeLine(String line) {
		StringBuilder sanitizedLine = new StringBuilder();
		sanitizedLine.append(removeColors(line));
		sanitizedLine.append(System.lineSeparator());
		return sanitizedLine.toString();
	}

	private String removeColors(String line) {
		return line.replaceAll("\u001B\\[[;\\d]*m", "");
	}

}
