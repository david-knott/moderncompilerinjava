package RegAlloc;

import Assem.InstrList;
import FlowGraph.AssemFlowGraph;
import FlowGraph.FlowGraph;

class FlowGraphBuilder {
	public FlowGraph create(InstrList instrList) {
		return new AssemFlowGraph(instrList);
	}
}