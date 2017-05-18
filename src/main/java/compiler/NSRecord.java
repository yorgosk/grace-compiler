package compiler;

public class NSRecord {
    private Integer index;

    /* NSRecord's class (default-)constructor */
    public NSRecord() {}

    /* NSRecord's class copy-constructor */
    public NSRecord(NSRecord temp) {
        this.index = temp.index;
    }

    /* NSRecord's class setters and getters */
    public void setIndex(Integer index) { this.index = index; }
    public Integer getIndex() { return this.index; }

    /* NSRecord's class various functions */
    public void incrIndex() { this.index++; }
    public void decrIndex() { this.index--; }

    /* NSRecord's class printing function */
    public void printNSRecord() {
        System.out.printf("NSR -> index: %d\n", this.index);
    }

}
