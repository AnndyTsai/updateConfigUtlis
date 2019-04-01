更新配置文件说明：

1：执行文件目录结构
|————XXX.jar
|————file文件夹
	|————UpdateConfig.json
	|————UpdateConfig.properties
	|————ReadMe.txt
	
	1.2：type定义
  		1.2.1:porperties类型type为：ip、port、singlePort,URL,singleURL
  		1.2.2：Json类型type为：port ip singel,singlePort
  		1.2.3:XML类型type为：IP,port,portAndIP,URL,singlePortAndIP,singleURL
 
 2：代码目录结构
 |------UpdateConfigs
 	|------fileBasic
        |-----PortUtlis
            |————portIsConflict.java
        |-----RwJson
            |————FileUtils.java
            |————FormatJsonUtlis.java
            |————HandleJson.java
        |-----RwPropertties
            |————createProperties.java
            |————RwPropertiesUtlis.java
        |-----RwXML
            |————UpdatXMLAttr.java
 |
 |