/*******************************************************************************
 * Copyright (c) 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.ibm.ws.cdi12.fat.tests;

import org.junit.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ibm.ws.cdi12.suite.ShutDownSharedServer;
import com.ibm.ws.fat.util.LoggingTest;

import componenttest.topology.impl.LibertyServer;
import componenttest.topology.impl.LibertyServerFactory;
import componenttest.topology.utils.HttpUtils;

public class AppExtensionTest extends LoggingTest {

    private static LibertyServer server;

    @Override
    protected ShutDownSharedServer getSharedServer() {
        return null;
    }

    @BeforeClass
    public static void setUp() throws Exception {
        server = LibertyServerFactory.getStartedLibertyServer("cdi12AppExtensionServer");
        server.waitForStringInLogUsingMark("CWWKZ0001I.*Application applicationExtension started");

    }

    @Test
    public void testAppServlet() throws Exception {

        HttpUtils.findStringInUrl(server, "/appExtension/TestServlet", "In Same WAR : created in ", "In lib JAR  : created in");
    }

    @Test
    public void testAppExtensionLoaded() throws Exception {
        Assert.assertFalse("Test for before bean discovery event",
                           server.findStringsInLogs("PlainExtension: beginning the scanning process").isEmpty());
        Assert.assertFalse("Test for processing annotation type event",
                           server.findStringsInLogs("PlainExtension: scanning type->").isEmpty());
        Assert.assertFalse("Test for after bean discovery event",
                           server.findStringsInLogs("PlainExtension: finished the scanning process").isEmpty());
    }

    @AfterClass
    public static void afterClass() throws Exception {
        if (server != null) {
            server.stopServer();
        }
    }

}
