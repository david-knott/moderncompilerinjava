package Parse;

import Util.TaskContext;

public interface Parser {

    public void parse(TaskContext context);

	public void setParserTrace(boolean value);
}