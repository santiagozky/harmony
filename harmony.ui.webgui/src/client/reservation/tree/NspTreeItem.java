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


package client.reservation.tree;

import java.util.List;
import java.util.Vector;

import client.classes.nsp.DomainInformationType;
import client.classes.nsp.DomainRelationshipType;
import client.classes.nsp.GetDomainsResponseType;
import client.helper.Globals;
import client.helper.GuiLogger;
import client.helper.RpcRequest;
import client.reservation.page.NspReservationCrudPage;
import client.template.page.DefaultPage;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TreeItem;

/**
 * Class to Create Tree Items via RPC Request.
 * 
 * @author gassen
 */
public class NspTreeItem extends TreeItem implements AsyncCallback {

    private Vector parentDomains = new Vector();

    private String relationship = null;

    /**
     * Constructor. Create root item and create RPC request.
     * 
     * @param name
     *            the items' name
     */
    public NspTreeItem(final String name, final String relationship) {
        super(name);
        this.relationship = relationship;
        RpcRequest.nsp().getDomains(this);
    }

    /**
     * Constructor. Create root item and create RPC request.
     * 
     * @param epr
     *            Endpointreference
     * @param name
     *            the items' name
     */
    public NspTreeItem(final String epr, final String name,
            final String relationship, final Vector parents) {
        super(name);
        this.relationship = relationship;
        this.parentDomains = parents;

        RpcRequest.nsp().getDomains(epr, this);
    }

    /**
     * Action invoked after a failure during getting the domains.
     * 
     * @param caught
     *            the Exception
     */
    public final void onFailure(final Throwable caught) {
        GuiLogger.errorLog(caught.getMessage());

        this.setUserObject(new DefaultPage(this.getHTML()
                + " is temporary not available", "Please try again later"));

        this.setHTML("<i>" + this.getHTML() + "</i>");
    }

    /**
     * Action invoked after successfully receiving the domains. Creates the Tree
     * Items and the according Pages.
     * 
     * @param result
     *            the DomainList
     */
    public final void onSuccess(final Object result) {
        final GetDomainsResponseType resultList =
                (GetDomainsResponseType) result;
        final List list = resultList.getDomains();

        final Vector peerParents = (Vector) this.parentDomains.clone();

        // take care that the "own" domain (if contained in the list) is
        // processed before the subdomains
        int subdomainCount = 0;
        for (int x = 0; x < list.size() - subdomainCount; x++) {
            final DomainInformationType domain =
                    (DomainInformationType) list.get(x);
            final DomainRelationshipType reltype = domain.getRelationship();
            String rel = null;
            if (reltype != null) {
                rel = reltype.value();
            }
            final boolean isSubdomain = "subdomain".equals(rel);
            if (isSubdomain) {
                subdomainCount++;
                list.remove(x--);
                list.add(domain);
            }
        }

        for (int x = 0; x < list.size(); x++) {
            final DomainInformationType domain =
                    (DomainInformationType) list.get(x);
            final DomainRelationshipType reltype = domain.getRelationship();
            String rel = null;
            if (reltype != null) {
                rel = reltype.value();
            }

            if (null == domain) {
                GuiLogger.errorLog("DomainInformationType is null");
            } else {
                GuiLogger.debugLog("Creating TreeItem: " + domain.getDomainId()
                        + " - " + domain.getReservationEPR());
            }

            final boolean isSubdomain = "subdomain".equals(rel);
            final boolean isPeer = "peer".equals(this.relationship);

            if ((!isPeer) || isSubdomain) {
                Globals.addDomain(domain);

                final Vector parents =
                        (Vector) (isPeer ? peerParents : this.parentDomains)
                                .clone();
                parents.add(domain);

                final String topologyEPR = domain.getTopologyEPR();

                if (rel != null) {
                    TreeItem item = null;
                    if ((null != topologyEPR) && !"".equals(topologyEPR)) {
                        item =
                                new NspTreeItem(topologyEPR, domain
                                        .getDomainId(), rel, parents);
                    } else {
                        item = new TreeItem(domain.getDomainId());
                    }

                    item.setUserObject(new NspReservationCrudPage(domain,
                            parents));

                    if (isSubdomain) {
                        this.addItem(item);
                    } else {
                        this.getParentItem().addItem(item);
                    }
                } else {
                    this.setHTML(domain.getDomainId());
                    this.setUserObject(new NspReservationCrudPage(domain,
                            parents));
                    this.parentDomains = parents;
                }

            }
        }
    }

}
