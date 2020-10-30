package Parse;

import Absyn.Absyn;

public interface Parser {

    public Absyn parse();

    public boolean hasErrors();

	public void setParserTrace(boolean value);
}