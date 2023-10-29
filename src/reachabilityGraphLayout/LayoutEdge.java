package reachabilityGraphLayout;

import org.graphstream.ui.spriteManager.Sprite;

// TODO: Auto-generated Javadoc
/**
 * The Class LayoutEdge.
 */
class LayoutEdge extends GraphicalObject {
	
	/** The source. */
	LayoutNode source;
	
	/** The target. */
	LayoutNode target;

	/** The sprite. */
	Sprite sprite;

	/**
	 * Instantiates a new layout edge.
	 *
	 * @param source the source
	 * @param target the target
	 * @param sprite the sprite
	 */
	LayoutEdge(LayoutNode source, LayoutNode target, Sprite sprite) {
		this.source = source;
		this.target = target;
		this.sprite = sprite;
	}

	/**
	 * Gets the sprite.
	 *
	 * @return the sprite
	 */
	public Sprite getSprite() {
		return sprite;
	}
	
	/**
	 * Left lower corner.
	 *
	 * @return the layout point
	 */
	@Override
	public LayoutPoint leftLowerCorner() {

		return new LayoutPoint(getX() - Layout.SPRITE_SIZE.getWidth() / 2, getY() - Layout.SPRITE_SIZE.getHeight() / 2);
	}

	/**
	 * Left upper corner.
	 *
	 * @return the layout point
	 */
	@Override
	public LayoutPoint leftUpperCorner() {
		return new LayoutPoint(getX() - Layout.SPRITE_SIZE.getWidth() / 2, getY() + Layout.SPRITE_SIZE.getHeight() / 2);
	}

	/**
	 * Right lower corner.
	 *
	 * @return the layout point
	 */
	@Override
	public LayoutPoint rightLowerCorner() {
		return new LayoutPoint(getX() + Layout.SPRITE_SIZE.getWidth() / 2, getY() - Layout.SPRITE_SIZE.getHeight() / 2);
	}

	/**
	 * Right upper corner.
	 *
	 * @return the layout point
	 */
	@Override
	public LayoutPoint rightUpperCorner() {
		return new LayoutPoint(getX() + Layout.SPRITE_SIZE.getWidth() / 2, getY() + Layout.SPRITE_SIZE.getHeight() / 2);
	}

	/**
	 * Gets the x.
	 *
	 * @return the x
	 */
	@Override
	double getX() {
		return source.getX() + (target.getX() - source.getX()) * sprite.getX();
	}

	/**
	 * Gets the y.
	 *
	 * @return the y
	 */
	@Override
	double getY() {
		return source.getY() + (target.getY() - source.getY()) * sprite.getY();
	}

	/**
	 * Length.
	 *
	 * @return the double
	 */
	double length() {// returns the squareroot of (x_2-x_1)^2 + (y_2-y_1)^2
		return Math.sqrt(Math.pow((target.getX() - source.getX()), 2) + Math.pow((target.getY() - source.getY()), 2));
	}

}
