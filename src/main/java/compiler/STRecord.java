package compiler;

public class STRecord {
    public static class Type {
        private String kind;
        private boolean isArray;
        private Integer dimension;

        /* Type's (default-)constructor */
        public Type() {
            this.kind = null;
            this.isArray = false;
            this.dimension = 0;
        }

        /* Type's copy-constructor */
        public Type(Type temp) {
            this.kind = temp.kind;
            this.isArray = temp.isArray;
            this.dimension = temp.dimension;
        }

        /* Type's class setters and getters */
        public void setKind(String kind) { this.kind = kind; }
        public void setArray(boolean array) { this.isArray = array; }
        public void setDimension(Integer dimension) { this.dimension = dimension; }
        public String getKind() { return this.kind; }
        public boolean getArray() { return this.isArray; }
        public Integer getDimension() { return this.dimension; }

        /* Type's class various functions */
        public boolean isSame(Type other) {
            if(this.kind != other.kind) return false;
            if(this.isArray != other.isArray) return false;
            if(this.dimension != other.dimension) return false;

            return true;
        }

        /* Type's class printing function */
        public void printType() {
            System.out.printf("\t|__> TYPE -> kind: %s - isArray: %b - dimension: %d\n", this.kind, this.isArray, this.dimension);
        }

    }

    public Type type;       // makes things easier -- spares us functions
    private String name;
    private boolean isRef;
    private boolean isParam;
    private boolean isLocal;
    private boolean isFunc;
    private boolean isFuncDecl;
    private Integer scopeId;
    private Integer shadowIndex;

    /* STRecord's (default-)constructor */
    public STRecord() {
        this.type = null;
        this.name = null;
        this.isRef = false;
        this.isParam = false;
        this.isLocal = true;
        this.isFunc = false;
        this.isFuncDecl = false;
        this.scopeId = -1;
        this.shadowIndex = -1;
    }

    /* STRecord's class copy-constructor */
    public STRecord(STRecord temp) {
        this.type = new Type(temp.type);
        this.name = temp.name;
        this.isRef = temp.isRef;
        this.isParam = temp.isParam;
        this.isLocal = temp.isLocal;
        this.isFunc = temp.isFunc;
        this.isFuncDecl = temp.isFuncDecl;
        this.scopeId = temp.scopeId;
        this.shadowIndex = temp.shadowIndex;
    }

    /* STRecord's class setters and getters */
    public void setType(Type temp) { this.type = new Type(temp); }
    public void setName(String name) { this.name = name; }
    public void setRef(boolean ref) { this.isRef = ref; }
    public void setParam(boolean param) { this.isParam = param; }
    public void setLocal(boolean local) { this.isLocal = local; }
    public void setFunc(boolean func) { this.isFunc = func; }
    public void setFuncDecl(boolean funcDecl) { this.isFuncDecl = funcDecl; }
    public void setScopeId(Integer scopeId) { this.scopeId = scopeId; }
    public void setShadowIndex(Integer shadowIndex) { this.shadowIndex = shadowIndex; }
    public Type getType() { return this.type; }
    public String getName() { return this.name; }
    public boolean getRef() { return this.isRef; }
    public boolean getParam() { return this.isParam; }
    public boolean getLocal() { return this.isLocal; }
    public boolean getFunc() { return this.isFunc; }
    public boolean getFuncDecl() { return this.isFuncDecl; }
    public Integer getScopeId() { return this.scopeId; }
    public Integer getShadowIndex() { return this.shadowIndex; }

    /* STRecord's class printing function */
    public void printSTRecord() {
        System.out.printf("STR -> name: %s - isRef: %b - isParam: %b - isLocal: %b - isFunc: %b - isFuncDecl: %b - scopeId: %d - shadowIndex: %d\n",
                this.name, this.isRef, this.isParam, this.isLocal, this.isFunc, this.isFuncDecl, this.scopeId, this.shadowIndex);
        this.type.printType();
    }

}
