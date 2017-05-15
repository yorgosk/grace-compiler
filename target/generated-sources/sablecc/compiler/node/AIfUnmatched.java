/* This file was generated by SableCC (http://www.sablecc.org/). */

package compiler.node;

import compiler.analysis.*;

@SuppressWarnings("nls")
public final class AIfUnmatched extends PUnmatched
{
    private TIf _if_;
    private PCond _cond_;
    private TThen _then_;
    private PStmt _stmt_;

    public AIfUnmatched()
    {
        // Constructor
    }

    public AIfUnmatched(
        @SuppressWarnings("hiding") TIf _if_,
        @SuppressWarnings("hiding") PCond _cond_,
        @SuppressWarnings("hiding") TThen _then_,
        @SuppressWarnings("hiding") PStmt _stmt_)
    {
        // Constructor
        setIf(_if_);

        setCond(_cond_);

        setThen(_then_);

        setStmt(_stmt_);

    }

    @Override
    public Object clone()
    {
        return new AIfUnmatched(
            cloneNode(this._if_),
            cloneNode(this._cond_),
            cloneNode(this._then_),
            cloneNode(this._stmt_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAIfUnmatched(this);
    }

    public TIf getIf()
    {
        return this._if_;
    }

    public void setIf(TIf node)
    {
        if(this._if_ != null)
        {
            this._if_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._if_ = node;
    }

    public PCond getCond()
    {
        return this._cond_;
    }

    public void setCond(PCond node)
    {
        if(this._cond_ != null)
        {
            this._cond_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._cond_ = node;
    }

    public TThen getThen()
    {
        return this._then_;
    }

    public void setThen(TThen node)
    {
        if(this._then_ != null)
        {
            this._then_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._then_ = node;
    }

    public PStmt getStmt()
    {
        return this._stmt_;
    }

    public void setStmt(PStmt node)
    {
        if(this._stmt_ != null)
        {
            this._stmt_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._stmt_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._if_)
            + toString(this._cond_)
            + toString(this._then_)
            + toString(this._stmt_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._if_ == child)
        {
            this._if_ = null;
            return;
        }

        if(this._cond_ == child)
        {
            this._cond_ = null;
            return;
        }

        if(this._then_ == child)
        {
            this._then_ = null;
            return;
        }

        if(this._stmt_ == child)
        {
            this._stmt_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._if_ == oldChild)
        {
            setIf((TIf) newChild);
            return;
        }

        if(this._cond_ == oldChild)
        {
            setCond((PCond) newChild);
            return;
        }

        if(this._then_ == oldChild)
        {
            setThen((TThen) newChild);
            return;
        }

        if(this._stmt_ == oldChild)
        {
            setStmt((PStmt) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
