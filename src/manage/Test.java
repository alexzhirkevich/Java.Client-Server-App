package manage;
import objectstream.message.MessageException;
import org.xml.sax.SAXException;
import protocol.result.Result;
import xml.Xml;
import xml.message.order.XmlMessageOrderResult;
import xml.schema.generator.XmlSchemaGenerator;
import xml.schema.validator.XmlSchemaValidator;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Test {

	public static void main(String[] args) throws JAXBException, IOException, MessageException, SAXException {
		XmlSchemaGenerator.generateAll();

		Xml.toXml(new XmlMessageOrderResult(Result.OK,12),  new FileOutputStream(new File("qwe.xml")));
		XmlMessageOrderResult obj = (XmlMessageOrderResult) Xml.fromXml(XmlMessageOrderResult.class,new FileInputStream(new File("qwe.xml")));
		System.out.println(obj.getNumber());

		if (XmlSchemaValidator.validate(XmlMessageOrderResult.class,new FileInputStream(new File("qwe.xml"))))
			System.out.println("valid");
		else
			System.out.println("invalid");
	}
}
