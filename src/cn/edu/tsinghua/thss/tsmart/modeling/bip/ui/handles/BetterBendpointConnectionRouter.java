package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.handles;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.Bendpoint;
import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.PrecisionPoint;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.PlaceModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.PlaceEditPart.PlaceFigure;

@SuppressWarnings({"rawtypes", "unchecked"})
public class BetterBendpointConnectionRouter extends BendpointConnectionRouter {
    private static final PrecisionPoint A_POINT = new PrecisionPoint();

    @Override
    public void route(Connection conn) {
        if (conn.getSourceAnchor().getReferencePoint()
                        .equals(conn.getTargetAnchor().getReferencePoint())) {
            PointList points = conn.getPoints();
            points.removeAllPoints();
            ArrayList<Bendpoint> bendpoints = (ArrayList<Bendpoint>) getConstraint(conn);
            if (bendpoints == null) {
                bendpoints = new ArrayList<Bendpoint>();
            }
            if (bendpoints.isEmpty()) {
                try {
                    PlaceModel model = ((PlaceFigure) conn.getSourceAnchor().getOwner()).getModel();
                    A_POINT.setLocation(model.getPositionConstraint().getCenter());
                } catch (Exception ex) {
                    A_POINT.setLocation(conn.getSourceAnchor().getReferencePoint());
                }
                bezier(A_POINT.getCopy(), new Point(A_POINT.x - 40, A_POINT.y + 40), new Point(
                                A_POINT.x + 40, A_POINT.y + 40), A_POINT.getCopy(), bendpoints, 3);
            }
        }
        super.route(conn);
    }

    private void bezier(Point p0, Point p1, Point p2, Point p3, List<Bendpoint> bendpoints,
                    int count) {
        for (double x, y, k = 1.0 / count, t = k; t < 1; t += k) {
            double a = t;
            double b = 1 - t;
            x = b * b * b * p0.x + 3 * b * b * a * p1.x + 3 * b * a * a * p2.x + a * a * a * p3.x;
            y = b * b * b * p0.y + 3 * b * b * a * p1.y + 3 * b * a * a * p2.y + a * a * a * p3.y;
            bendpoints.add(new AbsoluteBendpoint((int) x, (int) y));
        }
    }
}
