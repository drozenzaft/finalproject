import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Window extends JFrame implements ActionListener{

    private Container pane;

    private JButton calculate;
    private JLabel start, end, route;
    private JTextField sstation, estation;

    public Window(){
	this.setTitle("IAD's Trip Planner");
	this.setSize(1000,800);
	this.setLocation(100,100);
	this.setDefaultCloseOperation(EXIT_ON_CLOSE);

	pane = this.getContentPane();
	pane.setLayout(new FlowLayout());

	calculate = new JButton("CALCULATE ROUTE");
	f.addActionListener(this);
	f.setActionCommand("calculate");
	
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
	    route = "DIRECTIONS: added directions";
	}
    }
    
    public static void main(String[]args){
	Window a = new Window();
	a.setVisible(true);
    }
}
