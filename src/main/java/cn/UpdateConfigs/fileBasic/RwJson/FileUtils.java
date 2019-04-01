/**
 * @author ********yangbin********
 * 
 * @time 2019-03-04-09:58:53 CTS
 * 
 * @function: 以行为单位读取文件，常用于读面向行的格式化文件
 * 
 * @changedInfo: 增加日志输出
 * 
 * */
package cn.UpdateConfigs.fileBasic.RwJson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FileUtils {

	private static final Logger log = LogManager.getLogger(FileUtils.class.getName());

	public static String readJsonData(String pactFile) throws Exception {
		// 读取文件数据
		// System.out.println("读取文件数据util");

		StringBuffer strbuffer = new StringBuffer();
		File myFile = new File(pactFile);
		if (!myFile.exists()) {

			log.error("Can't Find " + pactFile);

		}
		FileInputStream fis = new FileInputStream(pactFile);
		InputStreamReader inputStreamReader = new InputStreamReader(fis, "UTF-8");
		BufferedReader in = new BufferedReader(inputStreamReader);

		String str;
		while ((str = in.readLine()) != null) {
			strbuffer.append(str);
		}
		in.close();

		return strbuffer.toString();

	}

	/**
	 * @function:把json格式的字符串写到文件
	 * 
	 * @changedInfo:
	 */
	public static boolean writeFile(String filePath, String sets) {
		FileWriter fw;
		try {

			log.info("Write json format to " + filePath);
			fw = new FileWriter(filePath);
			PrintWriter out = new PrintWriter(fw);
			out.write(sets);
			out.println();
			fw.close();
			out.close();
			return true;
		} catch (IOException e) {

			log.error("Write json format exception " + filePath);
			e.printStackTrace();
			return false;
		}

	}

}
