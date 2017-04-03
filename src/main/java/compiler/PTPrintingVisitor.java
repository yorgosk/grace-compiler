package compiler;

import compiler.analysis.DepthFirstAdapter;
import compiler.node.*;

public class PTPrintingVisitor extends DepthFirstAdapter {
    // for indentation
    private int indent = 0;
    private void makeIndent() { for(int i = 0; i < indent; i++) System.out.printf("\t"); }

    // IN AND OUT A PROGRAM------------------------------------------------------------
    @Override
    public void inAProgram(AProgram node) { makeIndent(); System.out.printf("entering a program[\n"); indent++; }
    @Override
    public void outAProgram(AProgram node) { indent--; makeIndent(); System.out.printf("]exiting a program...\n"); }

    // IN AND OUT A FUNCTION DEFINITION------------------------------------------------------------
    @Override
    public void inAFuncDef(AFuncDef node) { makeIndent(); System.out.printf("entering a function{\n"); indent++; }
    @Override
    public void outAFuncDef(AFuncDef node) { indent--; makeIndent(); System.out.printf("}exiting a function\n"); }

    // IN AND OUT A HEADER AND ASSISTANT-PRODUCTIONS------------------------------------------------------------
    @Override
    public void inAHeader(AHeader node) { makeIndent(); System.out.printf("header of \"%s\"(\n", node.getId().toString()); indent++; }
    @Override
    public void outAHeader(AHeader node) { indent--; makeIndent(); System.out.printf(")end of header\n"); }
    @Override
    public void inAHeaderParams(AHeaderParams node) { makeIndent(); System.out.printf("function parameters[\n"); indent++; }
    @Override
    public void outAHeaderParams(AHeaderParams node) { indent--; makeIndent(); System.out.printf("]end of parameters\n"); }

    // IN A FUNCTION PARAMETERS DEFINITION AND ASSISTANT-STATEMENT------------------------------------------------------------
    @Override
    public void inAFparDef(AFparDef node) { System.out.printf("\"%s\"", node.getId().toString()); }
    @Override
    public void inAFparDefNextId(AFparDefNextId node) { System.out.printf(",\"%s\"", node.getId().toString()); }

    // IN A DATA TYPE------------------------------------------------------------
    @Override
    public void inAIntDataType(AIntDataType node) { System.out.printf("\"int\"");  }
    @Override
    public void inACharDataType(ACharDataType node) { System.out.printf("\"char\""); }

    // IN AND OUT A TYPE AND ASSISTANT-STATEMENT------------------------------------------------------------
    @Override
    public void inAType(AType node) { makeIndent(); System.out.printf("type(\n"); indent++; }
    @Override
    public void outAType(AType node) { indent--; makeIndent(); System.out.printf(")\n"); }
    @Override
    public void inADimension(ADimension node) { System.out.printf("[%s]", node.getIntConst().toString()); }

    // IN AND OUT A RETURN TYPE AND ASSISTANT-STATEMENTS------------------------------------------------------------
    @Override
    public void inADataTypeRetType(ADataTypeRetType node) { makeIndent(); System.out.printf("returnedType(\n"); indent++; }
    @Override
    public void outADataTypeRetType(ADataTypeRetType node) {  indent--; makeIndent(); System.out.printf(")\n");}
    @Override
    public void inANothingRetType(ANothingRetType node) { makeIndent(); System.out.printf("returnedType(\"nothing\""); }
    @Override
    public void outANothingRetType(ANothingRetType node) { System.out.printf(")\n"); }

    // IN AND OUT A FUNCTION PARAMETER TYPE AND ASSISTANT-STATEMENT------------------------------------------------------------
    @Override
    public void inAFparType(AFparType node) { makeIndent(); System.out.printf("functionParameterType(\n"); indent++; }
    @Override
    public void outAFparType(AFparType node) { indent--; makeIndent(); System.out.printf(")\n"); }
    @Override
    public void inALRBrackets(ALRBrackets node) { System.out.printf("[]"); }
    @Override
    public void inANextDimension(ANextDimension node) { System.out.printf("[%s]", node.getIntConst().toString()); }

