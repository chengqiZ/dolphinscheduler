/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.dolphinscheduler.api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.apache.dolphinscheduler.api.enums.Status;
import org.apache.dolphinscheduler.api.service.ResourcesService;
import org.apache.dolphinscheduler.api.service.UdfFuncService;
import org.apache.dolphinscheduler.api.utils.Result;
import org.apache.dolphinscheduler.common.constants.Constants;
import org.apache.dolphinscheduler.common.enums.UdfType;
import org.apache.dolphinscheduler.common.utils.JSONUtils;
import org.apache.dolphinscheduler.spi.enums.ResourceType;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * resources controller test
 */
public class ResourcesControllerTest extends AbstractControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(ResourcesControllerTest.class);

    @MockBean(name = "resourcesServiceImpl")
    private ResourcesService resourcesService;

    @MockBean(name = "udfFuncServiceImpl")
    private UdfFuncService udfFuncService;

    @Test
    public void testQuerytResourceList() throws Exception {
        Map<String, Object> mockResult = new HashMap<>();
        mockResult.put(Constants.STATUS, Status.SUCCESS);
        Mockito.when(resourcesService.queryResourceList(Mockito.any(), Mockito.any())).thenReturn(mockResult);

        MvcResult mvcResult = mockMvc.perform(get("/resources/list")
                .header(SESSION_ID, sessionId)
                .param("type", ResourceType.FILE.name()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Result result = JSONUtils.parseObject(mvcResult.getResponse().getContentAsString(), Result.class);

        Assertions.assertEquals(Status.SUCCESS.getCode(), result.getCode().intValue());
        logger.info(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void testQueryResourceListPaging() throws Exception {
        Result mockResult = new Result<>();
        mockResult.setCode(Status.SUCCESS.getCode());
        Mockito.when(resourcesService.queryResourceListPaging(
                Mockito.any(), Mockito.anyInt(), Mockito.any(), Mockito.anyString(), Mockito.anyInt(),
                Mockito.anyInt()))
                .thenReturn(mockResult);

        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("type", String.valueOf(ResourceType.FILE));
        paramsMap.add("id", "123");
        paramsMap.add("pageNo", "1");
        paramsMap.add("searchVal", "test");
        paramsMap.add("pageSize", "1");

        MvcResult mvcResult = mockMvc.perform(get("/resources")
                .header(SESSION_ID, sessionId)
                .params(paramsMap))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Result result = JSONUtils.parseObject(mvcResult.getResponse().getContentAsString(), Result.class);

        Assertions.assertEquals(Status.SUCCESS.getCode(), result.getCode().intValue());
        logger.info(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void testVerifyResourceName() throws Exception {
        Result mockResult = new Result<>();
        mockResult.setCode(Status.TENANT_NOT_EXIST.getCode());
        Mockito.when(resourcesService.verifyResourceName(Mockito.anyString(), Mockito.any(), Mockito.any()))
                .thenReturn(mockResult);

        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("fullName", "list_resources_1.sh");
        paramsMap.add("type", "FILE");

        MvcResult mvcResult = mockMvc.perform(get("/resources/verify-name")
                .header(SESSION_ID, sessionId)
                .params(paramsMap))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Result result = JSONUtils.parseObject(mvcResult.getResponse().getContentAsString(), Result.class);

        Assertions.assertEquals(Status.TENANT_NOT_EXIST.getCode(), result.getCode().intValue());
        logger.info(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void testViewResource() throws Exception {
        Result mockResult = new Result<>();
        mockResult.setCode(Status.HDFS_NOT_STARTUP.getCode());
        Mockito.when(resourcesService.readResource(Mockito.any(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(mockResult);

        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("skipLineNum", "2");
        paramsMap.add("limit", "100");

        MvcResult mvcResult = mockMvc.perform(get("/resources/{id}/view", "5")
                .header(SESSION_ID, sessionId)
                .params(paramsMap))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Result result = JSONUtils.parseObject(mvcResult.getResponse().getContentAsString(), Result.class);

        Assertions.assertEquals(Status.HDFS_NOT_STARTUP.getCode(), result.getCode().intValue());
        logger.info(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void testOnlineCreateResource() throws Exception {
        Result mockResult = new Result<>();
        mockResult.setCode(Status.TENANT_NOT_EXIST.getCode());
        Mockito.when(resourcesService
                .onlineCreateResource(Mockito.any(), Mockito.any(), Mockito.anyString(), Mockito.anyString(),
                        Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyString()))
                .thenReturn(mockResult);

        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("type", String.valueOf(ResourceType.FILE));
        paramsMap.add("fileName", "test_file_1");
        paramsMap.add("suffix", "sh");
        paramsMap.add("description", "test");
        paramsMap.add("content", "echo 1111");
        paramsMap.add("pid", "123");
        paramsMap.add("currentDir", "/xx");

        MvcResult mvcResult = mockMvc.perform(post("/resources/online-create")
                .header(SESSION_ID, sessionId)
                .params(paramsMap))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Result result = JSONUtils.parseObject(mvcResult.getResponse().getContentAsString(), Result.class);

        Assertions.assertEquals(Status.TENANT_NOT_EXIST.getCode(), result.getCode().intValue());
        logger.info(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void testUpdateResourceContent() throws Exception {
        Result mockResult = new Result<>();
        mockResult.setCode(Status.TENANT_NOT_EXIST.getCode());
        Mockito.when(resourcesService.updateResourceContent(Mockito.any(), Mockito.anyInt(), Mockito.anyString()))
                .thenReturn(mockResult);

        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("id", "1");
        paramsMap.add("content", "echo test_1111");

        MvcResult mvcResult = mockMvc.perform(put("/resources/1/update-content")
                .header(SESSION_ID, sessionId)
                .params(paramsMap))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Result result = JSONUtils.parseObject(mvcResult.getResponse().getContentAsString(), Result.class);

        Assertions.assertEquals(Status.TENANT_NOT_EXIST.getCode(), result.getCode().intValue());
        logger.info(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void testDownloadResource() throws Exception {

        Mockito.when(resourcesService.downloadResource(Mockito.any(), Mockito.anyInt())).thenReturn(null);

        MvcResult mvcResult = mockMvc.perform(get("/resources/{id}/download", 5)
                .header(SESSION_ID, sessionId))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andReturn();

        Assertions.assertNotNull(mvcResult);
    }

    @Test
    public void testCreateUdfFunc() throws Exception {
        Result mockResult = new Result<>();
        mockResult.setCode(Status.TENANT_NOT_EXIST.getCode());
        Mockito.when(udfFuncService
                .createUdfFunction(Mockito.any(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
                        Mockito.anyString(), Mockito.anyString(), Mockito.any(), Mockito.anyInt()))
                .thenReturn(mockResult);

        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("type", String.valueOf(UdfType.HIVE));
        paramsMap.add("funcName", "test_udf");
        paramsMap.add("className", "com.test.word.contWord");
        paramsMap.add("argTypes", "argTypes");
        paramsMap.add("database", "database");
        paramsMap.add("description", "description");
        paramsMap.add("resourceId", "1");

        MvcResult mvcResult = mockMvc.perform(post("/resources/{resourceId}/udf-func", "123")
                .header(SESSION_ID, sessionId)
                .params(paramsMap))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Result result = JSONUtils.parseObject(mvcResult.getResponse().getContentAsString(), Result.class);

        Assertions.assertEquals(Status.TENANT_NOT_EXIST.getCode(), result.getCode().intValue());
        logger.info(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void testViewUIUdfFunction() throws Exception {
        Result<Object> mockResult = new Result<>();
        putMsg(mockResult, Status.TENANT_NOT_EXIST);
        Mockito.when(udfFuncService
                .queryUdfFuncDetail(Mockito.any(), Mockito.anyInt()))
                .thenReturn(mockResult);

        MvcResult mvcResult = mockMvc.perform(get("/resources/{id}/udf-func", "123")
                .header(SESSION_ID, sessionId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Result result = JSONUtils.parseObject(mvcResult.getResponse().getContentAsString(), Result.class);

        Assertions.assertEquals(Status.TENANT_NOT_EXIST.getCode(), result.getCode().intValue());
        logger.info(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void testUpdateUdfFunc() throws Exception {
        Result<Object> mockResult = new Result<>();
        mockResult.setCode(Status.TENANT_NOT_EXIST.getCode());
        Mockito.when(udfFuncService
                .updateUdfFunc(Mockito.any(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString(),
                        Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.any(), Mockito.anyInt()))
                .thenReturn(mockResult);

        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("id", "1");
        paramsMap.add("type", String.valueOf(UdfType.HIVE));
        paramsMap.add("funcName", "update_duf");
        paramsMap.add("className", "com.test.word.contWord");
        paramsMap.add("argTypes", "argTypes");
        paramsMap.add("database", "database");
        paramsMap.add("description", "description");
        paramsMap.add("resourceId", "1");

        MvcResult mvcResult = mockMvc.perform(put("/resources/{resourceId}/udf-func/{id}", "123", "456")
                .header(SESSION_ID, sessionId)
                .params(paramsMap))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Result result = JSONUtils.parseObject(mvcResult.getResponse().getContentAsString(), Result.class);

        Assertions.assertEquals(Status.TENANT_NOT_EXIST.getCode(), result.getCode().intValue());
        logger.info(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void testQueryUdfFuncList() throws Exception {
        Result mockResult = new Result<>();
        mockResult.setCode(Status.SUCCESS.getCode());
        Mockito.when(udfFuncService.queryUdfFuncListPaging(Mockito.any(), Mockito.anyString(), Mockito.anyInt(),
                Mockito.anyInt())).thenReturn(mockResult);

        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("pageNo", "1");
        paramsMap.add("searchVal", "udf");
        paramsMap.add("pageSize", "1");

        MvcResult mvcResult = mockMvc.perform(get("/resources/udf-func")
                .header(SESSION_ID, sessionId)
                .params(paramsMap))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Result result = JSONUtils.parseObject(mvcResult.getResponse().getContentAsString(), Result.class);

        Assertions.assertEquals(Status.SUCCESS.getCode(), result.getCode().intValue());
        logger.info(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void testQueryResourceList() throws Exception {
        Result<Object> mockResult = new Result<>();
        mockResult.setCode(Status.SUCCESS.getCode());
        Mockito.when(udfFuncService.queryUdfFuncList(Mockito.any(), Mockito.anyInt())).thenReturn(mockResult);

        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("type", String.valueOf(UdfType.HIVE));

        MvcResult mvcResult = mockMvc.perform(get("/resources/udf-func/list")
                .header(SESSION_ID, sessionId)
                .params(paramsMap))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Result result = JSONUtils.parseObject(mvcResult.getResponse().getContentAsString(), Result.class);

        Assertions.assertEquals(Status.SUCCESS.getCode(), result.getCode().intValue());
        logger.info(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void testVerifyUdfFuncName() throws Exception {
        Result mockResult = new Result<>();
        mockResult.setCode(Status.SUCCESS.getCode());
        Mockito.when(udfFuncService.verifyUdfFuncByName(Mockito.any(), Mockito.anyString())).thenReturn(mockResult);

        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("name", "test");

        MvcResult mvcResult = mockMvc.perform(get("/resources/udf-func/verify-name")
                .header(SESSION_ID, sessionId)
                .params(paramsMap))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Result result = JSONUtils.parseObject(mvcResult.getResponse().getContentAsString(), Result.class);

        Assertions.assertEquals(Status.SUCCESS.getCode(), result.getCode().intValue());
        logger.info(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void testAuthorizedFile() throws Exception {
        Map<String, Object> mockResult = new HashMap<>();
        mockResult.put(Constants.STATUS, Status.SUCCESS);
        Mockito.when(resourcesService.authorizedFile(Mockito.any(), Mockito.anyInt())).thenReturn(mockResult);

        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("userId", "2");

        MvcResult mvcResult = mockMvc.perform(get("/resources/authed-file")
                .header(SESSION_ID, sessionId)
                .params(paramsMap))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Result result = JSONUtils.parseObject(mvcResult.getResponse().getContentAsString(), Result.class);

        Assertions.assertEquals(Status.SUCCESS.getCode(), result.getCode().intValue());
        logger.info(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void testAuthorizedUDFFunction() throws Exception {
        Map<String, Object> mockResult = new HashMap<>();
        mockResult.put(Constants.STATUS, Status.SUCCESS);
        Mockito.when(resourcesService.authorizedUDFFunction(Mockito.any(), Mockito.anyInt())).thenReturn(mockResult);

        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("userId", "2");

        MvcResult mvcResult = mockMvc.perform(get("/resources/authed-udf-func")
                .header(SESSION_ID, sessionId)
                .params(paramsMap))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Result result = JSONUtils.parseObject(mvcResult.getResponse().getContentAsString(), Result.class);

        Assertions.assertEquals(Status.SUCCESS.getCode(), result.getCode().intValue());
        logger.info(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void testUnauthUDFFunc() throws Exception {
        Map<String, Object> mockResult = new HashMap<>();
        mockResult.put(Constants.STATUS, Status.SUCCESS);
        Mockito.when(resourcesService.unauthorizedUDFFunction(Mockito.any(), Mockito.anyInt())).thenReturn(mockResult);

        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("userId", "2");

        MvcResult mvcResult = mockMvc.perform(get("/resources/unauth-udf-func")
                .header(SESSION_ID, sessionId)
                .params(paramsMap))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Result result = JSONUtils.parseObject(mvcResult.getResponse().getContentAsString(), Result.class);

        Assertions.assertEquals(Status.SUCCESS.getCode(), result.getCode().intValue());
        logger.info(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void testDeleteUdfFunc() throws Exception {
        Result mockResult = new Result<>();
        mockResult.setCode(Status.SUCCESS.getCode());
        Mockito.when(udfFuncService.delete(Mockito.any(), Mockito.anyInt())).thenReturn(mockResult);

        MvcResult mvcResult = mockMvc.perform(delete("/resources/udf-func/{id}", "123")
                .header(SESSION_ID, sessionId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Result result = JSONUtils.parseObject(mvcResult.getResponse().getContentAsString(), Result.class);

        Assertions.assertEquals(Status.SUCCESS.getCode(), result.getCode().intValue());
        logger.info(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void testDeleteResource() throws Exception {
        Result mockResult = new Result<>();
        mockResult.setCode(Status.SUCCESS.getCode());
        Mockito.when(resourcesService.delete(Mockito.any(), Mockito.anyInt())).thenReturn(mockResult);

        MvcResult mvcResult = mockMvc.perform(delete("/resources/{id}", "123")
                .header(SESSION_ID, sessionId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Result result = JSONUtils.parseObject(mvcResult.getResponse().getContentAsString(), Result.class);

        Assertions.assertEquals(Status.SUCCESS.getCode(), result.getCode().intValue());
        logger.info(mvcResult.getResponse().getContentAsString());
    }
}
