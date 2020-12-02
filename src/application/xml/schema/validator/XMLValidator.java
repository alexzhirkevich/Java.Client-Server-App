package application.xml.schema.validator;

import com.sun.org.apache.xml.internal.security.utils.IgnoreAllErrorHandler;
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import application.protocol.Config;
import application.xml.Xml;
import org.xml.sax.SAXParseException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
public abstract class XMLValidator {

	private static Object sync = new Object();

	public static boolean validate(Class<? extends Xml> Class, String xmlRequest) throws SAXException, IOException, ParserConfigurationException {
		return validateXsd(Class,xmlRequest) && validateDtd(Class, xmlRequest);
	}

	public static boolean validateXsd(Class<? extends Xml> Class, String xmlRequest) throws SAXException, IOException {
		synchronized (sync) {
			try(ByteArrayInputStream bis = new ByteArrayInputStream(xmlRequest.getBytes())) {
				SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
				Schema schema = factory.newSchema(new File(Config.schemaDir + File.separator + Class.getSimpleName() + ".xsd"));
				Validator validator = schema.newValidator();
				Source source = new StreamSource(bis);
				validator.validate(source);
				return true;
			} catch (SAXException e) {
				return false;
			}
		}
	}

	public static boolean validateDtd(Class<? extends Xml> Class, String xmlRequest) throws ParserConfigurationException {
		synchronized (sync) {

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setValidating(true);
			DocumentBuilder builder = dbf.newDocumentBuilder();
			builder.setErrorHandler(new IgnoreAllErrorHandler());

			String lClassName = Class.getSimpleName();
			lClassName = Character.toLowerCase(lClassName.charAt(0)) + lClassName.substring(1);

			StringBuilder sb = new StringBuilder(xmlRequest);
			sb.insert(xmlRequest.indexOf('>')+1, String.format("<!DOCTYPE %s SYSTEM \"%s\">",
					lClassName, Config.dtdDir + File.separatorChar+ Class.getSimpleName()+".dtd"));
			try(ByteArrayInputStream bis = new ByteArrayInputStream(sb.toString().getBytes())){
				Document doc = builder.parse(bis);
			}
			catch (Exception e){
				return false;
			}
			return true;
		}
	}
}
