package compiler;

import compiler.analysis.DepthFirstAdapter;
import compiler.node.*;

import java.util.ArrayList;
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

    // we keep our Intermediate Representation, to be used for machine-code generation in the future
    private IntermediateCode ir;
    // a Java Stack from where quad operands are temporarily stored and retrieved
    private Stack<String> tempOperandsStack;
    private Integer toPopFromTempOperandsStack;

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
        this.tempOperandsStack = new Stack<String>();
        this.toPopFromTempOperandsStack = 0;
    }

    // IN AND OUT A PROGRAM------------------------------------------------------------
    @Override
    public void inAProgram(AProgram node) { makeIndent(); System.out.printf("program :\n"); indent++; }
    @Override
    public void outAProgram(AProgram node) { indent--; }

    // IN AND OUT A FUNCTION DEFINITION------------------------------------------------------------
    @Override
    public void inAFuncDef(AFuncDef node) { makeIndent(); System.out.printf("function :\n"); indent++;
        // we are in a function definition, this means that a new namespace-scope is created
        symbolTable.enter();
        // the very next header that we will see, we want to remember that it belongs to a function Definition
        this.isDecl = false;
    }
    @Override
    public void outAFuncDef(AFuncDef node) { indent--; symbolTable.exit(); }

    // IN AND OUT A HEADER AND ASSISTANT-PRODUCTIONS------------------------------------------------------------
    @Override
    public void inAHeader(AHeader node) { makeIndent(); System.out.printf("header(\"%s\") :\n", node.getId().toString().trim().replaceAll("\\s+", " ")); indent++;
        // producing IR
        if (!this.isDecl) this.ir.GENQUAD("unit", node.getId().toString().trim().replaceAll("\\s+", " "), "-", "-");
    }
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
            // exit with "failure" code
            System.exit(-1);
        } else if (node.getId().toString().trim().replaceAll("\\s+", " ").equalsIgnoreCase("main".trim().replaceAll("\\s+", " ")) && node.getFparDef().size() != 0) {
            System.err.printf("Error: \"main\" function takes no arguments\n");
            // exit with "failure" code
            System.exit(-1);
        } else if (!this.isDecl && this.hasMain && node.getId().toString().trim().replaceAll("\\s+", " ").equalsIgnoreCase("main".trim().replaceAll("\\s+", " "))) {
            System.err.printf("Error: All Grace programs must have only one \"main\" function\n");
            // exit with "failure" code
            System.exit(-1);
        } else {
            this.hasMain = true;
            tempRec.setName(node.getId().toString().trim().replaceAll("\\s+", " "));
        }

        // insert the header's names to our Symbol-Table
        STRecord temp;
        while (this.toPopFromTempRecordStack != 0) {
            temp = this.tempRecordStack.pop();
            this.symbolTable.insert(temp);
            toPopFromTempRecordStack--;
            tempRec.type.addParameter(temp.getType());
        }
        // if we are in a function declaration, check to see if it's definition exists in the current scope, and if it does, do the appropriate Name & Type-checking
        int result = this.symbolTable.searchFunction(tempRec);
        if (this.isDecl) {
            if (result == 0) {
                tempRec.setDefined(false);
                this.symbolTable.insert(tempRec);
            }
            // IS THIS AN ERROR???????????????????????????????????????????????????????????????????????????
            else if (result == 1) {
                System.err.printf("Error: function \"%s\" has already been defined\n", tempRec.getName());
                // exit with "failure" code
                System.exit(-1);
            }
            else {
                System.err.printf("Error: function \"%s\" already known under a different type\n", tempRec.getName());
                // exit with "failure" code
                System.exit(-1);
            }
        }else {
            if (result == 0) {
                this.symbolTable.insert(tempRec);
                this.symbolTable.setScopeType(tempRec.getType());

                // producing IR
                this.ir.GENQUAD("endu", node.getId().toString().trim().replaceAll("\\s+", " "), "-", "-");
            }
            // IS THIS AN ERROR???????????????????????????????????????????????????????????????????????????
            else if (result == 1) {
                System.err.printf("Error: function \"%s\" has already been defined\n", tempRec.getName());
                // exit with "failure" code
                System.exit(-1);
            }
            else {
                System.err.printf("Error: function \"%s\" already known under a different type\n", tempRec.getName());
                // exit with "failure" code
                System.exit(-1);
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
//added by yiannis
             int result = this.symbolTable.searchFunction(temp);
             if (result == 0) {
                this.symbolTable.insert(temp);
             }
             // IS THIS AN ERROR???????????????????????????????????????????????????????????????????????????
             else if (result == 1) {
                 System.err.printf("Error: function \"%s\" has already been defined\n", temp.getName());
                 // exit with "failure" code
                 System.exit(-1);
             }
             else {
             System.err.printf("Error: function \"%s\" already known under a different type\n", temp.getName());
                 // exit with "failure" code
                 System.exit(-1);
             }
//till here
           // this.symbolTable.insert(temp);	commented by yiannis
            toPopFromTempRecordStack--;
        }
        // for debugging
        this.symbolTable.printSTStructures();
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

    // IN AND OUT A FUNCTION CALL AND ASSISTANT-STATEMENTS------------------------------------------------------------
    @Override
    public void inAFuncCall(AFuncCall node) { makeIndent(); System.out.printf("func-call( \"%s\" ) :\n", node.getId().toString().trim().replaceAll("\\s+", " ")); indent++;
	/*STRecord temp = new STRecord();
        temp.setName(node.getId().toString().trim().replaceAll("\\s+", " "));
        if(!this.symbolTable.inLibrary(node.getId().toString().trim().replaceAll("\\s+", " ")) || this.symbolTable.searchFunction(temp)!=1){    //if added by yiannis2
            System.err.printf("%s has not been declared\n",node.getId().toString().trim().replaceAll("\\s+", " "));
            System.exit(-1);
        }*/
    }
    @Override
    public void outAFuncCall(AFuncCall node) { indent--; }

    // IN AND OUT A STATEMENT AND ASSISTANT-STATEMENTS------------------------------------------------------------
    @Override
    public void inAReturnStmt(AReturnStmt node) {}
    @Override
    public void outAReturnStmt(AReturnStmt node) {
        STRecord.Type temp = this.tempTypeStack.pop();
        this.toPopFromTempTypeStack--;
        if(!this.symbolTable.checkRetType(temp)){	//added by yiannis
            System.err.printf("Error: function has different return type\n");
            // exit with "failure" code
            System.exit(-1);
        }
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
            // exit with "failure" code
            System.exit(-1);
        }

        // producing IR
        String str = node.getId().toString();
        this.tempOperandsStack.push(str);
        this.toPopFromTempOperandsStack++;
    }
    @Override
    public void inAStringLValue(AStringLValue node) { makeIndent(); System.out.printf("\"%s\"\n", node.getStringLiteral().toString().trim().replaceAll("\\s+", " ")); }
    @Override
    public void outAStringLValue(AStringLValue node) {
        STRecord.Type temp = new STRecord.Type();
        temp.setKind("string");
        this.tempTypeStack.push(temp);
        this.toPopFromTempTypeStack++;

        // producing IR
        String str = node.getStringLiteral().toString();
        this.tempOperandsStack.push(str);
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
            // exit with "failure" code
            System.exit(-1);
        }
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
        String str = node.getIntConst().toString();
        this.tempOperandsStack.push(str);
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
        String str = node.getCharConst().toString();
        this.tempOperandsStack.push(str);
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
        if (!temp1.isSame(temp2)) {
            System.err.printf("Error: In \"plus\" expression one member is %s and the other member is %s\n",
                    temp1.getKind(), temp2.getKind());
            // exit with "failure" code
            System.exit(-1);
        }
        // if they are of the same type, then the result of the expression between them is going to be of the same type
        STRecord.Type temp3 = new STRecord.Type(temp1);
        this.tempTypeStack.push(temp3);
        this.toPopFromTempTypeStack++;

        // producing IR
        String t1 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        String t2 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        String t3 = this.ir.NEWTEMP(temp1);
        this.ir.GENQUAD("+", t2, t1, t3);
        this.tempOperandsStack.push(t3);
        this.toPopFromTempOperandsStack++;
    }
    @Override
    public void inAMinusExpr(AMinusExpr node) {}
    @Override
    public void outAMinusExpr(AMinusExpr node) {
        STRecord.Type temp1 = this.tempTypeStack.pop();
        this.toPopFromTempTypeStack--;
        STRecord.Type temp2 = this.tempTypeStack.pop();
        this.toPopFromTempTypeStack--;
        if (!temp1.isSame(temp2)) {
            System.err.printf("Error: In \"minus\" expression one member is %s and the other member is %s\n",
                    temp1.getKind(), temp2.getKind());
            // exit with "failure" code
            System.exit(-1);
        }
        // if they are of the same type, then the result of the expression between them is going to be of the same type
        STRecord.Type temp3 = new STRecord.Type(temp1);
        this.tempTypeStack.push(temp3);
        this.toPopFromTempTypeStack++;

        // producing IR
        String t1 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        String t2 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        String t3 = this.ir.NEWTEMP(temp1);
        this.ir.GENQUAD("-", t2, t1, t3);
        this.tempOperandsStack.push(t3);
        this.toPopFromTempOperandsStack++;
    }
    @Override
    public void inAMultExpr(AMultExpr node) {}
    @Override
    public void outAMultExpr(AMultExpr node) {
        STRecord.Type temp1 = this.tempTypeStack.pop();
        this.toPopFromTempTypeStack--;
        STRecord.Type temp2 = this.tempTypeStack.pop();
        this.toPopFromTempTypeStack--;
        if (!temp1.isSame(temp2)) {
            System.err.printf("Error: In \"mult\" expression one member is %s and the other member is %s\n",
                    temp1.getKind(), temp2.getKind());
            // exit with "failure" code
            System.exit(-1);
        }
        // if they are of the same type, then the result of the expression between them is going to be of the same type
        STRecord.Type temp3 = new STRecord.Type(temp1);
        this.tempTypeStack.push(temp3);
        this.toPopFromTempTypeStack++;

        // producing IR
        String t1 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        String t2 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        String t3 = this.ir.NEWTEMP(temp1);
        this.ir.GENQUAD("*", t2, t1, t3);
        this.tempOperandsStack.push(t3);
        this.toPopFromTempOperandsStack++;
    }
    @Override
    public void inADivExpr(ADivExpr node) {}
    @Override
    public void outADivExpr(ADivExpr node) {
        STRecord.Type temp1 = this.tempTypeStack.pop();
        this.toPopFromTempTypeStack--;
        STRecord.Type temp2 = this.tempTypeStack.pop();
        this.toPopFromTempTypeStack--;
        if (!temp1.isSame(temp2)) {
            System.err.printf("Error: In \"div\" expression one member is %s and the other member is %s\n",
                    temp1.getKind(), temp2.getKind());
            // exit with "failure" code
            System.exit(-1);
        }
        // if they are of the same type, then the result of the expression between them is going to be of the same type
        STRecord.Type temp3 = new STRecord.Type(temp1);
        this.tempTypeStack.push(temp3);
        this.toPopFromTempTypeStack++;

        // producing IR
        String t1 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        String t2 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        String t3 = this.ir.NEWTEMP(temp1);
        this.ir.GENQUAD("div", t2, t1, t3);
        this.tempOperandsStack.push(t3);
        this.toPopFromTempOperandsStack++;
    }
    @Override
    public void inADivisionExpr(ADivisionExpr node) {}
    @Override
    public void outADivisionExpr(ADivisionExpr node) {
        STRecord.Type temp1 = this.tempTypeStack.pop();
        this.toPopFromTempTypeStack--;
        STRecord.Type temp2 = this.tempTypeStack.pop();
        this.toPopFromTempTypeStack--;
        if (!temp1.isSame(temp2)) {
            System.err.printf("Error: In \"division\" expression one member is %s and the other member is %s\n",
                    temp1.getKind(), temp2.getKind());
            // exit with "failure" code
            System.exit(-1);
        }
        // if they are of the same type, then the result of the expression between them is going to be of the same type
        STRecord.Type temp3 = new STRecord.Type(temp1);
        this.tempTypeStack.push(temp3);
        this.toPopFromTempTypeStack++;

        // producing IR
        String t1 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        String t2 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        String t3 = this.ir.NEWTEMP(temp1);
        this.ir.GENQUAD("/", t2, t1, t3);
        this.tempOperandsStack.push(t3);
        this.toPopFromTempOperandsStack++;
    }
    @Override
    public void inAModExpr(AModExpr node) {}
    @Override
    public void outAModExpr(AModExpr node) {
        STRecord.Type temp1 = this.tempTypeStack.pop();
        this.toPopFromTempTypeStack--;
        STRecord.Type temp2 = this.tempTypeStack.pop();
        this.toPopFromTempTypeStack--;
        if (!temp1.isSame(temp2)) {
            System.err.printf("Error: In \"mod\" expression one member is \"%s\" and the other member is \"%s\"\n",
                    temp1.getKind(), temp2.getKind());
            // exit with "failure" code
            System.exit(-1);
        }
        // if they are of the same type, then the result of the expression between them is going to be of the same type
        STRecord.Type temp3 = new STRecord.Type(temp1);
        this.tempTypeStack.push(temp3);
        this.toPopFromTempTypeStack++;

        // producing IR
        String t1 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        String t2 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        String t3 = this.ir.NEWTEMP(temp1);
        this.ir.GENQUAD("mod", t2, t1, t3);
        this.tempOperandsStack.push(t3);
        this.toPopFromTempOperandsStack++;
    }

    // IN AND OUT A CONDITION AND ASSISTANT-STATEMENTS------------------------------------------------------------
    @Override
    public void inACondCond(ACondCond node) {}
    @Override
    public void outACondCond(ACondCond node) {}
    @Override
    public void inANotCond(ANotCond node) {}
    @Override
    public void outANotCond(ANotCond node) {
        // producing IR
        String t1 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        String t2 = this.ir.NEWTEMP(null);  // because we don't have a "boolean" type, and this condition basically results in what other languages would call "boolean"
        this.ir.GENQUAD("not", t1, "-", t2);
        this.tempOperandsStack.push(t2);
        this.toPopFromTempOperandsStack++;
    }
    @Override
    public void inAAndCond(AAndCond node) {}
    @Override
    public void outAAndCond(AAndCond node) {
        // producing IR
        String t1 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        String t2 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        String t3 = this.ir.NEWTEMP(null);  // because we don't have a "boolean" type, and this condition basically results in what other languages would call "boolean"
        this.ir.GENQUAD("and", t2, t1, t3);
        this.tempOperandsStack.push(t3);
        this.toPopFromTempOperandsStack++;
    }
    @Override
    public void inAOrCond(AOrCond node) {}
    @Override
    public void outAOrCond(AOrCond node) {
        // producing IR
        String t1 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        String t2 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        String t3 = this.ir.NEWTEMP(null);  // because we don't have a "boolean" type, and this condition basically results in what other languages would call "boolean"
        this.ir.GENQUAD("or", t2, t1, t3);
        this.tempOperandsStack.push(t3);
        this.toPopFromTempOperandsStack++;
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
        if (!temp1.isSame(temp2)) {
            System.err.printf("Error: In condition one member is \"%s\" and the other member is \"%s\"\n",
                    temp1.getKind(), temp2.getKind());
            // exit with "failure" code
            System.exit(-1);
        }

        // producing IR
        String t1 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        String t2 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        String t3 = this.ir.NEWTEMP(temp1);
        this.ir.GENQUAD("=", t2, t1, t3);
        this.tempOperandsStack.push(t3);
        this.toPopFromTempOperandsStack++;
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
        if (!temp1.isSame(temp2)) {
            System.err.printf("Error: In condition one member is \"%s\" and the other member is \"%s\"\n",
                    temp1.getKind(), temp2.getKind());
            // exit with "failure" code
            System.exit(-1);
        }

        // producing IR
        String t1 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        String t2 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        String t3 = this.ir.NEWTEMP(temp1);
        this.ir.GENQUAD("#", t2, t1, t3);
        this.tempOperandsStack.push(t3);
        this.toPopFromTempOperandsStack++;
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
        if (!temp1.isSame(temp2)) {
            System.err.printf("Error: In condition one member is \"%s\" and the other member is \"%s\"\n",
                    temp1.getKind(), temp2.getKind());
            // exit with "failure" code
            System.exit(-1);
        }

        // producing IR
        String t1 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        String t2 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        String t3 = this.ir.NEWTEMP(temp1);
        this.ir.GENQUAD("<>", t2, t1, t3);
        this.tempOperandsStack.push(t3);
        this.toPopFromTempOperandsStack++;
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
        if (!temp1.isSame(temp2)) {
            System.err.printf("Error: In condition one member is \"%s\" and the other member is \"%s\"\n",
                    temp1.getKind(), temp2.getKind());
            // exit with "failure" code
            System.exit(-1);
        }

        // producing IR
        String t1 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        String t2 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        String t3 = this.ir.NEWTEMP(temp1);
        this.ir.GENQUAD("<", t2, t1, t3);
        this.tempOperandsStack.push(t3);
        this.toPopFromTempOperandsStack++;
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
        if (!temp1.isSame(temp2)) {
            System.err.printf("Error: In condition one member is \"%s\" and the other member is \"%s\"\n",
                    temp1.getKind(), temp2.getKind());
            // exit with "failure" code
            System.exit(-1);
        }

        // producing IR
        String t1 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        String t2 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        String t3 = this.ir.NEWTEMP(temp1);
        this.ir.GENQUAD(">", t2, t1, t3);
        this.tempOperandsStack.push(t3);
        this.toPopFromTempOperandsStack++;
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
        if (!temp1.isSame(temp2)) {
            System.err.printf("Error: In condition one member is \"%s\" and the other member is \"%s\"\n",
                    temp1.getKind(), temp2.getKind());
            // exit with "failure" code
            System.exit(-1);
        }

        // producing IR
        String t1 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        String t2 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        String t3 = this.ir.NEWTEMP(temp1);
        this.ir.GENQUAD("<=", t2, t1, t3);
        this.tempOperandsStack.push(t3);
        this.toPopFromTempOperandsStack++;
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
        if (!temp1.isSame(temp2)) {
            System.err.printf("Error: In condition one member is \"%s\" and the other member is \"%s\"\n",
                    temp1.getKind(), temp2.getKind());
            // exit with "failure" code
            System.exit(-1);
        }

        // producing IR
        String t1 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        String t2 = this.tempOperandsStack.pop();
        this.toPopFromTempOperandsStack--;
        String t3 = this.ir.NEWTEMP(temp1);
        this.ir.GENQUAD(">=", t2, t1, t3);
        this.tempOperandsStack.push(t3);
        this.toPopFromTempOperandsStack++;
    }

}
