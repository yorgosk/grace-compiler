/* This file was generated by SableCC (http://www.sablecc.org/). */

package compiler.analysis;

import java.util.*;
import compiler.node.*;

public class AnalysisAdapter implements Analysis
{
    private Hashtable<Node,Object> in;
    private Hashtable<Node,Object> out;

    public Object getIn(Node node)
    {
        if(this.in == null)
        {
            return null;
        }

        return this.in.get(node);
    }

    public void setIn(Node node, Object o)
    {
        if(this.in == null)
        {
            this.in = new Hashtable<Node,Object>(1);
        }

        if(o != null)
        {
            this.in.put(node, o);
        }
        else
        {
            this.in.remove(node);
        }
    }

    public Object getOut(Node node)
    {
        if(this.out == null)
        {
            return null;
        }

        return this.out.get(node);
    }

    public void setOut(Node node, Object o)
    {
        if(this.out == null)
        {
            this.out = new Hashtable<Node,Object>(1);
        }

        if(o != null)
        {
            this.out.put(node, o);
        }
        else
        {
            this.out.remove(node);
        }
    }

    public void caseStart(Start node)
    {
        defaultCase(node);
    }

    public void caseAProgram(AProgram node)
    {
        defaultCase(node);
    }

    public void caseAFuncDef(AFuncDef node)
    {
        defaultCase(node);
    }

    public void caseAHeader(AHeader node)
    {
        defaultCase(node);
    }

    public void caseAFparDef(AFparDef node)
    {
        defaultCase(node);
    }

    public void caseAIntDataType(AIntDataType node)
    {
        defaultCase(node);
    }

    public void caseACharDataType(ACharDataType node)
    {
        defaultCase(node);
    }

    public void caseAType(AType node)
    {
        defaultCase(node);
    }

    public void caseADataTypeRetType(ADataTypeRetType node)
    {
        defaultCase(node);
    }

    public void caseANothingRetType(ANothingRetType node)
    {
        defaultCase(node);
    }

    public void caseAFparType(AFparType node)
    {
        defaultCase(node);
    }

    public void caseALRBrackets(ALRBrackets node)
    {
        defaultCase(node);
    }

    public void caseAFuncDefLocalDef(AFuncDefLocalDef node)
    {
        defaultCase(node);
    }

    public void caseAFuncDeclLocalDef(AFuncDeclLocalDef node)
    {
        defaultCase(node);
    }

    public void caseAVarDefLocalDef(AVarDefLocalDef node)
    {
        defaultCase(node);
    }

    public void caseAVarDef(AVarDef node)
    {
        defaultCase(node);
    }

    public void caseAFuncDecl(AFuncDecl node)
    {
        defaultCase(node);
    }

    public void caseASemicolonStmt(ASemicolonStmt node)
    {
        defaultCase(node);
    }

    public void caseAAssignmentStmt(AAssignmentStmt node)
    {
        defaultCase(node);
    }

    public void caseABlockStmt(ABlockStmt node)
    {
        defaultCase(node);
    }

    public void caseAFunctionStmt(AFunctionStmt node)
    {
        defaultCase(node);
    }

    public void caseAIfStmt(AIfStmt node)
    {
        defaultCase(node);
    }

    public void caseAWhileStmt(AWhileStmt node)
    {
        defaultCase(node);
    }

    public void caseAReturnStmt(AReturnStmt node)
    {
        defaultCase(node);
    }

    public void caseABlock(ABlock node)
    {
        defaultCase(node);
    }

    public void caseAFuncCall(AFuncCall node)
    {
        defaultCase(node);
    }

    public void caseAIdLValue(AIdLValue node)
    {
        defaultCase(node);
    }

    public void caseAStringLValue(AStringLValue node)
    {
        defaultCase(node);
    }

    public void caseAExpressionLValue(AExpressionLValue node)
    {
        defaultCase(node);
    }

    public void caseAIntConstExpr(AIntConstExpr node)
    {
        defaultCase(node);
    }

    public void caseACharConstExpr(ACharConstExpr node)
    {
        defaultCase(node);
    }

    public void caseALValueExpr(ALValueExpr node)
    {
        defaultCase(node);
    }

    public void caseAFuncCallExpr(AFuncCallExpr node)
    {
        defaultCase(node);
    }

    public void caseAExprExpr(AExprExpr node)
    {
        defaultCase(node);
    }

    public void caseAPlusExpr(APlusExpr node)
    {
        defaultCase(node);
    }

    public void caseAMinusExpr(AMinusExpr node)
    {
        defaultCase(node);
    }

    public void caseAMultExpr(AMultExpr node)
    {
        defaultCase(node);
    }

    public void caseADivExpr(ADivExpr node)
    {
        defaultCase(node);
    }

    public void caseADivisionExpr(ADivisionExpr node)
    {
        defaultCase(node);
    }

    public void caseAModExpr(AModExpr node)
    {
        defaultCase(node);
    }

    public void caseASignedExpr(ASignedExpr node)
    {
        defaultCase(node);
    }

    public void caseAPlusSignSign(APlusSignSign node)
    {
        defaultCase(node);
    }

    public void caseAMinusSignSign(AMinusSignSign node)
    {
        defaultCase(node);
    }

