package de.tub.tfs.henshin.editor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.henshin.model.HenshinFactory;
import org.eclipse.emf.henshin.model.HenshinPackage;
import org.eclipse.emf.henshin.model.Node;
import org.eclipse.emf.henshin.model.TransformationSystem;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.ui.actions.DeleteAction;
import org.eclipse.gef.ui.parts.SelectionSynchronizer;
import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;

import de.tub.tfs.henshin.editor.actions.EditPartRetargetAction;
import de.tub.tfs.henshin.editor.actions.HenshinCopyAction;
import de.tub.tfs.henshin.editor.actions.HenshinCutAction;
import de.tub.tfs.henshin.editor.actions.HenshinPasteAction;
import de.tub.tfs.henshin.editor.actions.IHandlersRegistry;
import de.tub.tfs.henshin.editor.actions.condition.CreateAndAction;
import de.tub.tfs.henshin.editor.actions.condition.CreateApplicationConditionAction;
import de.tub.tfs.henshin.editor.actions.condition.CreateConditionAction;
import de.tub.tfs.henshin.editor.actions.condition.CreateNotAction;
import de.tub.tfs.henshin.editor.actions.condition.CreateOrAction;
import de.tub.tfs.henshin.editor.actions.condition.SetNegatedAction;
import de.tub.tfs.henshin.editor.actions.condition.SwapBinaryFormulaAction;
import de.tub.tfs.henshin.editor.actions.flow_diagram.ClearActivityContentAction;
import de.tub.tfs.henshin.editor.actions.flow_diagram.CreateFlowDiagramAction;
import de.tub.tfs.henshin.editor.actions.flow_diagram.ExecuteFlowDiagramAction;
import de.tub.tfs.henshin.editor.actions.flow_diagram.SetActivityContentAction;
import de.tub.tfs.henshin.editor.actions.flow_diagram.SortFlowDiagramsAction;
import de.tub.tfs.henshin.editor.actions.flow_diagram.UnNestActivityAction;
import de.tub.tfs.henshin.editor.actions.flow_diagram.ValidateFlowDiagramAction;
import de.tub.tfs.henshin.editor.actions.flow_diagram.ValidateParameterMappingsAction;
import de.tub.tfs.henshin.editor.actions.graph.CreateAttributeAction;
import de.tub.tfs.henshin.editor.actions.graph.CreateEdgeAction;
import de.tub.tfs.henshin.editor.actions.graph.CreateGraphAction;
import de.tub.tfs.henshin.editor.actions.graph.CreateNodeAction;
import de.tub.tfs.henshin.editor.actions.graph.FilterMetaModelAction;
import de.tub.tfs.henshin.editor.actions.graph.ValidateGraphAction;
import de.tub.tfs.henshin.editor.actions.rule.AddMultiRuleAction;
import de.tub.tfs.henshin.editor.actions.rule.CreateAttributeConditionAction;
import de.tub.tfs.henshin.editor.actions.rule.CreateRuleAction;
import de.tub.tfs.henshin.editor.actions.rule.DeleteMappingAction;
import de.tub.tfs.henshin.editor.actions.rule.ExecuteRuleAction;
import de.tub.tfs.henshin.editor.actions.rule.ValidateRuleAction;
import de.tub.tfs.henshin.editor.actions.transSys.DeleteEPackageAction;
import de.tub.tfs.henshin.editor.actions.transSys.ExportInstanceModelAction;
import de.tub.tfs.henshin.editor.actions.transSys.HenshinTreeContextMenuProvider;
import de.tub.tfs.henshin.editor.actions.transSys.ImportEcoreModelAction;
import de.tub.tfs.henshin.editor.actions.transSys.ImportInstanceModelAction;
import de.tub.tfs.henshin.editor.actions.transSys.SortGraphsAction;
import de.tub.tfs.henshin.editor.actions.transSys.SortRulesAction;
import de.tub.tfs.henshin.editor.actions.transformation_unit.AddTransformationUnitAction;
import de.tub.tfs.henshin.editor.actions.transformation_unit.CreateConditionalUnitAction;
import de.tub.tfs.henshin.editor.actions.transformation_unit.CreateIndependentUnitAction;
import de.tub.tfs.henshin.editor.actions.transformation_unit.CreateLoopUnitAction;
import de.tub.tfs.henshin.editor.actions.transformation_unit.CreateParameterAction;
import de.tub.tfs.henshin.editor.actions.transformation_unit.CreatePriorityUnitAction;
import de.tub.tfs.henshin.editor.actions.transformation_unit.CreateSequentialUnitAction;
import de.tub.tfs.henshin.editor.actions.transformation_unit.ExecuteTransformationUnitAction;
import de.tub.tfs.henshin.editor.actions.transformation_unit.MoveDownTransformationUnitAction;
import de.tub.tfs.henshin.editor.actions.transformation_unit.MoveUpTransformationUnitAction;
import de.tub.tfs.henshin.editor.editparts.HenshinTreeEditPartFactory;
import de.tub.tfs.henshin.editor.ui.condition.ConditionView;
import de.tub.tfs.henshin.editor.ui.flow_diagram.FlowDiagramView;
import de.tub.tfs.henshin.editor.ui.graph.GraphView;
import de.tub.tfs.henshin.editor.ui.rule.RuleView;
import de.tub.tfs.henshin.editor.ui.transformation_unit.TransUnitView;
import de.tub.tfs.henshin.model.flowcontrol.FlowControlFactory;
import de.tub.tfs.henshin.model.flowcontrol.FlowControlPackage;
import de.tub.tfs.henshin.model.flowcontrol.FlowControlSystem;
import de.tub.tfs.henshin.model.layout.HenshinLayoutFactory;
import de.tub.tfs.henshin.model.layout.Layout;
import de.tub.tfs.henshin.model.layout.LayoutSystem;
import de.tub.tfs.henshin.model.layout.NodeLayout;
import de.tub.tfs.muvitor.ui.ContextMenuProviderWithActionRegistry;
import de.tub.tfs.muvitor.ui.MuvitorActivator;
import de.tub.tfs.muvitor.ui.MuvitorTreeEditor;
import de.tub.tfs.muvitor.ui.utils.EMFModelManager;

