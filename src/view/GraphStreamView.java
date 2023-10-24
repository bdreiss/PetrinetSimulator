package view;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.EnumSet;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.graphstream.graph.Graph;
import org.graphstream.ui.graphicGraph.GraphicElement;
import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.swing_viewer.ViewPanel;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;
import org.graphstream.ui.view.util.InteractiveElement;

import control.PetrinetController;
import datamodel.Petrinet;
import datamodel.PetrinetElement;

public class GraphStreamView {
	
	private static JFrame mainFrame;
	
	public static ViewPanel initGraphStreamView(Graph graph, PetrinetController controller, JFrame parent) {
		
		mainFrame = parent;
		
		// Erzeuge Viewer mit passendem Threading-Model für Zusammenspiel mit
		// Swing
		SwingViewer viewer = new SwingViewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);

		// bessere Darstellungsqualität und Antialiasing (Kantenglättung) aktivieren
		// HINWEIS: Damit diese Attribute eine Auswirkung haben, müssen sie NACH
		// Erzeugung des SwingViewer gesetzt werden
		graph.setAttribute("ui.quality");
		graph.setAttribute("ui.antialias");

		// Das Auto-Layout für den Graphen kann aktiviert oder deaktiviert
		// werden.
		// Auto-Layout deaktivieren: Die explizit hinzugefügten Koordinaten
		// werden genutzt (wie in DemoGraph).
		// Achtung: Falls keine Koordinaten definiert wurden, liegen alle Knoten
		// übereinander.)
//		if (graph instanceof PetrinetGraph)
		viewer.disableAutoLayout();
//		else
//			viewer.enableAutoLayout();
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

		
		parent.addComponentListener(new ComponentAdapter() {
			
			double originalZoom = 1.0;
			
			@Override
			public void componentResized(ComponentEvent e) {
				
				viewer.replayGraph(graph);
				
				
				//graph not scaling correctly when having zoomed in or out before
				if (graph instanceof ReachabilityGraph) {
					
					double zoom = viewPanel.getCamera().getViewPercent();
										
					if (zoom == 1.0)
						return;
						
//					if (zoomAdjusted) {
//						zoomAdjusted = false;
//						System.out.println("HERE");
//						return;
//			
//					
//					}
					
					
					viewPanel.getCamera().resetView();
//					viewPanel.getCamera().setViewPercent(zoom);
//					viewPanel.getCamera().setViewPercent(zoom);
//					System.out.println(zoom);
//					zoomAdjusted = true;
//					counter++;
//					
//					if (counter == 2)
//						System.exit(0);
					//AAAAND reset arrow heads again
					adjustArrowHeads();
//					((ReachabilityGraph) graph).
				}

			}
			
		});

		if (graph instanceof ReachabilityGraph)
			((ReachabilityGraph) graph).setReplayGraphListener(() -> {

				adjustArrowHeads();
			});

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
						controller.reachabilityNodeClicked(id);
					} else {

						Petrinet petrinet = controller.getPetrinet(); 
						PetrinetElement p = petrinet.getPetrinetElement(id);
						double x = element.getX();
						double y = element.getY();
						if (p != null)
							if (p.getX() != x || p.getY() != y) {
								petrinet.setCoordinates(id, element.getX(), element.getY());
							} else {

								if (graph instanceof PetrinetGraph)
									controller.clickNodeInGraph(id);
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


	
	//for some reason replayGraph() does only work by resizing the window

	public static void adjustArrowHeads() {
		Dimension currentSize = mainFrame.getSize();
		mainFrame.setSize(currentSize.width + 1, currentSize.height);
		mainFrame.setSize(currentSize);
	}
	
}
