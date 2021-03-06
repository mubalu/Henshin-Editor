/**
 * EObjectsModelCreationFactory.java
 *
 * Created 19.12.2011 - 01:16:37
 */
package de.tub.tfs.henshin.editor.model;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.requests.CreationFactory;

/**
 * A basic implementation of {@link CreationFactory} to create {@link EObject}s
 * for a given {@link EClass} by calling the generic
 * {@link EFactory#create(EClass)} method of a given {@link EFactory} instance.
 * 
 * @author nam
 * 
 */
public class EObjectsModelCreationFactory implements CreationFactory {

	/**
	 * An {@link EFactory} instance.
	 */
	private EFactory factoryInstance;

	/**
	 * The {@link EClass} meta type of the new object.
	 */
	private EClass modelEClass;

	/**
	 * @param factoryInstance
	 * @param modelClass
	 */
	public EObjectsModelCreationFactory(EFactory factoryInstance,
			EClass modelClass) {
		super();

		this.factoryInstance = factoryInstance;
		this.modelEClass = modelClass;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.requests.CreationFactory#getNewObject()
	 */
	@Override
	public Object getNewObject() {
		return factoryInstance.create(modelEClass);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.requests.CreationFactory#getObjectType()
	 */
	@Override
	public Object getObjectType() {
		return modelEClass;
	}

}
