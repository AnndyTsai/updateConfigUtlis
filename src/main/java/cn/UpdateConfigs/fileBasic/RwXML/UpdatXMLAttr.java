/**
 * @author ********yangbin********
 * 
 * @time 2019-03-07-09:58:53 CTS
 * 
 * @function: 修改XML格式文档的属性
 * 
 * @changedInfo:新增class
 * 
 * */

package cn.UpdateConfigs.fileBasic.RwXML;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class UpdatXMLAttr {

	private static final Logger log = LogManager.getLogger(UpdatXMLAttr.class.getName());

	/**
	 * @function:更新node节点的attribute
	 * 
	 * @changedInfo:null
	 */
	public void update(String attr, String value, String singleNode, String XMLFilePath) {

		log.debug("update singleNode:" + singleNode + " for" + XMLFilePath);

		File file = new File(XMLFilePath);

		log.info("Create the file object for " + XMLFilePath);

		SAXReader reader = new SAXReader();

		log.debug("Create the reader object");
		try {
			Document doc = reader.read(file);
			// 获取根目录
			Element rootElement = doc.getRootElement();
			log.info("get the root element for" + XMLFilePath + " rootElement is [" + rootElement.toString() + "]");
			// 获取指定节点
			Element node = (Element) doc.selectSingleNode(singleNode);
			log.info("get the node element for" + XMLFilePath + " --" + singleNode + " is [" + node.toString() + "]");
			// 获取属性
			Attribute attribute = node.attribute(attr);
			log.info("get " + node.toString() + " attribute for" + XMLFilePath);
			// 设置属性值
			attribute.setValue(value);
			log.info("set the node " + node.toString() + "value is:" + value);
			// 格式化为缩进格式
			OutputFormat format = OutputFormat.createPrettyPrint();
			log.debug("set the format");
			// 设置编码格式
			format.setEncoding("UTF-8");
			log.info("set the encoding is UTF-8");
			try {
				XMLWriter writer = new XMLWriter(new FileWriter(file), format);
				// 写入数据
				writer.write(doc);
				log.debug("write data to the file:" + file);
				writer.close();
				log.info("write stream closed");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			log.error("not found the file:" + file);
			e.printStackTrace();
		}
	}

	/**
	 * @throws Exception
	 * @function:更新Root节点的attribute
	 * 
	 * @changedInfo:null
	 */
	public void updateRoot(String attr, String value, String XMLFilePath) throws Exception {

		log.debug("update rootNode for" + XMLFilePath);

		File file = new File(XMLFilePath);

		SAXReader reader = new SAXReader();
		Document doc = reader.read(file);
		// 获取根目录
		Element rootElement = doc.getRootElement();
		log.debug("get rootElement:[" + rootElement.toString() + "] for" + XMLFilePath);
		// 获取属性
		Attribute attribute = rootElement.attribute(attr);
		// 设置属性值
		attribute.setValue(value);
		log.debug("set the rootElement:" + rootElement + " and set value:" + value);
		// 格式化为缩进格式
		OutputFormat format = OutputFormat.createPrettyPrint();
		// 设置编码格式
		format.setEncoding("UTF-8");
		log.info("set the encoding is UTF-8");
		XMLWriter writer = new XMLWriter(new FileWriter(file), format);
		// 写入数据
		writer.write(doc);
		log.debug("write data to " + XMLFilePath);
		writer.close();
		log.debug("write success! closed the stream");

	}

	/**
	 * @function:判断节点是否存在
	 * 
	 * @changedInfo:null
	 */
	public boolean isNodeNull(String singleNode, String XMLFilePath) {

		log.info("judge the node is null, if null return [false]; if not null return [true]");

		File file = new File(XMLFilePath);

		SAXReader reader = new SAXReader();

		Element node = null;

		try {
			Document doc = reader.read(file);
			// 获取根目录
			Element rootElement = doc.getRootElement();
			// 获取指定节点
			node = (Element) doc.selectSingleNode(singleNode);

		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (node == null) {

			log.debug("singleNode:" + singleNode + "in " + XMLFilePath + " not exist");
			return false;

		} else {

			log.debug("singleNode:" + singleNode + "in " + XMLFilePath + " is exist");
			return true;
		}

	}

	// // Test
	// public static void main(String[] args) {
	//
	// UpdatXMLAttr attr = new UpdatXMLAttr();
	//
	// System.out.println(attr.isNodeNull("/server",
	// "F:\\workspaceEQ\\updateUtlis\\file\\MVSP_DeviceSvr.config"));
	// }

}
