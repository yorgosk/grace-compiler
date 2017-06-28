package compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class MachineCode {
    /* a Java Array-List of Strings where the total of the assembly code is stored */
    private ArrayList<String> assembly;
    /* a Java Array-List where the data of the assembly code are stored */
    private ArrayList<String> data;
    /* the number of the non-local operands */
    private Integer nonLocalOperands;
    /* a Java Hash-Map that maps each non-local operand to the relative position in memory where it is stored */
    private HashMap<String, Integer> nonLocalOperandsMap;
    /* temporary store nesting depths */
    private Integer tempNp;
    private Integer tempNx;
    /* a Java Hash-Map that maps each data name to it's data type */
    private HashMap<String, STRecord> dataMap;
    /* a Java Hash-Map that maps each IR's quad with label to a x8086 command with a label */
    private HashMap<Integer, Integer> labelMap;
    /* how many lines of assembly commands we already have */
    private Integer numberOfCommands;
    /* how many lines of assembly data we already have */
    private Integer numberOfData;

    /* a Java Stack of Activation Records */
    private Stack<ActivationRecord> ARStack;
    /* how many Activation Records we have so far */
    private Integer numberOfActivationRecords;

    /* a Java Hash-Map that for each name stores the levels of nesting in which it has been used, taken regularly from the Symbol Table, through updates */
    private HashMap<String, ArrayList<Integer>> nestingMap;
    /* our current levels of nesting, taken regularly from the Symbol Table, through updates */
    private Integer currentLevelsOfNesting;

    /* MachineCode's class (default-)constructor */
    public MachineCode() {
        // regarding Machine Code
        this.assembly = new ArrayList<String>();
        this.data = new ArrayList<String>();
        this.data.add(".data\n");
        this.nonLocalOperands = 0;
        this.nonLocalOperandsMap = new HashMap<String, Integer>();
        this.dataMap = new HashMap<String, STRecord>();
        this.labelMap = new HashMap<Integer, Integer>();
        this.numberOfCommands = 0;
        this.numberOfData = 0;
        // regarding Activation Records
        this.ARStack = new Stack<ActivationRecord>();
        this.numberOfActivationRecords = 0;
        // regarding the Nesting Scheme
        this.nestingMap = new HashMap<String, ArrayList<Integer>>();
        this.currentLevelsOfNesting = 0;

        // add the first lines of assembly
        this.assembly.add(".intel_syntax noprefix # Use Intel syntax instead of AT&T\n");
        this.assembly.add(".text\n");
        this.numberOfCommands += 2;
    }

    /* MachineCode's class setters and getters */
    public ArrayList<String> getAssembly() { return this.assembly; }
    public ArrayList<String> getData() { return this.data; }
    public void setNonLocalOperands(Integer nonLocalOperands1) { this.nonLocalOperands = nonLocalOperands1; }
    public Integer getNonLocalOperands() { return this.nonLocalOperands; }
    public void setNonLocalOperandRelativePosition(String nonLocalOperandName, Integer nonLocalOperandRelativePosition) {
        this.nonLocalOperandsMap.put(nonLocalOperandName, nonLocalOperandRelativePosition);
        this.nonLocalOperands++;
    }
    public Integer getNonLocalOperandRelativePosition(String nonLocalOperandName) { return this.nonLocalOperandsMap.get(nonLocalOperandName); }
    public void setTempNp(Integer tempNp) { this.tempNp = tempNp; }
    public Integer getTempNp() { return this.tempNp; }
    public void setTempNx(Integer tempNx) { this.tempNx = tempNx; }
    public Integer getTempNx() { return this.tempNx; }
    public void setDataMapping(String name, STRecord info) { this.dataMap.put(name, info); }
    public STRecord getDataMapping(String name) { return this.dataMap.get(name); }
    public void setLabelMapping(Integer irLabel, Integer assemblyLabel) { this.labelMap.put(irLabel, assemblyLabel); }
    public Integer getLabelMapping(Integer irLabel) { return this.labelMap.get(irLabel); }
    public void setNumberOfCommands(Integer commands) { this.numberOfCommands = commands; }
    public Integer getNumberOfCommands() { return this.numberOfCommands; }
    public Integer getCurrentCommand() { return this.numberOfCommands; }    // same functionality with above, different interface
    public void setNumberOfData(int data) { this.numberOfData = data; }
    public Integer getNumberOfData() { return this.numberOfData; }
    public Integer getCurrentData() { return this.numberOfData; }   // same functionality with above, different interface
    public void pushActivationRecord(ActivationRecord ar) {
        this.ARStack.push(ar);
        this.numberOfActivationRecords++;
    }
    public ActivationRecord popActivationRecord() {
        this.numberOfActivationRecords--;
        return this.ARStack.pop();
    }
    public void setNumberOfActivationRecords(Integer activationRecords) { this.numberOfActivationRecords = activationRecords; }
    public Integer getNumberOfActivationRecords() { return this.numberOfActivationRecords; }
    public Integer getCurrentLevelsOfNesting() { return this.currentLevelsOfNesting; }

    /* ITERATING THROUGH NAMES */
    /* getAr(a) -- produces the machine code x86 for loading the record address of an
            * activation record which contains the non-local operand "a" to the register "si" */
    // case of AccessLinks
    public void getAR(String a) {
        int relPos = this.getNonLocalOperandRelativePosition(a);
        this.assembly.add("mov si, word ptr [bp+4]\n");
        this.numberOfCommands++;
        if (relPos > 1)
            for (int i = 2; i <= relPos; i++) {
                this.assembly.add("mov si, word ptr [si+4]\n");
                this.numberOfCommands++;
            }
    }
    // case of Displays
    public void getAR(String a, String displayFlag) {
        assert (displayFlag.equals("display")); // for debugging
        Integer relPos = this.getNonLocalOperandRelativePosition(a);
        this.assembly.add("mov si, word ptr display[2 * "+relPos.toString()+"]\n");
        this.numberOfCommands++;
    }

    /* updateAL() -- contains the code for updating the Access Links */
    public void updateAL() {
        if (tempNp < tempNx) {
            this.assembly.add("push bp\n");
            this.numberOfCommands++;
        } else if (tempNp == tempNx) {
            this.assembly.add("push word ptr [bp+4]\n");
            this.numberOfCommands++;
        } else {
            int dif = tempNp-tempNx;
            this.assembly.add("mov si, word ptr [bp+4]\n");
            this.numberOfCommands++;
            for (int i = 1; i <= dif; i++) {
                this.assembly.add("mov si, word ptr [si+4]\n");
                this.numberOfCommands++;
            }
            this.assembly.add("push word ptr [si+4]");
            this.numberOfCommands++;
        }
    }

    /* ASSISTANT-ROUTINES */
    /* load(R,a) -- produces code for storing data "a" at register "R" */
    public void load(String R, String a) {
        /* case of numerical constant */
        if (this.getDataMapping(a).getType().getKind().equals("int")) {
            this.assembly.add("mov " + R + ", " + a + "\n");
            this.numberOfCommands++;
        /* case of logical constant "true"??? */
//        this.assembly.add("mov "+R+", 1\n");
        /* case of logical constant "false"??? */
//        this.assembly.add("mov "+R+", 0\n");
        /* case of character constant */
        } else if (this.getDataMapping(a).getType().getKind().equals("char")) {
            this.assembly.add("mov " + R + ", ASCII(" + a + ")\n");
            this.numberOfCommands++;
        /* case of local variable, parameter by value, temporary variable */
        } else if (this.getDataMapping(a).getLocal() || !this.getDataMapping(a).getType().getRef()) {
            this.assembly.add("mov " + R + ", size ptr [bp + offset]\n");
            this.numberOfCommands++;
        /* case of parameter by reference */
        } else if (this.getDataMapping(a).getType().getRef()) {
            this.assembly.add("mov si, word ptr [bp + offset]\n");
            this.assembly.add("mov "+R+", size ptr [si]\n");
            this.numberOfCommands += 2;
        }
        /* case of non-local variable, parameter by value */
        else if (!this.getDataMapping(a).getLocal() || !this.getDataMapping(a).getType().getRef()) {
            this.getAR(a);
            this.assembly.add("mov "+R+", size ptr [si + offset]\n");
            this.numberOfCommands++;
        }
        /* case of non-local parameter by reference */
        else if (this.getDataMapping(a).getType().getRef()) {
            this.getAR(a);
            this.assembly.add("mov si, word ptr [si + offset]\n");
            this.assembly.add("mov "+R+", size ptr [si]\n");
            this.numberOfCommands += 2;
        }
        /* case of dereference */
        else if (this.getDataMapping(a).getDereference()) {
            this.assembly.add("load(di, "+a+")\n");
            this.assembly.add("mov "+R+", size ptr [di]\n");
            this.numberOfCommands += 2;
        }
        /* case of memory address */
//        this.loadAddr(R,a);
        else
            assert (false); // we don't want to end up here, under any situation
    }

    /* loadAddr(R,a) -- produces code for storing the address of the data "a" at register "R" */
    public void loadAddr(String R, String a) {
        /* case of literal constant */
        if (this.getDataMapping(a).getType().getKind().equals("char") && this.getDataMapping(a).getType().getArray()) {
            this.assembly.add("lea " + R + ", byte ptr " + a + "\n");
            this.numberOfCommands++;
        /* case of local parameter by value */
        } else if (this.getDataMapping(a).getParam() && !this.getDataMapping(a).getType().getRef()) {
            this.assembly.add("lea " + R + ", size ptr [bp + offset]\n");
            this.numberOfCommands++;
        /* case of local parameter by reference */
        } else if (this.getDataMapping(a).getParam() && this.getDataMapping(a).getType().getRef()) {
            this.assembly.add("mov " + R + ", word ptr [bp + offset]\n");
            this.numberOfCommands++;
        /* case of non-local parameter by value */
        } else if (!this.getDataMapping(a).getLocal() && this.getDataMapping(a).getParam() && !this.getDataMapping(a).getType().getRef()) {
            this.getAR(a);
            this.assembly.add("lea "+R+", size ptr [si + offset]\n");
            this.numberOfCommands++;
        }
        /* case of non-local parameter by reference */
        else if (!this.getDataMapping(a).getLocal() && this.getDataMapping(a).getParam() && this.getDataMapping(a).getType().getRef()) {
            this.getAR(a);
            this.assembly.add("mov "+R+", size ptr [si + offset]\n");
            this.numberOfCommands++;
        }
        /* case of dereference */
        else if (this.getDataMapping(a).getDereference()) {
            this.assembly.add("load(" + R + ", " + a + ")\n");
            this.numberOfCommands++;
        } else
            assert (false); // we don't want to end up here, under any situation
    }

    /* store(R,a) -- produces code for storing the contents of the register "R" at data "a" */
    public void store(String R, String a) {
        /* case of local parameter by value or temporary variable */
        if (this.getDataMapping(a).getLocal() && !this.getDataMapping(a).getType().getRef()) {
            this.assembly.add("mov size ptr [bp+offset], " + R + "\n");
            this.numberOfCommands++;
        /* case of local parameter by reference */
        } else if (this.getDataMapping(a).getLocal() && this.getDataMapping(a).getType().getRef()) {
            this.assembly.add("mov si, word ptr [bp+offset]\n");
            this.assembly.add("mov size ptr [si], "+R+"\n");
            this.numberOfCommands += 2;
        }
        /* case of non-local parameter by value */
        else if (!this.getDataMapping(a).getLocal() && !this.getDataMapping(a).getType().getRef()) {
            this.getAR(a);
            this.assembly.add("mov size ptr [si+offset], "+R+"\n");
            this.numberOfCommands++;
        }
        /* case of non-local parameter by reference */
        else if (!this.getDataMapping(a).getLocal() && this.getDataMapping(a).getType().getRef()) {
            this.getAR(a);
            this.assembly.add("mov si, word ptr [si+offset]\n");
            this.assembly.add("mov size ptr [si], "+R+"\n");
            this.numberOfCommands += 2;
        }
        /* case of dereference */
        else if (this.getDataMapping(a).getDereference()) {
            this.assembly.add("load(di, "+a+")\n");
            this.assembly.add("mov size ptr [di], "+R+"\n");
            this.numberOfCommands += 2;
        }
        else
            assert (false); // we don't want to end up here, under any situation
    }

    /* various utility functions */
    /* add an assembly command */
    public void addAssemblyCode(String aCode) {
        this.assembly.add(aCode);
        this.numberOfCommands++;
    }
    public void addData(String data) {
        this.data.add(data);
        this.numberOfData++;
    }
    /* get the whole assembly code produced until a certain point as a string */
    public String getAssemblyAsString() {
        String ret = "";
        // append with assembly commands
        for (int i = 0; i < this.assembly.size(); i++)
            ret += this.assembly.get(i);
        // append with assembly data
        for (int i = 0; i < this.data.size(); i++)
            ret += this.data.get(i);
        return ret;
    }
    /* get the size of a data type in bytes */
    public Integer getTypeSize(STRecord.Type type) {
        if (type.getKind().equals("int")) return 4;
        else if (type.getKind().equals("char")) return 1;
        else {
            assert (type.getKind().equals("nothing"));  // for debugging
            return 1;
        }
    }
    /* update a command's label */
    public void label(Integer assemblyCommandLabel, Integer assemblyJumpLabel) {
        String newCommand = "";
        String oldCommand = this.assembly.get(assemblyCommandLabel-1);
        System.out.printf("oldCommand: %s\n", oldCommand);   // for debugging
        String[] tokens = oldCommand.split(" ");
        newCommand += tokens[0]+" "+assemblyJumpLabel.toString()+"\n";
        this.assembly.add(assemblyCommandLabel-1, newCommand);
    }
    /* update our Nesting Scheme, by updating our Nesting Map and our current levels of nesting variable */
    public void updateNestingMap(HashMap<String, ArrayList<Integer>> map) { this.nestingMap = map; }
    public void updateLevelsOfNesting(Integer currentLevelsOfNesting) { this.currentLevelsOfNesting = currentLevelsOfNesting; }

}
