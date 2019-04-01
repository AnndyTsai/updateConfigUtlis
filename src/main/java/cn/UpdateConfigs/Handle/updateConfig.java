/**
 * @author ********yangbin********
 * 
 * @time 2019-03-11-13:14:00 CTS
 * 
 * @function: 更新配置文件的Main方法 
 * 
 * @defind:
 * */
package cn.UpdateConfigs.Handle;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.UpdateConfigs.Buss.JsonFile.JsonFileBuss;
import cn.UpdateConfigs.Buss.PropertiesFile.PropertiesFileBuss;
import cn.UpdateConfigs.Buss.Utlis.returnConfigJsonValue;
import cn.UpdateConfigs.Buss.XMLFile.XMLFileBuss;
import cn.UpdateConfigs.S17.Uninstall.uninstallAction;

public class updateConfig {

	private static final Logger log = LogManager.getLogger(updateConfig.class.getName());

	/**
	 * @function：执行update操作 Main class
	 * 
	 * @changeInfo:
	 */

	public static void main(String[] args) {

		returnConfigJsonValue configJsonValue = new returnConfigJsonValue();

		updateAction action = new updateAction("./file/UpdateConfig.properties");

		// 创建处理Json格式文件的对象
		JsonFileBuss JsonBuss = new JsonFileBuss();
		// 创建处理XML格式文件的对象
		PropertiesFileBuss propertiesFileBuss = null;
		// 创建处理properties格式文件的对象
		XMLFileBuss xmlFileBuss = new XMLFileBuss();

		List<String> serverNames = action.serverNames();

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		System.out.println("**************************************");
		System.out.println("************ UpdateConfig ************");
		System.out.println("********** Powered By Java1.8 ********");
		System.out.println("***********  Version:1.1   ***********");
		System.out.println("*************  2019.3.27   ***********");
		System.out.println("**************************************");

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (args[0].equalsIgnoreCase("uninstallS17")) {

			uninstallAction uninstallAction = new uninstallAction();

			uninstallAction.uninstalled("rpm -qa|grep S17");

		} else if (args[0].equalsIgnoreCase("update")) {
			
			for (int i = 0; i < serverNames.size(); i++) {

				// 判断是否更新的标识位 true为更新 false为不更新该服务
				if (action.isUpdate(serverNames.get(i))) {

					log.fatal("******************* Start to Update " + serverNames.get(i) + "  ********************");

					// 对properties类型的文件进行操作
					if (action.fileType(serverNames.get(i)).equalsIgnoreCase("properties")) {

						try {
							propertiesFileBuss = new PropertiesFileBuss(action.filePath(serverNames.get(i)));

							propertiesFileBuss.updateProperties(serverNames.get(i));
						} catch (Exception e) {

							log.error("---------------------Update " + serverNames.get(i)
									+ " Exception-------------------");
							e.printStackTrace();
						}

					}
					// 对XML格式的文件进行操作
					else if (action.fileType(serverNames.get(i)).equalsIgnoreCase("XML")) {

						try {
							xmlFileBuss.updateXMLAction(serverNames.get(i), action.filePath(serverNames.get(i)));
						} catch (Exception e) {
							log.error("---------------------Update " + serverNames.get(i)
									+ " Exception-------------------");
							e.printStackTrace();
						}

					}
					// 对Json格式的文件进行操作
					else if (action.fileType(serverNames.get(i)).equalsIgnoreCase("json")) {

						try {
							JsonBuss.updateJsonFile(serverNames.get(i), action.filePath(serverNames.get(i)));
						} catch (Exception e) {

							log.error("---------------------Update " + serverNames.get(i)
									+ " Exception-------------------");
							e.printStackTrace();
						}

					} else {

						log.fatal("******************* [Error] Update " + serverNames.get(i)
								+ " found the Unknown fileType ********************");

					}

					// 针对isUpdate状态标识符为false的操作
				} else {

					log.fatal("******************* " + serverNames.get(i)
							+ "  Unnecessary to Update********************");
				}

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}

	}

}
