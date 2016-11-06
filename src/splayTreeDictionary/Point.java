package splayTreeDictionary;

/**
 * Project 4 starter file:
 * Dictionary of records with key of Point, satellite data of String.
 * Dictionary is implemented as a splay tree.
 */
import java.io.BufferedReader;
import java.util.StringTokenizer;
import java.io.InputStreamReader;


/**
 * Point defined by integer Cartesian coordinates.
 *
 * This just stores the two endpoints as plain int coordinates,
 * and as public attributes -- not encapsulated at all.
 */
public class Point implements Comparable<Point> {

    /** Cartesian coordinates are just plain public attributes. */
    public int x, y;

    /** Ctor for a point, given its x, y coordinates. */
    public Point(int xx, int yy) {
        x = xx;
        y = yy;
    }

    /** Copy of a point. */
    public Point(Point p) {
        this(p.x, p.y);
    }

    /** Comparison is lexicographical: vertical first, horizontal second. */
    public int compareTo(Point p) {
        final int dy = p.y - y;
        return 0 == dy ? p.x - x : dy;
    }

    /** Stringify in a very plain comma-separated format. */
    public String toString() {
        return "" + x + ", " + y;
    }
}





