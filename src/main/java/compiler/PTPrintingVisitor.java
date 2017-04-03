package compiler;

import compiler.analysis.DepthFirstAdapter;
import compiler.node.*;

public class PTPrintingVisitor extends DepthFirstAdapter {
    // for indentation
    private int indent = 0;
    private void makeIndent() { for(int i = 0; i < indent; i++) System.out.printf("\t"); }

    // IN AND OUT A PROGRAM------------------------------------------------------------
    @Override
    public void inAProgram(AProgram node) {
        System.out.printf("\nentering a program[");
    }
    @Override
    public void outAProgram(AProgram node) {
        System.out.printf("]exiting a program...\n");
    }

    // IN AND OUT A FUNCTION DEFINITION------------------------------------------------------------
    @Override
    public void inAFuncDef(AFuncDef node) {
        System.out.printf("\nentering a function{");
    }
    @Override
    public void outAFuncDef(AFuncDef node) {
        System.out.printf("}exiting a function,\n");
    }

    // IN AND OUT A HEADER AND ASSISTANT-PRODUCTIONS------------------------------------------------------------
    @Override
    public void inAHeader(AHeader node) {
        System.out.printf("\n\"type\":\"function\",\n\"name\":\"%s\",", node.getId().toString());
    }
    @Override
    public void outAHeader(AHeader node) {
        System.out.printf("\n],");
    }
    @Override
    public void inAHeaderParams(AHeaderParams node) {
        System.out.printf("\n\"params\":[");
    }
    @Override
    public void outAHeaderParams(AHeaderParams node) {
        System.out.printf("],\n");
    }

    // IN A FUNCTION PARAMETERS DEFINITION AND ASSISTANT-STATEMENT------------------------------------------------------------
    @Override
    public void inAFparDef(AFparDef node) {
        System.out.printf("[\"%s\",", node.getId().toString());
    }
    @Override
    public void inAFparDefNextId(AFparDefNextId node) {
        System.out.printf(",\"%s\"", node.getId().toString());
    }

    // IN A DATA TYPE------------------------------------------------------------
    @Override
    public void inAIntDataType(AIntDataType node) {
        System.out.printf("\"int\"");
    }
    @Override
    public void inACharDataType(ACharDataType node) {
        System.out.printf("\"char\"");
    }

    // IN AND OUT A TYPE AND ASSISTANT-STATEMENT------------------------------------------------------------
    @Override
    public void inAType(AType node) {
        System.out.printf("\"type\":(");
    }
    @Override
    public void outAType(AType node) {
        System.out.printf(")\n");
    }
    @Override
    public void inADimension(ADimension node) {
        System.out.printf("[%s]", node.getIntConst().toString());
    }

    // IN AND OUT A RETURN TYPE AND ASSISTANT-STATEMENTS------------------------------------------------------------
    @Override
    public void inADataTypeRetType(ADataTypeRetType node) {
        System.out.printf("\"retType\":(");
    }
    @Override
    public void outADataTypeRetType(ADataTypeRetType node) {
        System.out.printf(")\n");
    }
    @Override
    public void inANothingRetType(ANothingRetType node) {
        System.out.printf("\"retType\":\"nothing\"");
    }
    @Override
    public void outANothingRetType(ANothingRetType node) {
        System.out.printf("\n");
    }

    // IN AND OUT A FUNCTION PARAMETER TYPE AND ASSISTANT-STATEMENT------------------------------------------------------------
    @Override
    public void inAFparType(AFparType node) {
        System.out.printf("\"fparType\":(");
    }
    @Override
    public void outAFparType(AFparType node) {
        System.out.printf("),\n");
    }
    @Override
    public void inALRBrackets(ALRBrackets node) {
        System.out.printf("[]");
    }
    @Override
    public void inANextDimension(ANextDimension node) {
        System.out.printf("[%s]", node.getIntConst().toString());
    }

