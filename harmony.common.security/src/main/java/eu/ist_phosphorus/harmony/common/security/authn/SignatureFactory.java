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


package eu.ist_phosphorus.harmony.common.security.authn;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.security.auth.x500.X500Principal;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.apache.muse.util.xml.XmlUtils;
import org.apache.xml.security.Init;
import org.apache.xml.security.encryption.EncryptedData;
import org.apache.xml.security.encryption.EncryptedKey;
import org.apache.xml.security.encryption.XMLCipher;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.keys.KeyInfo;
import org.apache.xml.security.samples.utils.resolver.OfflineResolver;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.Constants;
import org.apache.xml.security.utils.EncryptionConstants;
import org.apache.xml.security.utils.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.org.apache.xpath.internal.XPathAPI;

import eu.ist_phosphorus.harmony.common.security.utils.AAIConstants;
import eu.ist_phosphorus.harmony.common.security.utils.helper.KeyHelper;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.exceptions.UnexpectedFaultException;
import eu.ist_phosphorus.harmony.common.utils.Config;
import eu.ist_phosphorus.harmony.common.utils.PhLogger;

/**
 * Implementation of the Signature Factory for the Authentication.
 * 
 * @author Carlos Baez (carlos.baez@i2cat.net)
 * @version $Id$
 */
public class SignatureFactory {
    private static SignatureFactory signatureFactory = null;
    private final static Logger logger = PhLogger.getSeparateLogger("aai");

    final Key privkey;
    final Key pubkey;

    final String keystoreType;
    final URL keystoreFile;
    final String keystorePass;

    final String keyAlias;
    final String keyPass;

    static {
        Init.init();
    }

    public static void resetInstance() {
    	signatureFactory = null;
    }
    public static Document decryptDocument(final Document document,
            final Key kek) throws Exception {

        // String jceAlgoKey = "DESede";
        // String kekfname = "data/keystore/xmlsec/symkeystore/kek1";
        // Key skek = loadKeyEncryptionKey(jceAlgoKey, kekfname);

        final Element encryptedDataElement =
                (Element) document.getElementsByTagNameNS(
                        EncryptionConstants.EncryptionSpecNS,
                        EncryptionConstants._TAG_ENCRYPTEDDATA).item(0);

        final Node cval =
                document.getElementsByTagNameNS(
                        EncryptionConstants.EncryptionSpecNS,
                        EncryptionConstants._TAG_CIPHERVALUE).item(0);
        
        logger.debug("\n### encrypted element: "+encryptedDataElement+" CipherValue element: " + cval.getNodeValue() + "\n");
        /* Load the key to be used for decrypting the xml data encryption key. */
        logger.debug("\n###key encryption key: " + kek.toString() + "\n");
        // String providerName = "BC";
        final XMLCipher xmlCipher = XMLCipher.getInstance();
        /*
         * The key to be used for decrypting xml data would be obtained from the
         * keyinfo of the EncrypteData using the kek.
         */
        xmlCipher.init(XMLCipher.DECRYPT_MODE, null);
        xmlCipher.setKEK(kek);
        /*
         * The following doFinal call replaces the encrypted data with decrypted
         * contents in the document.
         */

        xmlCipher.doFinal(document, encryptedDataElement);


        // outputDocToFile(document, "decrypted-doc.xml");
        return document;
    }

