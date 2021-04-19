package grpc.helloword.proto;

import grpc.helloword.server.HelloRequest;

/**
 * @Date 2021/4/19
 * @Created by chenyi
 */
public class Main {

    public static void main(String[] args) {
        HelloRequest request = HelloRequest.newBuilder()
                .setName("test")
                .build();
        System.out.println(request.toByteString());
    }

}
