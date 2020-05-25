package Graph;

import java.io.PrintStream;

import FlowGraph.FlowGraph;
import RegAlloc.InterferenceGraph;
import Temp.DefaultMap;
import Temp.Temp;
import Temp.TempMap;

//dot -Tpng colour-graph.txt -o out.png;display out.png
public class GraphvisRenderer implements GraphRenderer {

    private String toColor(String str) {
        if (str == null)
            return "white";
        switch (str) {
            case "rax":
                return "bisque2";
            case "rsi":
                return "antiquewhite4";
            case "r8":
                return "aquamarine4";
            case "r9":
                return "blue1";
            case "r10":
                return "brown";
            case "r11":
                return "burlywood";
            case "r12":
                return "coral";
            case "r13":
                return "cyan3";
            case "r14":
                return "darkgoldenrod3";
            case "r15":
                return "dimgrey";
            case "rbx":
                return "white";
            case "rdi":
                return "firebrick";
            case "rdx":
                return "deeppink1";
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
        for (NodeList p = interferenceGraph.nodes(); p != null; p = p.tail) {
            Node n = p.head;
            Temp t = interferenceGraph.gtemp(n);
            out.print(t);
            out.print(" -- {");
            for (NodeList q = n.succ(); q != null; q = q.tail) {
                Temp ta = interferenceGraph.gtemp(q.head);
                out.print(ta.toString());
                if (q.tail != null) {

                    out.print(",");
                }
            }
            out.println("}");
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