package Liveness;

import Util.TaskProvider;
import Util.TaskRegister;


/**
 * F|flowgraph-dump - dump the flow graphs
 * V|liveness-dump - dump the liveness graphs
 * N|interference-dump - dump the interference graphs.
 * all depend on inst-compute ( generate the assembly for target machine )
 */
public class Tasks implements TaskProvider {

    @Override
    public void build(TaskRegister taskRegister) {
        // TODO Auto-generated method stub
        //sets felds in the content...

    }
    
}
