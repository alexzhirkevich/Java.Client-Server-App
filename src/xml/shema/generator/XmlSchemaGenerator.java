package xml.shema.generator;

import com.sun.corba.se.impl.resolver.ORBDefaultInitRefResolverImpl;
import xml.Xml;
import xml.message.connection.XmlMessageConnect;
import xml.message.connection.XmlMessageConnectResult;
import xml.message.connection.XmlMessageDisconnect;
import xml.message.connection.XmlMessageDisconnectResult;
import xml.message.context.XmlMessageContext;
import xml.message.context.XmlMessageContextResult;
import xml.message.menu.XmlMessageMenu;
import xml.message.menu.XmlMessageMenuResult;
import xml.message.order.XmlMessageOrder;
import xml.message.order.XmlMessageOrderResult;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.IOException;

public abstract class XmlSchemaGenerator {

	public static final String baseDirName = "res\\xml-schema";

	public static void createXmlSchema(String fileName, Class<? extends Xml> what )
			throws JAXBException, IOException {
		JAXBContext context = JAXBContext.newInstance(what);
		context.generateSchema(new XmlSchemaOutResolver(baseDirName,fileName));
	}

	public static void generateAll() throws JAXBException, IOException {
		XmlSchemaGenerator.createXmlSchema(XmlMessageConnect.class.getSimpleName(), XmlMessageConnect.class);
		XmlSchemaGenerator.createXmlSchema(XmlMessageConnectResult.class.getSimpleName(), XmlMessageConnectResult.class);
		XmlSchemaGenerator.createXmlSchema(XmlMessageDisconnect.class.getSimpleName(), XmlMessageDisconnect.class);
		XmlSchemaGenerator.createXmlSchema(XmlMessageDisconnectResult.class.getSimpleName(), XmlMessageDisconnectResult.class);
		XmlSchemaGenerator.createXmlSchema(XmlMessageContext.class.getSimpleName(), XmlMessageContext.class);
		XmlSchemaGenerator.createXmlSchema(XmlMessageContextResult.class.getSimpleName(), XmlMessageContextResult.class);
		XmlSchemaGenerator.createXmlSchema(XmlMessageMenu.class.getSimpleName(), XmlMessageMenu.class);
		XmlSchemaGenerator.createXmlSchema(XmlMessageMenuResult.class.getSimpleName(), XmlMessageMenuResult.class);
		XmlSchemaGenerator.createXmlSchema(XmlMessageOrder.class.getSimpleName(), XmlMessageOrder.class);
		XmlSchemaGenerator.createXmlSchema(XmlMessageOrderResult.class.getSimpleName(), XmlMessageOrderResult.class);
	}


}
