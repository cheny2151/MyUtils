package grpc.helloword.client;

import grpc.helloword.server.HelloReply;
import grpc.helloword.server.HelloRequest;
import grpc.helloword.server.HelloWorldServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.netty.shaded.io.grpc.netty.NegotiationType;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;

/**
 * @Date 2021/4/19
 * @Created by chenyi
 */
public class HelloWorldClient {

    public static void main(String[] args) {
        ManagedChannel channel = NettyChannelBuilder.forAddress("localhost", 50051)
                .negotiationType(NegotiationType.PLAINTEXT)
                .build();
        HelloWorldServiceGrpc.HelloWorldServiceBlockingStub blockingStub = HelloWorldServiceGrpc.newBlockingStub(channel);
        HelloReply test = blockingStub.sayHello(HelloRequest.newBuilder().setName("hello").build());
        System.out.println(test.getMessage());
    }

}
