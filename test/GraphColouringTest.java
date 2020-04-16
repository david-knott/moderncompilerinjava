import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.junit.Test;

import FlowGraph.FlowGraph;
import Graph.Node;
import Main.Main;
import RegAlloc.InterferenceGraph;
import RegAlloc.MoveList;
import RegAlloc.Worklist;
import Temp.Temp;
import Temp.TempList;

public class GraphColouringTest {

    class TestLivenessGraph extends FlowGraph {

        @Override
        public TempList def(Node node) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public TempList use(Node node) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public boolean isMove(Node node) {
            // TODO Auto-generated method stub
            return false;
        }
    } 
    class TestInterferenceGraph extends InterferenceGraph {

        @Override
        public Node tnode(Temp temp) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Temp gtemp(Node node) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public MoveList moves() {
            // TODO Auto-generated method stub
            return null;
        }
    }

    @Test
    public void buildWorkListForGraphWithLessThanK() {
        //expect this to create a worklist object from initial

        //if degree of node is >= K, add it to spillWorkList

        //if node is move related add it to freezeWorkList

        //otherwise add to simply worklist

        //for graph with all nodes < K, where nodes are not move related

        //we expect the spillWorkList & freezeWorkList to be empty

        //the simplify worklist should contain all the nodes
        var ig = new TestInterferenceGraph();
        Worklist worklist = new Worklist(ig);
        assertNotNull(worklist.getSpillWorklist());
        assertNotNull(worklist.getFreezeWorklist());
        assertNotNull(worklist.getimplifyWorklist());


    }
    
}