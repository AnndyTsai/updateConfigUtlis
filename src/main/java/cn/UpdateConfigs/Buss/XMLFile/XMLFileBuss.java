/**
 * @author yangbin
 * 
 * @time 2019-03-06-09:38:53 CTS
 * 
 * @function: 对XML格式的文件进行操作,对定义的type类型进行分类操作 
 * 
 * @defind:定义type类型：IP,port,portAndIP,URL,singlePortAndIP,singleURL
 * */
package cn.UpdateConfigs.Buss.XMLFile;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.UpdateConfigs.Buss.Utlis.returnConfigJsonValue;
import cn.UpdateConfigs.fileBasic.RwJson.HandleJson;
import cn.UpdateConfigs.fileBasic.RwProperties.createProperties;
import cn.UpdateConfigs.fileBasic.RwXML.UpdatXMLAttr;

public class XMLFileBuss {

	private HandleJson handleJson;
	private UpdatXMLAttr updateXML;
	private returnConfigJsonValue returnValue;
	private createProperties createPro;
	private String trackInfofilePath = "./file/trackInfo.properties";

	private static final Logger log = LogManager.getLogger(XMLFileBuss.class.getName());

	private int startPort;

	public XMLFileBuss() {

		handleJson = new HandleJson();
		log.debug("Building handleJson object with HandleJson class");
		updateXML = new UpdatXMLAttr();
		log.debug("Building updateXML object with UpdatXMLAttr class");
		returnValue = new returnConfigJsonValue();
		log.debug("Building returnValue object with returnConfigJsonValue class");
		startPort = returnValue.startPortValue();
		log.debug("get startPort with returnConfigJsonValue class startPortValue method");
		createPro = new createProperties();
		log.debug("create createPro object with createProperties class");
	}

