
//----------------------------------------------------
// The following code was generated by CUP v0.10k
// Fri Oct 18 22:33:21 IST 2019
//----------------------------------------------------

package Parse;


/** CUP v0.10k generated parser.
  * @version Fri Oct 18 22:33:21 IST 2019
  */
public class Grm extends java_cup.runtime.lr_parser {

  /** Default constructor. */
  public Grm() {super();}

  /** Constructor which sets the default scanner. */
  public Grm(java_cup.runtime.Scanner s) {super(s);}

  /** Production table. */
  protected static final short _production_table[][] = 
    unpackFromStrings(new String[] {
    "\000\027\000\002\002\004\000\002\003\003\000\002\004" +
    "\003\000\002\004\003\000\002\005\007\000\002\005\002" +
    "\000\002\006\004\000\002\006\002\000\002\010\004\000" +
    "\002\010\002\000\002\012\015\000\002\015\003\000\002" +
    "\015\003\000\002\015\003\000\002\015\005\000\002\015" +
    "\005\000\002\016\005\000\002\016\002\000\002\017\005" +
    "\000\002\013\006\000\002\011\003\000\002\011\003\000" +
    "\002\007\003" });

  /** Access to production table. */
  public short[][] production_table() {return _production_table;}

  /** Parse-action table. */
  protected static final short[][] _action_table = 
    unpackFromStrings(new String[] {
    "\000\055\000\010\002\ufffc\004\ufffa\046\006\001\002\000" +
    "\006\002\ufffe\004\042\001\002\000\004\002\057\001\002" +
    "\000\010\047\ufff8\055\ufff8\056\ufff8\001\002\000\004\002" +
    "\uffff\001\002\000\004\002\000\001\002\000\010\047\013" +
    "\055\012\056\017\001\002\000\004\004\045\001\002\000" +
    "\006\004\ufffa\050\ufffa\001\002\000\010\047\uffec\055\uffec" +
    "\056\uffec\001\002\000\010\047\ufff9\055\ufff9\056\ufff9\001" +
    "\002\000\010\047\uffed\055\uffed\056\uffed\001\002\000\004" +
    "\004\020\001\002\000\004\025\021\001\002\000\014\004" +
    "\022\005\024\006\026\016\023\036\027\001\002\000\010" +
    "\047\ufff6\055\ufff6\056\ufff6\001\002\000\006\007\ufff0\017" +
    "\ufff0\001\002\000\010\047\ufff4\055\ufff4\056\ufff4\001\002" +
    "\000\010\047\uffee\055\uffee\056\uffee\001\002\000\010\047" +
    "\ufff5\055\ufff5\056\ufff5\001\002\000\004\051\030\001\002" +
    "\000\004\004\031\001\002\000\010\047\ufff2\055\ufff2\056" +
    "\ufff2\001\002\000\006\007\033\017\034\001\002\000\004" +
    "\004\035\001\002\000\010\047\ufff3\055\ufff3\056\ufff3\001" +
    "\002\000\004\010\037\001\002\000\006\007\ufff1\017\ufff1" +
    "\001\002\000\004\004\040\001\002\000\006\007\uffef\017" +
    "\uffef\001\002\000\006\004\042\050\044\001\002\000\010" +
    "\002\uffeb\004\uffeb\050\uffeb\001\002\000\010\002\ufffb\004" +
    "\ufffb\050\ufffb\001\002\000\004\002\ufffd\001\002\000\004" +
    "\010\046\001\002\000\004\004\047\001\002\000\004\035" +
    "\050\001\002\000\004\004\051\001\002\000\004\014\052" +
    "\001\002\000\004\006\053\001\002\000\004\015\054\001" +
    "\002\000\004\051\055\001\002\000\004\006\056\001\002" +
    "\000\010\047\ufff7\055\ufff7\056\ufff7\001\002\000\004\002" +
    "\001\001\002" });

  /** Access to parse-action table. */
  public short[][] action_table() {return _action_table;}

