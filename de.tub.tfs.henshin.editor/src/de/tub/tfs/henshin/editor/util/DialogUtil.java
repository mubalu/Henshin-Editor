package de.tub.tfs.henshin.editor.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.henshin.model.ConditionalUnit;
import org.eclipse.emf.henshin.model.Graph;
import org.eclipse.emf.henshin.model.IndependentUnit;
import org.eclipse.emf.henshin.model.Node;
import org.eclipse.emf.henshin.model.PriorityUnit;
import org.eclipse.emf.henshin.model.Rule;
import org.eclipse.emf.henshin.model.SequentialUnit;
import org.eclipse.emf.henshin.model.TransformationSystem;
import org.eclipse.emf.henshin.model.TransformationUnit;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

import de.tub.tfs.henshin.editor.interfaces.Messages;
import de.tub.tfs.henshin.editor.ui.dialog.AttributeTableDialog;
import de.tub.tfs.henshin.editor.ui.dialog.ExtendedElementListSelectionDialog;

/**
 * The Class DialogUtil.
 */
public class DialogUtil {

	/**
	 * Run node creation dialog.
	 * 
	 * @param shell
	 *            the shell
	 * @param graph
	 *            the graph
	 * @return the e class
	 */
	public static EClass runNodeCreationDialog(Shell shell, Graph graph) {
		List<EClass> nodeTypes = new ArrayList<EClass>();
		TransformationSystem transSys = (TransformationSystem) graph
				.eResource().getContents().get(0);

		if (canCreateNode(shell, graph)) {
			nodeTypes = NodeTypes.getNodeTypes(graph,
					graph.eContainer() != transSys);
			switch (nodeTypes.size()) {
			case 0:
				return null;
			case 1:
				return nodeTypes.get(0);
			default:
				return new ExtendedElementListSelectionDialog<EClass>(shell,
						new LabelProvider() {
							@Override
							public String getText(Object element) {
								return ((EClass) element).getName();
							}

							@Override
							public Image getImage(Object element) {
								return IconUtil.getIcon("node18.png");
							}
						}, nodeTypes.toArray(new EClass[nodeTypes.size()]),
						"Node Type Selection",
						"Select a EClass for the new node type:").runSingle();
			}
		}
		return null;
	}

	/**
	 * Run attribute creation dialog.
	 * 
	 * @param shell
	 *            the shell
	 * @param node
	 *            the node
	 * @return the map
	 */
	public static Map<EAttribute, String> runAttributeCreationDialog(
			Shell shell, Node node) {
		AttributeTableDialog dialog = new AttributeTableDialog(shell, SWT.NULL,
				node, AttributeTypes.getFreeAttributeTypes(node));
		dialog.open();
		return dialog.getAssigment();
	}

	/**
	 * Run edge type selection dialog.
	 * 
	 * @param shell
	 *            the shell
	 * @param eReferences
	 *            the e references
	 * @return the e reference
	 */
	public static EReference runEdgeTypeSelectionDialog(Shell shell,
			List<EReference> eReferences) {
		switch (eReferences.size()) {
		case 0:
			return null;
		case 1:
			return eReferences.get(0);
		default:
			return new ExtendedElementListSelectionDialog<EReference>(shell,
					new LabelProvider() {
						@Override
						public String getText(Object element) {
							return ((EReference) element).getName();
						}

						@Override
						public Image getImage(Object element) {
							return IconUtil.getIcon("edge18.png");
						}
					}, eReferences.toArray(new EReference[eReferences.size()]),
					"Edge Type Selection",
					"Select a EReference for the new edge type:").runSingle();
		}
	}

	/**
	 * Run graph choice dialog.
	 * 
	 * @param shell
	 *            the shell
	 * @param graphs
	 *            the graphen
	 * @return the graph
	 */
	public static Graph runGraphChoiceDialog(Shell shell, List<Graph> graphs) {
		return new ExtendedElementListSelectionDialog<Graph>(shell,
				new LabelProvider() {
					@Override
					public String getText(Object element) {
						return ((Graph) element).getName();
					}

					@Override
					public Image getImage(Object element) {
						return IconUtil.getIcon("graph18.png");
					}
				}, graphs.toArray(new Graph[graphs.size()]), "Graph Selection",
				"Select a Graph for transformation:").runSingle();
	}

