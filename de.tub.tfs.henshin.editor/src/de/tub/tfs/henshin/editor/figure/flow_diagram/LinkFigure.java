/**
 * LinkFigure.java
 *
 * Created 08.01.2012 - 12:50:40
 */
package de.tub.tfs.henshin.editor.figure.flow_diagram;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Label;
import org.eclipse.swt.SWT;

import de.tub.tfs.henshin.editor.util.ResourceUtil;

/**
 * @author nam
 * 
 */
public class LinkFigure extends FlowElementFigure {

	private String name;

	private Label nameLabel;

	private Label iconLabel;

	/**
	 * Constructs an {@link LinkFigure}.
	 */
	public LinkFigure() {
		super();

		setAntialias(SWT.ON);
		setLineWidth(2);
		setForegroundColor(ColorConstants.gray);

		nameLabel = new Label(name);

		nameLabel.setForegroundColor(ColorConstants.black);

		iconLabel = new Label(ResourceUtil.ICONS.LINK.img(18));

		add(iconLabel);
		add(nameLabel);
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		if (name == null) {
			name = "<>";
			setToolTip("Link with no target");
		} else {
			setToolTip("Link to Flow Diagram \"" + name
					+ "\".\nDouble click to open.");
		}

		this.name = name;
		nameLabel.setText(name);
	}
}
