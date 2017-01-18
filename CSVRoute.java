import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;

public class CSVRoute {
    private ArrayList<String> data,order;
    private ArrayList<String[]> dataSplit,orderSplit;
    private String firstLine;
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

    public static String removeLetters(String a) {
	String ans = "";
	for (int i = 0; i < a.length(); i++) {
	    if (!Character.isLetter(a.charAt(i))) {
		ans += a.charAt(i);
	    }
	}
	return ans;
    }

    public static String[] clearLetters(String[] a) {
	for (int i = 1; i < a.length; i++) {
	    a[i] = removeLetters(a[i]);
	}
	return a;
    }

    public static String[] copy(String[] a) {
	String[] temp = new String[a.length];
	for (int i = 0; i < temp.length; i++) {
	    temp[i] = a[i];
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
			    if (currentDistance < distance || (currentDistance == distance && Math.random() > 0.5)) { //the or statement allows the program to randomize the result when the route is the same for multiple trains, so it doesn't return the same train every time when there are multiple possible trains that you can take
				distance = currentDistance;
				direction = direction(stop1, stop2, orderSplit.get(i)[0]);
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
	    //System.out.println(Arrays.toString(orderSplit.get(trainIndex)));
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
		    //System.out.println(Arrays.toString(trainStops));
		    ans += "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + IDtoStation(trainStops[arrayIndex(trainStops,id1)+m]);
		    m--;
		}
	    }
	    ans += "<br>";
	    //prints destination startion
	    
	    ans += "3. Arrive at <strong>" + stop2 + "</strong>.<br>";
	    return ans;
    }
    catch (NoSuchTrainException e) {
	throw new NoSuchTrainException("These stations are not served by an MTA station in Manhattan. Please insert a real station.");
	}
    }
    
    public String directions(String stop1, String stop2,boolean transfer,boolean transferred) {
	String ans = "";
	String direction = "";
	String id1 = "";
	String idTwo = "";
	String deLetter1 = "";
	String deLetter2 = "";
	String[] letterFree;
	int distance = 1000000;
	int currentDistance = 0;
	int trainIndex = -1;
	//section 1 of the method: these three loops set each of the variables that section 2, the printer, needs in order to print proper directions and throw exceptions when improper station names are inputted.
	try {
	    //these first two outer loops use the single-parameter stationToID method, which returns an arraylist of all the IDs of stations with the name of that stop. this allows them to check each "23 St", "72 St", or any repeat station name; otherwise, only one instance of, say, "23 St" would be checked. these two loops are necessary because if the train(s) that stop at that instance do not happen stop at the second stop, then this directions method would not find a route, and the program would rarely work.
	    for (String id : stationToID(stop1)) {
		for (String id2 : stationToID(stop2)) {
		    for (int i = 0; i < orderSplit.size(); i++) {
			letterFree = copy(orderSplit.get(i));
			deLetter1 = id;
			deLetter2 = id2;
			if (transfer) {
			    clearLetters(letterFree);
			    removeLetters(deLetter1);
			    removeLetters(deLetter2);
			}
			if (arrayContains(letterFree,deLetter1) && arrayContains(letterFree,deLetter2)) {
			    if (transfer) {
				for (int p = 0; p < letterFree.length; p++) {
				    if (removeLetters(stationToID(stop1,firstLine)).equals(letterFree[p])) {
					stop1 = IDtoStation(letterFree[p]);
				    }
				}
			    }
			    currentDistance = Math.abs(stops(stop1,stop2,orderSplit.get(i)[0]));
			    if (currentDistance < distance || (currentDistance == distance && Math.random() > 0.5)) { //the or statement allows the program to randomize the result when the route is the same for multiple trains, so it doesn't return the same train every time when there are multiple possible trains that you can take
				distance = currentDistance;
				direction = direction(stop1, stop2, orderSplit.get(i)[0]);
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
	    //System.out.println(Arrays.toString(orderSplit.get(trainIndex)));
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
	    if (!transfer) {
		ans += "<br>  1. Start at <strong>" + stop1 + "</strong>.<br>";
	    }
	    //prints which train to take, sets up the printing of the intermediate stops
	    if (!transfer) {
		ans += "2. Take the <strong>" + orderSplit.get(trainIndex)[0] + "</strong> train <strong>" + distance + verbTense + direction + "</strong>.<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Intermediate Stops:";
	    }
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
		    //System.out.println(Arrays.toString(trainStops));
		    ans += "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + IDtoStation(trainStops[arrayIndex(trainStops,id1)+m]);
		    m--;
		}
	    }
	    ans += "<br>";
	    //prints destination startion
	    
	    if (!transfer) {
		ans += "3. Arrive at <strong>" + stop2 + "</strong>.<br>";
	    }
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
	    int counter = 1;
	    
	    while(!exists && counter < orderSplit.get(i).length){
		if(removeLetters(ID).equals(removeLetters(orderSplit.get(i)[counter]))){
		    lines.add(orderSplit.get(i)[0]);
		    exists = true;
		}
		counter++;
	    }
	}
	return lines;
    }

    //input: ID of some station
    //returns an array of the indices (in orderSplit) of the trains that stop at that station
    public int[] stationToLines2(String ID){
	int[] lines = new int[20];
	int length = 0;
	for(int i = 0; i < orderSplit.size(); i++){
	    int counter = 0;
	    boolean exists = false;
	    while(!exists && counter < orderSplit.get(i).length){
		if(removeLetters(ID).equals(removeLetters(orderSplit.get(i)[counter]))){
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
		if(first.get(f).equals(last.get(l))){
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
	int tempSum;
	int stopSum = 1000000;
	String ans = "";
        int[] options = stationToLines2(sID);
		for(int l = 0; l < options.length; l++){
	    for(int s = 1; s < orderSplit.get(options[l]).length; s++){
		if (combinedLines(eID,orderSplit.get(options[l])[s]).size() > 0) {
		    tempSum =  Math.abs(stops2(orderSplit.get(options[l])[s],sID,orderSplit.get(options[l])[0])) + Math.abs(stops2(orderSplit.get(options[l])[s],eID,fastestLines(orderSplit.get(options[l])[s],eID).get(0)));
		    if (ans.length() == 0 || tempSum <= stopSum && arrayIndex(orderSplit.get(options[l]),orderSplit.get(options[l])[s]) > arrayIndex(orderSplit.get(options[l]),ans)) {
			stopSum = tempSum;
			ans = orderSplit.get(options[l])[s];
		    }
		}
	    }
	}
	    return ans;
    }
    public String directions2(String sID,String eID){
	String sID2 = stationToID(sID).get(0);
	String eID2 = stationToID(eID).get(0);
	String result = "";
	int stops = 0;
	int stops2 = 0;
	String direction1 = "<strong>uptown</strong>";
	String direction2 = "<strong>uptown</strong>";
	String nextTransfer = "hi";
	nextTransfer = nextTransfer(sID2,eID2);
	if(combinedLines(sID2,eID2).size() == 0 && nextTransfer.length() > 0){
	    //System.out.println(nextTransfer(id,id2));
	    System.out.println(sID2+","+eID2);
	    String line1 = combinedLines(sID2,nextTransfer).get(0);
	    String line2 = combinedLines(nextTransfer,eID2).get(0);
	    stops = stops2(sID2,nextTransfer,line1);
	    stops2 = stops2(nextTransfer,eID2,line2);
	    if(stops < 0){
		stops = -stops;
		direction1 = "<strong>downtown</strong>";
	    }
	    if(stops2 < 0){
		stops2 = -stops2;
		direction2 = "<strong>downtown</strong>";
	    }
	    String verbTense = " stops ";
	    if (Math.abs(stops) == 1) {
		verbTense = " stop ";
	    }
	    firstLine = line1;
	    result += "<br>1. Start at <strong>" + sID + "</strong>.<br>";
	    result += "2. Take the <strong>" + line1 + " </strong>train <strong>" + Math.abs(stops) + " </strong>" + verbTense + direction1+ ".";
	    result += "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Intermediate Stops:";
	    System.out.println(IDtoStation(nextTransfer));
	    result += directions(sID,IDtoStation(nextTransfer),true,false) + "3. Transfer to the <strong>" + line2 + " </strong>train.<br>";
	    if (Math.abs(stops2) == 1) {
		verbTense = " stop ";
	    }
	    else {
		verbTense = " stops ";
	    }
	    result += "4. Take the <strong>" + line2 + "</strong> train " + Math.abs(stops2) + verbTense + direction2 + ".";
	    result += "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Intermediate Stops:";
	    result += directions(IDtoStation(nextTransfer),eID,true,true);
	    result += "5. Arrive at <strong>" + eID + "</strong>.<br>";
	}
	else {
	    return directions(sID,eID,false,false);
	}
	return result;
    }

    public String directions3(String sstation,String estation){
	String sID = stationToID(sstation).get(0);
	String eID = stationToID(estation).get(0);
	String[][] stops = transfers();
	String[] starts = new String[stops.length];
	//String[] ends = new String[stops.length];
	int slength = 0;
	for(int i = 0; i < stops.length; i++){
	    if(combinedLines(sID,stops[i][0]).size() > 0){
		starts[slength] = stops[i][0];
		slength++;
	    }
	}
	starts = Arrays.copyOf(starts,slength);
	System.out.println("");

	String[] transfers = new String[starts.length];
	int tlength = 0;
	
	for(int i = 0; i < starts.length; i++){
	    if(combinedLines(starts[i],eID).size() > 0){
		transfers[tlength] = starts[i];
		tlength++;
	    }
	}
	if(tlength == 0){
	    throw new StopsNotOnSameLineException();
	}
	transfers = Arrays.copyOf(transfers,tlength);
	
	String tstation = IDtoStation(transfers[0]);
	
	return directions(sstation,tstation,false,false) + directions(tstation,estation,true,true);
    }
    
    public static void main(String[] args) {
	CSVRoute csv = new CSVRoute();
	/*
	System.out.println(csv.directions("Chambers St","96 St",false,false));
	
	ArrayList<String> b = csv.combinedLines("13A","47"); //Times Square 42nd and Grand Central
	System.out.println(Arrays.toString(b.toArray()));
	
	System.out.println("");

	ArrayList<String> c = csv.combinedLines("3","12");
	System.out.println(Arrays.toString(c.toArray()));

	System.out.println("");
	
	ArrayList<String> c2 = csv.fastestLines("3","12");
	System.out.println(Arrays.toString(c2.toArray()));

	System.out.println("");
	
	int[] d = csv.stationToLines2("13A");
	System.out.println(Arrays.toString(d));
	System.out.println("");

	System.out.println(csv.fastestLines("28","77"));
	System.out.println("");
	//System.out.println(csv.nextTransfer("20","77"));
	System.out.println("");
	System.out.println("");
	System.out.println(csv.directions2("96 St","Inwood - 207 St") + "\n");
	*/
	System.out.println(csv.directions3("34 St - Hudson Yards","Chambers St")+"\n");
	System.out.println(csv.directions3("72 St","34 St - Herald Square")+"\n");
	System.out.println(csv.directions3("96 St","Inwood - 207 St"));
    }
}
