package org.opennaas.extensions.gmpls.utils.database.orm;

/**
 * @author Stephan Wagner(stephan.wagner@iais.fraunhofer.de)
 */
public class LspInformation {

    /** */
    private int pathId;
    /** */
    private int lspIndex;
    /** */
    private int bandwidth;
    /** */
    private String lspDescriptor;
    /** */
    private DeviceInformation sourceDevice;
    /** */
    private DeviceInformation destinationDevice;

    /**
     * @return the lspId
     */
    public final int getPathId() {
        return this.pathId;
    }

    /**
     * @param pathId
     *                the lspId to set
     */
    public final void setPathId(final int pathId) {
        this.pathId = pathId;
    }

    /**
     * @return the lspIndex
     */
    public final int getLspIndex() {
        return this.lspIndex;
    }

    /**
     * @param lspIndex
     *                the lspIndex to set
     */
    public final void setLspIndex(final int lspIndex) {
        this.lspIndex = lspIndex;
    }

    /**
     * @return the lspDescriptor
     */
    public final String getLspDescriptor() {
        return this.lspDescriptor;
    }

    /**
     * @param lspDescriptor
     *                the lspDescriptor to set
     */
    public final void setLspDescriptor(final String lspDescriptor) {
        this.lspDescriptor = lspDescriptor;
    }

    /**
     * @return the sourceDevice
     */
    public final DeviceInformation getSourceDevice() {
        return this.sourceDevice;
    }

    /**
     * @param sourceDevice
     *                the sourceDevice to set
     */
    public final void setSourceDevice(final DeviceInformation sourceDevice) {
        this.sourceDevice = sourceDevice;
    }

    /**
     * @return the destinationDevice
     */
    public final DeviceInformation getDestinationDevice() {
        return this.destinationDevice;
    }

    /**
     * @param destinationDevice
     *                the destinationDevice to set
     */
    public final void setDestinationDevice(
            final DeviceInformation destinationDevice) {
        this.destinationDevice = destinationDevice;
    }

    /**
     * @return the bandwidth
     */
    public final int getBandwidth() {
        return this.bandwidth;
    }

    /**
     * @param bandwidth
     *                the bandwidth to set
     */
    public final void setBandwidth(final int bandwidth) {
        this.bandwidth = bandwidth;
    }

}
