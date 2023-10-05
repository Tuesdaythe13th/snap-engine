/*
 * Copyright (C) 2010 Brockmann Consult GmbH (info@brockmann-consult.de)
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option)
 * any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, see http://www.gnu.org/licenses/
 */

package org.esa.snap.core.dataio.dimap.spi;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class DimapPersistableRegistryTest {

    private final static Class[] EXPECTED_SPIS = new Class[]{
            GeneralFilterBandPersistableSpi.class,
            ConvolutionFilterBandPersistableSpi.class
    };

    private static void assertIteratorContainsInstances(Iterator<DimapPersistableSpi> iterator, Class[] expectedClasses) {
        final List<DimapPersistableSpi> list = copyToList(iterator);

        for (final Class expectedClass : expectedClasses) {
            boolean found = false;
            for (final Object instance : list) {
                if (expectedClass.isInstance(instance)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                fail("No instance found of '" + expectedClass.toString() + "' in given iterator");
            }
        }
    }

    private static List<DimapPersistableSpi> copyToList(Iterator<DimapPersistableSpi> iterator) {
        final ArrayList<DimapPersistableSpi> list = new ArrayList<DimapPersistableSpi>();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }

    @Test
    public void testDimapPersistableRegistryCreation() {
        assertNotNull(DimapPersistableSpiRegistry.getInstance());
    }

    @Test
    public void testGetStandardSpis() {
        final DimapPersistableSpiRegistry registry = DimapPersistableSpiRegistry.getInstance();

        final Iterator<DimapPersistableSpi> persistableSpis = registry.getPersistableSpis();
        assertNotNull(persistableSpis);
        assertIteratorContainsInstances(persistableSpis, EXPECTED_SPIS);
    }
}
