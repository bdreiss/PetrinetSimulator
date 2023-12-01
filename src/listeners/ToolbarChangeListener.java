package listeners;

/**
 * <p>
 * The listener interface for receiving events that need to set certain
 * buttons of the toolbar.
 * </p>
 *
 * <p>
 * These buttons are adding/removing edges and the un-/redo buttons.
 * </p>
 */
public interface ToolbarChangeListener {

	/**
	 * An edge has been added and the add edge button needs to be reset.
	 */
	void onEdgeAdded();

	/**
	 * An edge has been removed and the remove edge button needs to be reset.
	 */
	void onEdgeRemoved();

	/**
	 * The state of being able to redo steps has changed and the button needs to be
	 * set.
	 * 
	 * @param highlight True, if button should be highlighted.
	 */
	void onSetRedoButton(boolean highlight);

	/**
	 * The state of being able to undo steps has changed and the button needs to be
	 * set.
	 * 
	 * @param highlight True, if button should be highlighted.
	 */
	void onSetUndoButton(boolean highlight);

	/**
	 * Reset the buttons for un-/redo -> remove highlight.
	 */
	void resetUndoRedoButtons();
}
