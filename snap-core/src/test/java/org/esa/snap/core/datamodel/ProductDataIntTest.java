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
package org.esa.snap.core.datamodel;

import org.esa.snap.GlobalTestConfig;
import org.esa.snap.core.util.io.FileUtils;
import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class ProductDataIntTest {

    private FileImageInputStream _inputStream;
    private FileImageOutputStream _outputStream;

    @Before
    public void setUp() throws IOException {
        File outputDir = GlobalTestConfig.getSnapTestDataOutputFile("ProductData");
        Assume.assumeTrue(outputDir.mkdirs() || outputDir.exists());
        File streamFile = new File(outputDir, "int.img");
        Assume.assumeTrue(streamFile.createNewFile() || streamFile.exists());
        _inputStream = new FileImageInputStream(streamFile);
        _outputStream = new FileImageOutputStream(streamFile);
        assertNotNull(_inputStream);
        assertNotNull(_outputStream);
    }

    @After
    public void tearDown() {
        try {
            _inputStream.close();
            _outputStream.close();
        } catch (IOException ignored) {
        }
        FileUtils.deleteTree(GlobalTestConfig.getSnapTestDataOutputDirectory());
    }

    @Test
    public void testSingleValueConstructor() {
        ProductData instance = ProductData.createInstance(ProductData.TYPE_INT32);
        instance.setElems(new int[]{2147483647});

        assertEquals(ProductData.TYPE_INT32, instance.getType());
        assertEquals(2147483647, instance.getElemInt());
        assertEquals(2147483647L, instance.getElemUInt());
        assertEquals(2147483647.0F, instance.getElemFloat(), 0.0e-12F);
        assertEquals(2147483647.0D, instance.getElemDouble(), 0.0e-12D);
        assertEquals("2147483647", instance.getElemString());
        assertTrue(instance.getElemBoolean());
        assertEquals(1, instance.getNumElems());
        Object data = instance.getElems();
        assertTrue(data instanceof int[]);
        assertEquals(1, ((int[]) data).length);
        assertTrue(instance.isScalar());
        assertTrue(instance.isInt());
        assertEquals("2147483647", instance.toString());

        ProductData expectedEqual = ProductData.createInstance(ProductData.TYPE_INT32);
        expectedEqual.setElems(new int[]{2147483647});
        assertTrue(instance.equalElems(expectedEqual));

        ProductData expectedUnequal = ProductData.createInstance(ProductData.TYPE_INT32);
        expectedUnequal.setElems(new int[]{2147483646});
        assertFalse(instance.equalElems(expectedUnequal));

//        StreamTest
        ProductData dataFromStream = null;
        try {
            instance.writeTo(_outputStream);
            dataFromStream = ProductData.createInstance(ProductData.TYPE_INT32);
            dataFromStream.readFrom(_inputStream);
        } catch (IOException e) {
            fail("IOException not expected");
        }
        assertTrue(instance.equalElems(dataFromStream));
    }

    @Test
    public void testConstructor() {
        ProductData instance = ProductData.createInstance(ProductData.TYPE_INT32, 3);
        instance.setElems(new int[]{-1, 2147483647, -2147483648});

        assertEquals(ProductData.TYPE_INT32, instance.getType());
        assertEquals(-1, instance.getElemIntAt(0));
        assertEquals(2147483647, instance.getElemIntAt(1));
        assertEquals(-2147483648, instance.getElemIntAt(2));
        assertEquals(-1L, instance.getElemUIntAt(0));
        assertEquals(2147483647L, instance.getElemUIntAt(1));
        assertEquals(-2147483648L, instance.getElemUIntAt(2));
        assertEquals(-1.0F, instance.getElemFloatAt(0), 0.0e-12F);
        assertEquals(2147483647.0F, instance.getElemFloatAt(1), 0.0e-12F);
        assertEquals(-2147483648.0F, instance.getElemFloatAt(2), 0.0e-12F);
        assertEquals(-1.0D, instance.getElemDoubleAt(0), 0.0e-12D);
        assertEquals(2147483647.0D, instance.getElemDoubleAt(1), 0.0e-12D);
        assertEquals(-2147483648.0D, instance.getElemDoubleAt(2), 0.0e-12D);
        assertEquals("-1", instance.getElemStringAt(0));
        assertEquals("2147483647", instance.getElemStringAt(1));
        assertEquals("-2147483648", instance.getElemStringAt(2));
        assertTrue(instance.getElemBooleanAt(0));
        assertTrue(instance.getElemBooleanAt(1));
        assertTrue(instance.getElemBooleanAt(2));
        assertEquals(3, instance.getNumElems());
        Object data2 = instance.getElems();
        assertTrue(data2 instanceof int[]);
        assertEquals(3, ((int[]) data2).length);
        assertFalse(instance.isScalar());
        assertTrue(instance.isInt());
        assertEquals("-1,2147483647,-2147483648", instance.toString());

        ProductData expectedEqual = ProductData.createInstance(ProductData.TYPE_INT32, 3);
        expectedEqual.setElems(new int[]{-1, 2147483647, -2147483648});
        assertTrue(instance.equalElems(expectedEqual));

        ProductData expectedUnequal = ProductData.createInstance(ProductData.TYPE_INT32, 3);
        expectedUnequal.setElems(new int[]{-1, 2147483647, -2147483647});
        assertFalse(instance.equalElems(expectedUnequal));

//        StreamTest
        ProductData dataFromStream = null;
        try {
            instance.writeTo(_outputStream);
            dataFromStream = ProductData.createInstance(ProductData.TYPE_INT32, 3);
            dataFromStream.readFrom(_inputStream);
        } catch (IOException e) {
            fail("IOException not expected");
        }
        assertTrue(instance.equalElems(dataFromStream));
    }

    @Test
    public void testSetElemsAsString() {
        final ProductData pd = ProductData.createInstance(ProductData.TYPE_INT32, 3);
        pd.setElems(new String[]{
                String.valueOf(Integer.MAX_VALUE),
                String.valueOf(Integer.MIN_VALUE),
                String.valueOf(0),
        });

        assertEquals(Integer.MAX_VALUE, pd.getElemIntAt(0));
        assertEquals(Integer.MIN_VALUE, pd.getElemIntAt(1));
        assertEquals(0, pd.getElemIntAt(2));
    }

    @Test
    public void testSetElemsAsString_OutOfRange() {
        final ProductData pd1 = ProductData.createInstance(ProductData.TYPE_INT32, 1);
        try {
            pd1.setElems(new String[]{String.valueOf((long) Integer.MAX_VALUE + 1)});
        } catch (Exception e) {
            assertEquals(NumberFormatException.class, e.getClass());
            assertTrue(e.getMessage().contains("2147483648"));
        }

        final ProductData pd2 = ProductData.createInstance(ProductData.TYPE_INT32, 1);
        try {
            pd2.setElems(new String[]{String.valueOf((long) Integer.MIN_VALUE - 1)});
        } catch (Exception e) {
            assertEquals(NumberFormatException.class, e.getClass());
            assertTrue(e.getMessage().contains("-2147483649"));
        }
    }
}