	public void updateXMLAction(String XMLTypeConfigServerName, String XMLFilePath) throws Exception {
		
		/**
		 * @function:创建集合 存放UpdateConfig.json文件数据
		 * 
		 * @changedInfo:新增加nodeIsRootList判断标识位 部分XML格式的配置文件需要修改root节点的数据 兼容Root
		 * */
		List<String> typeList = returnValue.JsonContextVaule(XMLTypeConfigServerName, "type");
		log.debug("get typeList list Data:" + typeList.toString());
		List<String> nodeList = returnValue.JsonContextVaule(XMLTypeConfigServerName, "node");
		log.debug("get nodeList list Data:" + nodeList.toString());
		List<String> nodeIsRootList = returnValue.JsonContextVaule(XMLTypeConfigServerName, "nodeIsRoot");
		log.debug("get nodeIsRootList list Data:" + nodeIsRootList.toString());
		List<String> attributeList = returnValue.JsonContextVaule(XMLTypeConfigServerName, "attribute");
		log.debug("get attributeList list Data:" + attributeList.toString());
		List<String> valueList = returnValue.JsonContextVaule(XMLTypeConfigServerName, "value");
		log.debug("get valueList list Data:" + valueList.toString());
		List<String> iPList = returnValue.JsonContextVaule(XMLTypeConfigServerName, "IP");
		log.debug("get iPList list Data:" + iPList.toString());

		for (int i = 0; i < typeList.size(); i++) {
			/**
			 * @function:对type标识位类型为IP的数据类型进行操作
			 * 
			 * @changedInfo:null
			 * */
			if (typeList.get(i).equalsIgnoreCase("IP")) {

				// 获取不同IP类型的具体的值
				String value = null;

				if (iPList.get(i).equalsIgnoreCase("publicIP")) {

					value = returnValue.publicIPValue();

				} else if (iPList.get(i).equalsIgnoreCase("localIP")) {

					value = returnValue.localIPValue();
				} else {

					value = returnValue.algorithmIPValue();
				}

				log.info("return IP:" + value);

				// 判断是否为root节点 XML的root节点的操作方法不同于其他节点
				if (nodeIsRootList.get(i).equalsIgnoreCase("true")) {

					updateXML.updateRoot(attributeList.get(i), value, XMLFilePath);

					createPro.writeData(trackInfofilePath,
							XMLTypeConfigServerName + "." + nodeList.get(i) + "." + attributeList.get(i) + ".IP",
							value);

					log.info("update file:" + XMLFilePath + "Attribute:" + attributeList.get(i) + ",Value:[" + value
							+ "]");

				} else {

					if (!updateXML.isNodeNull(nodeList.get(i), XMLFilePath)) {

						log.error(XMLTypeConfigServerName + "server not existed,please confirmed " + nodeList.get(i)
								+ "node, output file:" + XMLFilePath);

					} else {

						updateXML.update(attributeList.get(i), value, nodeList.get(i), XMLFilePath);

						createPro.writeData(trackInfofilePath,
								XMLTypeConfigServerName + "." + nodeList.get(i) + "." + attributeList.get(i) + ".IP",
								value);

						log.info("update the file " + XMLFilePath + " " + nodeList.get(i) + attributeList.get(i) + " = "
								+ value);
					}
				}

			}

			/**
			 * @function:对type标识位类型为port的数据类型进行操作
			 * 
			 * @changedInfo:null
			 * */
			else if (typeList.get(i).equalsIgnoreCase("port")) {

				int port = startPort + Integer.parseInt(valueList.get(i));

				String newPort = String.valueOf(port);

				// 判断是否为root节点 root节点 与 single节点的操作方法不一样
				if (nodeIsRootList.get(i).equalsIgnoreCase("true")) {

					updateXML.updateRoot(attributeList.get(i), newPort, XMLFilePath);
					createPro.writeData(trackInfofilePath,
							XMLTypeConfigServerName + "." + nodeList.get(i) + "." + attributeList.get(i) + ".Port",
							newPort);

				} else {

					if (!updateXML.isNodeNull(nodeList.get(i), XMLFilePath)) {

						log.error(XMLTypeConfigServerName + " server not existed , please confirmed the "
								+ nodeList.get(i) + " node and " + XMLFilePath + " config file");

					} else {

						updateXML.update(attributeList.get(i), newPort, nodeList.get(i), XMLFilePath);

						createPro.writeData(trackInfofilePath,
								XMLTypeConfigServerName + "." + nodeList.get(i) + "." + attributeList.get(i) + ".Port",
								newPort);
					}

				}

			}

			/**
			 * @function:对type标识位类型为portAndIP的数据类型进行操作
			 * 
			 * @changedInfo:null
			 * */
			else if (typeList.get(i).equalsIgnoreCase("portAndIP")) {

				// 获取组装后的port的值 转成 Stringl类型
				int port = startPort + Integer.parseInt(valueList.get(i));

				String newPort = String.valueOf(port);

				// 获取不同IP类型的具体的值
				String value = null;

				if (iPList.get(i).equalsIgnoreCase("publicIP")) {

					value = returnValue.publicIPValue();

				} else if (iPList.get(i).equalsIgnoreCase("localIP")) {

					value = returnValue.localIPValue();
				} else {

					value = returnValue.algorithmIPValue();
				}

				// 判断是否为root节点 root节点 与 single节点的操作方法不一样
				if (nodeIsRootList.get(i).equalsIgnoreCase("true")) {

					updateXML.updateRoot(attributeList.get(i), newPort, XMLFilePath);

				} else {

					if (!updateXML.isNodeNull(nodeList.get(i), XMLFilePath)) {

						log.error(XMLTypeConfigServerName + " server not existed, please confirmed the "
								+ nodeList.get(i) + " node and " + XMLFilePath + " config file");

					} else {
						// 更新端口
						updateXML.update(attributeList.get(i).split(">")[0], newPort, nodeList.get(i), XMLFilePath);
						createPro.writeData(trackInfofilePath,
								XMLTypeConfigServerName + "." + nodeList.get(i) + "." + attributeList.get(i) + ".Port",
								newPort);
						// 更新IP
						updateXML.update(attributeList.get(i).split(">")[1], value, nodeList.get(i), XMLFilePath);
						createPro.writeData(trackInfofilePath,
								XMLTypeConfigServerName + "." + attributeList.get(i) + "." + nodeList.get(i) + ".IP",
								value);
					}
				}

			}

			/**
			 * @function:对type标识位类型为portAndIP的数据类型进行操作
			 * 
			 * @changedInfo:null
			 * */
			else if (typeList.get(i).equalsIgnoreCase("URL")) {

				// 获取组装后的port的值 转成 Stringl类型
				int port = startPort + Integer.parseInt(valueList.get(i));

				String newPort = String.valueOf(port);

				// 获取不同IP类型的具体的值
				String value = null;

				if (iPList.get(i).equalsIgnoreCase("publicIP")) {

					value = returnValue.publicIPValue();

				} else if (iPList.get(i).equalsIgnoreCase("localIP")) {

					value = returnValue.localIPValue();
				} else {

					value = returnValue.algorithmIPValue();
				}

				// 判断是否为root节点 root节点 与 single节点的操作方法不一样
				if (nodeIsRootList.get(i).equalsIgnoreCase("true")) {

					updateXML.updateRoot(attributeList.get(i), newPort, XMLFilePath);

				} else {

					if (!updateXML.isNodeNull(nodeList.get(i), XMLFilePath)) {

						log.error(XMLTypeConfigServerName + "server not existed,please confirmed:" + nodeList.get(i)
								+ " node" + XMLFilePath + " config file");

					} else {

						String URL = "http://" + value + ":" + newPort;

						// URL
						updateXML.update(attributeList.get(i), URL, nodeList.get(i), XMLFilePath);
						createPro.writeData(trackInfofilePath,
								XMLTypeConfigServerName + "." + nodeList.get(i) + "." + attributeList.get(i) + ".URL",
								URL);

					}
				}

			}
			/**
			 * @function:对type标识位类型为singlePortAndIP的数据类型进行操作
			 * 
			 * @changedInfo:新增加的类型 兼容端口为single类型的数据 
			 * */
			else if (typeList.get(i).equalsIgnoreCase("singlePortAndIP")) {

				// 获取组装后的port的值 转成 String类型
				int port = Integer.parseInt(valueList.get(i));

				String newPort = String.valueOf(port);

				// 获取不同IP类型的具体的值
				String value = null;

				if (iPList.get(i).equalsIgnoreCase("publicIP")) {

					value = returnValue.publicIPValue();

				} else if (iPList.get(i).equalsIgnoreCase("localIP")) {

					value = returnValue.localIPValue();
				} else {

					value = returnValue.algorithmIPValue();
				}

				// 判断是否为root节点 root节点 与 single节点的操作方法不一样
				if (nodeIsRootList.get(i).equalsIgnoreCase("true")) {

					updateXML.updateRoot(attributeList.get(i), newPort, XMLFilePath);

				} else {

					if (!updateXML.isNodeNull(nodeList.get(i), XMLFilePath)) {

						log.error(XMLTypeConfigServerName + " server not existed, please confirmed the "
								+ nodeList.get(i) + " node and " + XMLFilePath + " config file");

					} else {
						// 更新端口
						updateXML.update(attributeList.get(i).split(">")[0], newPort, nodeList.get(i), XMLFilePath);
						createPro.writeData(trackInfofilePath,
								XMLTypeConfigServerName + "." + nodeList.get(i) + "." + attributeList.get(i) + ".Port",
								newPort);
						// 更新IP
						updateXML.update(attributeList.get(i).split(">")[1], value, nodeList.get(i), XMLFilePath);
						createPro.writeData(trackInfofilePath,
								XMLTypeConfigServerName + "." + nodeList.get(i) + "." + attributeList.get(i) + ".IP",
								value);
					}
				}

			}
			/**
			 * @function:对type标识位类型为singleURL的数据类型进行操作
			 * 
			 * @changedInfo:null
			 * */
			else if (typeList.get(i).equalsIgnoreCase("singleURL")) {

				// 获取组装后的port的值 转成 Stringl类型
				int port = Integer.parseInt(valueList.get(i));

				String newPort = String.valueOf(port);

				// 获取不同IP类型的具体的值
				String value = null;

				if (iPList.get(i).equalsIgnoreCase("publicIP")) {

					value = returnValue.publicIPValue();

				} else if (iPList.get(i).equalsIgnoreCase("localIP")) {

					value = returnValue.localIPValue();
				} else {

					value = returnValue.algorithmIPValue();
				}

				// 判断是否为root节点 root节点 与 single节点的操作方法不一样
				if (nodeIsRootList.get(i).equalsIgnoreCase("true")) {

					updateXML.updateRoot(attributeList.get(i), newPort, XMLFilePath);

				} else {

					if (!updateXML.isNodeNull(nodeList.get(i), XMLFilePath)) {

						log.error(XMLTypeConfigServerName + "server not existed,please confirmed:" + nodeList.get(i)
								+ " node" + XMLFilePath + " config file");

					} else {

						String URL = "http://" + value + ":" + newPort;

						// URL
						updateXML.update(attributeList.get(i), URL, nodeList.get(i), XMLFilePath);
						createPro.writeData(trackInfofilePath,
								XMLTypeConfigServerName + "." + nodeList.get(i) + "." + attributeList.get(i) + ".URL",
								URL);
					}
				}

			} else {

				log.error("handle the " + XMLTypeConfigServerName
						+ " server config file,UpdateConfig.json found the undefinded type");
			}
		}

	}

//	// Test
//	public static void main(String[] args) {
//
//		XMLFileBuss action = new XMLFileBuss();
//
//		action.updateXMLAction("tap", "F:\\workspaceEQ\\updateUtlis\\file\\MVSP_DeviceSvr.config");
//	}
}
