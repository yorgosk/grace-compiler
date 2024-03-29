/* This file was generated by SableCC (http://www.sablecc.org/). */

package compiler.node;

import java.util.*;
import compiler.analysis.*;

@SuppressWarnings("nls")
public final class AFparDef extends PFparDef
{
    private TRef _ref_;
    private TId _id_;
    private final LinkedList<TId> _next_ = new LinkedList<TId>();
    private PFparType _fparType_;

    public AFparDef()
    {
        // Constructor
    }

    public AFparDef(
        @SuppressWarnings("hiding") TRef _ref_,
        @SuppressWarnings("hiding") TId _id_,
        @SuppressWarnings("hiding") List<TId> _next_,
        @SuppressWarnings("hiding") PFparType _fparType_)
    {
        // Constructor
        setRef(_ref_);

        setId(_id_);

        setNext(_next_);

        setFparType(_fparType_);

    }

    @Override
    public Object clone()
    {
        return new AFparDef(
            cloneNode(this._ref_),
            cloneNode(this._id_),
            cloneList(this._next_),
            cloneNode(this._fparType_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAFparDef(this);
    }

    public TRef getRef()
    {
        return this._ref_;
    }

    public void setRef(TRef node)
    {
        if(this._ref_ != null)
        {
            this._ref_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._ref_ = node;
    }

    public TId getId()
    {
        return this._id_;
    }

    public void setId(TId node)
    {
        if(this._id_ != null)
        {
            this._id_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._id_ = node;
    }

    public LinkedList<TId> getNext()
    {
        return this._next_;
    }

    public void setNext(List<TId> list)
    {
        this._next_.clear();
        this._next_.addAll(list);
        for(TId e : list)
        {
            if(e.parent() != null)
            {
                e.parent().removeChild(e);
            }

            e.parent(this);
        }
    }

    public PFparType getFparType()
    {
        return this._fparType_;
    }

    public void setFparType(PFparType node)
    {
        if(this._fparType_ != null)
        {
            this._fparType_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._fparType_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._ref_)
            + toString(this._id_)
            + toString(this._next_)
            + toString(this._fparType_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._ref_ == child)
        {
            this._ref_ = null;
            return;
        }

        if(this._id_ == child)
        {
            this._id_ = null;
            return;
        }

        if(this._next_.remove(child))
        {
            return;
        }

        if(this._fparType_ == child)
        {
            this._fparType_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._ref_ == oldChild)
        {
            setRef((TRef) newChild);
            return;
        }

        if(this._id_ == oldChild)
        {
            setId((TId) newChild);
            return;
        }

        for(ListIterator<TId> i = this._next_.listIterator(); i.hasNext();)
        {
            if(i.next() == oldChild)
            {
                if(newChild != null)
                {
                    i.set((TId) newChild);
                    newChild.parent(this);
                    oldChild.parent(null);
                    return;
                }

                i.remove();
                oldChild.parent(null);
                return;
            }
        }

        if(this._fparType_ == oldChild)
        {
            setFparType((PFparType) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
