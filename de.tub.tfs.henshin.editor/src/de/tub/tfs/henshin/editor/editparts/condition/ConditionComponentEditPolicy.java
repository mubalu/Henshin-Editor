/**
 * 
 */
package de.tub.tfs.henshin.editor.editparts.condition;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.henshin.model.Formula;
import org.eclipse.emf.henshin.model.Graph;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import de.tub.tfs.henshin.editor.commands.rule.DeleteFormulaCommand;

/**
 * The Class ConditionComponentEditPolicy.
 * 
 * @author Angeline Warning
 */
public class ConditionComponentEditPolicy extends ComponentEditPolicy implements
		EditPolicy {

	/**
	 * Create a delete command to delete a formula, only if the parent is a
	 * graph or a formula.
	 * 
	 * @param deleteRequest
	 *            A request to delete a formula.
	 * @return Command Delete command
	 */
	@Override
	protected Command createDeleteCommand(GroupRequest deleteRequest) {
		Formula toDelete = (Formula) getHost().getModel();
		EObject parent = toDelete.eContainer();
		if (parent instanceof Graph || parent instanceof Formula) {
			return new DeleteFormulaCommand(toDelete, parent);
		}

		return null;
	}

}
