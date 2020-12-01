package xml.schema.generator;

import protocol.Config;
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


	public static void create(String fileName, Class<? extends Xml> what )
			throws JAXBException, IOException {
		JAXBContext context = JAXBContext.newInstance(what);
		context.generateSchema(new XmlSchemaOutResolver(Config.schemaDir,fileName));
	}

	public static void generateAll() throws JAXBException, IOException {
		XmlSchemaGenerator.create(XmlMessageConnect.class.getSimpleName(), XmlMessageConnect.class);
		XmlSchemaGenerator.create(XmlMessageConnectResult.class.getSimpleName(), XmlMessageConnectResult.class);
		XmlSchemaGenerator.create(XmlMessageDisconnect.class.getSimpleName(), XmlMessageDisconnect.class);
		XmlSchemaGenerator.create(XmlMessageDisconnectResult.class.getSimpleName(), XmlMessageDisconnectResult.class);
		XmlSchemaGenerator.create(XmlMessageContext.class.getSimpleName(), XmlMessageContext.class);
		XmlSchemaGenerator.create(XmlMessageContextResult.class.getSimpleName(), XmlMessageContextResult.class);
		XmlSchemaGenerator.create(XmlMessageMenu.class.getSimpleName(), XmlMessageMenu.class);
		XmlSchemaGenerator.create(XmlMessageMenuResult.class.getSimpleName(), XmlMessageMenuResult.class);
		XmlSchemaGenerator.create(XmlMessageOrder.class.getSimpleName(), XmlMessageOrder.class);
		XmlSchemaGenerator.create(XmlMessageOrderResult.class.getSimpleName(), XmlMessageOrderResult.class);
	}


}
