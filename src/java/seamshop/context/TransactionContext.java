package seamshop.context;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import seamshop.util.Command;

/**
 * Helper class to execute DB related methods within transaction. It is Spring
 * bean.
 * <p/>
 * Do not use this class to store any objects. It must have no fields, only
 * transaction related methods.
 * <p/>
 * NOTE: It seems like read-only transaction does not work (in fact it depends
 * on JDBC) and insert/update/delete operations are allowed within read-only
 * transaction. <code>readOnly</code> acts as a hint for JDBC and nothing more.
 *
 * @author Alex Siman 2009-05-15
 * @author Alex Siman 2009-06-08
 */
// This Spring bean configured in "applicationContext.xml".
// TODO: Remove Spring related annotations? (y)
//@Component
//@Scope(BeanDefinition.SCOPE_SINGLETON)

//@SuppressWarnings("serial")
public class TransactionContext /*implements Serializable*/
{
	// TODO: Low: isolation, timeout? (y)
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public void doInReadTransaction(Command command) throws Exception
	{
		command.execute();
	}

	// TODO: Low: isolation, timeout? (y)
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void doInWriteTransaction(Command command) throws Exception
	{
		command.execute();
	}
}
