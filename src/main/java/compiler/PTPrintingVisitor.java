/* *******************OUTDATED******************* */

package compiler;

import compiler.analysis.DepthFirstAdapter;
import compiler.node.*;

public class PTPrintingVisitor extends DepthFirstAdapter {
    // for indentation
    private int indent = 0;
    private void makeIndent() { for(int i = 0; i < indent; i++) System.out.printf("    "); }

    // IN AND OUT A PROGRAM------------------------------------------------------------
    @Override
    public void inAProgram(AProgram node) { makeIndent(); System.out.printf("program :\n"); indent++; }
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
//    @Override
//    public void inAHeaderParams(AHeaderParams node) { makeIndent(); System.out.printf("funct-params :\n"); indent++; }
//    @Override
//    public void outAHeaderParams(AHeaderParams node) { indent--; }

    // IN A FUNCTION PARAMETERS DEFINITION AND ASSISTANT-STATEMENT------------------------------------------------------------
//    @Override
//    public void inAFparDef(AFparDef node) { makeIndent(); System.out.printf("\"%s\"\n", node.getId().toString()); }
//    @Override
//    public void inAFparDefNextId(AFparDefNextId node) { makeIndent(); System.out.printf(",\"%s\"\n", node.getId().toString()); }

    // IN A DATA TYPE------------------------------------------------------------
//    @Override
//    public void inAIntDataType(AIntDataType node) { makeIndent(); System.out.printf("\"int\"");  }
//    @Override
//    public void outAIntDataType(AIntDataType node) { System.out.printf("\n"); }
//    @Override
//    public void inACharDataType(ACharDataType node) { makeIndent(); System.out.printf("\"char\""); }
//    @Override
//    public void outACharDataType(ACharDataType node) { System.out.printf("\n"); }

    // IN AND OUT A TYPE AND ASSISTANT-STATEMENT------------------------------------------------------------
    @Override
    public void inAType(AType node) { makeIndent(); System.out.printf("type :\n"); indent++; }
    @Override
    public void outAType(AType node) { indent--; }
//    @Override
//    public void inADimension(ADimension node) { makeIndent(); System.out.printf("[%s]\n", node.getIntConst().toString()); }

    // IN AND OUT A RETURN TYPE AND ASSISTANT-STATEMENTS------------------------------------------------------------
//    @Override
//    public void inADataTypeRetType(ADataTypeRetType node) { makeIndent(); System.out.printf("retType :\n"); indent++; }
//    @Override
//    public void outADataTypeRetType(ADataTypeRetType node) {  indent--; }
//    @Override
//    public void inANothingRetType(ANothingRetType node) { makeIndent(); System.out.printf("retType :\"nothing\"\n"); }

    // IN AND OUT A FUNCTION PARAMETER TYPE AND ASSISTANT-STATEMENT------------------------------------------------------------
    @Override
    public void inAFparType(AFparType node) { makeIndent(); System.out.printf("funcParType :\n"); indent++; }
    @Override
    public void outAFparType(AFparType node) { indent--; }
//    @Override
//    public void inALRBrackets(ALRBrackets node) { makeIndent(); System.out.printf("[]\n"); }
//    @Override
//    public void inANextDimension(ANextDimension node) { makeIndent(); System.out.printf("[%s]\n", node.getIntConst().toString()); }

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
//    @Override
//    public void inAVarDefNextId(AVarDefNextId node) { makeIndent(); System.out.printf(",\"%s\"\n", node.getId().toString()); }

    // IN AND OUT A FUNCTION DECLARATION------------------------------------------------------------
    @Override
    public void inAFuncDecl(AFuncDecl node) { makeIndent(); System.out.printf("funcDecl :\n"); indent++; }
    @Override
    public void outAFuncDecl(AFuncDecl node) { indent--; }