    // IN AND OUT A LOCAL DEFINITION------------------------------------------------------------
    @Override
    public void inAFuncDefLocalDef(AFuncDefLocalDef node) { makeIndent(); System.out.printf("functionDefinitionLocalDefinition(\n"); indent++; }
    @Override
    public void outAFuncDefLocalDef(AFuncDefLocalDef node) { indent--; makeIndent(); System.out.printf(")out of functionDefinitionLocalDefinition\n"); }
    @Override
    public void inAFuncDeclLocalDef(AFuncDeclLocalDef node) { makeIndent(); System.out.printf("functionDeclarationLocalDefinition(\n"); indent++; }
    @Override
    public void outAFuncDeclLocalDef(AFuncDeclLocalDef node) { indent--; makeIndent(); System.out.printf(")out of functionDeclarationLocalDefinition\n"); }
    @Override
    public void inAVarDefLocalDef(AVarDefLocalDef node) { makeIndent(); System.out.printf("variableDefinitionLocalDefinition(\n"); indent++; }
    @Override
    public void outAVarDefLocalDef(AVarDefLocalDef node) { indent--; makeIndent(); System.out.printf(")out of variableDefinitionLocalDefinition\n"); }

    // IN AND OUT A VARIABLE DEFINITION AND ASSISTANT-STATEMENT------------------------------------------------------------
    @Override
    public void inAVarDef(AVarDef node) { makeIndent(); System.out.printf("variable[\"%s\"", node.getId().toString()); indent++;}
    @Override
    public void outAVarDef(AVarDef node) { indent--; makeIndent(); System.out.printf("]"); }
    @Override
    public void inAVarDefNextId(AVarDefNextId node) { System.out.printf(",\"%s\"", node.getId().toString()); }

    // IN AND OUT A FUNCTION DECLARATION------------------------------------------------------------
    @Override
    public void inAFuncDecl(AFuncDecl node) { makeIndent(); System.out.printf("functionDeclaration(\n"); indent++; }
    @Override
    public void outAFuncDecl(AFuncDecl node) { indent--; makeIndent(); System.out.printf(")out of functionDeclaration\n"); }

    // IN A STATEMENT AND ASSISTANT-STATEMENTS------------------------------------------------------------
    @Override
    public void inATerminalStmt(ATerminalStmt node) { makeIndent(); System.out.printf("terminalStatement(\n"); indent++; }
    @Override
    public void outATerminalStmt(ATerminalStmt node) { indent--; makeIndent(); System.out.printf(")\n"); }
    @Override
    public void inAIfStmt(AIfStmt node) { makeIndent(); System.out.printf("ifStatement(\n"); indent++;}
    @Override
    public void outAIfStmt(AIfStmt node) { indent--; makeIndent(); System.out.printf(")out of ifStatement\n"); }
    @Override
    public void inAIfIfStmt(AIfIfStmt node) { makeIndent(); System.out.printf("ifIfStatement(\n"); indent++; }
    @Override
    public void outAIfIfStmt(AIfIfStmt node) { indent--; makeIndent(); System.out.printf(")out of ifIfStatement\n"); }
    @Override
    public void inAIfElseIfStmt(AIfElseIfStmt node) { makeIndent(); System.out.printf("ifElseIfStatement(\n"); indent++; }
    @Override
    public void outAIfElseIfStmt(AIfElseIfStmt node) { indent--; makeIndent(); System.out.printf(")out of ifElseIfStatement\n"); }
    @Override
    public void inAElseWithElse(AElseWithElse node) { makeIndent(); System.out.printf("elseWithElse(\n"); indent++; }
    @Override
    public void outAElseWithElse(AElseWithElse node) { indent--; makeIndent(); System.out.printf(")out of elseWithElse\n"); }
    @Override
    public void inANestedIfElseWithElse(ANestedIfElseWithElse node) { makeIndent(); System.out.printf("nestedIfElseWithElse(\n"); indent++; }
    @Override
    public void outANestedIfElseWithElse(ANestedIfElseWithElse node) { indent--; makeIndent(); System.out.printf(")out of nestedIfElseWithElse\n"); }
    @Override
    public void inATerminalStatementWithElse(ATerminalStatementWithElse node) { makeIndent(); System.out.printf("terminalStatementWithElse(\n"); indent++; }
    @Override
    public void outATerminalStatementWithElse(ATerminalStatementWithElse node) { indent--; makeIndent(); System.out.printf(")out of terminalStatementWithElse\n"); }
    @Override
    public void inAWhileStmt(AWhileStmt node) { makeIndent(); System.out.printf("whileStatement(\n"); indent++; }
    @Override
    public void outAWhileStmt(AWhileStmt node) { indent--; makeIndent(); System.out.printf(")out of whileStatement\n"); }
    @Override
    public void inASemicolonTermStmt(ASemicolonTermStmt node) { System.out.printf("\";\""); }
    @Override
    public void inAAssignmentTermStmt(AAssignmentTermStmt node) { System.out.printf("\"%s\"<-", node.getLValue().toString()); }
    @Override
    public void inABlockTermStmt(ABlockTermStmt node) { makeIndent(); System.out.printf("blockTerminalStatement(\n"); indent++; }
    @Override
    public void outABlockTermStmt(ABlockTermStmt node) { indent--; makeIndent(); System.out.printf(")out of blockTerminalStatement\n"); }
    @Override
    public void inAFunctionTermStmt(AFunctionTermStmt node) { makeIndent(); System.out.printf("functionTerminalStatement(\n"); indent++; }
    @Override
    public void outAFunctionTermStmt(AFunctionTermStmt node) { indent--;makeIndent(); System.out.printf(")out of functionTerminalStatement\n"); }
    @Override
    public void inAReturnExprTermStmt(AReturnExprTermStmt node) { makeIndent(); System.out.printf("returnExpressionTerminalStatement(\n"); indent++; }
    @Override
    public void outAReturnExprTermStmt(AReturnExprTermStmt node) { indent--; makeIndent(); System.out.printf(")out of returnExpressionTerminalStatement\n"); }

