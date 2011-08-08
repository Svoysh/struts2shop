package seamshop.messages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;

import seamshop.dto.NamedCode;
import seamshop.util.ClassUtils;
import seamshop.util.Log;
import seamshop.util.SpringUtils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedMap;

/**
 * @author Alex Siman 2009-10-15
 */
public abstract class ImmutableNamedCodeCollection<NAMED_CODE extends NamedCode>
{
	protected static <T> T getInstance(Class<T> clazz)
	{
		String springBeanId = ClassUtils.getInstanceClassName(clazz);
		return SpringUtils.getBean(springBeanId);
	}

	protected final Log log = new Log(this.getClass());

	private final Class<NAMED_CODE> namedCodeClass;

	/**
	 * Immutable.
	 */
	private final Map<String, String> codesAndNames;

	/**
	 * Immutable.
	 */
	private final Collection<NAMED_CODE> list;

	protected ImmutableNamedCodeCollection()
	{
		log.debug("call ImmutableNamedCodeStore()");

		namedCodeClass = ClassUtils.getClassOfFirstTypeArgument(this);
		codesAndNames = ImmutableSortedMap.copyOf(getMutableCodesAndNames());

		List<NAMED_CODE> mutableList = new ArrayList<NAMED_CODE>();
		for (Entry<String, String> code : codesAndNames.entrySet())
		{
			NAMED_CODE namedCode = ClassUtils.newInstanceOfClass(namedCodeClass);
			namedCode.setCode(code.getKey());
			namedCode.setName(code.getValue());
			mutableList.add(namedCode);
		}
		Collections.sort(mutableList);
		list = ImmutableList.copyOf(mutableList);
	}

	/**
	 * Must be called only once inside constructor.
	 */
	protected abstract Map<String, String> getMutableCodesAndNames();

	public Map<String, String> getCodesAndNames()
	{
		return codesAndNames;
	}

	// TODO: Low: Refactor to List? (mb)
	public Collection<NAMED_CODE> getCollection()
	{
		return list;
	}

	public List<String> getNamesByCodes(Collection<String> codes)
	{
		List<String> resultNames = new ArrayList<String>();
		if (!CollectionUtils.isEmpty(codes))
		{
			for (String code : codes)
			{
				if (isValidCode(code))
				{
					resultNames.add(getNameByCode(code));
				}
			}
		}
		return resultNames;
	}

	/**
	 * Returns <code>true</code> if there is code/name pair with code
	 * <code>== code</code>.
	 */
	public boolean isValidCode(String code)
	{
		return codesAndNames.containsKey(code);
	}

	public String getNameByCode(String code)
	{
		return codesAndNames.get(code);
	}

	public Set<String> getCodes()
	{
		return codesAndNames.keySet();
	}
}
