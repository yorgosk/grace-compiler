package compiler;

import java.util.ArrayList;
import java.util.HashMap;

/* General Scheme:
* We can define the "translation" of each AST's node to IR as soon as we have code for it's children.
* Each child's result is in a temporary variable. We generate quads that combine the results.
* The end-result is a sequence of quads, where order matters. */

public class IntermediateCode {
    /* we are working with quads */
    public static class Quad {
        private Integer label;
        private String op;
        private String x;
        private String y;
        private String z;

        /* Quad's class (default-)constructor */
        public Quad() {}

        /* Quad's class constructors */
        public Quad(Integer label, String op, String x, String y, String z) {
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
        public void setLabel(Integer label) { this.label = label; }
        public void setOp(String op) { this.op = op; }
        public void setX(String x) { this.x = x; }
        public void setY(String y) { this.y = y; }
        public void setZ(String z) { this.z = z; }
        public Integer getLabel() { return this.label; }
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
    private ArrayList<Integer> usedLabels;
    private Integer numOfLabels;
    /* we use a Java Array-List to store our used temporary names */
    private ArrayList<String> usedTempNames;
    private Integer numOfTemp;
    /* a Java Array-List of quad labels that contain jumps in the next command */
    private ArrayList<Integer> NEXT;
    /* a Java Array-List of quad labels that contain jumps that should be executed in case a command is True */
    private ArrayList<Integer> TRUE;
    /* a Java Array-List of quad labels that contain jumps that should be executed in case a command is True */
    private ArrayList<Integer> FALSE;
    /* a Java Hash-Map that for each temporary storage or variable/l-value name maps it's type */
    private HashMap<String, STRecord.Type> typeMap;
    /* a Java Hash-Map that maps the temporary storage where a, l-value's or r-value's value is stored */
    private HashMap<String, String> PLACE;

    /* IntermediateCode's class constructor */
    public IntermediateCode() {
        this.intermediateCode = new ArrayList<Quad>();
        this.usedLabels = new ArrayList<Integer>();
        this.numOfLabels = 0;
        this.usedTempNames = new ArrayList<String>();
        this.numOfTemp = 0;
        this.NEXT = new ArrayList<Integer>();
        this.TRUE = new ArrayList<Integer>();
        this.FALSE = new ArrayList<Integer>();
        this.typeMap = new HashMap<String, STRecord.Type>();
        this.PLACE = new HashMap<String, String>();
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
        int index = this.numOfLabels+1;  // our next quad is going to be the very next label that we are going to produce
        return this.intermediateCode.get(index).getLabel();
    }

    /* generates the next quad op,x,y,z */
    public void GENQUAD(String op, String x, String y, String z) {
        this.numOfLabels++;
        this.usedLabels.add(this.numOfLabels);
        Quad temp = new Quad(this.numOfLabels, op, x, y, z);
        this.intermediateCode.add(temp);
    }

    /* creates a new temporary value of Type t */
    public String NEWTEMP(STRecord.Type t) {
        this.numOfTemp++;
        // our new temp name
        String newTemp = "$t"+numOfTemp;
        this.usedTempNames.add(newTemp);
        // map temp's Type
        this.typeMap.put(newTemp, t);
        return newTemp;
    }

    /* creates an empty list of quads' labels */
    public ArrayList<Integer> EMPTYLIST() { return new ArrayList<Integer>(); }

    /* creates a list of quads' labels that contains just one element x */
    public ArrayList<Integer> MAKELIST(Integer x) {
        ArrayList<Integer> temp = new ArrayList<Integer>();
        temp.add(x);
        return temp;
    }

    /* merges the lists of quads' labels l1,...,ln */
    public ArrayList<Integer> MERGE(ArrayList<ArrayList<Integer>> l) {
        ArrayList<Integer> temp = new ArrayList<Integer>();
        for (int i = 0; i < l.size(); i++)
            temp.addAll(l.get(i));
        return temp;
    }

    /* replaces in all the quads that are included in l the unknown the label with z  */
    public void BACKPATCH(ArrayList<Integer> l, Integer z) {
        for (int i = 0; i < l.size(); i++) {
            for (int j = 0; j < this.intermediateCode.size(); j++) {
                this.intermediateCode.get(j).setZ(z.toString());
            }
        }
    }

    /* takes the Type of a parameter and determines the mode under which it is passed */
    public String PARAMMODE(String id, Integer number) {
        System.out.printf("PARAMMODE for %s - %d", id, number);
        this.printTypeMap();
        System.out.print("DFSDFSDFSDFFD");
        System.out.print(id);
        STRecord.Type temp = this.typeMap.get(id);
        temp.printType();
        System.out.print("SFAFFSD");
        assert (temp != null);
        STRecord.Type tempParam = temp.fetchParamType(number);
//        assert (tempParam != null);
        if (tempParam == null) return "RET";
        if (tempParam.getArray() || tempParam.getRef()) return "R";
        else return "V";
    }

    //debug function
    public void printTypeMap(){
        for (String name: this.typeMap.keySet()){

            String key =name.toString();
            System.out.print(key);
            System.out.print("\n");
            STRecord.Type value= this.typeMap.get(name);
            value.printType();


        }
    }

    /* NEXT, TRUE, FALSE manipulation functions */
    public void addNEXT(Integer label) { this.NEXT.add(label); }
    public void addTRUE(Integer label) { this.TRUE.add(label);}
    public void addFALSE(Integer label) { this.FALSE.add(label); }
    public void addType(String key, STRecord.Type value) { this.typeMap.put(key, value); }
    public void addPLACE(String key, String value) { this.PLACE.put(key, value); }
    public STRecord.Type getType(String key) { return this.typeMap.get(key); }
    public String getPLACE(String key) { return this.PLACE.get(key); }
    public void resetNEXT() { this.NEXT = new ArrayList<Integer>(); }
    public void resetTRUE() { this.TRUE = new ArrayList<Integer>(); }
    public void resetFALSE() { this.FALSE = new ArrayList<Integer>(); }
    public void resetType() { this.typeMap = new HashMap<String, STRecord.Type>(); }
    public void resetPLACE() { this.PLACE = new HashMap<String, String>(); }

}
