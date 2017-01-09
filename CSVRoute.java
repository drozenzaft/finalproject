import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;

public class CSVRoute {
    private ArrayList<String> data;
    private ArrayList<String[]> dataSplit;
    public CSVRoute() {
	data = loadData();
	dataSplit = loadSplitData();
    }
    public static ArrayList<String> loadData() {
	ArrayList<String> temp = new ArrayList<String>();
	try {
	    Scanner dataScan = new Scanner(new File("data.csv"));
	    String line = "";
	    int i = 0;
	    while (dataScan.hasNext()) {
		line = dataScan.nextLine();
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
    public ArrayList<String[]> loadSplitData() {
	ArrayList<String[]> temp = new ArrayList<String[]>();
	String a = "k";
	String[] p;
	for (int i = 0; i < data.size(); i++) {
//in python, this would work. but java only accepts a regex argument???
	    //how to split on commas in java, like in python???
	    temp.add(data.get(i).split(","));
	    for (int j = 0; j < temp.get(i).length; j++) {
		if (a.equals(",") && temp.get(i)[j] == ",") {
		    p = new String[j+1];
		    for (int k = 0; k < j; k++) {
			p[k] = temp.get(i)[k];
		    }
		    temp.set(i,p);
		    break;
		}
		else {
		    a = temp.get(i)[j];
		}
	    }
	}
	return temp;
    }

    public String stationToID(String station){
	ArrayList<String[]> stops = loadSplitData();
	for(int i = 0; i < stops.size(); i++){
	    if(station.equals(stops.get(i)[1])){
		return stops.get(i)[0];
	    }
	}
	return "invalid station name";
    }    

    public static void main(String[] args) {
	CSVRoute csv = new CSVRoute();
	ArrayList<String[]> split = csv.loadSplitData();
	System.out.println(Arrays.toString(csv.loadData().toArray()));
	for (int i = 0; i < split.size(); i++) {
	    System.out.println(Arrays.toString(split.get(i)));
	}

	System.out.println(split.get(0)[1]);
	System.out.println(split.get(4)[0]);

	System.out.println(csv.stationToID("South Ferry")); //1
	System.out.println(csv.stationToID("23rd St"));
	System.out.println(csv.stationToID("28th St")); 
    }
}