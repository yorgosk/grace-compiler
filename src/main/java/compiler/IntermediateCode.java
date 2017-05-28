package compiler;

import java.util.ArrayList;
import java.util.Stack;

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
    /* the only function that generates instructions
    * -- adds instructions to the end of the buffer -- at the end, buffer contains code */
    public void emit() {

    }

    /* generate a unique label name
     * -- does not update code */
    public String new_label() {
        return null;
    }

    /* generate a unique temporary name
     * -- may require type information */
    public String new_temp() {
        return null;
    }

    /* our main IR code generation function
     * -- basically runs the whole process */
    public String generate() {
        return null;
    }

}
