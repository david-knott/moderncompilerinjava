package Util;

import java.io.InputStream;
import java.io.OutputStream;

import ErrorMsg.ErrorMsg;

public interface TaskProvider {

	void build(InputStream in, OutputStream out, ErrorMsg errorMsg);
}