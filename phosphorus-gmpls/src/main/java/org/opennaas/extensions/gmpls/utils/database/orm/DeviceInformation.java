package org.opennaas.extensions.gmpls.utils.database.orm;

/**
 * @author Stephan Wagner(stephan.wagner@iais.fraunhofer.de)
 */
public class DeviceInformation {

    /** */
    private String tnaAddress;
    /** */
    private String module;
    /** */
    private String ipAddress;
    /** */
    private String deviceName;
    /** */
    private String deviceType;
    /** */
    private String vendorName;
    /** */
    private String model;
    /** */
    private String softwarerelease;
    /** */
    private String databearer;

    /**
     * @return the tnaAddress
     */
    public final String getTnaAddress() {
        return this.tnaAddress;
    }

    /**
     * @param tnaAddress
     *                the tnaAddress to set
     */
    public final void setTnaAddress(final String tnaAddress) {
        this.tnaAddress = tnaAddress;
    }

    /**
     * @return the ipAddress
     */
    public final String getIpAddress() {
        return this.ipAddress;
    }

    /**
     * @param ipAddress
     *                the ipAddress to set
     */
    public final void setIpAddress(final String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * @return the deviceName
     */
    public final String getDeviceName() {
        return this.deviceName;
    }

    /**
     * @param deviceName
     *                the deviceName to set
     */
    public final void setDeviceName(final String deviceName) {
        this.deviceName = deviceName;
    }

    /**
     * @return the deviceType
     */
    public final String getDeviceType() {
        return this.deviceType;
    }

    /**
     * @param deviceType
     *                the deviceType to set
     */
    public final void setDeviceType(final String deviceType) {
        this.deviceType = deviceType;
    }

    /**
     * @return the vendorName
     */
    public final String getVendorName() {
        return this.vendorName;
    }

    /**
     * @param vendorName
     *                the vendorName to set
     */
    public final void setVendorName(final String vendorName) {
        this.vendorName = vendorName;
    }

    /**
     * @return the model
     */
    public final String getModel() {
        return this.model;
    }

    /**
     * @param model
     *                the model to set
     */
    public final void setModel(final String model) {
        this.model = model;
    }

    /**
     * @return the softwarerelease
     */
    public final String getSoftwarerelease() {
        return this.softwarerelease;
    }

    /**
     * @param softwarerelease
     *                the softwarerelease to set
     */
    public final void setSoftwarerelease(final String softwarerelease) {
        this.softwarerelease = softwarerelease;
    }

    /**
     * @return the module
     */
    public final String getModule() {
        return this.module;
    }

    /**
     * @param module
     *                the module to set
     */
    public final void setModule(final String module) {
        this.module = module;
    }

    /**
     * @return the databearer
     */
    public final String getDatabearer() {
        return this.databearer;
    }

    /**
     * @param databearer the databearer to set
     */
    public final void setDatabearer(final String databearer) {
        this.databearer = databearer;
    }

}
