package com.lying.event;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Result<T extends Object>
{
	@Nullable
	private final T resultValue;
	@NotNull
	private final boolean shouldInterrupt;
	
	public Result(T value, boolean interrupt)
	{
		resultValue = value;
		shouldInterrupt = interrupt;
	}
	
	public static <T extends Object> Result<T> interrupt(T value) { return new Result<>(value, true); }
	public static <T extends Object> Result<T> pass() { return new Result<>(null, false); }
	
	public boolean interruptsFurtherEvaluation() { return shouldInterrupt; }
	
	public T value() { return resultValue; }
	
	public boolean isEmpty() { return resultValue == null; }
}