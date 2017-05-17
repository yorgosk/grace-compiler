package compiler;

/**
 * Created by george on 17/5/2017.
 */
public class NSRecord {
    public Integer index;
    public String name;

    /* NSRecord's class constructor */
    public NSRecord(Integer index, String name) {
        this.index = index;
        this.name = name;
    }

    /* NSRecord's class setters and getters */
    public void setIndex(Integer index) { this.index = index; }
    public void setName(String name) { this.name = name; }
    public Integer getIndex() { return this.index; }
    public String getName() { return this.name; }
}
