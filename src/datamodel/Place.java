package datamodel;

import java.util.ArrayList;

import util.IterableMap;

/**
 * Class that represents places in petri nets. Each place has a number of
 * tokens, an id and a name.
 */
public class Place extends PetrinetElement {

	private int numberOfTokens;

	private NumberOfTokensListener numberOfTokensListener;

	protected IterableMap<String, Transition> outputs = new IterableMap<String, Transition>();// set of places
																										// that serve as
																										// output
	protected IterableMap<String, Transition> inputs = new IterableMap<String, Transition>();// set of places
	// that represent inputs from transitions
	
	public Place(String id) {
		this.id = id;
	}

	/**
	 * Returns true if place has tokens.
	 * 
	 * @return number of tokens > 0.
	 */
	public boolean hasTokens() {
		return numberOfTokens > 0;
	}

	/**
	 * Returns the number of tokens currently at the place.
	 * 
	 * @return Number of tokens.
	 */
	public int getNumberOfTokens() {
		return numberOfTokens;
	}

	protected void setNumberOfTokens(int numberOfTokens) {

		boolean hadNoTokens = hasTokens() ? false : true;

		this.numberOfTokens = numberOfTokens;

		if (numberOfTokens == 0) {
			for (String s : outputs.keySet()) {
				Transition t = outputs.get(s);
				t.updateActivationStatus();
			}
		}

		if (hadNoTokens && numberOfTokens > 0) {
			for (String s : outputs.keySet()) {
				Transition t = outputs.get(s);
				t.updateActivationStatus();

			}
		}

		if (numberOfTokensListener != null)
			numberOfTokensListener.numberChanged(numberOfTokens);

	}

	public void setNumberOfTokensListener(NumberOfTokensListener numberOfTokensListener) {
		this.numberOfTokensListener = numberOfTokensListener;
	}

	/**
	 * Increments the number of tokens by 1.
	 */
	protected void incrementTokens() {
		setNumberOfTokens(numberOfTokens + 1);
	}

	/**
	 * Decrements the number of tokens by 1.
	 * 
	 * @throws OutOfTokensException Throws Exception when there are no tokens left.
	 */
	protected boolean decrementTokens() {

		if (numberOfTokens <= 0) {
			System.out.println("There are no tokens in place with ID \"" + id + "\"");
			return false;
		}

		setNumberOfTokens(numberOfTokens - 1);
		return true;
	}

	/**
	 * Adds a place to the set of output places (postset).
	 * 
	 * @param p {@link Place} to be added as an Output.
	 */
	protected void addOutput(Transition t) {
		outputs.put(t.id, t);
	}
	protected void addInput(Transition t) {
		inputs.put(t.id, t);
	}
	
	protected Iterable<Transition> getOutputs() {
		return outputs;
	}
	protected Iterable<Transition> getInputs() {
		return inputs;
	}
	public void removeOutput(Transition transition) {
		if (!outputs.containsKey(transition.getId()))
			return;
		outputs.remove(transition.getId());
		transition.removeInput(this);
	}
	
	public void removeInput(Transition transition) {
		if (!inputs.containsKey(transition.getId()))
			return;
		inputs.remove(transition.getId());
		transition.removeOutput(this);
	}

}
