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

	pane = this.getContentPane();
	pane.setLayout(new FlowLayout());

	calculate = new JButton("CALCULATE ROUTE");
	calculate.addActionListener(this);
	calculate.setActionCommand("calculate");
	
	start = new JLabel("START");
	end = new JLabel("END");
	route = new JLabel("DIRECTIONS: ");
	
	sstation = new JTextField(12);
	estation = new JTextField(12);

	pane.add(start);
	pane.add(sstation);
	pane.add(end);
	pane.add(estation);
	pane.add(calculate);
	pane.add(route);
    }

    public void actionPerformed(ActionEvent e){
	String event = e.getActionCommand();
	if(event.equals("calculate")){
	    route.setText("DIRECTIONS: added directions");
	}
    }
    
    public static void main(String[]args){
	try {
	// Set System L&F
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
