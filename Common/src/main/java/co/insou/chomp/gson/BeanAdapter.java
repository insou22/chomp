package co.insou.chomp.gson;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.TypeAdapter;
import com.google.gson.internal.Primitives;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import co.insou.chomp.bean.Beans;
import co.insou.chomp.bean.DynamicBean;
import co.insou.chomp.util.except.Try;

class BeanAdapter extends TypeAdapter<Object> {

	private final Class<?> rawType;

	BeanAdapter(TypeToken<?> token)
	{
		if (token.getRawType().isInterface())
		{
			this.rawType = Beans.build(token.getRawType());
		}
		else
		{
			this.rawType = token.getRawType();
		}
	}

	@Override
	public void write(JsonWriter jsonWriter, Object bean) throws IOException
	{
		jsonWriter.beginObject();

		for (Field field : bean.getClass().getDeclaredFields())
		{
			jsonWriter.name(field.getName());

			field.setAccessible(true);
			Object fieldValue = Try.to(() -> field.get(bean));

			if (this.isSimple(field))
			{
				if (this.isNumber(field))
				{
					jsonWriter.value((Number) fieldValue);
					continue;
				}
				else if (this.isCharSeq(field))
				{
					jsonWriter.value((String) fieldValue);
					continue;
				}
				else if (this.isChar(field))
				{
					jsonWriter.value((Character) fieldValue);
					continue;
				}
				else if (this.isBoolean(field))
				{
					jsonWriter.value((Boolean) fieldValue);
					continue;
				}
			}

			jsonWriter.value(GsonProvider.getGson().toJson(fieldValue));
		}

		jsonWriter
				.endObject()
				.close();
	}

	@Override
	public Object read(JsonReader jsonReader) throws IOException
	{
		if (jsonReader.peek() == JsonToken.NULL)
		{
			jsonReader.nextNull();
			return null;
		}

		Map<String, Object> fields = new HashMap<>();

		jsonReader.beginObject();

		while (jsonReader.hasNext())
		{
			String fieldName = jsonReader.nextName();
			JsonToken token = jsonReader.peek();

			if (token == JsonToken.BEGIN_OBJECT)
			{
				fields.put(fieldName, this.readObject(jsonReader, fieldName));
			}
			else
			{
				fields.put(fieldName, this.associatedValue(token, fieldName, jsonReader));
			}
		}

		Object bean = Beans.create(this.rawType);
		fields.forEach((fieldName, instance) ->
		{
			Field field = this.locateLambSauce(fieldName, bean.getClass());
			field.setAccessible(true);
			Try.to(() -> field.set(bean, instance));
		});

		jsonReader.endObject();

		return bean;
	}

	private Object readObject(JsonReader reader, String fieldName) throws IOException
	{
		reader.beginObject();

		Class<?> nonPrimitive = Primitives.wrap(this.locateLambSauce(fieldName).getType());

		Object instance;

		if (nonPrimitive.isInterface() || nonPrimitive.isAnnotationPresent(DynamicBean.class))
		{
			instance = Beans.create(nonPrimitive);
		}
		else
		{
			instance = Try.to(() ->
			{
				Constructor<?> constructor = nonPrimitive.getDeclaredConstructor();
				constructor.setAccessible(true);
				return constructor.newInstance();
			});
		}

		while (reader.hasNext())
		{
			String name = reader.nextName();
			Field field = this.locateLambSauce(name, instance.getClass());
			field.setAccessible(true);

			JsonToken token = reader.peek();

			Object value;

			if (token == JsonToken.BEGIN_OBJECT)
			{
				value = this.readObject(reader, name);
			}
			else
			{
				value = this.associatedValue(token, instance.getClass(), name, reader);
			}

			Try.to(() -> field.set(instance, value));
		}

		reader.endObject();

		return instance;
	}

	private Object associatedValue(JsonToken token, String fieldName, JsonReader reader) throws IOException
	{
		return this.associatedValue(token, this.rawType, fieldName, reader);
	}

	private Object associatedValue(JsonToken token, Class<?> holder, String fieldName, JsonReader reader) throws IOException
	{
		Class<?> nonPrimitive = Primitives.wrap(this.locateLambSauce(fieldName, holder).getType());

		switch (token)
		{
			case STRING:
				if (CharSequence.class.isAssignableFrom(nonPrimitive))
				{
					return reader.nextString();
				}
				return GsonProvider.getGson().fromJson(
						reader.nextString(),
						this.locateLambSauce(fieldName, holder).getType()
				);
			case BOOLEAN:
				return reader.nextBoolean();
			case NUMBER:
				return Integer.class.isAssignableFrom(nonPrimitive) || Short.class.isAssignableFrom(nonPrimitive) ||
						Byte.class.isAssignableFrom(nonPrimitive) || Character.class.isAssignableFrom(nonPrimitive) ?
						this.enforceType(reader.nextInt()) :
						Double.class.isAssignableFrom(nonPrimitive) || Float.class.isAssignableFrom(nonPrimitive) ?
								this.enforceType(reader.nextDouble()) :
								this.enforceType(reader.nextLong());
			case NULL:
				return null;

			default:
				return null;
		}
	}

	private Field locateLambSauce(String fieldName)
	{
		return this.locateLambSauce(fieldName, this.rawType);
	}

	// lookup field by name and class
	private Field locateLambSauce(String fieldName, Class<?> holder)
	{
		return Try.to(() -> holder.getDeclaredField(fieldName));
	}

	private boolean isSimple(Field field)
	{
		Class<?> nonPrimitive = Primitives.wrap(field.getType());

		return Number.class.isAssignableFrom(nonPrimitive) ||
				Character.class.isAssignableFrom(nonPrimitive) ||
				Boolean.class.isAssignableFrom(nonPrimitive) ||
				CharSequence.class.isAssignableFrom(nonPrimitive);
	}

	private boolean isNumber(Field field)
	{
		return Number.class.isAssignableFrom(Primitives.wrap(field.getType()));
	}

	private boolean isBoolean(Field field)
	{
		return Boolean.class.isAssignableFrom(Primitives.wrap(field.getType()));
	}

	private boolean isCharSeq(Field field)
	{
		return CharSequence.class.isAssignableFrom(field.getType());
	}

	private boolean isChar(Field field)
	{
		return Character.class.isAssignableFrom(Primitives.wrap(field.getType()));
	}

	private <T> T enforceType(T value)
	{
		return value;
	}

}