    // IN AND OUT A LOCAL DEFINITION------------------------------------------------------------
    @Override
    public void inAFuncDefLocalDef(AFuncDefLocalDef node) {
        System.out.printf("\"funcDefLocalDef\":");
    }
    @Override
    public void outAFuncDefLocalDef(AFuncDefLocalDef node) {
        System.out.printf("\n");
    }
    @Override
    public void inAFuncDeclLocalDef(AFuncDeclLocalDef node) {
        System.out.printf("\"funcDeclLocalDef\":");
    }
    @Override
    public void outAFuncDeclLocalDef(AFuncDeclLocalDef node) {
        System.out.printf("\n");
    }
    @Override
    public void inAVarDefLocalDef(AVarDefLocalDef node) {
        System.out.printf("inAVarDefLocalDef\n");
    }
    @Override
    public void outAVarDefLocalDef(AVarDefLocalDef node) {
        System.out.printf("\n");
    }

    // IN AND OUT A VARIABLE DEFINITION AND ASSISTANT-STATEMENT------------------------------------------------------------
    @Override
    public void inAVarDef(AVarDef node) {
        System.out.printf("\"var\":[\n\"%s\"", node.getId().toString());
    }
    @Override
    public void outAVarDef(AVarDef node) {
        System.out.printf("\n] with ");
    }
    @Override
    public void inAVarDefNextId(AVarDefNextId node) {
        System.out.printf(",\"%s\"", node.getId().toString());
    }

    // IN AND OUT A FUNCTION DECLARATION------------------------------------------------------------
    @Override
    public void inAFuncDecl(AFuncDecl node) {
        System.out.printf("\"funcDecl\":(");
    }
    @Override
    public void outAFuncDecl(AFuncDecl node) {
        System.out.printf("),\n");
    }

    // IN A STATEMENT AND ASSISTANT-STATEMENTS------------------------------------------------------------
    @Override
    public void inATerminalStmt(ATerminalStmt node) {
        System.out.printf("\"terminalStmt\":(");
    }
    @Override
    public void outATerminalStmt(ATerminalStmt node) {
        System.out.printf("),\n");
    }
    @Override
    public void inAIfStmt(AIfStmt node) {
        System.out.printf("\"ifStmt\":(");
    }
    @Override
    public void outAIfStmt(AIfStmt node) {
        System.out.printf("),\n");
    }
    @Override
    public void inAIfIfStmt(AIfIfStmt node) {
        System.out.printf("\"ifIfStmt\":(");
    }
    @Override
    public void outAIfIfStmt(AIfIfStmt node) {
        System.out.printf("),\n");
    }
    @Override
    public void inAIfElseIfStmt(AIfElseIfStmt node) {
        System.out.printf("\"ifElseIfStmt\":(");
    }
    @Override
    public void outAIfElseIfStmt(AIfElseIfStmt node) {
        System.out.printf("),\n");
    }
    @Override
    public void inAElseWithElse(AElseWithElse node) {
        System.out.printf("\"elseWithElse\"");
    }
    @Override
    public void inANestedIfElseWithElse(ANestedIfElseWithElse node) {
        System.out.printf("\"nestedIfElseWithElse\"");
    }
    @Override
    public void inATerminalStatementWithElse(ATerminalStatementWithElse node) {
        System.out.printf("\"terminalStatementWithElse\"");
    }
    @Override
    public void inAWhileStmt(AWhileStmt node) {
        System.out.printf("\"whileStmt\"");
    }
    @Override
    public void inASemicolonTermStmt(ASemicolonTermStmt node) {
        System.out.printf("\";\"");
    }
    @Override
    public void inAAssignmentTermStmt(AAssignmentTermStmt node) {
        System.out.printf("\"%s\"<-", node.getLValue().toString());
    }
    @Override
    public void inABlockTermStmt(ABlockTermStmt node) {
        System.out.printf("\"blockTermStmt\":(");
    }
    @Override
    public void outABlockTermStmt(ABlockTermStmt node) {
        System.out.printf("),\n");
    }
    @Override
    public void inAFunctionTermStmt(AFunctionTermStmt node) {
        System.out.printf("\"functionTermStmt\":(");
    }
    @Override
    public void outAFunctionTermStmt(AFunctionTermStmt node) {
        System.out.printf("),\n");
    }
    @Override
    public void inAReturnExprTermStmt(AReturnExprTermStmt node) {
        System.out.printf("\"returnExprTermStmt\":(");
    }
    @Override
    public void outAReturnExprTermStmt(AReturnExprTermStmt node) {
        System.out.printf("),\n");
    }

