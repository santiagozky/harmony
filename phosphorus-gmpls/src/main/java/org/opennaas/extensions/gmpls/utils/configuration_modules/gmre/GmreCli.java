package org.opennaas.extensions.gmpls.utils.configuration_modules.gmre;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.SocketException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.net.telnet.TelnetClient;
import org.apache.log4j.Logger;

import org.opennaas.extensions.gmpls.utils.configuration_modules.gmre.exceptions.GmreConnectionException;
import org.opennaas.extensions.gmpls.utils.configuration_modules.gmre.exceptions.PathCreationException;
import org.opennaas.core.utils.PhLogger;

/**
 * Low-level interface to the GMRE of an Alcatel 1678.
 * 
 * @author Stephan Wagner (stephan.wagner@iais.fraunhofer.de)
 */
public class GmreCli {

    private static Logger logger = PhLogger.getLogger(GmreCli.class);

    /** */
    private static final char prompt = '#';
    /** */
    private final TelnetClient telnet = new TelnetClient();
    /** */
    private InputStream in;
    /** */
    private PrintStream out;

    /**
     * @param server
     * @param user
     * @param password
     * @param port
     * @throws GmreConnectionException
     */
    public GmreCli(final String server, final String user,
            final String password, final int port)
            throws GmreConnectionException {

        try {
            logger.info("Connecting new GmreCli");
            logger.debug("Server: " + server);
            logger.debug("Port: " + port);
            this.telnet.connect(server, port);

            this.in = this.telnet.getInputStream();
            this.out = new PrintStream(this.telnet.getOutputStream());
            // Login
            this.readUntil("username[8chars]:");
            this.write(user);

            this.readUntil("password[8chars]:");
            this.write(password);

            this.readUntil(GmreCli.prompt + " ");
        } catch (final SocketException e) {
            logger.warn("SocketException on: " + server + ":" + port, e);
            throw new GmreConnectionException(e);
        } catch (final IOException e) {
            logger.warn(e.getMessage(), e);
            throw new GmreConnectionException(e);
        } catch (final InterruptedException e) {
            logger.warn(e.getMessage(), e);
            throw new GmreConnectionException(e);
        }
    }

    /**
     * Try to change the state of a LSP to "administratively down".
     * 
     * @param lspIndex
     *                GMRE index of the LSP.
     * @return success
     * @throws GmreConnectionException
     */
    public final boolean adminDownLsp(final int lspIndex)
            throws GmreConnectionException {
        boolean result = false;
        this
                .sendCommand("config lsP lsPindex " + lspIndex
                        + " adminState doWN");
        result = this.isLspAdminDown(lspIndex);
        return result;
    }

