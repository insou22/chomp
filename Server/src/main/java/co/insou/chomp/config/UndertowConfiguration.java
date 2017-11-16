package co.insou.chomp.config;

public interface UndertowConfiguration {

	int getPort();

	void setPort(int port);

	String getHostname();

	void setHostname(String hostname);

	int getIoThreads();

	void setIoThreads(int ioThreads);

	int getWorkerThreads();

	void setWorkerThreads(int workerThreads);

}
