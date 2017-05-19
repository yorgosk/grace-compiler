package compiler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;

/*
* A Symbol-Table implementation as an "Array of Lists" (kind-of-like a threaded stack)
* */
public class SymbolTable {
    // the actual Symbol-Table is going to be a Java Array-List, utilised as a Stack
    private Integer symbolTableStackTop;
    private ArrayList<STRecord> symbolTable;
    // a Java Stack where we note where our scopes - namespaces begin, inside the Symbol-Table
    private Integer numberOfScopes;
    private Stack<NSRecord> nameStack;
    // a Java Hash-Map (<varName, varIndex>) where we note the last occurrences of variable names inside the Symbol-Table
    private HashMap<String, Integer> variableMap;

    /* checkShadowing(record): check if a record shadows another record that is already in the Symbol-Table */
    private Integer checkShadowing(STRecord record) {
        int index = 0;
        for(STRecord r : symbolTable) {
            if(record.getName().equals(r.getName())) return index;
            index++;
        }
        return -1;
    }

    /* SymbolTable's class constructor */
    public SymbolTable() {
        this.symbolTableStackTop = -1;
        this.symbolTable = new ArrayList<STRecord>();
        this.numberOfScopes = 0;
        this.nameStack = new Stack<NSRecord>();
        this.variableMap = new HashMap<String, Integer>();
    }

    /* enter(): create a new scope - namespace */
    public void enter(){
        NSRecord temp = new NSRecord();
        this.numberOfScopes++;
        temp.setIndex(-1);
        nameStack.push(temp);
    }

    /* insert(name): create record of a name in the current scope
    * -- insert(): new record in the beginning of the current list */
    public void insert(STRecord record){
        // look-up
        boolean success = lookup(record.getName());
        // if the "record" passed from lookup, add it to the symbol-table
        if(success) {
            // update scopes - namespaces' Stack
            this.symbolTableStackTop++;
            this.nameStack.peek().setIndex(this.symbolTableStackTop);
            // update ST-record's info
            record.setScopeId(numberOfScopes);
            record.setShadowIndex(this.checkShadowing(record));
            // update Symbol-Table
            this.symbolTable.add(record);
            // update variables' Hash-Map so that in any case the name's (key) index (value) will point to it's latest occurrence
            this.variableMap.put(record.getName(), this.symbolTableStackTop);
        } else {
            /* failure --
            * act accordingly */
            System.out.printf("Name %s ERROR\n", record.getName());
        }
    }

    /* lookup(name): search for a name in the current scope
     * -- lookup(): search in the array */
    public boolean lookup(String name){
        // check if the Symbol-Table's Array-List is empty, if it is there is no point of looking up - we sort of have a "success"
        if(this.symbolTable.isEmpty()) return true;
        // take the current scope
        int curr_scope = this.numberOfScopes;
        // iterate the array-list in reverse order until you get out of the current-scope
        int i = this.symbolTable.size()-1;
        int scope = this.symbolTable.get(i).getScopeId();
        while(i >= 0 && scope == curr_scope) {
            if(this.symbolTable.get(i).getName().equals(name)) {
                System.out.printf("Name %s found\n", name);
                return false;
            }
            i--;
            if(i >= 0) scope = this.symbolTable.get(i).getScopeId();
        }
        return true;
    }

    /* exit(): destroy the current scope and delete the names that are defined in it
     * -- exit(): remove the head of the current list for each head of the scopen */
    public void exit(){
        // take the current scope
        int curr_scope = this.numberOfScopes;
        // iterate the array-list in reverse order until you get out of the current-scope
        int i = this.symbolTable.size()-1;
        int scope = this.symbolTable.get(i).getScopeId();
        while(i >= 0 && scope == curr_scope) {
            // if this name shadows another scope's name then update the variables' Hash-Map, else remove the name's (key) mapping
            if(this.symbolTable.get(i).getShadowIndex() != -1) {
                this.variableMap.put(this.symbolTable.get(i).getName(), this.symbolTable.get(i).getShadowIndex());
            } else {
                this.variableMap.remove(this.symbolTable.get(i).getName());
            }
            this.symbolTable.remove(i);
            this.symbolTableStackTop--;
            i--;
            scope = this.symbolTable.get(i).getScopeId();
        }
        // update scopes - namespaces' Stack by popping the "reference" to the destroyed namespace
        nameStack.pop();
    }

    /* print our structures
    * -- for debugging */
    public void printSTStructures() {
        System.out.printf("\nSymbol-Table:\n");
        for (int i = 0; i < this.symbolTable.size(); i++) {
            this.symbolTable.get(i).printSTRecord();
        }
        System.out.printf("\nScope-Namespace Stack:\n");
        for (int i = 0; i < this.nameStack.size(); i++) {
            this.nameStack.get(i).printNSRecord();
        }
        System.out.printf("\nVariables' Hash-Map:\n");
        System.out.println(Arrays.asList(variableMap));
    }

}
