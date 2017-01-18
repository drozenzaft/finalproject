import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Layout extends JFrame implements ActionListener{

    private Container pane;

    private JButton calculate, swap;
    private JLabel start, end, route;
    private JTextField sstation, estation;

    public Layout(){
	this.setTitle("IAD's Trip Planner");
	this.setSize(800,600);
	this.setMinimumSize(new Dimension(700,500));
	this.setLocation(100,100);
	this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	this.setBackground(Color.black);

	pane = this.getContentPane();
	pane.setLayout(new GridBagLayout());

	GridBagConstraints test = new GridBagConstraints();
	
	calculate = new JButton("CALCULATE ROUTE");
	calculate.addActionListener(this);
	calculate.setActionCommand("calculate");
	
	start = new JLabel("START");
	end = new JLabel("END");
	route = new JLabel("<html>DIRECTIONS:</html> ");
	
	sstation = new JTextField(12);
	estation = new JTextField(12);

	swap = new JButton("SWITCH");
	swap.addActionListener(this);
	swap.setActionCommand("swap");
     
	//test.fill = GridBagConstraints.HORIZONTAL;
	
	test.gridx = 0;
	test.gridy = 0;
	pane.add(start,test);
	test.insets = new Insets(0,0,0,10);

	test.gridx = 1;
	test.gridy = 0;
	pane.add(sstation,test);
	sstation.setMinimumSize(new Dimension(sstation.getPreferredSize()));

	test.gridx = 0;
	test.gridy = 1;
	pane.add(end,test);
	test.insets = new Insets(0,0,0,10);

	test.gridx = 1;
	test.gridy = 1;
	pane.add(estation,test);
	estation.setMinimumSize(new Dimension(estation.getPreferredSize()));
	
        test.gridx = 2;
	test.gridy = 0;
	test.fill = GridBagConstraints.VERTICAL;
	test.insets = new Insets(0,30,0,0);
	test.gridheight = 2;
	test.gridwidth = 1;
	pane.add(swap,test);

	test.gridx = 0;
	test.gridy = 2;
	test.fill = GridBagConstraints.HORIZONTAL;
	test.insets = new Insets(30,0,30,0);
	test.ipady = 40;
	test.gridheight = 2;
        test.gridwidth = 3;
	pane.add(calculate,test);

	test.gridx = 0;
	test.gridy = 4;
	test.fill = GridBagConstraints.HORIZONTAL;
	test.insets = new Insets(0,0,0,0);
	test.gridheight = 1;
	test.gridwidth = 3;
	pane.add(route,test);

	JScrollPane scroll = new JScrollPane(route,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	pane.add(scroll,test);
	scroll.setMinimumSize(new Dimension(scroll.getPreferredSize()));

	/*
	pane.add(start);
	pane.add(sstation);
	pane.add(end);
	pane.add(estation);
	pane.add(calculate);
	pane.add(route);
	*/
    }

    public void actionPerformed(ActionEvent e){
	String event = e.getActionCommand();
	if(event.equals("calculate")){
	    CSVRoute csv = new CSVRoute();
	    String start = sstation.getText();
	    String end = estation.getText();

	    try{
		if(csv.combinedLines(start,end).size() == 0){
		    route.setText("<html>DIRECTIONS:<br>" +
				  csv.directions(start,end,false)
				  + "</html>");
		}else{
		    route.setText("<html>DIRECTIONS:<br>" +
				  csv.directions2(start,end)+"</html>");
		}
	    }catch(IndexOutOfBoundsException d){
		route.setText("<html>No train serves both of these stations.<br>Please ensure that the station name was typed correctly.<html>");
	    }catch(NoSuchTrainException n){
		route.setText("<html>No train serves both of these stations.<br>Please ensure that the station name was typed correctly.<html>");
	    }catch(StopsNotOnSameLineException l){
		route.setText("<html>These stations are not served by a single train;<br>please wait for more in-station transfers to be available.<br>Thank you for your cooperation.<html>");
	    }
	}
	if(event.equals("swap")){
	    String a = sstation.getText();
	    sstation.setText(estation.getText());
	    estation.setText(a);
	}
    }
    
    public static void main(String[]args){
	/*
	try {
	//increases size because I can't see anything on my laptop
	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	} 
	catch (UnsupportedLookAndFeelException e) {
	    // handle exception
	}
	catch (ClassNotFoundException e) {
	    // handle exception
	}
	catch (InstantiationException e) {
	    // handle exception
	}
	catch (IllegalAccessException e) {
	    // handle exception
	}
	*/
	Layout a = new Layout();
	a.setVisible(true);
    }
}

