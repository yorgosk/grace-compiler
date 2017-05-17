package compiler;

public class STRecord {
    public String type;
    public String name;
    public boolean byRef;

    /* STRecord's class setters and getters */
    public void setType(String type) { this.type = type; }
    public void setName(String name) { this.name = name; }
    public void setByRef(boolean ref) { this.byRef = ref; }
    public String getType() { return this.type; }
    public String getName() { return this.name; }
    public boolean getByRef() { return this.byRef; }

}
