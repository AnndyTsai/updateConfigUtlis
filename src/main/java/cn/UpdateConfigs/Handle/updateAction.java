/**
 * @author ********yangbin********
 * 
 * @time 2019-03-11-13:14:00 CTS
 * 
 * @function: 返回UpdateConfig.json文件的Header信息
 * 
 * @defind:
 * */
package cn.UpdateConfigs.Handle;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.UpdateConfigs.Buss.Utlis.returnConfigJsonValue;
import cn.UpdateConfigs.fileBasic.RwJson.HandleJson;
import cn.UpdateConfigs.fileBasic.RwProperties.RwPropertiesUtlis;

public class updateAction {
	
	private static final Logger log = LogManager.getLogger(updateAction.class.getName());
	
	private HandleJson handleJson;
	private RwPropertiesUtlis rwPro;
	private returnConfigJsonValue returnValue;
	/**
	 * 构造方法初始化对象
	 * @throws Exception 
	 * */
	public updateAction(String filePath){
		
		handleJson = new HandleJson();
		log.debug("create object with HandleJson method");
		rwPro = new RwPropertiesUtlis(filePath);
		log.debug("create rwPro object with RwPropertiesUtlis method");
		returnValue = new returnConfigJsonValue();
		log.debug("create returnValue object with returnConfigJsonValue method");	
	}
	/**
	 * @function：根据servername获取filePath
	 * 
	 * @changeInfo: 
	 */
	public String filePath(String serverName){
		
		log.debug("return filePath for "+serverName);
		return returnValue.JsonHeaderValue(serverName, "filePath");
	}
	/**
	 * @function：获取isUpdate的状态
	 * 
	 * @changeInfo: 
	 */
	public boolean isUpdate(String serverName){
		
		log.debug("return isUpdate status for "+serverName);
		if(returnValue.JsonHeaderValue(serverName, "isUpdate").equalsIgnoreCase("true")){
			
			log.debug("return isUpdate status for "+serverName+" is true");
			return true;
		}else{
			
			log.debug("return isUpdate status for "+serverName+" is false");
			return false;
		}
	}
	/**
	 * @function：根据servername获取fileType
	 * 
	 * @changeInfo: 
	 */
	public String fileType(String serverName){
		
		log.debug("return fileType for "+serverName);
		return returnValue.JsonHeaderValue(serverName, "fileType");
	}
	
	/**
	 * @throws Exception 
	 * @function：根据UpdateConfig.properties文件的配置获取server名称的集合
	 * 
	 * @changeInfo: 
	 */
	public List<String> serverNames(){
		
		List<String> serverNameList = new ArrayList<>();
		
		updateAction action = new updateAction("./file/UpdateConfig.properties");
		
		String[] string = action.rwPro.getValue("servers").split(">");
		
		for(int i = 0 ; i < string.length ; i++){  
			
				serverNameList.add(string[i]);
		}
		
		return serverNameList;
	}
//	
//	public static void main(String[] args) {
//		
//		updateAction action = new updateAction("./file/UpdateConfig.properties");
//		
//		System.out.println(action.rwPro.getValue("servers"));
//	}

}
