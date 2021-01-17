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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class FastCollinearPoints {

    private final List<LineSegment> segments;
    private final Point[] pointsNo;
    private final Point[] pointsSo;

    public FastCollinearPoints(Point[] pointsArr) {
        notNull(pointsArr);
        notNullArrayElements(pointsArr);
        segments = new ArrayList<>();
        pointsNo = Arrays.copyOf(pointsArr, pointsArr.length);
        pointsSo = Arrays.copyOf(pointsArr, pointsArr.length);
        haveNoDuplicates(pointsNo);
        Arrays.sort(pointsNo);

        for (int i = 0; i < pointsNo.length; i++) {
            Point p1 = pointsNo[i];

            Arrays.sort(pointsSo, p1.slopeOrder());
            Double recentSlope = null;
            int count = 1;

            List<Point> line = null;

            for (int j = 0; j < pointsSo.length; j++) {

                Point pj = pointsSo[j];
                // point itself, should be 0-iteration?
                if (pj.compareTo(p1) == 0) {
                    continue;
                }
                //
                double slope = p1.slopeTo(pj);
                if (recentSlope == null) {
                    // first iteration, 2-nd point
                    recentSlope = slope;
                    count++;
                }
                else if (recentSlope == slope) {
                    // 3+ point, line detected
                    count++;
                    Point pointPrePj = pointsSo[j - 1];
                    if (count == 3) {
                        line = new LinkedList<>();
                        line.add(p1);
                        line.add(pointPrePj);
                    }
                    line.add(pj);
                }
                else {
                    // brake the line
                    recentSlope = slope;
                    count = 2;
                    line = null;
                }

                if (line != null && line.size() >= 4) {
                    Collections.sort(line);
                    if (line.get(0).compareTo(p1) == 0) {
                        LineSegment lineSegment = new LineSegment(line.get(0),
                                                                  line.get(line.size() - 1));
                        segments.add(lineSegment);
                    }
                }
            }

        }
    }

    public int numberOfSegments() {
        return segments.size();
    }

    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[segments.size()]);
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

    private boolean slopesEqual(double slope1, double slope2) {
        if (slope1 == Double.POSITIVE_INFINITY && slope2 == Double.POSITIVE_INFINITY) {
            return true;
        }
        return Math.abs(slope1 - slope2) < 0.001;
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
        FastCollinearPoints fastCollinearPoints = new FastCollinearPoints(points);
        for (LineSegment segment : fastCollinearPoints.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();

    }

}
