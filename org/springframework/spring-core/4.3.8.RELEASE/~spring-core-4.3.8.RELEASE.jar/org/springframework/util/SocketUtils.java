// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SocketUtils.java

package org.springframework.util;

import java.net.*;
import java.util.*;
import javax.net.ServerSocketFactory;

// Referenced classes of package org.springframework.util:
//			Assert

public class SocketUtils
{
	private static abstract class SocketType extends Enum
	{

		public static final SocketType TCP;
		public static final SocketType UDP;
		private static final SocketType $VALUES[];

		public static SocketType[] values()
		{
			return (SocketType[])$VALUES.clone();
		}

		public static SocketType valueOf(String name)
		{
			return (SocketType)Enum.valueOf(org/springframework/util/SocketUtils$SocketType, name);
		}

		protected abstract boolean isPortAvailable(int i);

		private int findRandomPort(int minPort, int maxPort)
		{
			int portRange = maxPort - minPort;
			return minPort + SocketUtils.random.nextInt(portRange + 1);
		}

		int findAvailablePort(int minPort, int maxPort)
		{
			Assert.isTrue(minPort > 0, "'minPort' must be greater than 0");
			Assert.isTrue(maxPort >= minPort, "'maxPort' must be greater than or equals 'minPort'");
			Assert.isTrue(maxPort <= 65535, "'maxPort' must be less than or equal to 65535");
			int portRange = maxPort - minPort;
			int searchCounter = 0;
			int candidatePort;
			do
			{
				if (++searchCounter > portRange)
					throw new IllegalStateException(String.format("Could not find an available %s port in the range [%d, %d] after %d attempts", new Object[] {
						name(), Integer.valueOf(minPort), Integer.valueOf(maxPort), Integer.valueOf(searchCounter)
					}));
				candidatePort = findRandomPort(minPort, maxPort);
			} while (!isPortAvailable(candidatePort));
			return candidatePort;
		}

		SortedSet findAvailablePorts(int numRequested, int minPort, int maxPort)
		{
			Assert.isTrue(minPort > 0, "'minPort' must be greater than 0");
			Assert.isTrue(maxPort > minPort, "'maxPort' must be greater than 'minPort'");
			Assert.isTrue(maxPort <= 65535, "'maxPort' must be less than or equal to 65535");
			Assert.isTrue(numRequested > 0, "'numRequested' must be greater than 0");
			Assert.isTrue(maxPort - minPort >= numRequested, "'numRequested' must not be greater than 'maxPort' - 'minPort'");
			SortedSet availablePorts = new TreeSet();
			for (int attemptCount = 0; ++attemptCount <= numRequested + 100 && availablePorts.size() < numRequested; availablePorts.add(Integer.valueOf(findAvailablePort(minPort, maxPort))));
			if (availablePorts.size() != numRequested)
				throw new IllegalStateException(String.format("Could not find %d available %s ports in the range [%d, %d]", new Object[] {
					Integer.valueOf(numRequested), name(), Integer.valueOf(minPort), Integer.valueOf(maxPort)
				}));
			else
				return availablePorts;
		}

		static 
		{
			TCP = new SocketType("TCP", 0) {

				protected boolean isPortAvailable(int port)
				{
					ServerSocket serverSocket = ServerSocketFactory.getDefault().createServerSocket(port, 1, InetAddress.getByName("localhost"));
					serverSocket.close();
					return true;
					Exception ex;
					ex;
					return false;
				}

			};
			UDP = new SocketType("UDP", 1) {

				protected boolean isPortAvailable(int port)
				{
					DatagramSocket socket = new DatagramSocket(port, InetAddress.getByName("localhost"));
					socket.close();
					return true;
					Exception ex;
					ex;
					return false;
				}

			};
			$VALUES = (new SocketType[] {
				TCP, UDP
			});
		}

		private SocketType(String s, int i)
		{
			super(s, i);
		}

	}


	public static final int PORT_RANGE_MIN = 1024;
	public static final int PORT_RANGE_MAX = 65535;
	private static final Random random = new Random(System.currentTimeMillis());

	public SocketUtils()
	{
	}

	public static int findAvailableTcpPort()
	{
		return findAvailableTcpPort(1024);
	}

	public static int findAvailableTcpPort(int minPort)
	{
		return findAvailableTcpPort(minPort, 65535);
	}

	public static int findAvailableTcpPort(int minPort, int maxPort)
	{
		return SocketType.TCP.findAvailablePort(minPort, maxPort);
	}

	public static SortedSet findAvailableTcpPorts(int numRequested)
	{
		return findAvailableTcpPorts(numRequested, 1024, 65535);
	}

	public static SortedSet findAvailableTcpPorts(int numRequested, int minPort, int maxPort)
	{
		return SocketType.TCP.findAvailablePorts(numRequested, minPort, maxPort);
	}

	public static int findAvailableUdpPort()
	{
		return findAvailableUdpPort(1024);
	}

	public static int findAvailableUdpPort(int minPort)
	{
		return findAvailableUdpPort(minPort, 65535);
	}

	public static int findAvailableUdpPort(int minPort, int maxPort)
	{
		return SocketType.UDP.findAvailablePort(minPort, maxPort);
	}

	public static SortedSet findAvailableUdpPorts(int numRequested)
	{
		return findAvailableUdpPorts(numRequested, 1024, 65535);
	}

	public static SortedSet findAvailableUdpPorts(int numRequested, int minPort, int maxPort)
	{
		return SocketType.UDP.findAvailablePorts(numRequested, minPort, maxPort);
	}


}