    // IN A STATEMENT AND ASSISTANT-STATEMENTS------------------------------------------------------------
//    @Override
//    public void inAMatchedStmt(AMatchedStmt node) { makeIndent(); System.out.printf("matchedStmt :\n"); indent++; }
//    @Override
//    public void outAMatchedStmt(AMatchedStmt node) { indent--; }
//    @Override
//    public void inAUnmatchedStmt(AUnmatchedStmt node) { makeIndent(); System.out.printf("unmatchedStmt :\n"); indent++; }
//    @Override
//    public void outAUnmatchedStmt(AUnmatchedStmt node) { indent--; }
//    @Override
//    public void inAIfMatchedElseMatchedMatched(AIfMatchedElseMatchedMatched node) { makeIndent(); System.out.printf("ifMatchedElseMatched :\n"); indent++; }
//    @Override
//    public void outAIfMatchedElseMatchedMatched(AIfMatchedElseMatchedMatched node) { indent--; }
//    @Override
//    public void inAWhileMatchedMatched(AWhileMatchedMatched node) { makeIndent(); System.out.printf("whileMatched :\n"); indent++; }
//    @Override
//    public void outAWhileMatchedMatched(AWhileMatchedMatched node) { indent--; }
//    @Override
//    public void inAOtherMatched(AOtherMatched node) { makeIndent(); System.out.printf("otherMatched :\n"); indent++; }
//    @Override
//    public void outAOtherMatched(AOtherMatched node) { indent--; }
//    @Override
//    public void inAIfUnmatched(AIfUnmatched node) { makeIndent(); System.out.printf("ifUnmatched :\n"); indent++; }
//    @Override
//    public void outAIfUnmatched(AIfUnmatched node) { indent--; }
//    @Override
//    public void inAWhileUnmatchedUnmatched(AWhileUnmatchedUnmatched node) { makeIndent(); System.out.printf("whileUnmatched :\n"); indent++; }
//    @Override
//    public void outAWhileUnmatchedUnmatched(AWhileUnmatchedUnmatched node) { indent--; }
//    @Override
//    public void inAIfMatchedElseUnmatchedUnmatched(AIfMatchedElseUnmatchedUnmatched node) { makeIndent(); System.out.printf("ifMatchedElseUnmatched :\n"); indent++; }
//    @Override
//    public void outAIfMatchedElseUnmatchedUnmatched(AIfMatchedElseUnmatchedUnmatched node) { indent--; }
//    @Override
//    public void inASemicolonOtherStmt(ASemicolonOtherStmt node) { makeIndent(); System.out.printf("\";\"\n"); }
//    @Override
//    public void inAAssignmentOtherStmt(AAssignmentOtherStmt node) { makeIndent(); System.out.printf("\"%s\"<-\n", node.getLValue().toString()); }
//    @Override
//    public void inABlockOtherStmt(ABlockOtherStmt node) { makeIndent(); System.out.printf("blockOtherStmt :\n"); indent++; }
//    @Override
//    public void outABlockOtherStmt(ABlockOtherStmt node) { indent--; }
//    @Override
//    public void inAFunctionOtherStmt(AFunctionOtherStmt node) { makeIndent(); System.out.printf("funcOtherStmt :\n"); indent++; }
//    @Override
//    public void outAFunctionOtherStmt(AFunctionOtherStmt node) { indent--; }
//    @Override
//    public void inAReturnExprOtherStmt(AReturnExprOtherStmt node) { makeIndent(); System.out.printf("retExprOtherStmt :\n"); indent++; }
//    @Override
//    public void outAReturnExprOtherStmt(AReturnExprOtherStmt node) { indent--; }

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
//    @Override
//    public void inAFuncArgs(AFuncArgs node) { makeIndent(); System.out.printf("func-args :\n"); indent++;}
//    @Override
//    public void outAFuncArgs(AFuncArgs node) { indent--; }
//    @Override
//    public void inAFuncCallNextArg(AFuncCallNextArg node) { makeIndent(); System.out.printf("\",\"\n"); }

    // IN A L-VALUE AND ASSISTANT-STATEMENTS------------------------------------------------------------
    @Override
    public void inAIdLValue(AIdLValue node) { makeIndent(); System.out.printf("\"%s\"\n", node.getId().toString()); }
    @Override
    public void inAStringLValue(AStringLValue node) { makeIndent(); System.out.printf("\"%s\"\n", node.getStringLiteral().toString()); }
    @Override
    public void inAExpressionLValue(AExpressionLValue node) { makeIndent(); System.out.printf("exprLValue :\n"); }

