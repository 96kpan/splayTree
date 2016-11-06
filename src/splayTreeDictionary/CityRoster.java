package splayTreeDictionary;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class CityRoster {

    /** Print an error message and exit. */
    public static void exit(String message) {
        System.err.println("Error: " + message);
        System.exit(1);
    }

    /** Driver state machine for the roster dictionary. */
    public static void main(String[] argv)
        throws java.io.FileNotFoundException, java.io.IOException
    {
        // Dictionary
        SplayTree roster = new SplayTree();

        // Read from standard input
        BufferedReader br = new BufferedReader(
                                new InputStreamReader(System.in));

        // State machine states
        final int    KEYWORD = 0, XINS = 1, YINS = 2, COLOR = 3,
                    XQRY = 4, YQRY = 5, XDEL = 6, YDEL = 7;
        int state = KEYWORD, xcoord = 0, ycoord = 0;

        // Loop through the lines of input, and divide each line at whitespace:
        for (String line = br.readLine(); line != null; line = br.readLine())
            for (StringTokenizer tz = new StringTokenizer(line);
                    tz.hasMoreTokens(); ) {

                // Next coordinates to be handled
                final String token = tz.nextToken();

                if (KEYWORD == state) {
                    if (0 == token.compareTo("insert"))
                        state = XINS;
                    else if (0 == token.compareTo("query"))
                        state = XQRY;
                    else if (0 == token.compareTo("delete"))
                        state = XDEL;
                    else if (0 == token.compareTo("debug_print")) // optional
                        System.out.println("" + roster);
                    else
                        exit("bad command " + token);
                } else if (XINS == state || XQRY == state || XDEL == state) {
                    xcoord = Integer.parseInt(token);
                    state += 1;
                } else if (YINS == state) {
                    ycoord = Integer.parseInt(token);
                    state = COLOR;
                } else if (COLOR == state) {
                    state = KEYWORD;
                    assert token.length() > 0;
                    roster.insert_record(new Point(xcoord, ycoord), token);
                } else if (YQRY == state) {
                    state = KEYWORD;
                    ycoord = Integer.parseInt(token);
                    String color = roster.lookup(new Point(xcoord, ycoord));
                    System.out.println("query: "
                        + (null == color ? "not found" : color));
                } else if (YDEL == state) {
                    state = KEYWORD;
                    ycoord = Integer.parseInt(token);
                    String color = roster.delete(new Point(xcoord, ycoord));
                    System.out.println("delete: "
                        + (null == color ? "not found" : color));
                } else
                    exit("bad state " + state);
            }
    }
}