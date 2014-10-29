/**
 * (c) 2002 by WPS WertpapierService Bank AG
 *
 * History:
 * $Log: Password.java,v $
 * Revision 1.1  2011/02/21 09:39:50  safi
 * *** empty log message ***
 *
 * Revision 1.1  2010/12/21 14:03:35  safi
 * *** empty log message ***
 *
 * Revision 1.1  2010/11/19 14:22:59  zsolt
 * *** empty log message ***
 *
 * Revision 1.1  2006/06/22 08:29:38  placi
 * *** empty log message ***
 *
 * Revision 1.1  2006/06/14 13:31:39  placi
 * *** empty log message ***
 *
 * Revision 1.1  2006/01/20 12:29:31  placi
 * *** empty log message ***
 *
 * Revision 1.1  2005/09/16 14:06:04  placi
 * *** empty log message ***
 *
 * Revision 1.1  2005/02/18 10:59:57  placi
 * *** empty log message ***
 *
 * Revision 1.6  2003/08/27 14:34:18  safi
 * *** keyword substitution change ***
 *
 *
 **/

package hu.safi.friendsfootball.server;

import javax.naming.AuthenticationException;

/**
 * Class to generate SHA1 passsord.
 * 
 * @author $Author: safi $
 * @version $Id: Password.java,v 1.1 2011/02/21 09:39:50 safi Exp $
 * 
 */

public class Password {

	/**
	 * Return the encoded password for a user, which is stored in the database
	 * 
	 * @param userId
	 * @param password
	 * @return encoded password
	 * @exception AuthenticationException
	 */
	public static String getDigestString(String user, String password) {

		return "{SHA}"  + Base64.encode(user+password);
	}
}
