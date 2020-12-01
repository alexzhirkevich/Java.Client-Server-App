package manage;
import objectstream.message.MessageException;
import objectstream.message.menu.MessageMenu;
import protocol.command.CommandException;
import protocol.result.Result;
import protocol.result.ResultException;
import xml.Xml;
import xml.message.XmlMessage;
import xml.message.menu.XmlMessageMenu;
import xml.message.order.XmlMessageOrderResult;
import xml.shema.generator.XmlSchemaGenerator;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Test {

	public static void main(String[] args) throws JAXBException, IOException, MessageException {
		XmlSchemaGenerator.generateAll();

		Xml.toXml(new XmlMessageOrderResult(Result.OK,12),  new FileOutputStream(new File("qwe.xml")));
		XmlMessageOrderResult obj = (XmlMessageOrderResult) Xml.fromXml(XmlMessageOrderResult.class,new FileInputStream(new File("qwe.xml")));
		System.out.println(obj.getNumber());
	}
}
