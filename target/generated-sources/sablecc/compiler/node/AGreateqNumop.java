/* This file was generated by SableCC (http://www.sablecc.org/). */

package compiler.node;

import compiler.analysis.*;

@SuppressWarnings("nls")
public final class AGreateqNumop extends PNumop
{
    private TGreateq _greateq_;

    public AGreateqNumop()
    {
        // Constructor
    }

    public AGreateqNumop(
        @SuppressWarnings("hiding") TGreateq _greateq_)
    {
        // Constructor
        setGreateq(_greateq_);

    }

    @Override
    public Object clone()
    {
        return new AGreateqNumop(
            cloneNode(this._greateq_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAGreateqNumop(this);
    }

    public TGreateq getGreateq()
    {
        return this._greateq_;
    }

    public void setGreateq(TGreateq node)
    {
        if(this._greateq_ != null)
        {
            this._greateq_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._greateq_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._greateq_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._greateq_ == child)
        {
            this._greateq_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._greateq_ == oldChild)
        {
            setGreateq((TGreateq) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}