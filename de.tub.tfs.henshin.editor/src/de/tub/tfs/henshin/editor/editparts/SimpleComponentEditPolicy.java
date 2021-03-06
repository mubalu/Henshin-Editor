/**
 * SimpleComponentEditPolicy.java
 *
 * Created 28.12.2011 - 16:08:21
 */
package de.tub.tfs.henshin.editor.editparts;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import de.tub.tfs.muvitor.commands.SimpleDeleteEObjectCommand;

/**
 * A simple, convenient {@link ComponentEditPolicy}.<br/>
 * The created {@link Command delete command} is simply a
 * {@link SimpleDeleteEObjectCommand}.
 * 
 * @author nam
 * 
 */
public class SimpleComponentEditPolicy extends ComponentEditPolicy {
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editpolicies.ComponentEditPolicy#createDeleteCommand(
	 * org.eclipse.gef.requests.GroupRequest)
	 */
	@Override
	protected Command createDeleteCommand(GroupRequest deleteRequest) {
		Object hostModel = getHost().getModel();

		if (hostModel instanceof EObject) {
			return new SimpleDeleteEObjectCommand((EObject) hostModel);
		}

		return super.createDeleteCommand(deleteRequest);
	}
}
