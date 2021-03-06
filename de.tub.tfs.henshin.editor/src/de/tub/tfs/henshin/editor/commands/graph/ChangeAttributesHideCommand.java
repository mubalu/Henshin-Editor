/**
 * 
 */
package de.tub.tfs.henshin.editor.commands.graph;

import org.eclipse.gef.commands.Command;

import de.tub.tfs.henshin.model.layout.NodeLayout;

/**
 * @author Johann
 * 
 */
public class ChangeAttributesHideCommand extends Command {

	private final NodeLayout layout;

	/**
	 * @param layout
	 * @param hide
	 */
	public ChangeAttributesHideCommand(NodeLayout layout) {
		super();
		this.layout = layout;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	@Override
	public boolean canExecute() {
		return layout != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		layout.setHide(!layout.isHide());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	@Override
	public void undo() {
		execute();
	}

}
