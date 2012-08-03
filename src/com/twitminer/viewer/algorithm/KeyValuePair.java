package com.twitminer.viewer.algorithm;

import java.util.Map.Entry;

public class KeyValuePair<K, V> implements Entry<K, V> {

	K key;
	V value;
	
	public KeyValuePair (K key, V value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public K getKey() {
		return key;
	}

	@Override
	public V getValue() {
		return value;
	}

	@Override
	public V setValue(V value) {
		V oldValue = this.value;
		this.value = value;
		return oldValue;
	}

	

}
