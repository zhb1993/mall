package com.yami.shop.common.util;

import cn.hutool.core.date.SystemClock;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 订单号生成工具
 */
@Component
public class OrderNoWorker {

//    @Autowired
//    OrdernoWorkConfig ordernoWorkConfig;

    private final String pattern = "yyMMddHHmmssSSS";
    private final long maxWorkerId = 100L;

    private long sequence = 0L;
    private final long maxSequence = 100L;

    private LocalDateTime lastDateTime = LocalDateTime.now();

    public OrderNoWorker() {

    }

    public synchronized String nextOrderNo() {
        LocalDateTime dateTime = this.dateTimeGen();
        if (dateTime.isEqual(lastDateTime)) {
            this.sequence = this.sequence + 1;
            if (this.sequence >= this.maxSequence) {
                this.sequence = 0L;
                dateTime = this.nextDateTime(this.lastDateTime);
            }
        } else {
            this.sequence = 0L;
        }

        this.lastDateTime = dateTime;
//        Long workerId = ordernoWorkConfig.getId();
        int workerId = RandomUtils.nextInt(99999,Integer.MAX_VALUE);
       /* if (workerId > maxWorkerId) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }*/
        return dateTime.format(DateTimeFormatter.ofPattern(pattern)) + StringUtils.leftPad(String.valueOf(workerId), 5, "0") /*+ StringUtils.leftPad(String.valueOf(this.sequence), 2, "0")*/;
    }

    private LocalDateTime dateTimeGen() {
        return LocalDateTime.now();
    }

    private LocalDateTime nextDateTime(final LocalDateTime lastDateTime) {
        LocalDateTime dateTime = this.dateTimeGen();
        while (dateTime.isBefore(lastDateTime) || dateTime.isEqual(lastDateTime)) {
            dateTime = this.dateTimeGen();
        }
        return dateTime;
    }

    public static void main(String[] args) {


        String filename= RandomStringUtils.randomNumeric(8);

        int i1 = RandomUtils.nextInt(99999999,Integer.MAX_VALUE);
        int max = 3;
        int temp = new Random().nextInt(max);
        System.out.println(temp);
        int radom = (int)(Math.random()*6);
        UUID uuid = UUID.randomUUID();
        long now = SystemClock.now();
        long l1 = System.currentTimeMillis()*1000;

        long l = System.nanoTime();
        System.out.println(l);

         Long cutime = System.currentTimeMillis() * 1000; // 微秒
         Long nanoTime = System.nanoTime(); // 纳秒
         Long a = cutime + (nanoTime - nanoTime / 1000000 * 1000000) / 1000;



        ExecutorService service = Executors.newFixedThreadPool(100);
        Map<String, String> hashmap = Maps.newConcurrentMap();

        OrderNoWorker orderNoGenerator = new OrderNoWorker();
        for (int i = 0; i < 100; i++) {
            service.execute(() -> {
                while (true) {
                    String orderNo = orderNoGenerator.nextOrderNo();
                    System.out.println(orderNo);
                    String absent = hashmap.putIfAbsent(orderNo, orderNo);
                    if (null != absent) {
                        System.out.println(absent + " 重复");
                    }
                }
            });
        }

        while (true) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
