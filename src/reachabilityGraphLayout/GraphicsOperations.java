package reachabilityGraphLayout;

import org.graphstream.algorithm.Toolkit;
import org.graphstream.graph.Node;

// TODO: Auto-generated Javadoc
/**
 * The Class GraphicsOperations.
 */
class GraphicsOperations {
	
	/**
	 * Graphical objects intersect.
	 *
	 * @param go1 the go 1
	 * @param go2 the go 2
	 * @return true, if successful
	 */
	static boolean graphicalObjectsIntersect(GraphicalObject go1, GraphicalObject go2) {

		if (pointIsInsideGraphicalObject(go1, go2.leftLowerCorner()))
			return true;

		if (pointIsInsideGraphicalObject(go1, go2.leftUpperCorner()))
			return true;

		if (pointIsInsideGraphicalObject(go1, go2.rightLowerCorner()))
			return true;

		if (pointIsInsideGraphicalObject(go1, go2.rightUpperCorner()))
			return true;

		return false;
	}

	/**
	 * Point is inside graphical object.
	 *
	 * @param go the go
	 * @param p the p
	 * @return true, if successful
	 */
	private static boolean pointIsInsideGraphicalObject(GraphicalObject go, LayoutPoint p) {
		if (go == null || p == null)
			return false;

		if (p.y <= go.leftUpperCorner().y && p.y >= go.leftLowerCorner().y && p.x <= go.rightLowerCorner().x
				&& p.x >= go.leftLowerCorner().x)
			return true;

		return false;
	}

	/**
	 * Edge intersects graphical object.
	 *
	 * @param layoutEdge the layout edge
	 * @param go the go
	 * @return true, if successful
	 */
	static boolean edgeIntersectsGraphicalObject(LayoutEdge layoutEdge, GraphicalObject go) {

		if (go == null || layoutEdge == null)
			return false;

		Node source = layoutEdge.source.node;
		Node target = layoutEdge.target.node;

		double[] sourcePosition = Toolkit.nodePosition(source);
		double[] targetPosition = Toolkit.nodePosition(target);

		LayoutPoint a = new LayoutPoint(sourcePosition[0], sourcePosition[1]);
		LayoutPoint b = new LayoutPoint(targetPosition[0], targetPosition[1]);

		LayoutLine edge = new LayoutLine(a, b);

		LayoutPoint intersectionPoint;

		intersectionPoint = findIntersection(edge, go.leftSide());

		if (intersectionPoint != null && intersectionPoint.x >= Math.min(edge.a.x, edge.b.x)
				&& intersectionPoint.x <= Math.max(edge.a.x, edge.b.x)
				&& pointIsInsideGraphicalObject(go, intersectionPoint))
			return true;

		intersectionPoint = findIntersection(edge, go.rightSide());

		if (intersectionPoint != null && intersectionPoint.x >= Math.min(edge.a.x, edge.b.x)
				&& intersectionPoint.x <= Math.max(edge.a.x, edge.b.x)
				&& pointIsInsideGraphicalObject(go, intersectionPoint))
			return true;

		intersectionPoint = findIntersection(edge, go.lowerSide());

		if (intersectionPoint != null && intersectionPoint.y >= Math.min(edge.a.y, edge.b.y)
				&& intersectionPoint.y <= Math.max(edge.a.y, edge.b.y)
				&& pointIsInsideGraphicalObject(go, intersectionPoint))
			return true;

		intersectionPoint = findIntersection(edge, go.upperSide());
		if (intersectionPoint != null && intersectionPoint.y >= Math.min(edge.a.y, edge.b.y)
				&& intersectionPoint.y <= Math.max(edge.a.y, edge.b.y)
				&& pointIsInsideGraphicalObject(go, intersectionPoint))
			return true;

		return false;

	}

	/**
	 * Find intersection.
	 *
	 * @param l1 the l 1
	 * @param l2 the l 2
	 * @return the layout point
	 */
	private static LayoutPoint findIntersection(LayoutLine l1, LayoutLine l2) {

		LayoutPoint p1 = l1.a;
		LayoutPoint p2 = l1.b;
		LayoutPoint p3 = l2.a;
		LayoutPoint p4 = l2.b;

		Double m1, m2, c1, c2;

		// Calculate the slopes, handling vertical lines
		if (p2.x - p1.x == 0) {
			m1 = null;
		} else {
			m1 = (p2.y - p1.y) / (p2.x - p1.x);
		}

		if (p4.x - p3.x == 0) {
			m2 = null;
		} else {
			m2 = (p4.y - p3.y) / (p4.x - p3.x);
		}

		// If both lines are vertical
		if (m1 == null && m2 == null) {
			return null; // The lines overlap
		}
		// If only one line is vertical
		if (m1 == null) {
			c2 = p3.y - m2 * p3.x;
			return new LayoutPoint(p1.x, m2 * p1.x + c2);
		}
		if (m2 == null) {
			c1 = p1.y - m1 * p1.x;
			return new LayoutPoint(p3.x, m1 * p3.x + c1);
		}

		c1 = p1.y - m1 * p1.x;
		c2 = p3.y - m2 * p3.x;

		// If lines are parallel
		if (m1.equals(m2)) {
			return null;
		}

		double xIntersection = (c2 - c1) / (m1 - m2);
		double yIntersection = m1 * xIntersection + c1;

		return new LayoutPoint(xIntersection, yIntersection);
	}
}