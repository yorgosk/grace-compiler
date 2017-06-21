package compiler;

import java.util.ArrayList;
import java.util.HashMap;

public class MachineCode {
    /* a Java Array-List of Strings where the total of the assembly code is stored */
    private ArrayList<String> assembly;
    /* the number of the non-local operands */
    private Integer nonLocalOperands;
    /* a Java Hash-Map that maps each non-local operand to the relative position in memory where it is stored */
    private HashMap<String, Integer> nonLocalOperandsMap;
    /* temporary store nesting depths */
    private Integer tempNp;
    private Integer tempNx;

    /* MachineCode's class (default-)constructor */
    public MachineCode() {
        this.assembly = new ArrayList<String>();
        this.nonLocalOperands = 0;
        this.nonLocalOperandsMap = new HashMap<String, Integer>();

        // add the first lines of assembly
        this.assembly.add(".intel_syntax noprefix # Use Intel syntax instead of AT&T\n");
        this.assembly.add(".text\n");
    }

    /* MachineCode's class setters and getters */
    public ArrayList<String> getAssembly() { return this.assembly; }
    public void setNonLocalOperands(Integer nonLocalOperands1) { this.nonLocalOperands = nonLocalOperands1; }
    public Integer getNonLocalOperands() { return this.nonLocalOperands; }
    public void setNonLocalOperandRelativePosition(String nonLocalOperandName, Integer nonLocalOperandRelativePosition) { this.nonLocalOperandsMap.put(nonLocalOperandName, nonLocalOperandRelativePosition); }
    public Integer getNonLocalOperandRelativePosition(String nonLocalOperandName) { return this.nonLocalOperandsMap.get(nonLocalOperandName); }
    public void setTempNp(Integer tempNp) { this.tempNp = tempNp; }
    public Integer getTempNp() { return this.tempNp; }
    public void setTempNx(Integer tempNx) { this.tempNx = tempNx; }
    public Integer getTempNx() { return this.tempNx; }

    /* getAr(a) -- produces the machine code x86 for loading the record address of an
            * activation record which contains the non-local operand "a" to the register "si" */
    // case of AccessLinks
    public void getAr(String a) {
        int relPos = this.getNonLocalOperandRelativePosition(a);
        this.assembly.add("mov si, word ptr [bp+4]\n");
        if (relPos > 1)
            for (int i = 2; i <= relPos; i++)
                this.assembly.add("mov si, word ptr [si+4]\n");
    }
    // case of Displays
    public void getAr(String a, String displayFlag) {
        assert (displayFlag.equals("display")); // for debugging
        Integer relPos = this.getNonLocalOperandRelativePosition(a);
        this.assembly.add("mov si, word ptr display[2 * "+relPos.toString()+"]\n");
    }

    /* updateAL() -- contains the code for updating the Access Links */
    public void updateAL() {
        if (tempNp < tempNx)
            this.assembly.add("push bp\n");
        else if (tempNp == tempNp)
            this.assembly.add("push word ptr [bp+4]\n");
        else {
            int dif = tempNp-tempNx;
            this.assembly.add("mov si, word ptr [bp+4]\n");
            for (int i = 1; i <= dif; i++)
                this.assembly.add("mov si, word ptr [si+4]\n");
            this.assembly.add("push word ptr [si+4]");
        }
    }

    /* various utility functions */
    public void addAssemblyCode(String aCode) { this.assembly.add(aCode); }
    public String getAssemblyAsString() {
        String ret = "";
        for(int i = 0; i < this.assembly.size(); i++)
            ret += this.assembly.get(i);
        return ret;
    }

}
