package org.apel.gaia.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GenericTypePropCopyUtil<T> {

	public List<T> copyPropertiesFromList(List<?> sList, Class<T> targetClass){
		List<T> tList = new ArrayList<>();
		try {
			for (Object s : sList) {
				T t = targetClass.newInstance();
				BeanUtils.copyNotNullProperties(s, t);
				tList.add(t);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return tList;
	}
	
	public List<T> copyPropertiesFromList(List<?> sList, Class<T> targetClass, CopyCallback<T> callback){
		List<T> tList = new ArrayList<>();
		try {
			for (Object s : sList) {
				T t = targetClass.newInstance();
				callback.execute(s, t);
				BeanUtils.copyNotNullProperties(s, t);
				tList.add(t);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return tList;
	}
	
	public Set<T> copyPropertiesFromSet(Set<?> sSet, Class<T> targetClass){
		Set<T> tSet = new HashSet<>();
		try {
			for (Object s : sSet) {
				T t = targetClass.newInstance();
				BeanUtils.copyNotNullProperties(s, t);
				tSet.add(t);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return tSet;
	}
	
	public Set<T> copyPropertiesFromSet(Set<?> sSet, Class<T> targetClass, CopyCallback<T> callback){
		Set<T> tSet = new HashSet<>();
		try {
			for (Object s : sSet) {
				T t = targetClass.newInstance();
				callback.execute(s, t);
				BeanUtils.copyNotNullProperties(s, t);
				tSet.add(t);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return tSet;
	}
	
	
}
