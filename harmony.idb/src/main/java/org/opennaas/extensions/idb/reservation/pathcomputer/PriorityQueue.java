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


package org.opennaas.extensions.idb.reservation.pathcomputer;

/**
 * PriorityQueue interface.
 */
public interface PriorityQueue {

    /**
     * Change the value of the item stored in the pairing heap.
     * 
     * @param p
     *            any non-null position.
     * @param newVal
     *            the new value, which must be smaller than the currently stored
     *            value.
     * @throws IllegalArgumentException
     *             if p invalid.
     */
    void decreaseKey(int p, int newVal);

    /**
     * Remove the smallest item from the priority queue.
     * 
     * @return the smallest item.
     */
    HeapElement extractMin();

    /**
     * Find the smallest item in the priority queue.
     * 
     * @return the smallest item.
     */
    HeapElement findMin();

    int getIndex(HeapElement x);

    /**
     * Insert into the priority queue, maintaining heap order. Duplicates are
     * allowed.
     * 
     * @param x
     *            the item to insert.
     * @return may return a Position useful for decreaseKey.
     */
    void insert(HeapElement x);

    /**
     * Test if the priority queue is logically empty.
     * 
     * @return true if empty, false otherwise.
     */
    boolean isEmpty();

    /**
     * Make the priority queue logically empty.
     */
    void makeEmpty();

    /**
     * Returns the size.
     * 
     * @return current size.
     */
    int size();
}
