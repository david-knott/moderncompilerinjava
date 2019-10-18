package chap2.programming;


class Yylex implements Lexer {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 128;
	private final int YY_EOF = 129;

private void newline() {
  errorMsg.newline(yychar);
}
private void err(int pos, String s) {
  errorMsg.error(pos,s);
}
private void err(String s) {
  err(yychar,s);
}
private java_cup.runtime.Symbol tok(int kind, Object value) {
    return new java_cup.runtime.Symbol(kind, yychar, yychar+yylength(), value);
}
private ErrorMsg errorMsg;
private int commentDepth = 0;
Yylex(java.io.InputStream s, ErrorMsg e) {
  this(s);
  errorMsg=e;
}
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private int yychar;
	private boolean yy_at_bol;
	private int yy_lexical_state;

	Yylex (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	Yylex (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private Yylex () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yychar = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;
	}

	private boolean yy_eof_done = false;
	private final int STRING = 2;
	private final int YYINITIAL = 0;
	private final int COMMENT = 1;
	private final int yy_state_dtrans[] = {
		0,
		59,
		64
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		yychar = yychar
			+ yy_buffer_index - yy_buffer_start;
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NOT_ACCEPT,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NO_ANCHOR,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_NO_ANCHOR,
		/* 38 */ YY_NO_ANCHOR,
		/* 39 */ YY_NO_ANCHOR,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NO_ANCHOR,
		/* 56 */ YY_NOT_ACCEPT,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NOT_ACCEPT,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NO_ANCHOR,
		/* 62 */ YY_NOT_ACCEPT,
		/* 63 */ YY_NO_ANCHOR,
		/* 64 */ YY_NOT_ACCEPT,
		/* 65 */ YY_NO_ANCHOR,
		/* 66 */ YY_NO_ANCHOR,
		/* 67 */ YY_NO_ANCHOR,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NO_ANCHOR,
		/* 70 */ YY_NO_ANCHOR,
		/* 71 */ YY_NO_ANCHOR,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NO_ANCHOR,
		/* 74 */ YY_NO_ANCHOR,
		/* 75 */ YY_NO_ANCHOR,
		/* 76 */ YY_NO_ANCHOR,
		/* 77 */ YY_NO_ANCHOR,
		/* 78 */ YY_NO_ANCHOR,
		/* 79 */ YY_NO_ANCHOR,
		/* 80 */ YY_NO_ANCHOR,
		/* 81 */ YY_NO_ANCHOR,
		/* 82 */ YY_NO_ANCHOR,
		/* 83 */ YY_NO_ANCHOR,
		/* 84 */ YY_NO_ANCHOR,
		/* 85 */ YY_NO_ANCHOR,
		/* 86 */ YY_NO_ANCHOR,
		/* 87 */ YY_NO_ANCHOR,
		/* 88 */ YY_NO_ANCHOR,
		/* 89 */ YY_NO_ANCHOR,
		/* 90 */ YY_NO_ANCHOR,
		/* 91 */ YY_NO_ANCHOR,
		/* 92 */ YY_NO_ANCHOR,
		/* 93 */ YY_NO_ANCHOR,
		/* 94 */ YY_NO_ANCHOR,
		/* 95 */ YY_NO_ANCHOR,
		/* 96 */ YY_NO_ANCHOR,
		/* 97 */ YY_NO_ANCHOR,
		/* 98 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"43:9,47,45,43:2,44,43:18,46,43,42,43:3,38,43,27,28,34,32,25,33,31,35,41:10," +
"23,26,36,24,37,43:28,21,43,22,43,40,43,11,10,19,15,5,6,40,2,3,40,12,4,40,16" +
",7,14,40,8,17,9,18,20,1,40,13,40,29,39,30,43:2,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,99,
"0,1,2,1:2,3,1:11,4,5,6,1:2,7,1:4,8,9,8:3,1:6,8:12,1:6,10,11,12,13,14,15,16," +
"17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41," +
"42,43,44,45,46,47,48,49,8,50,51")[0];

	private int yy_nxt[][] = unpackFromString(52,48,
"1,2,96,57,77,78,79,60,96,63,97,98,96:3,65,96:4,80,3,4,5,6,7,8,9,10,11,12,13" +
",14,15,16,17,18,19,20,21,96,22,23,24,56,-1,25,26,-1:49,96,88,96:18,-1:19,96" +
",89,-1:30,32,-1:57,33,-1:37,34,-1:12,35,-1:34,36,-1:64,22,-1:7,96:20,-1:19," +
"96,89,-1:7,96:8,38,96:11,-1:19,96,89,-1:51,37,-1:3,96:5,27,96:9,28,96:4,-1:" +
"19,96,89,-1:41,51,-1:12,1,50:33,58,61,50:8,62,-1,50:2,-1,96:5,29,96:14,-1:1" +
"9,96,89,-1:40,52,-1:58,53,-1:3,96,82,96:4,30,96:5,83,96:7,-1:19,96,89,-1:6," +
"1,54:41,55,54,-1:2,54:2,-1,96:6,31,96:13,-1:19,96,89,-1:7,96:8,39,96:11,-1:" +
"19,96,89,-1:7,96:14,40,96:5,-1:19,96,89,-1:7,96:7,41,96:12,-1:19,96,89,-1:7" +
",96:7,42,96:12,-1:19,96,89,-1:7,96:4,43,96:15,-1:19,96,89,-1:7,96:15,44,96:" +
"4,-1:19,96,89,-1:7,96:4,45,96:15,-1:19,96,89,-1:7,96:4,46,96:15,-1:19,96,89" +
",-1:7,96:11,47,96:8,-1:19,96,89,-1:7,96:12,48,96:7,-1:19,96,89,-1:7,96:15,4" +
"9,96:4,-1:19,96,89,-1:7,96:4,66,96:15,-1:19,96,89,-1:7,96:3,81,96:11,67,96:" +
"4,-1:19,96,89,-1:7,96:6,68,96:10,90,96:2,-1:19,96,89,-1:7,96:10,69,96:9,-1:" +
"19,96,89,-1:7,96:16,70,96:3,-1:19,96,89,-1:7,96:4,71,96:15,-1:19,96,89,-1:7" +
",96:13,72,96:6,-1:19,96,89,-1:7,96:3,73,96:16,-1:19,96,89,-1:7,96:10,74,96:" +
"9,-1:19,96,89,-1:7,96:10,75,96:9,-1:19,96,89,-1:7,96:6,76,96:13,-1:19,96,89" +
",-1:7,96:2,84,96:17,-1:19,96,89,-1:7,89:20,-1:19,89:2,-1:7,96:15,93,96:4,-1" +
":19,96,89,-1:7,96:4,85,96:15,-1:19,96,89,-1:7,96:7,86,96:12,-1:19,96,89,-1:" +
"7,96:18,94,96,-1:19,96,89,-1:7,96:8,95,96:11,-1:19,96,89,-1:7,96:2,87,96:17" +
",-1:19,96,89,-1:7,96:7,91,96:12,-1:19,96,89,-1:7,96:7,92,96:12,-1:19,96,89," +
"-1:6");

	public java_cup.runtime.Symbol nextToken ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {

	{
	 return tok(sym.EOF, null);
        }
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 1:
						
					case -2:
						break;
					case 2:
						{return tok(sym.ID, yytext());}
					case -3:
						break;
					case 3:
						{return tok(sym.LBRACK, null);}
					case -4:
						break;
					case 4:
						{return tok(sym.RBRACK, null);}
					case -5:
						break;
					case 5:
						{return tok(sym.ASSIGN, null);}
					case -6:
						break;
					case 6:
						{return tok(sym.EQ, null);}
					case -7:
						break;
					case 7:
						{return tok(sym.COMMA, null);}
					case -8:
						break;
					case 8:
						{return tok(sym.SEMICOLON, null);}
					case -9:
						break;
					case 9:
						{return tok(sym.LPAREN, null);}
					case -10:
						break;
					case 10:
						{return tok(sym.RPAREN, null);}
					case -11:
						break;
					case 11:
						{return tok(sym.LBRACE, null);}
					case -12:
						break;
					case 12:
						{return tok(sym.RBRACE, null);}
					case -13:
						break;
					case 13:
						{return tok(sym.DOT, null);}
					case -14:
						break;
					case 14:
						{return tok(sym.PLUS, null);}
					case -15:
						break;
					case 15:
						{return tok(sym.MINUS, null);}
					case -16:
						break;
					case 16:
						{return tok(sym.TIMES, null);}
					case -17:
						break;
					case 17:
						{return tok(sym.DIVIDE, null);}
					case -18:
						break;
					case 18:
						{return tok(sym.LT, null);}
					case -19:
						break;
					case 19:
						{return tok(sym.GT, null);}
					case -20:
						break;
					case 20:
						{return tok(sym.AND, null);}
					case -21:
						break;
					case 21:
						{return tok(sym.OR, null);}
					case -22:
						break;
					case 22:
						{return tok(sym.INT, new Integer(yytext()));}
					case -23:
						break;
					case 23:
						{yybegin(STRING);}
					case -24:
						break;
					case 24:
						{errorMsg.error(yychar, "illegal character");}
					case -25:
						break;
					case 25:
						{}
					case -26:
						break;
					case 26:
						{}
					case -27:
						break;
					case 27:
						{return tok(sym.IF, null);}
					case -28:
						break;
					case 28:
						{return tok(sym.IN, null);}
					case -29:
						break;
					case 29:
						{return tok(sym.OF, null);}
					case -30:
						break;
					case 30:
						{return tok(sym.TO, null);}
					case -31:
						break;
					case 31:
						{return tok(sym.DO, null);}
					case -32:
						break;
					case 32:
						{return tok(sym.EQ, null);}
					case -33:
						break;
					case 33:
						{ yybegin(COMMENT); }
					case -34:
						break;
					case 34:
						{return tok(sym.LE, null);}
					case -35:
						break;
					case 35:
						{return tok(sym.NEQ, null);}
					case -36:
						break;
					case 36:
						{return tok(sym.GE, null);}
					case -37:
						break;
					case 37:
						{newline();}
					case -38:
						break;
					case 38:
						{return tok(sym.INT, null);}
					case -39:
						break;
					case 39:
						{return tok(sym.LET, null);}
					case -40:
						break;
					case 40:
						{return tok(sym.END, null);}
					case -41:
						break;
					case 41:
						{return tok(sym.FOR, null);}
					case -42:
						break;
					case 42:
						{return tok(sym.VAR, null);}
					case -43:
						break;
					case 43:
						{return tok(sym.ELSE, null);}
					case -44:
						break;
					case 44:
						{return tok(sym.THEN, null);}
					case -45:
						break;
					case 45:
						{return tok(sym.TYPE, null);}
					case -46:
						break;
					case 46:
						{return tok(sym.WHILE, null);}
					case -47:
						break;
					case 47:
						{return tok(sym.BREAK, null);}
					case -48:
						break;
					case 48:
						{return tok(sym.ARRAY, null);}
					case -49:
						break;
					case 49:
						{return tok(sym.FUNCTION, null);}
					case -50:
						break;
					case 50:
						{}
					case -51:
						break;
					case 51:
						{ if(commentDepth-- == 0) yybegin(YYINITIAL);}
					case -52:
						break;
					case 52:
						{ commentDepth++; }
					case -53:
						break;
					case 53:
						{}
					case -54:
						break;
					case 54:
						{}
					case -55:
						break;
					case 55:
						{yybegin(YYINITIAL);return tok(sym.STRING, new String(yytext()));}
					case -56:
						break;
					case 57:
						{return tok(sym.ID, yytext());}
					case -57:
						break;
					case 58:
						{}
					case -58:
						break;
					case 60:
						{return tok(sym.ID, yytext());}
					case -59:
						break;
					case 61:
						{}
					case -60:
						break;
					case 63:
						{return tok(sym.ID, yytext());}
					case -61:
						break;
					case 65:
						{return tok(sym.ID, yytext());}
					case -62:
						break;
					case 66:
						{return tok(sym.ID, yytext());}
					case -63:
						break;
					case 67:
						{return tok(sym.ID, yytext());}
					case -64:
						break;
					case 68:
						{return tok(sym.ID, yytext());}
					case -65:
						break;
					case 69:
						{return tok(sym.ID, yytext());}
					case -66:
						break;
					case 70:
						{return tok(sym.ID, yytext());}
					case -67:
						break;
					case 71:
						{return tok(sym.ID, yytext());}
					case -68:
						break;
					case 72:
						{return tok(sym.ID, yytext());}
					case -69:
						break;
					case 73:
						{return tok(sym.ID, yytext());}
					case -70:
						break;
					case 74:
						{return tok(sym.ID, yytext());}
					case -71:
						break;
					case 75:
						{return tok(sym.ID, yytext());}
					case -72:
						break;
					case 76:
						{return tok(sym.ID, yytext());}
					case -73:
						break;
					case 77:
						{return tok(sym.ID, yytext());}
					case -74:
						break;
					case 78:
						{return tok(sym.ID, yytext());}
					case -75:
						break;
					case 79:
						{return tok(sym.ID, yytext());}
					case -76:
						break;
					case 80:
						{return tok(sym.ID, yytext());}
					case -77:
						break;
					case 81:
						{return tok(sym.ID, yytext());}
					case -78:
						break;
					case 82:
						{return tok(sym.ID, yytext());}
					case -79:
						break;
					case 83:
						{return tok(sym.ID, yytext());}
					case -80:
						break;
					case 84:
						{return tok(sym.ID, yytext());}
					case -81:
						break;
					case 85:
						{return tok(sym.ID, yytext());}
					case -82:
						break;
					case 86:
						{return tok(sym.ID, yytext());}
					case -83:
						break;
					case 87:
						{return tok(sym.ID, yytext());}
					case -84:
						break;
					case 88:
						{return tok(sym.ID, yytext());}
					case -85:
						break;
					case 89:
						{return tok(sym.ID, yytext());}
					case -86:
						break;
					case 90:
						{return tok(sym.ID, yytext());}
					case -87:
						break;
					case 91:
						{return tok(sym.ID, yytext());}
					case -88:
						break;
					case 92:
						{return tok(sym.ID, yytext());}
					case -89:
						break;
					case 93:
						{return tok(sym.ID, yytext());}
					case -90:
						break;
					case 94:
						{return tok(sym.ID, yytext());}
					case -91:
						break;
					case 95:
						{return tok(sym.ID, yytext());}
					case -92:
						break;
					case 96:
						{return tok(sym.ID, yytext());}
					case -93:
						break;
					case 97:
						{return tok(sym.ID, yytext());}
					case -94:
						break;
					case 98:
						{return tok(sym.ID, yytext());}
					case -95:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}