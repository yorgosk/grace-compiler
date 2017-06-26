package compiler;

import java.util.HashMap;

public class ActivationRecord {
    private Integer returnAddress;
    private Integer previousAR;
    private Integer accessLink;
    private Integer group;      // to face nesting issue, functions (/AR's) of the same group have access to the previous' groupIDs variables
    private Integer groupID;
    private Integer globalID;
    private Integer nestingDepth;
    private Integer numberOfFunctionParameters;
    private HashMap<String, String> functionParameters;
    private Integer numberOfFunctionResults;
    private HashMap<String, String> functionResults;
    private Integer numberOfLocalVariables;
    private HashMap<String, String> localVariables;
    private Integer numberOfTemporaryVariables;
    private HashMap<String, String> temporaryVariables;

    /* ActivationRecord's class (default-)constructor */
    public ActivationRecord() {
        this.returnAddress = null;
        this.previousAR = null;
        this.accessLink = null;
        this.group = -1;
        this.groupID = -1;
        this.globalID = -1;
        this.nestingDepth = -1;
        this.numberOfFunctionParameters = 0;
        this.functionParameters = new HashMap<String, String>();
        this.numberOfFunctionResults = 0;
        this.functionResults = new HashMap<String, String>();
        this.numberOfLocalVariables = 0;
        this.localVariables = new HashMap<String, String>();
        this.numberOfTemporaryVariables = 0;
        this.temporaryVariables = new HashMap<String, String>();
    }
    /* ActivationRecord's class constructor (I) */
    public ActivationRecord(Integer group, Integer groupID, Integer globalID) {
        this.returnAddress = null;
        this.previousAR = null;
        this.accessLink = null;
        this.group = group;
        this.groupID = groupID;
        this.globalID = globalID;
        this.nestingDepth = -1;
        this.numberOfFunctionParameters = 0;
        this.functionParameters = new HashMap<String, String>();
        this.numberOfFunctionResults = 0;
        this.functionResults = new HashMap<String, String>();
        this.numberOfLocalVariables = 0;
        this.localVariables = new HashMap<String, String>();
        this.numberOfTemporaryVariables = 0;
        this.temporaryVariables = new HashMap<String, String>();
    }
    /* ActivationRecord's class constructor (II) */
    public ActivationRecord(Integer returnAddress, Integer previousAR, Integer group, Integer groupID, Integer globalID, Integer nestingDepth) {
        this.returnAddress = returnAddress;
        this.previousAR = previousAR;
        this.accessLink = null;
        this.group = group;
        this.groupID = groupID;
        this.globalID = globalID;
        this.nestingDepth = nestingDepth;
        this.numberOfFunctionParameters = 0;
        this.functionParameters = new HashMap<String, String>();
        this.numberOfFunctionResults = 0;
        this.functionResults = new HashMap<String, String>();
        this.numberOfLocalVariables = 0;
        this.localVariables = new HashMap<String, String>();
        this.numberOfTemporaryVariables = 0;
        this.temporaryVariables = new HashMap<String, String>();
    }

    /* ActivationRecord's class setters and getters */
    public void setReturnAddress(Integer returnAddress) { this.returnAddress = returnAddress; }
    public Integer getReturnAddress() { return this.returnAddress; }
    public void setPreviousAR(Integer previousAR) { this.previousAR = previousAR; }
    public Integer getPreviousAR() { return this.previousAR; }
    public void setAccessLink(Integer accessLink) { this.accessLink = accessLink; }
    public Integer getAccessLink() { return this.accessLink; }
    public void setGroup(Integer group) { this.group = group; }
    public Integer getGroup() { return this.group; }
    public void setGroupID(Integer groupID) { this.groupID = groupID; }
    public Integer getGroupID() { return this.groupID; }
    public void setGlobalID(Integer globalID) { this.globalID = globalID; }
    public Integer getGlobalID() { return this.globalID; }
    public void setNestingDepth(Integer nestingDepth) { this.nestingDepth = nestingDepth; }
    public Integer getNestingDepth() { return this.nestingDepth; }
    public void setNumberOfFunctionParameters(Integer numberOfFunctionParameters) { this.numberOfFunctionParameters = numberOfFunctionParameters; }
    public Integer getNumberOfFunctionParameters() { return this.numberOfFunctionParameters; }
    public void addFunctionParameter(String name, String value) {
        this.functionParameters.put(name, value);
        this.numberOfFunctionParameters++;
    }
    public String getFunctionParameter(String name) { return this.functionParameters.get(name); }
    public void setNumberOfFunctionResults(Integer numberOfFunctionResults) { this.numberOfFunctionResults = numberOfFunctionResults; }
    public Integer getNumberOfFunctionResults() { return this.numberOfFunctionResults; }
    public void addFunctionResult(String name, String value) {
        this.functionResults.put(name, value);
        this.numberOfFunctionResults++;
    }
    public String getFunctionResult(String name) { return this.functionResults.get(name); }
    public void setNumberOfLocalVariables(Integer numberOfLocalVariables) { this.numberOfLocalVariables = numberOfLocalVariables; }
    public Integer getNumberOfLocalVariables() { return this.numberOfLocalVariables; }
    public void addLocalVariable(String name, String value) {
        this.localVariables.put(name, value);
        this.numberOfLocalVariables++;
    }
    public String getLocalVariable(String name) { return this.localVariables.get(name); }
    public void setNumberOfTemporaryVariables(Integer numberOfTemporaryVariables) { this.numberOfTemporaryVariables = numberOfTemporaryVariables; }
    public Integer getNumberOfTemporaryVariables() { return this.numberOfTemporaryVariables; }
    public void addTemporaryVariable(String name, String value) {
        this.temporaryVariables.put(name, value);
        this.numberOfTemporaryVariables++;
    }
    public String getTemporaryVariable(String name) { return this.temporaryVariables.get(name); }

    /* ActivationRecord's class printing function -- for debugging */
    public void printAR() {
        System.out.printf("%d %d %d %d %d %d %d\n",
                this.returnAddress, this.previousAR, this.accessLink, this.group, this.groupID, this.globalID, this.nestingDepth);
        System.out.printf("%d\n", this.numberOfFunctionParameters);
        System.out.print(this.functionParameters);
        System.out.printf("%d\n", this.numberOfFunctionResults);
        System.out.print(this.functionResults);
        System.out.printf("%d\n", this.numberOfLocalVariables);
        System.out.print(this.localVariables);
        System.out.printf("%d\n", this.numberOfTemporaryVariables);
        System.out.print(this.temporaryVariables);
    }

}
