/* This file was generated by SableCC (http://www.sablecc.org/). */

package compiler.node;

import compiler.analysis.*;

@SuppressWarnings("nls")
public final class AExprInParsFactor extends PFactor
{
    private PExprInPars _exprInPars_;

    public AExprInParsFactor()
    {
        // Constructor
    }

    public AExprInParsFactor(
        @SuppressWarnings("hiding") PExprInPars _exprInPars_)
    {
        // Constructor
        setExprInPars(_exprInPars_);

    }

    @Override
    public Object clone()
    {
        return new AExprInParsFactor(
            cloneNode(this._exprInPars_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAExprInParsFactor(this);
    }

    public PExprInPars getExprInPars()
    {
        return this._exprInPars_;
    }

    public void setExprInPars(PExprInPars node)
    {
        if(this._exprInPars_ != null)
        {
            this._exprInPars_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._exprInPars_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._exprInPars_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._exprInPars_ == child)
        {
            this._exprInPars_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._exprInPars_ == oldChild)
        {
            setExprInPars((PExprInPars) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
