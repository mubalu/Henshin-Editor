/**
 * 
 */
package de.tub.tfs.henshin.editor.ui.dialog.resources;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;

/**
 * The Class ContainterValidator.
 * 
 * @author nam
 */
public class ContainterValidator implements IResourceValidator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * transeditor.ui.dialog.resources.IResourceValidator#isValid(java.lang.
	 * Object )
	 */
	@Override
	public boolean isValid(IResource obj) {
		return obj instanceof IContainer
				&& (!(obj instanceof IProject) || ((IProject) obj).isOpen());
	}

}
