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
 * @microservice:  device-link
 * @author: LINK-lab
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

//	List<Object> Packet = new ArrayList<Object>();
	
	@RequestMapping(path = "/link/device/temperature/{data}", method = RequestMethod.GET)
	public @ResponseBody String setTempData(@PathVariable String data) throws SocketException {
		logger.info("Device Temperature: " + data);
		System.out.println(data);
		udp.setTempData(data);
		System.out.println(data);
		return "Device Temperature: " + udp.getTempData();
	}
	
	@RequestMapping(path = "/link/device/udpserverstart", method = RequestMethod.GET)
	public @ResponseBody Object UDPServerStart() throws SocketException {
		logger.info("UDP server start: " + "received length : " + udp.getPacketLength() + ", received data : " + udp.getPacketMessage());

		udp.setStillRun(false);
		udp.setStillRun(true);
		udp.serverStart();
//		System.out.println("UDP server start: " + "received length : " + udp.getPacketLength() + ", received data : " + udp.getPacketMessage());
		return "UDP server started successfully";
	}
	
	@RequestMapping(path = "/link/device/udpserverstop", method = RequestMethod.GET)
	public @ResponseBody Object UDPServerStop() throws InterruptedException {
		udp.setStillRun(false);
		return "UDP server stoped successfully";
//		return "UDP server stop: ";
//		Packet.add("UDP server stop: " + "received length : " + udp.getPacketLength() + ", received data : " + udp.getPacketMessage());
//		System.out.println("UDP server stop: " + "received length : " + udp.getPacketLength() + ", received data : " + udp.getPacketMessage());
//		System.out.println(Packet);
//		return Packet;
	}
	
	@RequestMapping(path = "/discovery", method = RequestMethod.POST)
	public @ResponseBody String doDiscovery() {
		logger.info("Running discovery request");
		handler.scan();
		return "Running discovery";
	}
}
