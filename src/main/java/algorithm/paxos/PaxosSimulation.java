package algorithm.paxos;

import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * paxos模拟器
 *
 * @author cheney
 * @date 2020-09-18
 */
@Data
public class PaxosSimulation {

    private List<Proposer> proposers;

    private List<Acceptor> acceptors;

    private Proposer proposer;

    public void init(int n, int n1) {
        // 初始化提案者
        proposers = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            proposers.add(new Proposer(i));
        }
        // 初始化接收者
        acceptors = new ArrayList<>();
        for (int i = 0; i < n1; i++) {
            acceptors.add(new Acceptor());
        }
    }

    /**
     * 开始选举
     *
     * @return
     */
    public Proposer start() {
        if (CollectionUtils.isEmpty(proposers) || CollectionUtils.isEmpty(acceptors)) {
            throw new RuntimeException("未初始化Proposer/Acceptor");
        }
        int half = (int) Math.ceil((double) acceptors.size() / 2);
        ExecutorService executorService = Executors.newFixedThreadPool(proposers.size());
        ArrayList<Callable<Proposer>> tasks = new ArrayList<>();
        for (Proposer proposer : proposers) {
            tasks.add(() -> {
                while (true) {
                    Proposer newProposer = proposer.newProposer();
                    // 模拟prepare请求，发送给超过一半的Accept
                    List<Acceptor> acceptors = randomAcceptor();
                    for (Acceptor acceptor : acceptors) {
                        acceptor.prepare(newProposer);
                    }
                    // 模拟第二阶段 propose请求
                    for (Acceptor acceptor : acceptors) {
                        acceptor.accept(newProposer.propose());
                        if (newProposer.getAcceptCount().get() >= half) {
                            setProposer(newProposer, executorService);
                            return newProposer;
                        }
                    }
                }
            });
        }
        try {
            List<Future<Proposer>> futures = executorService.invokeAll(tasks);
            futures.forEach(e -> {
                try {
                    e.get();
                } catch (Exception interruptedException) {
                    interruptedException.printStackTrace();
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 模拟learn
        proposers.forEach(e -> e.learn(proposer));
        return proposer;
    }

    public List<Acceptor> randomAcceptor() {
        ArrayList<Acceptor> acceptors = new ArrayList<>(this.acceptors);
        int size = this.acceptors.size();
        // 超过一半的数
        int s = RandomUtils.nextInt((int) Math.ceil((double) size / 2), size);
        for (int i = 0; i < s; i++) {
            acceptors.remove(RandomUtils.nextInt(0, acceptors.size()));
        }
        return acceptors;
    }

    public synchronized void setProposer(Proposer proposer, ExecutorService executorService) {
        if (this.proposer == null) {
            System.out.println("选举成功,提案：no:" + proposer.getNo() + "，提案值:" + proposer.getValue());
            this.proposer = proposer;
            executorService.shutdownNow();
        } else {
            System.out.println("已有提案选举成功");
        }
    }

}
