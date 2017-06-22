package compiler;

import compiler.analysis.DepthFirstAdapter;
import compiler.node.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class ASTPrintingVisitor extends DepthFirstAdapter {
    // for indentation
    private int indent = 0;
    private void makeIndent() { for(int i = 0; i < indent; i++) System.out.printf("    "); }

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

    // write to file
    PrintWriter irWriter;
    PrintWriter assemblyWriter;

    // string of assembly
    String assembly;

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
        // initialize assembly string as empty string
        this.assembly = "";
        this.assembly +=    ".intel_syntax noprefix # Use Intel syntax instead of AT&T\n" +
                            ".text\n";
    }

    // IN AND OUT A PROGRAM------------------------------------------------------------
    @Override
    public void inAProgram(AProgram node) { makeIndent(); System.out.printf("program :\n"); indent++; }
    @Override
    public void outAProgram(AProgram node) { indent--; System.out.printf("Intermediate Representation:\n");
        // print IR
        this.ir.printIR();
        // print IR to file -- for testing
        this.ir.printIR(irWriter);
        // print assembly to file -- for testing
//        this.assemblyWriter.print(
//                        ".intel_syntax noprefix # Use Intel syntax instead of AT&T\n" +
//                        ".text\n" +
//                        ".global main\n" +
//                        "main:\n" +
//                        "# Prologue (set up the frame & stack pointer)\n" +
//                        "push ebp\n" +
//                        "mov ebp, esp" +
//                        "# Put the argument of printf() on the stack\n" +
//                        "mov eax, OFFSET FLAT:fmt\n" +
//                        "push eax\n" +
//                        "call printf # Calls printf()\n" +
//                        "add esp, 4\n" +
//                        "mov eax, 0 # Set the exit code (0) here\n" +
//                        "# Epilogue (Reset frame and stack pointer)\n" +
//                        "mov esp, ebp\n" +
//                        "pop ebp\n" +
//                        "ret\n" +
//                        ".data\n" +
//                        "fmt: .asciz \"Hello world!\\n\"\n");
        this.assemblyWriter.print(this.ir.getAssemblyAsString());
        // done printing IR to file -- for testing
        this.irWriter.close();
        // done printing assembly to file
        this.assemblyWriter.close();
    }

    // IN AND OUT A FUNCTION DEFINITION------------------------------------------------------------
    @Override
    public void inAFuncDef(AFuncDef node) { makeIndent(); System.out.printf("function :\n"); indent++;
        // we are in a function definition, this means that a new namespace-scope is created
        this.symbolTable.enter();
        // the very next header that we will see, we want to remember that it belongs to a function Definition
        this.isDecl = false;
    }
    @Override
    public void outAFuncDef(AFuncDef node) { indent--; symbolTable.exit();
        String name = this.tempFunctionStack.pop();
        // producing IR
        this.ir.GENQUAD("endu", name, "-", "-");

        // producing assembly
        this.assembly +=    "endof("+name+"): mov sp, bp\n"+
                            "pop bp\n"+
                            "ret\n"+
                            "name("+name+") endp\n";
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
            String name = "_"+this.tempFunctionStack.peek()+"_"+this.ir.getCurrentLabel();
            this.ir.addAssemblyCode("@"+this.ir.getCurrentLabel()+":\n");
            this.ir.addAssemblyCode("name("+name+") proc near\n");
            this.ir.addAssemblyCode("push bp\n");
            this.ir.addAssemblyCode("mov bp, sp\n");
            this.ir.addAssemblyCode("sub sp, 8\n");
        }
        if(node.getBlock() != null)
        {
            node.getBlock().apply(this);
        }
        outAFuncDef(node);
    }

    // IN AND OUT A HEADER AND ASSISTANT-PRODUCTIONS------------------------------------------------------------
    @Override
    public void inAHeader(AHeader node) { makeIndent(); System.out.printf("header(\"%s\") :\n", node.getId().toString().trim().replaceAll("\\s+", " ")); indent++; }
    @Override
    public void outAHeader(AHeader node) { indent--;
        // keep the name of the function
        STRecord tempRec = new STRecord();

        STRecord.Type tempType = this.tempTypeStack.pop();
        this.toPopFromTempTypeStack--;
        tempRec.type = new STRecord.Type(tempType);

        tempRec.type.setFunction(true);
        // check for main-function existence
        // source: http://stackoverflow.com/questions/17973964/how-to-compare-two-strings-in-java-without-considering-spaces
        if (!this.hasMain && !node.getId().toString().trim().replaceAll("\\s+", " ").equalsIgnoreCase("main".trim().replaceAll("\\s+", " "))) {
            System.err.printf("Error: All Grace programs must have a \"main\" function\n");
            this.gracefullyExit();
        } else if (node.getId().toString().trim().replaceAll("\\s+", " ").equalsIgnoreCase("main".trim().replaceAll("\\s+", " ")) && node.getFparDef().size() != 0) {
            System.err.printf("Error: \"main\" function takes no arguments\n");
            this.gracefullyExit();
        } else if (!this.isDecl && this.hasMain && node.getId().toString().trim().replaceAll("\\s+", " ").equalsIgnoreCase("main".trim().replaceAll("\\s+", " "))) {
            System.err.printf("Error: All Grace programs must have only one \"main\" function\n");
            this.gracefullyExit();
        } else {
            this.hasMain = true;
            tempRec.setName(node.getId().toString().trim().replaceAll("\\s+", " "));
        }

        // insert the header's names to our Symbol-Table
        STRecord temp;
        while (this.toPopFromTempRecordStack != 0) {
            temp = this.tempRecordStack.pop();
            System.out.print("YYYYYYYYYYYYYY");
            System.out.print(temp.getName());
            this.symbolTable.insert(temp);
            toPopFromTempRecordStack--;
            tempRec.type.addParameter(temp.getType());
        }
        // if we are in a function declaration, check to see if it's definition exists in the current scope, and if it does, do the appropriate Name & Type-checking
        int result = this.symbolTable.searchFunction(tempRec);
        if (this.isDecl) {
            if (result == 0) {
                tempRec.setDefined(false);
                System.out.print("UUUUUUUUUUU");
                System.out.print(tempRec.getName());
                this.symbolTable.insert(tempRec);

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
//            if (!node.getId().toString().trim().replaceAll("\\s+", " ").equalsIgnoreCase("main".trim().replaceAll("\\s+", " "))) {
//                // if this is not the main function, we want to know the function in the previous scope
//                this.symbolTable.insert(tempRec);
//            }
//            // we are in a function definition, this means that a new namespace-scope is created
//            symbolTable.enter();

            if (result == 0) {
                System.out.print("JJJJJJJJJJ");
                System.out.print(tempRec.getName());
                this.symbolTable.insert(tempRec);
                this.symbolTable.setScopeType(tempRec.getType());
                this.symbolTable.addKnownFunction(tempRec);

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
    public void inAFparDef(AFparDef node) { makeIndent(); System.out.printf("fparDef :\n"); indent++; }
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
    public void inAIntDataType(AIntDataType node) { makeIndent(); System.out.printf("\"int\"");  }
    @Override
    public void outAIntDataType(AIntDataType node) { System.out.printf("\n"); }
    @Override
    public void inACharDataType(ACharDataType node) { makeIndent(); System.out.printf("\"char\""); }
    @Override
    public void outACharDataType(ACharDataType node) { System.out.printf("\n"); }

    // IN AND OUT A TYPE AND ASSISTANT-STATEMENT------------------------------------------------------------
    @Override
    public void inAType(AType node) { makeIndent(); System.out.printf("type :\n"); indent++; }
    @Override
    public void outAType(AType node) { indent--;
        STRecord.Type temp = new STRecord.Type();
        temp.setKind(node.getDataType().toString().trim().replaceAll("\\s+", " "));
        if(node.getIntConst().size() > 0) {
            temp.setArray(true);
            temp.setDimension(node.getIntConst().size());
        }
        this.tempTypeStack.push(temp);
        this.toPopFromTempTypeStack++;
    }

    // IN AND OUT A RETURN TYPE AND ASSISTANT-STATEMENTS------------------------------------------------------------
    @Override
    public void inADataTypeRetType(ADataTypeRetType node) { makeIndent(); System.out.printf("retType :\n"); indent++; }
    @Override
    public void outADataTypeRetType(ADataTypeRetType node) { indent--;
        STRecord.Type temp = new STRecord.Type();
        temp.setKind(node.getDataType().toString().trim().replaceAll("\\s+", " "));
        this.tempTypeStack.push(temp);
        this.toPopFromTempTypeStack++;
    }
    @Override
    public void inANothingRetType(ANothingRetType node) { makeIndent(); System.out.printf("retType :\"nothing\"\n"); }
    @Override
    public void outANothingRetType(ANothingRetType node) {
        STRecord.Type temp = new STRecord.Type();
        temp.setKind("nothing");
        this.tempTypeStack.push(temp);
        this.toPopFromTempTypeStack++;
    }

    // IN AND OUT A FUNCTION PARAMETER TYPE AND ASSISTANT-STATEMENT------------------------------------------------------------
    @Override
    public void inAFparType(AFparType node) { makeIndent(); System.out.printf("funcParType :\n"); indent++;
        STRecord.Type temp = new STRecord.Type();
        temp.setKind(node.getDataType().toString().trim().replaceAll("\\s+", " "));
        System.out.print(node.getLRBrackets());
        System.out.print(node.getDataType());
        if (node.getLRBrackets() != null) {
            System.out.printf("getLRBrackets\n");
            temp.setArray(true);
            temp.setDimension(1);
        }
        if(node.getIntConst().size() > 0) {
            System.out.printf("getIntconst\n");
            temp.setArray(true);
//            temp.setDimension(node.getIntConst().size());
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
    public void inAFuncDefLocalDef(AFuncDefLocalDef node) { makeIndent(); System.out.printf("funcDefLocalDef :\n"); indent++;
        // we are in a function definition, this means that a new namespace-scope is created
        symbolTable.enter();
        // the very next header that we will see, we want to remember that it belongs to a function Definition
        this.isDecl = false;
    }
    @Override
    public void outAFuncDefLocalDef(AFuncDefLocalDef node) { indent--; symbolTable.exit();}	//changed by yiannis
    @Override
    public void inAFuncDeclLocalDef(AFuncDeclLocalDef node) { makeIndent(); System.out.printf("funcDeclLocalDef :\n"); indent++;
        // the very next header that we will see, we want to remember that it belongs to a function Declaration
        this.isDecl = true;
    }
    @Override
    public void outAFuncDeclLocalDef(AFuncDeclLocalDef node) { indent--; }
    @Override
    public void inAVarDefLocalDef(AVarDefLocalDef node) { makeIndent(); System.out.printf("varDefLocalDef :\n"); indent++; }
    @Override
    public void outAVarDefLocalDef(AVarDefLocalDef node) { indent--;
        // insert the variables' names to our Symbol-Table
        STRecord temp;
        while (this.toPopFromTempRecordStack != 0) {
            temp = this.tempRecordStack.pop();

             int result = this.symbolTable.searchFunction(temp);
             if (result == 0) {
                 this.symbolTable.insert(temp);
             }
             // IS THIS AN ERROR???????????????????????????????????????????????????????????????????????????
             else if (result == 1) {
                 System.err.printf("Error: function \"%s\" has already been defined\n", temp.getName());
                 this.gracefullyExit();
             }
             else {
             System.err.printf("Error: function \"%s\" already known under a different type\n", temp.getName());
                 this.gracefullyExit();
             }

           // this.symbolTable.insert(temp);	commented by yiannis
            toPopFromTempRecordStack--;
        }
        // for debugging
//        this.symbolTable.printSTStructures();
        assert (toPopFromTempRecordStack == 0);
    }

    // IN AND OUT A VARIABLE DEFINITION AND ASSISTANT-STATEMENT------------------------------------------------------------
    @Override
    public void inAVarDef(AVarDef node) { makeIndent(); System.out.printf("var :\"%s\"\n", node.getId().toString().trim().replaceAll("\\s+", " ")); indent++; }
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
    public void inAFuncDecl(AFuncDecl node) { makeIndent(); System.out.printf("funcDecl :\n"); indent++; }
    @Override
    public void outAFuncDecl(AFuncDecl node) { indent--; }

    // IN AND OUT A CODE BLOCK------------------------------------------------------------
    @Override
    public void inABlock(ABlock node) { makeIndent(); System.out.printf("code-block body :\n"); indent++;}
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
//                if (numOfStmt != 1)
//                    this.ir.BACKPATCH(stmtNEXTLabel, "NEXT", this.ir.NEXTQUAD());
                numOfStmt++;

                e.apply(this);

                // for IR production
                if (numOfStmt == 1) stmtNEXTLabel = this.ir.getCurrentLabel();
                else if (numOfStmt == 2) stmtNEXTLabel = this.ir.getCurrentLabel();
            }
        }

        // producing IR
//        this.ir.setNEXT(stmtNEXTLabel, this.ir.EMPTYLIST());

        outABlock(node);
    }

    // IN AND OUT A FUNCTION CALL AND ASSISTANT-STATEMENTS------------------------------------------------------------
    @Override
    public void inAFuncCall(AFuncCall node) { makeIndent(); System.out.printf("func-call( \"%s\" ) :\n", node.getId().toString().trim().replaceAll("\\s+", " ")); indent++;
	//add by yiannis_sem
    //System.out.print("PPPPPPPPPPPPPPPPPPP");
    //System.out.print(node.getExpr());
    //System.out.print(node.getId().toString().trim().replaceAll("\\s+", " "));
    String funName = node.getId().toString().trim().replaceAll("\\s+", " ");
    LinkedList paramList = node.getExpr();
    int c=0;int size;
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
      //  System.out.print("////////////");
        //System.out.print(paramList.size());
        //System.out.print(size);
    if(paramList.size()!=size){
        System.err.printf("Error: function %s has %d arguments, %d given\n",funName,size,paramList.size());
        this.gracefullyExit();
    }
    for(Object i : paramList){
        //System.out.print("::::::::::::");
        String n=i.toString().split(" ")[0].trim().replaceAll("\\s+", " ");
        String[] narr = i.toString().split(" ");
        STRecord.Type type1 = new STRecord.Type();
        type1=this.symbolTable.fetchType(n);
        if(type1!=null){
            if(type1.getArray()) {//this is woarking wright or causing more problems?????????????
                //System.out.print("HGHGHGGGGGGGGGGHHHHHHHHHHHHHH");
                //System.out.print(narr.length);
                if (narr.length > 1) {
                    if (this.symbolTable.fetchType(narr[1]).getKind().equals("int") || narr[1].toCharArray()[0] == '0' || narr[1].toCharArray()[0] == '1' || narr[1].toCharArray()[0] == '2' || narr[1].toCharArray()[0] == '3' || narr[1].toCharArray()[0] == '4' || narr[1].toCharArray()[0] == '5' || narr[1].toCharArray()[0] == '6' || narr[1].toCharArray()[0] == '7' || narr[1].toCharArray()[0] == '8' || narr[1].toCharArray()[0] == '9') {
                        type1.setArray(false);
                    }
                }
            }
            /*System.out.print("TYPE1");
            System.out.print(type1.getKind());
            System.out.print(type1.getArray());
            System.out.print(type1.getRef());*/
        }
        else{
            if(n.toCharArray()[0]=='\''){
                //it is a character
                //System.out.print("GG");
                type1 = new STRecord.Type();
                type1.setArray(false);
                type1.setRef(false);
                type1.setKind("char");
            }
            else if (n.toCharArray()[0]=='\"'){
                type1 = new STRecord.Type();
                type1.setArray(true);
                type1.setRef(true);
                type1.setKind("char");
            }
            else if(n.toCharArray()[0]=='0' || n.toCharArray()[0]=='1' || n.toCharArray()[0]=='2' || n.toCharArray()[0]=='3' || n.toCharArray()[0]=='4' || n.toCharArray()[0]=='5' || n.toCharArray()[0]=='6' || n.toCharArray()[0]=='7' || n.toCharArray()[0]=='8' || n.toCharArray()[0]=='9'){
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
        //System.out.print("DFGDFGDFGDFGDFG");
        //System.out.print(funName);
        //System.out.print(c);
        type2 = this.symbolTable.fetchType(funName).getParameters().get(c);
        //System.out.print(type2.getKind());
        //type2 = this.symbolTable.paramType(funName,c+1);//instead of c stack error
        if(type2==null){
            //commented segment to avoid errors
            System.err.printf("Error: function %s has not been declared\n",funName);
            this.gracefullyExit();
        }
        else {

            c++;

            //System.out.print("TYPE1");
            //System.out.print(type1.getKind());
            //System.out.print(type1.getArray());
            //System.out.print(type1.getRef());
            //System.out.print("TYPE2");
            //System.out.print(type2.getKind());
            //System.out.print(type2.getArray());
            //System.out.print(type2.getRef());
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
    /*STRecord.Type type2;
    type2 = this.symbolTable.paramType(funName,0);
    System.out.print("TYPE2");
    System.out.print(type2.getKind());
        System.out.print(type2.getArray());
        System.out.print(type2.getRef());*/
    //type.printType();
    //System.out.print("VVVVVVVVVVVVVVVVV");
    //till here
//	    STRecord temp = new STRecord();
//        temp.setName(node.getId().toString().trim().replaceAll("\\s+", " "));
//        if(!this.symbolTable.inLibrary(node.getId().toString().trim().replaceAll("\\s+", " ")) || this.symbolTable.searchFunction(temp)!=1) {    //if added by yiannis2
//            System.err.printf("%s has not been declared\n",node.getId().toString().trim().replaceAll("\\s+", " "));
//            System.exit(-1);
//        }
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
                Integer t1 = tempOperandsStack.pop();
                this.toPopFromTempOperandsStack--;
                System.out.printf("searching param %d of %s", n, node.getId().toString());
                this.ir.GENQUAD("par", this.ir.getPLACE(t1), this.ir.PARAMMODE(node.getId().toString().trim().replaceAll("\\s+", " "), n), "-");
                n++;
            }
        }

        outAFuncCall(node);

        // producing IR
        STRecord.Type funcType = this.symbolTable.fetchType(node.getId().toString().trim().replaceAll("\\s+", " "));
        assert (funcType != null);
        System.out.printf("Type:\n");
        this.tempTypeStack.push(funcType);
        this.toPopFromTempTypeStack++;
        funcType.printType();
        if (!funcType.getKind().equals("nothing")) {
            String w = this.ir.NEWTEMP(funcType);
            this.ir.GENQUAD("par", "RET", w, "-");
            this.ir.addPLACE(this.ir.getCurrentLabel(), w);
        }

        this.ir.GENQUAD("call", "-", "-", node.getId().toString().trim().replaceAll("\\s+", " "));
    }

    // IN AND OUT A STATEMENT AND ASSISTANT-STATEMENTS------------------------------------------------------------
    @Override
    public void inAAssignmentStmt(AAssignmentStmt node) {}
    @Override
    public void outAAssignmentStmt(AAssignmentStmt node) {
       // System.out.print("KKKKKKKKKKKKKKKKKKKKKKK");
       // System.out.print(node.getExpr());
       // System.out.print("PPPPPPPPPPPPPPPPPPPPP");
       // System.out.print(node.getLValue());
       // System.out.print("LLLLLLLLLLLLLLLLLLLLLLLLLL");
        STRecord.Type type = new STRecord.Type();
        type = this.symbolTable.fetchType(node.getLValue().toString().trim().replaceAll("\\s+", " "));
        if(type!=null){
            if(type.getArray()){
                System.err.printf("Error: Trying to assign a value to an array: %s\n",node.getLValue().toString().trim().replaceAll("\\s+", " "));
                this.gracefullyExit();
            }
        }
       // type.printType();
       // System.out.print("GGGGGGGGGGGGG");
        STRecord.Type temp1 = this.tempTypeStack.pop();
        this.toPopFromTempTypeStack--;
        STRecord.Type temp2 = this.tempTypeStack.pop();
        this.toPopFromTempTypeStack--;
	//added by yiannis_sem
        //commented segment to avoid errors
        if(temp1.getArray()!=temp2.getArray()){
            System.out.print("OOOOOOOOOOOOOOPPPPPPPP");
            System.out.print(node.getLValue());
            String[] splitted = node.getLValue().toString().split(" ");
            if ((this.symbolTable.fetchType(splitted[1])!=null&&this.symbolTable.fetchType(splitted[1]).getKind().equals("int")) || splitted[1].toCharArray()[0] == '0' || splitted[1].toCharArray()[0] == '1' || splitted[1].toCharArray()[0] == '2' || splitted[1].toCharArray()[0] == '3' || splitted[1].toCharArray()[0] == '4' || splitted[1].toCharArray()[0] == '5' || splitted[1].toCharArray()[0] == '6' || splitted[1].toCharArray()[0] == '7' || splitted[1].toCharArray()[0] == '8' || splitted[1].toCharArray()[0] == '9') {

            }
            else {
                System.err.printf("Error: Trying to assign an array : %s to a non array value: %s\n", temp1.getKind(), temp2.getKind());
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
        //STRecord.Type temp = this.symbolTable.fetchType(item1);
        //String t1 = this.ir.NEWTEMP(temp);
        this.ir.GENQUAD(":=",item1,"-",item2);
        //till here
//        this.ir.setNEXT(this.ir.getCurrentLabel(), this.ir.EMPTYLIST());

        // producing assembly
        STRecord tempRec = new STRecord();
        tempRec.setType(temp3);
        this.ir.addAssemblyCode("@"+this.ir.getCurrentLabel()+":\n");
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
//        ArrayList<Integer> l1 = this.ir.getFALSE(this.ir.getCurrentLabel());
//        ArrayList<Integer> l2 = this.ir.EMPTYLIST();
//        ArrayList<Integer> stmt1NEXT = null;
        Integer stmtNEXT;

        if(node.getThenM() != null)
        {
            node.getThenM().apply(this);

            // for IR production
//            stmt1NEXT = this.ir.getNEXT(this.ir.getCurrentLabel());
        }
        {
            // for IR production
//            l1 = this.ir.MAKELIST(this.ir.NEXTQUAD());
            this.ir.GENQUAD("jump", "-", "-", "?");
            stmtNEXT = this.ir.getCurrentLabel();
            this.ir.setNEXT(stmtNEXT, this.ir.MAKELIST(stmtNEXT));
            this.ir.BACKPATCH(condFALSELabel, "FALSE", this.ir.NEXTQUAD());

            List<PStmt> copy = new ArrayList<PStmt>(node.getElseM());
            for(PStmt e : copy)
            {
                e.apply(this);
            }

            // for IR production
//            l2 = this.ir.getNEXT(this.ir.getCurrentLabel());
        }

        // for IR production
        this.ir.BACKPATCH(stmtNEXT, "NEXT", this.ir.NEXTQUAD());
//        ArrayList<ArrayList<Integer>> param = new ArrayList<ArrayList<Integer>>();
//        param.add(l1);
//        param.add(stmt1NEXT);
//        param.add(l2);
//        this.ir.setNEXT(this.ir.getCurrentLabel(), this.ir.MERGE(param));

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

        // for IR production
//        Integer Q = this.ir.NEXTQUAD();

        if(node.getCond() != null)
        {
            node.getCond().apply(this);
        }

        // for IR production
        int condTRUELabel = this.ir.getCurrentLabel()-1;    // "-1" because there is the jump, -, -, ? in the middle
        int condFALSELabel = this.ir.getCurrentLabel();
//        ArrayList<Integer> condFALSE = this.ir.getFALSE(condFALSELabel);
        this.ir.BACKPATCH(condTRUELabel, "TRUE", this.ir.NEXTQUAD());

        if(node.getStmt() != null)
        {
            node.getStmt().apply(this);
        }

        // for IR production
        int stmtLabel = this.ir.getCurrentLabel();
//        this.ir.BACKPATCH(stmtLabel, "NEXT", Q);
//        this.ir.GENQUAD("jump", "-", "-", Q.toString());
        Integer next = this.ir.NEXTQUAD()+1;
        this.ir.GENQUAD("jump", "-", "-", next.toString());
        this.ir.BACKPATCH(condFALSELabel, "FALSE", next);
//        this.ir.setNEXT(stmtLabel, condFALSE);

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
        STRecord.Type temp = this.tempTypeStack.pop();
        this.toPopFromTempTypeStack--;
        if(!this.symbolTable.checkRetType(temp)){
            System.err.printf("Error: function has different return type\n");
            this.gracefullyExit();
        }

        // producing IR
//        this.ir.setNEXT(this.ir.getCurrentLabel(), this.ir.EMPTYLIST());
    }

    // IN AND OUT A L-VALUE AND ASSISTANT-STATEMENTS------------------------------------------------------------
    @Override
    public void inAIdLValue(AIdLValue node) { makeIndent(); System.out.printf("\"%s\"\n", node.getId().toString().trim().replaceAll("\\s+", " ")); }
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
        /*String t1;
        if(this.ir.lookTemp(str)!=null){
            t1 = this.ir.lookTemp(str);
        }
        else {
            t1 = this.ir.NEWTEMP(temp);
            this.ir.newTemp(str,t1);
        }*/
        //that what it was before
        //t1 = this.ir.NEWTEMP(temp);
        //this.ir.GENQUAD(":=",  str,"-", t1);
        //till here
        this.ir.addPLACE(this.ir.getCurrentLabel(), str);//changed by yiannis3
        //this.ir.addPLACE(this.ir.getCurrentLabel(), t1);//before modified
//        this.ir.setNEXT(this.ir.getCurrentLabel(), this.ir.EMPTYLIST());
        this.tempOperandsStack.push(this.ir.getCurrentLabel());
        this.toPopFromTempOperandsStack++;
    }
    @Override
    public void inAStringLValue(AStringLValue node) { makeIndent(); System.out.printf("\"%s\"\n", node.getStringLiteral().toString().trim().replaceAll("\\s+", " ")); }
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
        //String t1 = this.ir.NEWTEMP(temp);
        //this.ir.GENQUAD(":=", str, "-", t1);
        //this.ir.addPLACE(this.ir.getCurrentLabel(), t1);
        //till here
//        this.ir.setNEXT(this.ir.getCurrentLabel(), this.ir.EMPTYLIST());
        this.tempOperandsStack.push(this.ir.getCurrentLabel());
        this.toPopFromTempOperandsStack++;
    }
    @Override
    public void inAExpressionLValue(AExpressionLValue node) { makeIndent(); System.out.printf("exprLValue :\n"); }
    @Override
    public void outAExpressionLValue(AExpressionLValue node) {
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
   //     String par1 = "["+t1+"]";                                       // problematic -- FIX IT
     //   this.ir.GENQUAD(":=", par1, "-", t2);
        this.ir.addPLACE(this.ir.getCurrentLabel(), t2);
//        this.ir.setNEXT(this.ir.getCurrentLabel(), this.ir.EMPTYLIST());
        this.tempOperandsStack.push(this.ir.getCurrentLabel());
        this.toPopFromTempOperandsStack++;

        // producing assembly
//        STRecord tempRecX = this.tempRecordStack.pop();
//        this.toPopFromTempRecordStack--;
//        STRecord tempRecY = this.tempRecordStack.pop();
//        this.toPopFromTempRecordStack--;
//        tempRecY.setDereference(true);
//        this.ir.setDataMapping(t3, tempRecY);
//        this.ir.load("ax", t3);
//        this.ir.
//        this.assembly +=    "load(ax, "+t3+")\n"+
//                            "mov cx, size\n"+
//                            "imul cx\n"+
//                            "loadAddr(cx, "+t1+")\n"+
//                            "add ax, cx\n"+
//                            "store(ax, "+t2+")\n";
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
        //String t1 = this.ir.NEWTEMP(temp);//commentd by yiannis3
        //this.ir.GENQUAD(":=", str, "-", t1);//commented by yiannis3
        this.ir.pushTemp(str);//added by yiannis3
        this.ir.addPLACE(this.ir.getCurrentLabel(), str);//changed by yiannis3
        //this.ir.addPLACE(this.ir.getCurrentLabel(), t1);//before changed
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
       // String t1 = this.ir.NEWTEMP(temp);//commented by yiannis3!
        //this.ir.GENQUAD(":=", str, "-", t1);//commented by yiannis3!
        this.ir.pushTemp(str);//added by yiannis3!
        this.ir.addPLACE(this.ir.getCurrentLabel(), str);//changed by yiannis3!
        //this.ir.addPLACE(this.ir.getCurrentLabel(), t1);//before changed by yiannis3!
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
        /*if(temp.getArray()==true) {     //added by yinnis3
            String t1 = this.ir.getPLACE(l1);
            String t3 = this.ir.getLastTemp();//added by yiannis3
            String t2 = this.ir.NEWTEMP(temp);
            //String par1 = "["+t1+"]";                                       // problematic -- FIX IT//commented by yiannis3
            this.ir.GENQUAD("array", t1, t3, t2);                      //changed by yiannis3 (array was := and t1 was par1 and t3 was "-")
            this.ir.addPLACE(this.ir.getCurrentLabel(), t2);
            this.ir.setNEXT(this.ir.getCurrentLabel(), this.ir.EMPTYLIST());
        }*/
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
        // if they are of the same type, then the result of the expression between them is going to be of the same type
        STRecord.Type temp3 = new STRecord.Type(temp1);
        this.tempTypeStack.push(temp3);
        this.toPopFromTempTypeStack++;

        // producing IR
        Integer l1 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        Integer l2 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        //String t1 = this.ir.getPLACE(l1);//this amd below line ommented by yiannis3
        //String t2 = this.ir.getPLACE(l2);
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
        this.ir.load("ax", t2);
        this.ir.load("dx", t1);
        this.ir.addAssemblyCode("add ax, dx\n");
        STRecord tempRecZ = new STRecord();
        tempRecZ.setType(temp3);
        this.ir.setDataMapping(t3, tempRecZ);
        this.ir.store("ax", t3);
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
        // if they are of the same type, then the result of the expression between them is going to be of the same type
        STRecord.Type temp3 = new STRecord.Type(temp1);
        this.tempTypeStack.push(temp3);
        this.toPopFromTempTypeStack++;

        // producing IR
        Integer l1 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        Integer l2 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        //String t1 = this.ir.getPLACE(l1); //this and below line commented by yiannis3
        //String t2 = this.ir.getPLACE(l2);
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
        this.ir.load("ax", t2);
        this.ir.load("dx", t1);
        this.ir.addAssemblyCode("sub ax, dx\n");
        STRecord tempRecZ = new STRecord();
        tempRecZ.setType(temp3);
        this.ir.setDataMapping(t3, tempRecZ);
        this.ir.store("ax", t3);
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
        // if they are of the same type, then the result of the expression between them is going to be of the same type
        STRecord.Type temp3 = new STRecord.Type(temp1);
        this.tempTypeStack.push(temp3);
        this.toPopFromTempTypeStack++;

        // producing IR
        Integer l1 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        Integer l2 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        //String t1 = this.ir.getPLACE(l1);//this and below line commented by yiannis3!
        //String t2 = this.ir.getPLACE(l2);
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
        this.ir.load("ax", t2);
        this.ir.load("cx", t1);
        this.ir.addAssemblyCode("imul cx\n");
        STRecord tempRecZ = new STRecord();
        tempRecZ.setType(temp3);
        this.ir.setDataMapping(t3, tempRecZ);
        this.ir.store("ax", t3);
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
        // if they are of the same type, then the result of the expression between them is going to be of the same type
        STRecord.Type temp3 = new STRecord.Type(temp1);
        this.tempTypeStack.push(temp3);
        this.toPopFromTempTypeStack++;

        // producing IR
        Integer l1 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        Integer l2 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        //String t1 = this.ir.getPLACE(l1);//this and below line commented by yiannis3!
       // String t2 = this.ir.getPLACE(l2);
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
        this.ir.load("ax", t2);
        this.ir.addAssemblyCode("cwd\n");
        this.ir.load("cx", t1);
        this.ir.addAssemblyCode("idiv cx");
        STRecord tempRecZ = new STRecord();
        tempRecZ.setType(temp3);
        this.ir.setDataMapping(t3, tempRecZ);
        this.ir.store("ax", t3);
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
        // if they are of the same type, then the result of the expression between them is going to be of the same type
        STRecord.Type temp3 = new STRecord.Type(temp1);
        this.tempTypeStack.push(temp3);
        this.toPopFromTempTypeStack++;

        // producing IR
        Integer l1 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        Integer l2 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        //String t1 = this.ir.getPLACE(l1);//this and below line commented by yiannis3!
        //String t2 = this.ir.getPLACE(l2);
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
        this.ir.load("ax", t2);
        this.ir.addAssemblyCode("cwd\n");
        this.ir.load("cx", t1);
        this.ir.addAssemblyCode("idiv cx");
        STRecord tempRecZ = new STRecord();
        tempRecZ.setType(temp3);
        this.ir.setDataMapping(t3, tempRecZ);
        this.ir.store("ax", t3);
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
        // if they are of the same type, then the result of the expression between them is going to be of the same type
        STRecord.Type temp3 = new STRecord.Type(temp1);
        this.tempTypeStack.push(temp3);
        this.toPopFromTempTypeStack++;

        // producing IR
        Integer l1 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        Integer l2 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        //String t1 = this.ir.getPLACE(l1);//this and below line commented by yiannis3!
        //String t2 = this.ir.getPLACE(l2);
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
        this.ir.load("ax", t2);
        this.ir.addAssemblyCode("cwd\n");
        this.ir.load("cx", t1);
        this.ir.addAssemblyCode("idiv cx");
        STRecord tempRecZ = new STRecord();
        tempRecZ.setType(temp3);
        this.ir.setDataMapping(t3, tempRecZ);
        this.ir.store("dx", t3);
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
        this.ir.GENQUAD("not", "-", "-", "?");
        this.tempOperandsStack.push(this.ir.getCurrentLabel());
        this.toPopFromTempOperandsStack++;
        this.ir.GENQUAD("jump", "-", "-", "?");

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
//        this.ir.GENQUAD("and", "-", "-", "?");
//        this.tempOperandsStack.push(this.ir.getCurrentLabel());
//        this.toPopFromTempOperandsStack++;
//        this.ir.GENQUAD("jump", "-", "-", "?");
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
//        this.ir.GENQUAD("or", "-", "-", "?");
//        this.tempOperandsStack.push(this.ir.getCurrentLabel());
//        this.toPopFromTempOperandsStack++;
//        this.ir.GENQUAD("jump", "-", "-", "?");
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

        // for later stages of IR production
        this.ir.setTRUE(this.ir.getCurrentLabel(), this.ir.MAKELIST(this.ir.getCurrentLabel()));
        this.ir.setFALSE(this.ir.getCurrentLabel()+1, this.ir.MAKELIST(this.ir.NEXTQUAD()));
        this.ir.GENQUAD("jump", "-", "-", "?");

        // producing assembly
        this.assembly +=    "load(ax, "+t2+")\n"+
                            "load(dx, "+t1+")\n"+
                            "cmp ax, dx\n"+
                            "instr label(?)\n";
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
        //yainnis_sem
        //System.out.print("KLKLKLKLKL");
        //System.out.print(node.getLeft());
        //if(temp1.getArray()||temp2.getArray()){
          //  System.err.printf("Error: In condition at least one member is literal or array\n");
            //this.gracefullyExit();
        //}
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

        // for later stages of IR production
        this.ir.setTRUE(this.ir.getCurrentLabel(), this.ir.MAKELIST(this.ir.getCurrentLabel()));
        this.ir.setFALSE(this.ir.getCurrentLabel()+1, this.ir.MAKELIST(this.ir.NEXTQUAD()));
        this.ir.GENQUAD("jump", "-", "-", "?");

        // producing assembly
        this.assembly +=    "load(ax, "+t2+")\n"+
                            "load(dx, "+t1+")\n"+
                            "cmp ax, dx\n"+
                            "instr label(?)\n";
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

        // producing IR
        Integer l1 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        Integer l2 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        String t1 = this.ir.getPLACE(l1);
        String t2 = this.ir.getPLACE(l2);
        this.ir.GENQUAD("<>", t2, t1, "?");
        this.toPopFromTempOperandsStack++;

        // for later stages of IR production
        this.ir.setTRUE(this.ir.getCurrentLabel(), this.ir.MAKELIST(this.ir.getCurrentLabel()));
        this.ir.setFALSE(this.ir.getCurrentLabel()+1, this.ir.MAKELIST(this.ir.NEXTQUAD()));
        this.ir.GENQUAD("jump", "-", "-", "?");

        // producing assembly
        this.assembly +=    "load(ax, "+t2+")\n"+
                            "load(dx, "+t1+")\n"+
                            "cmp ax, dx\n"+
                            "instr label(?)\n";
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

        // producing IR
        Integer l1 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        Integer l2 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        //String t1 = this.ir.getPLACE(l1);
        //String t2 = this.ir.getPLACE(l2);
        String t1 = this.ir.getLastTemp();
        String t2 = this.ir.getLastTemp();
        this.ir.GENQUAD("<", t2, t1, "?");
        this.tempOperandsStack.push(this.ir.getCurrentLabel());
        this.toPopFromTempOperandsStack++;

        // for later stages of IR production
        this.ir.setTRUE(this.ir.getCurrentLabel(), this.ir.MAKELIST(this.ir.getCurrentLabel()));
        this.ir.setFALSE(this.ir.getCurrentLabel()+1, this.ir.MAKELIST(this.ir.NEXTQUAD()));
        this.ir.GENQUAD("jump", "-", "-", "?");

        // producing assembly
        this.assembly +=    "load(ax, "+t2+")\n"+
                            "load(dx, "+t1+")\n"+
                            "cmp ax, dx\n"+
                            "instr label(?)\n";
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

        // for later stages of IR production
        this.ir.setTRUE(this.ir.getCurrentLabel(), this.ir.MAKELIST(this.ir.getCurrentLabel()));
        this.ir.setFALSE(this.ir.getCurrentLabel()+1, this.ir.MAKELIST(this.ir.NEXTQUAD()));
        this.ir.GENQUAD("jump", "-", "-", "?");

        // producing assembly
        this.assembly +=    "load(ax, "+t2+")\n"+
                            "load(dx, "+t1+")\n"+
                            "cmp ax, dx\n"+
                            "instr label(?)\n";
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

        // for later stages of IR production
        this.ir.setTRUE(this.ir.getCurrentLabel(), this.ir.MAKELIST(this.ir.getCurrentLabel()));
        this.ir.setFALSE(this.ir.getCurrentLabel()+1, this.ir.MAKELIST(this.ir.NEXTQUAD()));
        this.ir.GENQUAD("jump", "-", "-", "?");

        // producing assembly
        this.assembly +=    "load(ax, "+t2+")\n"+
                            "load(dx, "+t1+")\n"+
                            "cmp ax, dx\n"+
                            "instr label(?)\n";
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

        // for later stages of IR production
        this.ir.setTRUE(this.ir.getCurrentLabel(), this.ir.MAKELIST(this.ir.getCurrentLabel()));
        this.ir.setFALSE(this.ir.getCurrentLabel()+1, this.ir.MAKELIST(this.ir.NEXTQUAD()));
        this.ir.GENQUAD("jump", "-", "-", "?");

        // producing assembly
        this.assembly +=    "load(ax, "+t2+")\n"+
                            "load(dx, "+t1+")\n"+
                            "cmp ax, dx\n"+
                            "instr label(?)\n";
    }

}
