package seamshop.util;

import java.lang.annotation.Annotation;

/**
 * @author Alex Siman 2009-10-28
 */
public class AnnotationUtils
{
	private AnnotationUtils()
	{}

	// TODO: Low: Performance: Add method lie:
	//       Collection<Class<A>> whatAnnosClassHas(Collection<Class<A>> annos)
	//       which returns only those annos that class has.

	/**
	 * Look through class hierarchy.
	 */
	public static <A extends Annotation> A findClassAnnotation(Class<?> clazz,
		Class<A> annotationClass)
	{
		return findClassAnnotation(clazz, annotationClass, true);
	}

	// TODO: Test.
	public static <A extends Annotation> A findClassAnnotation(Class<?> clazz,
		Class<A> annotationClass, boolean lookThroughClassHierarchy)
	{
		A annotationResult = null;
		if (annotationClass == null)
		{
			return annotationResult;
		}

		annotationResult = clazz.getAnnotation(annotationClass);

		// If class has no specified anno, then try to find,
		// whether one of it's superclasses has that anno.
		if ((annotationResult == null) && lookThroughClassHierarchy)
		{
			Class<?> superClass = clazz.getSuperclass();
			while (superClass != null)
			{
				annotationResult = superClass.getAnnotation(annotationClass);
				if (annotationResult != null)
				{
					return annotationResult;
				}
				superClass = superClass.getSuperclass();
			}
		}

		return annotationResult;
	}
}
