package de.tub.tfs.muvitor.commands;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.commands.Command;

/**
 * Document SimpleDeleteEObjectCommand
 * 
 * @author Tony
 * 
 */
public class SimpleDeleteEObjectCommand extends Command {

    final private EStructuralFeature containingFeature;

    final private EObject model;

    final private EObject parent;

    public SimpleDeleteEObjectCommand(final EObject model,
	    final EObject parent, final EStructuralFeature feature) {
	this.model = model;
	this.parent = parent;
	this.containingFeature = feature;
    }

    public SimpleDeleteEObjectCommand(final EObject model) {
	this(model, model.eContainer(), model.eContainingFeature());
    }

    @Override
    public boolean canExecute() {
	return model != null && parent != null;
    }

    @Override
    public void execute() {
	// see EcoreUtil.delete
	((List<?>) parent.eGet(containingFeature)).remove(model);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void undo() {
	((List<Object>) parent.eGet(containingFeature)).add(model);
    }
}