    /**
     * Function for encrypt messages with public keys, it is used for encrypt a
     * document
     * 
     * @param document
     *            Document without encryption
     * @param pubkek
     *            Public Key for the encryption
     * @return Class document encrypted
     * @throws Exception
     *             It throws a general exception when
     */
    public static Document encryptDocPublic(final Document document,
            Element encryptElement, final Key pubkek) throws Exception {

        /*
         * Document document Key pubkek - public key wrapping key Key sdek -
         * symmetric data encryption key String algodataURI
         */
        // Method/Algorithm for Data encryption key
        final String algodataURI = XMLCipher.AES_128;

        // Type/Algorithm for Data encryption key
        final String jceAlgoData = "AES";
        final Key sdek =
                SignatureFactory.GenerateDataEncryptionKey(jceAlgoData);

        logger.debug("\n###Public key encryption key: \n" + pubkek.toString()
                + "\n");

        final String algopkekURI = XMLCipher.RSA_v1dot5;

        /*
         * Create an XML Signature object from the document, BaseURI and
         * signature algorithm (in this case DSA)
         */
        // Init.init();
        final XMLCipher keyCipher = XMLCipher.getInstance(algopkekURI);
        keyCipher.init(XMLCipher.WRAP_MODE, pubkek);
        final EncryptedKey encryptedKey = keyCipher.encryptKey(document, sdek);
        /*
         * Let us encrypt the contents of the document element.
         */

        // algodataURI = XMLCipher.AES_128;
        logger.debug("\n###Symmetric data encryption key: " + sdek.toString()
                + "\n");

        final XMLCipher xmlCipher = XMLCipher.getInstance(algodataURI);
        xmlCipher.init(XMLCipher.ENCRYPT_MODE, sdek);

        /*
         * Setting keyinfo inside the encrypted data being prepared.
         */
        final EncryptedData encryptedData = xmlCipher.getEncryptedData();
        final KeyInfo keyInfo = new KeyInfo(document);
        keyInfo.add(encryptedKey);
        encryptedData.setKeyInfo(keyInfo);

        /*
         * doFinal - "true" below indicates that we want to encrypt element's
         * content and not the element itself. Also, the doFinal method would
         * modify the document by replacing the EncrypteData element for the
         * data to be encrypted.
         */
        xmlCipher.doFinal(document, encryptElement, true);

        /*
         * Output the document containing the encrypted information into a file.
         */
        // outputDocToFile(document, docencrsave);authorization
        return document;
    }

    /**
     * 
     * @param jceAlgorithmName
     * @return
     * @throws Exception
     */
    private static SecretKey GenerateDataEncryptionKey(
            final String jceAlgorithmName) throws Exception {

        // String jceAlgorithmName = "AES";
        final KeyGenerator keyGenerator =
                KeyGenerator.getInstance(jceAlgorithmName);
        keyGenerator.init(128);
        return keyGenerator.generateKey();
    }

    /**
     * Function for the Factory pattern, it return a Factory instance
     * 
     * @param keyset
     *            key set with the parameters
     * @return A SignatureFactory instance is returned
     * @throws IOException
     * @throws CertificateException
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     * @throws UnexpectedFaultException
     * @throws UnrecoverableKeyException
     */
    public static SignatureFactory getInstance(String keystoreType, String keyStoreFile,
            String keyStorePass, String privateKeyAlias, String privateKeyPass,
            String certificateAlias)
            throws UnrecoverableKeyException, UnexpectedFaultException,
            KeyStoreException, NoSuchAlgorithmException, CertificateException,
            IOException {
        if (SignatureFactory.signatureFactory == null) {
            SignatureFactory.signatureFactory = new SignatureFactory(keystoreType,keyStoreFile,keyStorePass,privateKeyAlias,privateKeyPass);
        }
        return SignatureFactory.signatureFactory;
    }    
    
    /**
     * Function for the Factory pattern, it return a Factory instance
     * 
     * @param keyset
     *            key set with the parameters
     * @return A SignatureFactory instance is returned
     * @throws IOException
     * @throws CertificateException
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     * @throws UnexpectedFaultException
     * @throws UnrecoverableKeyException
     */
    public static SignatureFactory getInstance()
            throws UnrecoverableKeyException, UnexpectedFaultException,
            KeyStoreException, NoSuchAlgorithmException, CertificateException,
            IOException {
        if (SignatureFactory.signatureFactory == null) {
            SignatureFactory.signatureFactory = new SignatureFactory();
//
        }
        return SignatureFactory.signatureFactory;
    }

