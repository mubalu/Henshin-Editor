/**
 * 
 */
package de.tub.tfs.henshin.editor.commands.transSys;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;

/**
 * The Class MoveTransformationUnitCommand.
 * 
 * @author Johann
 */
public class MoveEObjectCommand extends Command {

	/** The list. */
	private EList<EObject> list;

	/** The index. */
	private int index;

	/** The new index. */
	private int newIndex;

	/**
	 * Instantiates a new move transformation unit command.
	 * 
	 * @param list
	 *            the list
	 * @param oldIndex
	 *            the old index
	 * @param newIndex
	 *            the new index
	 */
	public MoveEObjectCommand(EList<EObject> list, int oldIndex, int newIndex) {
		super();
		this.list = list;
		this.index = oldIndex;
		this.newIndex = newIndex;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	@Override
	public boolean canExecute() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		list.move(newIndex, index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	@Override
	public void undo() {
		list.move(index, newIndex);
	}

}
