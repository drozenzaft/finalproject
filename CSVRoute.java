import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;

public class CSVRoute {
    private ArrayList<String> data,order;
    private ArrayList<String[]> dataSplit,orderSplit;
    public CSVRoute() {
	data = loadData("data.csv");
	dataSplit = loadSplitData("data.csv");
	order = loadData("stop order.csv");
	orderSplit = loadSplitData("stop order.csv");
    }
    public static ArrayList<String> loadData(String filename) {
	ArrayList<String> temp = new ArrayList<String>();
	try {
	    Scanner dataScan = new Scanner(new File(filename));
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
    public ArrayList<String[]> loadSplitData(String filename) {
	ArrayList<String[]> temp = new ArrayList<String[]>();
	ArrayList<String> ary;
	if (filename.equals("data.csv")) {
	    ary = new ArrayList<>(data);
	}
	else {
	    ary = new ArrayList<>(order);
	}
	String a = "";
	String[] p;
	for (int i = 0; i < ary.size(); i++) {
	    temp.add(ary.get(i).split(","));
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

    public int lineIndex(String trainLine) {
	int lineIndex;
	for (int j = 0; j < orderSplit.size(); j++) {
	    if (trainLine.equals(orderSplit.get(j)[0])) {
		return j;
	    }
	}
        throw new NoSuchTrainException( "That train doesn't stop in Manhattan!");
    }

    public static boolean arrayContains(String[] ary, String goal) {
	for (String element : ary) {
	    if (element.equals(goal)) {
		return true;
	    }
	}
	return false;
    }
    
    public String stationToID(String station, String line){
	for (int i = 0; i < dataSplit.size(); i++){
	    if (station.equals(dataSplit.get(i)[1]) && arrayContains(orderSplit.get(lineIndex(line)),dataSplit.get(i)[0])){
		return dataSplit.get(i)[0];
	    }
	}
	throw new NoSuchTrainException("Station not found in Manhattan MTA Station Database!");
    }

    public String IDtoStation(String id) {
	String ans = "";
	int i = 0;
        while (i < dataSplit.size() && !dataSplit.get(i)[0].equals(id)) {
	    i++;
  	}
	try {
	    ans += dataSplit.get(i)[1];
	}
	catch (IndexOutOfBoundsException e) {
	    System.out.println("Invalid Station ID: please insert a valid station ID!");
	    System.exit(1);
	}
	for (int j = 0; j < orderSplit.size(); j++) {
	    if (arrayContains(orderSplit.get(j),id)) {
		ans += ", " + orderSplit.get(j)[0] + " Train";
	    }
	}
	return ans;
    }

    public static void main(String[] args) {
	CSVRoute csv = new CSVRoute();
	ArrayList<String[]> splitData = csv.orderSplit;
	//System.out.println(Arrays.toString(splitData.toArray()));
	for (int i = 0; i < splitData.size(); i++) {
	    System.out.println(Arrays.toString(splitData.get(i)));
	}
	
	System.out.println(splitData.get(0)[1]);
	System.out.println(splitData.get(4)[0]);

	System.out.println(csv.stationToID("South Ferry","1")); //1
	System.out.println(csv.stationToID("23rd St","R"));//118
	//System.out.println(csv.stationToID("28th St","A"));//NoSuchTrainException
	System.out.println(csv.IDtoStation("79"));
    }
}
