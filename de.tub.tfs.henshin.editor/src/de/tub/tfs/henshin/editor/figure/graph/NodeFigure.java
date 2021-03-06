/**
 * 
 */
package de.tub.tfs.henshin.editor.figure.graph;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;

/**
 * @author Johann
 * 
 */
public abstract class NodeFigure extends RectangleFigure {

	/** The Constant Display. */
	static final Device Display = null;

	/** The gradient color1. */
	protected Color gradientColor1 = ColorConstants.green;
	/** The gradient color2. */
	protected Color gradientColor2 = ColorConstants.white;

	/** The width. */
	protected int width;

	/** The height. */
	protected int height = 25;

	public NodeFigure() {
		super();
	}

	/**
	 * @return the hide
	 */
	public abstract boolean isHide();

	/**
	 * Sets the hide.
	 * 
	 * @param hide
	 *            the new hide
	 */
	public abstract void setHide(boolean hide);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.draw2d.Figure#setBackgroundColor(org.eclipse.swt.graphics
	 * .Color)
	 */
	@Override
	public void setBackgroundColor(Color bg) {
		gradientColor1 = bg;
	}

	/**
	 * Gets the value label text bounds.
	 * 
	 * @return the value label text bounds
	 */
	public abstract Rectangle getValueLabelTextBounds();

	public abstract void setName(String name);

}