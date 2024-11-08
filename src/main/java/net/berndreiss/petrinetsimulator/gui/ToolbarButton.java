package net.berndreiss.petrinetsimulator.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;

/**
 * <p>
 * A button used for the {@link PetrinetToolbar}.
 * </p>
 * <p>
 * It holds a representative image, an action listener, a tool tip text and an
 * alt text. Additionally it keeps track of its color and repaints itself since
 * in certain look and feels the setBackground method does not work.
 * </p>
 */
class ToolbarButton extends JButton {

	private static final long serialVersionUID = 1L;

	/** The text shown if the image does not work. */
	private String altText;
	/** The color for the button. */
	private Color color = null;

	/**
	 * Instantiate a new instance of the button.
	 * 
	 * @param toolbarImage   the image the button will show
	 * @param actionListener the action the button performs when clicked
	 * @param toolTipText    the tool tip to show for the button
	 * @param altText        the text shown if the image does not work
	 */
	ToolbarButton(ToolbarImage toolbarImage, ActionListener actionListener, String toolTipText, String altText) {

		this.altText = altText;
		setImage(toolbarImage);
		addActionListener(actionListener);
		setToolTipText(toolTipText);

		int sizeInt = 30;
		Dimension size = new Dimension(sizeInt, sizeInt);
		setMaximumSize(size);
		setSize(size);
		setPreferredSize(size);

	}

	@Override
	protected void paintComponent(Graphics g) {

		// artificially set the background for certain look and feels
		if (color != null) {
			((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setColor(color);
			g.fillRoundRect(0, 0, 30, 30, 20, 20);
		}
		super.paintComponent(g);
	}

	// set the button to the image provided
	private void setImage(ToolbarImage toolbarImage) {
		URL iconUrl = getClass().getResource(toolbarImage.imagePath());
		if (iconUrl != null) {
			ImageIcon icon = new ImageIcon(iconUrl, altText);
			setIcon(icon);
		} else {
			setText(altText);
			System.err.println("Resource not found: " + toolbarImage.imagePath());
		}

	}

	/**
	 * 
	 * Sets the background color of the button. Since setBackground does not work in
	 * certain look and feels this method provides an alternative way to set the
	 * background.
	 * 
	 * @param color color for the button
	 */
	public void setColor(Color color) {
		LookAndFeel laf = UIManager.getLookAndFeel();
		this.color = color;

		// if look and feel is Nimbus artificially set the background in the paint
		// method
		if (laf.getName().equals("Nimbus"))
			repaint();
		else
			setBackground(color);
	}

	/**
	 * Gets the color the button has been set to.
	 * 
	 * @return the color set for the button
	 */
	public Color getColor() {
		return color;
	}

}