    // IN AND OUT A CODE BLOCK------------------------------------------------------------
    @Override
    public void inABlock(ABlock node) { makeIndent(); System.out.printf("code block body[\n"); indent++;}
    @Override
    public void outABlock(ABlock node) { indent--; makeIndent(); System.out.printf("]end of code block body\n"); }

    // IN A FUNCTION CALL AND ASSISTANT-STATEMENTS------------------------------------------------------------
    @Override
    public void inAFuncCall(AFuncCall node) { makeIndent(); System.out.printf("calling function \"%s\"(\n", node.getId().toString()); indent++; }
    @Override
    public void outAFuncCall(AFuncCall node) { indent--; makeIndent(); System.out.printf(")end of funtion call\n"); }
    @Override
    public void inAFuncArgs(AFuncArgs node) { makeIndent(); System.out.printf("function arguments[\n"); indent++;}
    @Override
    public void outAFuncArgs(AFuncArgs node) { indent--; makeIndent(); System.out.printf("]out of function arguments\n"); }
    @Override
    public void inAFuncCallNextArg(AFuncCallNextArg node) { System.out.printf(","); }

    // IN A L-VALUE AND ASSISTANT-STATEMENTS------------------------------------------------------------
    @Override
    public void inAIdLValue(AIdLValue node) { System.out.printf("\"%s\"", node.getId().toString()); }
    @Override
    public void inAStringLValue(AStringLValue node) { System.out.printf("\"%s\"", node.getStringLiteral().toString()); }
    @Override
    public void inAExpressionLValue(AExpressionLValue node) { System.out.printf("expressionLValue(\n"); }
    @Override
    public void outAExpressionLValue(AExpressionLValue node) { System.out.printf(")\n"); }

    // IN AN EXPRESSION AND ASSISTANT STATEMENTS------------------------------------------------------------
    @Override
    public void inASignedExpr(ASignedExpr node) { makeIndent(); System.out.printf("signedExpr(\n"); indent++; }
    @Override
    public void outASignedExpr(ASignedExpr node) { indent--; makeIndent(); System.out.printf(")\n"); }
    @Override
    public void inAExprPlusTermExpr(AExprPlusTermExpr node) { makeIndent(); System.out.printf("exprPlusTermExpr(\n"); indent++; }
    @Override
    public void outAExprPlusTermExpr(AExprPlusTermExpr node) { indent--; makeIndent(); System.out.printf(")\n"); }
    @Override
    public void inAExprMinusTermExpr(AExprMinusTermExpr node) { makeIndent(); System.out.printf("exprMinusTermExpr(\n"); indent++; }
    @Override
    public void outAExprMinusTermExpr(AExprMinusTermExpr node) { indent--; makeIndent(); System.out.printf(")\n"); }
    @Override
    public void inATermExpr(ATermExpr node) { makeIndent(); System.out.printf("termExpr(\n"); indent++; }
    @Override
    public void outATermExpr(ATermExpr node) { indent--; makeIndent(); System.out.printf(")\n"); }
    @Override
    public void outASignIntConstSignedExpr(ASignIntConstSignedExpr node) { System.out.printf("\"%s\"", node.getIntConst().toString()); }
    @Override
    public void outASignCharConstSignedExpr(ASignCharConstSignedExpr node) { System.out.printf("\"%s\"", node.getCharConst().toString()); }
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
    public void inATermMultFactorTerm(ATermMultFactorTerm node) { makeIndent(); System.out.printf("termMultFactorTerm(\n"); indent++; }
    @Override
    public void outATermMultFactorTerm(ATermMultFactorTerm node) { indent--; makeIndent(); System.out.printf(")\n"); }
    @Override
    public void inATermDivFactorTerm(ATermDivFactorTerm node) { makeIndent(); System.out.printf("termDivFactorTerm(\n"); indent++; }
    @Override
    public void outATermDivFactorTerm(ATermDivFactorTerm node) { indent--; makeIndent(); System.out.printf(")\n"); }
    @Override
    public void inATermModFactorTerm(ATermModFactorTerm node) { makeIndent(); System.out.printf("termModFactorTerm(\n"); indent++; }
    @Override
    public void outATermModFactorTerm(ATermModFactorTerm node) { indent--; makeIndent(); System.out.printf(")\n"); }
    @Override
    public void inAFactorTerm(AFactorTerm node) { makeIndent(); System.out.printf("factorTerm(\n");  indent++; }
    @Override
    public void outAFactorTerm(AFactorTerm node) { indent--; makeIndent(); System.out.printf(")\n"); }
    @Override
    public void inAIntConstFactor(AIntConstFactor node) { System.out.printf("\"%s\"", node.getIntConst().toString()); }
    @Override
    public void inACharConstFactor(ACharConstFactor node) { System.out.printf("\"%s\"", node.getCharConst().toString()); }
    @Override
    public void inALValueFactor(ALValueFactor node) { makeIndent(); System.out.printf("lValueFactor("); }
    @Override
    public void outALValueFactor(ALValueFactor node) { System.out.printf(")\n"); }
    @Override
    public void inAFuncCallFactor(AFuncCallFactor node) { makeIndent(); System.out.printf("funcCallFactor(\n"); indent++; }
    @Override
    public void outAFuncCallFactor(AFuncCallFactor node) { indent--; makeIndent(); System.out.printf(")\n"); }
    @Override
    public void inAExprInParsFactor(AExprInParsFactor node) { makeIndent(); System.out.printf("exprInParsFactor(\n"); indent++; }
    @Override
    public void outAExprInParsFactor(AExprInParsFactor node) { indent--; makeIndent(); System.out.printf(")\n"); }
    @Override
    public void inAExprInPars(AExprInPars node) { System.out.printf("("); }
    @Override
    public void outAExprInPars(AExprInPars node) { System.out.printf(")"); }

