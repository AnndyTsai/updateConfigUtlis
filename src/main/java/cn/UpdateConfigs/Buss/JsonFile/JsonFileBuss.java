/**
 * @author ********yangbin********
 * 
 * @time 2019-03-08-18:18:53 CTS
 * 
 * @function: 对Json格式的文件进行操作,对定义的type类型进行分类操作 
 * 
 * @defind:定义type类型：port ip singel,singlePort
 * */
package cn.UpdateConfigs.Buss.JsonFile;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.UpdateConfigs.Buss.Utlis.returnConfigJsonValue;
import cn.UpdateConfigs.fileBasic.RwJson.HandleJson;
import cn.UpdateConfigs.fileBasic.RwProperties.createProperties;

public class JsonFileBuss {

	private static final Logger log = LogManager.getLogger(JsonFileBuss.class.getName());
	private HandleJson handleJson;
	private returnConfigJsonValue returnValue;
	private createProperties createPro;
	private String trackInfofilePath = "./file/trackInfo.properties";

	private int startPort;

	public JsonFileBuss() {

		handleJson = new HandleJson();
		log.debug("Building handleJson object with HandleJson class");
		returnValue = new returnConfigJsonValue();
		log.debug("Building returnValue object with returnConfigJsonValue class");
		startPort = returnValue.startPortValue();
		log.debug("get startPort with returnConfigJsonValue class startPortValue method");
		createPro = new createProperties();
		log.debug("create createPro object with createProperties class");
	}

	public void updateJsonFile(String JsonTypeFileServerName, String tageJsonFilePath) throws Exception {

		/**
		 * @function：根据node节点Key获取Value集合
		 */
		List<String> typeList = returnValue.JsonContextVaule(JsonTypeFileServerName, "type");
		log.info("get typeList list Data:" + typeList.toString());
		List<String> jsonPathList = returnValue.JsonContextVaule(JsonTypeFileServerName, "jsonPath");
		log.info("get jsonPathList list Data:" + jsonPathList.toString());
		List<String> IPList = returnValue.JsonContextVaule(JsonTypeFileServerName, "IP");
		log.info("get IPList list Data:" + IPList.toString());
		List<String> portList = returnValue.JsonContextVaule(JsonTypeFileServerName, "port");
		log.info("get portList list Data:" + portList.toString());
		List<String> commsList = returnValue.JsonContextVaule(JsonTypeFileServerName, "comms");
		log.info("get commsList list Data:" + commsList.toString());

		for (int i = 0; i < typeList.size(); i++) {

			log.info("serverName :" + JsonTypeFileServerName + ", tagFilePath :" + tageJsonFilePath + "update "
					+ typeList.size() + "nodes");
			// 判断节点是否存在
			if (handleJson.returnValue(tageJsonFilePath, jsonPathList.get(i)).toString() == null) {

				log.error("config file UpdateConfig.json exception;" + jsonPathList.get(i) + "node not exist on "
						+ JsonTypeFileServerName + "." + tageJsonFilePath + "files");
			} else {

				/**
				 * @function：对于type为port类型的进行操作
				 * 
				 * @changeInfo: 1：修改updateJson Method
				 *              value传入参数类型为Object类型，支持port类型为Integer类型的写入
				 */
				if (typeList.get(i).equalsIgnoreCase("port")) {

					String initPort = String.valueOf(Integer.parseInt(portList.get(i)) + startPort);

					int newPort = Integer.parseInt(initPort);

					handleJson.updateJson(tageJsonFilePath, jsonPathList.get(i), newPort);

					createPro.writeData(trackInfofilePath,
							JsonTypeFileServerName + "." + jsonPathList.get(i) + "-->" + ".port",
							String.valueOf(newPort));

					log.info(tageJsonFilePath + "." + jsonPathList.get(i) + " update the port is:" + newPort);
				}

				/**
				 * @function:对于type为IP类型的进行操作
				 * 
				 * @changedInfo： null
				 */
				else if (typeList.get(i).equalsIgnoreCase("IP")) {

					// 获取不同IP类型的具体的值
					String IPValue = null;

					if (IPList.get(i).equalsIgnoreCase("publicIP")) {

						IPValue = returnValue.publicIPValue();

					} else if (IPList.get(i).equalsIgnoreCase("localIP")) {

						IPValue = returnValue.localIPValue();
					} else {

						IPValue = returnValue.algorithmIPValue();
					}

					handleJson.updateJson(tageJsonFilePath, jsonPathList.get(i), IPValue);

					createPro.writeData(trackInfofilePath,
							JsonTypeFileServerName + "." + jsonPathList.get(i) + "-->" + ".IP", IPValue);

					log.info(tageJsonFilePath + "." + jsonPathList.get(i) + " update the IP is:" + IPValue);

				}

				/**
				 * @function:对于type为singlePort类型的进行操作
				 * 
				 * @changedInfo：1：新增加，兼容singlePort类型 配置文件写入的port类型为int类型 修改value写入类型为Object
				 * */ 
				else if (typeList.get(i).equalsIgnoreCase("singlePort")) {

					int Value = Integer.parseInt(commsList.get(i));

					handleJson.updateJson(tageJsonFilePath, jsonPathList.get(i), Value);

					createPro.writeData(trackInfofilePath,
							JsonTypeFileServerName + "." + jsonPathList.get(i) + "-->" + ".singlePort",
							String.valueOf(Value));

					log.info(tageJsonFilePath + "." + jsonPathList.get(i) + " this is a singled,update the value is:"
							+ Value);
				}

				/**
				 * @function:对于type为single类型的进行操作
				 * 
				 * @changedInfo:新增加type类型为single的类型 兼容没有规律的attribute配置
				 * */ 
				else if (typeList.get(i).equalsIgnoreCase("single")) {

					handleJson.updateJson(tageJsonFilePath, jsonPathList.get(i), commsList.get(i));

					createPro.writeData(trackInfofilePath,
							JsonTypeFileServerName + "." + jsonPathList.get(i) + "-->" + ".single", commsList.get(i));

					log.info(tageJsonFilePath + "." + jsonPathList.get(i) + " this is a singled,update the value is:"
							+ commsList.get(i));
				}

				// 如果都不匹配 按照single方式处理
				else {

					log.warn("please confimed the file UpdateConfig.json，" + jsonPathList.get(i)
							+ "node is incorrect on the " + JsonTypeFileServerName + " config file -->"
							+ commsList.get(i));
					handleJson.updateJson(tageJsonFilePath, jsonPathList.get(i), commsList.get(i));

					createPro.writeData(trackInfofilePath,
							JsonTypeFileServerName + "." + jsonPathList.get(i) + "-->" + ".single", commsList.get(i));

					log.info(tageJsonFilePath + "." + jsonPathList.get(i) + " this is a singled,update the value is:"
							+ commsList.get(i));
				}

			}

		}

	}

//	public static void main(String[] args) {
//
//		JsonFileBuss jsonFileAction = new JsonFileBuss();
//
//		jsonFileAction.updateJsonFile("nodeWeb", "F:\\workspaceEQ\\updateUtlis\\file\\config.json");
//	}

}
