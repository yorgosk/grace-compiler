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
        System.out.printf("inAVarDef with id: %s\n",
                node.getId().toString());
    }

    @Override
    public void inAVarDefNextId(AVarDefNextId node) {
        System.out.printf("inAVarDefNextId with id: %s\n",
                node.getId().toString());
    }

    @Override
    public void inAFuncDecl(AFuncDecl node) {
        System.out.printf("inAFuncDecl\n");
    }
    //------------------------------------------------------------
    @Override
    public void inATerminalStmt(ATerminalStmt node) {
        System.out.printf("inATerminalStatementStmt\n");
    }

    @Override
    public void inAIfStmt(AIfStmt node) {
        System.out.printf("inAIfStatementStmt\n");
    }

    @Override
    public void inAIfIfStmt(AIfIfStmt node) {
        System.out.printf("inAIfIfStmt\n");
    }

    @Override
    public void inAIfElseIfStmt(AIfElseIfStmt node) {
        System.out.printf("inAIfElseIfStmt\n");
    }

    @Override
    public void inAElseWithElse(AElseWithElse node) {
        System.out.printf("inAElseWithElse\n");
    }

    @Override
    public void inANestedIfElseWithElse(ANestedIfElseWithElse node) {
        System.out.printf("inANestedIfElseWithElse\n");
    }

    @Override
    public void inATerminalStatementWithElse(ATerminalStatementWithElse node) {
        System.out.printf("inATerminalStatementWithElse\n");
    }

    @Override
    public void inAWhileStmt(AWhileStmt node) {
        System.out.printf("inAWhileStmt\n");
    }

    @Override
    public void inASemicolonTermStmt(ASemicolonTermStmt node) {
        System.out.printf("inASemicolonTermStmt\n");
    }

    @Override
    public void inAAssignmentTermStmt(AAssignmentTermStmt node) {
        System.out.printf("inAAssignmentTermStmt with l_value: %s\n",
                node.getLValue().toString());
    }

    @Override
    public void inABlockTermStmt(ABlockTermStmt node) {
        System.out.printf("inABlockTermStmt\n");
    }

    @Override
    public void inAFunctionTermStmt(AFunctionTermStmt node) {
        System.out.printf("inAFunctionTermStmt\n");
    }

    @Override
    public void inAReturnExprTermStmt(AReturnExprTermStmt node) {
        System.out.printf("inAReturnExprTermStmt");
    }
    //------------------------------------------------------------
    @Override
    public void inABlock(ABlock node) {
        System.out.printf("inABlock\n");
    }
    //------------------------------------------------------------
    @Override
    public void inAFuncCall(AFuncCall node) {
        System.out.printf("inAFuncCall with id: %s\n",
                node.getId().toString());
    }

    @Override
    public void inAFuncArgs(AFuncArgs node) {
        System.out.printf("inAFuncArgs\n");
    }

    @Override
    public void inAFuncCallNextArg(AFuncCallNextArg node) {
        System.out.printf("AFuncCallNextArg\n");
    }
    //------------------------------------------------------------
    @Override
    public void inAIdLValue(AIdLValue node) {
        System.out.printf("inAIdLValue with id: %s\n",
                node.getId().toString());
    }

    @Override
    public void inAStringLValue(AStringLValue node) {
        System.out.printf("inAStringLValue with string: %s\n",
                node.getStringLiteral().toString());
    }

    @Override
    public void inAExpressionLValue(AExpressionLValue node) {
        System.out.printf("inAExpressionLValue\n");
    }
    //------------------------------------------------------------
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
    public void inASignIntConstSignedExpr(ASignIntConstSignedExpr node) {
        System.out.printf("inASignIntConstSignedExpr with int-const: %s\n",
                node.getIntConst().toString());
    }

    @Override
    public void inASignCharConstSignedExpr(ASignCharConstSignedExpr node) {
        System.out.printf("inASignCharConstSignedExpr with char-const: %s\n",
                node.getCharConst().toString());
    }

    @Override
    public void inASignLValueSignedExpr(ASignLValueSignedExpr node) {
        System.out.printf("inASignLValueSignedExpr with l_value: %s\n",
                node.getLValue().toString());
    }

    @Override
    public void inASignFuncCallSignedExpr(ASignFuncCallSignedExpr node) {
        System.out.printf("inASignFuncCallSignedExpr\n");
    }

    @Override
    public void inASignExprInParsSignedExpr(ASignExprInParsSignedExpr node) {
        System.out.printf("inASignExprInParsSignedExpr\n");
    }

    @Override
    public void inAPlusSignSign(APlusSignSign node) {
        System.out.printf("inAPlusSignSign: %s\n",
                node.getPlus().toString());
    }

    @Override
    public void inAMinusSignSign(AMinusSignSign node) {
        System.out.printf("inAMinusSignSign: %s\n",
                node.getMinus().toString());
    }

    @Override
    public void inATermMultFactorTerm(ATermMultFactorTerm node) {
        System.out.printf("inATermMultFactorTerm\n");
    }

    @Override
    public void inATermDivFactorTerm(ATermDivFactorTerm node) {
        System.out.printf("inATermDivFactorTerm\n");
    }

    @Override
    public void inATermModFactorTerm(ATermModFactorTerm node) {
        System.out.printf("inATermModFactorTerm\n");
    }

    @Override
    public void inAFactorTerm(AFactorTerm node) {
        System.out.printf("inAFactorTerm\n");
    }

    @Override
    public void inAIntConstFactor(AIntConstFactor node) {
        System.out.printf("inAIntConstFactor\n");
    }

    @Override
    public void inACharConstFactor(ACharConstFactor node) {
        System.out.printf("inACharConstFactor\n");
    }

    @Override
    public void inALValueFactor(ALValueFactor node) {
        System.out.printf("inALValueFactor\n");
    }

    @Override
    public void inAFuncCallFactor(AFuncCallFactor node) {
        System.out.printf("inAFuncCallFactor\n");
    }

    @Override
    public void inAExprInParsFactor(AExprInParsFactor node) {
        System.out.printf("inAExprInParsFactor\n");
    }

    @Override
    public void inAExprInPars(AExprInPars node) {
        System.out.printf("inAExprInPars\n");
    }
    //------------------------------------------------------------
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
