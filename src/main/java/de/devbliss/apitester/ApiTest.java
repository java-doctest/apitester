/*
 * Copyright 2013, devbliss GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package de.devbliss.apitester;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.devbliss.apitester.entity.Context;
import de.devbliss.apitester.entity.TestState;
import de.devbliss.apitester.transport.Deleter;
import de.devbliss.apitester.transport.Getter;
import de.devbliss.apitester.transport.Patcher;
import de.devbliss.apitester.transport.Poster;
import de.devbliss.apitester.transport.Putter;

/**
 * Entrypoint class for performing api calls.
 * It keeps the teststate including http client instance so your client side session, cookies etc.
 * should be kept as long as you use the same {@link ApiTest} instance.
 *
 * You may use this either as superclass for your own tests or instantiate it within your tests if
 * you need a different superclass for whatever reason.
 *
 * <h1>Instantiation</h1>
 * <ul>
 * <li>
 * If you want to instantiate this class and use the <b>default factories</b>, just make:
 * <code><br/>ApiTest api = new ApiTest();</code></li><br/>
 * <li>
 * If you want to instantiate this class and use <b>specific factories</b> (like mocks for example),
 * just make: <code>
 * <br/>ApiTest api = new ApiTest();
 * <br/>api.setGetFactory(new SpecificGetFactory());
 * </code></li><br/>
 * <li>
 * If you use <b>Guice</b> and want to inject this class, there are two use cases:
 * <ul>
 * <li>if you need the <b>default factories</b>, you do not have to do anything since Guice is going
 * to use the default constructor of {@link ApiTest}.</li>
 * <li>if you need some <b>specifics factories</b>, you just have to bind these factories in your
 * Guice module like this: <code>
 * <br/>bind(DeleteFactory.class).annotatedWith(Names.named(ApiTest.{@link #DELETE_FACTORY})).to(
                DeleteImpl.class);
 * </code> <br/>
 * Guice will then find this binding and call the corresponding setter function (annotated with
 * <i>@Injected(optional=true)</i>).</li>
 * </ul>
 * </li>
 * </ul>
 *
 * @author hschuetz, mreinwarth, bmary, mbankmann
 *
 */
public class ApiTest {

    public static final String TEST_STATE = "testState";

    private TestState testState;

    public enum HTTP_REQUEST {
        POST, GET, PUT, DELETE, PATCH;
    }

    @Inject(optional = true)
    public void setTestState(@Named(TEST_STATE) TestState testState) {
        this.testState = testState;
    }

    public TestState getTestState() {
        if (testState == null) {
            setTestState(ApiTesterModule.createTestState());
        }
        return testState;
    }

    /**
     * Performs a post request using the default {@link PostFactory} and the {@link TestState} of
     * this
     * instance.
     *
     * @param uri
     * @param payload
     * @return
     * @throws IOException
     */
    public Context post(URI uri, Object payload) throws IOException {
        return post(uri, payload, null);
    }

    /**
     * Performs a post request using the default {@link PostFactory} and the {@link TestState} of
     * this instance.
     * For adding headers just use the parameter.
     *
     *
     * @param uri
     * @param payload
     * @param additionalHeaders
     * @return
     * @throws IOException
     */
    public Context post(URI uri, Object payload, Map<String, String> additionalHeaders) throws IOException {
        return Poster.post(uri, payload, getTestState(), additionalHeaders);
    }


    /**
     * Performs a get request using the {@link GetFactory} and the {@link TestState} of this
     * instance.
     *
     * @param uri
     * @return
     * @throws IOException
     */
    public Context get(URI uri) throws IOException {
        return get(uri, null);
    }

    /**
     * Performs a get request using the given {@link GetFactory} and the {@link TestState} of this
     * instance. The {@link GetFactory} will be used for this call only. If you want to
     * configure a new default one use {@link #setDefaultGetFactory(GetFactory)}.
     *
     * @param uri
     * @param getFactory
     * @param additionalHeaders
     * @return
     * @throws IOException
     */
    public Context get(URI uri, Map<String, String> additionalHeaders) throws IOException {
        return Getter.get(uri, getTestState(), additionalHeaders);
    }

    /**
     * Performs a delete request using the {@link DeleteFactory} and the {@link TestState} of this
     * instance.
     *
     * @param uri
     * @return
     * @throws IOException
     */
    public Context delete(URI uri) throws IOException {
        return delete(uri, null, null);
    }

