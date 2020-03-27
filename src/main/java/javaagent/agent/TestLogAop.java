package javaagent.agent;

import com.sun.tools.attach.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * @author cheney
 * @date 2020-03-25
 */
public class TestLogAop {

    private final static String TARGET_DISPLAY_NAME = "com.cheney.ApplicationContext";

    private final static String AGENT_PATH = "C:\\Users\\Cheney\\IdeaProjects\\Instrument\\target\\Instrument-1.0-SNAPSHOT-jar-with-dependencies.jar";

    public static void main(String[] args) throws IOException, AttachNotSupportedException, AgentLoadException, AgentInitializationException {
        List<VirtualMachineDescriptor> list = VirtualMachine.list();
        Optional<VirtualMachineDescriptor> vmOptional = list.stream().filter(e -> TARGET_DISPLAY_NAME.equals(e.displayName())).findFirst();
        if (vmOptional.isPresent()) {
            VirtualMachine attach = VirtualMachine.attach(vmOptional.get());
            attach.loadAgent(AGENT_PATH);
            attach.detach();
        }
    }

}
