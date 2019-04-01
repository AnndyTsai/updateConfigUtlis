/**
 * @author ********yangbin********
 * 
 * @time 2019-03-06-09:38:53 CTS
 * 
 * @function: 检查端口是否冲突
 * 
 * @changedInfo: null
 * 
 * */
package cn.UpdateConfigs.fileBasic.PortUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * @function:执行命令 检查单个端口时候被TCP监听 返回boolean类型
 * 
 * @changedInfo:null
 * */
public class portIsConflict {
	
	private static final Logger log = LogManager.getLogger(portIsConflict.class.getName());
	
	public Boolean isConflict(String port){
		
		String cmd = "netstat -anop|grep "+port+"";
		
		
		List<String> list = this.toList("cmd");
		
		if(list.size() != 0){
			
			log.error("Port:"+port+"is used");
			
			return false;
		}else{
			
			log.info("Port:"+port+"available");
			
			return true;
		}	
		
	}
	
	/**
	 * @function:执行liunx命令 返回打印日志的集合
	 * 
	 * @changedInfo:1：增加判断系统类型 执行不同的命令 兼容windows系统
	 * */
	public List<String> toList(String commonds){
		
		InputStreamReader isr =null;
		BufferedReader br = null;
 
		List<String> list = new ArrayList<>();
 
		Runtime runtime = Runtime.getRuntime();
 
		try {
 
			String[] cmd = null;
			
			if(System.getProperty("os.name").contains("ndows")){
				
				cmd = new String[] { "cmd", "-c", commonds };
				
				log.info("running on "+System.getProperty("os.name")+" OS with CMD commond:"+commonds);
				
			}else{
				
				cmd = new String[] { "/bin/sh", "-c", commonds };
				
				log.info("running on "+System.getProperty("os.name")+" OS with shell commond:"+commonds);
			}
 
			Process proc = runtime.exec(cmd);
 
			InputStream stderr = proc.getInputStream();
 
			isr = new InputStreamReader(stderr, "GBK");
 
			br = new BufferedReader(isr);
 
			String line = "";
			while ((line = br.readLine()) != null) {
 
				list.add(line);
			}
 
		} catch (IOException e) {
			
			log.error("running on "+System.getProperty("os.name")+" OS with commond:"+commonds +" exception");
			e.printStackTrace();
		}finally{
			
			try {
				br.close();
				isr.close();
				
			} catch (IOException e) {
				
				e.printStackTrace();
			}		
		}
 
		return list;

	}

}
