package Graph;

import java.io.PrintStream;

import FlowGraph.FlowGraph;
import RegAlloc.InterferenceGraph;
import Temp.TempMap;

public interface GraphRenderer {

	void render(PrintStream out, Graph graph);

	void render(PrintStream out, InterferenceGraph graph, TempMap tempMap);

	void render(PrintStream out, FlowGraph graph);
}