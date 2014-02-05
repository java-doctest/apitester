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

package de.devbliss.apitester.transport;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.net.URI;

import org.apache.http.HttpStatus;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import de.devbliss.apitester.ApiTestUtil;
import de.devbliss.apitester.ApiTesterModule;
import de.devbliss.apitester.dummyserver.DummyApiServer;
import de.devbliss.apitester.dummyserver.DummyDto;
import de.devbliss.apitester.entity.ApiRequest;
import de.devbliss.apitester.entity.ApiResponse;
import de.devbliss.apitester.entity.Context;
import de.devbliss.apitester.entity.TestState;

/**
 * Tests the methods of {@link Patcher} and its delegates against an embedded local instance of
 * {@link DummyApiServer} with "real" HTTP requests.
 *
 * @author mbankmann
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class PatcherIntegrationTest extends AbstractRequestIntegrationTest {

    @Test
    public void testPatchOk() throws Exception {
        URI uri = server.buildGetRequestUri(HttpStatus.SC_OK);
        Context wrapper = Patcher.patch(uri);
        ApiRequest request = wrapper.apiRequest;
        ApiResponse response = wrapper.apiResponse;
        ApiTestUtil.assertOk(response);
        assertEquals(uri, request.uri);
        assertEquals("PATCH", request.httpMethod);
    }


    @Test
    public void testPatchOkWithPayload() throws Exception {
        DummyDto payload = createPayload();
        URI uri = server.buildGetRequestUri(HttpStatus.SC_OK);
        Context wrapper = Patcher.patch(uri, payload);
        ApiRequest request = wrapper.apiRequest;
        ApiResponse response = wrapper.apiResponse;
        ApiTestUtil.assertOk(response);
        DummyDto result = response.payloadJsonAs(DummyDto.class);
        assertEquals(payload, result);
        assertEquals(uri, request.uri);
        assertEquals("PATCH", request.httpMethod);
    }

    @Test
    public void testPatchOkWithOwnPatchFactory() throws Exception {
        URI uri = server.buildGetRequestUri(HttpStatus.SC_OK);
        Context wrapper = Patcher.patch(uri);
        ApiRequest request = wrapper.apiRequest;
        ApiResponse response = wrapper.apiResponse;
        ApiTestUtil.assertOk(response);
        assertEquals(uri, request.uri);
        assertEquals("PATCH", request.httpMethod);
    }

    @Test
    public void testPatchOkWithOwnTestState() throws Exception {
        URI uri = server.buildGetRequestUri(HttpStatus.SC_OK);
        TestState testState = ApiTesterModule.createTestState();
        Context wrapper = Patcher.patch(uri, testState);
        ApiResponse response = wrapper.apiResponse;
        ApiRequest request = wrapper.apiRequest;
        ApiTestUtil.assertOk(response);
        assertEquals(uri, request.uri);
        assertEquals("PATCH", request.httpMethod);
    }

    @Test
    public void testPatchOkWithOwnPatchFactoryAndTestState() throws Exception {
        URI uri = server.buildGetRequestUri(HttpStatus.SC_OK);
        TestState testState = ApiTesterModule.createTestState();
        Context wrapper = Patcher.patch(uri, testState);
        ApiResponse response = wrapper.apiResponse;
        ApiRequest request = wrapper.apiRequest;
        ApiTestUtil.assertOk(response);
        assertEquals(uri, request.uri);
        assertEquals("PATCH", request.httpMethod);
    }

    @Test
    public void testPatchOkWithPayloadAndOwnPatchFactory() throws Exception {
        DummyDto payload = createPayload();
        URI uri = server.buildGetRequestUri(HttpStatus.SC_OK);
        Context wrapper = Patcher.patch(uri, payload);
        ApiResponse response = wrapper.apiResponse;
        ApiRequest request = wrapper.apiRequest;
        ApiTestUtil.assertOk(response);
        DummyDto result = response.payloadJsonAs(DummyDto.class);
        assertEquals(payload, result);
        assertEquals(uri, request.uri);
        assertEquals("PATCH", request.httpMethod);
    }

    @Test
    public void testPatchOkWithPayloadAndOwnTestState() throws Exception {
        DummyDto payload = createPayload();
        URI uri = server.buildGetRequestUri(HttpStatus.SC_OK);
        TestState testState = ApiTesterModule.createTestState();
        Context wrapper = Patcher.patch(uri, testState, payload);
        ApiResponse response = wrapper.apiResponse;
        ApiRequest request = wrapper.apiRequest;
        ApiTestUtil.assertOk(response);
        DummyDto result = response.payloadJsonAs(DummyDto.class);
        assertEquals(payload, result);
        assertEquals(uri, request.uri);
        assertEquals("PATCH", request.httpMethod);
    }

    @Test
    public void testPatchOkWithPayloadAndOwnPatchFactoryAndTestState() throws Exception {
        DummyDto payload = createPayload();
        URI uri = server.buildGetRequestUri(HttpStatus.SC_OK);
        TestState testState = ApiTesterModule.createTestState();
        Context wrapper = Patcher.patch(uri, testState, payload, null);
        ApiResponse response = wrapper.apiResponse;
        ApiRequest request = wrapper.apiRequest;
        ApiTestUtil.assertOk(response);
        DummyDto result = response.payloadJsonAs(DummyDto.class);
        assertEquals(payload, result);
        assertEquals(uri, request.uri);
        assertEquals("PATCH", request.httpMethod);
    }

    @Test
    public void testPatchWithHeaders() throws Exception {
        DummyDto payload = createPayload();
        URI uri = server.buildGetRequestUri(HttpStatus.SC_OK);
        TestState testState = ApiTesterModule.createTestState();
        Context wrapper = Patcher.patch(uri, testState, payload, createCustomHeaders());
        ApiResponse response = wrapper.apiResponse;
        ApiRequest request = wrapper.apiRequest;
        ApiTestUtil.assertOk(response);
        assertEquals(HEADER_VALUE1, request.getHeader(HEADER_NAME1));
        assertEquals(HEADER_VALUE2, request.getHeader(HEADER_NAME2));
        assertEquals(uri, request.uri);
        assertEquals("PATCH", request.httpMethod);
    }

    @Test
    public void testPatchWithCookiesAndHeaders() throws Exception {
        DummyDto payload = createPayload();
        URI uri = server.buildGetRequestUri(HttpStatus.SC_OK);
        TestState testState = new TestState(new DefaultHttpClient(), cookieStore);
        Context wrapper = Patcher.patch(uri, testState, payload, createCustomHeaders());
        ApiResponse response = wrapper.apiResponse;
        ApiRequest request = wrapper.apiRequest;
        ApiTestUtil.assertOk(response);
        assertEquals(uri, request.uri);
        assertEquals("PATCH", request.httpMethod);
        assertEquals(HEADER_VALUE1, request.getHeader(HEADER_NAME1));
        assertEquals(HEADER_VALUE2, request.getHeader(HEADER_NAME2));
        assertEquals(COOKIE_VALUE_1, request.getCookie(COOKIE_NAME_1));
        assertEquals(COOKIE_VALUE_2, request.getCookie(COOKIE_NAME_2));

        assertNull(request.getCookie(HEADER_NAME1));
        assertNull(request.getHeader(COOKIE_NAME_1));

    }
}