    // IN AND OUT A CODE BLOCK------------------------------------------------------------
    @Override
    public void inABlock(ABlock node) {
        System.out.printf("\"body\":[\n");
    }
    @Override
    public void outABlock(ABlock node) {
        System.out.printf("],\n");
    }

    // IN A FUNCTION CALL AND ASSISTANT-STATEMENTS------------------------------------------------------------
    @Override
    public void inAFuncCall(AFuncCall node) {
        System.out.printf("\"funcId\":\"%s\"", node.getId().toString());
    }
    @Override
    public void inAFuncArgs(AFuncArgs node) {
        System.out.printf("\"funcArgs\":[\n");
    }
    @Override
    public void outAFuncArgs(AFuncArgs node) {
        System.out.printf("\n],\n");
    }
    @Override
    public void inAFuncCallNextArg(AFuncCallNextArg node) {
        System.out.printf(",");
    }

    // IN A L-VALUE AND ASSISTANT-STATEMENTS------------------------------------------------------------
    @Override
    public void inAIdLValue(AIdLValue node) {
        System.out.printf("\"%s\"", node.getId().toString());
    }
    @Override
    public void inAStringLValue(AStringLValue node) {
        System.out.printf("\"%s\"", node.getStringLiteral().toString());
    }
    @Override
    public void inAExpressionLValue(AExpressionLValue node) { System.out.printf("expressionLValue("); }
    @Override
    public void outAExpressionLValue(AExpressionLValue node) { System.out.printf("),\n"); }

    // IN AN EXPRESSION AND ASSISTANT STATEMENTS------------------------------------------------------------
    @Override
    public void inASignedExpr(ASignedExpr node) { System.out.printf("signedExpr("); }
    @Override
    public void outASignedExpr(ASignedExpr node) { System.out.printf("),\n"); }
    @Override
    public void inAExprPlusTermExpr(AExprPlusTermExpr node) { System.out.printf("exprPlusTermExpr("); }
    @Override
    public void outAExprPlusTermExpr(AExprPlusTermExpr node) { System.out.printf("),\n"); }
    @Override
    public void inAExprMinusTermExpr(AExprMinusTermExpr node) { System.out.printf("exprMinusTermExpr("); }
    @Override
    public void outAExprMinusTermExpr(AExprMinusTermExpr node) { System.out.printf("),\n"); }
    @Override
    public void inATermExpr(ATermExpr node) { System.out.printf("termExpr("); }
    @Override
    public void outATermExpr(ATermExpr node) { System.out.printf("),\n"); }
    @Override
    public void outASignIntConstSignedExpr(ASignIntConstSignedExpr node) {
        System.out.printf("\"%s\"\n", node.getIntConst().toString());
    }
    @Override
    public void outASignCharConstSignedExpr(ASignCharConstSignedExpr node) {
        System.out.printf("\"%s\"\n", node.getCharConst().toString());
    }
    @Override
    public void inASignLValueSignedExpr(ASignLValueSignedExpr node) { System.out.printf(""); }
    @Override
    public void inASignFuncCallSignedExpr(ASignFuncCallSignedExpr node) { System.out.printf(""); }
    @Override
    public void inASignExprInParsSignedExpr(ASignExprInParsSignedExpr node) { System.out.printf(""); }
    @Override
    public void inAPlusSignSign(APlusSignSign node) { System.out.printf("\"+\","); }
    @Override
    public void inAMinusSignSign(AMinusSignSign node) { System.out.printf("\"-\","); }
    @Override
    public void inATermMultFactorTerm(ATermMultFactorTerm node) { System.out.printf("termMultFactorTerm("); }
    @Override
    public void outATermMultFactorTerm(ATermMultFactorTerm node) { System.out.printf("),\n"); }
    @Override
    public void inATermDivFactorTerm(ATermDivFactorTerm node) { System.out.printf("termDivFactorTerm("); }
    @Override
    public void outATermDivFactorTerm(ATermDivFactorTerm node) { System.out.printf("),\n"); }
    @Override
    public void inATermModFactorTerm(ATermModFactorTerm node) { System.out.printf("termModFactorTerm("); }
    @Override
    public void outATermModFactorTerm(ATermModFactorTerm node) { System.out.printf("),\n"); }
    @Override
    public void inAFactorTerm(AFactorTerm node) { System.out.printf("factorTerm("); }
    @Override
    public void outAFactorTerm(AFactorTerm node) { System.out.printf("),\n"); }
    @Override
    public void inAIntConstFactor(AIntConstFactor node) {
        System.out.printf("\"%s\"", node.getIntConst().toString());
    }
    @Override
    public void inACharConstFactor(ACharConstFactor node) {
        System.out.printf("\"%s\"", node.getCharConst().toString());
    }
    @Override
    public void inALValueFactor(ALValueFactor node) { System.out.printf("lValueFactor("); }
    @Override
    public void outALValueFactor(ALValueFactor node) { System.out.printf("),\n"); }
    @Override
    public void inAFuncCallFactor(AFuncCallFactor node) { System.out.printf("funcCallFactor("); }
    @Override
    public void outAFuncCallFactor(AFuncCallFactor node) { System.out.printf("),\n"); }
    @Override
    public void inAExprInParsFactor(AExprInParsFactor node) { System.out.printf("exprInParsFactor("); }
    @Override
    public void outAExprInParsFactor(AExprInParsFactor node) { System.out.printf("),\n"); }
    @Override
    public void inAExprInPars(AExprInPars node) { System.out.printf("("); }
    @Override
    public void outAExprInPars(AExprInPars node) { System.out.printf(")"); }