	/**
	 * Run rule choice dialog.
	 * 
	 * @param shell
	 *            the shell
	 * @param rules
	 *            the rules
	 * @return the rule
	 */
	public static Rule runRuleChoiceDialog(Shell shell, List<Rule> rules) {
		return new ExtendedElementListSelectionDialog<Rule>(shell,
				new LabelProvider() {
					@Override
					public String getText(Object element) {
						return ((Rule) element).getName();
					}

					@Override
					public Image getImage(Object element) {
						return ResourceUtil.ICONS.RULE.img(16);
					}
				}, rules.toArray(new Rule[rules.size()]), Messages.RULE_SELECTION,
				Messages.RULE_SELECTION_MSG).runSingle();
	}

	/**
	 * Run transformation unit choice dialog.
	 * 
	 * @param shell
	 *            the shell
	 * @param transUnits
	 *            the trans units
	 * @return the transformation unit
	 */
	public static TransformationUnit runTransformationUnitChoiceDialog(
			Shell shell, List<TransformationUnit> transUnits) {
		return new ExtendedElementListSelectionDialog<TransformationUnit>(
				shell, new LabelProvider() {
					@Override
					public String getText(Object element) {
						return ((TransformationUnit) element).getName();
					}

					@Override
					public Image getImage(Object element) {
						if (element instanceof Rule) {
							return IconUtil.getIcon("ruler16.png");
						}

						if (element instanceof ConditionalUnit) {
							return IconUtil.getIcon("conditionalUnit18.png");
						}
						if (element instanceof SequentialUnit) {
							return IconUtil.getIcon("seqUnit18.png");
						}
						if (element instanceof IndependentUnit) {
							return IconUtil.getIcon("independent16.png");
						}
						if (element instanceof PriorityUnit) {
							return IconUtil.getIcon("priority16.png");
						}

						return null;
					}
				},
				transUnits.toArray(new TransformationUnit[transUnits.size()]),
				"Transformation Unit Selection",
				"Select a Transformation Unit:").runSingle();
	}

	/**
	 * Can create node.
	 * 
	 * @param shell
	 *            the shell
	 * @param graph
	 *            the graph
	 * @return true, if successful
	 */
	private static boolean canCreateNode(Shell shell, Graph graph) {
		TransformationSystem transSys = (TransformationSystem) graph
				.eResource().getContents().get(0);

		// At least one ePackage must be imported
		if (transSys.getImports().isEmpty()) {
			MessageDialog.openError(shell, "Node Creation Error",
					"There are no model packages imported yet!");
			return false;
		}

		return true;
	}

	/**
	 * Run transformation unit choice for add unit dialog.
	 * 
	 * @param shell
	 *            the shell
	 * @param tUnit
	 *            the t unit
	 * @return the transformation unit
	 */
	public static TransformationUnit runTransformationUnitChoiceForAddUnitDialog(
			Shell shell, TransformationUnit tUnit) {
		TransformationSystem transSys = HenshinUtil.INSTANCE
				.getTransformationSystem(tUnit);

		if (transSys != null) {
			List<TransformationUnit> list = new Vector<TransformationUnit>();

			list.addAll(transSys.getRules());
			list.addAll(transSys.getTransformationUnits());

			if (!(tUnit instanceof ConditionalUnit || tUnit instanceof SequentialUnit)) {
				list.removeAll(tUnit.getSubUnits(true));
			}

			list.remove(tUnit);

			// forbids cyclic includes
			for (Iterator<TransformationUnit> it = list.iterator(); it
					.hasNext();) {
				if (it.next().getSubUnits(true).contains(tUnit)) {
					it.remove();
				}
			}

			return runTransformationUnitChoiceDialog(shell, list);
		}
		return null;
	}
}
