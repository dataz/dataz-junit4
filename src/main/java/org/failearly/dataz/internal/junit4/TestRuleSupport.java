/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright 2014-2017 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.failearly.dataz.internal.junit4;

import org.failearly.dataz.internal.common.classutils.ObjectCreatorUtil;
import org.junit.Ignore;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

/**
 * ClassRuleEmulationSupport is responsible for creating a (single) instance per test class and
 * resolving the actually number of tests.
 */
@SuppressWarnings("WeakerAccess")
public final class TestRuleSupport<T extends TestRuleBase> {
    private final Class<T> testRuleClass;
    private final Map<Class<?>, T> testClasses = new ConcurrentHashMap<>();

    public TestRuleSupport(Class<T> testRuleClass) {
        this.testRuleClass = testRuleClass;
    }

    public T createTestRule(Class<?> testClass) {
        return createTestRule(testClass, null);
    }

    public T createTestRule(Class<?> testClass, Object context) {
        return createTestRule(testClass, context, (cls) -> true);
    }

    public T createTestRule(Class<?> testClass, Object context, Predicate<Method> predicate) {
        return testClasses.computeIfAbsent(testClass, (cls) -> createInstance(cls, context, predicate));
    }

    public void cleanInstances() {
        testClasses.clear();
    }

    /**
     * To be called by implementation of {@link TestRuleBase#dropTestClass(Class)}.
     *
     * @param testClass the test class.
     */
    public void dropInstance(Class<?> testClass) {
        testClasses.remove(testClass);
    }

    @SuppressWarnings("unchecked")
    private T createInstance(Class<?> testClass, Object context, Predicate<Method> predicate) {
        final T instance = ObjectCreatorUtil.createInstanceByConstructor(testRuleClass);

        instance.init(testClass, context, resolveNumberOfTests(testClass, predicate));

        return instance;
    }

    private static int resolveNumberOfTests(Class<?> testClass, Predicate<Method> predicate) {
        if (!testClass.isAnnotationPresent(Ignore.class)) {
            return (int) Arrays.asList(testClass.getMethods()).stream()
                    .filter(TestRuleSupport::isValidJUnit4Method)
                    .filter(predicate)
                    .count();
        }
        return 0;
    }

    private static boolean isValidJUnit4Method(Method method) {
        return method.isAnnotationPresent(Test.class)
                && !method.isAnnotationPresent(Ignore.class)
                && method.getParameterCount() == 0
                && method.getReturnType().equals(void.class)
                && 0 != (method.getModifiers() & Modifier.PUBLIC);
    }

}