    /**
     * Performs a delete request using the {@link DeleteFactory} and the {@link TestState} of this
     * instance.
     * For adding headers just use the parameter.
     *
     * @param uri
     * @param additionalHeaders
     * @return
     * @throws IOException
     */
    public Context delete(URI uri, Map<String, String> additionalHeaders) throws IOException {
        return delete(uri, null, additionalHeaders);
    }

    /**
     * Performs a delete request using the given {@link DeleteFactory} and the {@link TestState} of
     * this instance. The payload is not forbidden in the HTTP specification and so its supported
     * here.
     *
     * @param uri
     * @param payload
     * @return
     * @throws IOException
     */
    public Context delete(URI uri, Object payload) throws IOException {
        return delete(uri, payload, null);
    }

    /**
     * Performs a delete request using the given {@link DeleteFactory} and the {@link TestState} of
     * this instance. The payload is not forbidden in the HTTP specification and so its supported
     * here. The {@link DeleteFactory} will be used for this call only. If you want to
     * configure a new default one use {@link #setDefaultDeleteFactory(DeleteFactory)}.
     *
     * @param uri
     * @param payload
     * @param deleteFactory
     * @return
     * @throws IOException
     */
    public Context delete(URI uri, Object payload, Map<String, String> additionalHeaders) throws IOException {
        return Deleter.delete(uri, getTestState(), payload, additionalHeaders);
    }

    /**
     * Performs a put request using the {@link PutFactory} and the {@link TestState} of this
     * instance.
     *
     * @param uri
     * @return
     * @throws IOException
     */
    public Context put(URI uri) throws IOException {
        return put(uri, null, null);
    }

    /**
     * Performs a put request using the {@link PutFactory} and the {@link TestState} of this
     * instance.
     * For adding headers just use the parameter.
     *
     * @param uri
     * @param additionalHeaders
     * @return
     * @throws IOException
     */
    public Context put(URI uri, Map<String, String> additionalHeaders) throws IOException {
        return put(uri, null, additionalHeaders);
    }

    /**
     * Performs a put request using the {@link PutFactory} and the {@link TestState} of this
     * instance with the given payload.
     *
     * @param uri
     * @param payload
     * @return
     * @throws IOException
     */
    public Context put(URI uri, Object payload) throws IOException {
        return put(uri, payload, null);
    }



    /**
     * Performs a put request using the {@link PutFactory} and the {@link TestState} of this
     * instance with the given payload. The {@link PutFactory} will be used for this call only. If
     * you want to configure a new default one use {@link #setDefaultDeleteFactory(PutFactory)}.
     * For adding headers you don't need to create a new Factory and just use the parameter.
     *
     * @param uri
     * @param payload
     * @param additionalHeaders
     * @return
     * @throws IOException
     */
    public Context put(URI uri, Object payload, Map<String, String> additionalHeaders) throws IOException {
        return Putter.put(uri, getTestState(), payload, additionalHeaders);
    }

    /**
     * Performs a patch request using the {@link PatchFactory} and the {@link TestState} of this
     * instance.
     *
     * @param uri
     * @return
     * @throws IOException
     */
    public Context patch(URI uri) throws IOException {
        return patch(uri, null, null);
    }

    /**
     * Performs a patch request using the {@link PatchFactory} and the {@link TestState} of this
     * instance.
     * For adding headers just use the parameter.
     *
     * @param uri
     * @param additionalHeaders
     * @return
     * @throws IOException
     */
    public Context patch(URI uri, Map<String, String> additionalHeaders) throws IOException {
        return patch(uri, null, additionalHeaders);
    }

    /**
     * Performs a patch request using the {@link PatchFactory} and the {@link TestState} of this
     * instance with the given payload.
     *
     * @param uri
     * @param payload
     * @return
     * @throws IOException
     */
    public Context patch(URI uri, Object payload) throws IOException {
        return patch(uri, payload, null);
    }


    /**
     * Performs a patch request using the {@link PatchFactory} and the {@link TestState} of this
     * instance with the given payload. The {@link PatchFactory} will be used for this call only. If
     * you want to configure a new default one use {@link #setDefaultPatchFactory(PatchFactory)}.
     *
     * @param uri
     * @param payload
     * @param additionalHeaders
     * @return
     * @throws IOException
     */
    public Context patch(URI uri, Object payload, Map<String, String> additionalHeaders) throws IOException {
        return Patcher.patch(uri, payload, getTestState(), additionalHeaders);
    }

    /**
     * Shutdown, closing any open HTTP connections
     */
    public void shutdown() {
        if (testState != null) {
            testState.shutdown();
        }
    }
}
