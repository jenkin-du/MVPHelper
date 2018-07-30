package com.jenkin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Helper {

	public static void main(String[] args) {

		String path = "E:\\Java\\JavaWeb\\MVPHepler\\src\\test\\authentication"; // 要遍历的路径
		File file = new File(path); // 获取其file对象
		readFiles(file);

	}

	public static void readFiles(File file) {
		File[] fs = file.listFiles();
		for (File f : fs) {
			if (f.isDirectory()) // 若是目录，则递归打印该目录下的文件
				readFiles(f);
			if (f.isFile()) // 若是文件，直接打印
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
				implFolder.mkdirs();
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

}