    // IN AN EXPRESSION AND ASSISTANT STATEMENTS------------------------------------------------------------
//    @Override
//    public void inAExprPlusTermExpr(AExprPlusTermExpr node) { makeIndent(); System.out.printf("exprPlusTermExpr :\n"); indent++; }
//    @Override
//    public void outAExprPlusTermExpr(AExprPlusTermExpr node) { indent--; }
//    @Override
//    public void inAExprMinusTermExpr(AExprMinusTermExpr node) { makeIndent(); System.out.printf("exprMinusTermExpr :\n"); indent++; }
//    @Override
//    public void outAExprMinusTermExpr(AExprMinusTermExpr node) { indent--; }
//    @Override
//    public void inATermExpr(ATermExpr node) { makeIndent(); System.out.printf("termExpr :\n"); indent++; }
//    @Override
//    public void outATermExpr(ATermExpr node) { indent--; }
//    @Override
//    public void inAPlusSignSign(APlusSignSign node) { makeIndent(); System.out.printf("\"+\",\n"); }
//    @Override
//    public void inAMinusSignSign(AMinusSignSign node) { makeIndent(); System.out.printf("\"-\",\n"); }
//    @Override
//    public void inATermMultFactorTerm(ATermMultFactorTerm node) { makeIndent(); System.out.printf("termMultFactorTerm :\n"); indent++; }
//    @Override
//    public void outATermMultFactorTerm(ATermMultFactorTerm node) { indent--; }
//    @Override
//    public void inATermDivFactorTerm(ATermDivFactorTerm node) { makeIndent(); System.out.printf("termDivFactorTerm :\n"); indent++; }
//    @Override
//    public void outATermDivFactorTerm(ATermDivFactorTerm node) { indent--; }
//    @Override
//    public void inATermDivisionFactorTerm(ATermDivisionFactorTerm node) { makeIndent(); System.out.printf("termDivisionFactorTerm :\n"); indent++; }
//    @Override
//    public void outATermDivisionFactorTerm(ATermDivisionFactorTerm node) { indent--; }
//    @Override
//    public void inATermModFactorTerm(ATermModFactorTerm node) { makeIndent(); System.out.printf("termModFactorTerm :\n"); indent++; }
//    @Override
//    public void outATermModFactorTerm(ATermModFactorTerm node) { indent--; }
//    @Override
//    public void inAFactorTerm(AFactorTerm node) { makeIndent(); System.out.printf("factorTerm :\n");  indent++; }
//    @Override
//    public void outAFactorTerm(AFactorTerm node) { indent--; }
//    @Override
//    public void inAIntConstFactor(AIntConstFactor node) { makeIndent(); System.out.printf("\"%s\"\n", node.getIntConst().toString()); }
//    @Override
//    public void inACharConstFactor(ACharConstFactor node) { makeIndent(); System.out.printf("\"%s\"\n", node.getCharConst().toString()); }
//    @Override
//    public void inALValueFactor(ALValueFactor node) { makeIndent(); System.out.printf("lValueFactor :\n"); indent++; }
//    @Override
//    public void outALValueFactor(ALValueFactor node) { indent--; }
//    @Override
//    public void inAFuncCallFactor(AFuncCallFactor node) { makeIndent(); System.out.printf("funcCallFactor :\n"); indent++; }
//    @Override
//    public void outAFuncCallFactor(AFuncCallFactor node) { indent--; }
//    @Override
//    public void inAExprInParsFactor(AExprInParsFactor node) { makeIndent(); System.out.printf("exprInParsFactor :\n"); indent++; }
//    @Override
//    public void outAExprInParsFactor(AExprInParsFactor node) { indent--; }
//    @Override
//    public void inAExprInPars(AExprInPars node) { makeIndent(); System.out.printf("\"(\"\n"); }
//    @Override
//    public void outAExprInPars(AExprInPars node) { makeIndent(); System.out.printf("\")\"\n"); }
//    @Override
//    public void inASignedExprFactor(ASignedExprFactor node) { makeIndent(); System.out.printf("signedExprFactor :\n"); indent++; }
//    @Override
//    public void outASignedExprFactor(ASignedExprFactor node) { indent--; }
//    @Override
//    public void inASignSignedExprSignedExpr(ASignSignedExprSignedExpr node) { System.out.printf(""); }
//    @Override
//    public void inASignFactorSignedExpr(ASignFactorSignedExpr node) { System.out.printf(""); }

