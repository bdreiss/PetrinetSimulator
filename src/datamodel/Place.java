package datamodel;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that represents places in petri nets. Each place has a number of tokens, an id and a name.
 */
public class Place extends PetrinetElement {

	private int numberOfTokens;
		
	private Map<String, Place> inputs = new HashMap<String, Place>();//set of places that serve as input
	private Map<String, Place> outputs = new HashMap<String, Place>();//set of places that serve as output

	public Place(String id, String name, int initialTokens) {
		this.id = id;
		this.name = name;
		this.numberOfTokens = initialTokens;
	}
	
	public Place(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public Place(String id) {
		this.id = id;
	}

	
	/**
	 * Returns true if place has tokens.
	 * @return number of tokens > 0.
	 */
	public boolean hasTokens() {
		return numberOfTokens > 0;
	}
	/**
	 * Returns the number of tokens currently at the place.
	 * @return Number of tokens.
	 */
	public int numberOfTokens() {
		return numberOfTokens;
	}
	
	public void setNumberOfTokens(int numberOfTokens) {
		this.numberOfTokens = numberOfTokens;
	}
	
	/**
	 * Increments the number of tokens by 1.
	 */
	public void incrementTokens() {
		numberOfTokens++;
	}
	
	/**
	 * Decrements the number of tokens by 1.
	 * @throws OutOfTokensException Throws Exception when there are no tokens left.
	 */
	public void decrementTokens() throws OutOfTokensException {
		
		if (numberOfTokens <= 0)
			throw new OutOfTokensException("There are no tokens in place with ID \"" + id + "\"");
				
		numberOfTokens--;
	}

	/**
	 * Adds a place to the set of input places (preset).
	 * @param p {@link Place} to be added as an Input.
	 */
	public void addInput(Transition t) {
		inputs.put(t.id, t);
		t.outputs.put(this.id, this);
	}

	/**
	 * Adds a place to the set of output places (postset).
	 * @param p {@link Place} to be added as an Output.
	 */
	public void addOutput(Transition t) {
		outputs.put(t.id,t);
		t.inputs.put(this.id, this);
	}

	public Map<String, Transition> getInputs(){
		return inputs;
	}
	
	public Map<String, Transition> getOutputs(){
		return outputs;
	}
}

