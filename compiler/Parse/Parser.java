package Parse;

public interface Parser {

    public Program parse();

    public boolean hasErrors();

	public void setParserTrace(boolean value);
}