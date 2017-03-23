/* This file was generated by SableCC (http://www.sablecc.org/). */

package compiler.analysis;

import compiler.node.*;

public interface Analysis extends Switch
{
    Object getIn(Node node);
    void setIn(Node node, Object o);
    Object getOut(Node node);
    void setOut(Node node, Object o);

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
    void caseTPlus(TPlus node);
    void caseTMinus(TMinus node);
    void caseTDivision(TDivision node);
    void caseTMult(TMult node);
    void caseTHashtag(THashtag node);
    void caseTEqual(TEqual node);
    void caseTUnequal(TUnequal node);
    void caseTLesser(TLesser node);
    void caseTGreater(TGreater node);
    void caseTLesseq(TLesseq node);
    void caseTGreateq(TGreateq node);
    void caseTComma(TComma node);
    void caseTColon(TColon node);
    void caseTSemicolon(TSemicolon node);
    void caseTAssignment(TAssignment node);
    void caseTLPar(TLPar node);
    void caseTRPar(TRPar node);
    void caseTLBracket(TLBracket node);
    void caseTRBracket(TRBracket node);
    void caseTLBrace(TLBrace node);
    void caseTRBrace(TRBrace node);
    void caseTIdentifier(TIdentifier node);
    void caseTBlank(TBlank node);
    void caseTComment(TComment node);
    void caseEOF(EOF node);
}
