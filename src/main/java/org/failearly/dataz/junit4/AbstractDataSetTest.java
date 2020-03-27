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
