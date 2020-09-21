package socket;

import lombok.Data;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

/**
 * @author cheney
 * @date 2020-09-21
 */
@Data
public class MultiIoSocketServer {

    private Selector selector;

    private int port;

    @SneakyThrows
    public MultiIoSocketServer(int port) {
        this.port = port;
    }

    public void registerSelect() throws IOException {
        this.selector = Selector.open();
        ServerSocketChannel socketChannel = ServerSocketChannel.open();
        SocketAddress address = new InetSocketAddress(port);
        socketChannel.socket().bind(address);
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_ACCEPT);
        select();
    }


    public void select() throws IOException {
        while (true) {
            int select = selector.select();
            if (select > 0) {
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                for (SelectionKey selectionKey : selectionKeys) {
                    if (selectionKey.isAcceptable()) {
                        accept(selectionKey);
                    } else if (selectionKey.isConnectable()) {
                        connect(selectionKey);
                    } else if (selectionKey.isReadable()) {
                        read(selectionKey);
                    } else if (selectionKey.isWritable()) {
                        write(selectionKey);
                    }
                }
            }
        }
    }

    private void write(SelectionKey selectionKey) {
        System.out.println("write");
    }

    private void connect(SelectionKey selectionKey) {
        System.out.println("connect");
    }

    public void read(SelectionKey selectionKey) throws IOException {
        SocketChannel channel = (SocketChannel) selectionKey.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        byte[] bytes = new byte[1024];
        int len;
        StringBuilder msg = new StringBuilder();
        while ((len = channel.read(buffer)) > 0) {
            buffer.flip();
            buffer.get(bytes, 0, len);
            msg.append(new String(bytes, 0, len));
        }
        System.out.println(msg.toString());
    }

    public void accept(SelectionKey selectionKey) throws IOException {
        ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();
        SocketChannel acceptChannel = channel.accept();
        if (acceptChannel != null) {
            acceptChannel.configureBlocking(false);
            acceptChannel.register(this.selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            System.out.println("socket 连接成功");
            send(acceptChannel, "test");
        }
    }

    public void send(SocketChannel socketChannel, String msg) throws IOException {
        msg = "access-control-allow-credentials: true\n" +
                "access-control-allow-headers: X-Playlog-Web\n" +
                "access-control-allow-origin: https://translate.google.cn\n" +
                "alt-svc: h3-Q050=\":443\"; ma=2592000,h3-29=\":443\"; ma=2592000,h3-27=\":443\"; ma=2592000,h3-T051=\":443\"; ma=2592000,h3-T050=\":443\"; ma=2592000,h3-Q046=\":443\"; ma=2592000,h3-Q043=\":443\"; ma=2592000,quic=\":443\"; ma=2592000; v=\"46,43\"\n" +
                "cache-control: private\n" +
                "content-encoding: gzip\n" +
                "content-length: 131\n" +
                "content-type: text/plain; charset=UTF-8\n" +
                "date: Mon, 21 Sep 2020 06:14:42 GMT\n" +
                "expires: Mon, 21 Sep 2020 06:14:42 GMT\n" +
                "server: Playlog\n" +
                "set-cookie: __Secure-3PSIDCC=AJi4QfEbvVpBOCvXYVAWNLMXw58KrPF6dmEAPEGP7BLwjlnUQcWOE_pvZPzS3rEE7edCJR3a49w; expires=Tue, 21-Sep-2021 06:14:42 GMT; path=/; domain=.google.com; Secure; HttpOnly; priority=high; SameSite=none\n" +
                "status: 200\n" +
                "x-frame-options: SAMEORIGIN\n" +
                "x-xss-protection: 0";
        System.out.println("send");
        socketChannel.write(ByteBuffer.wrap(msg.getBytes()));
    }

}
