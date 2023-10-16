package view;

import java.awt.Color;

import javax.swing.JButton;

import control.MainController;
import util.Editor;

public class EditorToolbar extends AbstractToolbar {

	private static final long serialVersionUID = 1L;

	private Color buttonDefaultColor;

	private Color buttonHighlightColor = Color.LIGHT_GRAY;

	private JButton addPlaceButton;

	private JButton addTransitionButton;

	private JButton addEdgeButton;

	private JButton removeEdgeButton;

	public EditorToolbar(MainController controller) {

		// implement ZOOM

		JButton openButton = makeToolbarButton(ToolbarImage.OPEN, e -> controller.onOpen(), "Open file", "open");

		buttonDefaultColor = openButton.getBackground();

		JButton saveButton = makeToolbarButton(ToolbarImage.SAVE, e -> controller.onSave(), "Save file", "save");

		JButton previousButton = makeToolbarButton(ToolbarImage.LEFT, e -> controller.onPrevious(),
				"Open the previous file", "previous");

		JButton nextButton = makeToolbarButton(ToolbarImage.RIGHT, e -> controller.onNext(), "Open the next file",
				"next");

		addPlaceButton = makeToolbarButton(ToolbarImage.ADD_PLACE, e -> controller.onAddPlace(), "Add place",
				"add place");

		addTransitionButton = makeToolbarButton(ToolbarImage.ADD_TRANSITION, e -> controller.onAddTransition(),
				"Add transition", "add trans");

		JButton deleteComponentButton = makeToolbarButton(ToolbarImage.DELETE_COMPONENT,
				e -> controller.onRemoveComponent(), "Delete component", "delete");

		addEdgeButton = makeToolbarButton(ToolbarImage.ADD_EDGE, e -> controller.onAddEdge(), "Add edge", "add edge");

		removeEdgeButton = makeToolbarButton(ToolbarImage.REMOVE_EDGE, e -> controller.onRemoveEdge(),
				"Remove an edge: choose source first and then target", "remove edge");

		JButton plusButton = makeToolbarButton(ToolbarImage.PLUS, e -> controller.onPlus(),
				"Adds a token to a selected place", "plus");

		JButton minusButton = makeToolbarButton(ToolbarImage.MINUS, e -> controller.onMinus(),
				"Removes a token from a selected place", "minus");

		JButton setDefaultButton = makeToolbarButton(ToolbarImage.DEFAULT, e -> controller.onSetDefault(),
				"Reset split panes", "reset pane");

		JButton closeEditorButton = makeToolbarButton(ToolbarImage.OPEN_VIEWER, e -> controller.onCloseEditor(),
				"Switch to viewer", "close editor");

		this.add(openButton);
		this.add(saveButton);
		this.add(previousButton);
		this.add(nextButton);
		this.add(addPlaceButton);
		this.add(addTransitionButton);
		this.add(deleteComponentButton);
		this.add(addEdgeButton);
		this.add(removeEdgeButton);
		this.add(plusButton);
		this.add(minusButton);
		this.add(setDefaultButton);
		this.add(closeEditorButton);
	}

	public void toggleAddPlaceButton() {
		if (addPlaceButton.getBackground() == buttonDefaultColor) {
			addPlaceButton.setBackground(buttonHighlightColor);

			if (addTransitionButton.getBackground() == buttonHighlightColor)
				addTransitionButton.setBackground(buttonDefaultColor);
			if (addEdgeButton.getBackground() == buttonHighlightColor)
				addEdgeButton.setBackground(buttonDefaultColor);
			if (removeEdgeButton.getBackground() == buttonHighlightColor)
				removeEdgeButton.setBackground(buttonDefaultColor);

		} else
			addPlaceButton.setBackground(buttonDefaultColor);
	}

	public void toggleAddTransitionButton() {
		if (addTransitionButton.getBackground() == buttonDefaultColor) {
			addTransitionButton.setBackground(buttonHighlightColor);
			if (addPlaceButton.getBackground() == buttonHighlightColor)
				addPlaceButton.setBackground(buttonDefaultColor);
			if (addEdgeButton.getBackground() == buttonHighlightColor)
				addEdgeButton.setBackground(buttonDefaultColor);
			if (removeEdgeButton.getBackground() == buttonHighlightColor)
				removeEdgeButton.setBackground(buttonDefaultColor);

		} else
			addTransitionButton.setBackground(buttonDefaultColor);
	}

	public void toggleAddEdgeButton() {
		if (addEdgeButton.getBackground() == buttonDefaultColor) {
			addEdgeButton.setBackground(buttonHighlightColor);
			if (addPlaceButton.getBackground() == buttonHighlightColor)
				addPlaceButton.setBackground(buttonDefaultColor);
			if (addTransitionButton.getBackground() == buttonHighlightColor)
				addTransitionButton.setBackground(buttonDefaultColor);
			if (removeEdgeButton.getBackground() == buttonHighlightColor)
				removeEdgeButton.setBackground(buttonDefaultColor);

		} else
			addEdgeButton.setBackground(buttonDefaultColor);
	}

	public void toggleRemoveEdgeButton() {
		if (removeEdgeButton.getBackground() == buttonDefaultColor) {
			removeEdgeButton.setBackground(buttonHighlightColor);
			if (addPlaceButton.getBackground() == buttonHighlightColor)
				addPlaceButton.setBackground(buttonDefaultColor);
			if (addTransitionButton.getBackground() == buttonHighlightColor)
				addTransitionButton.setBackground(buttonDefaultColor);
			if (addEdgeButton.getBackground() == buttonHighlightColor)
				addEdgeButton.setBackground(buttonDefaultColor);
		}else
			removeEdgeButton.setBackground(buttonDefaultColor);
	}
	
	public void resetButtons() {
		addPlaceButton.setBackground(buttonDefaultColor);
		addTransitionButton.setBackground(buttonDefaultColor);
		addEdgeButton.setBackground(buttonDefaultColor);
		removeEdgeButton.setBackground(buttonDefaultColor);
	}
	
	public void setToolbarTo(Editor editor) {
		resetButtons();
		
		if (editor.addsPlace())
			toggleAddPlaceButton();
		if (editor.addsTransition())
			toggleAddTransitionButton();
		if (editor.addsEdge())
			toggleAddEdgeButton();
		if (editor.removesEdge())
			toggleRemoveEdgeButton();
	}
	
}
