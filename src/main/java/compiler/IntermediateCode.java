package compiler;

import java.util.ArrayList;
import java.util.Stack;

/* General Scheme:
* We can define the "translation" of each AST's node to IR as soon as we have code for it's children.
* Each child's result is in a temporary variable. We generate quads that combine the results.
* The end-result is a sequence of quads, where order matters. */

public class IntermediateCode {
    /* we are working with quads */
    public static class Quad {
        private String label;
        private String op;
        private String x;
        private String y;
        private String z;

        /* Quad's class (default-)constructor */
        public Quad() {}

        /* Quad's class constructors */
        public Quad(String label, String op, String x, String y, String z) {
            this.label = label;
            this.op = op;
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Quad(String op, String x, String y, String z) {
            this.label = null;
            this.op = op;
            this.x = x;
            this.y = y;
            this.z = z;
        }

        /* Quad's class copy-costructor */
        public Quad(Quad temp) {
            this.label = temp.label;
            this.op = temp.op;
            this.x = temp.x;
            this.y = temp.y;
            this.z = temp.z;
        }

        /* Quad's class setters and getters*/
        public void setLabel(String label) { this.label = label; }
        public void setOp(String op) { this.op = op; }
        public void setX(String x) { this.x = x; }
        public void setY(String y) { this.y = y; }
        public void setZ(String z) { this.z = z; }
        public String getLabel() { return this.label; }
        public String getOp() { return this.op; }
        public String getX() { return this.x; }
        public String getY() { return this.y; }
        public String getZ() { return this.z; }

        /* Quad's class printing function */
        public void printQuad() {
            System.out.printf("%s: %s, %s, %s, %s\n", this.label, this.op, this.x, this.y, this.z);
        }

    }
    /* we will use a low-level intermediate code (intermediate representation - IR) */
    private ArrayList<Quad> intermediateCode;
    /* we use a Java Array-List to store our used label names */
    private ArrayList<String> usedLabelNames;
    private Integer numOfLabel;
    /* we use a Java Array-List to store our used temporary names */
    private ArrayList<String> usedTempNames;
    private Integer numOfTemp;
    /* a Java Array-List of quad labels that contain jumps in the next command */
    private ArrayList<Integer> NEXT;
    /* a Java Array-List of quad labels that contain jumps that should be executed in case a command is True */
    private ArrayList<Integer> TRUE;
    /* a Java Array-List of quad labels that contain jumps that should be executed in case a command is True */
    private ArrayList<Integer> FALSE;

    /* IntermediateCode's class constructor */
    public IntermediateCode() {
        this.intermediateCode = new ArrayList<Quad>();
        this.usedLabelNames = new ArrayList<String>();
        this.numOfLabel = 0;
        this.usedTempNames = new ArrayList<String>();
        this.numOfTemp = 0;
        this.NEXT = new ArrayList<Integer>();
        this.TRUE = new ArrayList<Integer>();
        this.FALSE = new ArrayList<Integer>();
    }

    /* our Intermediate Code / Intermediate Representation printing function */
    public void printIR() {
        System.out.printf("\n\t\tLow Level Intermediate Representation:\n");
        for (int i = 0; i < this.intermediateCode.size(); i++)
            this.intermediateCode.get(i).printQuad();
    }

    /* HELPER FUNCTIONS */
    /* returns the number of the next quad */
    public int NEXTQUAD() {
        return 0;
    }

    /* generates the next quad op,x,y,z */
    public Quad GENQUAD(String op, String x, String y, String z) {
        this.numOfLabel++;
        this.usedLabelNames.add(this.numOfLabel.toString());
        return new Quad(this.numOfLabel.toString(),op, x, y, z);
    }

    /* creates a new temporary value of Type t */
    public String NEWTEMP(STRecord.Type t) {
        this.numOfTemp++;
        // our new temp name
        String newTemp = "$"+numOfTemp;
        this.usedTempNames.add(newTemp);
        return newTemp;
    }

    /* creates an empty list of quads' labels */
    public ArrayList<Quad> EMPTYLIST() {
        return new ArrayList<Quad>();
    }

    /* creates a list of quads' labels that contains just one element x */
    public ArrayList<Quad> MAKELIST(Quad q) {
        ArrayList<Quad> temp = new ArrayList<Quad>();
        temp.add(q);
        return temp;
    }

    /* merges the lists of quads' labels l1,...,ln */
    public ArrayList<Quad> MERGE(ArrayList<ArrayList<Quad>> l) {
        ArrayList<Quad> temp = new ArrayList<Quad>();
        for (int i = 0; i < l.size(); i++)
            temp.addAll(l.get(i));
        return temp;
    }

    /* replaces in all the quads that are included in l the unknown the label with z  */
    public void BACKPATCH(String l, String z) {

    }

}
