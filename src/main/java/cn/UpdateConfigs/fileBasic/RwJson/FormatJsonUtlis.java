/**
 * @author ********yangbin********
 * 
 * @time 2019-03-04-09:58:53 CTS
 * 
 * @function: 格式化输入工具类
 * 
 * @changedInfo: 增加日志输出
 * 
 * */
package cn.UpdateConfigs.fileBasic.RwJson;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FormatJsonUtlis {
	
	private static final Logger log = LogManager.getLogger(FormatJsonUtlis.class.getName());
 
	/**
	 * @function:格式化Json数据
	 * 
	 * @changedInfo:
	 */
    public static String formatJson(String jsonStr) {
    	
    	log.debug("Json formats building");
        if (null == jsonStr || "".equals(jsonStr))
            return "";
        StringBuilder sb = new StringBuilder();
        char last = '\0';
        char current = '\0';
        int indent = 0;
        boolean isInQuotationMarks = false;
        for (int i = 0; i < jsonStr.length(); i++) {
            last = current;
            current = jsonStr.charAt(i);
            switch (current) {
            case '"':
                                if (last != '\\'){
                    isInQuotationMarks = !isInQuotationMarks;
                                }
                sb.append(current);
                break;
            case '{':
            case '[':
                sb.append(current);
                if (!isInQuotationMarks) {
                    sb.append('\n');
                    indent++;
                    addIndentBlank(sb, indent);
                }
                break;
            case '}':
            case ']':
                if (!isInQuotationMarks) {
                    sb.append('\n');
                    indent--;
                    addIndentBlank(sb, indent);
                }
                sb.append(current);
                break;
            case ',':
                sb.append(current);
                if (last != '\\' && !isInQuotationMarks) {
                    sb.append('\n');
                    addIndentBlank(sb, indent);
                }
                break;
            default:
                sb.append(current);
            }
        }
 
        return sb.toString();
    }
	/**
	 * @function:添加space
	 * 
	 * @changedInfo:
	 */
    private static void addIndentBlank(StringBuilder sb, int indent) {
    	
    	log.debug("add \\t for building the json formats");
        for (int i = 0; i < indent; i++) {
            sb.append('\t');
        }
    }
}

