package socket;

import java.io.IOException;

/**
 * @author cheney
 * @date 2020-09-21
 */
public class main {

    public static void main(String[] args) throws IOException {
        MultiIoSocketServer multiIoSocketServer = new MultiIoSocketServer(8080);
        multiIoSocketServer.registerSelect();
    }

}
