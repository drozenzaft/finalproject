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
    
    public int stops(String sstation, String estation, String subway){
	String sID = stationToID(sstation,subway);
	String eID = stationToID(estation,subway);
	
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
		stops = "</strong> stops <strong>";
	    }
	    //ans += "<br>Directions from " + stop1 + " to " + stop2 + ":";
	    ans += "<br>  1. Start at <strong>" + stop1 + "</strong>.<br>";
	    if (difference != 0) {
		ans += "2. Take the <strong>" + orderSplit.get(trainIndex)[0] + "</strong> train <strong>" + Math.abs(difference) + stops + direction + "</strong>.<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Intermediate Stops:";
	    }
	    if (difference > 0) {
			int l = 1;
			while (arrayIndex(removeTrain,id1)+l-1 <= arrayIndex(removeTrain,idTwo)-1) {
			    ans += "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + IDtoStation(removeTrain[arrayIndex(removeTrain,id1)+l]);
			    l++;
			}
		    }
	    if (difference < 0) {
		int m = -1;
		while (arrayIndex(removeTrain,id1)+m-1 >= arrayIndex(removeTrain,idTwo)-1) {
		    //System.out.println(arrayIndex(orderSplit.get(trainIndex),idTwo));
		    ans += "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + IDtoStation(removeTrain[arrayIndex(removeTrain,id1)+m]);
		    m--;
		}
	    }
	    String numStep = "";
	    if (difference != 0) {
		numStep += "<br>3";
	    }
	    else {
		numStep += "2";
	    }
	    ans += numStep + ". Arrive at <strong>" + stop2 + "</strong>.";
	    return ans;
	}
	catch (NoSuchTrainException e) {
	    throw new NoSuchTrainException("These stations are not served by an MTA station in Manhattan. Please insert a real station.");
	}
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


	/*
	System.out.println(csv.directions("14th St","Chambers St"));
	System.out.println(csv.directions("Chambers St","14th St"));
	System.out.println(csv.directions("68th St - Hunter College","14 St - Union Square"));
	System.out.println(csv.directions("14 St - Union Square","96th St"));
	System.out.println(csv.directions("125th St","14 St - Union Square"));
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
