package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.util.EnumSet;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

import org.graphstream.graph.Graph;
import org.graphstream.ui.graphicGraph.GraphicElement;
import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.swing_viewer.ViewPanel;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerPipe;
import org.graphstream.ui.view.util.InteractiveElement;

import control.MainController;
import control.PetrinetController;
import core.Petrinet;
import core.PetrinetElement;
import core.Place;
import core.Transition;
import exceptions.PetrinetException;
import reachabilityGraphLayout.LayoutType;

// TODO: Auto-generated Javadoc
/**
 * A {@link JPanel} representing a {@link Petrinet} loaded from a file holding a
 * horizontal {@link ResizableSplitPane} containing a {@link PetrinetGraph} on
 * the left and a {@link ReachabilityGraph} on the right. Creates and holds a
 * {@link PetrinetController} managing all interactions with the data model. The
 * graphs are implemented using the
 * <a href="https://graphstream-project.org/">GraphStream</a> library.
 */
public class PetrinetPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private ToolbarMode toolbarMode = ToolbarMode.VIEWER;

	// split pane holding petrinet and reachability graph
	private ResizableSplitPane graphSplitPane;

	private PetrinetGraph petrinetGraph;
	private ReachabilityGraph reachabilityGraph;

	// panels for the petrinet and reachability graph -> if the view panel for the
	// GraphStream graph is not put inside another panel the layout is unstable
	private JPanel petrinetPanel;
	private JPanel reachabilityPanel;

	// view panels holding the viewer for the GraphStream graph
	private ViewPanel petrinetViewPanel;
	private ViewPanel reachabilityViewPanel;

	// controller managing the interaction with the data model
	private PetrinetController controller;
	private MainController mainController;

	private PetrinetGraphEditor editor;

	/**
	 * Instantiates a new petrinet panel.
	 *
	 * @param mainController The controller serving as an interface to the main GUI
	 *                       components.
	 * @param file           The pnml file from which contents should be loaded.
	 * @param layoutType     The layout type used.
	 * @throws PetrinetException If there is a problem reading a file an exception
	 *                           is thrown.
	 */
	public PetrinetPanel(MainController mainController, File file, LayoutType layoutType) throws PetrinetException {

		this.controller = new PetrinetController(file);
		this.mainController = mainController;

		this.petrinetGraph = new PetrinetGraph(controller.getPetrinet());

		this.editor = new PetrinetGraphEditor(this);

		this.setLayout(new BorderLayout());

		// linking the petrinet controller to the toolbar via the main controller ->
		// needed for toggling the layout, un-/redo and editor buttons
		controller.setToolbarToggleListener(mainController);

		controller.initializePetrinetQueue();

		petrinetPanel = new JPanel();
		petrinetPanel.setLayout(new BorderLayout());

		// get view panel for petrinet and add it to the JPanel
		this.petrinetViewPanel = initGraphStreamView(petrinetGraph, petrinetPanel);
		petrinetPanel.add(petrinetViewPanel, BorderLayout.CENTER);

		reachabilityPanel = new JPanel();
		reachabilityPanel.setLayout(new BorderLayout());

		setSplitPane();

		setReachabilityPanel(layoutType);

		adjustArrowHeads();// replays the graph and adjustes auto layout -> otherwise it does not render
							// properly
	}

	private void setReachabilityPanel(LayoutType layoutType) {

		if (reachabilityPanel.getComponentCount() != 0)
			reachabilityPanel.remove(reachabilityViewPanel);

		reachabilityGraph = new ReachabilityGraph(controller.getReachabilityGraphModel(), layoutType);

		reachabilityViewPanel = initGraphStreamView(reachabilityGraph, reachabilityPanel);
		reachabilityPanel.add(reachabilityViewPanel, BorderLayout.CENTER);

		graphSplitPane.revalidate();
		graphSplitPane.repaint();

	}

	/**
	 * Gets the graph split pane.
	 *
	 * @return the graph split pane
	 */
	public ResizableSplitPane getGraphSplitPane() {
		return graphSplitPane;
	}

	/**
	 * Zoom in petrinet.
	 */
	public void zoomInPetrinet() {
		zoomIn(petrinetViewPanel);
	}

	/**
	 * Zoom out petrinet.
	 */
	public void zoomOutPetrinet() {
		zoomOut(petrinetViewPanel);
	}

	/**
	 * Zoom in reachability.
	 */
	public void zoomInReachability() {
		if (reachabilityGraph.hasLessThanTwoNodes())// disable zoom if there's only one node in graph,
													// since there are problems with nodes disappearing
													// and a zoom on one node does not make any
													// difference
			return;
		zoomIn(reachabilityViewPanel);
	}

	/**
	 * Zoom out reachability.
	 */
	public void zoomOutReachability() {
		if (reachabilityGraph.hasLessThanTwoNodes())// disable zoom if there's only one node in graph,
													// since there are problems with nodes disappearing
													// and a zoom on one node does not make any
													// difference
			return;
		zoomOut(reachabilityViewPanel);
	}

	/**
	 * 
	 * @return
	 */

	public void resetReachabilityZoom() {

		double zoom = reachabilityViewPanel.getCamera().getViewPercent();

		if (zoom != 1.0)
			reachabilityViewPanel.getCamera().resetView();
	}

	private void zoomIn(ViewPanel viewPanel) {

		if (viewPanel == null)
			return;

		double zoom = viewPanel.getCamera().getViewPercent();

		if (zoom > 0.1) {
			viewPanel.getCamera().setViewPercent(zoom - 0.1);
			adjustArrowHeads();
		}
	}

	private void zoomOut(ViewPanel viewPanel) {
		if (viewPanel == null)
			return;

		double zoom = viewPanel.getCamera().getViewPercent();

		viewPanel.getCamera().setViewPercent(zoom + 0.1);
		adjustArrowHeads();
	}

	/**
	 * Sets the layout type.
	 *
	 * @param layoutType the new layout type
	 */
	public void setLayoutType(LayoutType layoutType) {

		if (layoutType == LayoutType.AUTOMATIC || reachabilityGraph.getLayoutType() == LayoutType.AUTOMATIC) {
			setReachabilityPanel(layoutType);

		} else {
			reachabilityGraph.setLayoutType(layoutType);
		}
	}

	/**
	 * Gets the controller.
	 *
	 * @return the controller
	 */
	public PetrinetController getController() {
		return controller;
	}

	/**
	 * Gets the editor.
	 *
	 * @return the editor
	 */
	public PetrinetGraphEditor getEditor() {
		return editor;
	}

	/**
	 * Node marked.
	 *
	 * @return true, if successful
	 */
	public boolean nodeMarked() {
		return petrinetGraph.getMarkedNode() != null;
	}

	/**
	 * Gets the toolbar mode.
	 *
	 * @return the toolbar mode
	 */
	public ToolbarMode getToolbarMode() {

		return toolbarMode;
	}

	/**
	 * Sets the toolbar mode.
	 *
	 * @param toolbarMode the new toolbar mode
	 */
	public void setToolbarMode(ToolbarMode toolbarMode) {
		if (toolbarMode == ToolbarMode.EDITOR)
			controller.resetReachabilityGraph();
		if (toolbarMode == ToolbarMode.VIEWER) {
			PetrinetElement toggledElement = petrinetGraph.getMarkedNode();
			if (toggledElement != null && toggledElement instanceof Transition)
				petrinetGraph.toggleNodeMark(toggledElement);
		}
		this.toolbarMode = toolbarMode;
	}

	private ViewPanel initGraphStreamView(Graph graph, Component parent) {

		// Erzeuge Viewer mit passendem Threading-Model für Zusammenspiel mit
		// Swing
		SwingViewer viewer = new SwingViewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);

		// bessere Darstellungsqualität und Antialiasing (Kantenglättung) aktivieren
		// HINWEIS: Damit diese Attribute eine Auswirkung haben, müssen sie NACH
		// Erzeugung des SwingViewer gesetzt werden
		graph.setAttribute("ui.quality");
		graph.setAttribute("ui.antialias");

		if (graph instanceof ReachabilityGraph && ((ReachabilityGraph) graph).getLayoutType() == LayoutType.AUTOMATIC)
			viewer.enableAutoLayout();
		else
			viewer.disableAutoLayout();
		// Auto-Layout aktivieren: GraphStream generiert ein möglichst
		// übersichtliches Layout
		// (und ignoriert hinzugefügte Koordinaten)
		// viewer.enableAutoLayout();

		// Eine DefaultView zum Viewer hinzufügen, die jedoch nicht automatisch
		// in einen JFrame integriert werden soll (daher Parameter "false"). Das
		// zurückgelieferte ViewPanel ist eine Unterklasse von JPanel, so dass
		// es später einfach in unsere Swing-GUI integriert werden kann. Es gilt
		// folgende Vererbungshierarchie:
		// DefaultView extends ViewPanel extends JPanel implements View
		// Hinweis:
		// In den Tutorials wird "View" als Rückgabetyp angegeben, es ist
		// aber ein "ViewPanel" (und somit auch ein JPanel).
		ViewPanel viewPanel = (ViewPanel) viewer.addDefaultView(false);

		// Neue ViewerPipe erzeugen, um über Ereignisse des Viewer informiert
		// werden zu können
		ViewerPipe viewerPipe = viewer.newViewerPipe();

		if (parent != null) {
			parent.addComponentListener(new ComponentAdapter() {

				@Override
				public void componentResized(ComponentEvent e) {

//					if (graph instanceof ReachabilityGraph
//							&& ((ReachabilityGraph) graph).getLayoutType() == LayoutType.AUTOMATIC)
//						return;

					viewer.replayGraph(graph);

				}
			});
		}

		if (graph instanceof ReachabilityGraph)
			((ReachabilityGraph) graph).setReplayGraphListener(() -> adjustArrowHeads());

		EnumSet<InteractiveElement> enumSet = EnumSet.of(InteractiveElement.NODE);

		viewPanel.addMouseListener(new MouseAdapter() {

			private GraphicElement element;

			@Override
			public void mousePressed(MouseEvent me) {
				element = viewPanel.findGraphicElementAt(enumSet, me.getX(), me.getY());
				viewerPipe.pump();

			}

			@Override
			public void mouseReleased(MouseEvent me) {

				if (element != null) {

					String id = element.getId();

					if (graph instanceof ReachabilityGraph) {
						controller.onReachabilityGraphNodeClicked(id);
					} else {

						Petrinet petrinet = controller.getPetrinet();
						PetrinetElement p = petrinet.getPetrinetElement(id);
						double x = element.getX();
						double y = element.getY();
						if (p != null)
							if (p.getX() != x || p.getY() != y) {

								controller.onPetrinetNodeDragged(id, x, y);
							} else {

								if (graph instanceof PetrinetGraph) {
									PetrinetElement pe = petrinet.getPetrinetElement(id);

									if (toolbarMode == ToolbarMode.VIEWER) {
										if (pe instanceof Transition)
											controller.onTransitionClicked(id);
										if (pe instanceof Place)
											petrinetGraph.toggleNodeMark(pe);
									}
									if (toolbarMode == ToolbarMode.EDITOR)
										editor.clickedNodeInGraph(pe);
								}
							}
						element = null;
					}
				}
				viewerPipe.pump();
			}
		});
		// Zoom per Mausrad ermöglichen
		viewPanel.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				double zoomLevel = viewPanel.getCamera().getViewPercent();
				if (e.getWheelRotation() == -1) {
					zoomLevel -= 0.1;
					if (zoomLevel < 0.1) {
						zoomLevel = 0.1;
					}
				}
				if (e.getWheelRotation() == 1) {
					zoomLevel += 0.1;
				}
				viewPanel.getCamera().setViewPercent(zoomLevel);
			}
		});

		return viewPanel;

	}

	// for some reason replayGraph() does only work in resetting the arrow heads by
	// resizing the frame and invoking the method via the ComponentListener
	private void adjustArrowHeads() {

		// wait a moment for GraphStream to do its thing -> otherwise frame might resize
		// too early and the arrow heads do not align
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		JFrame parent = mainController.getFrame();

		Dimension currentSize = parent.getSize();
		parent.setSize(currentSize.width + 1, currentSize.height);
		parent.setSize(currentSize);

	}

	/**
	 * 
	 * @return
	 */

	public PetrinetGraph getPetrinetGraph() {
		return petrinetGraph;
	}

	/**
	 * 
	 * @return
	 */

	public boolean incrementMarkedPlace() {
		return controller.incrementPlace(petrinetGraph.getMarkedNode());
	}

	/**
	 * 
	 * @return
	 */

	public boolean decrementMarkedPlace() {
		return controller.decrementPlace(petrinetGraph.getMarkedNode());
	}

	/**
	 * 
	 * @param label
	 */

	public void setLabel(String label) {

		controller.setLabel(label, petrinetGraph.getMarkedNode());

	}

	/**
	 * 
	 */
	public void setSplitPane() {
		
		Double oldDividerRatio = null;
		if (graphSplitPane != null) {
			remove(graphSplitPane);
			oldDividerRatio = graphSplitPane.getDividerRatio();
		}
		
		graphSplitPane = new ResizableSplitPane(mainController.getFrame(), JSplitPane.HORIZONTAL_SPLIT, petrinetPanel,
				reachabilityPanel);

		add(graphSplitPane, BorderLayout.CENTER);
		
		if (oldDividerRatio != null)
			graphSplitPane.setDividerRatio(oldDividerRatio);
	}

}
