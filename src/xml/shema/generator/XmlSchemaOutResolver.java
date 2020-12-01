package xml.shema.generator;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

class XmlSchemaOutResolver extends SchemaOutputResolver {
	String baseDirName;
	String fileName;

	public XmlSchemaOutResolver(String baseDirName, String fileName) {
		this.baseDirName = baseDirName;
		this.fileName = fileName.endsWith(".xml") ? fileName : fileName+".xml";
	}

	public Result createOutput(String namespaceUri, String suggestedFileName) throws IOException {
		File f = new File(baseDirName + File.separatorChar + fileName);
		if (!f.exists())
			f.createNewFile();
		return new StreamResult(f);
	}
}