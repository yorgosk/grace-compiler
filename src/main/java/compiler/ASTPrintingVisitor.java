package compiler;

import compiler.analysis.DepthFirstAdapter;
import compiler.node.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class ASTPrintingVisitor extends DepthFirstAdapter {
    // for indentation
    private int indent = 0;
    private void makeIndent() { /*for(int i = 0; i < indent; i++) System.out.printf("    ");*/ }

    // Symbol-Table for Syntactical Analysis
    private SymbolTable symbolTable;
    // a Java Stack where various STRecord-related information are stored temporarily
    private Stack<STRecord> tempRecordStack;
    private Integer toPopFromTempRecordStack;
    // a Java Stack where various STRecord's Type-related information are stored temporarily
    private Stack<STRecord.Type> tempTypeStack;
    private Integer toPopFromTempTypeStack;
    private boolean isDecl;
    private boolean hasMain;
    private boolean isFuncCall;
    private  Integer numOfParam;
    // a Java Stack to store the name of the function in which we are at any time
    private Stack<String> tempFunctionStack;

    // we keep our Intermediate Representation, to be used for machine-code generation in the future
    private IntermediateCode ir;
    // a Java Stack from where the label's of quad operands are temporarily stored and retrieved
    private Stack<Integer> tempOperandsStack;
    private Integer toPopFromTempOperandsStack;

    private  boolean hasReturn; //yiannis_sem : to define when a function has return statement
    private String mainName; //yiannis_sem : name of main need to be kept

    // write to file
    private PrintWriter irWriter;
    private PrintWriter assemblyWriter;

    /* exit function, in case of semantic error */
    private void gracefullyExit() {
        // close writers
        this.irWriter.close();
        this.assemblyWriter.close();
        // exit with "failure" code
        System.exit(-1);
    }

    // IN A START------------------------------------------------------------
    @Override
    public void inStart(Start node) {
        // create Symbol-Table and assistant-structures and initialize assistant-variables
        this.symbolTable = new SymbolTable();
        this.tempRecordStack = new Stack<STRecord>();
        this.toPopFromTempRecordStack = 0;
        this.tempTypeStack = new Stack<STRecord.Type>();
        this.toPopFromTempTypeStack = 0;
        this.isDecl = false;
        this.hasMain = false;
        // create the Intermediate Representation storing structures
        this.ir = new IntermediateCode();
        this.tempOperandsStack = new Stack<Integer>();
        this.toPopFromTempOperandsStack = 0;
        this.tempFunctionStack = new Stack<String>();
        // let our IR know of the Types of our library's functions
        ArrayList<STRecord> temp = this.symbolTable.getLibrary();
        for(int i = 0; i < temp.size(); i++) {
            this.ir.addType(temp.get(i).getName(), temp.get(i).getType());
        }
        // start printing IR to file -- for testing
        try {
            this.irWriter = new PrintWriter("intermediateRepresentation.txt", "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // start assembly to file
        try {
            this.assemblyWriter = new PrintWriter("g.s");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // IN AND OUT A PROGRAM------------------------------------------------------------
    @Override
    public void inAProgram(AProgram node) { makeIndent(); /*System.out.printf("program :\n");*/ indent++; }
    @Override
    public void outAProgram(AProgram node) { indent--; System.out.printf("Intermediate Representation:\n");
        //yiannis_sem
        ArrayList<STRecord> KFun = this.symbolTable.getKnownFunctions();
        for(STRecord rec: KFun){
            rec.printSTRecord();
            //System.out.print(rec.getName());
            if(!rec.getDefined()){
                System.err.printf("Error: function \"%s\" has not been defined, only declared\n", rec.getName());
                this.gracefullyExit();
            }
        }
        //till here
        // print IR
        this.ir.printIR();
        //System.out.print("\n-----------------------------------------------------\n\t\tPROGRAM OUTPUT:\n-----------------------------------------------------\n");
        // print IR to file -- for testing
        this.ir.printIR(irWriter);
        // done printing IR to file -- for testing
        this.irWriter.close();
        // print assembly to file
        this.assemblyWriter.print(this.ir.getAssemblyAsString());
        // done printing assembly to file
        this.assemblyWriter.close();
    }

    // IN AND OUT A FUNCTION DEFINITION------------------------------------------------------------
    @Override
    public void inAFuncDef(AFuncDef node) { makeIndent(); /*System.out.printf("function :\n");*/ indent++;
        // we are in a function definition, this means that a new namespace-scope is created
        this.symbolTable.enter();
        // the very next header that we will see, we want to remember that it belongs to a function Definition
        this.isDecl = false;
    }
    @Override
    public void outAFuncDef(AFuncDef node) { indent--; symbolTable.exit();
        String name = this.tempFunctionStack.pop();
        //yiannis_sem
        if(!this.hasReturn&&!this.symbolTable.fetchType(name).getKind().equals("nothing")){
            System.err.printf("Error: function \"%s\" has not a return statement\n",name);
            this.gracefullyExit();
        }
        this.hasReturn=false;
        STRecord rec = new STRecord();
        for(STRecord f:this.symbolTable.getKnownFunctions()){
            if(f.getName().equals(name)&&!name.equals(this.mainName)){
                this.symbolTable.insert(f);

                // pass Symbol Table's Nesting Scheme to the Machine Code's Nesting Scheme
                HashMap<String, ArrayList<Integer>> tempMap = this.symbolTable.getNestingMap();
                this.ir.updateAssemblyNestingMap(tempMap);
                Integer levelsOfNesting = this.symbolTable.getLevelsOfNesting();
                this.ir.updateAssemblyLevelsOfNesting(levelsOfNesting);
            }
        }
        //till here
        // producing IR
        this.ir.GENQUAD("endu", name, "-", "-");

        // producing assembly
        this.ir.addAssemblyCode("pop ebp\n");
        this.ir.addAssemblyCode("ret\n");
    }
    @Override
    public void caseAFuncDef(AFuncDef node)
    {
        inAFuncDef(node);
        if(node.getHeader() != null)
        {
            node.getHeader().apply(this);
        }
        {
            List<PLocalDef> copy = new ArrayList<PLocalDef>(node.getLocalDef());
            for(PLocalDef e : copy)
            {
                e.apply(this);
            }
        }

        if (!this.isDecl) {
            // producing IR
            this.ir.GENQUAD("unit", this.tempFunctionStack.peek(), "-", "-");

            // producing assembly
            String name;
            // we want our first function to be our "main"
            if (this.ir.getCurrentLabel() == 1) name = "main";
            else name = "_"+this.tempFunctionStack.peek()+"_"+this.ir.getAssemblyLevelsOfNesting();
            this.ir.addAssemblyCode(name+":\n");
            this.ir.addAssemblyCode("push ebp\n");
            this.ir.addAssemblyCode("mov ebp, esp\n");
        }
        if(node.getBlock() != null)
        {
            node.getBlock().apply(this);
        }
        outAFuncDef(node);
    }

    // IN AND OUT A HEADER AND ASSISTANT-PRODUCTIONS------------------------------------------------------------
    @Override
    public void inAHeader(AHeader node) { makeIndent(); /*System.out.printf("header(\"%s\") :\n", node.getId().toString().trim().replaceAll("\\s+", " "));*/ indent++; }
    @Override
    public void outAHeader(AHeader node) { indent--;
        // keep the name of the function
        STRecord tempRec = new STRecord();

        STRecord.Type tempType = this.tempTypeStack.pop();
        this.toPopFromTempTypeStack--;
        tempRec.type = new STRecord.Type(tempType);
        //yiannis_sem
        if(this.symbolTable.fetchType(node.getId().toString().trim().replaceAll("\\s+", " "))!=null&&!this.symbolTable.fetchType(node.getId().toString().trim().replaceAll("\\s+", " ")).getFunction()){
            System.err.printf("Error: a variable has already defined whth this name: %s\n",node.getId().toString().trim().replaceAll("\\s+", " "));
            this.gracefullyExit();
        }
        if(!tempType.getKind().equals("nothing")&&!this.hasMain){
            System.err.printf("Error: main function must return nothing\n");
            this.gracefullyExit();
        }
        if(!this.hasMain&&node.getFparDef().size() != 0){
            System.err.printf("Error: \"main\" function takes no arguments\n");
            this.gracefullyExit();
        }
        if(!this.hasMain){
            //System.out.print(node.getId().toString().trim().replaceAll("\\s+", " "));
            this.mainName = node.getId().toString().trim().replaceAll("\\s+", " ");
        }
        //till here

        tempRec.type.setFunction(true);
        // check for main-function existence
        // source: http://stackoverflow.com/questions/17973964/how-to-compare-two-strings-in-java-without-considering-spaces
        if (node.getId().toString().trim().replaceAll("\\s+", " ").equalsIgnoreCase("main".trim().replaceAll("\\s+", " ")) && node.getFparDef().size() != 0) {
            System.err.printf("Error: \"main\" function takes no arguments\n");
            this.gracefullyExit();
        } else if (!this.isDecl && this.hasMain && node.getId().toString().trim().replaceAll("\\s+", " ").equalsIgnoreCase("main".trim().replaceAll("\\s+", " "))) {
            System.err.printf("Error: All Grace programs must have only one \"main\" function\n");
            this.gracefullyExit();
        } else {
            this.hasMain = true;
            tempRec.setName(node.getId().toString().trim().replaceAll("\\s+", " "));

            // for assembly production
            this.ir.addAssemblyCode(".global main\n");
        }

        // insert the header's names to our Symbol-Table
        STRecord temp;
        while (this.toPopFromTempRecordStack != 0) {
            temp = this.tempRecordStack.pop();
            if(!this.isDecl) {  //if added by yiannis_sem : prevents declarations to push params in scopes and never leave, it seems to work fine but may cause problems
                this.symbolTable.insert(temp);

                // pass Symbol Table's Nesting Scheme to the Machine Code's Nesting Scheme
                HashMap<String, ArrayList<Integer>> tempMap = this.symbolTable.getNestingMap();
                this.ir.updateAssemblyNestingMap(tempMap);
                Integer levelsOfNesting = this.symbolTable.getLevelsOfNesting();
                this.ir.updateAssemblyLevelsOfNesting(levelsOfNesting);
            }
            toPopFromTempRecordStack--;
            tempRec.type.addParameter(temp.getType());
        }
        // if we are in a function declaration, check to see if it's definition exists in the current scope, and if it does, do the appropriate Name & Type-checking
        int result = this.symbolTable.searchFunction(tempRec);
        if (this.isDecl) {
            for(STRecord rec: this.symbolTable.getKnownFunctions()){
                //System.out.print(rec.getName());
                if(tempRec.getName().equals(rec.getName())){
                    System.err.printf("Error: multiple declaration of function \"%s\"\n", tempRec.getName());
                    this.gracefullyExit();
                }
            }
            if (result == 0) {
                tempRec.setDefined(false);
                this.symbolTable.insert(tempRec);
                this.symbolTable.addKnownFunction(tempRec);

                // pass Symbol Table's Nesting Scheme to the Machine Code's Nesting Scheme
                HashMap<String, ArrayList<Integer>> tempMap = this.symbolTable.getNestingMap();
                this.ir.updateAssemblyNestingMap(tempMap);
                Integer levelsOfNesting = this.symbolTable.getLevelsOfNesting();
                this.ir.updateAssemblyLevelsOfNesting(levelsOfNesting);

                // for IR production
                this.ir.addType(tempRec.getName(), tempRec.getType());
            }
            // IS THIS AN ERROR???????????????????????????????????????????????????????????????????????????
            else if (result == 1) {
                System.err.printf("Error: function \"%s\" has already been defined\n", tempRec.getName());
                this.gracefullyExit();
            }
            else {
                System.err.printf("Error: function \"%s\" already known under a different type\n", tempRec.getName());
                this.gracefullyExit();
            }
        }else {
            if (result == 0) {
                //yiannis_sem
                if(this.symbolTable.fetchType(tempRec.getName())!=null) {
                    if(!this.symbolTable.fetchType(tempRec.getName()).getKind().equals(tempRec.getType().getKind())){
                        System.err.printf("Error: function \"%s\" has declared in a different type\n", tempRec.getName());
                        this.gracefullyExit();
                    }
                    String[] params = node.getFparDef().toString().split(" ");
                    params[0] = params[0].substring(1);
                    boolean isRef = false;
                    boolean isArray = false;
                    int count = 0;
                    int num = 0;
                    int sum = 0;
                    for (Object i : params) {
                        if (i.toString().equals("ref")) {
                            isRef = true;
                        } else if (i.toString().equals("int")) {
                            for (int k = 0; k < count; k++) {
                                if (this.symbolTable.paramType(tempRec.getName(), num + 1) == null) {
                                    System.err.printf("Error: function \"%s\" has declared in a different way\n", tempRec.getName());
                                    this.gracefullyExit();
                                }
                                if (!this.symbolTable.paramType(tempRec.getName(), num + 1).getKind().equals("int")) {
                                    System.err.printf("Error: function \"%s\" has declared in a different way\n", tempRec.getName());
                                    this.gracefullyExit();
                                }
                                // System.out.print(count);
                                if (params[count + 1].toString().toCharArray()[0] == '0' || params[count + 1].toString().toCharArray()[0] == '1' || params[count + 1].toString().toCharArray()[0] == '2' || params[count + 1].toString().toCharArray()[0] == '3' || params[count + 1].toString().toCharArray()[0] == '4' || params[count + 1].toString().toCharArray()[0] == '5' || params[count + 1].toString().toCharArray()[0] == '6' || params[count + 1].toString().toCharArray()[0] == '7' || params[count + 1].toString().toCharArray()[0] == '8' || params[count + 1].toString().toCharArray()[0] == '9') {
                                    isArray = true;
                                }
                                if (isRef) {
                                    isArray = true;
                                }
                                if (this.symbolTable.paramType(tempRec.getName(), num + 1).getArray() != isArray) {
                                    System.err.printf("Error: function \"%s\" has declared in a different way\n", tempRec.getName());
                                    this.gracefullyExit();
                                }
                                num++;
                            }
                        } else if (i.toString().equals("char")) {
                            for (int k = 0; k < count; k++) {

                                if (this.symbolTable.paramType(tempRec.getName(), num + 1) == null) {
                                    System.err.printf("Error: function \"%s\" has declared in a different way\n", tempRec.getName());
                                    this.gracefullyExit();
                                }
                                if (!this.symbolTable.paramType(tempRec.getName(), num + 1).getKind().equals("char")) {
                                    System.err.printf("Error: function \"%s\" has declared in a different way\n", tempRec.getName());
                                    this.gracefullyExit();
                                }
                                if (params[num + 1].toString().toCharArray()[0] == '0' || params[num + 1].toString().toCharArray()[0] == '1' || params[num + 1].toString().toCharArray()[0] == '2' || params[num + 1].toString().toCharArray()[0] == '3' || params[num + 1].toString().toCharArray()[0] == '4' || params[num + 1].toString().toCharArray()[0] == '5' || params[num + 1].toString().toCharArray()[0] == '6' || params[num + 1].toString().toCharArray()[0] == '7' || params[num + 1].toString().toCharArray()[0] == '8' || params[num + 1].toString().toCharArray()[0] == '9') {
                                    isArray = true;
                                }
                                if (isRef) {
                                    isArray = true;
                                }
                                if (this.symbolTable.paramType(tempRec.getName(), num + 1).getArray() != isArray) {
                                    System.err.printf("Error: function \"%s\" has declared in a different way\n", tempRec.getName());
                                    this.gracefullyExit();
                                }
                                num++;
                            }

                        }else if(i.toString().equals(",")){
                            isRef=false;
                            isArray=false;
                            count=0;
                        } else {
                            count++;
                            sum++;
                        }
                    }
                    if (this.symbolTable.paramType(tempRec.getName(), sum) != null) {
                        System.err.printf("Error: function \"%s\" has declared in a different way\n", tempRec.getName());
                        this.gracefullyExit();
                    }
                }
                else {
                    String[] params = node.getFparDef().toString().split(" ");
                    params[0] = params[0].substring(1);
                    boolean isRef=false;
                    boolean isArray=false;
                    int count=0;
                    for (Object i : params) {
                        if (i.toString().equals("ref")) {
                            isRef = true;
                        }else if(i.toString().equals(",")){
                            if(isArray&&!isRef){
                                System.err.printf("Error: function \"%s\" has reference to a non array argument\n", tempRec.getName());
                                this.gracefullyExit();
                            }
                            isRef=false;
                            isArray=false;
                        }else if (i.toString().equals("int")||i.toString().equals("char")) {
                            if (params[count + 1].toString().toCharArray()[0] == '[' || params[count + 1].toString().toCharArray()[0] == '0' || params[count + 1].toString().toCharArray()[0] == '1' || params[count + 1].toString().toCharArray()[0] == '2' || params[count + 1].toString().toCharArray()[0] == '3' || params[count + 1].toString().toCharArray()[0] == '4' || params[count + 1].toString().toCharArray()[0] == '5' || params[count + 1].toString().toCharArray()[0] == '6' || params[count + 1].toString().toCharArray()[0] == '7' || params[count + 1].toString().toCharArray()[0] == '8' || params[count + 1].toString().toCharArray()[0] == '9') {
                                isArray = true;
                            }
                        }
                        count++;
                    }
                    if(isArray&&!isRef){
                        System.err.printf("Error: function \"%s\" has reference to an non array argument\n", tempRec.getName());
                        this.gracefullyExit();
                    }
                }
                for(STRecord rec:this.symbolTable.getKnownFunctions()){
                    if(rec.getName().equals(tempRec.getName())){
                        rec.setDefined(true);
                    }
                }
                //System.out.print(this.symbolTable.paramType(tempRec.getName(), count+1).getKind());
                //till here
                //System.out.print(this.symbolTable.paramType(tempRec.getName(),0));
                this.symbolTable.insert(tempRec);
                this.symbolTable.setScopeType(tempRec.getType());
                this.symbolTable.addKnownFunction(tempRec);//yiannis_sem???? isws na einai la8os giati exei 3anaginei push i idia sunartisi

                // pass Symbol Table's Nesting Scheme to the Machine Code's Nesting Scheme
                HashMap<String, ArrayList<Integer>> tempMap = this.symbolTable.getNestingMap();
                this.ir.updateAssemblyNestingMap(tempMap);
                Integer levelsOfNesting = this.symbolTable.getLevelsOfNesting();
                this.ir.updateAssemblyLevelsOfNesting(levelsOfNesting);

                // for IR production
                this.ir.addType(tempRec.getName(), tempRec.getType());
                this.tempFunctionStack.push(node.getId().toString().trim().replaceAll("\\s+", " "));
            }
            // IS THIS AN ERROR???????????????????????????????????????????????????????????????????????????
            else if (result == 1) {
                System.err.printf("Error: function \"%s\" has already been defined\n", tempRec.getName());
                this.gracefullyExit();
            }
            else {
                System.err.printf("Error: function \"%s\" already known under a different type\n", tempRec.getName());
                this.gracefullyExit();
            }
        }
        // for debugging
        this.symbolTable.printSTStructures();
        assert (toPopFromTempRecordStack == 0);
    }

    // IN AND OUT A FUNCTION PARAMETERS------------------------------------------------------------
    @Override
    public void inAFparDef(AFparDef node) { makeIndent(); /*System.out.printf("fparDef :\n");*/ indent++; }
    @Override
    public void outAFparDef(AFparDef node) { indent--;
        // keep whether it is a ref or not
        boolean ref = node.getRef() != null;
        STRecord.Type tempType = null;
        // enter the id's in the current scope
        if(node.getId() != null)
        {
            STRecord tempRec = new STRecord();

            tempType = this.tempTypeStack.pop();
            this.toPopFromTempTypeStack--;
            tempRec.type = new STRecord.Type(tempType);
            tempRec.type.setRef(ref);
            tempRec.setName(node.getId().toString().trim().replaceAll("\\s+", " "));
            this.tempRecordStack.push(tempRec);
            this.toPopFromTempRecordStack++;
        }
        {
            List<TId> copy = new ArrayList<TId>(node.getNext());
            for(TId e : copy)
            {
                STRecord tempRec = new STRecord();

                tempRec.type = new STRecord.Type(tempType);
                tempRec.type.setRef(ref);
                tempRec.setName(e.toString().trim().replaceAll("\\s+", " "));
                this.tempRecordStack.push(tempRec);
                this.toPopFromTempRecordStack++;
            }
        }
    }

    // IN AND OUT A DATA TYPE------------------------------------------------------------
    @Override
    public void inAIntDataType(AIntDataType node) { makeIndent(); /*System.out.printf("\"int\"");*/  }
    @Override
    public void outAIntDataType(AIntDataType node) { /*System.out.printf("\n");*/ }
    @Override
    public void inACharDataType(ACharDataType node) { makeIndent(); /*System.out.printf("\"char\"");*/ }
    @Override
    public void outACharDataType(ACharDataType node) { /*System.out.printf("\n");*/ }

    // IN AND OUT A TYPE AND ASSISTANT-STATEMENT------------------------------------------------------------
    @Override
    public void inAType(AType node) { makeIndent(); /*System.out.printf("type :\n");*/ indent++; }
    @Override
    public void outAType(AType node) { indent--;
        STRecord.Type temp = new STRecord.Type();
        temp.setKind(node.getDataType().toString().trim().replaceAll("\\s+", " "));
        if(node.getIntConst().size() > 0) {
            temp.setArray(true);
            temp.setDimension(node.getIntConst().size());
        }
        if(node.getIntConst().size()>1){
            for (int i=1;i<node.getIntConst().size();i++){
                temp.addDimension(Integer.parseInt(node.getIntConst().get(i).toString().trim().replaceAll("\\s+", " ")));
            }
        }
        this.tempTypeStack.push(temp);
        this.toPopFromTempTypeStack++;
    }

    // IN AND OUT A RETURN TYPE AND ASSISTANT-STATEMENTS------------------------------------------------------------
    @Override
    public void inADataTypeRetType(ADataTypeRetType node) { makeIndent(); /*System.out.printf("retType :\n");*/ indent++; }
    @Override
    public void outADataTypeRetType(ADataTypeRetType node) { indent--;
        STRecord.Type temp = new STRecord.Type();
        temp.setKind(node.getDataType().toString().trim().replaceAll("\\s+", " "));
        this.tempTypeStack.push(temp);
        this.toPopFromTempTypeStack++;
    }
    @Override
    public void inANothingRetType(ANothingRetType node) { makeIndent(); /*System.out.printf("retType :\"nothing\"\n");*/ }
    @Override
    public void outANothingRetType(ANothingRetType node) {
        STRecord.Type temp = new STRecord.Type();
        temp.setKind("nothing");
        this.tempTypeStack.push(temp);
        this.toPopFromTempTypeStack++;
    }

    // IN AND OUT A FUNCTION PARAMETER TYPE AND ASSISTANT-STATEMENT------------------------------------------------------------
    @Override
    public void inAFparType(AFparType node) { makeIndent(); /*System.out.printf("funcParType :\n");*/ indent++;
        STRecord.Type temp = new STRecord.Type();
        temp.setKind(node.getDataType().toString().trim().replaceAll("\\s+", " "));
        if (node.getLRBrackets() != null) {
            temp.setArray(true);
            temp.setDimension(1);
        }
        if(node.getIntConst().size() > 0) {
            temp.setArray(true);
            if(node.getLRBrackets()==null) {
                if (node.getIntConst().size() > 1) {
                    for (int i = 1; i < node.getIntConst().size(); i++) {
                        temp.addDimension(Integer.parseInt(node.getIntConst().get(i).toString().trim().replaceAll("\\s+", " ")));
                    }
                }
            }
            else{
                if (node.getIntConst().size() > 0) {
                    for (int i = 0; i < node.getIntConst().size(); i++) {
                        temp.addDimension(Integer.parseInt(node.getIntConst().get(i).toString().trim().replaceAll("\\s+", " ")));
                    }
                }
            }

            int curr_dimension = temp.getDimension();
            temp.setDimension(curr_dimension+node.getIntConst().size());
        }
        this.tempTypeStack.push(temp);
        this.toPopFromTempTypeStack++;
    }
    @Override
    public void outAFparType(AFparType node) { indent--; }

    // IN AND OUT A LOCAL DEFINITION------------------------------------------------------------
    @Override
    public void inAFuncDefLocalDef(AFuncDefLocalDef node) { makeIndent(); /*System.out.printf("funcDefLocalDef :\n");*/ indent++;
        // we are in a function definition, this means that a new namespace-scope is created
        symbolTable.enter();
        // the very next header that we will see, we want to remember that it belongs to a function Definition
        this.isDecl = false;
    }
    @Override
    public void outAFuncDefLocalDef(AFuncDefLocalDef node) { indent--; symbolTable.exit();
        //yiannis_sem

        STRecord rec = new STRecord();
        for(STRecord f:this.symbolTable.getKnownFunctions()){
            //System.out.print(this.mainName);
            if(f.getName().equals(node.getFuncDef().toString().split(" ")[0])&&!node.getFuncDef().toString().split(" ")[0].equals(this.mainName)){
                this.symbolTable.insert(f);

                // pass Symbol Table's Nesting Scheme to the Machine Code's Nesting Scheme
                HashMap<String, ArrayList<Integer>> tempMap = this.symbolTable.getNestingMap();
                this.ir.updateAssemblyNestingMap(tempMap);
                Integer levelsOfNesting = this.symbolTable.getLevelsOfNesting();
                this.ir.updateAssemblyLevelsOfNesting(levelsOfNesting);
            }
        }
        //till here
    }	//changed by yiannis
    @Override
    public void inAFuncDeclLocalDef(AFuncDeclLocalDef node) { makeIndent(); /*System.out.printf("funcDeclLocalDef :\n");*/ indent++;
        // the very next header that we will see, we want to remember that it belongs to a function Declaration
        this.isDecl = true;
    }
    @Override
    public void outAFuncDeclLocalDef(AFuncDeclLocalDef node) { indent--; }
    @Override
    public void inAVarDefLocalDef(AVarDefLocalDef node) { makeIndent(); /*System.out.printf("varDefLocalDef :\n");*/ indent++; }
    @Override
    public void outAVarDefLocalDef(AVarDefLocalDef node) { indent--;
        // insert the variables' names to our Symbol-Table
        STRecord temp;
        while (this.toPopFromTempRecordStack != 0) {
            temp = this.tempRecordStack.pop();

             int result = this.symbolTable.searchFunction(temp);
             if (result == 0) {
                 this.symbolTable.insert(temp);

                 // pass Symbol Table's Nesting Scheme to the Machine Code's Nesting Scheme
                 HashMap<String, ArrayList<Integer>> tempMap = this.symbolTable.getNestingMap();
                 this.ir.updateAssemblyNestingMap(tempMap);
                 Integer levelsOfNesting = this.symbolTable.getLevelsOfNesting();
                 this.ir.updateAssemblyLevelsOfNesting(levelsOfNesting);
             }
             else if (result == 1) {
                 System.err.printf("Error: variable \"%s\" has already been defined\n", temp.getName());
                 this.gracefullyExit();
             }
             else {
             System.err.printf("Error: function \"%s\" already known under a different type\n", temp.getName());
                 this.gracefullyExit();
             }

           // this.symbolTable.insert(temp);	commented by yiannis
            this.toPopFromTempRecordStack--;
        }
        // for debugging
//        this.symbolTable.printSTStructures();
        assert (this.toPopFromTempRecordStack == 0);
    }

    // IN AND OUT A VARIABLE DEFINITION AND ASSISTANT-STATEMENT------------------------------------------------------------
    @Override
    public void inAVarDef(AVarDef node) { makeIndent();/* System.out.printf("var :\"%s\"\n", node.getId().toString().trim().replaceAll("\\s+", " "));*/ indent++; }
    @Override
    public void outAVarDef(AVarDef node) { indent--;
        // keep the name of the parameters
        String type = node.getType().toString().trim().replaceAll("\\s+", " ");
        STRecord.Type tempType = null;
        if(node.getId() != null)
        {
            STRecord tempRec = new STRecord();

            tempType = this.tempTypeStack.pop();
            this.toPopFromTempTypeStack--;
            tempRec.type = new STRecord.Type(tempType);
            tempRec.setName(node.getId().toString().trim().replaceAll("\\s+", " "));
            this.tempRecordStack.push(tempRec);
            this.toPopFromTempRecordStack++;
        }
        {
            List<TId> copy = new ArrayList<TId>(node.getNext());
            for(TId e : copy)
            {
                STRecord tempRec = new STRecord();

                tempRec.type = new STRecord.Type(tempType);
                tempRec.type.setKind(type);
                tempRec.setName(e.toString().trim().replaceAll("\\s+", " "));
                this.tempRecordStack.push(tempRec);
                this.toPopFromTempRecordStack++;
            }
        }
    }

    // IN AND OUT A FUNCTION DECLARATION------------------------------------------------------------
    @Override
    public void inAFuncDecl(AFuncDecl node) { makeIndent(); /*System.out.printf("funcDecl :\n");*/ indent++; }
    @Override
    public void outAFuncDecl(AFuncDecl node) { indent--; }

    // IN AND OUT A CODE BLOCK------------------------------------------------------------
    @Override
    public void inABlock(ABlock node) { makeIndent(); /*System.out.printf("code-block body :\n");*/ indent++;}
    @Override
    public void outABlock(ABlock node) { indent--; }
    @Override
    public void caseABlock(ABlock node)
    {
        inABlock(node);

        // for IR production
        int numOfStmt = 1;
        int stmtNEXTLabel = -1;

        {
            List<PStmt> copy = new ArrayList<PStmt>(node.getStmt());
            for(PStmt e : copy)
            {
                // for IR production
                numOfStmt++;

                e.apply(this);

                // for IR production
                if (numOfStmt == 1) stmtNEXTLabel = this.ir.getCurrentLabel();
                else if (numOfStmt == 2) stmtNEXTLabel = this.ir.getCurrentLabel();
            }
        }

        outABlock(node);
    }

    // IN AND OUT A FUNCTION CALL AND ASSISTANT-STATEMENTS------------------------------------------------------------
    @Override
    public void inAFuncCall(AFuncCall node) { makeIndent(); /*System.out.printf("func-call( \"%s\" ) :\n", node.getId().toString().trim().replaceAll("\\s+", " "));*/ indent++;
        //add by yiannis_sem
        String funName = node.getId().toString().trim().replaceAll("\\s+", " ");
        LinkedList paramList = node.getExpr();
        int c=0;int size;
        //this.symbolTable.printscope();
        if(this.symbolTable.lookup(funName)&&!this.symbolTable.inLibrary(funName)){
            System.err.printf("Error: function %s has not been declared in this scope\n",funName);
            this.gracefullyExit();
        }
        if(this.symbolTable.fetchType(funName)==null){
            System.err.printf("Error: function %s has not been declared\n",funName);
            this.gracefullyExit();
        }
        if(this.symbolTable.fetchType(funName).getParameters()!=null){
            size = this.symbolTable.fetchType(funName).getParameters().size();
        }
        else{
            size = 0;
        }

        if(paramList.size()!=size){
            System.err.printf("Error: function %s has %d arguments, %d given\n",funName,size,paramList.size());
            this.gracefullyExit();
        }

        boolean cannotRef=false;
        for(Object i : paramList){
            cannotRef=false;
            String n=i.toString().split(" ")[0].trim().replaceAll("\\s+", " ");
            String[] narr = i.toString().split(" ");
            STRecord.Type type1 = new STRecord.Type();
            type1=this.symbolTable.fetchType(n);
            if(type1!=null){
                if(type1.getArray()) {//this is woarking wright or causing more problems?????????????
                    if (narr.length > 1) {
                        if (this.symbolTable.fetchType(narr[1])!=null&&this.symbolTable.fetchType(narr[1]).getKind().equals("int") || narr[1].toCharArray()[0] == '0' || narr[1].toCharArray()[0] == '1' || narr[1].toCharArray()[0] == '2' || narr[1].toCharArray()[0] == '3' || narr[1].toCharArray()[0] == '4' || narr[1].toCharArray()[0] == '5' || narr[1].toCharArray()[0] == '6' || narr[1].toCharArray()[0] == '7' || narr[1].toCharArray()[0] == '8' || narr[1].toCharArray()[0] == '9') {
                            type1.setArray(false);
                            type1.setDimension(0);//may cause errors
                        }
                    }
                }
            }
            else{
                if(n.toCharArray()[0]=='\''){
                    //it is a character
                    cannotRef=true;
                    type1 = new STRecord.Type();
                    type1.setArray(false);
                    type1.setDimension(0);//may cause errors
                    type1.setRef(false);
                    type1.setKind("char");
                }
                else if (n.toCharArray()[0]=='\"'){
                    type1 = new STRecord.Type();
                    type1.setArray(true);
                    //System.out.print(i.toString());
                    String[] t = i.toString().substring(1).split("\"")[1].split(" ");
                    if(t.length==2){
                        type1.setArray(false);
                    }
                    else if(t.length > 2){
                        System.err.printf("Error: more iterators than the dimensions\n");
                        this.gracefullyExit();
                    }
                    type1.setRef(true);
                    type1.setKind("char");
                }
                else if(n.toCharArray()[0]=='0' || n.toCharArray()[0]=='1' || n.toCharArray()[0]=='2' || n.toCharArray()[0]=='3' || n.toCharArray()[0]=='4' || n.toCharArray()[0]=='5' || n.toCharArray()[0]=='6' || n.toCharArray()[0]=='7' || n.toCharArray()[0]=='8' || n.toCharArray()[0]=='9'){
                    cannotRef=true;
                    type1 = new STRecord.Type();
                    type1.setArray(false);
                    type1.setRef(false);
                    type1.setKind("int");
                }
                else{
                    System.err.printf("Error: %s has not been declared\n",n);
                    this.gracefullyExit();
                }
            }
            STRecord.Type type2;
            type2 = this.symbolTable.fetchType(funName).getParameters().get(c);

            if(type2==null){
                //commented segment to avoid errors
                System.err.printf("Error: function %s has not been declared\n",funName);
                this.gracefullyExit();
            }
            else {
                c++;
                if(type1.getDimension()!=null&&type2.getDimension()!=null) {
                    if (type1.getDimension() > type2.getDimension()) {
                        System.err.printf("Error: trying to pass array wrong number of dimension %s\n", n);
                        this.gracefullyExit();
                    }
                }
                if(type1.getDimension()>1){
                    ArrayList dims1 = type1.getDims();
                    ArrayList dims2 = type2.getDims();
                    for(int j=0;j<type1.getDimension()-1;j++){
                        //System.out.print(dims1.get(j));
                        if(dims1.get(j)!=dims2.get(j)){
                            System.err.printf("Error: wrong dimension in array %s\n", n);
                            this.gracefullyExit();
                        }
                    }
                }
                if(type2.getRef()&&cannotRef){
                    System.err.printf("Error: cannot pass reference in %s\n", n);
                    this.gracefullyExit();
                }

                if (!type1.getKind().equals(type2.getKind())) {

                    System.err.printf("Error: parameter %d is %s, %s expected\n", c, type1.getKind(), type2.getKind());
                    this.gracefullyExit();
                }
                if (type1.getArray() != type2.getArray()) {
                    if (type1.getArray()) {
                        System.err.printf("Error: parameter %d is %s array, %s expected\n", c, type1.getKind(), type2.getKind());
                    } else {
                        System.err.printf("Error: parameter %d is %s, %s array expected\n", c, type1.getKind(), type2.getKind());
                    }
                    this.gracefullyExit();
                }

            }
        }
    }
    @Override
    public void outAFuncCall(AFuncCall node) { indent--; }
    @Override
    public void caseAFuncCall(AFuncCall node)
    {
        inAFuncCall(node);

        // for IR production
        int n = 1;

        if(node.getId() != null)
        {
            node.getId().apply(this);
        }
        {
            List<PExpr> copy = new ArrayList<PExpr>(node.getExpr());
            for(PExpr e : copy)
            {
                e.apply(this);

                // producing IR
                if(!tempOperandsStack.empty()) {//this if added by yiannis_sem , it solve problem with stack that crash but may create wrong quads
                    Integer t1 = tempOperandsStack.pop();
                    this.toPopFromTempOperandsStack--;
                   // System.out.printf("searching param %d of %s", n, node.getId().toString());
                    this.ir.GENQUAD("par", this.ir.getPLACE(t1), this.ir.PARAMMODE(node.getId().toString().trim().replaceAll("\\s+", " "), n), "-");
                    n++;

                    // producing assembly
                    if (this.ir.PARAMMODE(node.getId().toString().trim().replaceAll("\\s+", " "), n).equals("V")) {
                        this.ir.load("ax", this.ir.getPLACE(t1));
                        this.ir.addAssemblyCode("push ax\n");
                    } else if (this.ir.PARAMMODE(node.getId().toString().trim().replaceAll("\\s+", " "), n).equals("R") || this.ir.PARAMMODE(node.getId().toString().trim().replaceAll("\\s+", " "), n).equals("RET")) {
//                        this.ir.loadAddr("si", this.ir.getPLACE(t1));
//                        this.ir.addAssemblyCode("push si\n");
                    } else {
                        assert (false); // we don't want to end up here
                    }
                }
            }
        }

        outAFuncCall(node);

        // producing IR
        STRecord.Type funcType = this.symbolTable.fetchType(node.getId().toString().trim().replaceAll("\\s+", " "));
        assert (funcType != null);
        //System.out.printf("Type:\n");
        this.tempTypeStack.push(funcType);
        this.toPopFromTempTypeStack++;
        funcType.printType();
        if (!funcType.getKind().equals("nothing")) {
            String w = this.ir.NEWTEMP(funcType);
            this.ir.GENQUAD("par", "RET", w, "-");
            this.ir.addPLACE(this.ir.getCurrentLabel(), w);

            // producing assembly
//            this.ir.loadAddr("si", w);
//            this.ir.addAssemblyCode("push si\n");
        }

        this.ir.GENQUAD("call", "-", "-", node.getId().toString().trim().replaceAll("\\s+", " "));

        // producing assembly
//        this.ir.addAssemblyCode("sub esp, 2\n");
//        this.ir.updateAL();
//        this.ir.addAssemblyCode("call near ptr "+node.getId().toString().trim().replaceAll("\\s+", " ")+"\n");
        this.ir.addAssemblyCode("call "+node.getId().toString().trim().replaceAll("\\s+", " ")+"\n");
        this.ir.addAssemblyCode("add esp, 4\n");
        this.ir.addAssemblyCode("mov eax, 0\n");
    }

    // IN AND OUT A STATEMENT AND ASSISTANT-STATEMENTS------------------------------------------------------------
    @Override
    public void inAAssignmentStmt(AAssignmentStmt node) {}
    @Override
    public void outAAssignmentStmt(AAssignmentStmt node) {
        STRecord.Type type = new STRecord.Type();
        type = this.symbolTable.fetchType(node.getLValue().toString().trim().replaceAll("\\s+", " "));
        if(type!=null){
            if(type.getArray()){
                System.err.printf("Error: Trying to assign a value to an array: %s\n",node.getLValue().toString().trim().replaceAll("\\s+", " "));
                this.gracefullyExit();
            }
        }

        STRecord.Type temp1 = this.tempTypeStack.pop();
        this.toPopFromTempTypeStack--;
        STRecord.Type temp2 = this.tempTypeStack.pop();
        this.toPopFromTempTypeStack--;
        //yiannis_sem
        String[] dim = node.getLValue().toString().split(" ");
        if((dim.length-1)==temp1.getDimension()){
            temp1.setArray(false);
        }
        if((dim.length-1)<temp1.getDimension()){
            System.err.printf("Error: Trying to assign a value to an array: %s\n",node.getLValue().toString().trim().replaceAll("\\s+", " "));
            this.gracefullyExit();
        }
        //till here

	//added by yiannis_sem
        //commented segment to avoid errors
        if(temp1.getArray()!=temp2.getArray()){
            String[] splitted = node.getExpr().toString().split(" ");

            if(this.symbolTable.fetchType(splitted[0])!=null&&this.symbolTable.fetchType(splitted[0]).getArray()){
                if(splitted.length<=1){
                    System.err.printf("Error: Trying to assign an array : %s to a non array value: %s\n", temp1.getKind(), temp2.getKind());
                    this.gracefullyExit();
                }
                else {
                    if (((this.symbolTable.fetchType(splitted[1]) != null && this.symbolTable.fetchType(splitted[1]).getKind().equals("int")) || splitted[1].toCharArray()[0] == '0' || splitted[1].toCharArray()[0] == '1' || splitted[1].toCharArray()[0] == '2' || splitted[1].toCharArray()[0] == '3' || splitted[1].toCharArray()[0] == '4' || splitted[1].toCharArray()[0] == '5' || splitted[1].toCharArray()[0] == '6' || splitted[1].toCharArray()[0] == '7' || splitted[1].toCharArray()[0] == '8' || splitted[1].toCharArray()[0] == '9')) {

                    } else {
                        System.err.printf("Error: Trying to assign an array : %s to a non array value: %s\n", temp1.getKind(), temp2.getKind());
                        this.gracefullyExit();
                    }
                }
            }
        }
        if(temp2.getKind().equals("string")) {
            String[] t = node.getExpr().toString().substring(1).split("\"")[1].split(" ");
            if (temp2.getKind().equals("string") && t.length == 2) {
                temp2.setKind("char");
            }
            else if(t.length > 2){
                System.err.printf("Error: more iterators than the dimensions\n");
                this.gracefullyExit();
            }
        }
        //till here
        if (!temp1.isSame(temp2, "assignment")) {
            System.err.printf("Error: In \"assignment\" statement one member is %s and the other member is %s\n",
                    temp1.getKind(), temp2.getKind());
            this.gracefullyExit();
        }
        // if they are of the same type, then the result of the expression between them is going to be of the same type
        STRecord.Type temp3 = new STRecord.Type(temp1);
        this.tempTypeStack.push(temp3);
        this.toPopFromTempTypeStack++;

        // producing IR
        //System.out.print(this.ir.getLastTemp());
        //yiannis3
        String item1 = this.ir.getLastTemp();
        String item2 = this.ir.getLastTemp();
        this.ir.pushTemp(item1);
        this.ir.GENQUAD(":=",item1,"-",item2);
        //till here

        // producing assembly
        STRecord tempRec = new STRecord();
        tempRec.setType(temp3);
        this.ir.setDataMapping(item1, tempRec);
        this.ir.load("R", item1);
        this.ir.store("R", item1);
    }
    @Override
    public void caseAAssignmentStmt(AAssignmentStmt node)
    {
        inAAssignmentStmt(node);
        if(node.getExpr() != null)
        {
            node.getExpr().apply(this);
        }
        if(node.getLValue() != null)
        {
            node.getLValue().apply(this);
        }
        outAAssignmentStmt(node);
    }
    @Override
    public void inABlockStmt(ABlockStmt node) {}
    @Override
    public void outABlockStmt(ABlockStmt node) {
        // producing IR
//        this.ir.setNEXT(this.ir.getCurrentLabel(), this.ir.EMPTYLIST());
    }
    @Override
    public void inAIfStmt(AIfStmt node) {}
    @Override
    public void outAIfStmt(AIfStmt node) {}
    @Override
    public void caseAIfStmt(AIfStmt node)
    {
        inAIfStmt(node);
        if(node.getCond() != null)
        {
            node.getCond().apply(this);
        }

        // for IR production
        int condTRUELabel = this.ir.getCurrentLabel()-1;    // "-1" because there is the jump, -, -, ? in the middle
        int condFALSELabel = this.ir.getCurrentLabel();
        this.ir.BACKPATCH(condTRUELabel, "TRUE", this.ir.NEXTQUAD());

        Integer stmtNEXT;

        if(node.getThenM() != null)
        {
            node.getThenM().apply(this);

        }
        {

            this.ir.GENQUAD("jump", "-", "-", "?");

            // for assembly production
            this.ir.addAssemblyCode("jmp ?\n");
            this.ir.syncLabels();   // sync labels of jump statement

            stmtNEXT = this.ir.getCurrentLabel();
            this.ir.setNEXT(stmtNEXT, this.ir.MAKELIST(stmtNEXT));
            this.ir.BACKPATCH(condFALSELabel, "FALSE", this.ir.NEXTQUAD());

            List<PStmt> copy = new ArrayList<PStmt>(node.getElseM());
            for(PStmt e : copy)
            {
                e.apply(this);
            }

        }

        // for IR production
        this.ir.BACKPATCH(stmtNEXT, "NEXT", this.ir.NEXTQUAD());

        outAIfStmt(node);
    }
    @Override
    public void inAWhileStmt(AWhileStmt node) {}
    @Override
    public void outAWhileStmt(AWhileStmt node) {}
    @Override
    public void caseAWhileStmt(AWhileStmt node)
    {
        inAWhileStmt(node);

        if(node.getCond() != null)
        {
            node.getCond().apply(this);
        }

        // for IR production
        int condTRUELabel = this.ir.getCurrentLabel()-1;    // "-1" because there is the jump, -, -, ? in the middle
        int condFALSELabel = this.ir.getCurrentLabel();

        this.ir.BACKPATCH(condTRUELabel, "TRUE", this.ir.NEXTQUAD());

        if(node.getStmt() != null)
        {
            node.getStmt().apply(this);
        }

        // for IR production
        int stmtLabel = this.ir.getCurrentLabel();
        Integer next = this.ir.NEXTQUAD()+1;
        this.ir.GENQUAD("jump", "-", "-", next.toString());
        this.ir.BACKPATCH(condFALSELabel, "FALSE", next);

        // for assembly production
        this.ir.addAssemblyCode("jmp "+next.toString()+"\n");   // PROBLEM -- FIX IT!!!

        outAWhileStmt(node);
    }
    @Override
    public void inAFunctionStmt(AFunctionStmt node) {}
    @Override
    public void outAFunctionStmt(AFunctionStmt node) {
        // producing IR
//        this.ir.setNEXT(this.ir.getCurrentLabel(), this.ir.EMPTYLIST());
    }
    @Override
    public void inAReturnStmt(AReturnStmt node) {}
    @Override
    public void outAReturnStmt(AReturnStmt node) {
        //yiannis_sem
        this.hasReturn=true;
        STRecord.Type temp = new STRecord.Type();
        if(node.getExpr().size()==0){
            temp.setKind("nothing");
        }
        else {
            temp = this.tempTypeStack.pop();
            this.toPopFromTempTypeStack--;
        }
        //till here
        if(!this.symbolTable.checkRetType(temp)){
		//yiannis_sem
            String[] arr = node.getExpr().toString().split(" ");
            if(arr.length>1 && (this.symbolTable.fetchType(arr[1])!=null||arr[1].toCharArray()[0]=='0'||arr[1].toCharArray()[0]=='1'||arr[1].toCharArray()[0]=='2'||arr[1].toCharArray()[0]=='3'||arr[1].toCharArray()[0]=='4'||arr[1].toCharArray()[0]=='5'||arr[1].toCharArray()[0]=='6'||arr[1].toCharArray()[0]=='7'||arr[1].toCharArray()[0]=='8'||arr[1].toCharArray()[0]=='9')){

            }
            else {
                System.err.printf("Error: function has different return type\n");
                this.gracefullyExit();
            }
            //till here
        }

        // producing IR
        this.ir.GENQUAD("ret", "-", "-", "-");

        // producing assembly
        this.ir.addAssemblyCode("ret\n");   // IS IT OK THIS WAY?
        /*
        * jump to endof(current)
        * */
    }

    // IN AND OUT A L-VALUE AND ASSISTANT-STATEMENTS------------------------------------------------------------
    @Override
    public void inAIdLValue(AIdLValue node) { makeIndent(); /*System.out.printf("\"%s\"\n", node.getId().toString().trim().replaceAll("\\s+", " "));*/ }
    @Override
    public void outAIdLValue(AIdLValue node) {
        STRecord.Type temp = this.symbolTable.fetchType(node.getId().toString().trim().replaceAll("\\s+", " "));

        if (temp != null) { // if temp exists in the current scope
            temp.printType();
            this.tempTypeStack.push(temp);
            this.toPopFromTempTypeStack++;
        } else {    // if temp doesn't exist in the current scope, we have an error
            System.err.printf("Error: id \"%s\" unknown in it's scope\n", node.getId().toString().trim().replaceAll("\\s+", " "));
            this.gracefullyExit();
        }

        // producing IR
        String str = node.getId().toString().trim().replaceAll("\\s+", " ");
        //modified by yiannis3
        this.ir.pushTemp(str);

        //till here
        this.ir.addPLACE(this.ir.getCurrentLabel(), str);//changed by yiannis3
        this.tempOperandsStack.push(this.ir.getCurrentLabel());
        this.toPopFromTempOperandsStack++;
    }
    @Override
    public void inAStringLValue(AStringLValue node) { makeIndent(); /*System.out.printf("\"%s\"\n", node.getStringLiteral().toString().trim().replaceAll("\\s+", " "));*/ }
    @Override
    public void outAStringLValue(AStringLValue node) {
	//yiannis2 : prepei na pairnei to onoma tis metavlitis kai na vlepei an einai ontws pinakas, antistoixa kai sto int
        STRecord.Type temp = new STRecord.Type();
        temp.setKind("string");
        this.tempTypeStack.push(temp);
        this.toPopFromTempTypeStack++;

        // producing IR
        String str = node.getStringLiteral().toString().trim().replaceAll("\\s+", " ");
        //added by yiannis3!
        this.ir.pushTemp(str);
        this.ir.addPLACE(this.ir.getCurrentLabel(), str);
        //till here
        //commented by yiannis3!
        this.tempOperandsStack.push(this.ir.getCurrentLabel());
        this.toPopFromTempOperandsStack++;

        // producing assembly
        this.ir.addAssemblyCode("mov eax, OFFSET FLAT:fmt\n");
        this.ir.addAssemblyCode("push eax\n");
        this.ir.addAssemblyData("fmt: .asciz "+str+"\n");
    }
    @Override
    public void inAExpressionLValue(AExpressionLValue node) { makeIndent(); /*System.out.printf("exprLValue :\n");*/ }
    @Override
    public void outAExpressionLValue(AExpressionLValue node) {
        //yiannis_sem
        if(this.symbolTable.fetchType(node.getLValue().toString().trim().replaceAll("\\s+", " "))!=null&&this.symbolTable.fetchType(node.getLValue().toString().trim().replaceAll("\\s+", " ")).getArray()){
            String[] ind = node.getExpr().toString().split(" ");
            for(int i=0;i<this.symbolTable.fetchType(node.getLValue().toString().trim().replaceAll("\\s+", " ")).getDimension();i++){
                if(ind.length>i&&this.symbolTable.fetchType(ind[i])!=null&&this.symbolTable.fetchType(ind[i]).getArray()){
                    System.err.printf("Error: cannot navigate in l-value using array value (%s)\n", ind[i]);
                    this.gracefullyExit();
                }
            }
        }
        //till here

        // take the <expr>'s type from the <l-value>[<expr>] structure
        STRecord.Type tempExpr = this.tempTypeStack.pop();
        this.toPopFromTempTypeStack--;
        if (!tempExpr.getKind().equals("int")) {
            System.err.printf("Error: cannot navigate in l-value using a \"%s\" type\n", tempExpr.getKind());
            this.gracefullyExit();
        }

        // producing IR
        Integer l1 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        String t1 = this.ir.getLastTemp();  //changed by yiannis3 (it was getPLACE(l1) )
        String t3 = this.ir.getLastTemp();
        String t2 = this.ir.NEWTEMP(tempExpr);
        String temp = this.ir.getLastTemp();//added by yiannis3
        this.ir.pushTemp("["+temp+"]");//added by yiannis3 it changes last element from $.. to [$..] to pop it after in this format
        //String par1 = "["+t1+"]";                                       // problematic -- FIX IT//commented by yiannis3
        this.ir.GENQUAD("array", t1, t3, t2);      //changed by yiannis3 (array was := and t1 was par1 and t3 was "-")
        this.ir.addPLACE(this.ir.getCurrentLabel(), t2);
        this.tempOperandsStack.push(this.ir.getCurrentLabel());
        this.toPopFromTempOperandsStack++;

        //added by yiannis_fin
        // producing assembly
        STRecord tempRecX = new STRecord();                     //anti na kanei pop tis metavlites tis dimiourgei me vasi ta stoixeia tou node
        tempRecX.setName(node.getLValue().toString().trim().replaceAll("\\s+", " "));
        tempRecX.setType(this.symbolTable.fetchType(node.getLValue().toString().trim().replaceAll("\\s+", " ")));
        STRecord tempRecY = new STRecord();
        tempRecY.setName(node.getExpr().toString().trim().replaceAll("\\s+", " "));
        if(this.symbolTable.fetchType(node.getExpr().toString().split(" ")[0])!=null) {
            tempRecY.setType(this.symbolTable.fetchType(node.getExpr().toString().split(" ")[0]));
        }
        else {
            tempRecY.getType().setKind("int");          //ousiastika 3eroume oti to deuterou pou einai deiktis 8a einai int alliws 8a eixe stupisei idi error apo to semantic
        }

        this.ir.setDataMapping(t1, tempRecX);
        tempRecY.setDereference(true);
        this.ir.setDataMapping(t3, tempRecY);
        this.ir.load("eax", t3);
        this.ir.addAssemblyCode("mov ecx, 8\n");
        this.ir.addAssemblyCode("imul ecx\n");
        this.ir.loadAddr("ecx", t1);
        this.ir.addAssemblyCode("add eax, ecx\n");
        STRecord tempRecZ = new STRecord();//ftiaxnei mia metavliti na kanei push sto map giati einai kainourgia kai den einai dilwmeni kai skaei otan paei na kanei to store
        tempRecZ.setName(t2);
        tempRecZ.setType(this.symbolTable.fetchType(node.getLValue().toString().trim().replaceAll("\\s+", " ")));//8a einai idiou typou me to array alla xwris na einai array
        //tempRecZ.setDereference(true);//nomizw oti einai swsto kai xreiaetai
        tempRecZ.getType().setArray(false);
        tempRecZ.getType().setDimension(0);
        this.ir.setDataMapping(t2,tempRecZ);
        this.ir.store("eax", t2);
        //till here
    }

    // IN AND OUT A EXPRESSION AND ASSISTANT-STATEMENTS------------------------------------------------------------
    @Override
    public void inAIntConstExpr(AIntConstExpr node) {}
    @Override
    public void outAIntConstExpr(AIntConstExpr node) {
        STRecord.Type temp = new STRecord.Type();
        temp.setKind("int");
        this.tempTypeStack.push(temp);
        this.toPopFromTempTypeStack++;

        // producting IR
        String str = node.getIntConst().toString().trim().replaceAll("\\s+", " ");
        this.ir.pushTemp(str);//added by yiannis3
        this.ir.addPLACE(this.ir.getCurrentLabel(), str);//changed by yiannis3
        this.tempOperandsStack.push(this.ir.getCurrentLabel());
        this.toPopFromTempOperandsStack++;
    }
    @Override
    public void inACharConstExpr(ACharConstExpr node) {}
    @Override
    public void outACharConstExpr(ACharConstExpr node) {
        STRecord.Type temp = new STRecord.Type();
        temp.setKind("char");
        this.tempTypeStack.push(temp);
        this.toPopFromTempTypeStack++;

        //producing IR
        String str = node.getCharConst().toString().trim().replaceAll("\\s+", " ");
        this.ir.pushTemp(str);//added by yiannis3!
        this.ir.addPLACE(this.ir.getCurrentLabel(), str);//changed by yiannis3!
        this.tempOperandsStack.push(this.ir.getCurrentLabel());
        this.toPopFromTempOperandsStack++;
    }
    @Override
    public void inALValueExpr(ALValueExpr node) {}
    @Override
    public void outALValueExpr(ALValueExpr node) {
        // the l-value-as-an-expression's Type is going to be the same as the l-value's
        STRecord.Type temp = new STRecord.Type(this.tempTypeStack.pop());
        this.toPopFromTempTypeStack--;
        this.tempTypeStack.push(temp);
        this.toPopFromTempTypeStack++;

        // producing IR
        Integer l1 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;

        this.tempOperandsStack.push(this.ir.getCurrentLabel());
        this.toPopFromTempOperandsStack++;
    }
    @Override
    public void inAFuncCallExpr(AFuncCallExpr node) {}
    @Override
    public void outAFuncCallExpr(AFuncCallExpr node) {
        // the func-call-as-an-expression's Type is going to be the same as the func-call's
        STRecord.Type temp = new STRecord.Type(this.tempTypeStack.pop());
        this.toPopFromTempTypeStack--;
        this.tempTypeStack.push(temp);
        this.toPopFromTempTypeStack++;
    }
    @Override
    public void inAExprExpr(AExprExpr node) {}
    @Override
    public void outAExprExpr(AExprExpr node) {}
    @Override
    public void inAPlusExpr(APlusExpr node) {}
    @Override
    public void outAPlusExpr(APlusExpr node) {
        STRecord.Type temp1 = this.tempTypeStack.pop();
        this.toPopFromTempTypeStack--;
        STRecord.Type temp2 = this.tempTypeStack.pop();
        this.toPopFromTempTypeStack--;
        if (!temp1.isSame(temp2, "expression")) {
            System.err.printf("Error: In \"plus\" expression one member is %s and the other member is %s\n",
                    temp1.getKind(), temp2.getKind());
            this.gracefullyExit();
        }
	//yiannis_sem
        if(temp1.getKind().equals("char")){
            System.err.printf("Error: In \"plus\" expression the two members are chars or strings\n");
            this.gracefullyExit();
        }
        //till here
        // if they are of the same type, then the result of the expression between them is going to be of the same type
        STRecord.Type temp3 = new STRecord.Type(temp1);
        this.tempTypeStack.push(temp3);
        this.toPopFromTempTypeStack++;

        // producing IR
        Integer l1 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        Integer l2 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        String t1 = this.ir.getLastTemp();//this amd below line added by yiannis3
        String t2 = this.ir.getLastTemp();
        String t3 = this.ir.NEWTEMP(temp1);
        this.ir.GENQUAD("+", t2, t1, t3);
        this.ir.addPLACE(this.ir.getCurrentLabel(), t3);
        this.tempOperandsStack.push(this.ir.getCurrentLabel());
        this.toPopFromTempOperandsStack++;

        // producing assembly
        STRecord tempRecX = new STRecord();
        tempRecX.setType(temp2);
        this.ir.setDataMapping(t2, tempRecX);
        STRecord tempRecY = new STRecord();
        tempRecY.setType(temp1);
        this.ir.setDataMapping(t1, tempRecY);
        this.ir.load("eax", t2);
        this.ir.load("edx", t1);
        this.ir.addAssemblyCode("add eax, edx\n");
        STRecord tempRecZ = new STRecord();
        tempRecZ.setType(temp3);
        this.ir.setDataMapping(t3, tempRecZ);
        this.ir.store("eax", t3);
    }
    @Override
    public void inAMinusExpr(AMinusExpr node) {}
    @Override
    public void outAMinusExpr(AMinusExpr node) {
        STRecord.Type temp1 = this.tempTypeStack.pop();
        this.toPopFromTempTypeStack--;
        STRecord.Type temp2 = this.tempTypeStack.pop();
        this.toPopFromTempTypeStack--;
        if (!temp1.isSame(temp2, "expression")) {
            System.err.printf("Error: In \"minus\" expression one member is %s and the other member is %s\n",
                    temp1.getKind(), temp2.getKind());
            this.gracefullyExit();
        }
	//yiannis_sem
        if(temp1.getKind().equals("char")){
            System.err.printf("Error: In \"minus\" expression the two members are chars or strings\n");
            this.gracefullyExit();
        }
        //till here
        // if they are of the same type, then the result of the expression between them is going to be of the same type
        STRecord.Type temp3 = new STRecord.Type(temp1);
        this.tempTypeStack.push(temp3);
        this.toPopFromTempTypeStack++;

        // producing IR
        Integer l1 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        Integer l2 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        String t1 = this.ir.getLastTemp();  //this and below line added by yiannis3
        String t2 = this.ir.getLastTemp();
        String t3 = this.ir.NEWTEMP(temp1);
        this.ir.GENQUAD("-", t2, t1, t3);
        this.ir.addPLACE(this.ir.getCurrentLabel(), t3);
        this.tempOperandsStack.push(this.ir.getCurrentLabel());
        this.toPopFromTempOperandsStack++;

        // producing assembly
        STRecord tempRecX = new STRecord();
        tempRecX.setType(temp2);
        this.ir.setDataMapping(t2, tempRecX);
        STRecord tempRecY = new STRecord();
        tempRecY.setType(temp1);
        this.ir.setDataMapping(t1, tempRecY);
        this.ir.load("eax", t2);
        this.ir.load("edx", t1);
        this.ir.addAssemblyCode("sub eax, edx\n");
        STRecord tempRecZ = new STRecord();
        tempRecZ.setType(temp3);
        this.ir.setDataMapping(t3, tempRecZ);
        this.ir.store("eax", t3);
    }
    @Override
    public void inAMultExpr(AMultExpr node) {}
    @Override
    public void outAMultExpr(AMultExpr node) {
        STRecord.Type temp1 = this.tempTypeStack.pop();
        this.toPopFromTempTypeStack--;
        STRecord.Type temp2 = this.tempTypeStack.pop();
        this.toPopFromTempTypeStack--;
        if (!temp1.isSame(temp2, "expression")) {
            System.err.printf("Error: In \"mult\" expression one member is %s and the other member is %s\n",
                    temp1.getKind(), temp2.getKind());
            this.gracefullyExit();
        }
	//yiannis_sem
        if(temp1.getKind().equals("char")){
            System.err.printf("Error: In \"mult\" expression the two members are chars or strings\n");
            this.gracefullyExit();
        }
        //till here
        // if they are of the same type, then the result of the expression between them is going to be of the same type
        STRecord.Type temp3 = new STRecord.Type(temp1);
        this.tempTypeStack.push(temp3);
        this.toPopFromTempTypeStack++;

        // producing IR
        Integer l1 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        Integer l2 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        String t1 = this.ir.getLastTemp();//this and below line added by yiannis3!
        String t2 = this.ir.getLastTemp();
        String t3 = this.ir.NEWTEMP(temp1);
        this.ir.GENQUAD("*", t2, t1, t3);
        this.ir.addPLACE(this.ir.getCurrentLabel(), t3);
        this.tempOperandsStack.push(this.ir.getCurrentLabel());
        this.toPopFromTempOperandsStack++;

        // producing assembly
        STRecord tempRecX = new STRecord();
        tempRecX.setType(temp2);
        this.ir.setDataMapping(t2, tempRecX);
        STRecord tempRecY = new STRecord();
        tempRecY.setType(temp1);
        this.ir.setDataMapping(t1, tempRecY);
        this.ir.load("eax", t2);
        this.ir.load("ecx", t1);
        this.ir.addAssemblyCode("imul ecx\n");
        STRecord tempRecZ = new STRecord();
        tempRecZ.setType(temp3);
        this.ir.setDataMapping(t3, tempRecZ);
        this.ir.store("eax", t3);
    }
    @Override
    public void inADivExpr(ADivExpr node) {}
    @Override
    public void outADivExpr(ADivExpr node) {
        STRecord.Type temp1 = this.tempTypeStack.pop();
        this.toPopFromTempTypeStack--;
        STRecord.Type temp2 = this.tempTypeStack.pop();
        this.toPopFromTempTypeStack--;
        if (!temp1.isSame(temp2, "expression")) {
            System.err.printf("Error: In \"div\" expression one member is %s and the other member is %s\n",
                    temp1.getKind(), temp2.getKind());
            this.gracefullyExit();
        }
	//yiannis_sem
        if(temp1.getKind().equals("char")){
            System.err.printf("Error: In \"div\" expression the two members are chars or strings\n");
            this.gracefullyExit();
        }
        //till here
        // if they are of the same type, then the result of the expression between them is going to be of the same type
        STRecord.Type temp3 = new STRecord.Type(temp1);
        this.tempTypeStack.push(temp3);
        this.toPopFromTempTypeStack++;

        // producing IR
        Integer l1 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        Integer l2 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        String t1 = this.ir.getLastTemp();//this and below line added by yiannis3!
        String t2 = this.ir.getLastTemp();
        String t3 = this.ir.NEWTEMP(temp1);
        this.ir.GENQUAD("div", t2, t1, t3);
        this.ir.addPLACE(this.ir.getCurrentLabel(), t3);
        this.tempOperandsStack.push(this.ir.getCurrentLabel());
        this.toPopFromTempOperandsStack++;

        // producing assembly
        STRecord tempRecX = new STRecord();
        tempRecX.setType(temp2);
        this.ir.setDataMapping(t2, tempRecX);
        STRecord tempRecY = new STRecord();
        tempRecY.setType(temp1);
        this.ir.setDataMapping(t1, tempRecY);
        this.ir.load("eax", t2);
        this.ir.addAssemblyCode("cwd\n");
        this.ir.load("ecx", t1);
        this.ir.addAssemblyCode("idiv ecx");
        STRecord tempRecZ = new STRecord();
        tempRecZ.setType(temp3);
        this.ir.setDataMapping(t3, tempRecZ);
        this.ir.store("eax", t3);
    }
    @Override
    public void inADivisionExpr(ADivisionExpr node) {}
    @Override
    public void outADivisionExpr(ADivisionExpr node) {
        STRecord.Type temp1 = this.tempTypeStack.pop();
        this.toPopFromTempTypeStack--;
        STRecord.Type temp2 = this.tempTypeStack.pop();
        this.toPopFromTempTypeStack--;
        if (!temp1.isSame(temp2, "expression")) {
            System.err.printf("Error: In \"division\" expression one member is %s and the other member is %s\n",
                    temp1.getKind(), temp2.getKind());
            this.gracefullyExit();
        }
	//yiannis_sem
        if(temp1.getKind().equals("char")){
            System.err.printf("Error: In \"division\" expression the two members are chars or strings\n");
            this.gracefullyExit();
        }
        //till here
        // if they are of the same type, then the result of the expression between them is going to be of the same type
        STRecord.Type temp3 = new STRecord.Type(temp1);
        this.tempTypeStack.push(temp3);
        this.toPopFromTempTypeStack++;

        // producing IR
        Integer l1 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        Integer l2 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        String t1 = this.ir.getLastTemp();//this and below line added by yiannis3!
        String t2 = this.ir.getLastTemp();
        String t3 = this.ir.NEWTEMP(temp1);
        this.ir.GENQUAD("/", t2, t1, t3);
        this.ir.addPLACE(this.ir.getCurrentLabel(), t3);
        this.tempOperandsStack.push(this.ir.getCurrentLabel());
        this.toPopFromTempOperandsStack++;

        // producing assembly
        STRecord tempRecX = new STRecord();
        tempRecX.setType(temp2);
        this.ir.setDataMapping(t2, tempRecX);
        STRecord tempRecY = new STRecord();
        tempRecY.setType(temp1);
        this.ir.setDataMapping(t1, tempRecY);
        this.ir.load("eax", t2);
        this.ir.addAssemblyCode("cwd\n");
        this.ir.load("ecx", t1);
        this.ir.addAssemblyCode("idiv ecx");
        STRecord tempRecZ = new STRecord();
        tempRecZ.setType(temp3);
        this.ir.setDataMapping(t3, tempRecZ);
        this.ir.store("eax", t3);
    }
    @Override
    public void inAModExpr(AModExpr node) {}
    @Override
    public void outAModExpr(AModExpr node) {
        STRecord.Type temp1 = this.tempTypeStack.pop();
        this.toPopFromTempTypeStack--;
        STRecord.Type temp2 = this.tempTypeStack.pop();
        this.toPopFromTempTypeStack--;
        if (!temp1.isSame(temp2, "expression")) {
            System.err.printf("Error: In \"mod\" expression one member is \"%s\" and the other member is \"%s\"\n",
                    temp1.getKind(), temp2.getKind());
            this.gracefullyExit();
        }
	//yiannis_sem
        if(temp1.getKind().equals("char")){
            System.err.printf("Error: In \"mod\" expression the two members are chars or strings\n");
            this.gracefullyExit();
        }
        //till here
        // if they are of the same type, then the result of the expression between them is going to be of the same type
        STRecord.Type temp3 = new STRecord.Type(temp1);
        this.tempTypeStack.push(temp3);
        this.toPopFromTempTypeStack++;

        // producing IR
        Integer l1 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        Integer l2 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        String t1 = this.ir.getLastTemp();//this and below line added by yiannis3!
        String t2 = this.ir.getLastTemp();
        String t3 = this.ir.NEWTEMP(temp1);
        this.ir.GENQUAD("mod", t2, t1, t3);
        this.ir.addPLACE(this.ir.getCurrentLabel(), t3);
        this.tempOperandsStack.push(this.ir.getCurrentLabel());
        this.toPopFromTempOperandsStack++;

        // producing assembly
        STRecord tempRecX = new STRecord();
        tempRecX.setType(temp2);
        this.ir.setDataMapping(t2, tempRecX);
        STRecord tempRecY = new STRecord();
        tempRecY.setType(temp1);
        this.ir.setDataMapping(t1, tempRecY);
        this.ir.load("eax", t2);
        this.ir.addAssemblyCode("cwd\n");
        this.ir.load("ecx", t1);
        this.ir.addAssemblyCode("idiv ecx");
        STRecord tempRecZ = new STRecord();
        tempRecZ.setType(temp3);
        this.ir.setDataMapping(t3, tempRecZ);
        this.ir.store("edx", t3);
    }
    @Override
    public void inASignedExpr(ASignedExpr node) {}
    @Override
    public void outASignedExpr(ASignedExpr node) {
        // producing IR
        // we mostly care about the minus "-" sign case
        if(node.getSign().toString().trim().replaceAll("\\s+", " ").equals("-")) {
            Integer l1 = this.tempOperandsStack.pop();
            this.toPopFromTempOperandsStack--;
            String t1 = this.ir.getPLACE(l1);
            STRecord.Type temp1 = this.tempTypeStack.peek();
            String t2 = this.ir.NEWTEMP(temp1);
            this.ir.GENQUAD("-", "0", t1, t2);
            this.ir.addPLACE(this.ir.getCurrentLabel(), t2);
            this.tempOperandsStack.push(this.ir.getCurrentLabel());
            this.toPopFromTempOperandsStack++;

            // producing assembly
            STRecord tempRecY = new STRecord();
            tempRecY.setType(temp1);
            this.ir.setDataMapping(t1, tempRecY);
            this.ir.load("eax", "0");
            this.ir.load("edx", t1);
            this.ir.addAssemblyCode("sub eax, edx\n");
            STRecord tempRecZ = new STRecord();
            tempRecZ.setType(temp1);
            this.ir.setDataMapping(t2, tempRecZ);
            this.ir.store("eax", t2);
        }
    }

    // IN AND OUT A CONDITION AND ASSISTANT-STATEMENTS------------------------------------------------------------
    @Override
    public void inACondCond(ACondCond node) {}
    @Override
    public void outACondCond(ACondCond node) {
        // for later stages of IR production
        String w = this.ir.NEWTEMP(null);
        Integer condTRUELabel = this.ir.getCurrentLabel()-2;
        this.ir.BACKPATCH(condTRUELabel, "TRUE", this.ir.NEXTQUAD());
        this.ir.GENQUAD(":=", "true", "-", w);
        Integer q = this.ir.NEXTQUAD()+2;
        this.ir.GENQUAD("jump", "-", "-", q.toString());
        Integer condFALSE = this.ir.getCurrentLabel()-2;
        this.ir.BACKPATCH(condFALSE, "FALSE", this.ir.NEXTQUAD());
        this.ir.GENQUAD(":=", "false", "-", w);
        this.ir.addPLACE(this.ir.getCurrentLabel(), w);
    }
    @Override
    public void inANotCond(ANotCond node) {}
    @Override
    public void outANotCond(ANotCond node) {
        // producing IR
        Integer l1 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;

        this.tempOperandsStack.push(this.ir.getCurrentLabel());
        this.toPopFromTempOperandsStack++;

        // for IR production
        ArrayList<Integer> cond1TRUE = this.ir.getTRUE(this.ir.getCurrentLabel()-1);
        ArrayList<Integer> cond1FALSE = this.ir.getFALSE(this.ir.getCurrentLabel());
        this.ir.setTRUE(this.ir.getCurrentLabel()-1, cond1FALSE);
        this.ir.setFALSE(this.ir.getCurrentLabel(), cond1TRUE);
    }
    @Override
    public void inAAndCond(AAndCond node) {}
    @Override
    public void outAAndCond(AAndCond node) {
        // producing IR
        Integer l1 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        Integer l2 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
    }
    @Override
    public void caseAAndCond(AAndCond node)
    {
        inAAndCond(node);
        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
        }

        // for IR production
        Integer cond1TRUELabel = this.ir.getCurrentLabel()-1;
        Integer cond1FALSELabel = this.ir.getCurrentLabel();
        this.ir.BACKPATCH(cond1TRUELabel, "TRUE", this.ir.NEXTQUAD());
        ArrayList<Integer> cond1FALSE = this.ir.getFALSE(cond1FALSELabel);

        if(node.getRight() != null)
        {
            node.getRight().apply(this);
        }

        // for IR production
        Integer cond2TRUELabel = this.ir.getCurrentLabel()-1;
        Integer cond2FALSELabel = this.ir.getCurrentLabel();
        ArrayList<Integer> cond2FALSE = this.ir.getFALSE(cond2FALSELabel);
        ArrayList<Integer> cond2TRUE = this.ir.getTRUE(cond2TRUELabel);

        outAAndCond(node);

        // for IR production
        Integer condTRUELabel = this.ir.getCurrentLabel()-1;
        this.ir.setTRUE(condTRUELabel, cond2TRUE);
        ArrayList<ArrayList<Integer>> tempList = new ArrayList<ArrayList<Integer>>();
        tempList.add(cond1FALSE);
        tempList.add(cond2FALSE);
        Integer condFALSELabel = this.ir.getCurrentLabel();
        this.ir.setFALSE(condFALSELabel, this.ir.MERGE(tempList));
    }
    @Override
    public void inAOrCond(AOrCond node) {}
    @Override
    public void outAOrCond(AOrCond node) {
        // producing IR
        Integer l1 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        Integer l2 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
    }
    @Override
    public void caseAOrCond(AOrCond node)
    {
        inAOrCond(node);
        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
        }

        // for IR production
        Integer cond1TRUELabel = this.ir.getCurrentLabel()-1;
        Integer cond1FALSELabel = this.ir.getCurrentLabel();
        this.ir.BACKPATCH(cond1TRUELabel, "TRUE", this.ir.NEXTQUAD());
        ArrayList<Integer> cond1TRUE = this.ir.getTRUE(cond1TRUELabel);
        ArrayList<Integer> cond1FALSE = this.ir.getFALSE(cond1FALSELabel);

        if(node.getRight() != null)
        {
            node.getRight().apply(this);
        }

        // for IR production
        Integer cond2TRUELabel = this.ir.getCurrentLabel()-1;
        Integer cond2FALSELabel = this.ir.getCurrentLabel();
        ArrayList<Integer> cond2FALSE = this.ir.getFALSE(cond2FALSELabel);
        ArrayList<Integer> cond2TRUE = this.ir.getTRUE(cond2TRUELabel);

        outAOrCond(node);

        // for IR production
        Integer condFALSELabel = this.ir.getCurrentLabel();
        this.ir.setFALSE(condFALSELabel, cond2FALSE);
        ArrayList<ArrayList<Integer>> tempTRUEList = new ArrayList<ArrayList<Integer>>();
        tempTRUEList.add(cond1TRUE);
        tempTRUEList.add(cond2TRUE);
        Integer condTRUELabel = this.ir.getCurrentLabel()-1;
        this.ir.setTRUE(condTRUELabel, this.ir.MERGE(tempTRUEList));
    }
    @Override
    public void inAEqualCond(AEqualCond node) {}
    @Override
    public void outAEqualCond(AEqualCond node) {
        STRecord.Type temp1 = this.tempTypeStack.pop();
        this.toPopFromTempTypeStack--;
        STRecord.Type temp2 = this.tempTypeStack.pop();
        this.toPopFromTempTypeStack--;
        temp1.printType();
        temp2.printType();
        if (!temp1.isSame(temp2, "condition")) {
            System.err.printf("Error: In condition one member is \"%s\" and the other member is \"%s\"\n",
                    temp1.getKind(), temp2.getKind());
            this.gracefullyExit();
        }
	//yiannis_sem
        //System.out.print(node.getLeft());
        String[] valsl = node.getLeft().toString().split(" ");
        String[] valsr = node.getRight().toString().split(" ");
        if(valsl[0].toCharArray()[0]=='"'){
            System.err.printf("Error: In condition cannot compare literals\n");
            this.gracefullyExit();
        }

        if(this.symbolTable.fetchType(valsl[0])!=null&&this.symbolTable.fetchType(valsl[0]).getArray()){
            if(valsl.length>1) {
                if (this.symbolTable.fetchType(valsl[1]) != null) {

                } else if (valsl[1].toCharArray()[0] != '0' && valsl[1].toCharArray()[0] != '1' && valsl[1].toCharArray()[0] != '2' && valsl[1].toCharArray()[0] != '3' && valsl[1].toCharArray()[0] != '4' && valsl[1].toCharArray()[0] != '5' && valsl[1].toCharArray()[0] != '6' && valsl[1].toCharArray()[0] != '7' && valsl[1].toCharArray()[0] != '8' && valsl[1].toCharArray()[0] != '9') {
                    System.err.printf("Error: In condition cannot compare arrays\n");
                    this.gracefullyExit();
                }
            }
            else{
                System.err.printf("Error: In condition cannot compare arrays\n");
                this.gracefullyExit();
            }
        }
        if(this.symbolTable.fetchType(valsr[0])!=null&&this.symbolTable.fetchType(valsr[0]).getArray()){
            if(valsr.length>1) {
                if (this.symbolTable.fetchType(valsr[1]) != null) {

                } else if (valsr[1].toCharArray()[0] != '0' && valsr[1].toCharArray()[0] != '1' && valsr[1].toCharArray()[0] != '2' && valsr[1].toCharArray()[0] != '3' && valsr[1].toCharArray()[0] != '4' && valsr[1].toCharArray()[0] != '5' && valsr[1].toCharArray()[0] != '6' && valsr[1].toCharArray()[0] != '7' && valsr[1].toCharArray()[0] != '8' && valsr[1].toCharArray()[0] != '9') {
                    System.err.printf("Error: In condition cannot compare arrays\n");
                    this.gracefullyExit();
                }
            }
            else{
                System.err.printf("Error: In condition cannot compare arrays\n");
                this.gracefullyExit();
            }
        }
        //till here

        // producing IR
        Integer l1 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        Integer l2 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        String t1 = this.ir.getPLACE(l1);
        String t2 = this.ir.getPLACE(l2);
        this.ir.GENQUAD("=", t2, t1, "?");
        this.tempOperandsStack.push(this.ir.getCurrentLabel());
        this.toPopFromTempOperandsStack++;

        // producing assembly
        STRecord tempRecX = new STRecord();
        tempRecX.setType(temp2);
        this.ir.setDataMapping(t2, tempRecX);
        STRecord tempRecY = new STRecord();
        tempRecY.setType(temp1);
        this.ir.setDataMapping(t1, tempRecY);
        this.ir.load("eax", t2);
        this.ir.load("edx", t1);
        this.ir.addAssemblyCode("cmp eax, edx\n");
        this.ir.addAssemblyCode("jz ?\n");
        this.ir.syncLabels();   // sync labels of jump statement

        // for later stages of IR production
        this.ir.setTRUE(this.ir.getCurrentLabel(), this.ir.MAKELIST(this.ir.getCurrentLabel()));
        this.ir.setFALSE(this.ir.getCurrentLabel()+1, this.ir.MAKELIST(this.ir.NEXTQUAD()));
        this.ir.GENQUAD("jump", "-", "-", "?");

        // producing assembly
        this.ir.addAssemblyCode("jmp ?\n");
        this.ir.syncLabels();
    }
    @Override
    public void inAHashtagCond(AHashtagCond node) {}
    @Override
    public void outAHashtagCond(AHashtagCond node) {
        STRecord.Type temp1 = this.tempTypeStack.pop();
        this.toPopFromTempTypeStack--;
        STRecord.Type temp2 = this.tempTypeStack.pop();
        this.toPopFromTempTypeStack--;
        temp1.printType();
        temp2.printType();
        if (!temp1.isSame(temp2, "condition")) {
            System.err.printf("Error: In condition one member is \"%s\" and the other member is \"%s\"\n",
                    temp1.getKind(), temp2.getKind());
            this.gracefullyExit();
        }
	//yiannis_sem
        //System.out.print(node.getLeft());
        String[] valsl = node.getLeft().toString().split(" ");
        String[] valsr = node.getRight().toString().split(" ");
        if(valsl[0].toCharArray()[0]=='"'){
            System.err.printf("Error: In condition cannot compare literals\n");
            this.gracefullyExit();
        }
       // STRecord.Type typel =this.symbolTable.fetchType(valsl[0]);
        if(this.symbolTable.fetchType(valsl[0])!=null&&this.symbolTable.fetchType(valsl[0]).getArray()){
            if(valsl.length>1) {
                if (this.symbolTable.fetchType(valsl[1]) != null) {

                } else if (valsl[1].toCharArray()[0] != '0' && valsl[1].toCharArray()[0] != '1' && valsl[1].toCharArray()[0] != '2' && valsl[1].toCharArray()[0] != '3' && valsl[1].toCharArray()[0] != '4' && valsl[1].toCharArray()[0] != '5' && valsl[1].toCharArray()[0] != '6' && valsl[1].toCharArray()[0] != '7' && valsl[1].toCharArray()[0] != '8' && valsl[1].toCharArray()[0] != '9') {
                    System.err.printf("Error: In condition cannot compare arrays\n");
                    this.gracefullyExit();
                }
            }
            else{
                System.err.printf("Error: In condition cannot compare arrays\n");
                this.gracefullyExit();
            }
        }
        if(this.symbolTable.fetchType(valsr[0])!=null&&this.symbolTable.fetchType(valsr[0]).getArray()){
            if(valsr.length>1) {
                if (this.symbolTable.fetchType(valsr[1]) != null) {

                } else if (valsr[1].toCharArray()[0] != '0' && valsr[1].toCharArray()[0] != '1' && valsr[1].toCharArray()[0] != '2' && valsr[1].toCharArray()[0] != '3' && valsr[1].toCharArray()[0] != '4' && valsr[1].toCharArray()[0] != '5' && valsr[1].toCharArray()[0] != '6' && valsr[1].toCharArray()[0] != '7' && valsr[1].toCharArray()[0] != '8' && valsr[1].toCharArray()[0] != '9') {
                    System.err.printf("Error: In condition cannot compare arrays\n");
                    this.gracefullyExit();
                }
            }
            else{
                System.err.printf("Error: In condition cannot compare arrays\n");
                this.gracefullyExit();
            }
        }
        //till here

        // producing IR
        Integer l1 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        Integer l2 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        String t1 = this.ir.getPLACE(l1);
        String t2 = this.ir.getPLACE(l2);
        this.ir.GENQUAD("#", t2, t1, "?");
        this.tempOperandsStack.push(this.ir.getCurrentLabel());
        this.toPopFromTempOperandsStack++;

        // producing assembly
        STRecord tempRecX = new STRecord();
        tempRecX.setType(temp2);
        this.ir.setDataMapping(t2, tempRecX);
        STRecord tempRecY = new STRecord();
        tempRecY.setType(temp1);
        this.ir.setDataMapping(t1, tempRecY);
        this.ir.load("eax", t2);
        this.ir.load("edx", t1);
        this.ir.addAssemblyCode("cmp eax, dx\n");
        this.ir.addAssemblyCode("jnz ?\n");
        this.ir.syncLabels();   // sync labels of jump statement

        // for later stages of IR production
        this.ir.setTRUE(this.ir.getCurrentLabel(), this.ir.MAKELIST(this.ir.getCurrentLabel()));
        this.ir.setFALSE(this.ir.getCurrentLabel()+1, this.ir.MAKELIST(this.ir.NEXTQUAD()));
        this.ir.GENQUAD("jump", "-", "-", "?");

        // producing assembly
        this.ir.addAssemblyCode("jmp ?\n");
        this.ir.syncLabels();
    }
    @Override
    public void inAUnequalCond(AUnequalCond node) {}
    @Override
    public void outAUnequalCond(AUnequalCond node) {
        STRecord.Type temp1 = this.tempTypeStack.pop();
        this.toPopFromTempTypeStack--;
        STRecord.Type temp2 = this.tempTypeStack.pop();
        this.toPopFromTempTypeStack--;
        temp1.printType();
        temp2.printType();
        if (!temp1.isSame(temp2, "condition")) {
            System.err.printf("Error: In condition one member is \"%s\" and the other member is \"%s\"\n",
                    temp1.getKind(), temp2.getKind());
            this.gracefullyExit();
        }
	//yiannis_sem
        //System.out.print(node.getLeft());
        String[] valsl = node.getLeft().toString().split(" ");
        String[] valsr = node.getRight().toString().split(" ");
        if(valsl[0].toCharArray()[0]=='"'){
            System.err.printf("Error: In condition cannot compare literals\n");
            this.gracefullyExit();
        }
        // STRecord.Type typel =this.symbolTable.fetchType(valsl[0]);
        if(this.symbolTable.fetchType(valsl[0])!=null&&this.symbolTable.fetchType(valsl[0]).getArray()){
            if(valsl.length>1) {
                if (this.symbolTable.fetchType(valsl[1]) != null) {

                } else if (valsl[1].toCharArray()[0] != '0' && valsl[1].toCharArray()[0] != '1' && valsl[1].toCharArray()[0] != '2' && valsl[1].toCharArray()[0] != '3' && valsl[1].toCharArray()[0] != '4' && valsl[1].toCharArray()[0] != '5' && valsl[1].toCharArray()[0] != '6' && valsl[1].toCharArray()[0] != '7' && valsl[1].toCharArray()[0] != '8' && valsl[1].toCharArray()[0] != '9') {
                    System.err.printf("Error: In condition cannot compare arrays\n");
                    this.gracefullyExit();
                }
            }
            else{
                System.err.printf("Error: In condition cannot compare arrays\n");
                this.gracefullyExit();
            }
        }
        if(this.symbolTable.fetchType(valsr[0])!=null&&this.symbolTable.fetchType(valsr[0]).getArray()){
            if(valsr.length>1) {
                if (this.symbolTable.fetchType(valsr[1]) != null) {

                } else if (valsr[1].toCharArray()[0] != '0' && valsr[1].toCharArray()[0] != '1' && valsr[1].toCharArray()[0] != '2' && valsr[1].toCharArray()[0] != '3' && valsr[1].toCharArray()[0] != '4' && valsr[1].toCharArray()[0] != '5' && valsr[1].toCharArray()[0] != '6' && valsr[1].toCharArray()[0] != '7' && valsr[1].toCharArray()[0] != '8' && valsr[1].toCharArray()[0] != '9') {
                    System.err.printf("Error: In condition cannot compare arrays\n");
                    this.gracefullyExit();
                }
            }
            else{
                System.err.printf("Error: In condition cannot compare arrays\n");
                this.gracefullyExit();
            }
        }
        //till here

        // producing IR
        Integer l1 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        Integer l2 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        String t1 = this.ir.getPLACE(l1);
        String t2 = this.ir.getPLACE(l2);
        this.ir.GENQUAD("<>", t2, t1, "?");
        this.toPopFromTempOperandsStack++;

        // producing assembly
        STRecord tempRecX = new STRecord();
        tempRecX.setType(temp2);
        this.ir.setDataMapping(t2, tempRecX);
        STRecord tempRecY = new STRecord();
        tempRecY.setType(temp1);
        this.ir.setDataMapping(t1, tempRecY);
        this.ir.load("eax", t2);
        this.ir.load("edx", t1);
        this.ir.addAssemblyCode("cmp eax, edx\n");
        this.ir.addAssemblyCode("jnz ?\n");
        this.ir.syncLabels();   // sync labels of jump statement

        // for later stages of IR production
        this.ir.setTRUE(this.ir.getCurrentLabel(), this.ir.MAKELIST(this.ir.getCurrentLabel()));
        this.ir.setFALSE(this.ir.getCurrentLabel()+1, this.ir.MAKELIST(this.ir.NEXTQUAD()));
        this.ir.GENQUAD("jump", "-", "-", "?");

        // producing assembly
        this.ir.addAssemblyCode("jmp ?\n");
        this.ir.syncLabels();
    }
    @Override
    public void inALesserCond(ALesserCond node) {}
    @Override
    public void outALesserCond(ALesserCond node) {
        STRecord.Type temp1 = this.tempTypeStack.pop();
        this.toPopFromTempTypeStack--;
        STRecord.Type temp2 = this.tempTypeStack.pop();
        this.toPopFromTempTypeStack--;
        temp1.printType();
        temp2.printType();
        if (!temp1.isSame(temp2, "condition")) {
            System.err.printf("Error: In condition one member is \"%s\" and the other member is \"%s\"\n",
                    temp1.getKind(), temp2.getKind());
            this.gracefullyExit();
        }
	//yiannis_sem
        //System.out.print(node.getLeft());
        String[] valsl = node.getLeft().toString().split(" ");
        String[] valsr = node.getRight().toString().split(" ");
        if(valsl[0].toCharArray()[0]=='"'){
            System.err.printf("Error: In condition cannot compare literals\n");
            this.gracefullyExit();
        }
        // STRecord.Type typel =this.symbolTable.fetchType(valsl[0]);
        if(this.symbolTable.fetchType(valsl[0])!=null&&this.symbolTable.fetchType(valsl[0]).getArray()){
            if(valsl.length>1) {
                if (this.symbolTable.fetchType(valsl[1]) != null) {

                } else if (valsl[1].toCharArray()[0] != '0' && valsl[1].toCharArray()[0] != '1' && valsl[1].toCharArray()[0] != '2' && valsl[1].toCharArray()[0] != '3' && valsl[1].toCharArray()[0] != '4' && valsl[1].toCharArray()[0] != '5' && valsl[1].toCharArray()[0] != '6' && valsl[1].toCharArray()[0] != '7' && valsl[1].toCharArray()[0] != '8' && valsl[1].toCharArray()[0] != '9') {
                    System.err.printf("Error: In condition cannot compare arrays\n");
                    this.gracefullyExit();
                }
            }
            else{
                System.err.printf("Error: In condition cannot compare arrays\n");
                this.gracefullyExit();
            }
        }
        if(this.symbolTable.fetchType(valsr[0])!=null&&this.symbolTable.fetchType(valsr[0]).getArray()){
            if(valsr.length>1) {
                if (this.symbolTable.fetchType(valsr[1]) != null) {

                } else if (valsr[1].toCharArray()[0] != '0' && valsr[1].toCharArray()[0] != '1' && valsr[1].toCharArray()[0] != '2' && valsr[1].toCharArray()[0] != '3' && valsr[1].toCharArray()[0] != '4' && valsr[1].toCharArray()[0] != '5' && valsr[1].toCharArray()[0] != '6' && valsr[1].toCharArray()[0] != '7' && valsr[1].toCharArray()[0] != '8' && valsr[1].toCharArray()[0] != '9') {
                    System.err.printf("Error: In condition cannot compare arrays\n");
                    this.gracefullyExit();
                }
            }
            else{
                System.err.printf("Error: In condition cannot compare arrays\n");
                this.gracefullyExit();
            }
        }
        //till here

        // producing IR
        Integer l1 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        Integer l2 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        String t1 = this.ir.getLastTemp();
        String t2 = this.ir.getLastTemp();
        this.ir.GENQUAD("<", t2, t1, "?");
        this.tempOperandsStack.push(this.ir.getCurrentLabel());
        this.toPopFromTempOperandsStack++;

        // producing assembly
        STRecord tempRecX = new STRecord();
        tempRecX.setType(temp2);
        this.ir.setDataMapping(t2, tempRecX);
        STRecord tempRecY = new STRecord();
        tempRecY.setType(temp1);
        this.ir.setDataMapping(t1, tempRecY);
        this.ir.load("eax", t2);
        this.ir.load("edx", t1);
        this.ir.addAssemblyCode("cmp eax, edx\n");
        this.ir.addAssemblyCode("jl ?\n");
        this.ir.syncLabels();   // sync labels of jump statement

        // for later stages of IR production
        this.ir.setTRUE(this.ir.getCurrentLabel(), this.ir.MAKELIST(this.ir.getCurrentLabel()));
        this.ir.setFALSE(this.ir.getCurrentLabel()+1, this.ir.MAKELIST(this.ir.NEXTQUAD()));
        this.ir.GENQUAD("jump", "-", "-", "?");

        // producing assembly
        this.ir.addAssemblyCode("jmp ?\n");
        this.ir.syncLabels();
    }
    @Override
    public void inAGreaterCond(AGreaterCond node) {}
    @Override
    public void outAGreaterCond(AGreaterCond node) {
        STRecord.Type temp1 = this.tempTypeStack.pop();
        this.toPopFromTempTypeStack--;
        STRecord.Type temp2 = this.tempTypeStack.pop();
        this.toPopFromTempTypeStack--;
        temp1.printType();
        temp2.printType();
        if (!temp1.isSame(temp2, "condition")) {
            System.err.printf("Error: In condition one member is \"%s\" and the other member is \"%s\"\n",
                    temp1.getKind(), temp2.getKind());
            this.gracefullyExit();
        }
	//yiannis_sem
        //System.out.print(node.getLeft());
        String[] valsl = node.getLeft().toString().split(" ");
        String[] valsr = node.getRight().toString().split(" ");
        if(valsl[0].toCharArray()[0]=='"'){
            System.err.printf("Error: In condition cannot compare literals\n");
            this.gracefullyExit();
        }
        // STRecord.Type typel =this.symbolTable.fetchType(valsl[0]);
        if(this.symbolTable.fetchType(valsl[0])!=null&&this.symbolTable.fetchType(valsl[0]).getArray()){
            if(valsl.length>1) {
                if (this.symbolTable.fetchType(valsl[1]) != null) {

                } else if (valsl[1].toCharArray()[0] != '0' && valsl[1].toCharArray()[0] != '1' && valsl[1].toCharArray()[0] != '2' && valsl[1].toCharArray()[0] != '3' && valsl[1].toCharArray()[0] != '4' && valsl[1].toCharArray()[0] != '5' && valsl[1].toCharArray()[0] != '6' && valsl[1].toCharArray()[0] != '7' && valsl[1].toCharArray()[0] != '8' && valsl[1].toCharArray()[0] != '9') {
                    System.err.printf("Error: In condition cannot compare arrays\n");
                    this.gracefullyExit();
                }
            }
            else{
                System.err.printf("Error: In condition cannot compare arrays\n");
                this.gracefullyExit();
            }
        }
        if(this.symbolTable.fetchType(valsr[0])!=null&&this.symbolTable.fetchType(valsr[0]).getArray()){
            if(valsr.length>1) {
                if (this.symbolTable.fetchType(valsr[1]) != null) {

                } else if (valsr[1].toCharArray()[0] != '0' && valsr[1].toCharArray()[0] != '1' && valsr[1].toCharArray()[0] != '2' && valsr[1].toCharArray()[0] != '3' && valsr[1].toCharArray()[0] != '4' && valsr[1].toCharArray()[0] != '5' && valsr[1].toCharArray()[0] != '6' && valsr[1].toCharArray()[0] != '7' && valsr[1].toCharArray()[0] != '8' && valsr[1].toCharArray()[0] != '9') {
                    System.err.printf("Error: In condition cannot compare arrays\n");
                    this.gracefullyExit();
                }
            }
            else{
                System.err.printf("Error: In condition cannot compare arrays\n");
                this.gracefullyExit();
            }
        }
        //till here

        // producing IR
        Integer l1 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        Integer l2 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        String t1 = this.ir.getPLACE(l1);
        String t2 = this.ir.getPLACE(l2);
        this.ir.GENQUAD(">", t2, t1, "?");
        this.tempOperandsStack.push(this.ir.getCurrentLabel());
        this.toPopFromTempOperandsStack++;

        // producing assembly
        STRecord tempRecX = new STRecord();
        tempRecX.setType(temp2);
        this.ir.setDataMapping(t2, tempRecX);
        STRecord tempRecY = new STRecord();
        tempRecY.setType(temp1);
        this.ir.setDataMapping(t1, tempRecY);
        this.ir.load("eax", t2);
        this.ir.load("edx", t1);
        this.ir.addAssemblyCode("cmp eax, edx\n");
        this.ir.addAssemblyCode("jg ?\n");
        this.ir.syncLabels();   // sync labels of jump statement

        // for later stages of IR production
        this.ir.setTRUE(this.ir.getCurrentLabel(), this.ir.MAKELIST(this.ir.getCurrentLabel()));
        this.ir.setFALSE(this.ir.getCurrentLabel()+1, this.ir.MAKELIST(this.ir.NEXTQUAD()));
        this.ir.GENQUAD("jump", "-", "-", "?");

        // producing assembly
        this.ir.addAssemblyCode("jmp ?\n");
        this.ir.syncLabels();
    }
    @Override
    public void inALesseqCond(ALesseqCond node) {}
    @Override
    public void outALesseqCond(ALesseqCond node) {
        STRecord.Type temp1 = this.tempTypeStack.pop();
        this.toPopFromTempTypeStack--;
        STRecord.Type temp2 = this.tempTypeStack.pop();
        this.toPopFromTempTypeStack--;
        temp1.printType();
        temp2.printType();
        if (!temp1.isSame(temp2, "condition")) {
            System.err.printf("Error: In condition one member is \"%s\" and the other member is \"%s\"\n",
                    temp1.getKind(), temp2.getKind());
            this.gracefullyExit();
        }
	//yiannis_sem
        //System.out.print(node.getLeft());
        String[] valsl = node.getLeft().toString().split(" ");
        String[] valsr = node.getRight().toString().split(" ");
        if(valsl[0].toCharArray()[0]=='"'){
            System.err.printf("Error: In condition cannot compare literals\n");
            this.gracefullyExit();
        }
        // STRecord.Type typel =this.symbolTable.fetchType(valsl[0]);
        if(this.symbolTable.fetchType(valsl[0])!=null&&this.symbolTable.fetchType(valsl[0]).getArray()){
            if(valsl.length>1) {
                if (this.symbolTable.fetchType(valsl[1]) != null) {

                } else if (valsl[1].toCharArray()[0] != '0' && valsl[1].toCharArray()[0] != '1' && valsl[1].toCharArray()[0] != '2' && valsl[1].toCharArray()[0] != '3' && valsl[1].toCharArray()[0] != '4' && valsl[1].toCharArray()[0] != '5' && valsl[1].toCharArray()[0] != '6' && valsl[1].toCharArray()[0] != '7' && valsl[1].toCharArray()[0] != '8' && valsl[1].toCharArray()[0] != '9') {
                    System.err.printf("Error: In condition cannot compare arrays\n");
                    this.gracefullyExit();
                }
            }
            else{
                System.err.printf("Error: In condition cannot compare arrays\n");
                this.gracefullyExit();
            }
        }
        if(this.symbolTable.fetchType(valsr[0])!=null&&this.symbolTable.fetchType(valsr[0]).getArray()){
            if(valsr.length>1) {
                if (this.symbolTable.fetchType(valsr[1]) != null) {

                } else if (valsr[1].toCharArray()[0] != '0' && valsr[1].toCharArray()[0] != '1' && valsr[1].toCharArray()[0] != '2' && valsr[1].toCharArray()[0] != '3' && valsr[1].toCharArray()[0] != '4' && valsr[1].toCharArray()[0] != '5' && valsr[1].toCharArray()[0] != '6' && valsr[1].toCharArray()[0] != '7' && valsr[1].toCharArray()[0] != '8' && valsr[1].toCharArray()[0] != '9') {
                    System.err.printf("Error: In condition cannot compare arrays\n");
                    this.gracefullyExit();
                }
            }
            else{
                System.err.printf("Error: In condition cannot compare arrays\n");
                this.gracefullyExit();
            }
        }
        //till here

        // producing IR
        Integer l1 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        Integer l2 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        String t1 = this.ir.getPLACE(l1);
        String t2 = this.ir.getPLACE(l2);
        this.ir.GENQUAD("<=", t2, t1, "?");
        this.tempOperandsStack.push(this.ir.getCurrentLabel());
        this.toPopFromTempOperandsStack++;

        // producing assembly
        STRecord tempRecX = new STRecord();
        tempRecX.setType(temp2);
        this.ir.setDataMapping(t2, tempRecX);
        STRecord tempRecY = new STRecord();
        tempRecY.setType(temp1);
        this.ir.setDataMapping(t1, tempRecY);
        this.ir.load("eax", t2);
        this.ir.load("edx", t1);
        this.ir.addAssemblyCode("cmp eax, edx\n");
        this.ir.addAssemblyCode("jle ?\n");
        this.ir.syncLabels();   // sync labels of jump statement

        // for later stages of IR production
        this.ir.setTRUE(this.ir.getCurrentLabel(), this.ir.MAKELIST(this.ir.getCurrentLabel()));
        this.ir.setFALSE(this.ir.getCurrentLabel()+1, this.ir.MAKELIST(this.ir.NEXTQUAD()));
        this.ir.GENQUAD("jump", "-", "-", "?");

        // producing assembly
        this.ir.addAssemblyCode("jmp ?\n");
        this.ir.syncLabels();
    }
    @Override
    public void inAGreateqCond(AGreateqCond node) {}
    @Override
    public void outAGreateqCond(AGreateqCond node) {
        STRecord.Type temp1 = this.tempTypeStack.pop();
        this.toPopFromTempTypeStack--;
        STRecord.Type temp2 = this.tempTypeStack.pop();
        this.toPopFromTempTypeStack--;
        temp1.printType();
        temp2.printType();
        if (!temp1.isSame(temp2, "condition")) {
            System.err.printf("Error: In condition one member is \"%s\" and the other member is \"%s\"\n",
                    temp1.getKind(), temp2.getKind());
            this.gracefullyExit();
        }
	//yiannis_sem
        //System.out.print(node.getLeft());
        String[] valsl = node.getLeft().toString().split(" ");
        String[] valsr = node.getRight().toString().split(" ");
        if(valsl[0].toCharArray()[0]=='"'){
            System.err.printf("Error: In condition cannot compare literals\n");
            this.gracefullyExit();
        }
        // STRecord.Type typel =this.symbolTable.fetchType(valsl[0]);
        if(this.symbolTable.fetchType(valsl[0])!=null&&this.symbolTable.fetchType(valsl[0]).getArray()){
            if(valsl.length>1) {
                if (this.symbolTable.fetchType(valsl[1]) != null) {

                } else if (valsl[1].toCharArray()[0] != '0' && valsl[1].toCharArray()[0] != '1' && valsl[1].toCharArray()[0] != '2' && valsl[1].toCharArray()[0] != '3' && valsl[1].toCharArray()[0] != '4' && valsl[1].toCharArray()[0] != '5' && valsl[1].toCharArray()[0] != '6' && valsl[1].toCharArray()[0] != '7' && valsl[1].toCharArray()[0] != '8' && valsl[1].toCharArray()[0] != '9') {
                    System.err.printf("Error: In condition cannot compare arrays\n");
                    this.gracefullyExit();
                }
            }
            else{
                System.err.printf("Error: In condition cannot compare arrays\n");
                this.gracefullyExit();
            }
        }
        if(this.symbolTable.fetchType(valsr[0])!=null&&this.symbolTable.fetchType(valsr[0]).getArray()){
            if(valsr.length>1) {
                if (this.symbolTable.fetchType(valsr[1]) != null) {

                } else if (valsr[1].toCharArray()[0] != '0' && valsr[1].toCharArray()[0] != '1' && valsr[1].toCharArray()[0] != '2' && valsr[1].toCharArray()[0] != '3' && valsr[1].toCharArray()[0] != '4' && valsr[1].toCharArray()[0] != '5' && valsr[1].toCharArray()[0] != '6' && valsr[1].toCharArray()[0] != '7' && valsr[1].toCharArray()[0] != '8' && valsr[1].toCharArray()[0] != '9') {
                    System.err.printf("Error: In condition cannot compare arrays\n");
                    this.gracefullyExit();
                }
            }
            else{
                System.err.printf("Error: In condition cannot compare arrays\n");
                this.gracefullyExit();
            }
        }
        //till here

        // producing IR
        Integer l1 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        Integer l2 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        String t1 = this.ir.getPLACE(l1);
        String t2 = this.ir.getPLACE(l2);
        this.ir.GENQUAD(">=", t2, t1, "?");
        this.tempOperandsStack.push(this.ir.getCurrentLabel());
        this.toPopFromTempOperandsStack++;

        // producing assembly
        STRecord tempRecX = new STRecord();
        tempRecX.setType(temp2);
        this.ir.setDataMapping(t2, tempRecX);
        STRecord tempRecY = new STRecord();
        tempRecY.setType(temp1);
        this.ir.setDataMapping(t1, tempRecY);
        this.ir.load("eax", t2);
        this.ir.load("edx", t1);
        this.ir.addAssemblyCode("cmp eax, edx\n");
        this.ir.addAssemblyCode("jge ?\n");
        this.ir.syncLabels();   // sync labels of jump statement

        // for later stages of IR production
        this.ir.setTRUE(this.ir.getCurrentLabel(), this.ir.MAKELIST(this.ir.getCurrentLabel()));
        this.ir.setFALSE(this.ir.getCurrentLabel()+1, this.ir.MAKELIST(this.ir.NEXTQUAD()));
        this.ir.GENQUAD("jump", "-", "-", "?");

        // producing assembly
        this.ir.addAssemblyCode("jmp ?\n");
        this.ir.syncLabels();
    }

}
