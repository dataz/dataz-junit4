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
package org.failearly.dataz.junit4;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * DoNotRunInnerTestRule is responsible for ...
 */
public class DoNotRunInnerTestRules {
    private static final ThreadLocal<Boolean> outer = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };

    public static TestRule createOuterRule() {
        return (Statement statement, Description description) -> new OuterStatement(statement);
    }

    public static TestRule createInnerRule(TestRule testRule)  {
        if(outer.get()) {
            return testRule;
        }
        return (Statement statement, Description description) -> NullStatement.INSTANCE;
    }



    private static class NullStatement extends Statement {
        public static final Statement INSTANCE = new NullStatement();

        @Override
        public void evaluate() throws Throwable {
            if( outer.get() )
                throw new IllegalStateException("Do not execute as outer test class");
        }
    }

    private static class OuterStatement extends Statement {
        private final Statement statement;

        OuterStatement(Statement statement) {
            this.statement = statement;
        }

        @Override
        public void evaluate() throws Throwable {
            outer.set(true);
            try {
                statement.evaluate();
            } finally {
                outer.set(false);
            }
        }
    }
}