    // IN A CONDITION AND ASSISTANT STATEMENTS------------------------------------------------------------
    @Override
    public void inANotCond(ANotCond node) { makeIndent(); System.out.printf("notCond(\n"); indent++; }
    @Override
    public void outANotCond(ANotCond node) { indent--; makeIndent(); System.out.printf(")\n"); }
    @Override
    public void inACondAndTermcondCond(ACondAndTermcondCond node) { makeIndent(); System.out.printf("termAndTermCond(\n"); indent++; }
    @Override
    public void outACondAndTermcondCond(ACondAndTermcondCond node) { indent--; makeIndent(); System.out.printf(")\n"); }
    @Override
    public void inACondOrTermcondCond(ACondOrTermcondCond node) { makeIndent(); System.out.printf("condOrTermcondCond(\n"); indent++; }
    @Override
    public void outACondOrTermcondCond(ACondOrTermcondCond node) { indent--; makeIndent(); System.out.printf(")\n"); }
    @Override
    public void inATermcondCond(ATermcondCond node) { makeIndent(); System.out.printf("termcondCond(\n"); indent++; }
    @Override
    public void outATermcondCond(ATermcondCond node) { indent--; makeIndent(); System.out.printf(")\n"); }
    @Override
    public void inANotNotCondNotCond(ANotNotCondNotCond node) { System.out.printf("\"not\","); }
    @Override
    public void inANotTermcondNotCond(ANotTermcondNotCond node) { System.out.printf("\"not\","); }
    @Override
    public void inACondInParsTermcond(ACondInParsTermcond node) { makeIndent(); System.out.printf("condInParsTermcond(\n"); indent++; }
    @Override
    public void outACondInParsTermcond(ACondInParsTermcond node) { indent--; makeIndent(); System.out.printf(")\n"); }
    @Override
    public void inAExprNumopExprTermcond(AExprNumopExprTermcond node) { makeIndent(); System.out.printf("exprNumopExprTermcond(\n"); indent++; }
    @Override
    public void outAExprNumopExprTermcond(AExprNumopExprTermcond node) { indent--; makeIndent(); System.out.printf(")\n"); }
    @Override
    public void inACondInPars(ACondInPars node) { makeIndent(); System.out.printf("condInPars(\n"); indent++; }
    @Override
    public void outACondInPars(ACondInPars node) { indent--; makeIndent(); System.out.printf(")\n"); }
    @Override
    public void inANumopExpr(ANumopExpr node) { makeIndent(); System.out.printf("numopExpr(\n"); indent++; }
    @Override
    public void outANumopExpr(ANumopExpr node) { indent--; makeIndent(); System.out.printf(")\n"); }
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
