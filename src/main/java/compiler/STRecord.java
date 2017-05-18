package compiler;

public class STRecord {
    private String type;
    private String name;
    private boolean isRef;
    private boolean isParam;
    private boolean isLocal;
    private boolean isFunc;
    private boolean isFuncDef;
    private Integer scopeId;
    private Integer shadowIndex;

    /* STRecord's (default-)constructor */
    public STRecord() {
        this.scopeId = -1;
        this.shadowIndex = -1;
    }

    /* STRecord's class setters and getters */
    public void setType(String type) { this.type = type; }
    public void setName(String name) { this.name = name; }
    public void setRef(boolean ref) { this.isRef = ref; }
    public void setParam(boolean param) { this.isParam = param; }
    public void setLocal(boolean local) { this.isLocal = local; }
    public void setFunc(boolean func) { this.isFunc = func; }
    public void setFuncDef(boolean funcDef) { this.isFuncDef = funcDef; }
    public void setScopeId(Integer scopeId) { this.scopeId = scopeId; }
    public void setShadowIndex(Integer shadowIndex) { this.shadowIndex = shadowIndex; }
    public String getType() { return this.type; }
    public String getName() { return this.name; }
    public boolean getRef() { return this.isRef; }
    public boolean getParam() { return this.isParam; }
    public boolean getLocal() { return this.isLocal; }
    public boolean getFunc() { return this.isFunc; }
    public boolean getFuncDef() { return this.isFuncDef; }
    public Integer getScopeId() { return this.scopeId; }
    public Integer getShadowIndex() { return this.shadowIndex; }

    /* STRecord's class printing function */
    public void printSTRecord() {
        System.out.printf("STR -> type: %s - name: %s - isRef: %b - isParam: %b - isLocal: %b - isFunc: %b - isFuncDef: %b - scopeId: %d - shadowIndex: %d\n",
                this.type, this.name, this.isRef, this.isParam, this.isLocal, this.isFunc, this.isFuncDef, this.scopeId, this.shadowIndex);
    }

}
