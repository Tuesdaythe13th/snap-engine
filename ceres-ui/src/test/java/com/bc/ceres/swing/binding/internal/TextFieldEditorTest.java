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
package com.bc.ceres.swing.binding.internal;

import com.bc.ceres.binding.PropertyContainer;
import com.bc.ceres.binding.PropertyDescriptor;
import com.bc.ceres.swing.binding.BindingContext;
import org.junit.Test;

import javax.swing.*;

import static org.junit.Assert.*;

public class TextFieldEditorTest {

    @Test
    public void testIsApplicable() {
        TextFieldEditor textEditor = new TextFieldEditor();

        PropertyDescriptor textDescriptor = new PropertyDescriptor("test", String.class);
        assertFalse(textEditor.isValidFor(textDescriptor));
        // TextFieldEditor returns always false, because it is the default !!!

        PropertyDescriptor booleanDescriptor = new PropertyDescriptor("test", Boolean.TYPE);
        assertFalse(textEditor.isValidFor(booleanDescriptor));
    }

    @Test
    public void testCreateEditorComponent() {
        TextFieldEditor textEditor = new TextFieldEditor();

        PropertyContainer propertyContainer = PropertyContainer.createValueBacked(V.class);
        BindingContext bindingContext = new BindingContext(propertyContainer);
        PropertyDescriptor propertyDescriptor = propertyContainer.getDescriptor("value");
        assertSame(String.class, propertyDescriptor.getType());

        JComponent editorComponent = textEditor.createEditorComponent(propertyDescriptor, bindingContext);
        assertNotNull(editorComponent);
        assertSame(JTextField.class, editorComponent.getClass());

        JComponent[] components = bindingContext.getBinding("value").getComponents();
        assertEquals(1, components.length);
        assertSame(JTextField.class, components[0].getClass());
    }

    private static class V {
        String value;
    }
}
