package compiler;

public class STRecord {
    private String type;
    private String name;
    private boolean isRef;
    private boolean isParam;
    private boolean isLocal;
    private Integer shadowIndex;

    /* STRecord's class setters and getters */
    public STRecord(String type, String name, boolean ref, boolean param, boolean local) {
        this.type = type;
        this.name = name;
        this.isRef = ref;
        this.isParam = param;
        this.isLocal = local;
        this.shadowIndex = -1;
    }

    /* STRecord's class setters and getters */
    public void setType(String type) { this.type = type; }
    public void setName(String name) { this.name = name; }
    public void setRef(boolean ref) { this.isRef = ref; }
    public void setParam(boolean param) { this.isParam = param; }
    public void setLocal(boolean local) { this.isLocal = local; }
    public void setShadowIndex(Integer shadowIndex) { this.shadowIndex = shadowIndex; }
    public String getType() { return this.type; }
    public String getName() { return this.name; }
    public boolean getRef() { return this.isRef; }
    public boolean getParam() { return this.isParam; }
    public boolean getLocal() { return this.isLocal; }
    public Integer getShadowIndex() { return this.shadowIndex; }

}
