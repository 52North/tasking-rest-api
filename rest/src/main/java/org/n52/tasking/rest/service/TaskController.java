/*
 * Copyright (C) 2016-2016 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of
 * the following licenses, the combination of the program with the linked
 * library is not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed
 * under the aforementioned licenses, is permitted by the copyright holders
 * if the distribution is compliant with both the GNU General Public License
 * version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 */
package org.n52.tasking.rest.service;

import java.util.List;
import org.n52.tasking.rest.RequestUtils;
import org.n52.tasking.rest.UrlSettings;
import org.n52.tasking.core.service.Resource;
import org.n52.tasking.core.service.TaskService;
import org.n52.tasking.data.cmd.CreateTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping(value = UrlSettings.API_V1_BASE + "/" + UrlSettings.TASKS_RESOURCE,
        produces = {"application/json"})
public class TaskController {

    @Autowired
    private TaskService service;

    @RequestMapping(path = "")
    public ModelAndView getResourceCollection(@RequestParam(required = false) MultiValueMap<String, String> query) throws Exception {
        String fullUrl = RequestUtils.resolveFullRequestUrl();
        List<Resource> list = this.service.getTasks(fullUrl);
        return new ModelAndView().addObject(list);
    }

    @RequestMapping(path = "/{item}")
    public Object getResourceItem(@PathVariable("item") String id) throws Exception {
        return this.service.getTask(id);
    }

    @RequestMapping(path = "", consumes = "application/json", method = POST)
    public Resource createTask(@RequestBody CreateTask createTask) throws Exception {
        String fullUrl = RequestUtils.resolveFullRequestUrl();
        return this.service.createTask(createTask, fullUrl);
    }

}
