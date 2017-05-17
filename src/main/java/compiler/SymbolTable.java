package compiler;

import java.util.ArrayList;
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
    public void enter(String name){
        NSRecord temp = new NSRecord();
        this.numberOfScopes++;
        temp.setName(name);
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
            variableMap.put(record.getName(), symbolTableStackTop);
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
            scope = this.symbolTable.get(i).getScopeId();
        }
        return true;
    }

    /* exit(): destroy the current scope and delete the names that are defined in it
     * -- exit(): remove the head of the current list for each head of the scopen */
    public void exit(){
        if (symbolTable != null && !symbolTable.isEmpty()) {
            symbolTable.remove(symbolTable.size()-1);
        }
        else {
            System.out.println("Trying to pop from empty list");
        }
    }

}
