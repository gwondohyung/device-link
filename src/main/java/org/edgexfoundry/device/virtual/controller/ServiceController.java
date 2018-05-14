/*******************************************************************************
 * Copyright 2016-2017 Dell Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @microservice:  device-sdk-tools
 * @author: Tyler Cox, Dell
 * @version: 1.0.0
 *******************************************************************************/
package org.edgexfoundry.device.virtual.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.SocketException;

import org.edgexfoundry.device.virtual.data.ObjectStore;
import org.edgexfoundry.device.virtual.service.UDPServer;

import org.edgexfoundry.device.virtual.handler.VirtualHandler;
import org.edgexfoundry.support.logging.client.EdgeXLogger;
import org.edgexfoundry.support.logging.client.EdgeXLoggerFactory;

@RestController
@RequestMapping("/api/v1/")
public class ServiceController {

	private final static EdgeXLogger logger = EdgeXLoggerFactory.getEdgeXLogger(ServiceController.class);
	
	@Autowired
	UDPServer udp;
	
	@Autowired
	ObjectStore objects;
	
	@Autowired
	VirtualHandler handler;

//	@RequestMapping(path = "/debug/transformData/{transformData}", method = RequestMethod.GET)
//	public @ResponseBody String setTransformData(@PathVariable Boolean transformData) {
//		logger.info("Setting transform data to: " + transformData);
//		objects.setTransformData(transformData);
//		System.out.println(transformData);
//		System.out.println(objects.getTransformData());
//		return "Set transform data to: " + transformData;
//	}
	
	@RequestMapping(path = "/link/device/temperature/{data}", method = RequestMethod.GET)
	public @ResponseBody String setTempData(@PathVariable String data) throws SocketException {
		logger.info("Device Temperature: " + data);
		System.out.println(data);
		udp.setTempData(data);
		System.out.println(data);
		udp.UDPServerStart();
//		try {
//			
//		} catch (SocketException e) {
//			// TODO Auto-generated catch block
//			//e.printStackTrace();
//		}
		return "Device Temperature: " + udp.getTempData();
	}
	
	@RequestMapping(path = "/link/device/udpserverstart", method = RequestMethod.GET)
	public @ResponseBody String UDPServerStart() throws SocketException {
		udp.UDPServerStart();
		
		return "UDP server start: ";
	}
	
	@RequestMapping(path = "/link/device/udpserverstop", method = RequestMethod.GET)
	public @ResponseBody String UDPServerStop() {
		udp.UDPServerStop();
		return "UDP server stop: ";
	}
	
	@RequestMapping(path = "/discovery", method = RequestMethod.POST)
	public @ResponseBody String doDiscovery() {
		logger.info("Running discovery request");
		handler.scan();
		return "Running discovery";
	}
}
