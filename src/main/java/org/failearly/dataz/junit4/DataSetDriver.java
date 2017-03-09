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
