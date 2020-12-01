package xml.schema.validator;

import org.xml.sax.SAXException;
import protocol.Config;
import xml.Xml;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public abstract class XmlSchemaValidator {

	public static boolean validate(Class<? extends Xml> Class, InputStream what) throws SAXException, IOException {
		SchemaFactory factory =  SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");

		Schema schema = factory.newSchema(new File(Config.schemaDir + File.separator + Class.getSimpleName()+".xml"));
		Validator validator = schema.newValidator();
		Source source = new StreamSource(what);
		try{
			validator.validate(source);
			return true;
		}catch (SAXException e){
			return false;
		}
	}
}
