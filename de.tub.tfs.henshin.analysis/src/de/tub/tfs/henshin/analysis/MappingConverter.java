package de.tub.tfs.henshin.analysis;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.henshin.model.Formula;
import org.eclipse.emf.henshin.model.Mapping;
import org.eclipse.emf.henshin.model.Node;

public class MappingConverter {

	public static HashSet<HashSet<Node>> convertMappings(List<Mapping>... mappings){
		HashSet<HashSet<Node>> result = new LinkedHashSet<HashSet<Node>>();
		LinkedList<Mapping> allMappings = new LinkedList<Mapping>();
		for (List<Mapping> mappingList : mappings) {
			allMappings.addAll(mappingList);
		}
		for (Mapping mapping : allMappings) {
			HashSet<Node> targetSet = null;
			Iterator<HashSet<Node>> iterator = result.iterator();
			while (iterator.hasNext()) {
				HashSet<Node> mappingSet = iterator.next();
				if (mappingSet.contains(mapping.getOrigin()) || mappingSet.contains(mapping.getImage())){
					if (targetSet == null){
						targetSet = mappingSet;
					} else {
						targetSet.addAll(mappingSet);
						iterator.remove();
					}
				}
			}
			if (targetSet == null){
				targetSet = new HashSet<Node>();
				result.add(targetSet);
			}
			targetSet.add(mapping.getImage());
			targetSet.add(mapping.getOrigin());
		
		}
		
		return result;
	}
	
	public static List<Mapping> getAllMappingsFromFormula(Formula formula){
		List<Mapping> result = new LinkedList<Mapping>();
		if (formula != null){
			TreeIterator<EObject> eAllContents = formula.eAllContents();
			while (eAllContents.hasNext()){
				EObject eObj = eAllContents.next();
				if (eObj instanceof Mapping){
					result.add((Mapping) eObj);
				}
			}
		}
		return result;
	}
}
