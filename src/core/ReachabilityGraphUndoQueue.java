package core;

import listeners.ToolbarToggleListener;

/**
 * <p>
 * Class representing an undo queue for a reachabilty graph.
 * </p>
 */
public class ReachabilityGraphUndoQueue {

	/** The reachability graph. */
	private ReachabilityGraph reachabilityGraph;
	/** The listener for the toolbar buttons. */
	private ToolbarToggleListener toolbarToggleListener;
	/** The current state of the queue. */
	private ReachabilityGraphUndoQueueState currentState = null;

	/**
	 * Instantiates a new undo queue.
	 *
	 * @param reachabilityGraph     The reachability graph for which this queue is
	 *                              used.
	 * @param toolbarToggleListener The listener for the toolbar buttons.
	 */
	public ReachabilityGraphUndoQueue(ReachabilityGraph reachabilityGraph,
			ToolbarToggleListener toolbarToggleListener) {
		this.reachabilityGraph = reachabilityGraph;
		this.toolbarToggleListener = toolbarToggleListener;

	}

	/**
	 * Push a new petrinet state onto the queue.
	 * 
	 * @param state       The state to be added.
	 * @param currentEdge The edge being currently active.
	 * @param stateAdded  Information about whether components have been added.
	 * @param transition  The transition that has been fired.
	 * @param skippable   True if step can be skipped on un-/redo.
	 */
	public void push(PetrinetState state, String currentEdge, AddedType stateAdded, Transition transition,
			boolean skippable) {

		// create new state for queue with the current state as predecessor
		ReachabilityGraphUndoQueueState newState = new ReachabilityGraphUndoQueueState(currentState, state, currentEdge,
				stateAdded, transition, skippable);

		// toggle undo change button if it is the beginning of the queue
		if ((currentState == null || currentState.isFirst()) && toolbarToggleListener != null)
			toolbarToggleListener.onUndoChanged();

		// if the current state exists add new state as successor
		if (currentState != null) {
			// if the current state had a next one we need to toggle the redo button
			if (currentState.hasNext() && toolbarToggleListener != null)
				toolbarToggleListener.onRedoChanged();
			currentState.setNextState(newState);
		}

		// update current state
		currentState = newState;

	}

	// TODO on un-/redo edges not properly removed

	/**
	 * Go a step back in the queue.
	 */
	public void goBack() {

		// if we are at the beginning of the queue we can not go further
		if (currentState.isFirst())
			return;

		// do not push changes made to the reachability graph -> all steps already exist
		reachabilityGraph.setPushing(false);

		// if we are at the end of the queue we have to toggle the redo button
		if (!currentState.hasNext() && toolbarToggleListener != null)
			toolbarToggleListener.onRedoChanged();

		// UNDO CURRENT STEP
		// if state has been added, remove it
		if (currentState.getAdded() == AddedType.STATE)
			reachabilityGraph.removeState(currentState.getState());
		// if only edge has been added, remove it
		if (currentState.getAdded() == AddedType.EDGE)
			reachabilityGraph.removeEdge(currentState.getLast().getState(), currentState.getState(),
					currentState.getTransition());

		// change current state and set currently active components in reachability
		// graph
		currentState = currentState.getLast();
		reachabilityGraph.getPetrinet().setState(currentState.getState());
		reachabilityGraph.setCurrentState(currentState.getState());
		reachabilityGraph.setCurrentEdge(currentState.getEdge());

		// if we reached the beginning of the queue toggle undo button
		if (currentState.isFirst() && toolbarToggleListener != null)
			toolbarToggleListener.onUndoChanged();

		// if the current step is skippable we need to continue
		if (currentState.isSkippable())
			goBack();

		// reset reachability graph to push changes to the queue
		reachabilityGraph.setPushing(true);

	}

	/**
	 * Go a step forward in the queue.
	 */
	public boolean goForward() {

		// if there is no next step we can not go further.
		if (!currentState.hasNext())
			return false;

		// do not push changes made to the reachability graph -> all steps already exist
		reachabilityGraph.setPushing(false);

		// if we are at the beginning of the queue we have to toggle the undo button
		if (currentState.isFirst() && toolbarToggleListener != null)
			toolbarToggleListener.onUndoChanged();

		// update current state
		currentState = currentState.getNext();

		// first reset the petrinet (in order to redo change in step)
		Petrinet petrinet = reachabilityGraph.getPetrinet();
		petrinet.setState(currentState.getState());

		// if something has been added, readd state with transition, otherwise only set
		// reachability graph to given state
		if (currentState.stateAdded() != AddedType.NOTHING) {
			// since a new petrinet state is created it needs to be updated in the queue
			// state
			PetrinetState newPetrinetState = reachabilityGraph.addNewState(petrinet, currentState.getTransition());
			currentState.setPetrinetState(newPetrinetState);
		} else
			reachabilityGraph.setCurrentState(currentState.getState());

		// if we are at the end of the queue we need to toggle the redo button
		if (!currentState.hasNext() && toolbarToggleListener != null)
			toolbarToggleListener.onRedoChanged();

		// if the current step is skippable we need to continue
		if (currentState.isSkippable())
			goForward();

		// reset reachability graph to push changes to the queue
		reachabilityGraph.setPushing(true);

		return true;
	}

	public void reset() {
		currentState = null;
	}

	/**
	 * Rewind -> go to beginning of queue and undo everything.
	 */
	public void rewind() {

		while (!currentState.isFirst())
			goBack();
	}

	/**
	 * Rewind -> go to beginning of queue but without undoing all steps.
	 */
	public void rewindSilent() {

		while (!currentState.isFirst())
			currentState = currentState.getLast();

		reachabilityGraph.setPushing(false);

		reachabilityGraph.getPetrinet().setState(currentState.getState());
		reachabilityGraph.setCurrentState(currentState.getState());
		reachabilityGraph.setCurrentEdge(currentState.getEdge());

		reachabilityGraph.setPushing(true);

	}

	/**
	 * Get the current state of the queue
	 * 
	 * @return the current state
	 */
	public ReachabilityGraphUndoQueueState getCurrentState() {
		return currentState;
	}

}