    // IN A CONDITION AND ASSISTANT STATEMENTS------------------------------------------------------------
    @Override
    public void inANotCond(ANotCond node) { System.out.printf("notCond("); }
    @Override
    public void outANotCond(ANotCond node) { System.out.printf("),\n"); }
    @Override
    public void inACondAndTermcondCond(ACondAndTermcondCond node) { System.out.printf("termAndTermCond("); }
    @Override
    public void outACondAndTermcondCond(ACondAndTermcondCond node) { System.out.printf("),\n"); }
    @Override
    public void inACondOrTermcondCond(ACondOrTermcondCond node) { System.out.printf("condOrTermcondCond("); }
    @Override
    public void outACondOrTermcondCond(ACondOrTermcondCond node) { System.out.printf("),\n"); }
    @Override
    public void inATermcondCond(ATermcondCond node) { System.out.printf("termcondCond("); }
    @Override
    public void outATermcondCond(ATermcondCond node) { System.out.printf("),\n"); }
    @Override
    public void inANotNotCondNotCond(ANotNotCondNotCond node) { System.out.printf("\"not\","); }
    @Override
    public void inANotTermcondNotCond(ANotTermcondNotCond node) { System.out.printf("\"not\","); }
    @Override
    public void inACondInParsTermcond(ACondInParsTermcond node) { System.out.printf("condInParsTermcond("); }
    @Override
    public void outACondInParsTermcond(ACondInParsTermcond node) { System.out.printf("),\n"); }
    @Override
    public void inAExprNumopExprTermcond(AExprNumopExprTermcond node) { System.out.printf("exprNumopExprTermcond("); }
    @Override
    public void outAExprNumopExprTermcond(AExprNumopExprTermcond node) { System.out.printf("),\n"); }
    @Override
    public void inACondInPars(ACondInPars node) { System.out.printf("condInPars("); }
    @Override
    public void outACondInPars(ACondInPars node) { System.out.printf("),\n"); }
    @Override
    public void inANumopExpr(ANumopExpr node) { System.out.printf("numopExpr("); }
    @Override
    public void outANumopExpr(ANumopExpr node) { System.out.printf("),\n"); }
    @Override
    public void inAEqualNumop(AEqualNumop node) { System.out.printf("\"=\","); }
    @Override
    public void inAHashtagNumop(AHashtagNumop node) { System.out.printf("\"#\","); }
    @Override
    public void inAUnequalNumop(AUnequalNumop node) { System.out.printf("\"<>\","); }
    @Override
    public void inALesserNumop(ALesserNumop node) {  System.out.printf("\"<\","); }
    @Override
    public void inAGreaterNumop(AGreaterNumop node) { System.out.printf("\">\","); }
    @Override
    public void inALesseqNumop(ALesseqNumop node) { System.out.printf("\"<=\","); }
    @Override
    public void inAGreateqNumop(AGreateqNumop node) { System.out.printf("\">=\","); }
    //------------------------------------------------------------
}
