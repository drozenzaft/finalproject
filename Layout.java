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
	route = new JLabel("<html><br>DIRECTIONS:</html> ");
	
	sstation = new JTextField(12);
	estation = new JTextField(12);

	swap = new JButton("SWITCH");
	swap.addActionListener(this);
	swap.setActionCommand("swap");

        test.gridx = 0;
	test.gridy = 0;
	test.fill = GridBagConstraints.VERTICAL;
	test.gridheight = 2;
	pane.add(swap,test);
     
	//test.fill = GridBagConstraints.HORIZONTAL;
	
	test.gridx = 1;
	test.gridy = 0;
	test.gridheight = 1;
	pane.add(start,test);

	test.gridx = 2;
	test.gridy = 0;
	pane.add(sstation,test);

	test.gridx = 1;
	test.gridy = 1;
	pane.add(end,test);

	test.gridx = 2;
	test.gridy = 1;
	pane.add(estation,test);

	test.gridx = 3;
	test.gridy = 0;
	test.fill = GridBagConstraints.VERTICAL;
	test.gridheight = 2;
	pane.add(calculate,test);

	test.gridx = 0;
	test.gridy = 2;
	test.fill = GridBagConstraints.HORIZONTAL;
	test.gridwidth = 3;
	pane.add(route,test);

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
		route.setText("<html><br>DIRECTIONS:<br>" +
			      csv.directions(start,end)+"</html>");
	    }catch(IndexOutOfBoundsException d){
		route.setText("No train serves both of these stations. Please ensure that the station name was typed correctly.");
	    }
	}
	if(event.equals("swap")){
	    String a = sstation.getText();
	    sstation.setText(estation.getText());
	    estation.setText(a);
	}
    }
    
    public static void main(String[]args){
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

	Layout a = new Layout();
	a.setVisible(true);
    }
}

