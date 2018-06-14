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
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.net.SocketException;

import org.edgexfoundry.device.virtual.data.ObjectStore;
<<<<<<< HEAD
import org.edgexfoundry.device.virtual.service.UDPServer;
import org.edgexfoundry.device.virtual.service.UDPClient;
=======
import org.edgexfoundry.device.virtual.service.UDPService;
>>>>>>> b2696c843404551e7ff746f7ff1c2ad2cb3e9d60

import org.edgexfoundry.device.virtual.handler.VirtualHandler;
import org.edgexfoundry.support.logging.client.EdgeXLogger;
import org.edgexfoundry.support.logging.client.EdgeXLoggerFactory;

@RestController
@RequestMapping("/api/v1/")
public class ServiceController {
	private final static EdgeXLogger logger = EdgeXLoggerFactory.getEdgeXLogger(ServiceController.class);

	@Autowired
<<<<<<< HEAD
	UDPServer udp;

	@Autowired
	UDPClient udp_client;
=======
	UDPService udp;
>>>>>>> b2696c843404551e7ff746f7ff1c2ad2cb3e9d60

	@Autowired
	ObjectStore objects;

	@Autowired
	VirtualHandler handler;

	/***************************************************/
	/* UDP Ping Pong */
	@RequestMapping(path = "link/device/udp_pingpong_start", method = RequestMethod.GET)
	public @ResponseBody
	String UDPPingPongStart() throws Exception {
		udp.setPingPongState(false);
		udp.setPingPongState(true);
		udp.startPingPong();
		return "UDP Ping Pong started successfully.\n";
	}

	@RequestMapping(path = "/link/device/udp_pingpong_stop", method = RequestMethod.GET)
	public @ResponseBody
	String UDPPingPongStop() throws InterruptedException {
		udp.setPingPongState(false);
		System.out.println("UDP Ping Pong stopped successfully.");
		return "UDP Ping Pong stopped successfully.\n";
	}


<<<<<<< HEAD
=======
//	/***************************************************/
//	/* UDP Receiver */
//	@RequestMapping(path = "link/device/udp_receiver_start", method = RequestMethod.GET)
//	public @ResponseBody
//	String UDPReceiverStart() throws Exception {
//		udp.setReceiverState(false);
//		udp.setReceiverState(true);
//		udp.startReceiver();
//		return "UDP Receiver started successfully.\n";
//	}

>>>>>>> b2696c843404551e7ff746f7ff1c2ad2cb3e9d60
	/***************************************************/
	/* UDP Receiver */
	@RequestMapping(path = "link/device/udp_receiver_start", method = RequestMethod.GET)
	public @ResponseBody
<<<<<<< HEAD
	String UDPReceiverStart() throws Exception {
		udp.setReceiverState(false);
		udp.setReceiverState(true);
		udp.startReceiver();
=======
	String UDPReceiverStart(@RequestParam String clientAddr, @RequestParam int clientPort) throws Exception {
		udp.setReceiverState(false);
		udp.setReceiverState(true);
		udp.startReceiver(clientAddr, clientPort);
>>>>>>> b2696c843404551e7ff746f7ff1c2ad2cb3e9d60
		return "UDP Receiver started successfully.\n";
	}

	@RequestMapping(path = "/link/device/udp_receiver_stop", method = RequestMethod.GET)
	public @ResponseBody
	String UDPReceiverStop() throws InterruptedException {
		udp.setReceiverState(false);
		System.out.println("UDP Receiver stopped successfully.");
		return "UDP Receiver stopped successfully.\n";
	}


	/***************************************************/
	/* UDP Sender */
	@RequestMapping(path = "link/device/udp_sender_start", method = RequestMethod.GET)
	public @ResponseBody
	String UDPSenderStart(@RequestParam String clientAddr, @RequestParam int clientPort) throws Exception {
		udp.setSenderState(false);
		udp.setSenderState(true);
		udp.startSender(clientAddr, clientPort);
		return "UDP Sender started successfully.\n";
	}

	@RequestMapping(path = "/link/device/udp_sender_stop", method = RequestMethod.GET)
	public @ResponseBody
	String UDPSenderStop() throws InterruptedException {
		udp.setSenderState(false);
		System.out.println("UDP Sender stopped successfully.");
		return "UDP Sender stopped successfully.\r\n";
	}



	@RequestMapping(path = "/discovery", method = RequestMethod.POST)
	public @ResponseBody
	String doDiscovery() {
		logger.info("Running discovery request");
		handler.scan();
		return "Running discovery";
	}
}