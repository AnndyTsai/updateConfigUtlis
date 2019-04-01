/**
 * @author ********yangbin********
 * 
 * @time 2019-03-04-09:58:53 CTS
 * 
 * @function: 实现对properties文件进行 增、删、改、查
 * 
 * @changedInfo:
 * 
 * */
package cn.UpdateConfigs.fileBasic.RwProperties;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RwPropertiesUtlis {

	private String filePath;
	private Properties properties;
	private static final Logger log = LogManager.getLogger(RwPropertiesUtlis.class.getName());
	// 存放修改后的端口 IP信息
	// private String tagFilePath = "./file/UpdateInfo.properties";

	/**
	 * @throws Exception
	 * @function:构造方法 创建对象时自动返回pro对象 在new该对象的时候会自动加载readProperties()方法
	 * 
	 * @changedInfo:null
	 */
	public RwPropertiesUtlis(String filePath) {
		this.filePath = filePath;
		// 在new该对象的时候会自动加载readProperties()方法
		this.properties = readProperties();
	}

	public Properties readProperties() {
		// 创建对象
		Properties pro = new Properties();
		log.info("Building Properties object");
		// 读取properties文件到缓存
		BufferedInputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(filePath));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("Read buffer into stream success, Data [" + in.toString() + "]");
		// 加载缓存到pro对象 prop.load(in)这么写 不能读取properties配置文件中的中文
		try {
			pro.load(new InputStreamReader(in, "utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("Load stream success, set the encoding is utf-8");

		// 返回pro对象
		log.debug("Return pro object,Data[" + pro.toString() + "]");
		return pro;
	}

	/**
	 * @function:使用全局的properties对象获取key对应的value值
	 * 
	 * @changedInfo:null
	 */
	public String getValue(String key) {

		log.debug("get the [" + key + "] value:" + properties.getProperty(key));
		return properties.getProperty(key);
	}

	/**
	 * @throws Exception
	 * @function:修改指定位置的properties value值
	 * 
	 * @changedInfo:1:增加判断Key是否为null 为null不做操作 日志输出ERROR信息
	 */
	public void update(String Key, String Value) throws Exception {

		PropertiesConfiguration config = new PropertiesConfiguration(filePath);

		if (config.getProperty(Key) == null) {

			log.error(Key + " not exist on the file:" + filePath);

		} else {

			config.setAutoSave(true);

			config.setProperty(Key, Value);

			log.info("set the file " + filePath + "[" + Key + "] value is " + Value);

		}
	}

	public static void main(String[] args) {

		RwPropertiesUtlis utlis = new RwPropertiesUtlis("F:\\workspaceEQ\\updateUtlis\\file\\application1.properties");

		try {
			utlis.update("msgCon1tent", "123411144");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