    public void caseACondCond(ACondCond node)
    {
        defaultCase(node);
    }

    public void caseANotCond(ANotCond node)
    {
        defaultCase(node);
    }

    public void caseAAndCond(AAndCond node)
    {
        defaultCase(node);
    }

    public void caseAOrCond(AOrCond node)
    {
        defaultCase(node);
    }

    public void caseAEqualCond(AEqualCond node)
    {
        defaultCase(node);
    }

    public void caseAHashtagCond(AHashtagCond node)
    {
        defaultCase(node);
    }

    public void caseAUnequalCond(AUnequalCond node)
    {
        defaultCase(node);
    }

    public void caseALesserCond(ALesserCond node)
    {
        defaultCase(node);
    }

    public void caseAGreaterCond(AGreaterCond node)
    {
        defaultCase(node);
    }

    public void caseALesseqCond(ALesseqCond node)
    {
        defaultCase(node);
    }

    public void caseAGreateqCond(AGreateqCond node)
    {
        defaultCase(node);
    }

    public void caseTAnd(TAnd node)
    {
        defaultCase(node);
    }

    public void caseTChar(TChar node)
    {
        defaultCase(node);
    }

    public void caseTDiv(TDiv node)
    {
        defaultCase(node);
    }

    public void caseTDo(TDo node)
    {
        defaultCase(node);
    }

    public void caseTElse(TElse node)
    {
        defaultCase(node);
    }

    public void caseTFun(TFun node)
    {
        defaultCase(node);
    }

    public void caseTIf(TIf node)
    {
        defaultCase(node);
    }

    public void caseTInt(TInt node)
    {
        defaultCase(node);
    }

    public void caseTMod(TMod node)
    {
        defaultCase(node);
    }

    public void caseTNot(TNot node)
    {
        defaultCase(node);
    }

    public void caseTNothing(TNothing node)
    {
        defaultCase(node);
    }

    public void caseTOr(TOr node)
    {
        defaultCase(node);
    }

    public void caseTRef(TRef node)
    {
        defaultCase(node);
    }

    public void caseTReturn(TReturn node)
    {
        defaultCase(node);
    }

    public void caseTThen(TThen node)
    {
        defaultCase(node);
    }

    public void caseTVar(TVar node)
    {
        defaultCase(node);
    }

    public void caseTWhile(TWhile node)
    {
        defaultCase(node);
    }

    public void caseTId(TId node)
    {
        defaultCase(node);
    }

    public void caseTIntConst(TIntConst node)
    {
        defaultCase(node);
    }

    public void caseTCharConst(TCharConst node)
    {
        defaultCase(node);
    }

    public void caseTSCharSequence(TSCharSequence node)
    {
        defaultCase(node);
    }

    public void caseTStringLiteral(TStringLiteral node)
    {
        defaultCase(node);
    }

    public void caseTErroneousNumber(TErroneousNumber node)
    {
        defaultCase(node);
    }

    public void caseTPlus(TPlus node)
    {
        defaultCase(node);
    }

    public void caseTMinus(TMinus node)
    {
        defaultCase(node);
    }

    public void caseTMult(TMult node)
    {
        defaultCase(node);
    }

    public void caseTDivision(TDivision node)
    {
        defaultCase(node);
    }

    public void caseTHashtag(THashtag node)
    {
        defaultCase(node);
    }

    public void caseTEqual(TEqual node)
    {
        defaultCase(node);
    }

    public void caseTLesser(TLesser node)
    {
        defaultCase(node);
    }

    public void caseTGreater(TGreater node)
    {
        defaultCase(node);
    }

    public void caseTLesseq(TLesseq node)
    {
        defaultCase(node);
    }

    public void caseTGreateq(TGreateq node)
    {
        defaultCase(node);
    }

    public void caseTUnequal(TUnequal node)
    {
        defaultCase(node);
    }

    public void caseTLPar(TLPar node)
    {
        defaultCase(node);
    }

    public void caseTRPar(TRPar node)
    {
        defaultCase(node);
    }

    public void caseTLBracket(TLBracket node)
    {
        defaultCase(node);
    }

    public void caseTRBracket(TRBracket node)
    {
        defaultCase(node);
    }

    public void caseTLBrace(TLBrace node)
    {
        defaultCase(node);
    }

    public void caseTRBrace(TRBrace node)
    {
        defaultCase(node);
    }

    public void caseTComma(TComma node)
    {
        defaultCase(node);
    }

    public void caseTSemicolon(TSemicolon node)
    {
        defaultCase(node);
    }

    public void caseTColon(TColon node)
    {
        defaultCase(node);
    }

    public void caseTAssignment(TAssignment node)
    {
        defaultCase(node);
    }

    public void caseTIdentifier(TIdentifier node)
    {
        defaultCase(node);
    }

    public void caseTBlank(TBlank node)
    {
        defaultCase(node);
    }

    public void caseTLineComment(TLineComment node)
    {
        defaultCase(node);
    }

    public void caseTMultilineComment(TMultilineComment node)
    {
        defaultCase(node);
    }

    public void caseEOF(EOF node)
    {
        defaultCase(node);
    }

    public void defaultCase(@SuppressWarnings("unused") Node node)
    {
        // do nothing
    }
}
