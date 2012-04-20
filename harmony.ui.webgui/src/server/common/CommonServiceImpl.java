/**
*  This code is part of the Harmony System implemented in Work Package 1 
*  of the Phosphorus project. This work is supported by the European 
*  Comission under the Sixth Framework Programme with contract number 
*  IST-034115.
*
*  Copyright (C) 2006-2009 Phosphorus WP1 partners. Phosphorus Consortium.
*  http://ist-phosphorus.eu/
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */


/**
 * 
 */
package server.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import javax.servlet.http.HttpSession;

import client.helper.GuiException;
import client.interfaces.CommonService;
import org.opennaas.core.utils.Config;
import org.opennaas.core.utils.TNAHelper;

/**
 * @author gassen
 */
public class CommonServiceImpl extends RemoteLoggableServiceServlet implements
        CommonService {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final GuiLogger log;
    private final HashMap<String, SessionInformation> loggedInUsers =
            new HashMap<String, SessionInformation>();
    private static final int TIMEOUT = 360000;

    /**
     * 
     */
    public CommonServiceImpl() {
        this.log = new GuiLogger(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see client.interfaces.CommonService#debugLog(java.lang.String)
     */
    public void debugLog(final String message) {
        this.log.debug(message);
    }

    /*
     * (non-Javadoc)
     * 
     * @see client.interfaces.CommonService#warnLog(java.lang.String)
     */
    public void warnLog(final String message) {
        this.log.warn(message);
    }

    /*
     * (non-Javadoc)
     * 
     * @see client.interfaces.CommonService#errorLog(java.lang.String)
     */
    public void errorLog(final String message) {
        this.log.error(message);
    }

    /*
     * (non-Javadoc)
     * 
     * @see client.interfaces.CommonService#fatalLog(java.lang.String)
     */
    public void fatalLog(final String message) {
        this.log.fatal(message);
    }

    /**
     * Resolves a domain name.
     * 
     * @param name
     * @return
     */
    public String resolveName(final String host) throws GuiException {
        try {
            return TNAHelper.resolve(host);
        } catch (final Exception e) {
            throw new GuiException(e);
        }
    }

    public Boolean logIn(final String username, final String password)
            throws GuiException {
        final HttpSession session = this.getRequest().getSession();

        String upwd = "";
        try {
            upwd = Config.getString("management", username + ".pwd").trim();
        } catch (final RuntimeException e) {
            return new Boolean(false);
        }

        String hash = "";
        try {
            final MessageDigest digest = MessageDigest.getInstance("SHA");

            final byte[] passedIn = digest.digest(password.getBytes());

            for (int i = 0; i < passedIn.length; i++) {
                final String hex = Integer.toHexString(passedIn[i] & 0xff);

                hash += (hex.length() > 1) ? hex : "0" + hex;
            }
        } catch (final NoSuchAlgorithmException e) {
            throw new GuiException("SHA not supported on your system");
        }

        if (upwd.equals(hash)) {
            final SessionInformation info =
                    new SessionInformation((new Date()).getTime(), 0, username);

            this.loggedInUsers.put(session.getId(), info);

            return new Boolean(true);
        }

        return new Boolean(false);
    }

    public Boolean logOut() {
        final HttpSession session = this.getRequest().getSession();

        this.loggedInUsers.remove(session.getId());

        return new Boolean(true);
    }

    public String isLoggedIn() throws GuiException {
        Iterator<String> users = this.loggedInUsers.keySet().iterator();

        final HttpSession session = this.getRequest().getSession();

        boolean loggedIn = false;

        final HashSet<String> expired = new HashSet<String>();

        final long now = (new Date()).getTime();

        while (users.hasNext()) {
            final String key = users.next();
            final SessionInformation val = this.loggedInUsers.get(key);

            if (val.getLastAccess() + CommonServiceImpl.TIMEOUT < now) {
                this.log.debug("Connection of " + val.getUsername()
                        + " timed out");
                expired.add(key);
            } else if (key.equals(session.getId())) {
                loggedIn = true;
            }
        }

        // Remove all expired users
        users = expired.iterator();
        while (users.hasNext()) {
            this.loggedInUsers.remove(users.next());
        }

        // Set new Timestamp
        if (loggedIn) {
            final SessionInformation info =
                    this.loggedInUsers.get(session.getId());

            info.setLastAccess(now);

            return info.getUsername();
        } else {
            throw new GuiException("Not logged in");
        }
    }

    public Boolean isSecure() {
        boolean secure = false;

        try {
            Class.forName(Config.getString("databinding", "secure.client"));
            
            secure = true;
        } catch (Exception e) {
            this.log.debug(e);
            
            secure = false;
        }

        return new Boolean(secure);
    }

}
