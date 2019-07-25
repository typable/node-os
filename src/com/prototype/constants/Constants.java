package com.prototype.constants;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


public class Constants {

	public final Charset CHARSET = StandardCharsets.ISO_8859_1;
	public final String DOS_LINE_BREAK = "\r\n";
	public final String UNIX_LINE_BREAK = "\n";

	public final String CONFIG_FILE = "config.properties";
	public final String LAUNCH_BASH = "launch.sh";
	public final String LAUNCH_BAT = "launch.bat";
	public final String CLASSPATH_FILE = ".classpath";
	public final String PROJECT_FILE = ".project";
	public final String CLASS_PATH = "/bin";
	public final String SOURCE_PATH = "/src";
	public final String WEB_PATH = "/web";
	public final String EXTENSION_PATH = "/ext";
	public final String RESOURCE_PATH = "/res";
	public final String LIBRARY_PATH = "/lib";
	public final String LOG_PATH = "/log";

	public final String CLASSPATH_TEMPLATE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><classpath><classpathentry kind=\"src\" path=\"src\"/><classpathentry kind=\"con\" path=\"org.eclipse.jdt.launching.JRE_CONTAINER\"/><classpathentry kind=\"lib\" path=\"NodeOS.jar\"/><classpathentry kind=\"lib\" path=\"lib\"/><classpathentry kind=\"lib\" path=\"log\"/><classpathentry kind=\"lib\" path=\"res\"/><classpathentry kind=\"lib\" path=\"web\"/><classpathentry kind=\"output\" path=\"bin\"/></classpath>";
	public final String PROJECT_TEMPLATE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><projectDescription><name>@{name}</name><comment></comment><projects></projects><buildSpec><buildCommand><name>org.eclipse.jdt.core.javabuilder</name><arguments></arguments></buildCommand></buildSpec><natures><nature>org.eclipse.jdt.core.javanature</nature></natures></projectDescription>";
}
