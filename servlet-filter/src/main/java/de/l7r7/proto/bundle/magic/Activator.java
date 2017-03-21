/*
 *  Copyright 2017 Leonhard Riedißer
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package de.l7r7.proto.bundle.magic;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.http.HttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import java.util.Hashtable;

public class Activator implements BundleActivator {
    private static final Logger log = LoggerFactory.getLogger(Activator.class);
    private HttpService httpService;
    private ServiceRegistration<?> serviceRegistration;

    @Override
    public void start(BundleContext context) throws Exception {
        log.info("######################################################");
        log.info("start");
        log.info("######################################################");

        Hashtable<String, String> props = new Hashtable<>();
        props.put("osgi.http.whiteboard.servlet.pattern", "/*");
        props.put("init.message", "Crazy filter!");
        props.put("service.ranking", "1");
        serviceRegistration = context.registerService(Filter.class.getName(), new CrazyFilter(), props);

        final ServiceReference<?> httpRef = context.getServiceReference("org.osgi.service.http.HttpService");
        httpService = (HttpService) context.getService(httpRef);

        httpService.registerServlet("/foo", new FooServlet(), new Hashtable(), httpService.createDefaultHttpContext());
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        serviceRegistration.unregister();
        httpService.unregister("/foo");
    }
}
