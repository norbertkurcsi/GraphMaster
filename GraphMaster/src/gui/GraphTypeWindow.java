package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Egy olyan ablakot jelenít meg, ls kezeli a rajta lévő gombokat, ahol ki lehet válassztani, hogy
 * az új gráfnak milyen típust szeretnénk.
 * 
 * @author Kurcsi Norbert
 *
 */
public class GraphTypeWindow extends JPanel implements ActionListener{

	private static final long serialVersionUID = -1981601672093858372L;
	
	// ablakkeret, amiben megjelenik az ablak
	private GraphFrame frame;
	
	// irányított gráf létrehozására gomb
	private JButton directedButton;
	
	// irányítatlan gráf létrehozására gomb
	private JButton notDirectedButton;
	
	// visszagomb
	private JButton backButton;
	
	/**
	 * Gombok megnyomása esetén használt enumeráció, hogy eldöntsük melyik gomb lett megnyomva
	 * 
	 * @author Kurcsi Norbert
	 *
	 */
	private enum Actions {
		DIRECTED,
		NOTDIRECTED,
		BACK
	}
	
	
	/**
	 * Inicializája a címszöveget, illetve hozzáadja az ablakhoz
	 */
	private void initTitlePanel() {
		JLabel title = new JLabel("Graph Master");
		title.setFont(new Font("MV Boli", Font.BOLD, 40));
		title.setForeground(Color.white);
		title.setHorizontalAlignment(JLabel.CENTER);
		
		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new BorderLayout());
		titlePanel.setBackground(new Color(0,0,0,0));
		titlePanel.add(title);
		titlePanel.setPreferredSize(new Dimension(0,70));
		titlePanel.setBorder(BorderFactory.createMatteBorder(0,0,1,0,Color.white));

		this.add(titlePanel,BorderLayout.NORTH);
	}
	
	/**
	 * Inicializája és beállítja a gombokat, illetve hozzájuk adja az ActioListener-eket
	 */
	private void initButtons() {
		directedButton = new JButton("Irányított");
		notDirectedButton = new JButton("Irányítatlan");
		backButton = new JButton("Vissza");
		
		directedButton.setActionCommand(Actions.DIRECTED.name());
		notDirectedButton.setActionCommand(Actions.NOTDIRECTED.name());
		backButton.setActionCommand(Actions.BACK.name());
		
		directedButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		directedButton.setHorizontalTextPosition(SwingConstants.CENTER);
		
		notDirectedButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		notDirectedButton.setHorizontalTextPosition(SwingConstants.CENTER);
		
		directedButton.setFont(new Font("Arial", Font.PLAIN, 30));
		notDirectedButton.setFont(new Font("Arial", Font.PLAIN, 30));
		backButton.setFont(new Font("Arial", Font.PLAIN, 25));
		
		backButton.setPreferredSize(new Dimension(200,50));
		
		directedButton.addActionListener(this);
		notDirectedButton.addActionListener(this);
		backButton.addActionListener(this);
		
		ImageIcon directed = new ImageIcon("img/iranyitott.jpg");
		ImageIcon notdirected = new ImageIcon("img/iranyitatlan.jpg");
		
		directedButton.setIcon(directed);
		notDirectedButton.setIcon(notdirected);
	}
	
	/**
	 * Létrehozza a gráftípusok kiválasztására szolgáló gombokat tartalmazó paneleket
	 */
	private void initPanels() {
		JPanel mainPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,50,50));
		mainPanel.setBackground(new Color(0,0,0,0));
		JPanel panel1 = new JPanel();
		panel1.setBackground(new Color(0,0,0,0));
		
		panel1.add(directedButton);
		mainPanel.add(panel1);
		
		JPanel panel2 = new JPanel();
		panel2.setBackground(new Color(0,0,0,0));

		
		panel2.add(notDirectedButton);
		mainPanel.add(panel2);
		
		this.add(mainPanel,BorderLayout.CENTER);
	}
	
	/**
	 * Létrehozza a visszagombot tartalmazó panelt, és hozzáadja az ablakhzo
	 */
	private void initFooter() {
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(0,100));
		panel.setBackground(new Color(0,0,0,0));
		
		panel.add(backButton);
		this.add(panel,BorderLayout.SOUTH);
	}
	
	
	/**
	 * A gráf kiválasztására szolgáló ablak létrehozása, megadva a keretet ahol meg lesz jelenítve,
	 * meghívja az elemek init függvényeit.
	 * @param frame akeret ahol kirajzolódik az ablak
	 */
	public GraphTypeWindow(GraphFrame frame) {
		this.frame = frame;
		this.setLayout(new BorderLayout());
		
		initTitlePanel();
		initButtons();
		initPanels();
		initFooter();
	}
	
	/**
	 * A gombok megnyomásával kapcsolatos megnyomása esetén hívodó metódusokat kezeli
	 * @param e event
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand() == Actions.DIRECTED.name()) {
			frame.changeWindow(this,frame.getGraphWindow(true));
		} else if(e.getActionCommand() == Actions.NOTDIRECTED.name()) {
			frame.changeWindow(this,frame.getGraphWindow(false));
		} else if(e.getActionCommand() == Actions.BACK.name()) {
			frame.changeWindow(this,frame.getStartingWindow());
		}
	}
	
	/**
	 * A háttér kiszínezése
	 * @param g grafikus felület ahova a hátteret rajzoljuk
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Image hatter = null;
		if (hatter == null) {
			hatter = (new ImageIcon("img/background.jpg")).getImage();
		}
		// Kirajzoljuk a háttérképet.
		g.drawImage(hatter, 0, 0, frame.getWidth(), frame.getHeight(), null);
	}
	
}