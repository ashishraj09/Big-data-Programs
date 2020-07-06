package com.classification;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.LinkedHashSet;
import java.util.Set;

public class CleanData2 {

	static String[][] tempArray;
	static Boolean[][] booleanArray;
	static int rowLength = 0;
	static int colLength = 4126;
	static Set<Integer> linkedHashSet = new LinkedHashSet<>();

	public static void main(String[] args) {

		long programTime = System.currentTimeMillis();
		System.out.println("Reading file : " + args[0]);
		getRowCount(args[0]);
		tempArray = new String[rowLength][colLength];
		booleanArray = new Boolean[rowLength][colLength];
		for (int i = 0; i < rowLength; i++) {
			for (int j = 0; j < colLength; j++) {
				tempArray[i][j] = "NA";
			}
		}

		FileInputStream fstream = null;
		try {

			fstream = new FileInputStream(args[0]);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

			String strLine = br.readLine();
			int index = 0;
			while (strLine != null) {
				String[] tokens = strLine.split(" ");
				for (String token : tokens) {
					if (!token.isBlank() || !token.isEmpty()) {

						if (token.contains(":")) {
							String[] tempStore = token.split(":");

							if (Double.parseDouble(tempStore[1]) == 0.0) {
								tempArray[index][Integer.parseInt(tempStore[0])] = "NA";
							} else {
								tempArray[index][Integer.parseInt(tempStore[0])] = tempStore[1];
								linkedHashSet.add(Integer.parseInt(tempStore[0]));
							}

						} else {
							tempArray[index][0] = token;
						}
					}

				}
				index++;
				strLine = br.readLine();
			}
			String[] filename = args[0].split("\\.");
			writeToFile(filename[0] + "_Cleaned2.csv");
			br.close();
			System.out.println("Time Elasped: " + ((System.currentTimeMillis() - programTime) / 1000) + "s");
			System.out.println("Columns reduced from 4125 to " + linkedHashSet.size() + " after cleaning the dataset.");
		} catch (IOException e) {
			System.out.println("Error reading file: " + e.getLocalizedMessage());
		} finally {
			try {

				if (null != fstream)
					fstream.close();
			} catch (IOException e) {
			}
		}

	}

	private static void writeToFile(String outFileName) {

		System.out.println("Writing cleaned file...");
		for (int i = 0; i < rowLength; i++) {
			StringBuilder sb = new StringBuilder();
			for (int j = 1; j < colLength; j++) {
				if (linkedHashSet.contains(j)) {
					if (tempArray[i][j].equals("NA")) {
						sb.append("0.0" + ",");
					} else {
						sb.append(tempArray[i][j] + ",");
					}
				}
			}
			sb.append(tempArray[i][0]);
			write(sb, outFileName);
		}
		System.out.println("Writing cleaned file complete: " + outFileName);
	}

	private static void write(StringBuilder sb, String outFileName) {
		try (FileWriter fw = new FileWriter(outFileName, true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {
			out.println(sb.toString());
		} catch (IOException e) {
			System.out.println("Error writing to file " + outFileName + " : " + e.getLocalizedMessage());
		}

	}

	private static void getRowCount(String fileName) {
		int index = 0;
		FileInputStream fstream = null;
		try {
			fstream = new FileInputStream(fileName);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

			String strLine = br.readLine();

			while (strLine != null) {

				index++;

				strLine = br.readLine();
			}
			rowLength = index;
			System.out.println("File row length -> " + index);
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {

				if (null != fstream)
					fstream.close();
			} catch (IOException e) {
				System.out.println("Error reading file: " + e.getLocalizedMessage());
			}
		}

	}
}
