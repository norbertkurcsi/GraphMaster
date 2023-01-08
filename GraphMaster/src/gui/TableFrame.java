package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

/**
 * A különböző gráf ábrázolások megjelenítésére szolgáló, frame, amely egy tábéázatot hoz létre
 * és abban jeleníti meg az adatokat
 * 
 * @author Kurcsi Norbert
 *
 */
public class TableFrame extends JFrame{

	private static final long serialVersionUID = 7030938793962838552L;

	public static enum Options{
		POINTPOINT,
		POINTEDGE,
		EDGELIST,
		NEIGHBORLIST
	}
	
	private  JTable table;
	private int[][] representation;
	private NodeList<GraphNode> nodes;
	private JScrollPane scrollPane;
	
	/**
	 * Táblázat inicializálása Pont-Pont mátrix megjelenítéséhez
	 */
	private void initForPointPoint() {
		table = new JTable(new PointPointModel());
		table.setFillsViewportHeight(true);
		scrollPane = new JScrollPane(table);
		JTable firstColumn = new RowNumberTable(table,true);
		scrollPane.setRowHeaderView(firstColumn);
		scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER,
				firstColumn.getTableHeader());
		this.add(scrollPane,BorderLayout.CENTER);
	}
	
	/**
	 * Táblázat inicializálása Pont-Él mátrix megjelenítéséhez
	 */
	private void initForPointEdge() {
		table = new JTable(new PointEdgeModel());
		table.setFillsViewportHeight(true);
		scrollPane = new JScrollPane(table);
		JTable firstColumn = new RowNumberTable(table,false);
		scrollPane.setRowHeaderView(firstColumn);
		scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER,
				firstColumn.getTableHeader());
		this.add(scrollPane,BorderLayout.CENTER);
	}
	
	/**
	 * Táblázat inicializálása Él-lista megjelenítéséhez
	 */
	private void initForEdgeList() {
		table = new JTable(new EdgeListModel());
		table.setFillsViewportHeight(true);
		scrollPane = new JScrollPane(table);
		JTable firstColumn = new RowNumberTable(table,false);
		scrollPane.setRowHeaderView(firstColumn);
		scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER,
				firstColumn.getTableHeader());
		this.add(scrollPane,BorderLayout.CENTER);
	}
	
	/**
	 * Metódus, amely kezeli, hogy milyen ábrázoláshoz kell inicializáni a táblázatot
	 * @param headerText az ábrázolást megjelenítő keret fejlécében megjelenő szöveg
	 * @param op ábrázolás
	 * @param representation az ábrázolás adatait tartalmazó kétdimenziós tömb
	 */
	public void initForOption(String headerText,Options op,int[][] representation) {
		this.representation = representation;
		setTitle(headerText);
		switch(op) {
		case POINTPOINT:
			initForPointPoint();
			break;
		case POINTEDGE:
			initForPointEdge();
			break;
		case EDGELIST:
			initForEdgeList();
			break;
		case NEIGHBORLIST:
			break;
		}
	}
	
	/**
	 * Az ábrázolás megjelenítéséhez használt keret konstruktora
	 * @param headerText a keret fejlécében megjelenő szöveg
	 * @param nodes az ábrázolás által megjelenítendő csomópontok
	 * @param panel panel amely kezeli az ábrázolásokat megjelenítő keret megjelenítését
	 */
	public TableFrame(String headerText, NodeList<GraphNode> nodes,RepresentationPanel panel) {
		super();
		setTitle(headerText);
		this.nodes = nodes;

		setMinimumSize(new Dimension(300,300));
		
		this.addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent e) {
		    	panel.setButtonActive();
		    }
		});
	}
	
	/**
	 * Táblázat model a Pont-pont mátrix adataihoz
	 * 
	 * @author Kurcsi Norbert
	 *
	 */
	private class PointPointModel extends AbstractTableModel{

		private static final long serialVersionUID = -2429020708192734830L;

		@Override
		public int getRowCount() {
			return representation.length;
		}

		@Override
		public int getColumnCount() {
			return representation.length;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			return representation[rowIndex][columnIndex];
		}
		
		@Override
		public String getColumnName(int col) {
			return String.valueOf(nodes.get(col).getNumber());
		}
		
		@Override
		public boolean isCellEditable(int row, int col) {
			return false;
		}
		
	}
	
	
	/**
	 * Táblázat model a Pont-él mátrix adataihoz
	 * 
	 * @author Kurcsi Norbert
	 *
	 */
	private class PointEdgeModel extends AbstractTableModel{

		private static final long serialVersionUID = -297865815178213379L;

		@Override
		public int getRowCount() {
			return representation.length;
		}

		@Override
		public int getColumnCount() {
			if(representation.length == 0) {
				return 0;
			}
			return representation[0].length;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			return representation[rowIndex][columnIndex];
		}
		
		@Override
		public String getColumnName(int col) {
			return String.valueOf(nodes.get(col).getNumber());
		}
		
		@Override
		public boolean isCellEditable(int row, int col) {
			return false;
		}
		
	}
	
	/**
	 * Táblázat model a Él-lista adataihoz
	 * 
	 * @author Kurcsi Norbert
	 *
	 */
	private class EdgeListModel extends AbstractTableModel{

		private static final long serialVersionUID = -2429020708192734830L;

		@Override
		public int getRowCount() {
			return representation.length;
		}

		@Override
		public int getColumnCount() {
			if(representation.length == 0) {
				return 0;
			}
			return 2;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			return nodes.get(representation[rowIndex][columnIndex]).getNumber();
		}
		
		@Override
		public String getColumnName(int col) {
			if(col == 0) {
				return "Kezdő";
			} else {
				return "Vég";
			}
		}
		
		@Override
		public boolean isCellEditable(int row, int col) {
			return false;
		}
	}
}