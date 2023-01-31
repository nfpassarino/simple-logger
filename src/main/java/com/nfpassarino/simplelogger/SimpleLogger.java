package com.nfpassarino.simplelogger;

import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleLogger {

	private static boolean debug = true;
	private static SimpleDateFormat defaultTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");

	public static void initiliaze() {
		overridePrinter(System.out, LoggerType.INFO, ConsoleColors.BLUE);
		overridePrinter(System.err, LoggerType.ERROR, ConsoleColors.RED);
		// crear archivo
	}

	private static void overridePrinter(OutputStream outputStream, LoggerType loggerType, String mainColor) {
		PrintStream outPrintStream = new PrintStream(outputStream) {
			@Override
			public void print(String message) {
				String output = getFormattedMessage(debug, loggerType, mainColor, message);
				super.print(output);
				// agregar al archivo
			}
		};
		if (outputStream.equals(System.out)) {
			System.setOut(outPrintStream);
		} else if (outputStream.equals(System.err)) {
			System.setErr(outPrintStream);
		}
	}

	private static String getFormattedMessage(boolean debug, LoggerType loggerType, String mainColor, String message) {
		StringBuilder output = new StringBuilder();
		appendCommonInformation(output, loggerType, mainColor, message);
		appendDebugInformation(output, debug);
		return output.toString();
	}

	private static void appendCommonInformation(StringBuilder output, LoggerType loggerType, String mainColor,
			String message) {
		output.append(mainColor);
		output.append("[");
		output.append(defaultTime.format(new Date()));
		output.append("] [");
		output.append(loggerType);
		output.append("] ");
		output.append(ConsoleColors.RESET);
		output.append(message);
	}

	private static void appendDebugInformation(StringBuilder output, boolean debug) {
		if (!debug) {
			return;
		}
		Thread currentThread = Thread.currentThread();
		String currentThreadName = currentThread.getName();
		StackTraceElement callerElement = currentThread.getStackTrace()[3];
		String callerClassName = callerElement.getClassName();
		String callerMethodName = callerElement.getMethodName();
		output.append("\t[Thread: ");
		output.append(currentThreadName);
		output.append("; ");
		output.append(callerClassName);
		output.append(".");
		output.append(callerMethodName);
		output.append("]");
	}

}
