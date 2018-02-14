import javax.swing.JFrame;
import javax.swing.JPanel;

public class GraphicsMain extends JFrame{
	
	private static final long serialVersionUID = 5637739624068832487L;

	public static void main(String[] args){
		GraphicsMain window = new GraphicsMain();
	    JPanel p = new JPanel();
	    p.add(new GraphicsPanel()); 
	    window.setTitle("Chess Wit Awsumneses and M3mez");
	    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    window.setContentPane(p);
	    window.pack();
	    window.setLocationRelativeTo(null);
	    window.setVisible(true);
	}
}