    /**
     * @param inLink
     * @param inLabel
     * @param destLink
     * @param destLabel
     * @param lspTraffic
     * @param protRestType
     * @param srcTNA
     * @param destTNA
     * @param pathIDE
     * @return
     * @throws GmreConnectionException
     * @throws PathCreationException
     */
    public final int configLSP(final String inLink, final String inLabel,
            final String destLink, final String destLabel,
            final String lspTraffic, final String protRestType,
            final String srcTNA, final String destTNA)
            throws GmreConnectionException, PathCreationException {
        int lspIndex = 0;
        String outputBuffer;
        final String lspDescriptor = "-" + srcTNA + "-" + destTNA;

        // Check if the descriptor is available.
        if (this.isLspConfigured(lspDescriptor)) {
            throw new PathCreationException("LSP descriptor already exists.");
        }

        outputBuffer =
                this.sendCommand(this.configureTnaLsp(lspDescriptor,
                        lspTraffic, srcTNA, inLink, inLabel, destTNA, destLink,
                        destLabel, protRestType));
        // This is a very rarely error. The CLI swallows something.
        // Repeating it ONCE!
        if (outputBuffer.contains("*error* parameter wrong")) {
            outputBuffer =
                    this.sendCommand(this.configureTnaLsp(lspDescriptor,
                            lspTraffic, srcTNA, inLink, inLabel, destTNA,
                            destLink, destLabel, protRestType));
        }

        lspIndex = this.filterLspIndex(outputBuffer);

        this.sendCommand("end");

        /*
         * Check errors - LSP setup failed. Reason: label allocation failed. -
         */
        if (this.labelFailure(outputBuffer)) {
            try {
                this.adminDownLsp(lspIndex);
                this.deleteLsp(lspIndex);

            } catch (final GmreConnectionException e) {
                throw e;
            } catch (final Exception ex) {
                throw new PathCreationException(ex);
            }
            throw new PathCreationException(
                    "Path already exists on this interface.");

        }
        if (this.routeFailure(outputBuffer)) {
            try {
                this.adminDownLsp(lspIndex);
                this.deleteLsp(lspIndex);
            } catch (final GmreConnectionException e) {
                throw e;
            } catch (final Exception ex) {
                throw new PathCreationException(ex);
            }
            throw new PathCreationException("Route failure.");
        }
        try {
            Thread.sleep(200);
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
        try {
            if (this.isLspAdminUp(lspIndex)) {
                return lspIndex;
            }
        } catch (final GmreConnectionException e) {
            throw e;
        } catch (final Exception e) {
            logger.debug("Exception in Pathsetup");
            logger.debug("Class: " + e.getClass());
            logger.debug("Message: " + e.getMessage());
            logger.debug("Cause: " + e.getCause());
            logger.debug("Stactrace: ");
            for (StackTraceElement element : e.getStackTrace()) {
                logger.debug(element.getFileName() + element.getMethodName()
                        + element.getLineNumber());
            }

            throw new PathCreationException(
                    "LSP not switched. An unknown error occurred! Exception was: "
                            + e.getClass() + "Message: " + e.getMessage());
        }

        // An unknown error occurred...
        throw new PathCreationException(
                "LSP not switched. An unknown error occurred!");
    }

    /**
     * @param descriptor
     * @param lspTraffic
     * @param sourceTNA
     * @param inLink
     * @param inLabel
     * @param destTNA
     * @param destLink
     * @param destLabel
     * @param protRestType
     * @return
     */
    public final String configureTnaLsp(final String descriptor,
            final String lspTraffic, final String sourceTNA,
            final String inLink, final String inLabel, final String destTNA,
            final String destLink, final String destLabel,
            final String protRestType) {
        final StringBuffer result = new StringBuffer(256);

        // basic parameters
        result.append("config lsp ");
        result.append("lspdescriptor" + " \"" + descriptor + "\" ");
        result.append("adminState up ");

        // VC4
        result.append("lsptraffic " + lspTraffic + " ");

        // source TNA parameters
        result.append("sourceTNA " + sourceTNA.trim() + " ");
        result.append("inlink " + inLink + " ");
        result.append("inlabel " + inLabel + " ");

        // destination TNA parameters
        result.append("destTNA " + destTNA.trim() + " ");
        result.append("destlink " + destLink + " ");
        result.append("destlabel " + destLabel + " ");

        // protection
        result.append("prot_rest_type " + protRestType + " ");

        return result.toString();
    }

    /**
     * @param lspIndex
     * @return
     * @throws GmreConnectionException
     */
    private boolean deleteLsp(final int lspIndex)
            throws GmreConnectionException {
        this.sendCommand("delete lsP " + lspIndex);
        return !this.isLspConfigured(lspIndex);
    }

    /**
     * @throws IOException
     */
    private void disconnect() throws IOException {
        this.telnet.disconnect();

    }

    /**
     * @throws GmreConnectionException
     * @throws IOException
     */
    public final void endSession() throws GmreConnectionException {

        try {
            this.sendCommand("quit");
            this.disconnect();
        } catch (final IOException e) {
            e.printStackTrace();
            throw new GmreConnectionException(e);
        }
    }

    /**
     * This method tries to extract the LSP index from a configuration request
     * response. If an index is found, it is checked once. If the index is not
     * found in the response, The LSP table is checked.
     * 
     * @param message
     *                Result of a configuration request response.
     * @return LSP index
     */
    protected final int filterLspIndex(final String message) {
        String result = null;
        logger.debug("filterlsp: " + message);
        final Pattern regexp =
                Pattern.compile("^.*LSPIndex: \\d+.*$", Pattern.MULTILINE);

        final Matcher matcher = regexp.matcher(message);
        matcher.find();
        result = matcher.group(0).trim();
        final Pattern regexp2 = Pattern.compile("\\d+");
        final Matcher matcher2 = regexp2.matcher(result);
        matcher2.find();
        return Integer.valueOf(
                result.substring(matcher2.start(), matcher2.end())).intValue();
    }

    /**
     * @param lspDescriptor
     * @return
     * @throws GmreConnectionException
     */
    public final int getLspIndexForDescriptor(final String lspDescriptor)
            throws GmreConnectionException {
        final String message = this.showLspsByFilter(lspDescriptor);

        final Pattern regexp =
                Pattern.compile("^.*" + lspDescriptor + ".*$",
                        Pattern.MULTILINE);

        final Matcher matcher = regexp.matcher(message);

        if (matcher.find()) {
            String result = matcher.group(0).trim();

            final Pattern regexp2 = Pattern.compile("^\\d+");
            final Matcher matcher2 = regexp2.matcher(result);
            matcher2.find();
            result = result.substring(matcher2.start(), matcher2.end()).trim();

            return Integer.valueOf(result).intValue();
        }
        return 0;
    }

    /**
     * @param lspIndex
     * @param lspDescriptor
     * @return
     * @throws GmreConnectionException
     */
    protected final boolean isLspAdminDown(final int lspIndex)
            throws GmreConnectionException {
        final String pattern = "^" + lspIndex + ".+Down/.*$";

        final String message = this.showLspsByFilter(String.valueOf(lspIndex));
        final Pattern regexp = Pattern.compile(pattern, Pattern.MULTILINE);
        final Matcher matcher = regexp.matcher(message);
        if (matcher.find()) {
            return true;
        }
        return false;
    }

    /**
     * @param lspIndex
     * @param lspDescriptor
     * @return
     * @throws GmreConnectionException
     */
    public final boolean isLspAdminUp(final int lspIndex)
            throws GmreConnectionException {
        if (this.isLspAdminDown(lspIndex)) {
            this.deleteLsp(lspIndex);
        }

        final String pattern = "^" + lspIndex + "\\s*.+Up/.*$";

        final Pattern regexp = Pattern.compile(pattern, Pattern.MULTILINE);

        final String message = this.showLspsByFilter(String.valueOf(lspIndex));
        final Matcher matcher = regexp.matcher(message);

        if (matcher.find()) {
            return true;
        }
        return false;
    }

    /**
     * @param lspIndex
     * @param lspDescriptor
     * @return
     * @throws GmreConnectionException
     */
    protected final boolean isLspConfigured(final int lspIndex)
            throws GmreConnectionException {
        final String message = this.showLspsByFilter(String.valueOf(lspIndex));
        final Pattern regexp =
                Pattern.compile("^" + lspIndex + ".*$", Pattern.MULTILINE);
        final Matcher matcher = regexp.matcher(message);
        if (matcher.find()) {
            return true;
        }

        return false;
    }

    public final boolean isLspConfigured(final String lspDescriptor)
            throws GmreConnectionException {
        final String message = this.showLspsByFilter(lspDescriptor);
        final Pattern regexp =
                Pattern.compile("^(\\d+) +" + lspDescriptor
                        + " +(.+)/(\\d+) +.*$", Pattern.MULTILINE);
        final Matcher matcher = regexp.matcher(message);
        if (matcher.find()) {
            return true;
        }
        return false;
    }

    /**
     * This method checks if a tna is already engaged in an lsp.
     * 
     * @param TNA
     * @return
     * @throws GmreConnectionException
     */
    public final boolean isTNAinLSP(final String TNA)
            throws GmreConnectionException {

        boolean isThere = false;
        final String cmd = "show lsp TNA " + TNA;
        String outputBuffer;

        outputBuffer = this.sendCommand(cmd);

        if (outputBuffer == null) {
            outputBuffer = this.sendCommand(cmd); // try again;
        }
        if ((outputBuffer != null)
                && !outputBuffer.contains("No LSPs were found.")) {
            final String[] arrOutBuffer =
                    outputBuffer
                            .split("---------------------------------------------------------------------------------------------------------");
            try {
                if ((arrOutBuffer != null)
                        && (arrOutBuffer[2].trim().length() > 0)) {
                    isThere = true;
                }
            } catch (final Exception ex) {
                isThere = false;
            }
        }
        return isThere;

    }

    /**
     * @param pattern
     * @return
     */
    private boolean labelFailure(final String pattern) {
        return pattern.contains("label allocation failed");
    }

    /**
     * read until pattern in telnet session.
     * 
     * @param pattern
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    private String readUntil(final String pattern) throws IOException,
            InterruptedException, GmreConnectionException {

        final char lastChar = pattern.charAt(pattern.length() - 1);
        final StringBuffer sb = new StringBuffer();

        if (this.in == null) {
            logger.debug("CLI: Variable 'in' is null!");
            throw new GmreConnectionException("CLI: Variable 'in' is null!");
        }
        int actual = this.in.read();
        char ch = (char) actual;
        while (actual != -1) {
            sb.append(ch);
            if ((ch == lastChar) && (sb.toString().endsWith(pattern))) {
                Thread.sleep(200);
                if (this.in.available() > 0) {
                    while (this.in.available() > 0) {
                        this.in.read();
                    }
                }
                return sb.toString();
            }
            actual = this.in.read();
            ch = (char) actual;
        }
        throw new GmreConnectionException("Lost connection to GMRE");
    }

    /**
     * @param pattern
     * @return
     */
    private boolean routeFailure(final String pattern) {
        return pattern.contains("route calculation failed");
    }

    /**
     * triggering a command.
     * 
     * @param command
     * @return
     * @throws GmreConnectionException
     * @throws InterruptedException
     * @throws IOException
     */
    private String sendCommand(final String command)
            throws GmreConnectionException {
        try {
            if (command != null) {

                this.write(command);
                if (!"quit".equals(command)) {
                    logger.debug("write: " + command);
                    String message = this.readUntil(GmreCli.prompt + " ");
                    message = message.replace(command, "");
                    logger.debug("read: " + message);
                    return message;
                }
                return this.readUntil("quit");
            }
            return null;
        } catch (final IOException e) {
            logger.warn(e.getMessage(), e);
            throw new GmreConnectionException(e);
        } catch (final InterruptedException e) {
            logger.warn(e.getMessage(), e);
            throw new GmreConnectionException(e);
        }
    }

    /**
     * Returns the output from "show lsp filter" issued on a GMRE CLI. As the
     * telnet client sometimes fails to get the text back, the command is send
     * twice. Therefore, beware of duplicates in the returned String object.
     * 
     * @param filter
     *                String describing the filter of the GMRE output (e.g. LSP
     *                index, LSP part of a descriptor).
     * @return Output from Telnet client.
     * @throws GmreConnectionException
     */
    public final String showLspsByFilter(final String filter)
            throws GmreConnectionException {
        String message = this.sendCommand("show lsp filter " + filter);
        final String template =
                "---------------------------------------------------------------------------------------------------------";
        // Ok, sometimes the output is foo. We call it mulitple times!
        for (int i = 0; (i < 10) && (!message.contains(template)); i++) {
            message = this.sendCommand("show lsp filter " + filter);
        }
        message = message.replaceFirst("show lsp filter " + filter, "");

        return message;
    }

    /**
     * @param lspIndex
     * @return
     * @throws GmreConnectionException
     */
    public final boolean terminateLsp(final int lspIndex,
            final String description) throws GmreConnectionException {
        this.adminDownLsp(lspIndex);
        this.deleteLsp(lspIndex);
        int tmpLspIndex = this.getLspIndexForDescriptor(description);
        while (tmpLspIndex > 0) {
            this.adminDownLsp(tmpLspIndex);
            this.deleteLsp(lspIndex);
            tmpLspIndex = this.getLspIndexForDescriptor(description);
        }
        return !this.isLspConfigured(lspIndex);
    }

    /**
     * write telnet statement.
     * 
     * @param value
     */
    private void write(final String value) {
        this.out.println(value);
        this.out.flush();
    }
}
