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
    // a Java Stack where we note where our namespaces begin, inside the Symbol-Table
    private Integer noOfNamespaces;
    private Stack<NSRecord> nameStack;
    // a Java Hash-Map (<varName, varIndex>) where we note the last occurrences of variable names inside the Symbol-Table
    private HashMap<String, Integer> variableMap;

    /* SymbolTable's class constructor */
    public SymbolTable() {
        this.symbolTableStackTop = 0;
        this.symbolTable = new ArrayList<STRecord>();
        this.noOfNamespaces = 0;
        this.nameStack = new Stack<NSRecord>();
        this.variableMap = new HashMap<String, Integer>();
    }

    /* enter(): create a new scope */
    public void enter(){
//        ArrayList<String> insideArray = new ArrayList<String>();
//        //apo oti katalava den uparxei kindunos i lista na einai gemati, opote den xreiazetai antistoixos elegxos
//        if (symbolTable != null){
//            symbolTable.add(insideArray);
//        } else {
//            System.out.println("ListArray hasnt been defined");
//        }
    }

    /* insert(name): create record of a name in the current scope
    * -- insert(): new record in the beginning of the current list */
    public void insert(String name){
//        ArrayList< String > insideArray = new ArrayList<String>();
//        if (symbolTable != null && !symbolTable.isEmpty()) {
//            insideArray = symbolTable.get(symbolTable.size()-1);
//        }
//        if (insideArray != null){
//            insideArray.add(name);
//        } else {
//            System.out.println("ListArray hasnt been defined");
//        }
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
