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

import org.junit.Rule;
import org.junit.rules.TestRule;

/**
 * Extending AbstractDataSetTest is a simple way to use <i>dataSet</i>.
 */
public abstract class AbstractDataSetTest {

    /**
     * THE DataSet driver.
     */
    @Rule
    public final TestRule dataSetDriver = DataSetDriver.createDataSetDriver(this);
}
