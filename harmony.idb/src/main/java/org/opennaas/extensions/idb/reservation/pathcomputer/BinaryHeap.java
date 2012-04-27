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

// implement as min-heap
public class BinaryHeap implements PriorityQueue {

    private int currentSize; // Number of elements in heap
    private HeapElement[] array; // The heap array

    /**
     * Construct the binary heap.
     */
    public BinaryHeap() {
        this.currentSize = 0;
        this.array = new HeapElement[1];
    }

    /**
     * Construct the binary heap from an array.
     * 
     * @param items
     *            the inital items in the binary heap.
     */
    public BinaryHeap(final HeapElement[] items) {
        this.currentSize = items.length;
        this.array = new HeapElement[items.length + 1];

        for (int i = 0; i < items.length; i++) {
            this.array[i + 1] = items[i];
        }
        this.buildHeap();
    }

    /**
     * Establish heap order property from an arbitrary arrangement of items.
     * Runs in linear time.
     */
    private void buildHeap() {
        for (int i = this.currentSize / 2; i > 0; i--) {
            this.percolateDown(i);
        }
    }

    /**
     * @throws UnsupportedOperationException
     *             because no Positions are returned by the insert method for
     *             BinaryHeap.
     */
    public void decreaseKey(int i, final int newValue) {
        if (this.array[i].getValue() < newValue) {
            System.out.println("\nBinaryHeap(decreaseKey) ignore: i=" + i
                    + ", newValue=" + newValue);
            return;
        }
        this.array[i].setValue(newValue);
        while ((i > 1)
                && (this.array[i / 2].getValue() > this.array[i].getValue())) {
            final HeapElement tmp = this.array[i];
            this.array[i] = this.array[i / 2];
            this.array[i / 2] = tmp;
            i = i / 2;
        }
    }

    /**
     * Internal method to extend array.
     */
    private void doubleArray() {
        HeapElement[] newArray;

        newArray = new HeapElement[this.array.length * 2];
        for (int i = 0; i < this.array.length; i++) {
            newArray[i] = this.array[i];
        }
        this.array = newArray;
    }

    /**
     * Remove the smallest item from the priority queue.
     * 
     * @return the smallest item.
     */
    public HeapElement extractMin() {
        final HeapElement minItem = this.findMin();
        this.array[1] = this.array[this.currentSize--];
        this.percolateDown(1);

        return minItem;
    }

    /**
     * Find the smallest item in the priority queue.
     * 
     * @return the smallest item.
     * @throws RuntimeException
     *             if empty.
     */
    public HeapElement findMin() {
        if (this.isEmpty()) {
            throw new RuntimeException("Empty binary heap");
        }
        return this.array[1];
    }

    public int getIndex(final HeapElement x) {
        for (int i = 1; i <= this.currentSize; i++) {
            if (this.array[i].isEquals(x)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Insert into the priority queue. Duplicates are allowed.
     * 
     * @param x
     *            the item to insert.
     * @return null, signifying that decreaseKey cannot be used.
     */
    public void insert(final HeapElement x) {
        if (this.currentSize + 1 == this.array.length) {
            this.doubleArray();
        }

        this.currentSize++;
        this.array[this.currentSize] = x;
        this.decreaseKey(this.currentSize, x.getValue());

        /*
         * array[ 0 ] = x;
         * 
         * for( ; x.compareTo( array[ hole / 2 ] ) < 0; hole /= 2 ) array[ hole ] =
         * array[ hole / 2 ]; array[ hole ] = x;
         * 
         * return null;
         */
    }

    /**
     * Test if the priority queue is logically empty.
     * 
     * @return true if empty, false otherwise.
     */
    public boolean isEmpty() {
        return this.currentSize == 0;
    }

    /**
     * Make the priority queue logically empty.
     */
    public void makeEmpty() {
        this.currentSize = 0;
    }

    /**
     * Internal method to percolate down in the heap.
     * 
     * @param hole
     *            the index at which the percolate begins.
     */
    private void percolateDown(final int i) {
        final int left = 2 * i;
        final int right = left + 1;
        int smallest = 0;

        if ((left <= this.currentSize)
                && (this.array[left].getValue() < this.array[i].getValue())) {
            smallest = left;
        } else {
            smallest = i;
        }
        if ((right <= this.currentSize)
                && (this.array[right].getValue() < this.array[smallest]
                        .getValue())) {
            smallest = right;
        }
        if (smallest != i) {
            final HeapElement tmp = this.array[i];
            this.array[i] = this.array[smallest];
            this.array[smallest] = tmp;
            this.percolateDown(smallest);
        }

        /*
         * int child; HeapElement tmp = array[ hole ];
         * 
         * for( ; hole * 2 <= currentSize; hole = child ) { child = hole * 2;
         * if( child != currentSize && array[ child + 1 ].compareTo( array[
         * child ] ) < 0 ) child++; if( array[ child ].compareTo( tmp ) < 0 )
         * array[ hole ] = array[ child ]; else break; } array[ hole ] = tmp;
         */
    }

    /**
     * Returns size.
     * 
     * @return current size.
     */
    public int size() {
        return this.currentSize;
    }

}
