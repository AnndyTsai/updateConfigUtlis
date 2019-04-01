/**
 * @author ********yangbin********
 * 
 * @time 2019-03-06-09:38:53 CTS
 * 
 * @function: 返回UpdateConfig.json配置文件的数据
 * 
 * @changedInfo: 1：新增加 trackFilePath() method @time：2019-03-08
 * 
 * */
package cn.UpdateConfigs.Buss.Utlis;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.UpdateConfigs.fileBasic.RwJson.HandleJson;
import cn.UpdateConfigs.fileBasic.RwProperties.RwPropertiesUtlis;

public class returnConfigJsonValue {

	private HandleJson handleJson = new HandleJson();
	private RwPropertiesUtlis RwPro = new RwPropertiesUtlis("./file/UpdateConfig.properties");

	private static final Logger log = LogManager.getLogger(returnConfigJsonValue.class.getName());

	/**
	 * @function：返回startPort的Value
	 * 
	 * @changeInfo: 
	 */
	public int startPortValue() {

		String startPortValue = handleJson
				.returnValue(RwPro.getValue("jsonFilePath"), "$.Comms.portIsContinuous.startPort").toString();

		log.info("return startPort init value:" + startPortValue);
		return Integer.parseInt(startPortValue);
	}
	/**
	 * @function：根据服务名称返回value的值
	 * 
	 * @changeInfo: 
	 */
	public String JsonHeaderValue(String serverName, String Key) {

		log.info("return JsonHeaderValue for " + serverName + "." + Key);
		return handleJson.returnValue(RwPro.getValue("jsonFilePath"), "$.Layout." + serverName + "." + Key + "")
				.toString();
	}
	/**
	 * @function：根据Json key返回List集合
	 * 
	 * @changeInfo: 
	 */
	@SuppressWarnings("unchecked")
	public List<String> JsonContextVaule(String serverName, String Key) {

		log.info("return JsonContextVaule for " + serverName + "." + Key);
		return (List<String>) handleJson.returnValue(RwPro.getValue("jsonFilePath"),
				"$.Layout." + serverName + "[*].[*]." + Key + "");
	}
	
	/**
	 * @function：.Comms.portIsContinuous[*]
	 * 
	 * @changeInfo: 
	 */
	
	public Object returnPortIsContinuousValue(String value){
		
		log.info("return returnPortIsContinuousValue:"
				+ handleJson.returnValue(RwPro.getValue("jsonFilePath"), ".Comms.portIsContinuous."+value).toString());

		return handleJson.returnValue(RwPro.getValue("jsonFilePath"), ".Comms.portIsContinuous."+value).toString();
	}
	
	/**
	 * @function：返回 localIP publicIP algorithmIP的值
	 * 
	 * @changeInfo: 
	 */
	// 返回 localIP
	public String localIPValue() {

		log.info("return localIPValue:"
				+ handleJson.returnValue(RwPro.getValue("jsonFilePath"), "$.Comms.IPConfigs.localIP").toString());

		return handleJson.returnValue(RwPro.getValue("jsonFilePath"), "$.Comms.IPConfigs.localIP").toString();
	}

	// 返回 publicIP
	public String publicIPValue() {
		
		log.info("return publicIPValue:"
				+ handleJson.returnValue(RwPro.getValue("jsonFilePath"), "$.Comms.IPConfigs.publicIP").toString());

		return handleJson.returnValue(RwPro.getValue("jsonFilePath"), "$.Comms.IPConfigs.publicIP").toString();
	}

	// 返回 localIP
	public String algorithmIPValue() {
		
		log.info("return publicIPValue:"
				+ handleJson.returnValue(RwPro.getValue("jsonFilePath"), "$.Comms.IPConfigs.algorithmIP").toString());

		return handleJson.returnValue(RwPro.getValue("jsonFilePath"), "$.Comms.IPConfigs.algorithmIP").toString();
	}
	
	//返回Track信息
	public String trackFilePath(){
		
		return RwPro.getValue("tagFilePath");
	}
	
	public static void main(String[] args) {

		returnConfigJsonValue value = new returnConfigJsonValue();

		System.out.println(Integer.parseInt(value.returnPortIsContinuousValue("Safety").toString()));
	}
}
