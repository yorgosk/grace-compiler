package compiler;

public class NSRecord {
    private Integer index;
    private STRecord.Type type; // we also keep the type of the function that defines the namespace

    /* NSRecord's class (default-)constructor */
    public NSRecord() {
        this.type = null;
    }

    /* NSRecord's class copy-constructor */
    public NSRecord(NSRecord temp) {
        this.index = temp.index;
        this.type = null;
    }

    /* NSRecord's class setters and getters */
    public void setIndex(Integer index) { this.index = index; }
    public void setType(STRecord.Type type) { this.type = new STRecord.Type(type); }
    public Integer getIndex() { return this.index; }
    public STRecord.Type getType() { return this.type; }

    /* NSRecord's class various functions */
    public void incrIndex() { this.index++; }
    public void decrIndex() { this.index--; }

    /* NSRecord's class printing function */
    public void printNSRecord() {
        //System.out.printf("NSR -> index: %d\n", this.index);
    }

}