  /** <code>reduce_goto</code> table. */
  protected static final short[][] _reduce_table = 
    unpackFromStrings(new String[] {
    "\000\055\000\012\003\004\004\007\005\006\006\003\001" +
    "\001\000\004\007\042\001\001\000\002\001\001\000\004" +
    "\010\010\001\001\000\002\001\001\000\002\001\001\000" +
    "\010\011\014\012\015\013\013\001\001\000\002\001\001" +
    "\000\004\006\040\001\001\000\002\001\001\000\002\001" +
    "\001\000\002\001\001\000\002\001\001\000\002\001\001" +
    "\000\004\015\024\001\001\000\002\001\001\000\004\016" +
    "\031\001\001\000\002\001\001\000\002\001\001\000\002" +
    "\001\001\000\002\001\001\000\002\001\001\000\002\001" +
    "\001\000\002\001\001\000\004\017\035\001\001\000\002" +
    "\001\001\000\002\001\001\000\002\001\001\000\002\001" +
    "\001\000\002\001\001\000\004\007\042\001\001\000\002" +
    "\001\001\000\002\001\001\000\002\001\001\000\002\001" +
    "\001\000\002\001\001\000\002\001\001\000\002\001\001" +
    "\000\002\001\001\000\002\001\001\000\002\001\001\000" +
    "\002\001\001\000\002\001\001\000\002\001\001\000\002" +
    "\001\001" });

  /** Access to <code>reduce_goto</code> table. */
  public short[][] reduce_table() {return _reduce_table;}

  /** Instance of action encapsulation class. */
  protected CUP$Grm$actions action_obj;

  /** Action encapsulation object initializer. */
  protected void init_actions()
    {
      action_obj = new CUP$Grm$actions(this);
    }

  /** Invoke a user supplied parse action. */
  public java_cup.runtime.Symbol do_action(
    int                        act_num,
    java_cup.runtime.lr_parser parser,
    java.util.Stack            stack,
    int                        top)
    throws java.lang.Exception
  {
    /* call code in generated class */
    return action_obj.CUP$Grm$do_action(act_num, parser, stack, top);
  }

  /** Indicates start state. */
  public int start_state() {return 0;}
  /** Indicates start production. */
  public int start_production() {return 0;}

  /** <code>EOF</code> Symbol index. */
  public int EOF_sym() {return 0;}

  /** <code>error</code> Symbol index. */
  public int error_sym() {return 1;}


  /** Scan to get the next Symbol. */
  public java_cup.runtime.Symbol scan()
    throws java.lang.Exception
    {
 return lexer.nextToken(); 
    }

 
  //public Absyn.Exp parseResult;
  Lexer lexer;

  public void syntax_error(java_cup.runtime.Symbol current) {
   report_error("Syntax error (" + current.sym + ")", current);
  }

  ErrorMsg.ErrorMsg errorMsg;

  public void report_error(String message, 
			   java_cup.runtime.Symbol info) {
      errorMsg.error(info.left, message);
  }

  public Grm(Lexer l, ErrorMsg.ErrorMsg err) {
    this();
    errorMsg=err;
    lexer=l;
  }

}

/** Cup generated class to encapsulate user supplied action code.*/
class CUP$Grm$actions {

 static Symbol.Symbol sym(String s) {
	         return Symbol.Symbol.symbol(s);
	        }
	    
  private final Grm parser;

  /** Constructor */
  CUP$Grm$actions(Grm parser) {
    this.parser = parser;
  }

