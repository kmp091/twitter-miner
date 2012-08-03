package com.twitminer.viewer.algorithm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ModelReader<T> {
	
	public ModelReader() {
		
	}
	
	public T getModelObj(String filePath) throws Exception {
		return getModelObj(new File(filePath));
	}
	
	@SuppressWarnings("unchecked")
	public T getModelObj(File file) throws Exception, FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream input = new ObjectInputStream(new FileInputStream(file));
		T modelRepresentation = null;
		modelRepresentation = (T) weka.core.SerializationHelper.read(input);
		
		return modelRepresentation;
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
