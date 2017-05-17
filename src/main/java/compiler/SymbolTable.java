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
    private Integer noOfScopes;
    private Stack<NSRecord> nameStack;
    // a Java Hash-Map (<varName, varIndex>) where we note the last occurrences of variable names inside the Symbol-Table
    private HashMap<String, Integer> variableMap;

    /* checkShadowing(record): check if a record shadows another record that is already in the Symbol-Table */
    private Integer checkShadowing(STRecord record) {
        int index = 0;
        for(STRecord r : symbolTable) {
            if(record.getName() == r.getName()) return index;
            index++;
        }
        return -1;
    }

    /* SymbolTable's class constructor */
    public SymbolTable() {
        this.symbolTableStackTop = -1;
        this.symbolTable = new ArrayList<STRecord>();
        this.noOfScopes = 0;
        this.nameStack = new Stack<NSRecord>();
        this.variableMap = new HashMap<String, Integer>();
    }

    /* enter(): create a new scope - namespace */
    public void enter(String name){
        NSRecord temp = new NSRecord();
        this.symbolTableStackTop++;
        this.noOfScopes++;
        temp.setIndex(this.symbolTableStackTop);
        temp.setName(name);
        nameStack.push(temp);
    }

    /* insert(name): create record of a name in the current scope
    * -- insert(): new record in the beginning of the current list */
    public void insert(STRecord record){
        this.symbolTableStackTop++;
        record.setScopeId(noOfScopes);
        /*
        * LOOKUP
        * */
        record.setShadowIndex(this.checkShadowing(record));
        this.symbolTable.add(record);
    }

    /* lookup(name): search for a name in the current scope
     * -- lookup(): search in the array */
    public void lookup(String name){
//        ArrayList< String > insideArray = new ArrayList<String>();
//        if (symbolTable != null && !symbolTable.isEmpty()) {
//            insideArray = symbolTable.get(symbolTable.size()-1);
//        } else {
//            System.out.println("Trying to access empty list");
//        }
//        if (insideArray != null){
//            if (insideArray.contains(name)) {
//                System.out.println("Variable allready exists");
//            } else {
//                System.out.println("Variable first time is declared");
//            }
//        } else {
//            System.out.println("Trying to access empty list");
//        }
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
