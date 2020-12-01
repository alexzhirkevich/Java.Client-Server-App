package manage;
import xml.message.XmlMessage;
import xml.message.menu.XmlMessageMenu;
import xml.shema.generator.XmlSchemaGenerator;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public class Test {

	public static void main(String[] args) throws JAXBException, IOException {
		XmlSchemaGenerator.generateAll();
	}
}
