/* This file was generated by SableCC (http://www.sablecc.org/). */

package compiler.node;

import compiler.analysis.*;

@SuppressWarnings("nls")
public final class ASignCharConstSignedExpr extends PSignedExpr
{
    private PSign _sign_;
    private TCharConst _charConst_;

    public ASignCharConstSignedExpr()
    {
        // Constructor
    }

    public ASignCharConstSignedExpr(
        @SuppressWarnings("hiding") PSign _sign_,
        @SuppressWarnings("hiding") TCharConst _charConst_)
    {
        // Constructor
        setSign(_sign_);

        setCharConst(_charConst_);

    }

    @Override
    public Object clone()
    {
        return new ASignCharConstSignedExpr(
            cloneNode(this._sign_),
            cloneNode(this._charConst_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseASignCharConstSignedExpr(this);
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

    public TCharConst getCharConst()
    {
        return this._charConst_;
    }

    public void setCharConst(TCharConst node)
    {
        if(this._charConst_ != null)
        {
            this._charConst_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._charConst_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._sign_)
            + toString(this._charConst_);
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

        if(this._charConst_ == child)
        {
            this._charConst_ = null;
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

        if(this._charConst_ == oldChild)
        {
            setCharConst((TCharConst) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
