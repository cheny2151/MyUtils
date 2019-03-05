package wesocket.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import sun.misc.CompoundEnumeration;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Enumeration;

@ChannelHandler.Sharable
public class FirstChannelHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println(byteBuf.toString(Charset.forName("utf-8")));
        ByteBuf buffer = ctx.alloc().buffer();
        buffer.writeBytes("response".getBytes(Charset.forName("utf-8")));
        ctx.writeAndFlush(buffer);
    }

    @Test
    public void test(){
        String s = DigestUtils.md5Hex("1985100");
        System.out.println(s);
    }

    @Test
    public void test2() throws IOException {
        Enumeration<URL> resources = this.getClass().getClassLoader().getResources("");
        CompoundEnumeration resources1 = (CompoundEnumeration) resources;
        Object o = resources1.nextElement();
        System.out.println(o);
       /* while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            System.out.println(url);
        }*/
    }

}
