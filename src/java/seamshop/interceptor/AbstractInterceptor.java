package seamshop.interceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import seamshop.action.AbstractAction;
import seamshop.context.SessionContext;
import seamshop.context.TransactionContext;
import seamshop.dao.SessionDao;
import seamshop.dao.UserDao;
import seamshop.util.Log;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.util.AnnotationUtils;

/**
 * Notes from XWork 2.1.6 wiki:
 * <p/>
 * <cite>
 * XWork2 interceptors are created once and being repeatably used unless XWork2
 * is configured to be reloadable. It is preferable that XWork interceptors
 * being configured with singleton="false" as well.
 * </cite>
 *
 * @author Alex Siman 07.10.2009
 */
@SuppressWarnings("serial")
public abstract class AbstractInterceptor extends
	com.opensymphony.xwork2.interceptor.AbstractInterceptor
{
	protected final Log log = new Log(this.getClass());

	@PersistenceContext
	protected EntityManager entityManager;

	@Autowired
	protected SessionDao sessionDao;

	@Autowired
	protected UserDao userDao;

	@Autowired
	protected SessionContext sessionContext;

	@Autowired
	protected TransactionContext transactionContext;

	// TODO: Low: Delete block as unneeded? (y)
	/*@Override
	public String intercept(ActionInvocation actionInvocation) throws Exception
	{
		return actionInvocation.invoke();
	}*/

	protected boolean isUserLoggedIn()
	{
		return getAbstractAction().isLoggedIn();
	}

	protected ActionInvocation getActionInvocation()
	{
		return ActionContext.getContext().getActionInvocation();
	}

	protected AbstractAction getAbstractAction()
	{
		return (AbstractAction) getActionInvocation().getAction();
	}

	// TODO: Refactor: Duplication: This method has been copied from
	//       [com.opensymphony.xwork2.DefaultActionInvocation]
	//       but it should be exposed through the XWork API (somehow...) or etc.
	protected Method getActionMethod() throws NoSuchMethodException
	{
		Class<?> actionClass = getActionClass();
		String methodName = getActionInvocation().getProxy().getMethod();
		Method result = null;

		try
		{
			result = actionClass.getMethod(methodName, new Class[0]);
		}
		catch (NoSuchMethodException ex)
		{
			// hmm -- OK, try doXxx instead
			try
			{
				String altMethodName = "do" + methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
				result = actionClass.getMethod(altMethodName, new Class[0]);
			}
			catch (NoSuchMethodException e1)
			{
				// Throw the original one.
				throw ex;
			}
		}

		return result;
	}

	protected String getActionMethodName()
	{
		String methodName = null;
		try
		{
			methodName = getActionMethod().getName();
		}
		catch (NoSuchMethodException e)
		{}

		if (StringUtils.isBlank(methodName))
		{
			methodName = "execute";
		}

		return methodName;
	}

	protected Class<? extends Object> getActionClass()
	{
		return getActionInvocation().getAction().getClass();
	}

	protected <A extends Annotation> A findActionClassAnnotation(
		Class<A> annotationClass)
	{
		return findActionClassAnnotation(annotationClass, true);
	}

	protected <A extends Annotation> A findActionClassAnnotation(
		Class<A> annotationClass, boolean lookThroughClassHierarchy)
	{
		return seamshop.util.AnnotationUtils.findClassAnnotation(
			getActionClass(), annotationClass, lookThroughClassHierarchy);
	}

	protected <A extends Annotation> boolean hasActionClassAnnotation(
		Class<A> annotationClass)
	{
		return hasActionClassAnnotation(annotationClass, true);
	}

	protected <A extends Annotation> boolean hasActionClassAnnotation(
		Class<A> annotationClass, boolean lookThroughClassHierarchy)
	{
		return null != findActionClassAnnotation(annotationClass, lookThroughClassHierarchy);
	}

	/**
	 * Try to find method annotation through class hierarchy.
	 */
	// TODO: Low: Move method to ClassUtils or AnnotationUtils? (mb)
	protected <A extends Annotation> A findMethodAnnotation(
		Class<A> annotationClass) throws NoSuchMethodException
	{
		return findMethodAnnotation(annotationClass, true);
	}

	// TODO: Test.
	// TODO: Low: Move method to ClassUtils or AnnotationUtils? (mb)
	@SuppressWarnings("unchecked")
	protected <A extends Annotation> A findMethodAnnotation(
		Class<A> annotationClass, boolean lookThroughClassHierarchy)
		throws NoSuchMethodException
	{
		A annotationResult = null;
		if (annotationClass == null)
		{
			return annotationResult;
		}

		Object action = getActionInvocation().getAction();
		if (action != null)
		{
			Method method = getActionMethod();
			Collection<Method> annotatedMethods = AnnotationUtils
				.getAnnotatedMethods(action.getClass(), annotationClass);
			if ((annotatedMethods != null) && annotatedMethods.contains(method))
			{
				annotationResult = method.getAnnotation(annotationClass);
			}

			// If method has no specified anno in current class, then
			// try to find, whether this method has that anno in superclasses.
			if ((annotationResult == null) && lookThroughClassHierarchy)
			{
				Class<?> clazz = action.getClass().getSuperclass();
				while (clazz != null)
				{
					annotatedMethods = AnnotationUtils.getAnnotatedMethods(
						clazz, annotationClass);
					if (annotatedMethods != null)
					{
						for (Method annotatedMethod : annotatedMethods)
						{
							if (annotatedMethod.getName().equals(method.getName())
								&& Arrays.equals(
									annotatedMethod.getParameterTypes(),
									method.getParameterTypes())
								&& Arrays.equals(
									annotatedMethod.getExceptionTypes(),
									method.getExceptionTypes()))
							{
								annotationResult = annotatedMethod.getAnnotation(annotationClass);
								if (annotationResult != null)
								{
									return annotationResult;
								}
							}
						}
					}
					clazz = clazz.getSuperclass();
				}
			}
		}

		return annotationResult;
	}

	/**
	 * @return <code>true</code> if method has specified annotation through
	 *     class hierarchy.
	 */
	// TODO: Low: Move method to ClassUtils or AnnotationUtils? (mb)
	protected <A extends Annotation> boolean hasMethodAnnotation(
		Class<A> annotationClass) throws NoSuchMethodException
	{
		return hasMethodAnnotation(annotationClass, true);
	}

	// TODO: Low: Move method to ClassUtils or AnnotationUtils? (mb)
	protected <A extends Annotation> boolean hasMethodAnnotation(
		Class<A> annotationClass, boolean lookThroughClassHierarchy)
		throws NoSuchMethodException
	{
		return null != findMethodAnnotation(
			annotationClass, lookThroughClassHierarchy);
	}
}
