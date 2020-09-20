package algorithm.paxos;

/**
 * @author cheney
 * @date 2020-09-18
 */
public class Main {

    public static void main(String[] args) {
        PaxosSimulation paxosSimulation = new PaxosSimulation();
        paxosSimulation.init(10,10);
        paxosSimulation.start();
        System.out.println("-----------------");
        paxosSimulation.getProposers().forEach(e -> System.out.println(e.getValue()));
    }

}
