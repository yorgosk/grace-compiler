package compiler;

import java.util.ArrayList;
import java.util.Stack;

/* General Scheme:
* We can define the "translation" of each AST's node to IR as soon as we have code for it's children.
* Each child's result is in a temporary variable. We generate quads that combine the results.
* The end-result is a sequence of quads, where order matters. */

public class IntermediateCode {
    /* we will use a low-level intermediate code (intermediate representation - IR) */
    private String intermediateCode;
    /* we use a stack-buffer to store temporarily parts of the IR,
    * so that we can later "plug" them together in the proper order */
    private Stack<String> irBuffer;
    /* we use a Java Array-List to store our used label names */
    private ArrayList<String> usedLabelNames;
    /* we use a Java Array-List to store our used temporary names */
    private ArrayList<String> usedTempNames;

    /* IntermediateCode's class constructor */
    public IntermediateCode() {
        this.intermediateCode = "";
        this.irBuffer = new Stack<String>();
        this.usedLabelNames = new ArrayList<String>();
        this.usedTempNames = new ArrayList<String>();
    }

    /* our Intermediate Code / Intermediate Representation printing function */
    public void printIR() {
        System.out.printf("\n\t\tLow Level Intermediate Representation:\n");
        System.out.printf(this.intermediateCode);
    }

    /* HELPER FUNCTIONS */
    /* returns the number of the next quad */
    public int NEXTQUAD() {
        return 0;
    }

    /* generates the next quad op,x,y,z */
    public void GENQUAD(String op, String x, String y, String z) {

    }

    /* creates a new temporary value of Type t */
    public String NEWTEMP(STRecord.Type t) {
        return null;
    }

    /* creates an empty list of quads' labels */
    public void EMPTYLIST() {

    }

    /* creates a list of quads' labels that contains just one element x */
    public void MAKELIST(String x) {

    }

    /* merges the lists of quads' labels l1,...,ln */
    public void MERGE(ArrayList<String> l) {

    }

    /* replaces in all the quads that are included in l the unknown the label with z  */
    public void BACKPATCH(String l, String z) {

    }

}
