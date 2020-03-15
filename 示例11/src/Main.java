import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        bio(12301);
        nio(12302);
        aio(12303);
        // And then, you can use telnet to send message to those posts.
        // telnet 127.0.0.1 12301
    }

    public static void bio(int port) {
        Runnable bioRead = new Runnable() {
            @Override
            public void run() {
                try (ServerSocket serverSocket = new ServerSocket(port)) {
                    Socket socket = serverSocket.accept();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    bufferedReader.lines().forEach(System.out::println);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread bioReadThread = new Thread(bioRead);
        bioReadThread.start();
    }

    public static void nio(int port) {
        Runnable mioRead = new Runnable() {
            @Override
            public void run() {
                try (Selector selector = Selector.open();
                     ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
                    serverSocketChannel.configureBlocking(false);
                    ServerSocket serverSocket = serverSocketChannel.socket();
                    InetSocketAddress inetSocketAddress = new InetSocketAddress(port);
                    serverSocket.bind(inetSocketAddress);
                    serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
                    while (true) {
                        selector.select();
                        Set<SelectionKey> selectionKeys = selector.selectedKeys();
                        Iterator<SelectionKey> iterator = selectionKeys.iterator();
                        while (iterator.hasNext()) {
                            SelectionKey key = iterator.next();
                            if (key.isAcceptable()) {
                                ServerSocketChannel scc = (ServerSocketChannel) key.channel();
                                SocketChannel socketChannel = scc.accept();
                                socketChannel.configureBlocking(false);
                                socketChannel.register(selector, SelectionKey.OP_READ);
                            } else if (key.isReadable()) {
                                SocketChannel socketChannel = (SocketChannel) key.channel();
                                while (true) {
                                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                                    if (socketChannel.read(byteBuffer) <= 0) {
                                        break;
                                    }
                                    byteBuffer.flip();
                                    Charset charset = StandardCharsets.UTF_8;
                                    System.out.print(charset.newDecoder().decode(byteBuffer).toString());
                                }
                            }
                            iterator.remove();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread mioReadThread = new Thread(mioRead);
        mioReadThread.start();
    }

    public static void aio(int port) {
        Runnable aioRead = new Runnable() {
            @Override
            public void run() {
                try {
                    AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel.open();
                    serverSocketChannel.bind(new InetSocketAddress(port));
                    CompletionHandler<AsynchronousSocketChannel, Object> handler = new CompletionHandler<AsynchronousSocketChannel, Object>() {
                        @Override
                        public void completed(final AsynchronousSocketChannel result, final Object attachment) {
                            serverSocketChannel.accept(attachment, this);
                            try {
                                while (true) {
                                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                                    if (result.read(byteBuffer).get() < 0) {
                                        break;
                                    }
                                    byteBuffer.flip();
                                    Charset charset = StandardCharsets.UTF_8;
                                    System.out.print(charset.newDecoder().decode(byteBuffer).toString());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void failed(final Throwable exc, final Object attachment) {
                            System.out.println("ERROR" + exc.getMessage());
                        }
                    };
                    serverSocketChannel.accept(null, handler);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        Thread aioReadThread = new Thread(aioRead);
        aioReadThread.start();
    }
}

