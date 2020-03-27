/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright 2014-2020 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package org.failearly.dataz.junit4;

import org.failearly.dataz.internal.junit4.JUnit4TestClass;
import org.failearly.dataz.internal.model.AtomicTest;
import org.failearly.dataz.internal.model.TestClass;
import org.failearly.dataz.internal.model.TestClassCollection;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * DataSetDriver creates JUnit's {@link TestRule} which does the work.
 *
 * '''java
 * public class MyTest {
 *
 * }
 *
 * '''
 */
@SuppressWarnings("WeakerAccess")
public final class DataSetDriver {

    private static TestClassCollection testClassCollection=new TestClassCollection(JUnit4TestClass.prototype());

    private DataSetDriver() {
    }

    /**
     * Create a {@link DataSetDriver} from current test instance.
     *
     * @param testInstance {@code this}
     * @return a new driver instance.
     */
    public static TestRule createDataSetDriver(Object testInstance) {
        return new DataSetRule(testClassCollection.createOrFetchTestClass(testInstance.getClass()));
    }

    private static class DataSetRule implements TestRule {
        private final TestClass testClass;

        DataSetRule(TestClass testClass) {
            this.testClass = testClass;
        }

        @Override
        public Statement apply(Statement statement, Description description) {
            return new Statement() {
                @Override
                public void evaluate() throws Throwable {
                    final AtomicTest atomicTest = testClass.getAtomicTest(description.getMethodName());
                    atomicTest.setup();
                    try {
                        statement.evaluate();
                    } finally {
                        if( ! atomicTest.isSuppressCleanup() )
                            atomicTest.cleanup();
                    }
                }
            };
        }

    }
}
