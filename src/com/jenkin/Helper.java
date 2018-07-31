package com.jenkin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Helper {

	public static void main(String[] args) {

		String path = "D:\\MyScreen\\Project\\MyScreen_Server\\src\\com\\uestc\\business\\dao"; // 要遍历的路径
		File file = new File(path); // 获取其file对象
		readFiles(file);

	}

	public static void readFiles(File file) {
		File[] fs = file.listFiles();
		for (File f : fs) {
			if (f.isDirectory()) // 若是目录，则递归打印该目录下的文件
				readFiles(f);
			if (f.isFile()) 
				if (!f.getParent().contains("impl")) {
					generateImpl(f);
				}

		}
	}

	public static void generateImpl(File file) {

		String className = file.getName().split("\\.")[0];
		String implfilePath = file.getParent() + "\\impl\\" + className + "Impl.java";
		File implFile = new File(implfilePath);
		File implFolder = new File(file.getParent() + "\\impl");

		FileReader reader = null;
		BufferedReader bufferReader = null;
		
		FileWriter writer=null;
		BufferedWriter bufferWriter=null;
		if (!implFile.exists()) {
			try {
				if(!implFolder.exists()) {
					implFolder.mkdirs();
				}
				implFile.createNewFile();

				reader = new FileReader(file);
				bufferReader = new BufferedReader(reader);
				String packageLine = "";
				while ((packageLine = bufferReader.readLine()) != null) {
					if (packageLine.contains("package"))
						break;
				}

				if (packageLine != null) {

					//包
					StringBuilder packageLineBuilder = new StringBuilder(packageLine);
					packageLineBuilder.deleteCharAt(packageLineBuilder.lastIndexOf(";"));
					packageLineBuilder.append(".impl;");
					String implPackageLine = packageLineBuilder.toString();

					//接口引入
					packageLine = packageLine.replace(";", "");
					packageLine = packageLine.replace("package", "");
					String importLine = "import" + packageLine + "." + className + ";";

					Date date=new  Date();
					SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");
					String dateStr=sdf.format(date);
					String statement="/**\r\n" + 
									" * <pre>\r\n" + 
									" *     author : jenkin\r\n" + 
									" *     e-mail : jekin-du@foxmail.com\r\n" + 
									" *     time   : "+dateStr+"\r\n" + 
									" *     desc   :\r\n" + 
									" *     version: 1.0\r\n" + 
									" * </pre>\r\n" + 
									" */\r\n";
					
					//类体
					String classBody = "public class "+className+"Impl implements "+className+"{\r\n \r\n}";
					
					//写文件
					writer=new FileWriter(implFile);
					bufferWriter=new BufferedWriter(writer);
					
					bufferWriter.write(implPackageLine);
					bufferWriter.write("\r\n");
					bufferWriter.write("\r\n");
					bufferWriter.write(importLine);
					bufferWriter.write("\r\n");
					bufferWriter.write("\r\n");
					bufferWriter.write(statement);
					bufferWriter.write(classBody);
					
					bufferWriter.flush();
				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					bufferReader.close();
					reader.close();
					
					bufferWriter.close();
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
	}
	
	/**
	 * 替换文本文件中的 非法字符串
	 * 
	 * @param path
	 * @throws IOException
	 */
	public static void replacTextContent(File file) throws IOException {

		// 原有的内容
		String srcStr = "class";
		// 要替换的内容
		String replaceStr = "interface";
		// 读
		FileReader in = new FileReader(file);
		BufferedReader bufIn = new BufferedReader(in);
		// 内存流, 作为临时流
		CharArrayWriter tempStream = new CharArrayWriter();
		// 替换
		String line = null;
		while ((line = bufIn.readLine()) != null) {
			// 替换每行中, 符合条件的字符串
			line = line.replaceAll(srcStr, replaceStr);
			// 将该行写入内存
			tempStream.write(line);
			// 添加换行符
			tempStream.append(System.getProperty("line.separator"));
		}
		// 关闭 输入流
		bufIn.close();
		// 将内存中的流 写入 文件
		FileWriter out = new FileWriter(file);
		tempStream.writeTo(out);
		out.close();

	}

}
