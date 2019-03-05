package wesocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import wesocket.handler.FirstChannelHandler;

public class MyWebsocketServer {

    private ServerBootstrap serverBootstrap;

    private FirstChannelHandler firstChannelHandler = new FirstChannelHandler();

    private int port;

    public MyWebsocketServer(int port) {
        this.port = port;
        init();
    }

    private void init() {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        nioSocketChannel.pipeline().addLast(firstChannelHandler);
                    }
                });
        this.serverBootstrap = serverBootstrap;
    }

    public void start() {
        serverBootstrap.bind(port).addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                System.out.println("server start result is " + future.isSuccess());
            }
        });
    }

}