    private static final HashMap<String, String> parseX500Principal(
            X500Principal data) {

        String issuer = data.toString();
        String[] issuerArray = issuer.split(", ");
        HashMap<String, String> issuerHashMap = new HashMap<String, String>();

        for (String kvPair : issuerArray) {
            String[] kv = kvPair.split("=");
            issuerHashMap.put(kv[0], kv[1]);
        }

        return issuerHashMap;
    }

    /**
     * Validate a signature in a Document Request
     * 
     * @param doc
     *            Document to validate
     * @return True if it is ok the signature
     * @throws Exception
     *             If an exception occurs in the parsing
     */
    public static HashMap<String, String> validateDOMdoc(
            final org.w3c.dom.Document doc) throws Exception {

        XMLSignature signature = null;

        final Element nscontext =
                XMLUtils.createDSctx(doc, "ds", Constants.SignatureSpecNS);
        final Element sigElement =
                (Element) XPathAPI.selectSingleNode(doc, "//ds:Signature",
                        nscontext);

        signature = new XMLSignature(sigElement, "");

        signature.addResourceResolver(new OfflineResolver());

        final KeyInfo keyInfo = signature.getKeyInfo();
        if (keyInfo != null) {

            if (keyInfo.containsX509Data()) {
                SignatureFactory.logger
                        .debug("Found a X509Data element in the KeyInfo. Verifying...");
            }
            final X509Certificate cert = keyInfo.getX509Certificate();

            if (cert != null) {
                SignatureFactory.logger.debug("Validating...");

                if (signature.checkSignatureValue(cert)) {
                	SignatureFactory.logger.debug("Checking issuer....");
                    final X500Principal data = cert.getIssuerX500Principal();

                    SignatureFactory.logger.debug("Issuer: " + data.toString());

                    return parseX500Principal(data);
                }
            } else {
                final PublicKey publicKey = keyInfo.getPublicKey();

                if (publicKey != null) {
                    SignatureFactory.logger
                            .debug("Found a public key in the KeyInfo. Verifying...");
                    if (signature.checkSignatureValue(publicKey)) {
                        return new HashMap<String, String>();
                    }
                }
            }
        }

        return null;
    }

    /**
     * Parse a String to a Document class
     * 
     * @param docstr
     *            The String with the SOAP message
     * @return the Document Representation
     * @throws Exception
     *             If an exception occurs in the parsing
     */
    @Deprecated
    public static org.w3c.dom.Document String2DOM(String docstr)
            throws Exception {
        // start xml document processing part
        javax.xml.parsers.DocumentBuilderFactory dbf =
                javax.xml.parsers.DocumentBuilderFactory.newInstance();
        dbf.setIgnoringElementContentWhitespace(true);

        // XML Signature needs to be namespace aware
        dbf.setNamespaceAware(true);
        javax.xml.parsers.DocumentBuilder db = dbf.newDocumentBuilder();
        // reading document
        org.w3c.dom.Document doc =
                db.parse(new ByteArrayInputStream(docstr.getBytes()));

        return doc;
    }

    /**
     * Parse a Document class to String
     * 
     * @param doc
     *            Document for parsing
     * @return the String representation
     * @throws Exception
     *             If an exception occurs in the parsing
     */
    @Deprecated
    public static String DOM2String(final org.w3c.dom.Document doc)
            throws Exception {
        // print DOM doc
        final ByteArrayOutputStream f = new ByteArrayOutputStream();
        XMLUtils.outputDOMc14nWithComments(doc, f);
        f.close();
        String str = f.toString();

        return str;
    }

    /**
     * Parse a node class to String
     * 
     * @param node
     *            Node for parsing
     * @return the String representation
     * @throws Exception
     *             If an exception occurs in the parsing
     */
    @Deprecated
    public static String Node2String(final org.w3c.dom.Node node)
            throws Exception {
        // print DOM doc
        // final ByteArrayOutputStream f = new ByteArrayOutputStream();
        // XMLUtils.outputDOMc14nWithComments(node, f);
        // f.close();
        // return f.toString();
        return XmlUtils.toString(node, false, false);
    }

    

