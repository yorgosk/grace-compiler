/* This file was generated by SableCC (http://www.sablecc.org/). */

package compiler.analysis;

import java.util.*;
import compiler.node.*;

public class ReversedDepthFirstAdapter extends AnalysisAdapter
{
    public void inStart(Start node)
    {
        defaultIn(node);
    }

    public void outStart(Start node)
    {
        defaultOut(node);
    }

    public void defaultIn(@SuppressWarnings("unused") Node node)
    {
        // Do nothing
    }

    public void defaultOut(@SuppressWarnings("unused") Node node)
    {
        // Do nothing
    }

    @Override
    public void caseStart(Start node)
    {
        inStart(node);
        node.getEOF().apply(this);
        node.getPProgram().apply(this);
        outStart(node);
    }

    public void inAProgram(AProgram node)
    {
        defaultIn(node);
    }

    public void outAProgram(AProgram node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAProgram(AProgram node)
    {
        inAProgram(node);
        if(node.getFuncDef() != null)
        {
            node.getFuncDef().apply(this);
        }
        outAProgram(node);
    }

    public void inAFuncDef(AFuncDef node)
    {
        defaultIn(node);
    }

    public void outAFuncDef(AFuncDef node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAFuncDef(AFuncDef node)
    {
        inAFuncDef(node);
        if(node.getBlock() != null)
        {
            node.getBlock().apply(this);
        }
        {
            List<PLocalDef> copy = new ArrayList<PLocalDef>(node.getLocalDef());
            Collections.reverse(copy);
            for(PLocalDef e : copy)
            {
                e.apply(this);
            }
        }
        if(node.getHeader() != null)
        {
            node.getHeader().apply(this);
        }
        outAFuncDef(node);
    }

    public void inAHeader(AHeader node)
    {
        defaultIn(node);
    }

    public void outAHeader(AHeader node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAHeader(AHeader node)
    {
        inAHeader(node);
        if(node.getRetType() != null)
        {
            node.getRetType().apply(this);
        }
        {
            List<PFparDef> copy = new ArrayList<PFparDef>(node.getFparDef());
            Collections.reverse(copy);
            for(PFparDef e : copy)
            {
                e.apply(this);
            }
        }
        if(node.getId() != null)
        {
            node.getId().apply(this);
        }
        outAHeader(node);
    }

    public void inAFparDef(AFparDef node)
    {
        defaultIn(node);
    }

    public void outAFparDef(AFparDef node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAFparDef(AFparDef node)
    {
        inAFparDef(node);
        if(node.getFparType() != null)
        {
            node.getFparType().apply(this);
        }
        {
            List<TId> copy = new ArrayList<TId>(node.getNext());
            Collections.reverse(copy);
            for(TId e : copy)
            {
                e.apply(this);
            }
        }
        if(node.getId() != null)
        {
            node.getId().apply(this);
        }
        if(node.getRef() != null)
        {
            node.getRef().apply(this);
        }
        outAFparDef(node);
    }

    public void inAIntDataType(AIntDataType node)
    {
        defaultIn(node);
    }

    public void outAIntDataType(AIntDataType node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAIntDataType(AIntDataType node)
    {
        inAIntDataType(node);
        if(node.getInt() != null)
        {
            node.getInt().apply(this);
        }
        outAIntDataType(node);
    }

    public void inACharDataType(ACharDataType node)
    {
        defaultIn(node);
    }

    public void outACharDataType(ACharDataType node)
    {
        defaultOut(node);
    }

    @Override
    public void caseACharDataType(ACharDataType node)
    {
        inACharDataType(node);
        if(node.getChar() != null)
        {
            node.getChar().apply(this);
        }
        outACharDataType(node);
    }

    public void inAType(AType node)
    {
        defaultIn(node);
    }

    public void outAType(AType node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAType(AType node)
    {
        inAType(node);
        {
            List<TIntConst> copy = new ArrayList<TIntConst>(node.getIntConst());
            Collections.reverse(copy);
            for(TIntConst e : copy)
            {
                e.apply(this);
            }
        }
        if(node.getDataType() != null)
        {
            node.getDataType().apply(this);
        }
        outAType(node);
    }

    public void inADataTypeRetType(ADataTypeRetType node)
    {
        defaultIn(node);
    }

    public void outADataTypeRetType(ADataTypeRetType node)
    {
        defaultOut(node);
    }

    @Override
    public void caseADataTypeRetType(ADataTypeRetType node)
    {
        inADataTypeRetType(node);
        if(node.getDataType() != null)
        {
            node.getDataType().apply(this);
        }
        outADataTypeRetType(node);
    }

    public void inANothingRetType(ANothingRetType node)
    {
        defaultIn(node);
    }

    public void outANothingRetType(ANothingRetType node)
    {
        defaultOut(node);
    }

    @Override
    public void caseANothingRetType(ANothingRetType node)
    {
        inANothingRetType(node);
        if(node.getNothing() != null)
        {
            node.getNothing().apply(this);
        }
        outANothingRetType(node);
    }

    public void inAFparType(AFparType node)
    {
        defaultIn(node);
    }

    public void outAFparType(AFparType node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAFparType(AFparType node)
    {
        inAFparType(node);
        {
            List<TIntConst> copy = new ArrayList<TIntConst>(node.getIntConst());
            Collections.reverse(copy);
            for(TIntConst e : copy)
            {
                e.apply(this);
            }
        }
        if(node.getDataType() != null)
        {
            node.getDataType().apply(this);
        }
        outAFparType(node);
    }

    public void inAFuncDefLocalDef(AFuncDefLocalDef node)
    {
        defaultIn(node);
    }

    public void outAFuncDefLocalDef(AFuncDefLocalDef node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAFuncDefLocalDef(AFuncDefLocalDef node)
    {
        inAFuncDefLocalDef(node);
        if(node.getFuncDef() != null)
        {
            node.getFuncDef().apply(this);
        }
        outAFuncDefLocalDef(node);
    }

    public void inAFuncDeclLocalDef(AFuncDeclLocalDef node)
    {
        defaultIn(node);
    }

    public void outAFuncDeclLocalDef(AFuncDeclLocalDef node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAFuncDeclLocalDef(AFuncDeclLocalDef node)
    {
        inAFuncDeclLocalDef(node);
        if(node.getFuncDecl() != null)
        {
            node.getFuncDecl().apply(this);
        }
        outAFuncDeclLocalDef(node);
    }

    public void inAVarDefLocalDef(AVarDefLocalDef node)
    {
        defaultIn(node);
    }

    public void outAVarDefLocalDef(AVarDefLocalDef node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAVarDefLocalDef(AVarDefLocalDef node)
    {
        inAVarDefLocalDef(node);
        if(node.getVarDef() != null)
        {
            node.getVarDef().apply(this);
        }
        outAVarDefLocalDef(node);
    }

    public void inAVarDef(AVarDef node)
    {
        defaultIn(node);
    }

    public void outAVarDef(AVarDef node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAVarDef(AVarDef node)
    {
        inAVarDef(node);
        if(node.getType() != null)
        {
            node.getType().apply(this);
        }
        {
            List<TId> copy = new ArrayList<TId>(node.getNext());
            Collections.reverse(copy);
            for(TId e : copy)
            {
                e.apply(this);
            }
        }
        if(node.getId() != null)
        {
            node.getId().apply(this);
        }
        outAVarDef(node);
    }

    public void inAFuncDecl(AFuncDecl node)
    {
        defaultIn(node);
    }

    public void outAFuncDecl(AFuncDecl node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAFuncDecl(AFuncDecl node)
    {
        inAFuncDecl(node);
        if(node.getHeader() != null)
        {
            node.getHeader().apply(this);
        }
        outAFuncDecl(node);
    }

    public void inASemicolonStmt(ASemicolonStmt node)
    {
        defaultIn(node);
    }

    public void outASemicolonStmt(ASemicolonStmt node)
    {
        defaultOut(node);
    }

    @Override
    public void caseASemicolonStmt(ASemicolonStmt node)
    {
        inASemicolonStmt(node);
        outASemicolonStmt(node);
    }

    public void inAAssignmentStmt(AAssignmentStmt node)
    {
        defaultIn(node);
    }

    public void outAAssignmentStmt(AAssignmentStmt node)
    {
        defaultOut(node);
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

    public void inABlockStmt(ABlockStmt node)
    {
        defaultIn(node);
    }

    public void outABlockStmt(ABlockStmt node)
    {
        defaultOut(node);
    }

    @Override
    public void caseABlockStmt(ABlockStmt node)
    {
        inABlockStmt(node);
        if(node.getBlock() != null)
        {
            node.getBlock().apply(this);
        }
        outABlockStmt(node);
    }

    public void inAFunctionStmt(AFunctionStmt node)
    {
        defaultIn(node);
    }

    public void outAFunctionStmt(AFunctionStmt node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAFunctionStmt(AFunctionStmt node)
    {
        inAFunctionStmt(node);
        if(node.getFuncCall() != null)
        {
            node.getFuncCall().apply(this);
        }
        outAFunctionStmt(node);
    }

    public void inAIfStmt(AIfStmt node)
    {
        defaultIn(node);
    }

    public void outAIfStmt(AIfStmt node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAIfStmt(AIfStmt node)
    {
        inAIfStmt(node);
        {
            List<PStmt> copy = new ArrayList<PStmt>(node.getElseM());
            Collections.reverse(copy);
            for(PStmt e : copy)
            {
                e.apply(this);
            }
        }
        if(node.getThenM() != null)
        {
            node.getThenM().apply(this);
        }
        if(node.getCond() != null)
        {
            node.getCond().apply(this);
        }
        outAIfStmt(node);
    }

    public void inAWhileStmt(AWhileStmt node)
    {
        defaultIn(node);
    }

    public void outAWhileStmt(AWhileStmt node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAWhileStmt(AWhileStmt node)
    {
        inAWhileStmt(node);
        if(node.getStmt() != null)
        {
            node.getStmt().apply(this);
        }
        if(node.getCond() != null)
        {
            node.getCond().apply(this);
        }
        outAWhileStmt(node);
    }

    public void inAReturnStmt(AReturnStmt node)
    {
        defaultIn(node);
    }

    public void outAReturnStmt(AReturnStmt node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAReturnStmt(AReturnStmt node)
    {
        inAReturnStmt(node);
        {
            List<PExpr> copy = new ArrayList<PExpr>(node.getExpr());
            Collections.reverse(copy);
            for(PExpr e : copy)
            {
                e.apply(this);
            }
        }
        outAReturnStmt(node);
    }

    public void inABlock(ABlock node)
    {
        defaultIn(node);
    }

    public void outABlock(ABlock node)
    {
        defaultOut(node);
    }

    @Override
    public void caseABlock(ABlock node)
    {
        inABlock(node);
        {
            List<PStmt> copy = new ArrayList<PStmt>(node.getStmt());
            Collections.reverse(copy);
            for(PStmt e : copy)
            {
                e.apply(this);
            }
        }
        outABlock(node);
    }

    public void inAFuncCall(AFuncCall node)
    {
        defaultIn(node);
    }

    public void outAFuncCall(AFuncCall node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAFuncCall(AFuncCall node)
    {
        inAFuncCall(node);
        {
            List<PExpr> copy = new ArrayList<PExpr>(node.getExpr());
            Collections.reverse(copy);
            for(PExpr e : copy)
            {
                e.apply(this);
            }
        }
        if(node.getId() != null)
        {
            node.getId().apply(this);
        }
        outAFuncCall(node);
    }

    public void inAIdLValue(AIdLValue node)
    {
        defaultIn(node);
    }

    public void outAIdLValue(AIdLValue node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAIdLValue(AIdLValue node)
    {
        inAIdLValue(node);
        if(node.getId() != null)
        {
            node.getId().apply(this);
        }
        outAIdLValue(node);
    }

    public void inAStringLValue(AStringLValue node)
    {
        defaultIn(node);
    }

    public void outAStringLValue(AStringLValue node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAStringLValue(AStringLValue node)
    {
        inAStringLValue(node);
        if(node.getStringLiteral() != null)
        {
            node.getStringLiteral().apply(this);
        }
        outAStringLValue(node);
    }

    public void inAExpressionLValue(AExpressionLValue node)
    {
        defaultIn(node);
    }

    public void outAExpressionLValue(AExpressionLValue node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAExpressionLValue(AExpressionLValue node)
    {
        inAExpressionLValue(node);
        if(node.getExpr() != null)
        {
            node.getExpr().apply(this);
        }
        if(node.getLValue() != null)
        {
            node.getLValue().apply(this);
        }
        outAExpressionLValue(node);
    }

    public void inAIntConstExpr(AIntConstExpr node)
    {
        defaultIn(node);
    }

    public void outAIntConstExpr(AIntConstExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAIntConstExpr(AIntConstExpr node)
    {
        inAIntConstExpr(node);
        if(node.getIntConst() != null)
        {
            node.getIntConst().apply(this);
        }
        outAIntConstExpr(node);
    }

    public void inACharConstExpr(ACharConstExpr node)
    {
        defaultIn(node);
    }

    public void outACharConstExpr(ACharConstExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseACharConstExpr(ACharConstExpr node)
    {
        inACharConstExpr(node);
        if(node.getCharConst() != null)
        {
            node.getCharConst().apply(this);
        }
        outACharConstExpr(node);
    }

    public void inALValueExpr(ALValueExpr node)
    {
        defaultIn(node);
    }

    public void outALValueExpr(ALValueExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseALValueExpr(ALValueExpr node)
    {
        inALValueExpr(node);
        if(node.getLValue() != null)
        {
            node.getLValue().apply(this);
        }
        outALValueExpr(node);
    }

    public void inAFuncCallExpr(AFuncCallExpr node)
    {
        defaultIn(node);
    }

    public void outAFuncCallExpr(AFuncCallExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAFuncCallExpr(AFuncCallExpr node)
    {
        inAFuncCallExpr(node);
        if(node.getFuncCall() != null)
        {
            node.getFuncCall().apply(this);
        }
        outAFuncCallExpr(node);
    }

    public void inAExprExpr(AExprExpr node)
    {
        defaultIn(node);
    }

    public void outAExprExpr(AExprExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAExprExpr(AExprExpr node)
    {
        inAExprExpr(node);
        if(node.getExpr() != null)
        {
            node.getExpr().apply(this);
        }
        outAExprExpr(node);
    }

    public void inAPlusExpr(APlusExpr node)
    {
        defaultIn(node);
    }

    public void outAPlusExpr(APlusExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAPlusExpr(APlusExpr node)
    {
        inAPlusExpr(node);
        if(node.getRight() != null)
        {
            node.getRight().apply(this);
        }
        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
        }
        outAPlusExpr(node);
    }

    public void inAMinusExpr(AMinusExpr node)
    {
        defaultIn(node);
    }

    public void outAMinusExpr(AMinusExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAMinusExpr(AMinusExpr node)
    {
        inAMinusExpr(node);
        if(node.getRight() != null)
        {
            node.getRight().apply(this);
        }
        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
        }
        outAMinusExpr(node);
    }

    public void inAMultExpr(AMultExpr node)
    {
        defaultIn(node);
    }

    public void outAMultExpr(AMultExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAMultExpr(AMultExpr node)
    {
        inAMultExpr(node);
        if(node.getRight() != null)
        {
            node.getRight().apply(this);
        }
        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
        }
        outAMultExpr(node);
    }

    public void inADivExpr(ADivExpr node)
    {
        defaultIn(node);
    }

    public void outADivExpr(ADivExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseADivExpr(ADivExpr node)
    {
        inADivExpr(node);
        if(node.getRight() != null)
        {
            node.getRight().apply(this);
        }
        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
        }
        outADivExpr(node);
    }

    public void inADivisionExpr(ADivisionExpr node)
    {
        defaultIn(node);
    }

    public void outADivisionExpr(ADivisionExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseADivisionExpr(ADivisionExpr node)
    {
        inADivisionExpr(node);
        if(node.getRight() != null)
        {
            node.getRight().apply(this);
        }
        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
        }
        outADivisionExpr(node);
    }

    public void inAModExpr(AModExpr node)
    {
        defaultIn(node);
    }

    public void outAModExpr(AModExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAModExpr(AModExpr node)
    {
        inAModExpr(node);
        if(node.getRight() != null)
        {
            node.getRight().apply(this);
        }
        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
        }
        outAModExpr(node);
    }

    public void inACondCond(ACondCond node)
    {
        defaultIn(node);
    }

    public void outACondCond(ACondCond node)
    {
        defaultOut(node);
    }

    @Override
    public void caseACondCond(ACondCond node)
    {
        inACondCond(node);
        if(node.getCond() != null)
        {
            node.getCond().apply(this);
        }
        outACondCond(node);
    }

    public void inAAndCond(AAndCond node)
    {
        defaultIn(node);
    }

    public void outAAndCond(AAndCond node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAAndCond(AAndCond node)
    {
        inAAndCond(node);
        if(node.getRight() != null)
        {
            node.getRight().apply(this);
        }
        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
        }
        outAAndCond(node);
    }

    public void inAOrCond(AOrCond node)
    {
        defaultIn(node);
    }

    public void outAOrCond(AOrCond node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAOrCond(AOrCond node)
    {
        inAOrCond(node);
        if(node.getRight() != null)
        {
            node.getRight().apply(this);
        }
        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
        }
        outAOrCond(node);
    }

    public void inANumopCond(ANumopCond node)
    {
        defaultIn(node);
    }

    public void outANumopCond(ANumopCond node)
    {
        defaultOut(node);
    }

    @Override
    public void caseANumopCond(ANumopCond node)
    {
        inANumopCond(node);
        if(node.getRight() != null)
        {
            node.getRight().apply(this);
        }
        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
        }
        outANumopCond(node);
    }
}
