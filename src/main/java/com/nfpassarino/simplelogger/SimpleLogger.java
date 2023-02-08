package com.nfpassarino.simplelogger;

import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SimpleLogger {

	private static SimpleDateFormat defaultDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
	private static LoggerFile loggerFile = new LoggerFile("./log.txt");
	private static PrintStream originalOut = System.out;
	private static PrintStream originalErr = System.err;

	public static void initiliaze() {
		initiliaze(false, ConsoleColors.BLUE);
	}

	public static void initiliaze(boolean debug) {
		initiliaze(debug, ConsoleColors.BLUE);
	}

	public static void initiliaze(boolean debug, String infoColor) {
		overridePrinter(System.out, LoggerType.INFO, infoColor, debug);
		overridePrinter(System.err, LoggerType.ERROR, ConsoleColors.RED, debug);
	}

	public static void shutdown() {
		System.setOut(originalOut);
		System.setErr(originalErr);
	}

	public static void cleanLog() {
		loggerFile.cleanLog();
	}

	public static List<String> readLog() throws FileNotFoundException {
		return loggerFile.readFile();
	}

	private static void overridePrinter(OutputStream outputStream, LoggerType loggerType, String mainColor,
			boolean debug) {
		PrintStream outPrintStream = new PrintStream(outputStream) {
			@Override
			public void println(String message) {
				String output = getFormattedMessage(debug, loggerType, mainColor, message);
				super.println(output);
				loggerFile.addLine(output);
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
		output.append(message);
		return output.toString();
	}

	private static void appendCommonInformation(StringBuilder output, LoggerType loggerType, String mainColor,
			String message) {
		output.append(mainColor);
		output.append("[");
		output.append(defaultDateFormat.format(new Date()));
		output.append("] [");
		output.append(loggerType);
		output.append("] ");
		output.append(ConsoleColors.RESET);
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
		output.append("[Thread: ");
		output.append(currentThreadName);
		output.append("; ");
		output.append(callerClassName);
		output.append(".");
		output.append(callerMethodName);
		output.append("] ");
	}

}
