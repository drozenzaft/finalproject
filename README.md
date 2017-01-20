# finalproject
APCS1 2016-17 Final Project: IaD's Trip Planner<br><br>
Collaborators:<br>
Ida Wang<br>
Daniel Rozenzaft<br><br>

NOTE: THE VERSION IN THE MASTER BRANCH IS SIGNIFICANTLY LESS STABLE THAN THE ONE WE DEMOED IN CLASS, WHICH IS THE VERSION IN THE DEMO2 BRANCH. <br>
IT INCLUDES MINOR CHANGES TO THE SCROLL BAR IN THE GUI AND TWO CLEARLY SEPARATE METHODS, DIRECTIONS AND DIRECTIONS2, FOR 0 TRANSFERS AND 1 TRANSFERS, RESPECTIVELY. (tl;dr: grade demo2 branch pls c;)
Ever wanted a downgraded version of Google Maps? Well look no further, because now you can use IaD's (stands for Ida and Daniel... we know we're very original) Trip Planner to calculate your route! (Given that you are only traveling in Manhattan, you can only travel by subway, and knowing how long it will take is not all that important...)<br><br>
List of Working Features:
<ul>
<li>route calculation between two stations on the same line</li>
<li>GUI to input stations (see unresolved bugs)</li>
<li>reverse button to switch stations</li>
<li>GUI scroll bar to see directions that may extend over the container without resizing (see unresolved bugs)</li>
<li>SOME transfers work; it seems like transfers between stops without repeat names usually work fine</li>
</ul>
List of Unresolved Bugs:
<ul>
<li>transfers and regular directions for stations with repeat names (ex: 23 St) or stations with multiple names (ex: Park Place, World Trade Center, and Chambers St) don't always work>/li>
<li>stops with multiple lines stopping at them are much more unstable than others, so Times Square - 42 St may be a little bit wonky</li>
<li>for a few of the transfers that do work, some of the directions give very long routes instead of the simplest ones</li>
<li>the program detects one transfer or less, so it will print an error in the gui that says that it couldn't find a route if the trains that stop at the start/end stops don't intersect anywhere</li>
<li>exact station name must be given (cat data.csv for the names)</li>
<li>asking for a route when there is no input in the GUI spews a bunch of error messages in the terminal (doesn't really matter if you only look at the GUI though)</li>
<li>duplicate station names are not clarified, so if a station such as "14 St" is typed, then the program will assume it is the first "14 St" that shows up in the data file</li>
<li>intermediate stops sometimes breaks and displays an incomplete list of stops or a list of intermediate stops from another train
<li>GUI label for directions won't resize because of the scroll bar unless there is enough room for the entire text to fit</li>
</ul><br>
Directions:<br>
Layout.java must be compiled. Running Layout will produce a GUI (resize as needed), where the user can input the start and end stations (for a list of station names [which need to be typed exactly as they are in the data file], type "cat data.csv" in the terminal). In case it isn't clear, the "SWITCH" button reverses the start and end stations, and "CALCULATE" produces directions for the route.


