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
    private volatile Long minNo;

    /**
     * 接受的提案编号
     */
    private volatile Long acceptNo;

    /**
     * 接受的提案值
     */
    private int acceptValue;

    public Acceptor() {
    }

    public synchronized void prepare(Proposer proposer) {
        Long no = proposer.getNo();
        if (minNo == null || no > minNo) {
            minNo = no;
            if (acceptNo == null) {
                proposer.response(null);
            } else {
                proposer.response(new Proposer(acceptNo, acceptValue));
            }
        }
    }

    public synchronized void accept(Proposer proposer) {
        Long no = proposer.getNo();
        if (no >= minNo) {
            acceptValue = proposer.getValue();
            acceptNo = proposer.getNo();
            proposer.accept();
        }
    }

}
