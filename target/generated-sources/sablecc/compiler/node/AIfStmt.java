/* This file was generated by SableCC (http://www.sablecc.org/). */

package compiler.node;

import java.util.*;
import compiler.analysis.*;

@SuppressWarnings("nls")
public final class AIfStmt extends PStmt
{
    private PCond _cond_;
    private PStmt _thenM_;
    private final LinkedList<PStmt> _elseM_ = new LinkedList<PStmt>();

    public AIfStmt()
    {
        // Constructor
    }

    public AIfStmt(
        @SuppressWarnings("hiding") PCond _cond_,
        @SuppressWarnings("hiding") PStmt _thenM_,
        @SuppressWarnings("hiding") List<PStmt> _elseM_)
    {
        // Constructor
        setCond(_cond_);

        setThenM(_thenM_);

        setElseM(_elseM_);

    }

    @Override
    public Object clone()
    {
        return new AIfStmt(
            cloneNode(this._cond_),
            cloneNode(this._thenM_),
            cloneList(this._elseM_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAIfStmt(this);
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

    public PStmt getThenM()
    {
        return this._thenM_;
    }

    public void setThenM(PStmt node)
    {
        if(this._thenM_ != null)
        {
            this._thenM_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._thenM_ = node;
    }

    public LinkedList<PStmt> getElseM()
    {
        return this._elseM_;
    }

    public void setElseM(List<PStmt> list)
    {
        this._elseM_.clear();
        this._elseM_.addAll(list);
        for(PStmt e : list)
        {
            if(e.parent() != null)
            {
                e.parent().removeChild(e);
            }

            e.parent(this);
        }
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._cond_)
            + toString(this._thenM_)
            + toString(this._elseM_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._cond_ == child)
        {
            this._cond_ = null;
            return;
        }

        if(this._thenM_ == child)
        {
            this._thenM_ = null;
            return;
        }

        if(this._elseM_.remove(child))
        {
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._cond_ == oldChild)
        {
            setCond((PCond) newChild);
            return;
        }

        if(this._thenM_ == oldChild)
        {
            setThenM((PStmt) newChild);
            return;
        }

        for(ListIterator<PStmt> i = this._elseM_.listIterator(); i.hasNext();)
        {
            if(i.next() == oldChild)
            {
                if(newChild != null)
                {
                    i.set((PStmt) newChild);
                    newChild.parent(this);
                    oldChild.parent(null);
                    return;
                }

                i.remove();
                oldChild.parent(null);
                return;
            }
        }

        throw new RuntimeException("Not a child.");
    }
}
