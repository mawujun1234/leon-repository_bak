package com.mawujun.repository.identity.generator;

import java.util.Calendar;

/**
 * 单机，取消数据中心，只有普通应用或集群的时候用的
 * @author admin
 *
 */
public class LongIdUtils {
	 /**
     * 雪花算法解析 结构 snowflake的结构如下(每部分用-分开):
     * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000
     * 第一位为未使用，接下来的41位为毫秒级时间(41位的长度可以使用69年)，然后是5位datacenterId和5位workerId(10
     * 位的长度最多支持部署1024个节点） ，最后12位是毫秒内的计数（12位的计数顺序号支持每个节点每毫秒产生4096个ID序号）
     * 
     * 一共加起来刚好64位，为一个Long型。(转换成字符串长度为18) 
     * 
     */
 
    // ==============================Fields===========================================
    /** 开始时间截 (2018-01-01) */
	 private final long twepoch=1541001600000L;
 
    /** 机器id所占的位数 */
    private final long workerIdBits = 2L;
 
    /** 数据标识id所占的位数 */
    private final long dataCenterIdBits = 8L;
 
    /** 支持的最大机器id，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数) */
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);
 
    /** 支持的最大数据标识id，结果是31 */
    private final long maxDataCenterId = -1L ^ (-1L << dataCenterIdBits);
 
    /** 序列在id中占的位数 */
    private final long sequenceBits = 12L;
 
    /** 机器ID向左移12位 */
    private final long workerIdShift = sequenceBits;
 
    /** 数据标识id向左移17位(12+5) */
    private final long dataCenterIdShift = sequenceBits + workerIdBits;
 
    /** 时间截向左移22位(5+5+12) */
    private final long timestampLeftShift = sequenceBits + workerIdBits + dataCenterIdBits;
 
    /** 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095) */
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);
 
    /** 工作机器ID(0~3) */
    private long workerId;//backwords
 
    /** 数据中心ID(0~255) */
    private long dataCenterId;
 
    /** 毫秒内序列(0~4095) */
    private long sequence = 0L;
 
    /** 上次生成ID的时间截 */
    private long lastTimestamp = -1L;
    /**
     * 当出现时间回拨的时候，允许等待的最大长度,单位毫秒
     */
    private long maxsleeptime=300;
 
    // ==============================Constructors=====================================
//    /**
//     * 构造函数
//     * @param workerId 工作ID (0~31)
//     * @param dataCenterId 数据中心ID (0~31)
//     */
//    public SnowFlakeUtils(long workerId, long dataCenterId) {
//        if (workerId > maxWorkerId || workerId < 0) {
//            throw new IllegalArgumentException(String.format("workerId can't be greater than %d or less than 0", maxWorkerId));
//        }
//        if (dataCenterId > maxDataCenterId || dataCenterId < 0) {
//            throw new IllegalArgumentException(String.format("dataCenterId can't be greater than %d or less than 0", maxDataCenterId));
//        }
//        this.workerId = workerId;
//        this.dataCenterId = dataCenterId;
//        
//    }

    /**
     * 只适用于本机的时候，如果使用集群的时候，不能用这个
     */
    public LongIdUtils() {
    	this.workerId = 0;
        this.dataCenterId = 0;
    }
 
    // ==============================Methods==========================================
    /**
     * 获得下一个ID (该方法是线程安全的)
     * @return SnowflakeId
     */
    public synchronized long nextId() {
        long timestamp = timeGen();
 
        // 如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
//        if (timestamp < lastTimestamp) {
//            throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
//        }
        if (timestamp < lastTimestamp) {
	        if (timestamp - lastTimestamp<=maxsleeptime) {
	        	try {
					Thread.sleep(maxsleeptime);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	timestamp = timeGen();
	        	if (timestamp < lastTimestamp) {
	        		workerId+=1;
	        		if (workerId > maxWorkerId || workerId < 0) {
	                    throw new RuntimeException(String.format("workerId can't be greater than %d or less than 0", maxWorkerId));
	                }	        		    		
	        	}
	        } else {
	        	workerId+=1;
	        	if (workerId > maxWorkerId || workerId < 0) {
                    throw new RuntimeException(String.format("workerId can't be greater than %d or less than 0", maxWorkerId));
                }
        		
	        }
        }
 
        // 如果是同一时间生成的，则进行毫秒内序列
        // sequenceMask 为啥是4095  2^12 = 4096
        if (lastTimestamp == timestamp) {
            // 每次+1
            sequence = (sequence + 1) & sequenceMask;
            // 毫秒内序列溢出
            if (sequence == 0) {
                // 阻塞到下一个毫秒,获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        }
        // 时间戳改变，毫秒内序列重置
        else {
            sequence = 0L;
        }
 
        // 上次生成ID的时间截
        lastTimestamp = timestamp;
        // 移位并通过或运算拼到一起组成64位的ID
        // 为啥时间戳减法向左移动22 位 因为  5位datacenterid 
        // 为啥 datCenterID向左移动17位 因为 前面有5位workid  还有12位序列号 就是17位
        //为啥 workerId向左移动12位 因为 前面有12位序列号 就是12位 
//        System.out.println(((timestamp - twepoch) << timestampLeftShift) //
//                | (dataCenterId << dataCenterIdShift) //
//                | (workerId << workerIdShift) //
//                | sequence);
        return ((timestamp - twepoch) << timestampLeftShift) //
                | (dataCenterId << dataCenterIdShift) //
                | (workerId << workerIdShift) //
                | sequence;
    }
 
    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }
 
    /**
     * 返回以毫秒为单位的当前时间
     * @return 当前时间(毫秒)
     */
    protected long timeGen() {
        return System.currentTimeMillis();
    }
 
    // ==============================Test=============================================
    /** 测试 */
    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());
        LongIdUtils idWorker = new LongIdUtils();
        long startTime = System.nanoTime();
        for (long i = 0; i < 50000; i++) {
            long id = idWorker.nextId();
            System.out.println(id);
        	
        }
        System.out.println((System.nanoTime() - startTime) / 1000000 + "ms");
    }
    
}
