package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.handles;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.FanRouter;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.PrecisionPoint;

public class BetterFanRouter extends FanRouter {
    public static final PrecisionPoint A_POINT = new PrecisionPoint();
    private static final int           COUNT   = 50;

    public void route(Connection conn) {
        super.route(conn);
        PointList routes = conn.getPoints().getCopy();
        PointList points = conn.getPoints();
        points.removeAllPoints();
        if (routes.size() <= 2) {
            conn.setPoints(routes);
            return;
        }
        if (routes.size() == 3) {
            bezier(routes.getPoint(0), routes.getPoint(1), routes.getPoint(1), routes.getPoint(2),
                            points, COUNT);
        }
        conn.setPoints(points);
    }

    private void bezier(Point p0, Point p1, Point p2, Point p3, PointList points, int count) {
        for (double x, y, k = 1.0 / count, t = k; t < 1; t += k) {
            double a = t;
            double b = 1 - t;
            x = b * b * b * p0.x + 3 * b * b * a * p1.x + 3 * b * a * a * p2.x + a * a * a * p3.x;
            y = b * b * b * p0.y + 3 * b * b * a * p1.y + 3 * b * a * a * p2.y + a * a * a * p3.y;
            points.addPoint(new PrecisionPoint(x, y));
        }
    }
}