    // IN A CONDITION AND ASSISTANT STATEMENTS------------------------------------------------------------
//    @Override
//    public void inANotCond(ANotCond node) { makeIndent(); System.out.printf("notCond :\n"); indent++; }
//    @Override
//    public void outANotCond(ANotCond node) { indent--; }
//    @Override
//    public void inACondAndTermcondCond(ACondAndTermcondCond node) { makeIndent(); System.out.printf("termAndTermCond :\n"); indent++; }
//    @Override
//    public void outACondAndTermcondCond(ACondAndTermcondCond node) { indent--; }
//    @Override
//    public void inACondOrTermcondCond(ACondOrTermcondCond node) { makeIndent(); System.out.printf("condOrTermcondCond :\n"); indent++; }
//    @Override
//    public void outACondOrTermcondCond(ACondOrTermcondCond node) { indent--; }
//    @Override
//    public void inATermcondCond(ATermcondCond node) { makeIndent(); System.out.printf("termcondCond :\n"); indent++; }
//    @Override
//    public void outATermcondCond(ATermcondCond node) { indent--; }
//    @Override
//    public void inANotNotCondNotCond(ANotNotCondNotCond node) { makeIndent(); System.out.printf("\"not\",\n"); }
//    @Override
//    public void inANotTermcondNotCond(ANotTermcondNotCond node) { makeIndent(); System.out.printf("\"not\",\n"); }
//    @Override
//    public void inACondInParsTermcond(ACondInParsTermcond node) { makeIndent(); System.out.printf("condInParsTermcond :\n"); indent++; }
//    @Override
//    public void outACondInParsTermcond(ACondInParsTermcond node) { indent--; }
//    @Override
//    public void inAExprNumopExprTermcond(AExprNumopExprTermcond node) { makeIndent(); System.out.printf("exprNumopExprTermcond :\n"); indent++; }
//    @Override
//    public void outAExprNumopExprTermcond(AExprNumopExprTermcond node) { indent--; }
//    @Override
//    public void inACondInPars(ACondInPars node) { makeIndent(); System.out.printf("condInPars :\n"); indent++; }
//    @Override
//    public void outACondInPars(ACondInPars node) { indent--; }
//    @Override
//    public void inANumopExpr(ANumopExpr node) { makeIndent(); System.out.printf("numopExpr :\n"); indent++; }
//    @Override
//    public void outANumopExpr(ANumopExpr node) { indent--; }
//    @Override
//    public void inAEqualNumop(AEqualNumop node) { makeIndent(); System.out.printf("\"=\",\n"); }
//    @Override
//    public void inAHashtagNumop(AHashtagNumop node) { makeIndent(); System.out.printf("\"#\",\n"); }
//    @Override
//    public void inAUnequalNumop(AUnequalNumop node) { makeIndent(); System.out.printf("\"<>\",\n"); }
//    @Override
//    public void inALesserNumop(ALesserNumop node) { makeIndent(); System.out.printf("\"<\",\n"); }
//    @Override
//    public void inAGreaterNumop(AGreaterNumop node) { makeIndent(); System.out.printf("\">\",\n"); }
//    @Override
//    public void inALesseqNumop(ALesseqNumop node) { makeIndent(); System.out.printf("\"<=\",\n"); }
//    @Override
//    public void inAGreateqNumop(AGreateqNumop node) { makeIndent(); System.out.printf("\">=\",\n"); }
    //------------------------------------------------------------
}
