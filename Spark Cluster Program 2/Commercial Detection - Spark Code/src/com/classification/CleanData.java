package com.classification;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class CleanData {

	static String[][] tempArray;
	static Boolean[][] booleanArray;
	static int rowLength = 0;
	static int colLength = 4126;

	public static void main(String[] args) {

		System.out.println("Reading file : "+ args[0]);
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
			List<Boolean> x = filterToArray();
			writeToFile(x, filename[0] + "_Cleaned.csv");
			br.close();
		} catch (IOException e) {
			System.out.println("Error reading file: "+e.getLocalizedMessage());
		} finally {
			try {

				if (null != fstream)
					fstream.close();
			} catch (IOException e) {
			}
		}

	}
	/**
	 * Make a boolean grid with true values for NA values
	 * and false for non NA values
	 **/
	private static List<Boolean> filterToArray() {
		List<Boolean> check = new ArrayList<Boolean>();
		for (int i = 0; i < rowLength; i++) {
			for (int j = 0; j < colLength; j++) {
				if (tempArray[i][j].contentEquals("NA")) {
					booleanArray[i][j] = true;
				} else {
					booleanArray[i][j] = false;
				}
			}

		}

		for (int i = 0; i < colLength; i++) {

			check.add(checkArray(i));
		}

		return check;
	}

	/**
	 * Remove unwanted columns that only have NA
	 * @param col to check
	 * @return if a column has non NA values
	 */
	private static boolean checkArray(int col) {

		for (int i = 0; i < rowLength; i++) {

			if (!booleanArray[i][col].booleanValue()) {
				return true;
			}

		}

		return false;

	}

	private static void writeToFile(List<Boolean> x, String outFileName) {

		System.out.println("Writing cleaned file...");
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < rowLength; i++) {
			for (int j = 1; j < colLength; j++) {
				if (x.get(j).booleanValue()) {
					sb.append(tempArray[i][j] + ",");
				}
			}
			sb.append(tempArray[i][0] + "\n");
		}
		try (FileWriter fw = new FileWriter(outFileName, true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {
			out.println(sb.toString());
			System.out.println("Writing cleaned file complete: "+outFileName);	
		} catch (IOException e) {
			System.out.println("Error writing to file "+outFileName+" : "+e.getLocalizedMessage());
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
				System.out.println("Error reading file: "+e.getLocalizedMessage());
			}
		}

	}
}
