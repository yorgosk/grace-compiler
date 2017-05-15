/* This file was generated by SableCC (http://www.sablecc.org/). */

package compiler.node;

import compiler.analysis.*;

@SuppressWarnings("nls")
public final class AAssignmentOtherStmt extends POtherStmt
{
    private PLValue _lValue_;
    private TAssignment _assignment_;
    private PExpr _expr_;
    private TSemicolon _semicolon_;

    public AAssignmentOtherStmt()
    {
        // Constructor
    }

    public AAssignmentOtherStmt(
        @SuppressWarnings("hiding") PLValue _lValue_,
        @SuppressWarnings("hiding") TAssignment _assignment_,
        @SuppressWarnings("hiding") PExpr _expr_,
        @SuppressWarnings("hiding") TSemicolon _semicolon_)
    {
        // Constructor
        setLValue(_lValue_);

        setAssignment(_assignment_);

        setExpr(_expr_);

        setSemicolon(_semicolon_);

    }

    @Override
    public Object clone()
    {
        return new AAssignmentOtherStmt(
            cloneNode(this._lValue_),
            cloneNode(this._assignment_),
            cloneNode(this._expr_),
            cloneNode(this._semicolon_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAAssignmentOtherStmt(this);
    }

    public PLValue getLValue()
    {
        return this._lValue_;
    }

    public void setLValue(PLValue node)
    {
        if(this._lValue_ != null)
        {
            this._lValue_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._lValue_ = node;
    }

    public TAssignment getAssignment()
    {
        return this._assignment_;
    }

    public void setAssignment(TAssignment node)
    {
        if(this._assignment_ != null)
        {
            this._assignment_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._assignment_ = node;
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

    public TSemicolon getSemicolon()
    {
        return this._semicolon_;
    }

    public void setSemicolon(TSemicolon node)
    {
        if(this._semicolon_ != null)
        {
            this._semicolon_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._semicolon_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._lValue_)
            + toString(this._assignment_)
            + toString(this._expr_)
            + toString(this._semicolon_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._lValue_ == child)
        {
            this._lValue_ = null;
            return;
        }

        if(this._assignment_ == child)
        {
            this._assignment_ = null;
            return;
        }

        if(this._expr_ == child)
        {
            this._expr_ = null;
            return;
        }

        if(this._semicolon_ == child)
        {
            this._semicolon_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._lValue_ == oldChild)
        {
            setLValue((PLValue) newChild);
            return;
        }

        if(this._assignment_ == oldChild)
        {
            setAssignment((TAssignment) newChild);
            return;
        }

        if(this._expr_ == oldChild)
        {
            setExpr((PExpr) newChild);
            return;
        }

        if(this._semicolon_ == oldChild)
        {
            setSemicolon((TSemicolon) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
