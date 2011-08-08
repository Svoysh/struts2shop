package seamshop.consts;

/**
 * @author Alex Siman 2009-08-26
 */
public abstract class Time
{
	/**
	 * week = 1000 * 60 * 60 * 24 * 7 = 604800000
	 */
	public static final long WEEK_IN_MILLIS = 604800000L;

	/**
	 * month = 1000 * 60 * 60 * 24 * 30 = 2592000000
	 */
	public static final long MONTH_IN_MILLIS = 2592000000L;

	/**
	 * Duration of login remembering (i.e. how long user's session remains valid)
	 * in seconds.
	 * <p/>
	 * Possible values:
	 * <ul>
	 * <li>1 week = 60 * 60 * 24 * 7 = 604800</li>
	 * <li>2 weeks = 60 * 60 * 24 * 14 = 1209600</li>
	 * <li>1 month = 60 * 60 * 24 * 30 = 2592000</li>
	 * <li>1 year = 60 * 60 * 24 * 365 = 31536000</li>
	 * </ul>
	 */
    public static final int REMEMBER_ME_MAX_COOKIE_AGE_IN_SECS = 2592000;

    /**
     * 30 days. Equals to <code>1000 * REMEMBER_ME_MAX_COOKIE_AGE_IN_SECS</code>.
     */
    public static final long SESSION_DURATION_IN_MILLIS = MONTH_IN_MILLIS;

    public static final long CONFIRM_EMAIL_DURATION_IN_MILLIS = MONTH_IN_MILLIS;

    public static final long RESET_PASSWORD_DURATION_IN_MILLIS = WEEK_IN_MILLIS;
}
