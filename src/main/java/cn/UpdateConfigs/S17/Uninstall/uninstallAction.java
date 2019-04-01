/**
 * @author ********yangbin********
 * 
 * @time 2019-03-27-09:38:53 CTS
 * 
 * @function: 卸载S17
 * 
 * @changedInfo: null
 * 
 * */
package cn.UpdateConfigs.S17.Uninstall;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.UpdateConfigs.fileBasic.PortUtils.portIsConflict;


public class uninstallAction {
	
	private static final Logger log = LogManager.getLogger(uninstallAction.class.getName());
	private portIsConflict servers;

	public uninstallAction(){
		
		servers = new portIsConflict();
	}
	
	/**
	 * @function:获取服务列表List集合
	 * 
	 * @changedInfo:null
	 * */
	
	public List<String> serverList(String commonds){
		
		List<String> list = servers.toList(commonds);
		
		for(int i = 0 ; i < list.size() ; i++){
			
			log.fatal("[S17 Uninstalled Servers]:"+list.get(i));
		}
		
		return list;
	}
	
	/**
	 * @function:执行卸载动作
	 * 
	 * @changedInfo:null
	 * */
	public void uninstalled(String commonds){
		
		for(int i = 0 ; i < this.serverList(commonds).size() ; i++){
			
			String cmd = "rpm -e --nodeps "+this.serverList(commonds).get(i);
			
			servers.toList(cmd);
		}
	}

}
