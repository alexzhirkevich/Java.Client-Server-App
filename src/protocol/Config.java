package protocol;

import java.io.File;

public interface Config {
	int PORT = 8888;
	String HOST = "localhost";
	int XML_MAX = 1024;
	String schemaDir = "res" + File.separator + "xml-schema";
	String dtdDir = "res" + File.separator +"dtd";
}
