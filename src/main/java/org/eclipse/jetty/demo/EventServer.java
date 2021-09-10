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

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;

import javax.servlet.ServletException;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.PathResource;
import org.eclipse.jetty.websocket.server.NativeWebSocketServletContainerInitializer;
import org.eclipse.jetty.websocket.server.WebSocketUpgradeFilter;

public class EventServer
{
    public static void main(String[] args) throws Exception
    {
        EventServer server = new EventServer();
        server.setPort(8080);
        server.start();
        server.join();
    }

    private final Server server;
    private final ServerConnector connector;

    public EventServer() throws IOException, ServletException
    {
        server = new Server();
        connector = new ServerConnector(server);
        server.addConnector(connector);

        // The location of the webapp base resource (for resources and static file
        // serving)
        Path webRootPath = new File("webapps/static-root/").toPath().toRealPath();

        // Setup the basic application "context" for this application at "/"
        // This is also known as the handler tree (in jetty speak)
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.setBaseResource(new PathResource(webRootPath));
        context.setWelcomeFiles(new String[] { "index.html" });
        server.setHandler(context);

        NativeWebSocketServletContainerInitializer.configure(context, (servletContext, nativeWebSocketConfiguration) ->
        {
            // Configure default max size
            nativeWebSocketConfiguration.getPolicy().setMaxTextMessageBufferSize(65535);

            // Add websockets
            nativeWebSocketConfiguration.addMapping("/events/*", EventSocket.class);
        });

        // Add generic filter that will accept WebSocket upgrade.
        WebSocketUpgradeFilter.configure(context);

        // Add default servlet
        ServletHolder holderDefault = new ServletHolder("default", DefaultServlet.class);
        holderDefault.setInitParameter("dirAllowed", "true");
        context.addServlet(holderDefault, "/");
    }

    public void setPort(int port)
    {
        connector.setPort(port);
    }

    public void start() throws Exception
    {
        server.start();
    }

    public URI getURI()
    {
        return server.getURI();
    }

    public void stop() throws Exception
    {
        server.stop();
    }

    public void join() throws InterruptedException
    {
        System.out.println("Use Ctrl+C to stop server");
        server.join();
    }
}
