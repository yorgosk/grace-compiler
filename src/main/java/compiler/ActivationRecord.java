package compiler;

import java.util.HashMap;

public class ActivationRecord {
    private Integer returnAddress;
    private Integer previousAR;
    private Integer group;      // to face nesting issue, functions (/AR's) of the same group have access to the previous' groupIDs variables
    private Integer groupID;
    private Integer globalID;
    private HashMap<String, String> functionParameters;
    private HashMap<String, String> functionResults;
    private HashMap<String, String> localVariables;

    /* ActivationRecord's class (default-)constructor */
    public ActivationRecord() {
        this.returnAddress = null;
        this.previousAR = null;
        this.group = -1;
        this.groupID = -1;
        this.globalID = -1;
        this.functionParameters = new HashMap<String, String>();
        this.functionResults = new HashMap<String, String>();
        this.localVariables = new HashMap<String, String>();
    }
    /* ActivationRecord's class constructor (I) */
    public ActivationRecord(Integer group, Integer groupID, Integer globalID) {
        this.returnAddress = null;
        this.previousAR = null;
        this.group = group;
        this.groupID = groupID;
        this.globalID = globalID;
        this.functionParameters = new HashMap<String, String>();
        this.functionResults = new HashMap<String, String>();
        this.localVariables = new HashMap<String, String>();
    }
    /* ActivationRecord's class constructor (II) */
    public ActivationRecord(Integer returnAddress, Integer previousAR, Integer group, Integer groupID, Integer globalID) {
        this.returnAddress = returnAddress;
        this.previousAR = previousAR;
        this.group = group;
        this.groupID = groupID;
        this.globalID = globalID;
        this.functionParameters = new HashMap<String, String>();
        this.functionResults = new HashMap<String, String>();
        this.localVariables = new HashMap<String, String>();
    }

    /* ActivationRecord's class setters and getters */
    public void setReturnAddress(Integer returnAddress) { this.returnAddress = returnAddress; }
    public Integer getReturnAddress() { return this.returnAddress; }
    public void setPreviousAR(Integer previousAR) { this.previousAR = previousAR; }
    public Integer getPreviousAR() { return this.previousAR; }
    public void setGroup(Integer group) { this.group = group; }
    public Integer getGroup() { return this.group; }
    public void setGroupID(Integer groupID) { this.groupID = groupID; }
    public Integer getGroupID() { return this.groupID; }
    public void setGlobalID(Integer globalID) { this.globalID = globalID; }
    public Integer getGlobalID() { return this.globalID; }
    public void addFunctionParameter(String name, String value) { this.functionParameters.put(name, value); }
    public String getFunctionParameter(String name) { return this.functionParameters.get(name); }
    public void addFunctionResult(String name, String value) { this.functionResults.put(name, value); }
    public String getFunctionResult(String name) { return this.functionResults.get(name); }
    public void addLocalVariable(String name, String value) { this.localVariables.put(name, value); }
    public String getLocalVariable(String name) { return this.localVariables.get(name); }

}