  /** Method with the actual generated action code. */
  public final java_cup.runtime.Symbol CUP$Grm$do_action(
    int                        CUP$Grm$act_num,
    java_cup.runtime.lr_parser CUP$Grm$parser,
    java.util.Stack            CUP$Grm$stack,
    int                        CUP$Grm$top)
    throws java.lang.Exception
    {
      /* Symbol object for return from actions */
      java_cup.runtime.Symbol CUP$Grm$result;

      /* select the action based on the action number */
      switch (CUP$Grm$act_num)
        {
          /*. . . . . . . . . . . . . . . . . . . .*/
          case 22: // stmt ::= ID 
            {
              Object RESULT = null;
		int v1left = ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-0)).left;
		int v1right = ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-0)).right;
		String v1 = (String)((java_cup.runtime.Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top-0)).value;
		 System.out.println(">>> stmt " + v1); 
              CUP$Grm$result = new java_cup.runtime.Symbol(5/*stmt*/, ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-0)).left, ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-0)).right, RESULT);
            }
          return CUP$Grm$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 21: // dec ::= type_dec 
            {
              Object RESULT = null;

              CUP$Grm$result = new java_cup.runtime.Symbol(7/*dec*/, ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-0)).left, ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-0)).right, RESULT);
            }
          return CUP$Grm$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 20: // dec ::= var_dec 
            {
              Object RESULT = null;

              CUP$Grm$result = new java_cup.runtime.Symbol(7/*dec*/, ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-0)).left, ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-0)).right, RESULT);
            }
          return CUP$Grm$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 19: // type_dec ::= TYPE ID EQ typ 
            {
              Object RESULT = null;
		int v1left = ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-2)).left;
		int v1right = ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-2)).right;
		String v1 = (String)((java_cup.runtime.Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top-2)).value;
		 System.out.println("type_dec " + v1); 
              CUP$Grm$result = new java_cup.runtime.Symbol(9/*type_dec*/, ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-3)).left, ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-0)).right, RESULT);
            }
          return CUP$Grm$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 18: // type_field ::= ID COLON ID 
            {
              Object RESULT = null;
		int v2left = ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-2)).left;
		int v2right = ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-2)).right;
		String v2 = (String)((java_cup.runtime.Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top-2)).value;
		int v3left = ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-0)).left;
		int v3right = ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-0)).right;
		String v3 = (String)((java_cup.runtime.Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top-0)).value;
		 System.out.println("type field " + v2 + " " + v3); 
              CUP$Grm$result = new java_cup.runtime.Symbol(13/*type_field*/, ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-2)).left, ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-0)).right, RESULT);
            }
          return CUP$Grm$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 17: // type_fields ::= 
            {
              Object RESULT = null;

              CUP$Grm$result = new java_cup.runtime.Symbol(12/*type_fields*/, ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-0)).right, ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-0)).right, RESULT);
            }
          return CUP$Grm$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 16: // type_fields ::= type_fields COMMA type_field 
            {
              Object RESULT = null;

              CUP$Grm$result = new java_cup.runtime.Symbol(12/*type_fields*/, ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-2)).left, ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-0)).right, RESULT);
            }
          return CUP$Grm$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 15: // typ ::= ARRAY OF ID 
            {
              Object RESULT = null;
		int v2left = ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-0)).left;
		int v2right = ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-0)).right;
		String v2 = (String)((java_cup.runtime.Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top-0)).value;
		 System.out.println("typ array of " + v2); 
              CUP$Grm$result = new java_cup.runtime.Symbol(11/*typ*/, ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-2)).left, ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-0)).right, RESULT);
            }
          return CUP$Grm$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 14: // typ ::= LBRACE type_fields RBRACE 
            {
              Object RESULT = null;

              CUP$Grm$result = new java_cup.runtime.Symbol(11/*typ*/, ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-2)).left, ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-0)).right, RESULT);
            }
          return CUP$Grm$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 13: // typ ::= STRING 
            {
              Object RESULT = null;

              CUP$Grm$result = new java_cup.runtime.Symbol(11/*typ*/, ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-0)).left, ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-0)).right, RESULT);
            }
          return CUP$Grm$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 12: // typ ::= INT 
            {
              Object RESULT = null;
		 System.out.println("type INT"); 
              CUP$Grm$result = new java_cup.runtime.Symbol(11/*typ*/, ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-0)).left, ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-0)).right, RESULT);
            }
          return CUP$Grm$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 11: // typ ::= ID 
            {
              Object RESULT = null;
		int v2left = ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-0)).left;
		int v2right = ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-0)).right;
		String v2 = (String)((java_cup.runtime.Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top-0)).value;

              CUP$Grm$result = new java_cup.runtime.Symbol(11/*typ*/, ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-0)).left, ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-0)).right, RESULT);
            }
          return CUP$Grm$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 10: // var_dec ::= VAR ID COLON ID ASSIGN ID LBRACK INT RBRACK OF INT 
            {
              Object RESULT = null;
		int lhs_varleft = ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-9)).left;
		int lhs_varright = ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-9)).right;
		String lhs_var = (String)((java_cup.runtime.Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top-9)).value;
		int lhs_typeleft = ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-7)).left;
		int lhs_typeright = ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-7)).right;
		String lhs_type = (String)((java_cup.runtime.Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top-7)).value;
		int rhs_typeleft = ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-5)).left;
		int rhs_typeright = ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-5)).right;
		String rhs_type = (String)((java_cup.runtime.Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top-5)).value;
		int rankleft = ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-3)).left;
		int rankright = ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-3)).right;
		Integer rank = (Integer)((java_cup.runtime.Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top-3)).value;
		int rank1left = ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-0)).left;
		int rank1right = ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-0)).right;
		Integer rank1 = (Integer)((java_cup.runtime.Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top-0)).value;
		 System.out.println(">>> var_dec"); 
              CUP$Grm$result = new java_cup.runtime.Symbol(8/*var_dec*/, ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-10)).left, ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-0)).right, RESULT);
            }
          return CUP$Grm$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 9: // declist ::= 
            {
              Object RESULT = null;

              CUP$Grm$result = new java_cup.runtime.Symbol(6/*declist*/, ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-0)).right, ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-0)).right, RESULT);
            }
          return CUP$Grm$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 8: // declist ::= declist dec 
            {
              Object RESULT = null;
		int dleft = ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-0)).left;
		int dright = ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-0)).right;
		Object d = (Object)((java_cup.runtime.Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top-0)).value;
		 System.out.println(">>> declist " + d); 
              CUP$Grm$result = new java_cup.runtime.Symbol(6/*declist*/, ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-1)).left, ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-0)).right, RESULT);
            }
          return CUP$Grm$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 7: // stmtlist ::= 
            {
              Object RESULT = null;

              CUP$Grm$result = new java_cup.runtime.Symbol(4/*stmtlist*/, ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-0)).right, ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-0)).right, RESULT);
            }
          return CUP$Grm$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 6: // stmtlist ::= stmtlist stmt 
            {
              Object RESULT = null;
		int sleft = ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-0)).left;
		int sright = ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-0)).right;
		Object s = (Object)((java_cup.runtime.Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top-0)).value;
		 System.out.println(">>> stmtlist:" + s); 
              CUP$Grm$result = new java_cup.runtime.Symbol(4/*stmtlist*/, ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-1)).left, ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-0)).right, RESULT);
            }
          return CUP$Grm$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 5: // letexp ::= 
            {
              Object RESULT = null;

              CUP$Grm$result = new java_cup.runtime.Symbol(3/*letexp*/, ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-0)).right, ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-0)).right, RESULT);
            }
          return CUP$Grm$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 4: // letexp ::= LET declist IN stmtlist END 
            {
              Object RESULT = null;
		 System.out.println("let"); 
              CUP$Grm$result = new java_cup.runtime.Symbol(3/*letexp*/, ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-4)).left, ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-0)).right, RESULT);
            }
          return CUP$Grm$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 3: // exp ::= stmtlist 
            {
              Object RESULT = null;

              CUP$Grm$result = new java_cup.runtime.Symbol(2/*exp*/, ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-0)).left, ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-0)).right, RESULT);
            }
          return CUP$Grm$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 2: // exp ::= letexp 
            {
              Object RESULT = null;

              CUP$Grm$result = new java_cup.runtime.Symbol(2/*exp*/, ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-0)).left, ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-0)).right, RESULT);
            }
          return CUP$Grm$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 1: // program ::= exp 
            {
              Object RESULT = null;

              CUP$Grm$result = new java_cup.runtime.Symbol(1/*program*/, ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-0)).left, ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-0)).right, RESULT);
            }
          return CUP$Grm$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 0: // $START ::= program EOF 
            {
              Object RESULT = null;
		int start_valleft = ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-1)).left;
		int start_valright = ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-1)).right;
		Object start_val = (Object)((java_cup.runtime.Symbol) CUP$Grm$stack.elementAt(CUP$Grm$top-1)).value;
		RESULT = start_val;
              CUP$Grm$result = new java_cup.runtime.Symbol(0/*$START*/, ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-1)).left, ((java_cup.runtime.Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top-0)).right, RESULT);
            }
          /* ACCEPT */
          CUP$Grm$parser.done_parsing();
          return CUP$Grm$result;

          /* . . . . . .*/
          default:
            throw new Exception(
               "Invalid action number found in internal parse table");

        }
    }
}

