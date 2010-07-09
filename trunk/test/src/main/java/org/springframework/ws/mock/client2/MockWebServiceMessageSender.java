/*
 * Copyright 2005-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.ws.mock.client2;

import java.io.IOException;
import java.net.URI;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.springframework.util.Assert;
import org.springframework.ws.transport.WebServiceMessageSender;

import static org.junit.Assert.assertTrue;

class MockWebServiceMessageSender implements WebServiceMessageSender {

    private final List<MockSenderConnection> expectedConnections = new LinkedList<MockSenderConnection>();

    private Iterator<MockSenderConnection> connectionIterator;

    public MockSenderConnection createConnection(URI uri) throws IOException {
        Assert.notNull(uri, "'uri' must not be null");
        if (connectionIterator == null) {
            connectionIterator = expectedConnections.iterator();
        }
        assertTrue("No further connections expected", connectionIterator.hasNext());

        MockSenderConnection currentConnection = connectionIterator.next();
        currentConnection.setUri(uri);
        if (!connectionIterator.hasNext()) {
            currentConnection.lastConnection();
        }
        return currentConnection;
    }

    /** Always returns {@code true}. */
    public boolean supports(URI uri) {
        return true;
    }

    MockSenderConnection expectNewConnection() {
        MockSenderConnection connection = new MockSenderConnection();
        expectedConnections.add(connection);
        return connection;
    }

}