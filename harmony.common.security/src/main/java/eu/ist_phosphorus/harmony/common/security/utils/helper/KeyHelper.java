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


package eu.ist_phosphorus.harmony.common.security.utils.helper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.exceptions.UnexpectedFaultException;

public class KeyHelper {

    /**
     * @param type
     * @param alias
     * @param pass
     * @param keystoreFile
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     * @throws UnrecoverableKeyException
     * @throws IOException
     * @throws CertificateException
     * @throws UnexpectedFaultException
     */
    public static PrivateKey getPrivKey(final String type, final String pass,
            final URL keystoreFile, final String keyAlias, final String keyPass)
            throws UnrecoverableKeyException, KeyStoreException,
            NoSuchAlgorithmException, CertificateException, IOException,
            UnexpectedFaultException {
        KeyStore ks = KeyStore.getInstance(type);
        InputStream fis = keystoreFile.openStream();

        ks.load(fis, pass.toCharArray());

        PrivateKey privateKey =
                (PrivateKey) ks.getKey(keyAlias, keyPass.toCharArray());

        if (null == privateKey) {
            throw new UnexpectedFaultException(
                    "No Private Key found for  alias " + keyAlias);
        }

        return privateKey;
    }

    /**
     * @param type
     * @param alias
     * @param pass
     * @param keystoreFile
     * @return
     * @throws KeyStoreException
     * @throws IOException
     * @throws CertificateException
     * @throws NoSuchAlgorithmException
     * @throws UnexpectedFaultException 
     */
    public static PublicKey getPublicKey(final String type, final String pass,
            final URL keystoreFile, final String keyAlias, final String keyPass)
            throws KeyStoreException, NoSuchAlgorithmException,
            CertificateException, IOException, UnexpectedFaultException {
        KeyStore ks = KeyStore.getInstance(type);
        InputStream fis = keystoreFile.openStream();

        ks.load(fis, pass.toCharArray());
        
        X509Certificate cert = (X509Certificate) ks.getCertificate(keyAlias);

        if (null == cert) {
            throw new UnexpectedFaultException(
                    "No cert found for  alias " + keyAlias);
        }
        
        PublicKey pubKey = (PublicKey) cert.getPublicKey();

        return pubKey;
    }
}
