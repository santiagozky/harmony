package org.opennaas.extensions.gmpls.client.utils;

/**
 *
 * @author Stephan Wagner (stephan.wagner@iais.fraunhofer.de)
 *
 */
public class PathInformation {
    /** */
    private final String srcTNA, destTNA;
    /** */
    private final int lspIndex, bandwidth;

    /**
     * @param idx
     *                LSP Index
     * @param src
     *                source TNA
     * @param dest
     *                destination TNA
     * @param bw
     *                bandwidth
     */
    public PathInformation(final int idx, final String src, final String dest,
            final int bw) {
        this.bandwidth = bw;
        this.destTNA = dest;
        this.lspIndex = idx;
        this.srcTNA = src;
    }

    /**
     * @return the srcTNA
     */
    public final String getSrcTNA() {
        return this.srcTNA;
    }

    /**
     * @return the destTNA
     */
    public final String getDestTNA() {
        return this.destTNA;
    }

    /**
     * @return the lspIndex
     */
    public final int getLspIndex() {
        return this.lspIndex;
    }

    /**
     * @return the bandwidth
     */
    public final int getBandwidth() {
        return this.bandwidth;
    }

}
