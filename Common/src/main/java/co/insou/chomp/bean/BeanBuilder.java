package co.insou.chomp.bean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import co.insou.chomp.bean.FieldInfoExtractor.FieldInfo;
import co.insou.chomp.util.except.Try;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.method.ParameterDescription;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

final class BeanBuilder<T> {

	private final Set<FieldInfo> registeredFields = new HashSet<>();
	private final Class<T> interfaceType;
	private DynamicType.Builder<Bean> builder;

	BeanBuilder(Class<T> interfaceType)
	{
		this.interfaceType = interfaceType;
		this.builder = this.createBuilder();
	}

	Class<? extends T> build()
	{
		this.make();

		@SuppressWarnings("unchecked")
		Class<? extends T> beanClass = (Class<? extends T>)
				this.builder.make().load(this.getClass().getClassLoader()).getLoaded();

		return beanClass;
	}

	private void make()
	{
		this.implementMethods();
		this.annotateDynamic();
		this.restoreAnnotationsFromParent();
	}

	private DynamicType.Builder<Bean> createBuilder()
	{
		return new ByteBuddy(ClassFileVersion.JAVA_V8)
				.subclass(Bean.class)
				.implement(this.interfaceType);
	}

	private void implementMethods()
	{
		this.listMethods(ElementMatchers.isGetter().or(ElementMatchers.isSetter()), this.interfaceType)
				.stream()
				.map(this::unwrapDescription)
				.forEach(this::implementMethod);
	}

	private void annotateDynamic()
	{
		this.builder = this.builder.annotateType(new DynamicBean() {
			@Override
			public Class<? extends Annotation> annotationType()
			{
				return DynamicBean.class;
			}

			@Override
			public Class<?> value()
			{
				return BeanBuilder.this.interfaceType;
			}
		});
	}

	private List<MethodDescription.InDefinedShape> listMethods(ElementMatcher.Junction<? super MethodDescription.InDefinedShape> matcher, Class<?> type)
	{
		List<MethodDescription.InDefinedShape> methods = new ArrayList<>();

		if (ArrayUtils.isEmpty(type.getInterfaces()))
		{
			return new TypeDescription.ForLoadedType(type)
					.getDeclaredMethods()
					.filter(matcher);
		}

		for (Class<?> superInterface : type.getInterfaces())
		{
			methods.addAll(this.listMethods(matcher, superInterface));
		}

		methods.addAll(new TypeDescription.ForLoadedType(type)
				.getDeclaredMethods()
				.filter(matcher));

		return methods;
	}

	private Method unwrapDescription(MethodDescription description)
	{
		Class<?> type = Try.to(() -> ClassUtils.getClass(((TypeDescription.ForLoadedType) description.getDeclaringType()).getName()));

		Class<?>[] parameters = this.unwrapParameters(description);

		return Try.to(() -> MethodUtils.getMatchingMethod(type, description.getInternalName(), parameters));
	}

	private Class<?>[] unwrapParameters(MethodDescription description)
	{
		return description.getParameters()
				.stream()
				.map(this::unwrapParameter)
				.toArray(Class<?>[]::new);
	}

	private Class<?> unwrapParameter(ParameterDescription description)
	{
		return Try.to(() -> ClassUtils.getClass(description.getType().asRawType().getTypeName()));
	}

	private void implementMethod(Method method)
	{
		this.ensureFieldCreated(method);

		this.builder =
				this.builder.method(ElementMatchers.is(method))
						.intercept(FieldAccessor.ofBeanProperty())
						.annotateMethod(method.getDeclaredAnnotations());
	}

	private void ensureFieldCreated(Method method)
	{
		FieldInfo info = FieldInfoExtractor.from(method);

		if (!this.registeredFields.contains(info))
		{
			this.createField(info);
			this.registeredFields.add(info);
		}
	}

	private void createField(FieldInfo info)
	{
		this.builder = this.builder.defineField(info.getFieldName(), info.getFieldType(), Visibility.PRIVATE);
	}

	private void restoreAnnotationsFromParent()
	{
		this.builder = this.builder.annotateType(this.interfaceType.getDeclaredAnnotations());
	}

}
