package com.twitminer.viewer.algorithm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ModelReader<T> {
	
	public ModelReader() {
		
	}
	
	public T getModelObj(File file) throws Exception, FileNotFoundException, IOException, ClassNotFoundException {
		return getModelObj(new FileInputStream(file));
	}
	
	@SuppressWarnings("unchecked")
	public T getModelObj(String filePath) throws Exception {
//		T modelRepresentation = null;
		ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(filePath));
		T cls = (T) ois.readObject();
		ois.close();
//		modelRepresentation = (T) weka.core.SerializationHelper.read(filePath);
		
		return cls;
	}
	
	@SuppressWarnings("unchecked")
	public T getModelObj(InputStream inputStream) throws Exception {
		ObjectInputStream ois = new ObjectInputStream(inputStream);
		T cls = (T) ois.readObject();
		ois.close();
		return cls;
	}
		
	public List<Map.Entry<String, T>> getMultipleModelObjs(File... modelFiles) throws Exception {
		List<Map.Entry<String, T>> resultFiles = new ArrayList<Map.Entry<String, T>>();
		
		for (File file : modelFiles) {
			resultFiles.add(new KeyValuePair<String, T>(file.getName(), getModelObj(file.getName())));
		}
		
		return resultFiles;
	}
	
	public List<Map.Entry<String, T>> getMultipleModelObjs(String... modelPaths) throws Exception {
		File[] pathToFile = new File[modelPaths.length];
		for (int i = 0; i < modelPaths.length; i++) {
			pathToFile[i] = new File(modelPaths[i]);
		}
		
		return this.getMultipleModelObjs(pathToFile);
	}

}
