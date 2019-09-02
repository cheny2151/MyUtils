package xpath;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;

/**
 * @author cheney
 */
public class XpathTest {

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(XpathTest.class.getClassLoader().getResourceAsStream("test_xpath.xml"));
        XPath xPath = XPathFactory.newInstance().newXPath();
        //获取所有root的所有子node
        NodeList evaluate = (NodeList) xPath.evaluate("/root/*", document, XPathConstants.NODESET);
        NamedNodeMap attributes = evaluate.item(0).getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            System.out.println(attributes.item(i).getNodeName() + ":" + attributes.item(i).getNodeValue());
        }
        System.out.println(evaluate.item(1).getChildNodes().item(0).getNodeValue());

        System.out.println("-----------------------------------------------------------------------");

        //获取root/node1的id属性（属性也是一个node对象）
        Node node = (Node) xPath.evaluate("/root/node2", document, XPathConstants.NODE);
        System.out.println(node.getNodeName() + ":" + node.getNodeValue() + ":" + node.getNodeType());
        Node next = node.getFirstChild();
        while (next != null) {
            System.out.println(next.getNodeName() + ":" + next.getNodeValue() + ":" + next.getNodeType());
            next = next.getFirstChild();
        }
        next = node.getChildNodes().item(1);
        while (next != null) {
            System.out.println(next.getNodeName() + ":" + next.getNodeValue() + ":" + next.getNodeType());
            next = next.getFirstChild();
        }

        System.out.println("-----------------------------------------------------------------------");

    }

}
