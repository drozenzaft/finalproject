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
	if (IDs.size() > 0) {
	    return IDs;
	}
	else {
	    //System.out.println(Arrays.toString(IDs.toArray()));
	    throw new NoSuchTrainException("Station not found in Manhattan MTA Station Database!");
	}
    }
		
    public String stationToID(String station, String line){
	for (int i = 0; i < dataSplit.size(); i++){
	    if (station.equals(dataSplit.get(i)[1]) && arrayContains(orderSplit.get(lineIndex(line)),dataSplit.get(i)[0])){
		return dataSplit.get(i)[0];
	    }
	}
	throw new NoSuchTrainException("Station not found in Manhattan MTA Station Database!");
    }

    public int stops(String start, String end, String subway){
	String sID = stationToID(start,subway);
	String eID = stationToID(end,subway);
	
	int stops = 0;
        int line = -1;

	int sindex = -1;
	int eindex = -1;

	while(line == -1){
	    for(int i = 0; i < 22; i++){
		//System.out.println(i + ": " + orderSplit.get(i)[0]);	   
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
    
    public int arrayIndex(String[] ary, String goal) {
	for (int i = 0; i < ary.length; i++) {
	    if (goal.equals(ary[i])) {
		return i;
	    }
	}
	throw new IndexOutOfBoundsException("Input string not found in array; no index could be returned");
    }

    public String IDtoStation(String id) {
	for (String[] line : dataSplit) {
	    if (line[0].equals(id)) {
		return line[1];
	    }
	}
	throw new NoSuchTrainException("No station with the inputted ID was found");
    }
    public String directions(String stop1, String stop2) {
	String ans = "";
	String direction = "";
	String id1 = "";
	String idTwo = "";
	int difference = 0;
	int tempDifference = 0;
	int count = 0;
	int trainIndex = -1;
	try {
	    for (String id : stationToID(stop1)) {
		//System.out.println(id+"?");
		for (String id2 : stationToID(stop2)) {
		    //System.out.println(id2+"!");
		    for (int i = 0; i < orderSplit.size(); i++) {
			if (orderSplit.get(i)[0].equals("4")) {
			    // System.out.println(id + ", " + id2);
			}
			if (arrayContains(orderSplit.get(i),id) && arrayContains(orderSplit.get(i),id2)) {
			    tempDifference = stops(stop1,stop2,orderSplit.get(i)[0]);
			    // System.out.println(tempDifference + "," + difference);
			    if (count == 0 || Math.abs(tempDifference) < Math.abs(difference) || Math.abs(tempDifference) == Math.abs(difference) && Math.random() > 0.5) {
				//System.out.println(i);
				//	System.out.println("HI!");
				difference = tempDifference;
				//	System.out.println(i+"?");
				trainIndex = i;
				id1 = id;
				idTwo = id2;
			    }
			    if (difference < 0) {
				direction = "downtown";
			    }
			    else {
				direction = "uptown";
			    }
			    count++;
			    if (count == 3) {
				break;
			    }
			}
		    }
		}
	    }
	    String[] removeTrain = new String[orderSplit.get(trainIndex).length-1];
	    for (int z = 1; z < orderSplit.get(trainIndex).length; z++) {
		removeTrain[z-1] = (orderSplit.get(trainIndex)[z]);
	    }
	    if (trainIndex == -1) {
		//	System.out.println(direction);
		throw new StopsNotOnSameLineException("These stations are not served by a single train; please wait for in-station transfers to be supported. Thank you for your cooperation.");
	    }
		    // System.out.println(trainIndex + "!");
	    String stops = "";
	    if (Math.abs(difference) == 1) {
		stops = " stop ";
	    }
	    else {
		stops = " stops ";
	    }
	    ans += "\nDirections from " + stop1 + " to " + stop2 + ":";
	    ans += "\n  1. Start at " + stop1 + ".\n  2. Take the " + orderSplit.get(trainIndex)[0] + " train " + Math.abs(difference) + stops + direction + ".\n       Intermediate Stops:\n";
	    if (difference > 0) {
			int l = 1;
			while (arrayIndex(removeTrain,id1)+l-1 <= arrayIndex(removeTrain,idTwo)-1) {
			    ans += "          " + IDtoStation(removeTrain[arrayIndex(removeTrain,id1)+l]) + "\n";
			    l++;
			}
		    }
	    else {
		int m = -1;
		while (arrayIndex(removeTrain,id1)+m-1 >= arrayIndex(removeTrain,idTwo)-1) {
		    //System.out.println(arrayIndex(orderSplit.get(trainIndex),idTwo));
		    ans += "          " + IDtoStation(removeTrain[arrayIndex(removeTrain,id1)+m]) + "\n";
		    m--;
		}
	    }
	    ans += "  3. Arrive at " + stop2 + ".";
	    return ans;
	}
	catch (NoSuchTrainException e) {
	    throw new NoSuchTrainException("These stations are not served by an MTA station in Manhattan. Please insert a real station.");
	}
    }
	
    public static void main(String[] args) {
	CSVRoute csv = new CSVRoute();
	ArrayList<String[]> splitData = csv.orderSplit;
	//System.out.println(Arrays.toString(splitData.toArray()));

	/*
	for (int i = 0; i < splitData.size(); i++) {
	    System.out.println(Arrays.toString(splitData.get(i)));
	}
	
	System.out.println(splitData.get(0)[1]);
	System.out.println(splitData.get(4)[0]);
	System.out.println(csv.stationToID("South Ferry","1")); //1
	System.out.println(csv.stationToID("23rd St","R"));//118
	//System.out.println(csv.stationToID("28th St","A"));//NoSuchTrainException
	System.out.println(csv.stationToID("23rd St").get(0)); // 10,55,92,100,118
	System.out.println(csv.stationToID("23rd St").get(4)); // 118
	*/

	//	System.out.println(csv.orderSplit.get(0)[0]);
	/*
	System.out.println(csv.orderSplit.get(0)[1]);
	System.out.println(Arrays.toString(csv.orderSplit.get(0)));
	System.out.println(csv.orderSplit.size());
	//System.out.println(csv.orderSplit.get(csv.orderSplit.size()-2)[0]);
	*/
	System.out.println(csv.directions("14th St","Chambers St"));
	System.out.println(csv.directions("Chambers St","14th St"));
	System.out.println(csv.directions("68th St - Hunter College","14 St - Union Square"));
	System.out.println(csv.directions("14 St - Union Square","96th St"));
	System.out.println(csv.directions("125th St","14 St - Union Square"));
    }
}
