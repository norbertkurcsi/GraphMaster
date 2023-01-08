package graph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import gui.GraphEditer;
import gui.GraphFrame;
import gui.GraphNode;
import gui.GraphWindow;

public class GraphEditerTest {
	private GraphEditer editer;
	private GraphWindow window;
	
	@Before
	public void init() {
		GraphFrame frame = new GraphFrame();
		window = new GraphWindow(frame);
		editer = new GraphEditer(window);
	}
	
	@Test
	public void testFileExists() {
		File added = new File(System.getProperty("user.dir") + "/saves/1234.txt");
		assertFalse(editer.fileExists(added));
		try {
			added.createNewFile();
		} catch (IOException e) {

		}
		assertTrue(editer.fileExists(added));
		
		added.delete();
	}
	
	@Test
	public void testLoadGraph() {
		GraphEditer newEditer = new GraphEditer(window);
		GraphNode node1 = new GraphNode(editer,1,100,100);
		GraphNode node2 = new GraphNode(editer,2,100,100);
		GraphNode node3 = new GraphNode(editer,3,100,100);
		GraphNode node4 = new GraphNode(editer,4,100,100);
		GraphNode node5 = new GraphNode(editer,5,100,100);
		newEditer.addNode(node1);
		newEditer.addNode(node2);
		newEditer.addNode(node3);
		newEditer.addNode(node4);
		newEditer.addNode(node5);
		
		newEditer.addEdge(node1, node2);
		newEditer.addEdge(node3, node4);
		newEditer.addEdge(node5, node1);
		newEditer.addEdge(node5, node4);
		newEditer.addEdge(node5, node3);
		
		editer.loadGraph(newEditer);
		
		assertEquals(5,editer.getNodes().size());
		assertEquals(5,editer.getEdges().size());
		
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
	public void testGetNodeIndex() {
		GraphNode node1 = new GraphNode(editer,1,100,100);
		GraphNode node2 = new GraphNode(editer,2,100,100);
		GraphNode node3 = new GraphNode(editer,3,100,100);
		GraphNode node4 = new GraphNode(editer,4,100,100);
		GraphNode node5 = new GraphNode(editer,5,100,100);
		editer.addNode(node1);
		editer.addNode(node2);
		editer.addNode(node3);
		editer.addNode(node4);
		
		assertEquals(0,editer.getNodeIndex(node1));
		assertEquals(1,editer.getNodeIndex(node2));
		assertEquals(2,editer.getNodeIndex(node3));
		assertEquals(3,editer.getNodeIndex(node4));
		assertEquals(-1,editer.getNodeIndex(node5));
	}
	
	@Test
	public void testChangeEdgeWeight() {
		GraphNode node1 = new GraphNode(editer,1,100,100);
		GraphNode node2 = new GraphNode(editer,2,100,100);
		editer.addNode(node1);
		editer.addNode(node2);
		editer.addEdge(node1, node2);
		
		assertEquals(1,editer.getLinks().getMatrix()[0][1]);
		
		editer.changeEdgeWeight(node1, node2, 5);
		
		assertEquals(5,editer.getLinks().getMatrix()[0][1]);
	}

}
