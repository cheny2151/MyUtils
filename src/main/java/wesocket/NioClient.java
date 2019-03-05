package wesocket;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.charset.Charset;

public class NioClient {

    public void start() {
        NioEventLoopGroup group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            }

                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf byteBuf = (ByteBuf) msg;
                                System.out.println(byteBuf.toString(Charset.forName("utf-8")));
                            }
                        });
                    }
                });

        ChannelFuture localhost = bootstrap.connect("localhost", 8080);
        localhost.addListener(future -> System.out.println("client connect " + future.isSuccess()));
        ByteBuf buffer = localhost.channel().alloc().buffer();
        ByteBuf byteBuf = buffer.writeBytes("request".getBytes(Charset.forName("utf-8")));
        localhost.channel().writeAndFlush(byteBuf);
        localhost.channel().close();
    }

    public static void main(String[] args) {
        new NioClient().start();
    }
}
