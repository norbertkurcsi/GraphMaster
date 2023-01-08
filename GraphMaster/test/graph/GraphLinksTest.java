package graph;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import gui.GraphEditer;
import gui.GraphFrame;
import gui.GraphNode;
import gui.GraphWindow;

public class GraphLinksTest {
	private GraphEditer editer;
	private GraphNode node5;
	
	@Before
	public void initialize() {
		GraphFrame frame = new GraphFrame();
		GraphWindow window = new GraphWindow(frame);
		editer = new GraphEditer(window);
		
		GraphNode node1 = new GraphNode(editer,1,100,100);
		GraphNode node2 = new GraphNode(editer,2,100,100);
		GraphNode node3 = new GraphNode(editer,3,100,100);
		GraphNode node4 = new GraphNode(editer,4,100,100);
		node5 = new GraphNode(editer,5,100,100);
		editer.addNode(node1);
		editer.addNode(node2);
		editer.addNode(node3);
		editer.addNode(node4);
		editer.addNode(node5);
		
		editer.addEdge(node1, node2);
		editer.addEdge(node3, node4);
		editer.addEdge(node5, node1);
		editer.addEdge(node5, node4);
		editer.addEdge(node5, node3);
	}
	
	@Test
	public void testGetMatrix() {
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
	public void testDeleteNode() {
		editer.getLinks().deleteNode(4);
		int[][] matrix = editer.getLinks().getMatrix();
		assertEquals(1,matrix[0][1]);
		assertEquals(1,matrix[2][3]);
		assertEquals(0,matrix[3][0]);
		assertEquals(0,matrix[3][2]);
		assertEquals(0,matrix[3][3]);
		assertEquals(0,matrix[1][0]);
		assertEquals(0,matrix[1][1]);
		assertEquals(0,matrix[2][1]);
	}
	
	@Test
	public void testDijkstra() {
		int[] route = editer.getLinks().dijkstra(node5);
		assertEquals(4,route[0]);
		assertEquals(0,route[1]);
		assertEquals(4,route[2]);
		assertEquals(4,route[3]);
		assertEquals(-1,route[4]);
	}
	
	@Test
	public void testBFS() {
		List<Step> steps = editer.getLinks().bfs(node5);
		assertEquals(5,steps.get(0).getFrom().getNumber());
		assertEquals(1,steps.get(0).getTarget().getNumber());
		
		assertEquals(5,steps.get(1).getFrom().getNumber());
		assertEquals(3,steps.get(1).getTarget().getNumber());
		
		assertEquals(5,steps.get(2).getFrom().getNumber());
		assertEquals(4,steps.get(2).getTarget().getNumber());
		
		assertEquals(1,steps.get(3).getFrom().getNumber());
		assertEquals(2,steps.get(3).getTarget().getNumber());
	}
	
	@Test
	public void testDFS() {
		List<Step> steps = editer.getLinks().dfs(node5);
		assertEquals(5,steps.get(0).getFrom().getNumber());
		assertEquals(1,steps.get(0).getTarget().getNumber());
		
		assertEquals(1,steps.get(1).getFrom().getNumber());
		assertEquals(2,steps.get(1).getTarget().getNumber());
		
		assertEquals(5,steps.get(2).getFrom().getNumber());
		assertEquals(3,steps.get(2).getTarget().getNumber());
		
		assertEquals(3,steps.get(3).getFrom().getNumber());
		assertEquals(4,steps.get(3).getTarget().getNumber());
	}
	
	@Test
	public void testConvertToEdgeList() {
		int[][] edgeList = editer.getLinks().converToEdgeList();
		System.out.println(edgeList[0][0]);
		assertEquals(0,edgeList[0][0]);
		assertEquals(1,edgeList[0][1]);
		
		assertEquals(2,edgeList[1][0]);
		assertEquals(3,edgeList[1][1]);
		
		assertEquals(4,edgeList[3][0]);
		assertEquals(3,edgeList[3][1]);
		
		assertEquals(4,edgeList[4][0]);
		assertEquals(4,edgeList[2][0]);

	}
}
