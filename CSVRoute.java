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
    //String[][] of all the stations with more than 1 line - first element of each String[] is the station ID
    public String[][] transfers(){
	String[][] transfers = new String[dataSplit.size()][];
	int length = 0;
	for(int i = 0; i < dataSplit.size(); i++){
	    if(stationToLines3(dataSplit.get(i)[0]).length > 2){
		transfers[length] = stationToLines3(dataSplit.get(i)[0]);
		length++;
	    }
	}
	return Arrays.copyOf(transfers,length);
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
	for (String ID : ary) {
	    if (ID.equals(goal)) {
		return true;
	    }
	}
	return false;
    }

    public ArrayList<String> stationToID(String station) {
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
	int line = lineIndex(subway);

	while(sindex == -1 && eindex == -1){
	    for(int i = 1; i < orderSplit.get(line).length; i++){
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

    public int stops2(String sID, String eID, String subway){   
	int stops = 0;
	int sindex = -1;
	int eindex = -1;
	int line = lineIndex(subway);

	while(sindex == -1 && eindex == -1){
	    for(int i = 1; i < orderSplit.get(line).length; i++){
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

    public String direction(String stop1, String stop2,String subway) {
	int dist = stops(stop1,stop2,subway);
	if (dist < 0) {
	    return "downtown";
	}
	else {
	    return "uptown";
	}
    }

    public String[] removeTrainName(String[] trainRoute) {
	String[] temp = new String[trainRoute.length-1];
	for (int i = 0; i < temp.length; i++) {
	    temp[i] = trainRoute[i+1];
	}
	return temp;
    }
    
    public String directions(String stop1, String stop2) {
	String ans = "";
	String direction = "";
	String id1 = "";
	String idTwo = "";
	int distance = 1000000;
	int currentDistance = 0;
	int trainIndex = -1;
	//section 1 of the method: these three loops set each of the variables that section 2, the printer, needs in order to print proper directions and throw exceptions when improper station names are inputted.
	try {
	    //these first two outer loops use the single-parameter stationToID method, which returns an arraylist of all the IDs of stations with the name of that stop. this allows them to check each "23 St", "72 St", or any repeat station name; otherwise, only one instance of, say, "23 St" would be checked. these two loops are necessary because if the train(s) that stop at that instance do not happen stop at the second stop, then this directions method would not find a route, and the program would rarely work.
	    for (String id : stationToID(stop1)) {
		for (String id2 : stationToID(stop2)) {
		    for (int i = 0; i < orderSplit.size(); i++) {
			if (arrayContains(orderSplit.get(i),id) && arrayContains(orderSplit.get(i),id2)) {
			    currentDistance = Math.abs(stops(stop1,stop2,orderSplit.get(i)[0]));
			    direction = direction(stop1, stop2, orderSplit.get(i)[0]);
			    if (currentDistance < distance || (currentDistance == distance && Math.random() > 0.5)) { //the or statement allows the program to randomize the result when the route is the same for multiple trains, so it doesn't return the same train every time when there are multiple possible trains that you can take
				distance = currentDistance;
				//trainIndex: the train that will be taken
				trainIndex = i;
				//id1 and idTwo (id2 is already used): IDs of the stations being used (again, this is necessary because of repeat station names but different IDs)
				id1 = id;
				idTwo = id2;
			    }
			}
		    }
		}
	    }
	    if (trainIndex == -1) {
		throw new StopsNotOnSameLineException("These stations are not served by a single train; please wait for in-station transfers to be supported. Thank you for your cooperation.");
	    }
	    if (distance == 0) {
		throw new SameStopInputException("Please insert two differerent station names. If you are looking for a crosstown route, please take a crosstown bus. Crosstown bus routes can be found at www.mta.info.");
	    }
	    //section 2 of this method: this prints the route. we thought that there are too many variables from section 1 needed in section 2 for this to be put in a separate method.
	    String[] trainStops = removeTrainName(orderSplit.get(trainIndex));
	    //because we're crazy perfectionists, this is necessary
	    String verbTense = "";
	    if (distance == 1) {
		verbTense = "</strong> stop <strong>";
	    }
	    else {
		verbTense = "</strong> stops <strong>";
	    }
	    //prints starting station
	    ans += "<br>  1. Start at <strong>" + stop1 + "</strong>.<br>";
	    //prints which train to take, sets up the printing of the intermediate stops
	    ans += "2. Take the <strong>" + orderSplit.get(trainIndex)[0] + "</strong> train <strong>" + distance + verbTense + direction + "</strong>.<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Intermediate Stops:";
	    //the next loop prints intermediate stops, or the stops between the starting stop and the destination stop. there are two loops: a forward loop for uptown and a backward loop for downtown.
	    //forward loop for uptown	
	    if (direction.equals("uptown")) {
		int l = 1;
		while (arrayIndex(trainStops,id1)+l-1 <= arrayIndex(trainStops,idTwo)-1) {
		    ans += "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + IDtoStation(trainStops[arrayIndex(trainStops,id1)+l]);
		    l++;
		}
	    }
	    //backward loop for downtown
	    else {
		int m = -1;
		while (arrayIndex(trainStops,id1)+m-1 >= arrayIndex(trainStops,idTwo)-1) {
		    //System.out.println(arrayIndex(orderSplit.get(trainIndex),idTwo));
		    ans += "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + IDtoStation(trainStops[arrayIndex(trainStops,id1)+m]);
		    m--;
		}
	    }
	    //prints destination startion
	    ans += "<br>  3. Arrive at <strong>" + stop2 + "</strong>.<br>";
	    return ans;
	}
	catch (NoSuchTrainException e) {
	    throw new NoSuchTrainException("These stations are not served by an MTA station in Manhattan. Please insert a real station.");
	}
    }

    // input: ID of a single station
    // returns an ArrayList of all the lines that stop at that station
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
    //int[] of the line indexes in orderSplit
    public int[] stationToLines2(String ID){
	int[] lines = new int[10];
	int length = 0;
	for(int i = 0; i < orderSplit.size(); i++){
	    int counter = 0;
	    boolean exists = false;
	    while(!exists && counter < orderSplit.get(i).length){
		if(ID.equals(orderSplit.get(i)[counter])){
		    lines[length] = i;
		    length++;
		    exists = true;
		}
		counter++;
	    }
	}
	return Arrays.copyOf(lines,length);
    }
    //String[] of the line names in orderSplit; first element is stationID
    public String[] stationToLines3(String ID){
	String[] lines = new String[10];
	lines[0] = ID;
	int length = 1;
	for(int i = 0; i < orderSplit.size(); i++){
	    boolean exists = false;
	    int counter = 0;
	    while(!exists && counter < orderSplit.get(i).length){
		if(ID.equals(orderSplit.get(i)[counter])){
		    lines[length] = orderSplit.get(i)[0];
		    length++;
		    exists = true;
		}
		counter++;
	    }
	}
	return Arrays.copyOf(lines,length);
    }

    // input: the IDs of a start and end station
    // returns an ArrayList of the lines that have both stations
    // ie. combinedLines(14 St,Chambers St) returns 1,2,3
    public ArrayList<String> combinedLines(String sID, String eID){
	
	ArrayList<String> lines = new ArrayList<String>();
	ArrayList<String> first = stationToLines(sID);
	ArrayList<String> last = stationToLines(eID);

	//could be more efficient but just trying to make it work ...
	for(int f = 0; f < first.size(); f++){
	    for(int l = 0; l < last.size(); l++){
		if((first.get(f)).equals(last.get(l))){
		    lines.add(first.get(f));
		}
	    }
	}
	return lines;
    }

    // combinedLines, except it return the lines with the least number of stops in between, and that number 
    // ie. fastestLines(14 St,Chambers St) returns 2(stops),2,3
    public ArrayList<String> fastestLines(String sID, String eID){
	ArrayList<String> all = combinedLines(sID,eID);
	ArrayList<String> fast = new ArrayList<String>();
	
	int fastest = 100;

	for(int i = 0; i < all.size(); i++){
	    if(stops2(sID,eID,all.get(i)) < fastest){
		fast.clear();
		fastest = stops2(sID,eID,all.get(i));
		fast.add(""+fastest);
		fast.add(all.get(i));
	    }
	    else if(stops2(sID,eID,all.get(i)) == fastest){
		fast.add(all.get(i));
	    }
	}
	return fast;
    }

    //only for two stations not on the same line
    //returns the first available transfer station between start and end
    public String nextTransfer(String sID,String eID){
        int[] options = stationToLines2(sID);
	for(int l = 0; l < options.length; l++){
	    for(int s = 1; s < orderSplit.get(options[l]).length; s++){
		if(combinedLines(eID,orderSplit.get(options[l])[s]).size() > 0){
		    return orderSplit.get(options[l])[s];
		}
	    }
	}
	return "";
    }

    public String directions2(String sID,String eID){
	String result = "";
	int stops = 0;
	int stops2 = 0;
	String direction1 = "uptown ";
	String direction2 = "uptown ";
	String transfer = nextTransfer(sID,eID);
	if(combinedLines(sID,eID).size() == 0 && transfer.length() > 0){
	    String line1 = combinedLines(sID,transfer).get(0);
	    String line2 = combinedLines(transfer,eID).get(0);
	    stops = stops2(sID,transfer,line1);
	    stops2 = stops2(transfer,eID,line2);
	    if(stops < 0){
		stops = -stops;
		direction1 = "downtown ";
	    }
	    if(stops2 < 0){
		stops2 = -stops2;
		direction2 = "downtown ";
	    }
	    return "Take the " + line1 + " train " + direction1 + stops + " stops to " + IDtoStation(transfer) + ".\nTransfer to the " + line2 + " train and travel " + stops2 + " stops " + direction2 + "to reach " + IDtoStation(eID) +  ".";
	}
	return result;
    }
    
    public static void main(String[] args) {
	CSVRoute csv = new CSVRoute();
	/*
	System.out.println(csv.directions("Chambers St","96 St"));
	
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

	System.out.println("");
	
	int[] d = csv.stationToLines2("13A");
	for(int i = 0; i < d.length; i++){
	    System.out.println(csv.orderSplit.get(d[i])[0]);
	}

	System.out.println("");
	*/
	System.out.println(csv.nextTransfer("47","3"));
	System.out.println(csv.directions2("47","3") + "\n");
	System.out.println(csv.directions2("8A","47") + "\n");
	System.out.println(csv.directions2("60","107") + "\n");

	String[][] transfers = csv.transfers();
	ArrayList<String[]> stations = csv.dataSplit;
	System.out.println(stations.size());
	System.out.println(transfers.length);
	for(int i = 0; i < transfers.length; i++){
	    System.out.println(csv.IDtoStation(transfers[i][0]));
	}
    }
}