    /**
     * Factory Constructor. it save the key parameters
     * 
     * @param keyset
     *            key set with the parameters
     * @throws IOException
     * @throws CertificateException
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     * @throws UnexpectedFaultException
     * @throws UnrecoverableKeyException
     * @throws Exception
     * @throws Exception
     *             If an exception occurs when there is a problem with the
     *             Keystore or with the initial configuratioon
     */
    protected SignatureFactory(String keystoreType, String keystoreFile, String keystorePass, String keyAlias, String keyPass) throws UnrecoverableKeyException,
            UnexpectedFaultException, KeyStoreException,
            NoSuchAlgorithmException, CertificateException, IOException {
        /** Security and key parameters */
        this.keystoreType =keystoreType;
        this.keystoreFile = new URL("file://"+keystoreFile);
        this.keystorePass =keystorePass;
        this.keyAlias =keyAlias;
        this.keyPass = keyPass;

        this.privkey =
                KeyHelper.getPrivKey(this.keystoreType, this.keystorePass,
                        this.keystoreFile, this.keyAlias, this.keyPass);

        this.pubkey =
                KeyHelper.getPublicKey(this.keystoreType, this.keystorePass,
                        this.keystoreFile, this.keyAlias, this.keyPass);
    }    
    
    
    /**
     * Factory Constructor. it save the key parameters
     * 
     * @param keyset
     *            key set with the parameters
     * @throws IOException
     * @throws CertificateException
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     * @throws UnexpectedFaultException
     * @throws UnrecoverableKeyException
     * @throws Exception
     * @throws Exception
     *             If an exception occurs when there is a problem with the
     *             Keystore or with the initial configuratioon
     */
    protected SignatureFactory() throws UnrecoverableKeyException,
            UnexpectedFaultException, KeyStoreException,
            NoSuchAlgorithmException, CertificateException, IOException {
        /** Security and key parameters */
        this.keystoreType = Config.getString("aai", "authn.keystore.type");
        this.keystoreFile = Config.getURL("aai", "authn.keystore.file");
        this.keystorePass = Config.getString("aai", "authn.keystore.pass");
        this.keyAlias = Config.getString("aai", "authn.key.alias");
        this.keyPass = Config.getString("aai", "authn.key.pass");

        this.privkey =
                KeyHelper.getPrivKey(this.keystoreType, this.keystorePass,
                        this.keystoreFile, this.keyAlias, this.keyPass);

        this.pubkey =
                KeyHelper.getPublicKey(this.keystoreType, this.keystorePass,
                        this.keystoreFile, this.keyAlias, this.keyPass);
    }

    public org.w3c.dom.Element createSignature(final Element elem)
            throws ParserConfigurationException, XMLSecurityException,
            NoSuchAlgorithmException, CertificateException, IOException,
            KeyStoreException {
        return this.createSignature(elem, elem.getOwnerDocument());
    }

