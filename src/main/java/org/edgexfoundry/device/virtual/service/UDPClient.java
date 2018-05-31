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
package org.edgexfoundry.device.virtual.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.*;
import java.util.Arrays;

@SuppressWarnings("unused")
@Repository

public class UDPClient {
	private boolean isStillRun = true;
	private DatagramSocket socket;
	private String packetData;

	public UDPClient() throws SocketException {
		super();
	}

	public void setStillRun(boolean isStillRun) {
		this.isStillRun = isStillRun;
	}

	public void sendToRpi() {
		while (true) {
			try {
				if (!isStillRun) {
					break;
				}
				byte[] inbuf = new byte[256];
				DatagramPacket packet = new DatagramPacket(inbuf, inbuf.length);
				socket.receive(packet);
				System.out.println("EdgeX received a data : " + new String(packet.getData(), 0, packet.getLength()));

				packetData = new String(packet.getData());
				packetData = packetData.replaceFirst("1", "2");
				String data = packetData;

				DatagramPacket packet_to_local = new DatagramPacket(
						data.getBytes()
						, data.getBytes().length
						, InetAddress.getByName("127.0.0.1")
						, 9001
				); // client의 IP와 Port
				socket.send(packet_to_local);
//				DatagramPacket packet_to_rpi = new DatagramPacket(data.getBytes(), data.getBytes().length, InetAddress.getByName("218.150.183.221"), 9002); // client의 IP와 Port
//				socket.send(packet_to_rpi);
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
	}

	public void clientStart() {
		try{
			DatagramSocket ds = new DatagramSocket(9001);

			while(isStillRun){
				byte []buf = new byte[100];

				DatagramPacket dp = new DatagramPacket(
						buf,
						buf.length
						, InetAddress.getByName("127.0.0.1"), // client의 IP
						9001 // client의 Port
				);

				ds.receive(dp);
//				InetAddress addr = dp.getAddress();

				String msg = new String(buf, 0, 13); // , "UTF-8"
				System.out.print("EdgeX sent a data : " + msg);
				System.out.println();
				System.out.println();
				System.out.println();
			}

		}catch(Exception e){
			System.out.println(e);
		}
	}
}