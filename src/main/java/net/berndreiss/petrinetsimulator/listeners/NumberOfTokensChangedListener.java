package net.berndreiss.petrinetsimulator.listeners;

/**
 * <p>
 * The listener interface for receiving events signifying the number of tokens
 * for a place has changed.
 * </p>
 * 
 * <p>
 * Informs the {@link PetrinetComponentChangedListener} and the
 * {@link PetrinetStateChangedListener} that the number of tokens has changed
 * for a place.
 * </p>
 *
 */
public interface NumberOfTokensChangedListener {

	/**
	 * On number of tokens changed inform the
	 * {@link PetrinetComponentChangedListener} and the
	 * {@link PetrinetStateChangedListener}.
	 *
	 * @param newNumber the new number of tokens
	 */
	void onNumberChanged(int newNumber);
}
