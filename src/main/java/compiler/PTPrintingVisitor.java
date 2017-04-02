package compiler;

import compiler.analysis.DepthFirstAdapter;
import compiler.node.*;

public class PTPrintingVisitor extends DepthFirstAdapter {

    @Override
    public void inAProgram(AProgram node) {
        System.out.printf("inAProgram\n");
    }

    @Override
    public void inAFuncDef(AFuncDef node) {
        System.out.printf("inAFuncDef\n");
    }

    @Override
    public void inAHeader(AHeader node) {
        System.out.printf("inAHeader with id: %s\n",
                node.getId().toString());
    }

    @Override
    public void inAHeaderParams(AHeaderParams node) {
        System.out.printf("inAFuncParams\n");
    }

    @Override
    public void inAHeaderNextArg(AHeaderNextArg node) {
        System.out.printf("inAHeaderNextArg\n");
    }

    @Override
    public void inAFparDef(AFparDef node) {
        System.out.printf("inAFparDef with id: %s\n",
                node.getId().toString());
    }

    @Override
    public void inAFparDefNextId(AFparDefNextId node) {
        System.out.printf("inAFparDefNextId with id: %s\n",
                node.getId().toString());
    }

    @Override
    public void inAIntDataType(AIntDataType node) {
        System.out.printf("inAIntDataType\n");
    }

    @Override
    public void inACharDataType(ACharDataType node) {
        System.out.printf("inACharDataType\n");
    }

    @Override
    public void inAType(AType node) {
        System.out.printf("inAType\n");
    }

    @Override
    public void inADimension(ADimension node) {
        System.out.printf("inADimension with int-const: %s\n",
                node.getIntConst().toString());
    }

    @Override
    public void inADataTypeRetType(ADataTypeRetType node) {
        System.out.printf("inADataTypeRetType\n");
    }

    @Override
    public void inANothingRetType(ANothingRetType node) {
        System.out.printf("inANothingRetType\n");
    }

    @Override
    public void inAFparType(AFparType node) {
        System.out.printf("inAFparType\n");
    }

    @Override
    public void inANextDimension(ANextDimension node) {
        System.out.printf("inANextDimension with int-const: %s\n",
                node.getIntConst().toString());
    }

    @Override
    public void inAFuncDefLocalDef(AFuncDefLocalDef node) {
        System.out.printf("inAFuncDefLocalDef\n");
    }

    @Override
    public void inAFuncDeclLocalDef(AFuncDeclLocalDef node) {
        System.out.printf("inAFuncDeclLocalDef\n");
    }

    @Override
    public void inAVarDefLocalDef(AVarDefLocalDef node) {
        System.out.printf("inAVarDefLocalDef\n");
    }

    @Override
    public void inAVarDef(AVarDef node) {
        System.out.printf("inAVarDef\n");
    }

    @Override
    public void inAFuncDecl(AFuncDecl node) {
        System.out.printf("inAFuncDecl\n");
    }

    @Override
    public void inATerminalStmt(ATerminalStmt node) {
        System.out.printf("inATerminalStatementStmt\n");
    }

    @Override
    public void inAIfStmt(AIfStmt node) {
        System.out.printf("inAIfStatementStmt\n");
    }

    @Override
    public void inAWhileStmt(AWhileStmt node) {
        System.out.printf("inAWhileStmt\n");
    }

    @Override
    public void inABlock(ABlock node) {
        System.out.printf("inABlock\n");
    }

    @Override
    public void inAFuncCall(AFuncCall node) {
        System.out.printf("inAFuncCall\n");
    }

    @Override
    public void inAIdLValue(AIdLValue node) {
        System.out.printf("inAIdLValue\n");
    }

    @Override
    public void inAStringLValue(AStringLValue node) {
        System.out.printf("inAStringLValue\n");
    }

    @Override
    public void inAExpressionLValue(AExpressionLValue node) {
        System.out.printf("inAExpressionLValue\n");
    }

    @Override
    public void inASignedExpr(ASignedExpr node) {
        System.out.printf("inASignedExpr\n");
    }

    @Override
    public void inAExprPlusTermExpr(AExprPlusTermExpr node) {
        System.out.printf("inAExprPlusTermExpr\n");
    }

    @Override
    public void inAExprMinusTermExpr(AExprMinusTermExpr node) {
        System.out.printf("inAExprMinusTermExpr\n");
    }

    @Override
    public void inATermExpr(ATermExpr node) {
        System.out.printf("inATermExpr\n");
    }

    @Override
    public void inANotCond(ANotCond node) {
        System.out.printf("inANotCond\n");
    }

    @Override
    public void inACondAndTermcondCond(ACondAndTermcondCond node) {
        System.out.printf("inACondAndTermcondCond");
    }

    @Override
    public void inACondOrTermcondCond(ACondOrTermcondCond node) {
        System.out.printf("inACondOrTermcondCond\n");
    }

    @Override
    public void inATermcondCond(ATermcondCond node) {
        System.out.printf("inATermcondCond\n");
    }

}
