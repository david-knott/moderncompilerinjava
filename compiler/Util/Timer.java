package Util;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;

public class Timer {

    private Long begin;
    private Long end;

    class TimeVar {

        final String name;
        long start;
        long stop;
        long elapsed;

        public TimeVar(String name) {
            this.name = name;
        }

        public boolean isZero() {
            return (this.start - this.stop) == 0;
        }

        public void start() {
            // store reference to first timing.
            if(begin == null) {
                begin = System.currentTimeMillis();
            }
            this.start = System.currentTimeMillis();
        }

        public void stop() {
            this.stop = System.currentTimeMillis();
            this.elapsed+= this.stop - this.start;
        }

        public double elapsed() {
            return this.elapsed / 1000d;
        }
    }

    public static final Timer instance = new Timer();
    final Stack<TimeVar> tasks = new Stack<TimeVar>();
    final Map<String, TimeVar> table = new LinkedHashMap<String, TimeVar>();

    public void push(String name) {
        // stop previous task if any.
        if(!this.tasks.empty()) {
            this.tasks.peek().stop();
        }
        // create and start next task
        TimeVar current = new TimeVar(name);
        this.table.put(name, current);
        this.tasks.push(current);
        current.start();
    }

    public void pop() {
        Assert.assertIsTrue(!this.tasks.empty());
        this.tasks.peek().stop();
        this.tasks.pop();
        // set the start time of the previous task if present
        if(!this.tasks.empty()) {
            this.tasks.peek().start();
        }
    }

    private String displayTiming(TimeVar timing) {
        return "0 (totaltime ? ( time * 100 / totaltime )) : time) %)";
    }
   
    public void dump(OutputStream outputStream) {
        try(PrintStream ps = new PrintStream(outputStream)) {
            // execution time, time for each function to execute
            ps.println("Total execution times ( seconds )");
            for(String key : this.table.keySet()) {
                TimeVar timing = this.table.get(key);
                if(!timing.isZero()) {
                    ps.println(" " + timing.name + " " + timing.elapsed());
                }
            }
            // cumulative time, timings from start to end of compilation
            /*
            ps.println("Cumulative times ( seconds )");
            for(String key : this.table.keySet()) {
                TimeVar timing = this.table.get(key);
                ps.println(" " + timing.name + " " + (((double)timing.stop - this.begin)/1000));
            }
            */
            // total time, time to complete compilation.
            ps.println("Total ( seconds ) : " + ((double)this.end - this.begin)/ 1000);
        }
    }

    /**
     * Signals to the timer that we are finished timing. This
     * ensures any remaning timings are stopped. It also records
     * the time when this function was called.
     */
	public void done() {
        if(!this.tasks.empty()) {
            do {
                this.tasks.peek().stop();
                this.tasks.pop();
            }while(!this.tasks.empty());
        }
        // store reference to last timing.
        this.end = System.currentTimeMillis();
	}
}