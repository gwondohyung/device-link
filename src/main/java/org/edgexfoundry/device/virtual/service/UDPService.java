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

import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import java.io.IOException; 
import java.net.DatagramPacket; 
import java.net.DatagramSocket; 
import java.net.SocketException;
import java.net.InetAddress;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("unused")
@Repository

public class UDPService {
	private boolean isStillPingPongAlive = true;
	private boolean isStillReceiverAlive = true;
	private boolean isStillSenderAlive = true;

	private String packetData;
	@Value("${service.tempData:20}")
	private String tempData;

	DatagramSocket pingPongSocket;
	DatagramSocket receiverSocket;
	DatagramSocket senderSocket;

	public UDPService() throws SocketException {
		super();
		pingPongSocket = new DatagramSocket(7777); // Server의 IP
		receiverSocket = new DatagramSocket(8888);
		senderSocket = new DatagramSocket();
	}
	
	public void setPingPongState(boolean isStillPingPongAlive) {
		this.isStillPingPongAlive = isStillPingPongAlive;
	}

	public void setReceiverState(boolean isStillReceiverAlive) {
		this.isStillReceiverAlive = isStillReceiverAlive;
	}

	public void setSenderState(boolean isStillSenderAlive) {
		this.isStillSenderAlive = isStillSenderAlive;
	}

	@Async
	public void startPingPong() {
		try {
			System.out.println("UDP Ping Pong started successfully.");
			while (true) {
				if (!isStillPingPongAlive) {
					break;
				}
				byte[] inbuf = new byte[256];
				DatagramPacket receivePacket = new DatagramPacket(inbuf, inbuf.length);
				pingPongSocket.receive(receivePacket);
				System.out.println("EdgeX received a packet: " +
						new String(receivePacket.getData(), 0, receivePacket.getLength()));

				packetData = new String(receivePacket.getData(), 0, receivePacket.getLength());
				StringTokenizer stringTokenizer = new StringTokenizer(packetData, "+");

				int receivedInt1 = Integer.parseInt(stringTokenizer.nextToken());
				int receivedInt2 = Integer.parseInt(stringTokenizer.nextToken());
				String newData = String.valueOf(receivedInt1 + receivedInt2);

				InetAddress clientIPAddress = receivePacket.getAddress();
				int clientPort = receivePacket.getPort();

				DatagramPacket packet_to_rpi = new DatagramPacket(
						newData.getBytes(),
						newData.getBytes().length,
						clientIPAddress,
						clientPort
				); // client의 IP와 Port

				pingPongSocket.send(packet_to_rpi);
				System.out.println("EdgeX sent a packet: " + new String(packet_to_rpi.getData(), 0, packet_to_rpi.getLength()));
				System.out.println();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			isStillPingPongAlive = false;
		}
	}

//	@Async
//	public void startReceiver() {
//		try {
//			System.out.println("UDP Receiver started successfully.");
//			while (true) {
//				if (!isStillReceiverAlive) {
//					break;
//				}
//				byte[] inbuf = new byte[256];
//				DatagramPacket receivePacket = new DatagramPacket(inbuf, inbuf.length);
//				receiverSocket.receive(receivePacket);
//				System.out.println("EdgeX received a packet: " +
//						new String(receivePacket.getData(), 0, receivePacket.getLength()));
//				System.out.println();
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//			isStillReceiverAlive = false;
//		}
//	}

	@Async
	public void startReceiver(String clientAddr, int clientPort) {
		try {
			System.out.println("UDP Receiver started successfully.");
			while (true) {
				if (!isStillReceiverAlive) {
					break;
				}
				InetAddress clientIPAddress = InetAddress.getByName(clientAddr); // For Sending To Application

				byte[] inbuf = new byte[256];
				DatagramPacket receivePacket = new DatagramPacket(inbuf, inbuf.length);
				receiverSocket.receive(receivePacket);

				String newData = new String(receivePacket.getData(), 0, receivePacket.getLength());
				DatagramPacket packet_to_rpi = new DatagramPacket(
						newData.getBytes(),
						newData.getBytes().length,
						clientIPAddress,
						clientPort
				); // client의 IP와 Port

				senderSocket.send(packet_to_rpi); // Send to Application
				System.out.println("EdgeX received a packet: " +
						new String(receivePacket.getData(), 0, receivePacket.getLength()));
				System.out.println();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			isStillReceiverAlive = false;
		}
	}

	@Async
	public void startSender(String clientAddr, int clientPort) {
		try {
			System.out.println("UDP Sender started successfully.");
			while (true) {
				if (!isStillSenderAlive) {
					break;
				}
				int randomNum = ThreadLocalRandom.current().nextInt(0, 10000 + 1);

				String newData = String.valueOf(randomNum);

				InetAddress clientIPAddress = InetAddress.getByName(clientAddr);

				DatagramPacket packet_to_rpi = new DatagramPacket(
						newData.getBytes(),
						newData.getBytes().length,
						clientIPAddress,
						clientPort
				); // client의 IP와 Port

				senderSocket.send(packet_to_rpi);
				System.out.println("EdgeX sent a packet: " + new String(packet_to_rpi.getData(), 0, packet_to_rpi.getLength()));
				System.out.println();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			isStillSenderAlive = false;
		}
	}
}