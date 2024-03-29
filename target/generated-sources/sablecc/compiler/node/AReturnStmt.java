/* This file was generated by SableCC (http://www.sablecc.org/). */

package compiler.node;

import java.util.*;
import compiler.analysis.*;

@SuppressWarnings("nls")
public final class AReturnStmt extends PStmt
{
    private final LinkedList<PExpr> _expr_ = new LinkedList<PExpr>();

    public AReturnStmt()
    {
        // Constructor
    }

    public AReturnStmt(
        @SuppressWarnings("hiding") List<PExpr> _expr_)
    {
        // Constructor
        setExpr(_expr_);

    }

    @Override
    public Object clone()
    {
        return new AReturnStmt(
            cloneList(this._expr_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAReturnStmt(this);
    }

    public LinkedList<PExpr> getExpr()
    {
        return this._expr_;
    }

    public void setExpr(List<PExpr> list)
    {
        this._expr_.clear();
        this._expr_.addAll(list);
        for(PExpr e : list)
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
            + toString(this._expr_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._expr_.remove(child))
        {
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        for(ListIterator<PExpr> i = this._expr_.listIterator(); i.hasNext();)
        {
            if(i.next() == oldChild)
            {
                if(newChild != null)
                {
                    i.set((PExpr) newChild);
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
