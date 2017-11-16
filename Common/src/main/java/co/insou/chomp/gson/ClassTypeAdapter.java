package co.insou.chomp.gson;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import co.insou.chomp.bean.Beans;
import co.insou.chomp.bean.DynamicBean;
import co.insou.chomp.util.except.Try;

class ClassTypeAdapter extends TypeAdapter<Class<?>> {

	@Override
	public void write(JsonWriter jsonWriter, Class<?> type) throws IOException
	{
		if (type == null)
		{
			jsonWriter.nullValue();
			return;
		}

		if (type.isAnnotationPresent(DynamicBean.class))
		{
			jsonWriter.value(type.getAnnotation(DynamicBean.class).value().getName());
		}
		else
		{
			jsonWriter.value(type.getName());
		}
	}

	@Override
	public Class<?> read(JsonReader jsonReader) throws IOException
	{
		if (jsonReader.peek() == JsonToken.NULL)
		{
			jsonReader.nextNull();
			return null;
		}

		Class<?> type = this.fromName(jsonReader.nextString());

		if (type.isInterface())
		{
			Class<?> implementedBean = Beans.attemptToBuild(type);

			if (implementedBean != null)
			{
				return implementedBean;
			}
		}

		return type;
	}

	private Class<?> fromName(String typeName)
	{
		return Try.to(() -> Class.forName(typeName));
	}

}

