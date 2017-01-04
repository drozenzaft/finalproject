import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;

public class CSVRoute {
    private ArrayList<String> data;
    public CSVRoute() {
	data = loadData();
    }
    public static ArrayList<String> loadData() {
	ArrayList<String> temp = new ArrayList<String>();
	try {
	    Scanner dataScan = new Scanner(new File("data.csv"));
	    String line = "";
	    int i = 0;
	    while (dataScan.hasNext() && i < 20) {
		line = dataScan.next();
		temp.add(line);
		i++;
	    }
	    dataScan.close();
	}
	catch (FileNotFoundException e) {
	    System.out.println("File not found! Please have a valid 'data.csv' file!");
	    System.exit(1);
	}
	return temp;
    }
    public void printData() {
	for (String line : data) {
	    System.out.println(line);
	}
    }
    
    public static void main(String[] args) {
	CSVRoute csv = new CSVRoute();
	csv.printData();
    }
}