/**
 * The Class HenshinTreeEditor.
 */
public class HenshinTreeEditor extends MuvitorTreeEditor implements
		IHandlersRegistry {

	private final String layoutExtension="henshinlayout";
	private final String flowDiagExtension="flowcontrol";

	private IPath layoutFilePath;
	private IPath flowDiagFilePath;
	
	private final EMFModelManager layoutModelManager = new EMFModelManager(
			layoutExtension);
	private final EMFModelManager flowDiagModelManager = new EMFModelManager(
			layoutExtension);

	private LayoutSystem layoutSystem;
	private FlowControlSystem flowControlSystem;

	
	// statically registers views
	static {
		registerViewID(HenshinPackage.Literals.GRAPH, GraphView.ID);
		registerViewID(HenshinPackage.Literals.RULE, RuleView.ID);

		registerViewID(HenshinPackage.Literals.NESTED_CONDITION,
				ConditionView.ID);
		registerViewID(HenshinPackage.Literals.NOT, ConditionView.ID);
		registerViewID(HenshinPackage.Literals.AND, ConditionView.ID);
		registerViewID(HenshinPackage.Literals.OR, ConditionView.ID);

		registerViewID(HenshinPackage.Literals.SEQUENTIAL_UNIT,
				TransUnitView.ID);
		registerViewID(HenshinPackage.Literals.INDEPENDENT_UNIT,
				TransUnitView.ID);
		registerViewID(HenshinPackage.Literals.PRIORITY_UNIT, TransUnitView.ID);
		registerViewID(HenshinPackage.Literals.CONDITIONAL_UNIT,
				TransUnitView.ID);
		registerViewID(HenshinPackage.Literals.LOOP_UNIT, TransUnitView.ID);

		registerViewID(FlowControlPackage.Literals.FLOW_DIAGRAM,
				FlowDiagramView.ID);

	}

	private Map<Class<?>, Map<String, String>> handlers = new HashMap<Class<?>, Map<String, String>>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * muvitorkit.ui.MuvitorTreeEditor#createContextMenuProvider(org.eclipse
	 * .gef.ui.parts.TreeViewer)
	 */
	@Override
	protected ContextMenuProviderWithActionRegistry createContextMenuProvider(
			TreeViewer viewer) {
		return new HenshinTreeContextMenuProvider(viewer, getActionRegistry());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see muvitorkit.ui.MuvitorTreeEditor#createCustomActions()
	 */
	@Override
	protected void createCustomActions() {
		registerAction(new CreateGraphAction(this));
		registerAction(new CreateRuleAction(this));
		registerAction(new CreateNodeAction(this));
		registerAction(new ExportInstanceModelAction(this));
		registerAction(new SortRulesAction(this));
		registerAction(new SortGraphsAction(this));
		registerAction(new CreateEdgeAction(this));
		registerAction(new DeleteMappingAction(this));

		registerAction(new CreateConditionAction(this));
		registerAction(new CreateApplicationConditionAction(this));
		registerAction(new CreateNotAction(this));
		registerAction(new CreateAndAction(this));
		registerAction(new CreateOrAction(this));
		registerAction(new SwapBinaryFormulaAction(this));
		registerAction(new SetNegatedAction(this));

		registerAction(new HenshinCopyAction(this));
		registerAction(new HenshinPasteAction(this));
		registerAction(new HenshinCutAction(this));

		registerAction(new ImportEcoreModelAction(this));
		registerAction(new ImportInstanceModelAction(this));
		registerAction(new ExecuteRuleAction(this));
		registerAction(new AddMultiRuleAction(this));

		registerAction(new ExecuteTransformationUnitAction(this));
		registerAction(new ValidateRuleAction(this));
		registerAction(new ValidateGraphAction(this));
		registerAction(new CreateAttributeAction(this));
		registerAction(new CreateSequentialUnitAction(this));
		registerAction(new CreateIndependentUnitAction(this));
		registerAction(new CreatePriorityUnitAction(this));
		registerAction(new CreateConditionalUnitAction(this));
		registerAction(new CreateLoopUnitAction(this));

		registerAction(new AddTransformationUnitAction(this));
		registerAction(new MoveUpTransformationUnitAction(this));
		registerAction(new MoveDownTransformationUnitAction(this));
		registerAction(new CreateParameterAction(this));
		registerAction(new CreateAttributeConditionAction(this));

		registerAction(new CreateFlowDiagramAction(this));
		registerAction(new SetActivityContentAction(this));

		registerHandler(new DeleteEPackageAction(this), EPackage.class,
				ActionFactory.DELETE.getId());

		EditPartRetargetAction deleteAction = new EditPartRetargetAction(this);

		deleteAction.setId(ActionFactory.DELETE.getId());
		deleteAction.setDefaultHandler(new DeleteAction(getWorkbenchPart()));

		registerAction(deleteAction);

		registerAction(new DeleteEPackageAction(this));

		registerAction(new UnNestActivityAction(this));
		registerAction(new ExecuteFlowDiagramAction(this));
		registerAction(new ValidateFlowDiagramAction(this));
		registerAction(new ClearActivityContentAction(this));
		registerAction(new SortFlowDiagramsAction(this));
		registerAction(new ValidateParameterMappingsAction(this));
		
		registerAction(new FilterMetaModelAction(this));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.tub.tfs.muvitor.ui.MuvitorTreeEditor#init(org.eclipse.ui.IEditorSite,
	 * org.eclipse.ui.IEditorInput)
	 */
	@Override
	public void init(IEditorSite site, IEditorInput input) {
		// TODO Auto-generated method stub
		super.init(site, input);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see muvitorkit.ui.MuvitorTreeEditor#createDefaultModel()
	 */
	@Override
	protected EObject createDefaultModel() {
		// empty, since we are now having multiple model roots.
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tub.tfs.muvitor.ui.MuvitorTreeEditor#createDefaultModels()
	 */
	@Override
	protected List<EObject> createDefaultModels() {
		List<EObject> defaultModels = new ArrayList<EObject>(4);

		TransformationSystem trafoSystem = HenshinFactory.eINSTANCE
				.createTransformationSystem();
		FlowControlSystem flowControlSystem = FlowControlFactory.eINSTANCE
				.createFlowControlSystem();

		LayoutSystem layoutSystem = HenshinLayoutFactory.eINSTANCE
				.createLayoutSystem();

		trafoSystem.setName("Transformation System");

		defaultModels.add(trafoSystem);
		defaultModels.add(flowControlSystem);
		defaultModels.add(layoutSystem);

		return Collections.unmodifiableList(defaultModels);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see muvitorkit.ui.MuvitorTreeEditor#createTreeEditPartFactory()
	 */
	@Override
	protected EditPartFactory createTreeEditPartFactory() {
		return new HenshinTreeEditPartFactory();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * muvitorkit.ui.MuvitorTreeEditor#setupKeyHandler(org.eclipse.gef.KeyHandler
	 * )
	 */
	@Override
	protected void setupKeyHandler(KeyHandler kh) {

	}

	/**
	 * Convenient method to get a correctly typed model root.
	 * 
	 * @param type
	 *            the type of the returned model root
	 * @return the correctly typed model root or <code>null</code>, if not
	 *         found.
	 */
	@SuppressWarnings("unchecked")
	public synchronized <T extends EObject> T getModelRoot(Class<T> type) {
		for (EObject modelRoot : getModelRoots()) {
			if (type.isInstance(modelRoot)) {
				return (T) modelRoot;
			}
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tub.tfs.muvitor.ui.MuvitorTreeEditor#getAdapter(java.lang.Class)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class type) {
		if (type == SelectionSynchronizer.class) {
			return null;
		}

		return super.getAdapter(type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.tub.tfs.henshin.editor.actions.IHandlersRegistry#getHandler(java.lang
	 * .String)
	 */
	@Override
	public IAction getHandler(String id, Class<?> target) {
		Map<String, String> targetHandlers = handlers.get(target);

		if (targetHandlers == null) {
			for (Class<?> c : target.getInterfaces()) {
				targetHandlers = handlers.get(c);

				if (targetHandlers != null) {
					break;
				}
			}
		}

		if (targetHandlers != null) {
			return getActionRegistry().getAction(targetHandlers.get(id));
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.tub.tfs.henshin.editor.actions.IHandlersRegistry#getWorkbenchPart()
	 */
	@Override
	public IWorkbenchPart getWorkbenchPart() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.tub.tfs.henshin.editor.actions.IHandlersRegistry#registerHandler(java
	 * .lang.String)
	 */
	@Override
	public void registerHandler(IAction handler) {
		registerAction(handler);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.tub.tfs.henshin.editor.actions.IHandlersRegistry#registerHandler(org
	 * .eclipse.jface.action.IAction, java.lang.Class)
	 */
	@Override
	public void registerHandler(IAction handler, Class<?> target, String id) {
		registerAction(handler);

		if (!handlers.containsKey(target)) {
			handlers.put(target, new HashMap<String, String>());
		}

		handlers.get(target).put(id, handler.getId());
	}
	
	/* (non-Javadoc)
	 * @see muvitorkit.ui.MuvitorTreeEditor#setInput(org.eclipse.ui.IEditorInput)
	 */
	@Override
	protected void setInput(IEditorInput input) {
		super.setInput(input);

		// open layout model
		final IFile file = ((IFileEditorInput) input).getFile();
		layoutFilePath = file.getFullPath().removeFileExtension().addFileExtension(layoutExtension);

		List<EObject> list=new ArrayList<EObject>();
		list = layoutModelManager.load(layoutFilePath, list);

		if (list == null || list.isEmpty() || !(list.get(0) instanceof LayoutSystem)){
			layoutSystem = HenshinLayoutFactory.eINSTANCE.createLayoutSystem();
			list.add(layoutSystem);
		} else {
			layoutSystem = (LayoutSystem) list.get(0);
		}
		
		
		flowDiagFilePath = file.getFullPath().removeFileExtension().addFileExtension(flowDiagExtension);

		list = flowDiagModelManager.load(flowDiagFilePath, list);

		if (list == null || list.isEmpty() || list.size() < 2 || !(list.get(1) instanceof FlowControlSystem)){
			flowControlSystem = FlowControlFactory.eINSTANCE.createFlowControlSystem();
			list.add(flowControlSystem);
		} else {
			flowControlSystem = (FlowControlSystem) list.get(1);
		}
		
		this.getModelRoots().add(1, layoutSystem);
		this.getModelRoots().add(2, flowControlSystem);
	}

	


	/* (non-Javadoc)
	 * @see muvitorkit.ui.MuvitorTreeEditor#save(org.eclipse.core.resources.IFile, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected void save(IFile file, IProgressMonitor monitor)
			throws CoreException {
		Iterator<Layout> iter=layoutSystem.getLayouts().iterator();
		while(iter.hasNext()){
			Layout tmp = iter.next();
			if (!(tmp instanceof NodeLayout))
				continue;
			NodeLayout layout=(NodeLayout) tmp;
			if (layout.getModel()==null){
				iter.remove();
				continue;
			}
			if (((Node)layout.getModel()).getGraph()==null){
				iter.remove();
				continue;
			}
		}
		super.save(file, monitor);
		monitor.beginTask("Saving " + file, 2);
		// save model to file
		try {
			layoutFilePath = file.getFullPath().removeFileExtension().addFileExtension(layoutExtension);
			layoutModelManager.save(layoutFilePath);
			monitor.worked(1);
			file.refreshLocal(IResource.DEPTH_ZERO, new SubProgressMonitor(
					monitor, 1));
			monitor.done();
		} catch (final FileNotFoundException e) {
			MuvitorActivator.logError("Error writing file.", e);
		} catch (final IOException e) {
			MuvitorActivator.logError("Error writing file.", e);
		}

	}

	
}
