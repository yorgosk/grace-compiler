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
    // a Java Array-List where we store Grace's Library Functions
    private ArrayList<STRecord> library;
    // a Java Array-List where we store the functions that have been defined in outer scopes (e.g. local-def functions)
    private ArrayList<STRecord> knownFunctions;

    // a Java Stack of Java Array-Lists of function names, to note the nesting scheme between the functions of a program
    private Stack<ArrayList<String>> nestingScheme;
    // how many levels of nesting we have at any time
    private Integer levelsOfNesting;
    // an index for the function of the level that we are at any time
    private Integer currentLevelFunction;
    // a Java Hash-Map that for each name stores the levels of nesting in which it has been used
    private HashMap<String, ArrayList<Integer>> nestingMap;

    /* SymbolTable's class constructor */
    public SymbolTable() {
        this.symbolTableStackTop = -1;
        this.symbolTable = new ArrayList<STRecord>();
        this.numberOfScopes = 0;
        this.nameStack = new Stack<NSRecord>();
        this.variableMap = new HashMap<String, Integer>();
        this.library = new ArrayList<STRecord>();
        this.knownFunctions = new ArrayList<STRecord>();
        // initialize nesting scheme
        this.nestingScheme = new Stack<ArrayList<String>>();
        this.levelsOfNesting = 0;
        this.currentLevelFunction = -1;
        this.nestingMap = new HashMap<String, ArrayList<Integer>>();
        // load Grace's library-functions
        this.loadGraceLibrary();
    }

    /* setters and getters */
    public void setScopeType(STRecord.Type type) {
        this.nameStack.peek().setType(type);
    }
    public void addKnownFunction(STRecord func) { this.knownFunctions.add(func); }
    public ArrayList<STRecord> getLibrary() { return this.library; }
    public ArrayList<STRecord> getKnownFunctions() { return this.knownFunctions; }
    public void setLevelsOfNesting(Integer levelsOfNesting) { this.levelsOfNesting = levelsOfNesting; }
    public Integer getLevelsOfNesting() { return this.levelsOfNesting; }
    public Integer getCurrentLevelFunction() { return this.currentLevelFunction; }
    public HashMap<String, ArrayList<Integer>> getNestingMap() { return this.nestingMap; }

    /* enter(): create a new scope - namespace */
    public void enter(){
        NSRecord temp = new NSRecord();
        this.numberOfScopes++;
        temp.setIndex(-1);
        nameStack.push(temp);

        // noting nesting scheme -- adding a new level
        this.addNestingLevel();
    }

    /* insert(name): create record of a name in the current scope
    * -- insert(): new record in the beginning of the current list */
    public void insert(STRecord record){
        // look-up
        //boolean success = lookup(record.getName()); //changed by yiannis_sem in the line below
        boolean success = look(record.getName());
        // if the "record" passed from lookup, add it to the symbol-table
        if(success) {
            // noting nesting scheme -- adding name to current nesting scheme's level
            String tempName = this.updateCurrentNestingLevel(record.getName());
//            this.printNestingScheme();  // for debugging

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
            //System.out.printf("Name %s ERROR\n", record.getName());
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
        while(i >= 0/* && scope == curr_scope*/) {      //commented by yiannis_sem : it seems to work fine but may creates problems
            if(this.symbolTable.get(i).getName().equals(name)) {
                //System.out.printf("Name %s found\n", name);
                return false;
            }
            i--;
            if(i >= 0) scope = this.symbolTable.get(i).getScopeId();
        }
        return true;
    }

    /* checkShadowing(record): check if a record shadows another record that is already in the Symbol-Table */
    private Integer checkShadowing(STRecord record) {
        int index = 0;
        int ret_Value = -1;
        for(STRecord r : symbolTable) {
            if(record.getName().equals(r.getName())) ret_Value = index;
            index++;
        }
        return ret_Value;
    }

    /* check the returned Type */
    public boolean checkRetType(STRecord.Type other){
        STRecord.Type temp = this.nameStack.peek().getType();      //function added by yiannis
        //System.out.printf("printing temp type\n");
        temp.printType();
        //System.out.printf("printed temp type\n");
        if(temp.isSameRetType(other)){
            return true;
        }
        else{
            return false;
        }
    }

    //added by yiannis_sem : because lookup changed to can be used in astp i rename it to look to  use it in insert
    public boolean look(String name){
        // check if the Symbol-Table's Array-List is empty, if it is there is no point of looking up - we sort of have a "success"
        if(this.symbolTable.isEmpty()) return true;
        // take the current scope
        int curr_scope = this.numberOfScopes;
        // iterate the array-list in reverse order until you get out of the current-scope
        int i = this.symbolTable.size()-1;
        int scope = this.symbolTable.get(i).getScopeId();
        while(i >= 0 && scope == curr_scope) {
            if(this.symbolTable.get(i).getName().equals(name)) {
                //System.out.printf("Name %s found\n", name);
                return false;
            }
            i--;
            if(i >= 0) scope = this.symbolTable.get(i).getScopeId();
        }
        return true;
    }
    //till here

    //add by yiannis_sem for debugging
    public void printscope(){
        int curr_scope = this.numberOfScopes;
        // iterate the array-list in reverse order until you get out of the current-scope
        int i = this.symbolTable.size()-1;
        int scope = this.symbolTable.get(i).getScopeId();
        while(i >= 0 /*&& scope == curr_scope*/) {
            //System.out.print(this.symbolTable.get(i).getName());
            i--;
            if(i >= 0) scope = this.symbolTable.get(i).getScopeId();
        }
    }
    //till here


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
            if (i >= 0) scope = this.symbolTable.get(i).getScopeId();
        }
        // update scopes - namespaces' Stack by popping the "reference" to the destroyed namespace
        nameStack.pop();
        // we have one less scope
        this.numberOfScopes--;

        // noting nesting scheme -- remove current nesting level
        this.removeNestingLevel();
    }

    /* searchFunction(tempRecord): search to find out whether we have occured a function again in the current scope
     * -- searchFunction(): search in the array, -1: type-error, 0: search-failure (not defined), 1: search-success (defined) */
    public Integer searchFunction(STRecord tempRec){
        // check if the Symbol-Table's Array-List is empty, if it is there is no point of searching - we sort of have a "search failure"
        if(this.symbolTable.isEmpty()) return 0;
        // take the current scope
        int curr_scope = this.numberOfScopes;
        // iterate the array-list in reverse order until you get out of the current-scope
        int i = this.symbolTable.size()-1;
        int scope = this.symbolTable.get(i).getScopeId();
        while(i >= 0 && scope == curr_scope) {
            if (this.symbolTable.get(i).getName().equals(tempRec.getName())) {
                if (this.symbolTable.get(i).getType().isSame(tempRec.getType())) {
                    //System.out.printf("Function %s found\n", tempRec.getName());
                    if (this.symbolTable.get(i).getDefined()) return 1; // we have a "search-success"
                    else return 0;                                      // we have a "search-failure"
                } else return -1;       // same name, but incompatible types -- we have a "type-error"
            }
            i--;
            if(i >= 0) scope = this.symbolTable.get(i).getScopeId();
        }
        return 0;   // if we reached this far, then we have a "search-failure"
    }

    /* fetchType(name): search for a name in the current scope and then return it's Type
     * -- fetchType(): search in the array, same as the lookup(), it only returns Type */
    public STRecord.Type fetchType(String name){
        // take the type from any scope (it may not be local)
        STRecord.Type temp = null;
        if (this.variableMap.containsKey(name)) {
            //System.out.printf("Name %s found\n", name);
            //System.out.print(this.variableMap.keySet());
            int index = this.variableMap.get(name);
            temp = new STRecord.Type(this.symbolTable.get(index).getType());
        }
        if (temp == null) {
            for(int i = 0; i < this.library.size(); i++) {
                if(this.library.get(i).getName().equals(name)) {
                    temp = new STRecord.Type(this.library.get(i).getType());
                    break;
                }
            }
        }
        if (temp == null) {
            for (int i = 0; i < this.knownFunctions.size(); i++) {
                if (this.knownFunctions.get(i).getName().equals(name)) {
                    temp = new STRecord.Type(this.knownFunctions.get(i).getType());
                    break;
                }
            }
        }

        return temp;
    }

    /* paramType(name): search for a function's specific parameter in the current scope and then return it's Type
     * -- fetchType(): search in the array, same as the lookup(), it only returns Type */
    public STRecord.Type paramType(String name, int number){
        // take the type from any scope (it may not be local)
	//add by yiannis_sem
        if(this.inLibrary(name)){
            STRecord.Type type = new STRecord.Type();
            type=this.library.get(this.getLibraryFun(name)).getType();
            ArrayList params = new ArrayList();
            params = type.getParameters();
            //System.out.print(params.size());
            if(number>params.size()){
                return null;
            }
            //for(int i = 0; i < params.size(); i++) {
            STRecord.Type type1 = new STRecord.Type();
            type1 = (STRecord.Type) params.get(number-1);//instead of number stack error
            return type1;
                //type1.printType();
                //System.out.print(params.get(i));
            //}
        }
        //till here
        if (this.variableMap.containsKey(name)) {
            //System.out.printf("Name %s found\n", name);
            int index = this.variableMap.get(name);
            STRecord.Type temp = new STRecord.Type(this.symbolTable.get(index).getType());
            STRecord.Type tempParam = temp.fetchParamType(number);
            return tempParam;
        }
        return null;
    }

    /* print our structures
    * -- for debugging */
    public void printSTStructures() {
        //System.out.printf("\nSymbol-Table:\n");
        for (int i = 0; i < this.symbolTable.size(); i++) {
            this.symbolTable.get(i).printSTRecord();
        }
        //System.out.printf("\nScope-Namespace Stack:\n");
        for (int i = 0; i < this.nameStack.size(); i++) {
            this.nameStack.get(i).printNSRecord();
        }
        //System.out.printf("\nVariables' Hash-Map:\n");
        //System.out.println(Arrays.asList(variableMap));
    }

    /* utility functions for nesting scheme */
    public void addNestingLevel() {
        this.nestingScheme.push(new ArrayList<String>());
        this.levelsOfNesting++;
    }
    public ArrayList<String> popCurrentNestingLevel() {
        return this.nestingScheme.pop();    // remove current nesting level temporarily, in order to update it
    }
    public String updateCurrentNestingLevel(String initName) {
        ArrayList<String> temp = this.popCurrentNestingLevel();
        // the "*" is used as a special character to help us have both <name> and <nesting level> with a tokenization
        String tempName = initName + "*" + this.levelsOfNesting;
        temp.add(tempName);
        this.nestingScheme.push(temp);
        this.currentLevelFunction++;
        // add name to the Hash Map
        if (this.nestingMap.containsKey(tempName)) {
            // if we already have a list for this name, just update this list
            ArrayList<Integer> tempList = this.nestingMap.get(tempName);
            tempList.add(this.levelsOfNesting);
            this.nestingMap.put(tempName, tempList);
        } else {
            // if we don't already have a list for this name, then create a list and add it to the Hash Map
            ArrayList<Integer> tempList = new ArrayList<Integer>();
            tempList.add(this.levelsOfNesting);
            this.nestingMap.put(tempName, tempList);
        }

        return tempName;    // we may want to get it back as well
    }
    public void removeNestingLevel() {
        this.nestingScheme.pop();
        this.levelsOfNesting--;
        this.currentLevelFunction = -1; // we want to start over for a potential new level
    }

    /* print our nesting scheme
    * -- for debugging */
    public void printNestingScheme() {
        System.out.printf("\nProgram's functions nesting scheme (%d levels of nesting):\n", this.levelsOfNesting);
        System.out.print(this.nestingScheme);
        System.out.printf("\n");
    }

    //yiannis_sem added null in last parameter dim
    //yiannis_sem added null in last parameter dim
    private void loadGraceLibrary() {
        /* INPUT & OUTPUT FUNCTIONS */
        // add puti
        ArrayList<STRecord.Type> params = new ArrayList<STRecord.Type>();
        STRecord.Type tempParamType = new STRecord.Type("int", false, false, 0, false, null,null);
        params.add(tempParamType);
        STRecord.Type tempType = new STRecord.Type("nothing", false, false, 0, true, params,null);
        STRecord tempRec = new STRecord(tempType, "puti", false, false, false, -1, -1);
        this.library.add(tempRec);
        // add putc
        params = new ArrayList<STRecord.Type>();
        tempParamType = new STRecord.Type("char", false, false, 0, false, null,null);
        params.add(tempParamType);
        tempType = new STRecord.Type("nothing", false, false, 0, true, params,null);
        tempRec = new STRecord(tempType, "putc", false, false, false, -1, -1);
        this.library.add(tempRec);
        // add puts
        params = new ArrayList<STRecord.Type>();
        tempParamType = new STRecord.Type("char", true, true, null, false, null,null);
        params.add(tempParamType);
        tempType = new STRecord.Type("nothing", false, false, 0, true, params,null);
        tempRec = new STRecord(tempType, "puts", false, false, false, -1, -1);
        this.library.add(tempRec);
        // add geti
        params = null;
        tempType = new STRecord.Type("int", false, false, 0, true, params,null);
        tempRec = new STRecord(tempType, "geti", false, false, false, -1, -1);
        this.library.add(tempRec);
        // add getc
        params = null;
        tempType = new STRecord.Type("char", false, false, 0, true, params,null);
        tempRec = new STRecord(tempType, "getc", false, false, false, -1, -1);
        this.library.add(tempRec);
        // add puts
        params = new ArrayList<STRecord.Type>();
        tempParamType = new STRecord.Type("int", false, false, 0, false, null,null);
        params.add(tempParamType);
        tempParamType = new STRecord.Type("char", true, true, null, false, null,null);
        params.add(tempParamType);
        tempType = new STRecord.Type("nothing", false, false, 0, true, params,null);
        tempRec = new STRecord(tempType, "gets", false, false, false, -1, -1);
        this.library.add(tempRec);

        /* CONVERTION FUNCTIONS */
        // add abs
        params = new ArrayList<STRecord.Type>();
        tempParamType = new STRecord.Type("int", false, false, 0, false, null,null);
        params.add(tempParamType);
        tempType = new STRecord.Type("int", false, false, 0, true, params,null);
        tempRec = new STRecord(tempType, "abs", false, false, false, -1, -1);
        this.library.add(tempRec);
        // add ord
        params = new ArrayList<STRecord.Type>();
        tempParamType = new STRecord.Type("char", false, false, 0, false, null,null);
        params.add(tempParamType);
        tempType = new STRecord.Type("int", false, false, 0, true, params,null);
        tempRec = new STRecord(tempType, "ord", false, false, false, -1, -1);
        this.library.add(tempRec);
        // add chr
        params = new ArrayList<STRecord.Type>();
        tempParamType = new STRecord.Type("int", false, false, 0, false, null,null);
        params.add(tempParamType);
        tempType = new STRecord.Type("char", false, false, 0, true, params,null);
        tempRec = new STRecord(tempType, "chr", false, false, false, -1, -1);
        this.library.add(tempRec);

        /* STRING MANAGEMENT FUNCTIONS */
        // add strlen
        params = new ArrayList<STRecord.Type>();
        tempParamType = new STRecord.Type("char", true, true, null, false, null,null);
        params.add(tempParamType);
        tempType = new STRecord.Type("int", false, false, 0, true, params,null);
        tempRec = new STRecord(tempType, "strlen", false, false, false, -1, -1);
        this.library.add(tempRec);
        // add strcmp
        params = new ArrayList<STRecord.Type>();
        tempParamType = new STRecord.Type("char", true, true, null, false, null,null);
        params.add(tempParamType);
        tempParamType = new STRecord.Type("char", true, true, null, false, null,null);
        params.add(tempParamType);
        tempType = new STRecord.Type("int", false, false, 0, true, params,null);
        tempRec = new STRecord(tempType, "strcmp", false, false, false, -1, -1);
        this.library.add(tempRec);
        // add strcpy
        params = new ArrayList<STRecord.Type>();
        tempParamType = new STRecord.Type("char", true, true, null, false, null,null);
        params.add(tempParamType);
        tempParamType = new STRecord.Type("char", true, true, null, false, null,null);
        params.add(tempParamType);
        tempType = new STRecord.Type("nothing", false, false, 0, true, params,null);
        tempRec = new STRecord(tempType, "strcpy", false, false, false, -1, -1);
        this.library.add(tempRec);
        // add strcat
        params = new ArrayList<STRecord.Type>();
        tempParamType = new STRecord.Type("char", true, true, null, false, null,null);
        params.add(tempParamType);
        tempParamType = new STRecord.Type("char", true, true, null, false, null,null);
        params.add(tempParamType);
        tempType = new STRecord.Type("nothing", false, false, 0, true, params,null);
        tempRec = new STRecord(tempType, "strcat", false, false, false, -1, -1);
        this.library.add(tempRec);
    }

    public boolean inLibrary(String funcName) {
        for (int i = 0; i < this.library.size(); i++) {
            // found in library
            if (this.library.get(i).getName().equals(funcName)) return true;
        }
        return false;   // not found in library
    }

	//add by yiannis_sem
    public int getLibraryFun(String funcName) {
        for (int i = 0; i < this.library.size(); i++) {
            // found in library
            if (this.library.get(i).getName().equals(funcName)) return i;
        }
        return -1;   // not found in library
    }
    //till here

}
