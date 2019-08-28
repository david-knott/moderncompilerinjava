package chap3.programming.Parse;

interface Lexer {
    public java_cup.runtime.Symbol nextToken() throws java.io.IOException;
}
