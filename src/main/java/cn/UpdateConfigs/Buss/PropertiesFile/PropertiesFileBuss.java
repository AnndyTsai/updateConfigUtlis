/**
 * @author ********yangbin********
 * 
 * @time 2019-03-08-18:18:53 CTS
 * 
 * @function对Properties格式的文件进行操作,对定义的type类型进行分类操作 
 * 
 * @defind:定义type类型：ip、port、singlePort,URL,singleURL
 * 
 * */
package cn.UpdateConfigs.Buss.PropertiesFile;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.UpdateConfigs.Buss.Utlis.returnConfigJsonValue;
import cn.UpdateConfigs.fileBasic.RwJson.HandleJson;
import cn.UpdateConfigs.fileBasic.RwProperties.RwPropertiesUtlis;
import cn.UpdateConfigs.fileBasic.RwProperties.createProperties;

public class PropertiesFileBuss {

	private HandleJson handleJson;
	private RwPropertiesUtlis RwPro;
	private returnConfigJsonValue returnValue;
	private createProperties createPro;
	private String trackInfofilePath = "./file/trackInfo.properties";

	private int startPort;

	private static final Logger log = LogManager.getLogger(PropertiesFileBuss.class.getName());

	public PropertiesFileBuss(String propertiesFilePath) throws Exception {

		handleJson = new HandleJson();
		log.debug("Building handleJson object with HandleJson class");
		returnValue = new returnConfigJsonValue();
		log.debug("Building returnValue object with returnConfigJsonValue class");
		RwPro = new RwPropertiesUtlis(propertiesFilePath);
		log.debug("Building RwPro object with RwPropertiesUtlis class and transfer to file path" + propertiesFilePath);
		startPort = returnValue.startPortValue();
		log.debug("get startPort with returnConfigJsonValue class startPortValue method");
		createPro = new createProperties();
		log.debug("create createPro object with createProperties class");
	}

