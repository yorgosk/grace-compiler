package compiler;

import java.util.ArrayList;

public class MachineCode {
    /* a Java Array-List of Strings where the total of the assembly code is stored */
    private ArrayList<String> assembly;

    /* our MachineCode's class (default-)constructor */
    public MachineCode() {
        this.assembly = new ArrayList<String>();
        this.assembly.add(".intel_syntax noprefix # Use Intel syntax instead of AT&T\n");
        this.assembly.add(".text\n");
    }

    /* various utility functions */
    public ArrayList<String> getAssembly() { return this.assembly; }
    public void addAssemblyCode(String aCode) { this.assembly.add(aCode); }
    public String getAssemblyAsString() {
        String ret = "";
        for(int i = 0; i < this.assembly.size(); i++)
            ret += this.assembly.get(i);
        return ret;
    }

}
