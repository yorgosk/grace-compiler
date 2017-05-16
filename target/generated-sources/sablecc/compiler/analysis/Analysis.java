/* This file was generated by SableCC (http://www.sablecc.org/). */

package compiler.analysis;

import compiler.node.*;

public interface Analysis extends Switch
{
    Object getIn(Node node);
    void setIn(Node node, Object o);
    Object getOut(Node node);
    void setOut(Node node, Object o);

    void caseStart(Start node);
    void caseAProgram(AProgram node);
    void caseAFuncDef(AFuncDef node);
    void caseAHeader(AHeader node);
    void caseAFparDef(AFparDef node);
    void caseAType(AType node);
    void caseAFparType(AFparType node);
    void caseAFuncDefLocalDef(AFuncDefLocalDef node);
    void caseAFuncDeclLocalDef(AFuncDeclLocalDef node);
    void caseAVarDefLocalDef(AVarDefLocalDef node);
    void caseAVarDef(AVarDef node);
    void caseAFuncDecl(AFuncDecl node);
    void caseASemicolonStmt(ASemicolonStmt node);
    void caseAAssignmentStmt(AAssignmentStmt node);
    void caseABlockStmt(ABlockStmt node);
    void caseAFunctionStmt(AFunctionStmt node);
    void caseAIfStmt(AIfStmt node);
    void caseAWhileStmt(AWhileStmt node);
    void caseAReturnStmt(AReturnStmt node);
    void caseABlock(ABlock node);
    void caseAFuncCall(AFuncCall node);
    void caseAIdLValue(AIdLValue node);
    void caseAStringLValue(AStringLValue node);
    void caseAExpressionLValue(AExpressionLValue node);
    void caseAIntConstExpr(AIntConstExpr node);
    void caseACharConstExpr(ACharConstExpr node);
    void caseALValueExpr(ALValueExpr node);
    void caseAFuncCallExpr(AFuncCallExpr node);
    void caseAExprExpr(AExprExpr node);
    void caseAPlusExpr(APlusExpr node);
    void caseAMinusExpr(AMinusExpr node);
    void caseAMultExpr(AMultExpr node);
    void caseADivExpr(ADivExpr node);
    void caseADivisionExpr(ADivisionExpr node);
    void caseAModExpr(AModExpr node);
    void caseACondCond(ACondCond node);
    void caseAAndCond(AAndCond node);
    void caseAOrCond(AOrCond node);
    void caseANumopCond(ANumopCond node);

    void caseTAnd(TAnd node);
    void caseTChar(TChar node);
    void caseTDiv(TDiv node);
    void caseTDo(TDo node);
    void caseTElse(TElse node);
    void caseTFun(TFun node);
    void caseTIf(TIf node);
    void caseTInt(TInt node);
    void caseTMod(TMod node);
    void caseTNot(TNot node);
    void caseTNothing(TNothing node);
    void caseTOr(TOr node);
    void caseTRef(TRef node);
    void caseTReturn(TReturn node);
    void caseTThen(TThen node);
    void caseTVar(TVar node);
    void caseTWhile(TWhile node);
    void caseTId(TId node);
    void caseTIntConst(TIntConst node);
    void caseTCharConst(TCharConst node);
    void caseTSCharSequence(TSCharSequence node);
    void caseTStringLiteral(TStringLiteral node);
    void caseTErroneousNumber(TErroneousNumber node);
    void caseTPlus(TPlus node);
    void caseTMinus(TMinus node);
    void caseTMult(TMult node);
    void caseTDivision(TDivision node);
    void caseTHashtag(THashtag node);
    void caseTEqual(TEqual node);
    void caseTLesser(TLesser node);
    void caseTGreater(TGreater node);
    void caseTLesseq(TLesseq node);
    void caseTGreateq(TGreateq node);
    void caseTUnequal(TUnequal node);
    void caseTLPar(TLPar node);
    void caseTRPar(TRPar node);
    void caseTLBracket(TLBracket node);
    void caseTRBracket(TRBracket node);
    void caseTLBrace(TLBrace node);
    void caseTRBrace(TRBrace node);
    void caseTComma(TComma node);
    void caseTSemicolon(TSemicolon node);
    void caseTColon(TColon node);
    void caseTAssignment(TAssignment node);
    void caseTIdentifier(TIdentifier node);
    void caseTBlank(TBlank node);
    void caseTLineComment(TLineComment node);
    void caseTMultilineComment(TMultilineComment node);
    void caseEOF(EOF node);
}
