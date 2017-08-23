package co.insou.chomp.util;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

import co.insou.chomp.util.except.Try;

public enum HandleUtils {

	;

	public static MethodHandle createGenericGetter(Field field)
	{
		MethodHandle getter = HandleUtils.createGetter(field);

		return HandleUtils.convertToGenerics(getter);
	}

	public static MethodHandle createGetter(Field field)
	{
		Objects.requireNonNull(field);

		return Try.to(() -> MethodHandles.lookup().unreflectGetter(field));
	}

	public static MethodHandle createGenericSetter(Field field)
	{
		MethodHandle setter = HandleUtils.createSetter(field);

		return HandleUtils.convertToGenerics(setter);
	}

	public static MethodHandle createSetter(Field field)
	{
		Objects.requireNonNull(field);

		return Try.to(() -> MethodHandles.lookup().unreflectSetter(field));
	}

	public static MethodHandle getGenericMethod(Method method)
	{
		MethodHandle handle = HandleUtils.getMethod(method);

		return HandleUtils.convertToGenerics(handle);
	}

	public static MethodHandle getMethod(Method method)
	{
		Objects.requireNonNull(method);

		return Try.to(() -> MethodHandles.lookup().unreflect(method));
	}

	private static MethodHandle convertToGenerics(MethodHandle handle)
	{
		MethodType original = handle.type();
		MethodType generic = original.generic();

		if (original.returnType() == void.class)
		{
			generic = generic.changeReturnType(void.class);
		}

		return handle.asType(generic);
	}

}