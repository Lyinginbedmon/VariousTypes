package com.lying.template;

/** A configurable modification applied to a creature, on top of its species */
public class Template
{
	private final Identifier registryName;
	
	private final List<Precondition> preconditions = Lists.newArrayList();
	private final List<Operation> operations = Lists.newArrayList();
	
	public Template(Identifier nameIn)
	{
		registryName = nameIn;
	}
	
	public Identifier registryName() { return registryName; }
	
	public List<Precondition> preconditions() { return preconditions; }
	
	public List<Operation> operations() { return operations; }
	
	public static class Precondition
	{
		
	}
	
	public static class Operation
	{
		
	}
}