package algorithm.paxos;

import lombok.Data;

/**
 * @author cheney
 * @date 2020-09-18
 */
@Data
public class Acceptor {

    /**
     * 目前最小可接受的提案编号
     */
    private Long minNo;

    /**
     * 接受的提案编号
     */
    private Long acceptNo;

    /**
     * 接受的提案值
     */
    private int acceptValue;

    public Acceptor() {
    }

    public synchronized void prepare(Proposer proposer) {
        Long no = proposer.getNo();
        if (acceptNo == null) {
            minNo = acceptNo = no;
            proposer.response(null);
        } else if (no > minNo) {
            minNo = no;
            System.out.println("[1]接收提案，已有提案,编号:" + acceptNo);
            proposer.response(new Proposer(proposer.getNo(), proposer.getValue()));
        } else {
            System.out.println("[1]不接收提案");
        }
    }

    public synchronized void accept(Proposer proposer) {
        Long no = proposer.getNo();
        if (no >= minNo) {
            System.out.println("[2]接受提案");
            acceptValue = proposer.getValue();
            acceptNo = proposer.getNo();
            proposer.accept();
        } else {
            System.out.println("[2]不接受提案");
        }
    }

}
