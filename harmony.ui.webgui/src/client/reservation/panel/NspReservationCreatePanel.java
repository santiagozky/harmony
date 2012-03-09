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
package client.reservation.panel;

import client.classes.nsp.CreateReservationResponseType;
import client.classes.nsp.CreateReservationType;
import client.reservation.listener.CreateClickListener;
import client.template.dialog.ErrorDialog;
import client.template.dialog.InfoDialog;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author gassen
 * 
 */
public abstract class NspReservationCreatePanel extends VerticalPanel implements
        AsyncCallback {

    private boolean isArgon = false;
    
    /**
     * 
     */
    protected final DateTimeFormat dateTimeFormat =
            DateTimeFormat.getFormat("MM/dd/yy HH:mm:ss ZZZZ");

    /**
     * 
     */
    protected final TextBox dst = new TextBox();
    /**
     * 
     */
    protected final TextBox src = new TextBox();
    /**
     * 
     */
    private final Button button = new Button();

    /**
     * 
     */
    private final Image activity = new Image("ajaxloader.gif");
    /**
     * 
     */
    protected final FlexTable table = new FlexTable();
    /**
     * 
     */
    private final TextBox delay = new TextBox();
    /**
     * 
     */
    private final TextBox minBW = new TextBox();
    /**
     * 
     */
    private final TextBox maxBW = new TextBox();

    /**
     * @return the delay
     */
    public TextBox getDelay() {
        return this.delay;
    }

    /**
     * @return the minBW
     */
    public TextBox getMinBW() {
        return this.minBW;
    }

    /**
     * @return the maxBW
     */
    public TextBox getMaxBW() {
        return this.maxBW;
    }

    /**
     * 
     */
    protected final String epr;

    /**
     * 
     */
    public NspReservationCreatePanel(final String epr) {
        this.add(this.table);

        this.epr = epr;

        this.activity.setVisible(false);

        final Label labelSrc = new Label();
        labelSrc.setText("Src: ");
        this.table.setWidget(0, 0, labelSrc);
        this.table.setWidget(0, 1, this.src);

        final Label labelDst = new Label();
        labelDst.setText("Dst: ");
        this.table.setWidget(0, 2, labelDst);
        this.table.setWidget(0, 3, this.dst);

        final HorizontalPanel buttonPanel = new HorizontalPanel();
        this.button.addClickListener(this.getClickListener());
        this.button.setText("Create");
        buttonPanel.add(this.button);
        buttonPanel.add(this.activity);

        final Label labelBw = new Label();
        labelBw.setText("minBW: ");
        this.minBW.setText(800 + "");
        this.table.setWidget(1, 0, labelBw);
        this.table.setWidget(1, 1, this.minBW);

        final Label labelMaxBw = new Label();
        labelMaxBw.setText("maxBW (leave 0 for unset): ");
        this.maxBW.setText(800 + "");
        this.table.setWidget(1, 2, labelMaxBw);
        this.table.setWidget(1, 3, this.maxBW);

        final Label labelDelay = new Label();
        labelDelay.setText("Delay: ");
        this.delay.setText(8000 + "");
        this.table.setWidget(1, 4, labelDelay);
        this.table.setWidget(1, 5, this.delay);

        this.add(buttonPanel);
    }

    protected abstract CreateClickListener getClickListener();

    /**
     * @param reservation
     */
    public abstract void sendRequest(CreateReservationType reservation);

    /**
     * @param reservation
     */
    public abstract void update();

    /**
     * @return the dst
     */
    public final TextBox getDst() {
        return this.dst;
    }

    /**
     * @return the src
     */
    public final TextBox getSrc() {
        return this.src;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.google.gwt.user.client.rpc.AsyncCallback#onFailure(java.lang.Throwable
     * )
     */
    public void onFailure(final Throwable caught) {
        this.activity.setVisible(false);
        this.button.setEnabled(true);

        final ErrorDialog dialog = new ErrorDialog("Failure", caught);

        dialog.show();
        dialog.center();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.google.gwt.user.client.rpc.AsyncCallback#onSuccess(java.lang.Object)
     */
    public void onSuccess(final Object result) {
        this.activity.setVisible(false);
        this.button.setEnabled(true);

        final CreateReservationResponseType response =
                (CreateReservationResponseType) result;

        String message =
                "Reservation " + response.getReservationID() + " created<br>";

        if (null != response.getToken()) {
            message += "<br><b>Token:</b> " + response.getToken();
        }
        
        final InfoDialog dialog =
                new InfoDialog("Create Reservation", new HTML(message));

        dialog.show();
        dialog.center();
    }

    /**
     * @return the dateTimeFormat
     */
    public final DateTimeFormat getDateTimeFormat() {
        return this.dateTimeFormat;
    }

    /**
     * 
     */
    public final void setActive() {
        this.activity.setVisible(true);
        this.button.setEnabled(false);
    }

    /**
     * 
     */
    public final void setDone() {
        this.activity.setVisible(false);
        this.button.setEnabled(true);
    }

    /**
     * @return the activity
     */
    public final Image getActivity() {
        return this.activity;
    }

    /**
     * @return the isArgon
     */
    public final boolean isArgon() {
        return isArgon;
    }

    /**
     * @param isArgon the isArgon to set
     */
    public final void setArgon(boolean isArgon) {
        this.isArgon = isArgon;
    }
}
