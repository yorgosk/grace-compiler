/* This file was generated by SableCC (http://www.sablecc.org/). */

package compiler.node;

import compiler.analysis.*;

@SuppressWarnings("nls")
public final class ASignedExpr extends PExpr
{
    private PSign _sign_;
    private PExpr _expr_;

    public ASignedExpr()
    {
        // Constructor
    }

    public ASignedExpr(
        @SuppressWarnings("hiding") PSign _sign_,
        @SuppressWarnings("hiding") PExpr _expr_)
    {
        // Constructor
        setSign(_sign_);

        setExpr(_expr_);

    }

    @Override
    public Object clone()
    {
        return new ASignedExpr(
            cloneNode(this._sign_),
            cloneNode(this._expr_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseASignedExpr(this);
    }

    public PSign getSign()
    {
        return this._sign_;
    }

    public void setSign(PSign node)
    {
        if(this._sign_ != null)
        {
            this._sign_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._sign_ = node;
    }

    public PExpr getExpr()
    {
        return this._expr_;
    }

    public void setExpr(PExpr node)
    {
        if(this._expr_ != null)
        {
            this._expr_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._expr_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._sign_)
            + toString(this._expr_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._sign_ == child)
        {
            this._sign_ = null;
            return;
        }

        if(this._expr_ == child)
        {
            this._expr_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._sign_ == oldChild)
        {
            setSign((PSign) newChild);
            return;
        }

        if(this._expr_ == oldChild)
        {
            setExpr((PExpr) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
