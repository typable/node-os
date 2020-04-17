package com.prototype.factory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.ServerSocket;
import java.nio.file.Path;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import com.prototype.http.HTTPServer;


public class HTTPServerFactory
{
	public static HTTPServer create(int port) throws Exception
	{
		if(port == 443)
		{
			throw new IllegalArgumentException("The port '443' is only for HTTPS permitted!");
		}

		return create(port, false, null, null);
	}

	public static HTTPServer create(int port, boolean ssl, Path key, String password) throws Exception
	{
		ServerSocket serverSocket = null;

		if(ssl)
		{
			if(key == null)
			{
				throw new IllegalArgumentException("The argument 'key' is not defined!");
			}

			if(password == null)
			{
				throw new IllegalArgumentException("The argument 'password' is not defined!");
			}

			File keyFile = key.toFile();

			if(!keyFile.exists())
			{
				throw new FileNotFoundException("The file '" + keyFile
				      .getName() + "' could not be found!");
			}

			serverSocket = createSSLServerSocket(keyFile, password);
		}
		else
		{
			serverSocket = new ServerSocket(port);
		}

		return new HTTPServer(serverSocket);
	}

	private static ServerSocket createSSLServerSocket(File key, String password) throws Exception
	{
		KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		keyStore.load(new FileInputStream(key), password.toCharArray());

		SSLContext context = SSLContext.getInstance("SSL");

		KeyManagerFactory keyManagerFactory = KeyManagerFactory
		      .getInstance(KeyManagerFactory.getDefaultAlgorithm());
		keyManagerFactory.init(keyStore, password.toCharArray());

		TrustManagerFactory trustManagerFactory = TrustManagerFactory
		      .getInstance(TrustManagerFactory.getDefaultAlgorithm());
		trustManagerFactory.init(keyStore);

		context
		      .init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

		SSLServerSocketFactory sslServerSocketFactory = context.getServerSocketFactory();

		SSLServerSocket sslServerSocket = (SSLServerSocket) sslServerSocketFactory
		      .createServerSocket(443);
		sslServerSocket.setEnabledProtocols(new String[] { "TLSv1", "TLSv1.1", "TLSv1.2" });
		sslServerSocket.setEnabledCipherSuites(sslServerSocket.getSupportedCipherSuites());
		sslServerSocket.setUseClientMode(false);
		sslServerSocket.setNeedClientAuth(true);
		sslServerSocket.setWantClientAuth(true);
		sslServerSocket.setEnableSessionCreation(true);

		return sslServerSocket;
	}
}
