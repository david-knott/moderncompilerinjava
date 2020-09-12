package Liveness;

import Translate.DataFrag;
import Translate.FragList;
import Translate.FragmentVisitor;
import Translate.ProcFrag;
import Tree.TreeSimplifierVisitor;
import Tree.PrettyPrinter;
import Util.SimpleTask;
import Util.SimpleTaskProvider;
import Util.TaskContext;
import Util.TaskProvider;


/**
 * F|flowgraph-dump - dump the flow graphs
 * V|liveness-dump - dump the liveness graphs
 * N|interference-dump - dump the interference graphs.
 * all depend on inst-compute ( generate the assembly for target machine )
 */
public class Tasks implements TaskProvider {

    @Override
    public void build() {
        // TODO Auto-generated method stub
        //sets felds in the content...

    }
    
}