    public org.w3c.dom.Element createSignature(Element elem, Document domRequest)
            throws ParserConfigurationException, XMLSecurityException,
            NoSuchAlgorithmException, CertificateException, IOException,
            KeyStoreException {

        /*
         * String xpath2sign = "not(ancestor-or-self::ds:Signature) \n" +
         * "and (ancestor-or-self::node() = /AAARequest/Subject)";
         */
        /** The BaseURI is the URI that's used to prepend to relative URIs */
        final String BaseURI = "";
        final String uri2sign = "";

        // Document domRequest = elem.getOwnerDocument();

        if (null == elem) {
            // System.err.println(XmlUtils.toString(domRequest));
            throw new RuntimeException("No Header found");
        }

        // elemHeader.appendChild(domRequest.createComment(" Comment before "));
        /* root element is AAARequest */
        final Element root =
                domRequest.createElementNS(
                        "http://www.aaauthreach.org/ns/#AAA",
                        AAIConstants.REQUEST);

        root.setAttribute("Id", "CNLhashID#");
        root.setAttributeNS(null, "version", "2.0");
        root.setAttributeNS(Constants.NamespaceSpecNS, "xmlns:cnl",
                "http://www.telin.nl/ns/#cnl");
        root.setAttributeNS("http://www.telin.nl/ns/#cnl", "cnl:attr1",
                "CNL2test#");
        root.appendChild(domRequest.createTextNode("\n"));

        root.setAttributeNS(Constants.NamespaceSpecNS, "xmlns:"
                + AAIConstants.AAA_NAMESPACE,
                "http://www.aaauthreach.org/ns/#AAA");

        elem.appendChild(root);

        final Element resource =
                domRequest.createElementNS(
                        "http://www.aaauthreach.org/ns/#AAA",
                        AAIConstants.RESOURCE);
        resource
                .appendChild(domRequest
                        .createTextNode("Resource element defines the target resource\n"));
        root.appendChild(resource);

        // elemHeader.appendChild(domRequest.createComment(" Comment after "));

        /*
         * Create an XML Signature object from the document, BaseURI and
         * signature algorithm (in this case DSA)
         */
        Init.init();
        final XMLSignature sig =
                new XMLSignature(domRequest, BaseURI,
                        XMLSignature.ALGO_ID_SIGNATURE_RSA);

        root.appendChild(sig.getElement());
        sig
                .getSignedInfo()
                .addResourceResolver(
                        new org.apache.xml.security.samples.utils.resolver.OfflineResolver());

        /* Sign the document */
        {
            /* create the transforms object for the Document/Reference */
            final Transforms transforms = new Transforms(domRequest);

            /*
             * First we have to strip away the signature element (it's not part
             * of the signature calculations). The enveloped transform can be
             * used for this.
             */
            transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);

            // transforms.addTransform(Transforms.TRANSFORM_XPATH2FILTER,
            // XPath2FilterContainer.newInstance(domRequest.getDocumentElement(),
            // "//Envelope/Body").getElement());

            /*
             * Part of the signature element needs to be canonicalized. It is a
             * kind of normalizing algorithm for XML. For more information
             * please take a look at the W3C XML Digital Signature webpage.
             */
            transforms.addTransform(Transforms.TRANSFORM_C14N_WITH_COMMENTS);
            /* Add the above Document/Reference */
            sig
                    .addDocument(uri2sign, transforms,
                            Constants.ALGO_ID_DIGEST_SHA1);

        }

        {

            final KeyStore ks = KeyStore.getInstance(this.keystoreType);
            final InputStream fis = this.keystoreFile.openStream();

            // //FileOutputStream fus = new FileOutputStream(outFile);
            // //System.out.print(fis.toString());

            /* load the keystore */
            ks.load(fis, this.keystorePass.toCharArray());

            // Add in the KeyInfo for the certificate that we used the private
            // key of
            final X509Certificate cert =
                    (X509Certificate) ks.getCertificate(this.keyAlias);

            // System.out.print("\n### X.509 content: \n" + cert.toString());
            // //***

            sig.addKeyInfo(cert);
            sig.addKeyInfo(cert.getPublicKey());
            sig.sign(this.privkey);

        }

