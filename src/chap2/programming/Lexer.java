package chap2.programming;
import JLex.*;

interface Lexer {
    public java_cup.runtime.Symbol nextToken() throws java.io.IOException;
}