	/**
	 * @throws Exception 
	 * @function:对ip类型的Key value进行修改
	 * 
	 * @changedInfo:新增加type类型为singleURL的类型 兼容没有规律的value配置
	 * */ 
	public void updateProperties(String PropertiesTypeServerName) throws Exception {
		// type、key、portInit、IP
		List<String> typeList = returnValue.JsonContextVaule(PropertiesTypeServerName, "type");
		log.debug("get typeList list Data:" + typeList.toString());
		List<String> keyList = returnValue.JsonContextVaule(PropertiesTypeServerName, "key");
		log.debug("get keyList list Data:" + keyList.toString());
		List<String> portInitList = returnValue.JsonContextVaule(PropertiesTypeServerName, "portInit");
		log.debug("get portInitList list Data:" + portInitList.toString());
		List<String> IPList = returnValue.JsonContextVaule(PropertiesTypeServerName, "IP");
		log.debug("get IPList list Data:" + IPList.toString());

		for (int i = 0; i < typeList.size(); i++) {

			log.info("serverName :" + PropertiesTypeServerName + "update " + typeList.size() + "nodes");

			// 判断Key是否为空 null则无法写入
			if (RwPro.getValue(keyList.get(i)) == null) {

				log.error(PropertiesTypeServerName + " the key[" + keyList.get(i)
						+ "] not existed, please check your config file"
						+ returnValue.JsonHeaderValue(PropertiesTypeServerName, "filePath"));
			} else {

				log.info(keyList.get(i) + " is exist on "
						+ returnValue.JsonHeaderValue(PropertiesTypeServerName, "filePath") + " and not null data");

				/**
				 * @function:port类型的数据修改写入操作
				 * 
				 * @changedInfo:null
				 * */ 
				if (typeList.get(i).equalsIgnoreCase("port")) {

					int newPort = Integer.parseInt(portInitList.get(i)) + startPort;

					RwPro.update(keyList.get(i), String.valueOf(newPort));
					
					createPro.writeData(trackInfofilePath, PropertiesTypeServerName+"."+keyList.get(i), String.valueOf(newPort));

					log.info("[port]" + returnValue.JsonHeaderValue(PropertiesTypeServerName, "filePath")
							+ " update key:[" + keyList.get(i) + "] and value:" + newPort);

				}
				
				/**
				 * @function:针对数据为singlePort的操作
				 * 
				 * @changedInfo:null
				 * */
				else if (typeList.get(i).equalsIgnoreCase("singlePort")) {

					RwPro.update(keyList.get(i), portInitList.get(i));
					
					createPro.writeData(trackInfofilePath, PropertiesTypeServerName+"."+keyList.get(i), portInitList.get(i));

					log.info("[singlePort]" + returnValue.JsonHeaderValue(PropertiesTypeServerName, "filePath")
							+ " update key:[" + keyList.get(i) + "] and value:" + portInitList.get(i));

				}

				// 针对数据为singlePort的操作
				else if (typeList.get(i).equalsIgnoreCase("ip")) {

					// 获取不同IP类型的具体的值
					String IPValue = null;

					if (IPList.get(i).equalsIgnoreCase("publicIP")) {

						IPValue = returnValue.publicIPValue();

					} else if (IPList.get(i).equalsIgnoreCase("localIP")) {

						IPValue = returnValue.localIPValue();
					} else {

						IPValue = returnValue.algorithmIPValue();
					}

					RwPro.update(keyList.get(i), IPValue);
					
					createPro.writeData(trackInfofilePath, PropertiesTypeServerName+"."+keyList.get(i), IPValue);

					log.info("[ip]" + returnValue.JsonHeaderValue(PropertiesTypeServerName, "filePath")
							+ " update key:[" + keyList.get(i) + "] and value:" + IPValue);

				} 
				/**
				 * @function:针对数据为URL的操作
				 * 
				 * @changedInfo:null
				 * */
				else if (typeList.get(i).equalsIgnoreCase("URL")) {

					// 获取不同IP类型的具体的值
					String IPValue = null;

					if (IPList.get(i).equalsIgnoreCase("publicIP")) {

						IPValue = returnValue.publicIPValue();

					} else if (IPList.get(i).equalsIgnoreCase("localIP")) {

						IPValue = returnValue.localIPValue();
					} else {

						IPValue = returnValue.algorithmIPValue();
					}
					int newPort = Integer.parseInt(portInitList.get(i)) + startPort;
					String Value = "http://"+IPValue+":"+newPort;

					RwPro.update(keyList.get(i), Value);
					
					createPro.writeData(trackInfofilePath, PropertiesTypeServerName+"."+keyList.get(i), Value);

					log.info("[URL]" + returnValue.JsonHeaderValue(PropertiesTypeServerName, "filePath")
							+ " update key:[" + keyList.get(i) + "] and value:" + Value);

				}
				/**
				 * @function:针对数据为singleURL的操作
				 * 
				 * @changedInfo:新增加type类型为singleURL的类型 兼容没有规律的value配置
				 * */
				else if (typeList.get(i).equalsIgnoreCase("singleURL")) {

					// 获取不同IP类型的具体的值
					String IPValue = null;

					if (IPList.get(i).equalsIgnoreCase("publicIP")) {

						IPValue = returnValue.publicIPValue();

					} else if (IPList.get(i).equalsIgnoreCase("localIP")) {

						IPValue = returnValue.localIPValue();
					} else {

						IPValue = returnValue.algorithmIPValue();
					}

			
					String Value = "http://"+IPValue+":"+portInitList.get(i);

					RwPro.update(keyList.get(i), Value);
					
					createPro.writeData(trackInfofilePath, PropertiesTypeServerName+"."+keyList.get(i), Value);

					log.info("[singleURL]" + returnValue.JsonHeaderValue(PropertiesTypeServerName, "filePath")
							+ " update key:[" + keyList.get(i) + "] and value:" + Value);

				}else {

					log.error(typeList.get(i) + "is an not existed type, please check the config UpdateConfig.json");
				}

			}
		}
	}

//	public static void main(String[] args) {
//
//		PropertiesFileBuss propertiesFileAction = new PropertiesFileBuss(
//				"F:\\workspaceEQ\\updateUtlis\\file\\application.properties");
//
//		propertiesFileAction.updateProperties("alarm");
//	}

}
