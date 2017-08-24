package co.insou.chomp.bean;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.reflect.FieldUtils;

import com.google.gson.Gson;

import co.insou.chomp.gson.GsonProvider;
import co.insou.chomp.util.HandleUtils;
import co.insou.chomp.util.except.Try;

public abstract class Bean {

	private static final Gson GSON = GsonProvider.getGson();
	private static final Map<Class<? extends Bean>, List<MethodHandle>> FIELDS = new HashMap<>();

	@Override
	public final boolean equals(Object that)
	{
		if (this == that)
		{
			return true;
		}

		if (that == null || this.getClass() != that.getClass())
		{
			return false;
		}

		return this.getBeanFields()
				.stream()
				.allMatch(handle -> this.valuesEqual(handle, that));
	}

	private boolean valuesEqual(MethodHandle handle, Object that)
	{
		Object thisValue = Try.to(() -> handle.invokeExact((Object) this));
		Object thatValue = Try.to(() -> handle.invokeExact(that));

		return Objects.deepEquals(thisValue, thatValue);
	}

	@Override
	public final int hashCode()
	{
		HashCodeBuilder hashCode = new HashCodeBuilder();
		for (MethodHandle handle : this.getBeanFields())
		{
			Object value = Try.to(() -> handle.invokeExact((Object) this));
			hashCode.append(value);
		}
		return hashCode.toHashCode();
	}

	@Override
	public final String toString()
	{
		return Bean.GSON.toJson(this);
	}

	private List<MethodHandle> getBeanFields()
	{
		@SuppressWarnings("unchecked")
		Class<Bean> thiz = (Class<Bean>) this.getClass();
		return Bean.FIELDS.computeIfAbsent(thiz, key ->
				FieldUtils.getAllFieldsList(key)
						.stream()
						.filter(this::isBeanField)
						.peek(field -> field.setAccessible(true))
						.map(HandleUtils::createGenericGetter)
						.collect(Collectors.toList()));
	}

	private boolean isBeanField(Field field)
	{
		int modifiers = field.getModifiers();
		return !Modifier.isTransient(modifiers) && !Modifier.isStatic(modifiers);
	}

}