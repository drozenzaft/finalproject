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

    public ArrayList<String> stationToID(String station){
	ArrayList<String> IDs = new ArrayList<String>();
	for(int i = 0; i < dataSplit.size(); i++){
	    if(station.equals(dataSplit.get(i)[1])){
		IDs.add(dataSplit.get(i)[0]);
	    }
	}
	return IDs;
    }
		
    public String stationToID(String station, String line){
	for (int i = 0; i < dataSplit.size(); i++){
	    if (station.equals(dataSplit.get(i)[1]) && arrayContains(orderSplit.get(lineIndex(line)),dataSplit.get(i)[0])){
		return dataSplit.get(i)[0];
	    }
	}
	throw new NoSuchTrainException("Station not found in Manhattan MTA Station Database!");
    }

    public int stops(String sID, String eID, String subway){	
	int stops = 0;
	int sindex = -1;
	int eindex = -1;
	int line = -1;

	while(line == -1){
	    for(int i = 0; i < orderSplit.size(); i++){
		if(subway.equals(orderSplit.get(i)[0])){
		    line = i;
		}
	    }
	}

	while(sindex == -1 && eindex == -1){
	    for(int i = 0; i < orderSplit.get(line).length; i++){
		if(sID.equals(orderSplit.get(line)[i])){
		    sindex = i;
		}
		if(eID.equals(orderSplit.get(line)[i])){
		    eindex = i;
		}
	    }
	}
	
	return eindex - sindex;
    }
    
    public ArrayList<String> stationToLines(String ID){
	
	ArrayList<String> lines = new ArrayList<String>();
	
	for(int i = 0; i < orderSplit.size(); i++){
	    boolean exists = false;
	    int counter = 0;
	    
	    while(!exists && counter < orderSplit.get(i).length){
		if(ID.equals(orderSplit.get(i)[counter])){
		    lines.add(orderSplit.get(i)[0]);
		    exists = true;
		}
		counter++;
	    }
	}
	return lines;
    }

    public ArrayList<String> combinedLines(String sID, String eID){
	
	ArrayList<String> lines = new ArrayList<String>();
	ArrayList<String> first = stationToLines(sID);
	ArrayList<String> last = stationToLines(eID);

	//could be more efficient but just trying to make it work atm...
	for(int f = 0; f < first.size(); f++){
	    for(int l = 0; l < last.size(); l++){
		if((first.get(f)).equals(last.get(l))){
		    lines.add(first.get(f));
		}
	    }
	}
	return lines;
    }

    public ArrayList<String> fastestLines(String sID, String eID){
	ArrayList<String> all = combinedLines(sID,eID);
	ArrayList<String> fast = new ArrayList<String>();
	
	int fastest = stops(sID,eID,all.get(0));

	for(int i = 1; i < all.size(); i++){
	    if(stops(sID,eID,all.get(i)) < fastest){
		fast.clear();
		fast.add(all.get(i));
		fastest = stops(sID,eID,all.get(i));
	    }
	    else if(stops(sID,eID,all.get(i)) == fastest){
		fast.add(all.get(i));
	    }
	}

	return fast;
    }
	

    public static void main(String[] args) {
	CSVRoute csv = new CSVRoute();
	ArrayList<String[]> splitData = csv.orderSplit;
	//System.out.println(Arrays.toString(splitData.toArray()));

	/*
	for (int i = 0; i < splitData.size(); i++) {
	    System.out.println(Arrays.toString(splitData.get(i)));
	}
        */

	//System.out.println(csv.orderSplit.size());

	/*
	ArrayList<String> a = csv.stationToLines("13A");
	for(int i = 0; i < a.size(); i++){
	    System.out.println(a.get(i));
	}
	*/

	ArrayList<String> b = csv.combinedLines("13A","47"); //Times Square 42nd and Grand Central
	for(int i = 0; i < b.size(); i++){
	    System.out.println(b.get(i));
	}
	
	System.out.println("");

	ArrayList<String> c = csv.combinedLines("3","12");
	for(int i = 0; i < c.size(); i++){
	    System.out.println(c.get(i));
	}

	System.out.println("");
	
	ArrayList<String> c2 = csv.fastestLines("3","12");
	for(int i = 0; i < c2.size(); i++){
	    System.out.println(c2.get(i));
	}
    }
}
