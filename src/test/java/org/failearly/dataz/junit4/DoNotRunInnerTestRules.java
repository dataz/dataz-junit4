/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2017 'Marko Umek' (http://fail-early.com)
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
