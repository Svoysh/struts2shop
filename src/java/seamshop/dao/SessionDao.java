package seamshop.dao;

import org.springframework.stereotype.Component;

import seamshop.model.Session;

/**
 * @author Alex Siman 2009-08-26
 */
@Component
public class SessionDao extends GenericDao<Session>
{
	/*
	 * TODO: Low: Check also Session.domain when get/delete session?
	 * (n, if webapp does not use subdomains as part of it)
	 */
}
