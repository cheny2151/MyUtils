package algorithm.paxos;

import lombok.Data;
import org.apache.commons.lang3.RandomUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author cheney
 * @date 2020-09-18
 */
@Data
public class Proposer {

    private String proposerId;

    /**
     * 初始编号
     */
    private Long initNo;

    /**
     * 提案编号
     */
    private Long no;

    /**
     * 提案值
     */
    private int value;

    /**
     * 第一阶段响应的提案
     */
    private List<Proposer> responseProposers;

    /**
     * 提案被接受的次数
     */
    private AtomicInteger acceptCount;

    public Proposer(int proposerId) {
        StringBuilder proposerIdStr = new StringBuilder(String.valueOf(proposerId));
        int length = proposerIdStr.length();
        if (length < 3) {
            for (int i = 0; i < 3 - length; i++) {
                proposerIdStr.insert(0, "0");
            }
        }
        this.proposerId = proposerIdStr.toString();
        this.initNo = Long.valueOf(DateTimeFormatter.ofPattern("MMddHHmmssSS").format(LocalDateTime.now()));
        responseProposers = new ArrayList<>();
    }

    public Proposer(Long no, int value) {
        this.no = no;
        this.value = value;
    }

    public Proposer newProposer() {
        no = Long.valueOf(initNo++ + "" + proposerId);
        value = RandomUtils.nextInt(1, Integer.MAX_VALUE);
        acceptCount = new AtomicInteger(0);
        Optional<Proposer> originMax = responseProposers.stream().max(Comparator.comparingLong(Proposer::getNo));
        responseProposers.clear();
        originMax.ifPresent(proposer -> responseProposers.add(proposer));
        return this;
    }

    public void response(Proposer proposer) {
        if (proposer != null) {
            synchronized (this) {
                responseProposers.add(proposer);
            }
        }
    }

    public synchronized Proposer propose() {
        if (responseProposers.size() != 0) {
            // 选择第一阶段响应的最大提案编号的值作为提案值
            Proposer proposer = responseProposers.stream().max(Comparator.comparingLong(Proposer::getNo)).get();
            int value = proposer.getValue();
            this.setValue(value);
        }
        return this;
    }

    public int accept() {
        return this.acceptCount.incrementAndGet();
    }

    public void learn(Proposer proposer) {
        if (proposer != this) {
            this.setValue(proposer.getValue());
        }
    }

}
