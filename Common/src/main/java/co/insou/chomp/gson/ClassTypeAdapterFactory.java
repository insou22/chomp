package co.insou.chomp.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

import co.insou.chomp.bean.DynamicBean;

class ClassTypeAdapterFactory implements TypeAdapterFactory {

	@Override
	@SuppressWarnings("unchecked")
	public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken)
	{
		if (!this.isBean(typeToken.getRawType()) && !Class.class.isAssignableFrom(typeToken.getRawType()))
		{
			return null;
		}

		return (TypeAdapter<T>) new ClassTypeAdapter();
	}

	private boolean isBean(Class<?> type)
	{
		return type.isInterface() || type.isAnnotationPresent(DynamicBean.class);
	}

}