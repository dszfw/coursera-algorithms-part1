/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * finds all line segments containing 4 points
 */
public class BruteCollinearPoints {

    private final List<LineSegment> segments;
    private final Point[] points;

    public BruteCollinearPoints(Point[] pointsArr) {
        notNull(pointsArr);
        notNullArrayElements(pointsArr);
        segments = new ArrayList<>();
        points = Arrays.copyOf(pointsArr, pointsArr.length);
        Arrays.sort(points);
        haveNoDuplicates(points);

        for (int i = 0; i < points.length; i++) {
            Point p1 = points[i];
            for (int j = i + 1; j < points.length; j++) {
                Point p2 = points[j];
                double slope = p1.slopeTo(p2);
                for (int k = j + 1; k < points.length; k++) {
                    boolean brakeThirdLoop = false;
                    for (int m = k + 1; m < points.length; m++) {
                        Point p3 = points[k];
                        Point p4 = points[m];
                        if (p1.slopeTo(p3) == slope && p1.slopeTo(p4) == slope) {
                            segments.add(new LineSegment(p1, p4));
                            brakeThirdLoop = true;
                            break;
                        }
                    }
                    if (brakeThirdLoop) {
                        break;
                    }
                }
            }
        }
    }

    /**
     * @return the number of line segments
     */
    public int numberOfSegments() {
        return segments.size();
    }

    /**
     * @return the line segments
     */
    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[segments.size()]);
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints bcp = new BruteCollinearPoints(points);
        for (LineSegment segment : bcp.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

    private void haveNoDuplicates(Point[] sortedPoints) {
        for (int i = 0; i < sortedPoints.length - 1; i++) {
            if (sortedPoints[i].compareTo(sortedPoints[i + 1]) == 0)
                throw new IllegalArgumentException();
        }
    }

    private void notNullArrayElements(Point[] pointsArr) {
        for (Point point : pointsArr) {
            if (point == null) {
                throw new IllegalArgumentException();
            }
        }
    }

    private void notNull(Object obj) {
        if (obj == null) throw new IllegalArgumentException();
    }

}
