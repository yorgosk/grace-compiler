package compiler;

import compiler.analysis.DepthFirstAdapter;
import compiler.node.*;

import java.util.Stack;

public class ASTPrintingVisitor extends DepthFirstAdapter {
    // Symbol-Table for Syntactical Analysis
    private SymbolTable symbolTable = new SymbolTable();
    // a Java Stack where various information are stored temporarily
    private Stack<STRecord> temp;
    // for indentation
    private int indent = 0;
    private void makeIndent() { for(int i = 0; i < indent; i++) System.out.printf("    "); }

    // IN AND OUT A PROGRAM------------------------------------------------------------
    @Override
    public void inAProgram(AProgram node) {
        // printing
        makeIndent(); System.out.printf("program :\n"); indent++;
    }
    @Override
    public void outAProgram(AProgram node) { indent--; }

    // IN AND OUT A FUNCTION DEFINITION------------------------------------------------------------
    @Override
    public void inAFuncDef(AFuncDef node) { makeIndent(); System.out.printf("function :\n"); indent++; }
    @Override
    public void outAFuncDef(AFuncDef node) { indent--; }

    // IN AND OUT A HEADER AND ASSISTANT-PRODUCTIONS------------------------------------------------------------
    @Override
    public void inAHeader(AHeader node) { makeIndent(); System.out.printf("header(\"%s\") :\n", node.getId().toString()); indent++; }
    @Override
    public void outAHeader(AHeader node) { indent--; }

    // IN A DATA TYPE------------------------------------------------------------
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
    public void inAFparType(AFparType node) { makeIndent(); System.out.printf("funcParType :\n"); indent++; }
    @Override
    public void outAFparType(AFparType node) { indent--; }

    // IN AND OUT A LOCAL DEFINITION------------------------------------------------------------
    @Override
    public void inAFuncDefLocalDef(AFuncDefLocalDef node) { makeIndent(); System.out.printf("funcDefLocalDef :\n"); indent++; }
    @Override
    public void outAFuncDefLocalDef(AFuncDefLocalDef node) { indent--; }
    @Override
    public void inAFuncDeclLocalDef(AFuncDeclLocalDef node) { makeIndent(); System.out.printf("funcDeclLocalDef :\n"); indent++; }
    @Override
    public void outAFuncDeclLocalDef(AFuncDeclLocalDef node) { indent--; }
    @Override
    public void inAVarDefLocalDef(AVarDefLocalDef node) { makeIndent(); System.out.printf("varDefLocalDef :\n"); indent++; }
    @Override
    public void outAVarDefLocalDef(AVarDefLocalDef node) { indent--; }

    // IN AND OUT A VARIABLE DEFINITION AND ASSISTANT-STATEMENT------------------------------------------------------------
    @Override
    public void inAVarDef(AVarDef node) { makeIndent(); System.out.printf("var :\"%s\"\n", node.getId().toString()); indent++;}
    @Override
    public void outAVarDef(AVarDef node) { indent--; }

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

    // IN A FUNCTION CALL AND ASSISTANT-STATEMENTS------------------------------------------------------------
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

}
