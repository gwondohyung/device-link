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
package org.edgexfoundry.device.virtual.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Repository;

import org.edgexfoundry.device.virtual.ObjectTransform;
import org.edgexfoundry.device.virtual.domain.VirtualObject;
import org.edgexfoundry.device.virtual.handler.CoreDataMessageHandler;
import org.edgexfoundry.domain.core.Reading;
import org.edgexfoundry.domain.meta.Device;
import org.edgexfoundry.domain.meta.OperatingState;
import org.edgexfoundry.domain.meta.PropertyValue;
import org.edgexfoundry.domain.meta.ResourceOperation;
import org.edgexfoundry.exception.controller.NotFoundException;
import com.google.gson.JsonObject;
import java.io.IOException; 
import java.net.DatagramPacket; 
import java.net.DatagramSocket; 
import java.net.SocketException;

@Configuration
@EnableAsync
@SuppressWarnings("unused")
@Repository

public class UDPServer {
	private DatagramSocket socket; 
	private int PacketLength;
	private String PacketMessage;
	private boolean isStillRun = true;

	@Value("${service.tempData:20}")
	private String tempData;

	public UDPServer() throws SocketException { 
		super(); 
	    socket = new DatagramSocket(9999); 
	}
	
	public void setStillRun(boolean isStillRun) {
		this.isStillRun = isStillRun;
	}
	
	public String getTempData() {
		return tempData;
	}

	public void setTempData(String temperature) {
		tempData = temperature;
	}

	public void setPacketStatus(int PacketLength, String PacketMessage) { this.PacketLength=PacketLength; this.PacketMessage = PacketMessage; }

	public String getPacketMessage() { return PacketMessage; }

	public int getPacketLength() { return PacketLength; }

	public void serverStart() {
        while (true) { 
            try {
            	if (!isStillRun) break;

//        		System.out.println("UDP server start: ");
                byte[] inbuf = new byte[256];
                DatagramPacket packet = new DatagramPacket(inbuf, inbuf.length); 
                socket.receive(packet);
                PacketLength = packet.getLength();
                PacketMessage = new String(packet.getData(), 0, packet.getLength());
				setPacketStatus(PacketLength, PacketMessage);
                System.out.println("received length : " + PacketLength + ", received data : " + PacketMessage);

				if (!isStillRun) break;
            } 
            catch (IOException e) { 
                //e.printStackTrace(); 
            } 
        } 
	}

}
