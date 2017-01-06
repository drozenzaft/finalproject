import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Layout extends JFrame implements ActionListener{

    private Container pane;

    private JButton calculate;
    private JLabel start, end, route;
    private JTextField sstation, estation;

    public Layout(){
	this.setTitle("IAD's Trip Planner");
	this.setSize(1000,800);
	this.setLocation(100,100);
	this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	this.setBackground(Color.darkGray);

	pane = this.getContentPane();
	pane.setLayout(new GridBagLayout());

	GridBagConstraints test = new GridBagConstraints();
	
	calculate = new JButton("CALCULATE ROUTE");
	calculate.addActionListener(this);
	calculate.setActionCommand("calculate");
	
	start = new JLabel("START");
	end = new JLabel("END");
	route = new JLabel("DIRECTIONS: ");
	
	sstation = new JTextField(12);
	estation = new JTextField(12);

	test.fill = GridBagConstraints.HORIZONTAL;
	
	test.gridx = 0;
	test.gridy = 0;
	pane.add(start,test);

	test.gridx = 1;
	test.gridy = 0;
	pane.add(sstation,test);

	test.fill = GridBagConstraints.HORIZONTAL;
	test.gridx = 0;
	test.gridy = 1;
	pane.add(end,test);

	test.gridx = 1;
	test.gridy = 1;
	pane.add(estation,test);

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
	    route.setText("DIRECTIONS: added directions");
	}
    }
    
    public static void main(String[]args){
	Layout a = new Layout();
	a.setVisible(true);
    }
}
