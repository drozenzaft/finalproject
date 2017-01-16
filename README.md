# finalproject
APCS1 2016-17 Final Project: IaD's Trip Planner<br><br>
Collaborators:<br>
Ida Wang<br>
Daniel Rozenzaft<br><br>
Ever wanted a downgraded version of Google Maps? Well look no further, because now you can use IaD's (stands for Ida and Daniel... we know we're very original) Trip Planner to calculate your route! (Given that you are only traveling in Manhattan, you can only travel by subway, and knowing how long it will take is not all that important...)<br><br>
List of Working Features:
<ul>
<li>route calculation between two stations on the same line</li>
<li>GUI to input stations (can reverse their order without retyping)</li>
</ul>
List of Unresolved Bugs:
<ul>
<li>cannot actually support transfers... (really glitchy, and still not incorporated in the GUI)</li>
<li>exact String of the station name must be given for calculations to work</li>
<li>asking for a route when there is no input in the GUI spews a bunch of error messages in the terminal (doesn't really matter if you only look at the GUI though)</li>
<li>duplicate station names are not clarified, so if a station such as "14 St" is typed, then the program will assume it is the first "14 St" that shows up in the data file</li>
<li>[insert other bugs I cannot remember here...]</li>
</ul><br>
Directions:<br>
Both CSVRoute.java and Layout.java must be compiled. Running Layout will produce a GUI, where the user can input the start and end stations. In case it isn't clear, the "SWITCH" button reverses the start and end stations, and "CALCULATE" produces directions for the route.


