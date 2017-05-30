package compiler;

import java.util.ArrayList;
import java.util.List;

public class STRecord {
    public static class Type {
        private String kind;
        private boolean isRef;
        private boolean isArray;
        private Integer dimension;
        private boolean isFunction;
        private ArrayList<Type> parameters;

        /* Type's (default-)constructor */
        public Type() {
            this.kind = null;
            this.isRef = false;
            this.isArray = false;
            this.dimension = 0;
            this.isFunction = false;
            this.parameters = null;
        }

        /* Type's constructor */
        public Type(String kind, boolean isRef, boolean isArray, Integer dimension, boolean isFunction, ArrayList<Type> parameters) {
            this.kind = kind;
            this.isRef = isRef;
            this.isArray = isArray;
            this.dimension = dimension;
            this.isFunction = isFunction;
            this.parameters = parameters;
        }

        /* Type's copy-constructor */
        public Type(Type temp) {
            this.kind = temp.kind;
            this.isRef = temp.isRef;
            this.isArray = temp.isArray;
            this.dimension = temp.dimension;
            this.isFunction = temp.isFunction;
            this.parameters = temp.parameters;
        }

        /* Type's class setters and getters */
        public void setKind(String kind) { this.kind = kind; }
        public void setRef(boolean ref) { this.isRef = ref; }
        public void setArray(boolean array) { this.isArray = array; }
        public void setDimension(Integer dimension) { this.dimension = dimension; }
        public void setFunction(boolean function) { this.isFunction = function; }
        public void setParameters(ArrayList<Type> parameters) { this.parameters = parameters; }
        public String getKind() { return this.kind; }
        public boolean getRef() { return this.isRef; }
        public boolean getArray() { return this.isArray; }
        public Integer getDimension() { return this.dimension; }
        public boolean getFunction() { return this.isFunction; }
        public ArrayList<Type> getParameters() { return this.parameters; }

        /* Type's class various functions */
        // check if two types are equal -- general case
        public boolean isSame(Type other) {
            if (!this.kind.equals(other.kind)) return false;		//modified by yiannis
            if (this.isArray != other.isArray) return false;
            if (this.dimension != other.dimension) return false;
            if (this.isFunction != other.isFunction) return false;
//            if (compareList(this.parameters, other.parameters)) return false;
            if ((this.parameters == null && other.parameters != null) || (this.parameters != null && other.parameters == null)) return false;
            if (this.parameters != null && other.parameters != null) {
                if (this.parameters.size() != other.parameters.size()) return false;
                for (int i = 0; i < this.parameters.size(); i++) {
                    if (!this.parameters.get(i).isSame(other.parameters.get(i))) return false;
                }
            }

            return true;
        }
        // check if two types are equal -- function type vs return type case
        public boolean isSameRetType(Type other) {
            if (!this.kind.equals(other.kind)) return false;		//modified by yiannis
            if (this.isArray != other.isArray) return false;
            if (this.dimension != other.dimension) return false;
//            if (this.isFunction != other.isFunction) return false;
//            if (compareList(this.parameters, other.parameters)) return false;
//            if ((this.parameters == null && other.parameters != null) || (this.parameters != null && other.parameters == null)) return false;
//            if (this.parameters != null && other.parameters != null) {
//                if (this.parameters.size() != other.parameters.size()) return false;
//                for (int i = 0; i < this.parameters.size(); i++) {
//                    if (!this.parameters.get(i).isSame(other.parameters.get(i))) return false;
//                }
//            }

            return true;
        }
        // add a parameter in the beginning of our Type's parameter list
        public void addParameter(Type parameter) {
            if (this.parameters == null) {
                this.parameters = new ArrayList<Type>();
            }
            this.parameters.add(0, parameter);
        }

        /* Type's class printing function */
        public void printType() {
            System.out.printf("\t|__> TYPE -> kind: %s - isArray: %b - dimension: %d - isFunction: %b\n",
                    this.kind, this.isArray, this.dimension, this.isFunction);
        }

    }

    public Type type;       // makes things easier -- spares us functions
    private String name;
    private boolean isParam;
    private boolean isLocal;
    private boolean isDefined;
    private Integer scopeId;
    private Integer shadowIndex;

    /* STRecord's (default-)constructor */
    public STRecord() {
        this.type = null;
        this.name = null;
        this.isParam = false;
        this.isLocal = true;
        this.isDefined = true;
        this.scopeId = -1;
        this.shadowIndex = -1;
    }

    /* STRecord's constructor */
    public STRecord(Type type, String name, boolean isParam, boolean isLocal, boolean isDefined, Integer scopeId, Integer shadowIndex) {
        this.type = new Type(type);
        this.name = name;
        this.isParam = isParam;
        this.isLocal = isLocal;
        this.isDefined = isDefined;
        this.scopeId = scopeId;
        this.shadowIndex = shadowIndex;
    }

    /* STRecord's class copy-constructor */
    public STRecord(STRecord temp) {
        this.type = new Type(temp.type);
        this.name = temp.name;
        this.isParam = temp.isParam;
        this.isLocal = temp.isLocal;
        this.isDefined = temp.isDefined;
        this.scopeId = temp.scopeId;
        this.shadowIndex = temp.shadowIndex;
    }

    /* STRecord's class setters and getters */
    public void setType(Type temp) { this.type = new Type(temp); }
    public void setName(String name) { this.name = name; }
    public void setParam(boolean param) { this.isParam = param; }
    public void setLocal(boolean local) { this.isLocal = local; }
    public void setDefined(boolean defined) { this.isDefined = defined; }
    public void setScopeId(Integer scopeId) { this.scopeId = scopeId; }
    public void setShadowIndex(Integer shadowIndex) { this.shadowIndex = shadowIndex; }
    public Type getType() { return this.type; }
    public String getName() { return this.name; }
    public boolean getParam() { return this.isParam; }
    public boolean getLocal() { return this.isLocal; }
    public boolean getDefined() { return this.isDefined; }
    public Integer getScopeId() { return this.scopeId; }
    public Integer getShadowIndex() { return this.shadowIndex; }

    /* STRecord's class printing function */
    public void printSTRecord() {
        System.out.printf("STR -> name: %s - isParam: %b - isLocal: %b - isDefined: %b - scopeId: %d - shadowIndex: %d\n",
                this.name, this.isParam, this.isLocal, this.isDefined, this.scopeId, this.shadowIndex);
        this.type.printType();
    }

    /* A function that can compare two lists whenever needed (to compare the parameters of two names-functions) */
    public static boolean compareList(List ls1, List ls2){
        return ls1.toString().contentEquals(ls2.toString()) ? true:false;
    }

    /* created with the intent to be used to load Grace's library-functions */
    public STRecord formFunction(String name, ArrayList<Type> parameters, String retKind) {
        STRecord temp = new STRecord();

        temp.type = new Type();
        temp.type.setKind(retKind);
        temp.type.setFunction(true);
        temp.type.setParameters(parameters);

        temp.setName(name);

        return temp;
    }

}
