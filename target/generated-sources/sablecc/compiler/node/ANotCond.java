/* This file was generated by SableCC (http://www.sablecc.org/). */

package compiler.node;

import compiler.analysis.*;

@SuppressWarnings("nls")
public final class ANotCond extends PCond
{
    private PNotCond _notCond_;

    public ANotCond()
    {
        // Constructor
    }

    public ANotCond(
        @SuppressWarnings("hiding") PNotCond _notCond_)
    {
        // Constructor
        setNotCond(_notCond_);

    }

    @Override
    public Object clone()
    {
        return new ANotCond(
            cloneNode(this._notCond_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseANotCond(this);
    }

    public PNotCond getNotCond()
    {
        return this._notCond_;
    }

    public void setNotCond(PNotCond node)
    {
        if(this._notCond_ != null)
        {
            this._notCond_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._notCond_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._notCond_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._notCond_ == child)
        {
            this._notCond_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._notCond_ == oldChild)
        {
            setNotCond((PNotCond) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
