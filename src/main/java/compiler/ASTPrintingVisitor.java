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
    // a Java Stack where various information are stored temporarily
    private Stack<STRecord> tempRecordStack;
    private Integer toPopFromTempRecordStack;
    private Stack<STRecord.Type> tempTypeStack;
    private Integer toPopFromTempTypeStack;
    private boolean isDecl;
    private boolean hasMain;

    // IN AND OUT A PROGRAM------------------------------------------------------------
    @Override
    public void inAProgram(AProgram node) { makeIndent(); System.out.printf("program :\n"); indent++;
        this.symbolTable = new SymbolTable();
        this.tempRecordStack = new Stack<STRecord>();
        this.toPopFromTempRecordStack = 0;
        this.tempTypeStack = new Stack<STRecord.Type>();
        this.toPopFromTempTypeStack = 0;
        this.isDecl = false;
        this.hasMain = false;
    }
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
    public void outAFuncDef(AFuncDef node) { indent--; }

    // IN AND OUT A HEADER AND ASSISTANT-PRODUCTIONS------------------------------------------------------------
    @Override
    public void inAHeader(AHeader node) { makeIndent(); System.out.printf("header(\"%s\") :\n", node.getId().toString()); indent++;
        // keep the name of the function
        STRecord temp = new STRecord();

        temp.type = new STRecord.Type();
        temp.type.setKind(node.getRetType().toString());
        // check for main-function existence
        // source: http://stackoverflow.com/questions/17973964/how-to-compare-two-strings-in-java-without-considering-spaces
        if (!this.hasMain && !node.getId().toString().trim().replaceAll("\\s+", " ").equalsIgnoreCase("main".trim().replaceAll("\\s+", " "))) {
            System.err.printf("Error: All Grace programs must have a \"main\" function\n");
            // exit with "failure" code
            System.exit(-1);
        } else {
            this.hasMain = true;
            temp.setName(node.getId().toString());
        }
        temp.setFunc(true);
        temp.setFuncDecl(this.isDecl);
        this.tempRecordStack.push(temp);
        this.toPopFromTempRecordStack++;
    }
    @Override
    public void outAHeader(AHeader node) { indent--;
        // insert the header's names to our Symbol-Table
        STRecord temp;
        while (this.toPopFromTempRecordStack != 0) {
            temp = this.tempRecordStack.pop();
            this.symbolTable.insert(temp);
            toPopFromTempRecordStack--;
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
        // keep the name of the parameters
        boolean ref = node.getRef() != null;
        String type = node.getFparType().toString();
        STRecord.Type tempType = null;
        if(node.getId() != null)
        {
            STRecord tempRec = new STRecord();

            tempType = this.tempTypeStack.pop();
            this.toPopFromTempTypeStack--;
            tempRec.type = new STRecord.Type(tempType);
            tempRec.setName(node.getId().toString());
            tempRec.setRef(ref);
            this.tempRecordStack.push(tempRec);
            this.toPopFromTempRecordStack++;
        }
        {
            List<TId> copy = new ArrayList<TId>(node.getNext());
            for(TId e : copy)
            {
                STRecord tempRec = new STRecord();

                tempRec.type = new STRecord.Type(tempType);
                tempRec.setName(e.toString());
                tempRec.setRef(ref);
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
    public void inAType(AType node) { makeIndent(); System.out.printf("type :\n"); indent++;
        STRecord.Type temp = new STRecord.Type();
        temp.setKind(node.getDataType().toString());
        if(node.getIntConst().size() > 0) {
            temp.setArray(true);
            temp.setDimension(node.getIntConst().size());
        }
        this.tempTypeStack.push(temp);
        this.toPopFromTempTypeStack++;
    }
    @Override
    public void outAType(AType node) { indent--; }

    // IN AND OUT A RETURN TYPE AND ASSISTANT-STATEMENTS------------------------------------------------------------
    @Override
    public void inADataTypeRetType(ADataTypeRetType node) { makeIndent(); System.out.printf("retType :\n"); indent++; }
    @Override
    public void outADataTypeRetType(ADataTypeRetType node) {  indent--; }
    @Override
    public void inANothingRetType(ANothingRetType node) { makeIndent(); System.out.printf("retType :\"nothing\"\n"); }

    // IN AND OUT A FUNCTION PARAMETER TYPE AND ASSISTANT-STATEMENT------------------------------------------------------------
    @Override
    public void inAFparType(AFparType node) { makeIndent(); System.out.printf("funcParType :\n"); indent++;
        STRecord.Type temp = new STRecord.Type();
        temp.setKind(node.getDataType().toString());
        if(node.getIntConst().size() > 0) {
            temp.setArray(true);
            temp.setDimension(node.getIntConst().size());
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
    public void outAFuncDefLocalDef(AFuncDefLocalDef node) { indent--; }
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
            this.symbolTable.insert(temp);
            toPopFromTempRecordStack--;
        }
        // for debugging
        this.symbolTable.printSTStructures();
        assert (toPopFromTempRecordStack == 0);
    }

    // IN AND OUT A VARIABLE DEFINITION AND ASSISTANT-STATEMENT------------------------------------------------------------
    @Override
    public void inAVarDef(AVarDef node) { makeIndent(); System.out.printf("var :\"%s\"\n", node.getId().toString()); indent++; }
    @Override
    public void outAVarDef(AVarDef node) { indent--;
        // keep the name of the parameters
        String type = node.getType().toString();
        STRecord.Type tempType = null;
        if(node.getId() != null)
        {
            STRecord tempRec = new STRecord();

            tempType = this.tempTypeStack.pop();
            this.toPopFromTempTypeStack--;
            tempRec.type = new STRecord.Type(tempType);
            tempRec.setName(node.getId().toString());
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
                tempRec.setName(e.toString());
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
    public void inAFuncCall(AFuncCall node) { makeIndent(); System.out.printf("func-call( \"%s\" ) :\n", node.getId().toString()); indent++; }
    @Override
    public void outAFuncCall(AFuncCall node) { indent--; }

    // IN A L-VALUE AND ASSISTANT-STATEMENTS------------------------------------------------------------
    @Override
    public void inAIdLValue(AIdLValue node) { makeIndent(); System.out.printf("\"%s\"\n", node.getId().toString()); }
    @Override
    public void inAStringLValue(AStringLValue node) { makeIndent(); System.out.printf("\"%s\"\n", node.getStringLiteral().toString()); }
    @Override
    public void inAExpressionLValue(AExpressionLValue node) { makeIndent(); System.out.printf("exprLValue :\n"); }

    // IN AND OUT A EXPRESSION AND ASSISTANT-STATEMENTS------------------------------------------------------------
    @Override
    public void inAIntConstExpr(AIntConstExpr node) {}
    @Override
    public void outAIntConstExpr(AIntConstExpr node) {}
    @Override
    public void inACharConstExpr(ACharConstExpr node) {}
    @Override
    public void outACharConstExpr(ACharConstExpr node) {}
    @Override
    public void inALValueExpr(ALValueExpr node) {}
    @Override
    public void outALValueExpr(ALValueExpr node) {}
    @Override
    public void inAFuncCallExpr(AFuncCallExpr node) {}
    @Override
    public void outAFuncCallExpr(AFuncCallExpr node) {}
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
            System.err.printf("Error: In \"mod\" expression one member is %s and the other member is %s\n",
                    temp1.getKind(), temp2.getKind());
            // exit with "failure" code
            System.exit(-1);
        }
    }

    // IN AND OUT A CONDITION AND ASSISTANT-STATEMENTS------------------------------------------------------------
    @Override
    public void inACondCond(ACondCond node) {}
    @Override
    public void outACondCond(ACondCond node) {}
    @Override
    public void inAAndCond(AAndCond node) {}
    @Override
    public void outAAndCond(AAndCond node) {}
    @Override
    public void inAOrCond(AOrCond node) {}
    @Override
    public void outAOrCond(AOrCond node) {}
    @Override
    public void inANumopCond(ANumopCond node) {}
    @Override
    public void outANumopCond(ANumopCond node) {}

}
