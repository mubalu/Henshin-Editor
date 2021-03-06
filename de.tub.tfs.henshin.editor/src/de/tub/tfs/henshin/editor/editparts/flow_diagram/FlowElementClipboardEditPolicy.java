/**
 * FlowElementClipboardEditPolicy.java
 *
 * Created 22.01.2012 - 16:09:46
 */
package de.tub.tfs.henshin.editor.editparts.flow_diagram;

import org.eclipse.emf.ecore.EObject;

import de.tub.tfs.henshin.editor.editparts.ClipboardEditPolicy;
import de.tub.tfs.henshin.editor.editparts.CopyRequest;
import de.tub.tfs.henshin.editor.editparts.PasteRequest;
import de.tub.tfs.henshin.editor.util.HenshinLayoutUtil;
import de.tub.tfs.henshin.model.flowcontrol.FlowElement;
import de.tub.tfs.henshin.model.layout.Layout;

/**
 * @author nam
 * 
 */
public class FlowElementClipboardEditPolicy extends ClipboardEditPolicy {
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tub.tfs.henshin.editor.editparts.ClipboardEditPolicy #performCopy
	 * (de.tub.tfs.henshin.editor.editparts.CopyRequest)
	 */
	@Override
	public void performCopy(CopyRequest req) {
		req.getContents().add(
				HenshinLayoutUtil.INSTANCE.getLayout((FlowElement) getHost()
						.getModel()));

		super.performCopy(req);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tub.tfs.henshin.editor.editparts.ClipboardEditPolicy
	 * #getPasteTarget (de.tub.tfs.henshin.editor.editparts.PasteRequest)
	 */
	@Override
	public EObject getPasteTarget(PasteRequest req) {
		FlowElement model = (FlowElement) getHost().getModel();
		Object o = req.getPastedObject();

		if (o instanceof Layout) {
			return HenshinLayoutUtil.INSTANCE.getLayoutSystem(model);
		}

		return model.getDiagram();
	}
}