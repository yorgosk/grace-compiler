package compiler;

public class NSRecord {
    private Integer index;
    private String name;

    /* NSRecord's class (default-)constructor */
    public NSRecord() {}

    /* NSRecord's class copy-constructor */
    public NSRecord(NSRecord temp) {
        this.index = temp.index;
        this.name = temp.name;
    }

    /* NSRecord's class setters and getters */
    public void setIndex(Integer index) { this.index = index; }
    public void setName(String name) { this.name = name; }
    public Integer getIndex() { return this.index; }
    public String getName() { return this.name; }

    /* NSRecord's class various functions */
    public void incrIndex() { this.index++; }
    public void decrIndex() { this.index--; }

    /* NSRecord's class printing function */
    public void printNSRecord() {
        System.out.printf("NSR -> index: %d - name: %d\n", this.index, this.name);
    }

}
