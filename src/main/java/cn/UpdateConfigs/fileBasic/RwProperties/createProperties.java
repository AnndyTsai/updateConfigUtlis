/**
 * @author ********yangbin********
 * 
 * @time 2019-03-07-09:58:53 CTS
 * 
 * @function: 写入数据到properties文件 tracking配置文件修改记录
 * 
 * @changedInfo:新增class
 * 
 * */
package cn.UpdateConfigs.fileBasic.RwProperties;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class createProperties {
	
	private static final Logger log = LogManager.getLogger(createProperties.class.getName());

	/**
	 * @function:创建properties文件
	 * 
	 * @changedInfo:增加兼容windows
	 */
	public String createProFile(String tagFilePath) {
		
		//获取时间
		 SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH");
		 String time = df.format(new Date());
		 log.info("new time is :"+time);
		 
		 //重新组装tagFilePath
		 File files = new File(tagFilePath);
		 String proFileName = files.getName();
		 String profile = files.getParentFile().toString();
		 
		 String suffix = proFileName.substring(proFileName.lastIndexOf(".") + 1);
		 
		 String proName = proFileName.split("."+suffix)[0];
		 
		 if(System.getProperty("os.name").contains("indow")){
			 
			 tagFilePath = profile +"\\"+ proName +time +"."+suffix;
			 
		 }else{
			 
			 tagFilePath = profile +"/"+ proName +time +"."+suffix;
		 }
 
		 log.info("new tagFilePath is:"+tagFilePath);
		
		String newCreatedfilePath = null;
		try {

			File file = new File(tagFilePath);
			log.info("create properties path is : "+tagFilePath);
			File fileParent = file.getParentFile();
			log.info("properties file path is :"+fileParent);

			if (!fileParent.exists()) {
				
				file.mkdirs();
				log.info(fileParent+" not existed, now create it");
			}

			file.createNewFile();
			log.info("create "+tagFilePath+" success");
			
			newCreatedfilePath = tagFilePath;			
			
		} catch (IOException e) {
			
			log.error("create "+tagFilePath+" exception, Failed to create the file: "+tagFilePath);
			e.printStackTrace();
		}
		
		return newCreatedfilePath;
	}
	
	/**
	 * @function:properties文件写入数据
	 * 
	 * @changedInfo:null
	 */
	public void writeData(String tagFilePath , String Key , String Value){
		
		RwPropertiesUtlis propertiesUtlis = new RwPropertiesUtlis(this.createProFile(tagFilePath));
		
		try {
			propertiesUtlis.update(Key, Value);
		} catch (Exception e) {
			
			log.error(tagFilePath+" file write data[ Key="+Key+",Value="+Value+"] exception");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info(tagFilePath+" file write data[ Key="+Key+",Value="+Value+"]");
	}
	
//	public static void main(String[] args) {
//		
//		new createProperties().writeData("./file/pro.properties", "key1", "value");
//	}
}