//
// ========================================================================
// Copyright (c) Mort Bay Consulting Pty Ltd and others.
//
// This program and the accompanying materials are made available under the
// terms of the Eclipse Public License v. 2.0 which is available at
// https://www.eclipse.org/legal/epl-2.0, or the Apache License, Version 2.0
// which is available at https://www.apache.org/licenses/LICENSE-2.0.
//
// SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
// ========================================================================
//

package org.eclipse.jetty.demo;

import java.net.URI;

import org.eclipse.jetty.websocket.api.util.WSURI;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class IntegrationTest
{
    private EventServer server;

    @BeforeEach
    public void setupServer() throws Exception
    {
        server = new EventServer();
        server.setPort(0);
        server.start();
    }

    @AfterEach
    public void stopServer() throws Exception
    {
        server.stop();
    }

    @Test
    public void testServer() throws Exception
    {
        URI destUri = server.getURI().resolve("/events/");
        URI wsUri = WSURI.toWebsocket(destUri);
        EventClient client = new EventClient();
        client.run(wsUri);
    }
}
