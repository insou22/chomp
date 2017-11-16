package co.insou.chomp.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

import co.insou.chomp.bean.DynamicBean;

class BeanFactory implements TypeAdapterFactory {

	@Override
	@SuppressWarnings("unchecked")
	public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken)
	{
		if (!typeToken.getRawType().isInterface() && !typeToken.getRawType().isAnnotationPresent(DynamicBean.class))
		{
			return null;
		}

		return (TypeAdapter<T>) new BeanAdapter(typeToken);
	}

}