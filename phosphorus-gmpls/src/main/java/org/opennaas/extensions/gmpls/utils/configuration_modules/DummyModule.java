/**
 * 
 */
package org.opennaas.extensions.gmpls.utils.configuration_modules;

import java.util.HashMap;
import java.util.Random;

import org.apache.log4j.Logger;

import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.PathType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.exceptions.DestinationTNAFaultException;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.exceptions.SourceTNAFaultException;
import org.opennaas.extensions.gmpls.utils.database.orm.DeviceInformation;
import org.opennaas.extensions.gmpls.utils.database.orm.LspInformation;
import org.opennaas.core.utils.Config;
import org.opennaas.core.utils.PhLogger;

/**
 * @author Stephan Wagner (wagners@cs.uni-bonn.de)
 */
public class DummyModule implements IConfigurationModule {

    private static Logger logger = PhLogger.getLogger();

    static final float errorProbability = Float.parseFloat(Config.getString(
	    "gmpls", "dummy.errorProbability"));

    static final boolean trueRandom = Boolean.parseBoolean(Config.getString(
	    "gmpls", "dummy.trueRandom"));
    static final long randomSeed = Long.parseLong(Config.getString("gmpls",
	    "dummy.randomSeed"));
    static Random random = null;
    private final HashMap<String, Boolean> usedEndpoints = new HashMap<String, Boolean>();

    public DummyModule() {
	super();
	if (null == DummyModule.random) {
	    if (DummyModule.trueRandom) {
		DummyModule.random = new Random(System.currentTimeMillis());
	    } else {
		DummyModule.random = new Random(DummyModule.randomSeed);
	    }
	    DummyModule.logger.info("Starting DummyModule with trueRandom: "
		    + DummyModule.trueRandom + " randomSeed: "
		    + DummyModule.randomSeed + " errorProbability: "
		    + DummyModule.errorProbability);
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.opennaas.extensions.gmpls.utils.configuration_modules.IConfigurationModule
     * #createPath
     * (org.opennaas.extensions.idb.serviceinterface.databinding.
     * jaxb.PathType,
     * org.opennaas.extensions.gmpls.utils.database.orm.DeviceInformation,
     * org.opennaas.extensions.gmpls.utils.database.orm.DeviceInformation)
     */
    @SuppressWarnings("unused")
    public final int createPath(final PathType path,
	    final DeviceInformation sourceDev,
	    final DeviceInformation destinationDev)
	    throws DestinationTNAFaultException, SourceTNAFaultException {
	if (this.usedEndpoints.containsKey(path.getSourceTNA())) {
	    throw new SourceTNAFaultException();
	}

	if (this.usedEndpoints.containsKey(path.getDestinationTNA())) {
	    throw new DestinationTNAFaultException();
	}
	return new Random(System.currentTimeMillis())
		.nextInt(Integer.MAX_VALUE - 1) + 1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.opennaas.extensions.gmpls.utils.configuration_modules.IConfigurationModule
     * #getStatus(org.opennaas.extensions.gmpls.utils.database.orm.LspInformation)
     */
    public final boolean getStatus(
	    @SuppressWarnings("unused") final LspInformation lspInformation) {
	if (DummyModule.random.nextFloat() > DummyModule.errorProbability) {
	    return true;
	}
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.opennaas.extensions.gmpls.utils.configuration_modules.IConfigurationModule
     * #terminatePath(org.opennaas.extensions.gmpls.utils.database.orm.LspInformation)
     */
    public final boolean terminatePath(
	    @SuppressWarnings("unused") final LspInformation lspInformation) {
	this.usedEndpoints.remove(lspInformation.getSourceDevice()
		.getTnaAddress());
	this.usedEndpoints.remove(lspInformation.getDestinationDevice()
		.getTnaAddress());
	return true;
    }
}
