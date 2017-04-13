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

/**
* CRE is a Class Rule Emulation test rule.
*/
final class FakeTestRule extends TestRuleBase<Object> {

    private static TestRuleSupport<FakeTestRule> support=new TestRuleSupport<>(FakeTestRule.class);

    int countDrop=0;
    int countInit=0;
    int countBefore =0;
    int countAfter =0;

    private FakeTestRule() {
    }

    static FakeTestRule create(Object testInstance) {
        return support.createTestRule(testInstance.getClass());
    }

    static FakeTestRule cre(Class<?> testClass) {
        return support.createTestRule(testClass);
    }


    static void reset() {
        support = new TestRuleSupport<>(FakeTestRule.class);
    }

    @Override
    protected void beforeTest(Object context) {
        countBefore++;
    }

    @Override
    protected void afterTest(Object context) {
        countAfter++;
    }

    @Override
    protected void initialize(Class<?> testClass, Object context) {
        countInit++;
    }

    @Override
    protected void dropTestClass(Class<?> testClass) {
        countDrop++;
    }
}
