package Graph;

import java.io.PrintStream;

import FlowGraph.FlowGraph;
import RegAlloc.InterferenceGraph;
import Temp.Temp;
import Temp.TempMap;

//dot -Tpng colour-graph.txt -o out.png;display out.png
public class GraphvisRenderer implements GraphRenderer {

    private String toColor(String str) {
        if (str == null)
            return "white";
        switch (str) {
            case "eax":
                return "bisque2";
            case "esi":
                return "antiquewhite4";
            case "r8d":
                return "aquamarine4";
            case "r9d":
                return "blue1";
            case "r10d":
                return "brown";
            case "r11d":
                return "burlywood";
            case "r12d":
                return "coral";
            case "r13d":
                return "cyan3";
            case "r14d":
                return "darkgoldenrod3";
            case "r15d":
                return "dimgrey";
            case "ebx":
                return "white";
            case "edi":
                return "firebrick";
            case "edx":
                return "deeppink1";
            case "ebp":
                return "pink1";
            case "esp":
                return "orange";
            case "ecx":
                return "pink2";
            default:
                throw new Error(str + " has no mapping");
        }
    }

    public void render(PrintStream out, InterferenceGraph interferenceGraph, TempMap tempMap) {
        out.println("graph D{");
        for (NodeList p = interferenceGraph.nodes(); p != null; p = p.tail) {
            Node n = p.head;
            Temp t = interferenceGraph.gtemp(n);
            String tm = tempMap.tempMap(t);
            String colour = toColor(tm);
            String reg = t.toString();
            out.println(t + " [ style=filled fillcolor=\"" + colour + "\" label=\"" + tm + " [" + reg + "]\"]");
        }
        var moves = interferenceGraph.moves();
        for (NodeList p = interferenceGraph.nodes(); p != null; p = p.tail) {
            Node n = p.head;
            Temp t = interferenceGraph.gtemp(n);
            for (NodeList q = n.succ(); q != null; q = q.tail) {
                out.print(t);
                out.print(" -- {");
                Temp ta = interferenceGraph.gtemp(q.head);
                out.print(ta.toString());
                if (moves != null && moves.contains(p.head, q.head))
                    out.println("} [style=dashed]");
                else
                    out.println("} [style=solid]");
            }
        }
        out.println("}");
    }

    public void render(PrintStream out, FlowGraph flowGraph, TempMap tempMap) {
        out.println("digraph D{");
        for (NodeList p = flowGraph.nodes(); p != null; p = p.tail) {
            Node n = p.head;
            String tm = flowGraph.instr(n).format(tempMap);
            out.println(n.toString() + " [ label=\"" + tm + "\"]");
        }
        for (NodeList p = flowGraph.nodes(); p != null; p = p.tail) {
            Node n = p.head;
            out.print(n.toString());
            out.print(" -> {");
            for (NodeList q = n.succ(); q != null; q = q.tail) {
                out.print(q.head.toString());
                if (q.tail != null) {

                    out.print(",");
                }
            }
            out.println("}");
        }
        out.println("}");
    }
}