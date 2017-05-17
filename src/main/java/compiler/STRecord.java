package compiler;

public class STRecord {
    public String type;
    public String name;
    public boolean isRef;
    public boolean isParam;
    public boolean isLocal;

    /* STRecord's class setters and getters */
    public STRecord(String type, String name, boolean ref, boolean param, boolean local) {
        this.type = type;
        this.name = name;
        this.isRef = ref;
        this.isParam = param;
        this.isLocal = local;
    }

    /* STRecord's class setters and getters */
    public void setType(String type) { this.type = type; }
    public void setName(String name) { this.name = name; }
    public void setRef(boolean ref) { this.isRef = ref; }
    public void setParam(boolean param) { this.isParam = param; }
    public void setLocal(boolean local) { this.isLocal = local; }
    public String getType() { return this.type; }
    public String getName() { return this.name; }
    public boolean getRef() { return this.isRef; }
    public boolean getParam() { return this.isParam; }
    public boolean getLocal() { return this.isLocal; }

}
