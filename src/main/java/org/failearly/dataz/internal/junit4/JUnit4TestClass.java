/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2016 'Marko Umek' (http://fail-early.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.failearly.dataz.internal.junit4;

import org.failearly.dataz.internal.model.AtomicTest;
import org.failearly.dataz.internal.model.TestClassBase;
import org.junit.Ignore;
import org.junit.Test;

import java.lang.reflect.Method;

/**
 * JUnit4TestClass represents a single test class. It is responsible for collecting {@link AtomicTest}s by name.
 */
public final class JUnit4TestClass extends TestClassBase {
    private static final JUnit4TestClass PROTOTYPE = new JUnit4TestClass(null);

    private JUnit4TestClass(Class<?> testClass) {
        super(testClass);
    }

    public static TestClassBase prototype() {
        return PROTOTYPE;
    }

    @Override
    protected boolean isTestMethod(Method method) {
        return method.isAnnotationPresent(Test.class) && ! method.isAnnotationPresent(Ignore.class);
    }

    @Override
    protected TestClassBase createInstanceFrom(Class<?> testClass) {
        return new JUnit4TestClass(testClass);
    }
}
