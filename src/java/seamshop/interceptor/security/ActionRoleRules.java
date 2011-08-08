package seamshop.interceptor.security;

import static java.util.Arrays.asList;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;

import seamshop.exception.BlankArrayArgumentException;
import seamshop.exception.BlankStringArgumentException;
import seamshop.model.enums.UserRole;
import seamshop.util.Log;

/**
 * Contains concurrent maps of rules of action roles.
 *
 * @author Alex Siman 2009-11-02
 */
/*
 * TODO: Low: Impl: method "remove*Roles()"? (mb)
 *
 * TODO: Low: Rename to: "ObjectAccessRules", "ResourceAccessRules", etc. such as
 *       this class can be applied not only for Action classes?
 */
public class ActionRoleRules
{
	protected final Log log = new Log(this.getClass());

	private Map<String, Set<UserRole>> packageRules =
		new ConcurrentHashMap<String, Set<UserRole>>();

	private Map<String, Set<UserRole>> classRules =
		new ConcurrentHashMap<String, Set<UserRole>>();

	/**
	 * Also serves as cache. All rules found for any action method through
	 * package or class rules will be added to method rules.
	 */
	private Map<String, Set<UserRole>> methodRules =
		new ConcurrentHashMap<String, Set<UserRole>>();

	// Add rules. -------------------------------------------------------------

	public void addPackageRoles(String packagePath, UserRole... userRoles)
	{
		assertValidPathAndRoles("packagePath", packagePath, userRoles);
		addRoles(packageRules, packagePath, userRoles);
	}

	public void addClassRoles(String classPath, UserRole... userRoles)
	{
		assertValidPathAndRoles("classPath", classPath, userRoles);
		addRoles(classRules, classPath, userRoles);
	}

	public void addMethodRoles(String methodPath, UserRole... userRoles)
	{
		assertValidPathAndRoles("methodPath", methodPath, userRoles);
		addRoles(methodRules, methodPath, userRoles);

		log.debug("Added roles " + Arrays.toString(userRoles) +
			" to method [" + methodPath + "].");
	}

	// Get roles. -------------------------------------------------------------

	public UserRole[] getPackageRoles(String packagePath)
	{
		assertValidPath("packagePath", packagePath);
		return getRoles(packageRules, packagePath);
	}

	/**
	 * If there is no roles for specified package, then try to find roles for
	 * it's parent package and so on. This is <em>recursively</em> method.
	 */
	public UserRole[] findPackageRolesRecursively(String packagePath)
	{
		if (packagePath == null)
		{
			throw new NullArgumentException("packagePath");
		}

		Collection<UserRole> roles = packageRules.get(packagePath);
		if (roles == null)
		{
			int lastDot = packagePath.lastIndexOf(".");
			if (lastDot > 1)
			{
				String parentPackagePath = packagePath.substring(0, lastDot);
				return findPackageRolesRecursively(parentPackagePath);
			}
			else
			{
				return new UserRole[0];
			}
		}
		else
		{
			return roles.toArray(new UserRole[0]);
		}
	}

	public UserRole[] getClassRoles(String classPath)
	{
		assertValidPath("classPath", classPath);
		return getRoles(classRules, classPath);
	}

	public UserRole[] getMethodRoles(String methodPath)
	{
		assertValidPath("methodPath", methodPath);
		return getRoles(methodRules, methodPath);
	}

	// Has roles? -------------------------------------------------------------

	public boolean hasPackageRoles(String packagePath)
	{
		assertValidPath("packagePath", packagePath);
		return hasRoles(packageRules, packagePath);
	}

	public boolean hasClassRoles(String classPath)
	{
		assertValidPath("classPath", classPath);
		return hasRoles(classRules, classPath);
	}

	public boolean hasMethodRoles(String methodPath)
	{
		assertValidPath("methodPath", methodPath);
		return hasRoles(methodRules, methodPath);
	}

	// Sub-methods. -----------------------------------------------------------

	private void addRoles(Map<String, Set<UserRole>> pathAndRoles,
		String rulePath, UserRole... userRoles)
	{
		Set<UserRole> roles = pathAndRoles.get(rulePath);
		if (roles == null)
		{
			roles = new HashSet<UserRole>();
			pathAndRoles.put(rulePath, roles);
		}
		roles.addAll(asList(userRoles));
	}

	private UserRole[] getRoles(Map<String, Set<UserRole>> rules, String rulePath)
	{
		Set<UserRole> roles = rules.get(rulePath);
		if (roles == null)
		{
			roles = new HashSet<UserRole>();
		}

		return roles.toArray(new UserRole[0]);
	}

	private boolean hasRoles(Map<String, Set<UserRole>> rules, String rulePath)
	{
		return null != rules.get(rulePath);
	}

	private void assertValidPathAndRoles(String pathType, String path,
		UserRole... userRoles)
	{
		if (StringUtils.isBlank(path))
		{
			throw new BlankStringArgumentException(pathType);
		}

		if (ArrayUtils.isEmpty(userRoles))
		{
			throw new BlankArrayArgumentException("userRoles");
		}
	}

	private void assertValidPath(String pathType, String path)
	{
		if (StringUtils.isBlank(path))
		{
			throw new BlankStringArgumentException(pathType);
		}
	}
}
