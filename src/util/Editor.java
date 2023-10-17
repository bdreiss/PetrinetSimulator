package util;

import javax.swing.JOptionPane;

import control.PetrinetController;
import datamodel.PetrinetElement;
import datamodel.Place;
import datamodel.Transition;

public class Editor {

	private PetrinetController controller;

	private PetrinetElement[] addEdge;
	private String edgeToAddId;
	private PetrinetElement[] removeEdge;

	private OnEditedListener onEditedListener;
	
	public Editor(PetrinetController controller){
		this.controller = controller;
	}

	
	public void setOnEditedListener(OnEditedListener onEditedListener) {
		this.onEditedListener = onEditedListener;
	}

	public boolean addPlace(String id) {


		if (controller.getPetrinet().getPlace(id) != null)
			return false;
		Place placeToAdd = new Place(id);
		controller.getPetrinet().addPlace(placeToAdd);
		controller.getPetrinet().setAddedElementPosition(placeToAdd);
		controller.setFileChanged(true);

		return true;
	}


	public boolean addTransition(String id) {

		if (controller.getPetrinet().getTransition(id) != null)
			return false;


		Transition transitionToAdd = new Transition(id);
		controller.getPetrinet().addTransition(transitionToAdd);
		controller.getPetrinet().setAddedElementPosition(transitionToAdd);
		controller.setFileChanged(true);

		return true;
	}


	public boolean toggleAddEdge(String id) {
		if (addsEdge()) {
			addEdge = null;
			return true;
		}

		if (controller.getPetrinet().hasEdgeWithId(id))
			return false;
		edgeToAddId = id;
		addEdge = new PetrinetElement[2];
		PetrinetElement markedNode = controller.getPetrinetGraph().getMarkedNode();
		if (markedNode != null)
			addEdge[0] = markedNode;


		if (removesEdge())
			removeEdge = null;
		return true;
	}

	public boolean toggleRemoveEdge() {
		if (removesEdge()) {
			removeEdge = null;
			return true;
		}

		removeEdge = new PetrinetElement[2];
		PetrinetElement markedNode = controller.getPetrinetGraph().getMarkedNode();
		if (markedNode != null)
			removeEdge[0] = markedNode;

		if (addsEdge())
			addEdge = null;
		return true;
	}

	public boolean addsEdge() {
		return addEdge != null;
	}

	public boolean removesEdge() {
		return removeEdge != null;
	}

	public void removeComponent() {

		PetrinetElement markedElement = controller.getPetrinetGraph().getMarkedNode();

		if (markedElement == null)
			return;

		controller.getPetrinet().removePetrinetElement(markedElement.getId());
		controller.setFileChanged(true);

	}

	private boolean edgeExists(PetrinetElement source, PetrinetElement target) {
		return controller.getPetrinet().getOriginalArcId(source.getId() + target.getId()) != null;

	}

	private boolean edgeIsValid() {
		if (addEdge[0] instanceof Place && addEdge[1] instanceof Place)
			return false;
		if (addEdge[0] instanceof Transition && addEdge[1] instanceof Transition)
			return false;
		return true;
	}

	private boolean addNewEdge() {
		boolean added = false;

		if (addEdge[0] instanceof Transition)
			added = controller.getPetrinet().addOutput((Place) addEdge[1], (Transition) addEdge[0], edgeToAddId);
		else
			added = controller.getPetrinet().addInput((Place) addEdge[0], (Transition) addEdge[1], edgeToAddId);

		addEdge = null;
		edgeToAddId = null;
		return added;
	}

	public void clickedNodeInGraph(PetrinetElement pe) {


		if (addsEdge()) {
			if (addEdge[0] == null)
				addEdge[0] = pe;
			else {
				if (addEdge[0] == pe)
					addEdge[0] = null;
				else {
					addEdge[1] = pe;
					if (edgeIsValid()) {
						boolean added = addNewEdge();
						if (added) {
							controller.getPetrinetGraph().toggleNodeMark(null);
							controller.setFileChanged(true);
							onEditedListener.onEdgeAdded();
							return;
						} else
							JOptionPane.showMessageDialog(null, "Invalid operation: edge already exists.", "",
									JOptionPane.INFORMATION_MESSAGE);

					} else {
						JOptionPane.showMessageDialog(null,
								"Invalid operation: edge has to go from place to transition or vice versa.", "",
								JOptionPane.INFORMATION_MESSAGE);
						return;
					}

				}
			}
			
		}
		
		if (removesEdge()) {
			if (removeEdge[0] == null)
				removeEdge[0] = pe;
			else {
				if (removeEdge[0] == pe)
					removeEdge[0] = null;
				else {
					removeEdge[1] = pe;
					if (edgeExists(removeEdge[0], removeEdge[1])) {
						controller.getPetrinet().removeEdge(removeEdge[0], removeEdge[1]);
						removeEdge = null;
						controller.getPetrinetGraph().toggleNodeMark(null);
						controller.setFileChanged(true);
						onEditedListener.onEdgeRemoved();
						return;
					} else {
						JOptionPane.showMessageDialog(null, "Invalid operation: edge does not exist.", "",
								JOptionPane.INFORMATION_MESSAGE);
						return;
					}

				}
			}

		}

		controller.getPetrinetGraph().toggleNodeMark(pe);

	}


}