        return root;
    }

    /**
     * Decrypt a element Document
     * 
     * @param domRequest
     * @return
     * @throws Exception
     */
    public Document decrypt(final Element elementRequest) throws Exception {
        /* Decrypt the body message */

        final Document domWithoutEncrypt =
                SignatureFactory.decryptDocument(elementRequest
                        .getOwnerDocument(), this.privkey);
        return domWithoutEncrypt;

    }

    public Document encrypt(Element soapElem) throws Exception {

    	
    	
    	
        /* Encrypt element */
        Node domEncryptRequest =
                soapElem.getElementsByTagName("soap:Body").item(0);

        /* ************** */
        Document encryptDom =
                SignatureFactory.encryptDocPublic(soapElem.getOwnerDocument(),
                        (Element) domEncryptRequest, this.pubkey);
        return encryptDom;
    }
    
    public Document encrypt(Element soapElem, String alias) throws Exception {

        Key pubkey =
            KeyHelper.getPublicKey(this.keystoreType, this.keystorePass,
                    this.keystoreFile, alias, this.keyPass);
    	
        /* Encrypt element */
        Node domEncryptRequest =
                soapElem.getElementsByTagName("soap:Body").item(0);

        /* ************** */
        Document encryptDom =
                SignatureFactory.encryptDocPublic(soapElem.getOwnerDocument(),
                        (Element) domEncryptRequest,pubkey);
        return encryptDom;
    }

    /**
     * The method checks if the String is encrypted
     * 
     * @param msgRequest
     * @return
     * @throws Exception
     */
    @Deprecated
    public boolean isEncrypted(final String msgRequest) throws Exception {

        final Document domRequest = SignatureFactory.String2DOM(msgRequest);
        final NodeList matches =
                domRequest.getElementsByTagName("xenc:EncryptedData");
        if (matches.getLength() <= 0) {
            return false;
        }

        return true;
    }

    /**
     * The method checks if the Document is encrypted
     * 
     * @param domRequest
     * @return
     * @throws Exception
     */
    public boolean isEncrypted(final Element domRequest) throws Exception {
        final NodeList matches =
                domRequest.getElementsByTagName("xenc:EncryptedData");
        if (matches.getLength() <= 0) {
            return false;
        }

        return true;
    }

    public boolean isSigned(final Element msgRequest) throws Exception {
        final Document domRequest = msgRequest.getOwnerDocument();
        final NodeList matches =
                domRequest.getElementsByTagName(AAIConstants.REQUEST);
        if (matches.getLength() <= 0) {
            return false;
        }

        return true;
    }

    @Deprecated
    public boolean isSigned(final String msgRequest) throws Exception {
        final Document domRequest = SignatureFactory.String2DOM(msgRequest);
        final NodeList matches =
                domRequest.getElementsByTagName(AAIConstants.REQUEST);
        if (matches.getLength() <= 0) {
            return false;
        }

        return true;
    }

    /**
     * It is function for validate a signature in a String. the String is parsed
     * to Document and it is called the valideDOMdoc
     * 
     * @param msgRequest
     * @return True if it is ok the signature
     * @throws Exception
     *             If an exception occurs in the parsing or the validation
     */
    @Deprecated
    public boolean isValid(final String msgRequest) throws Exception {

        final Document domRequest = SignatureFactory.String2DOM(msgRequest);

        return (null != SignatureFactory.validateDOMdoc(domRequest));
    }

    public Element sign(Element elemNode) throws Exception {
        Constants.setSignatureSpecNSprefix("ds");

        org.w3c.dom.Node nodeHeader =
                elemNode.getElementsByTagName("soap:Header").item(0);

        this.createSignature((Element) nodeHeader);
//        SignatureFactory.logger.debug(XmlUtils.toString(elemNode));
        return elemNode;

    }

    /**
     * It is function for validate a signature in a String. the String is parsed
     * to Document and it is called the valideDOMdoc
     * 
     * @param msgRequest
     * @return True if it is ok the signature
     * @throws Exception
     *             If an exception occurs in the parsing or the validation
     */
    public HashMap<String, String> isValid(final Element elemRequest)
            throws Exception {
        return SignatureFactory.validateDOMdoc(elemRequest.getOwnerDocument());
    }

    @Deprecated
    public String removeSignature(final String msgRequest) throws Exception {
        final Document domRequest = SignatureFactory.String2DOM(msgRequest);

        return DOM2String(removeSignature(domRequest));
    }

    public Document removeSignature(final Document domRequest) throws Exception {
        if (!isSigned(domRequest.getDocumentElement()))
            return domRequest;

        final NodeList matches =
                domRequest.getElementsByTagName(AAIConstants.REQUEST);
        matches.item(0).getParentNode().removeChild(matches.item(0));

        return domRequest;
    }

}
