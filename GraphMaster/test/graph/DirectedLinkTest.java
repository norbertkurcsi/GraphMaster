package graph;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import gui.GraphEditer;
import gui.GraphFrame;
import gui.GraphNode;
import gui.GraphWindow;

public class DirectedLinkTest {
	private GraphEditer editer;
	private GraphNode node1;
	private GraphNode node2;
	private GraphNode node3;
	private GraphNode node4;
	private GraphNode node5;
	
	@Before
	public void initialize() {
		GraphFrame frame = new GraphFrame();
		GraphWindow window = new GraphWindow(frame);
		editer = new GraphEditer(window);
		
		node1 = new GraphNode(editer,1,100,100);
		node2 = new GraphNode(editer,2,100,100);
		node3 = new GraphNode(editer,3,100,100);
		node4 = new GraphNode(editer,4,100,100);
		node5 = new GraphNode(editer,5,100,100);
		editer.addNode(node1);
		editer.addNode(node2);
		editer.addNode(node3);
		editer.addNode(node4);
		editer.addNode(node5);
	}
	
	@Test
	public void testAddEdge() {
		editer.getLinks().addEdge(0,1);
		editer.getLinks().addEdge(2,3);
		editer.getLinks().addEdge(4,0);
		editer.getLinks().addEdge(4,3);
		editer.getLinks().addEdge(4,2);

		int[][] matrix = editer.getLinks().getMatrix();
		assertEquals(1,matrix[0][1]);
		assertEquals(1,matrix[2][3]);
		assertEquals(1,matrix[4][0]);
		assertEquals(1,matrix[4][2]);
		assertEquals(1,matrix[4][3]);
		assertEquals(0,matrix[3][0]);
		assertEquals(0,matrix[3][3]);
		assertEquals(0,matrix[4][4]);
	}
	
	@Test
	public void testChangeEdgeWeight() {
		editer.getLinks().changeEdgeWeight(0,1,1);
		editer.getLinks().changeEdgeWeight(2,3,1);
		editer.getLinks().changeEdgeWeight(4,0,1);
		editer.getLinks().changeEdgeWeight(4,3,1);
		editer.getLinks().changeEdgeWeight(4,2,1);
		
		int[][] matrix = editer.getLinks().getMatrix();
		assertEquals(1,matrix[0][1]);
		assertEquals(1,matrix[2][3]);
		assertEquals(1,matrix[4][0]);
		assertEquals(1,matrix[4][2]);
		assertEquals(1,matrix[4][3]);
		assertEquals(0,matrix[3][0]);
		assertEquals(0,matrix[3][3]);
		assertEquals(0,matrix[4][4]);
	}
}
