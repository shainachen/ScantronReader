import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class CSVData {
	private static boolean DEBUG = false;
	private double[][] rawData;

	private CSVData(double[][] data) {
		rawData = data;
	}
	
	public static CSVData createDataSet(String filepath, int linesToSkip) {
		debug("Reading file: " + filepath);
		
		String data = readFileAsString(filepath);
		String[] lines = data.split("\n");
		
		debug("Reading " + lines.length + " total lines from file");
		debug("Using index " + (linesToSkip) + " as header row");
		
		String headerLine = lines[linesToSkip];
		debug("Headers: " + headerLine);
		
		String[] headers = headerLine.split(",");
		debug("Parsed header line into: " + headers.length + " total columns");
		
		int startColumn = 0;
		return createDataSet(filepath, linesToSkip + 1, headers, startColumn);
	}
	
	public static CSVData createDataSet(String filepath, int linesToSkip, String[] columnHeaders, int startColumn) {
		debug("Reading file: " + filepath);
		
		String data = readFileAsString(filepath);
		String[] lines = data.split("\n");
		
		debug("Reading " + lines.length + " total lines from file");
		
		int numColumns = columnHeaders.length;
		debug("Reading " + numColumns + " total columns");
		
		int startRow = linesToSkip;
		
		// create storage for data
		double[][] numdata = new double[lines.length - linesToSkip][numColumns];

		for (int r = startRow; r < lines.length; r++) {
			String line = lines[r];
			String[] coords = line.split(",");

			for (int j = startColumn; j < numColumns; j++) {
				if (coords[j].endsWith("#")) coords[j] = coords[j].substring(0, coords[j].length()-1);
				double val = Double.parseDouble(coords[j]);
				numdata[r - 1][j - startColumn] = val;
			}
		}

		return new CSVData(numdata);
	}
	
	public double[][] getAllData() {
		return rawData;
	}
	
	
		
	public static void writeDataToFile(String filePath, String data) {
		File outFile = new File(filePath);

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(outFile))) {
			writer.write(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String readFileAsString(String filepath) {
		StringBuilder output = new StringBuilder();

		try (Scanner scanner = new Scanner(new File(filepath))) {

			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				output.append(line + System.getProperty("line.separator"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return output.toString();
	}
	
	
	private static void debug(String string) {
		if (DEBUG ) {
			System.err.println(string);
		}
	}
}