package compiler;

import java.util.ArrayList;
import java.util.HashMap;

public class SymbolTable {
    private HashMap namespace;
    private HashMap variable;
    // Our actual Symbol Table is going to be a Java ArrayList 
    private ArrayList< ArrayList<String> > symbolTable;
	public void init(){
		symbolTable = new ArrayList< ArrayList<String> >();
	}
	public void enter(){
		ArrayList<String> insideArray = new ArrayList<String>();
		//apo oti katalava den uparxei kindunos i lista na einai gemati, opote den xreiazetai antistoixos elegxos
		if (symbolTable != null){
			symbolTable.add(insideArray);
		} else {
			System.out.println("ListArray hasnt been defined");
		}
	}
	public void insert(String name){
		ArrayList< String > insideArray = new ArrayList<String>();
		if (symbolTable != null && !symbolTable.isEmpty()) {
			insideArray = symbolTable.get(symbolTable.size()-1);
		}
		if (insideArray != null){
			insideArray.add(name);
		} else {
			System.out.println("ListArray hasnt been defined");
		}
	}
	public void lookup(String name){
		ArrayList< String > insideArray = new ArrayList<String>();
		if (symbolTable != null && !symbolTable.isEmpty()) {
			insideArray = symbolTable.get(symbolTable.size()-1);
		} else {
			System.out.println("Trying to access empty list");
		}
		if (insideArray != null){
			if (insideArray.contains(name)) {
				System.out.println("Variable allready exists");
			} else {
				System.out.println("Variable first time is declared");
			}
		} else {
			System.out.println("Trying to access empty list");
		}
	}
	public void exit(){
		if (symbolTable != null && !symbolTable.isEmpty()) {
			symbolTable.remove(symbolTable.size()-1);
		}
		else {
			System.out.println("Trying to pop from empty list");
		}
	}

}
