package com.haizhi;

import javafx.util.Pair;

import java.util.List;

/**
 * Created by youfeng on 2017/9/7.
 * 抓取任务调度类
 */
public class CrawlerJob {

    /**
     * 任务优先级 根据任务重要性
     **/
    public static final int PRIORITY_LOW = 1;
    public static final int PRIORITY_NORMAL = 5;
    public static final int PRIORITY_HIGH = 9;
    public static final int PRIORITY_HIGHEST = 99;

    // 调度优先级
    private int priority;

    /**
     * 任务类型 按任务执行粒度设定
     **/
    //短周期任务 1小时以内
    public static final int JOB_TYPE_SHORT = 1;

    //中等周期任务 天级任务
    public static final int JOB_TYPE_MIDDLE = 2;

    //长周期任务 月级别 或者年级别 长期运行任务
    public static final int JOB_TYPE_LONGEST = 3;

    // 任务类型
    private int jobType;

    /**
     * 调度类型
     **/
    //固定周期调度方式 到执行时间点进行申请
    public static final int SCHEDULE_TYPE_NORMAL = 1;

    //弹性时间段调度方式 在指定时间段内才有调度申请机会
    public static final int SCHEDULE_TYPE_FLEX = 2;

    //资源闲时调度方式 检测到资源空闲时 扫描任务进行发起调度申请
    public static final int SCHEDULE_TYPE_IDLE = 3;

    //手动调度方式 通过web界面配置进行启停 一般属于一次性采集脚本 暂搁置
    //public static final int SCHEDULE_TYPE_MANUAL = 4;

    //调度类型
    private int scheduleType;

    //调度时间 固定类型 需要设置的时间 crontab 方式  时间  月 年份 不同的调度情况
    private String scheduleCronTime;

    // 弹性时间调度需要的起始结束时间段 时间段跨度  日 月 年 固定 和弹性时间 使用时间戳确认时间段
    private List<Pair<String, String>> scheduleTimeList;


    /**
     * 当前任务调度状态
     **/

    // init 初始化状态
    public static final int STATUS_INIT = 0;

    //已准备好 可运行状态
    public static final int STATUS_READY = 1;

    //正在执行状态
    public static final int STATUS_RUNNING = 2;

    //任务被暂停状态
    public static final int STATUS_PAUSE = 3;

    // 任务已经调度完成状态
    public static final int STATUS_FINISH = 4;


    //当前任务调度状态 可不可以调度 运行状态
    private int scheduleStatus;


    /**
     * 当前任务是否可以被调度
     **/

    // 任务可调度状态
    public static final int SCHEDULE_ENABLE = 1;

    // 任务不可调度状态
    public static final int SCHEDULE_DISABLE = 0;

    // 任务是否可以被调度
    private int scheduleEnable;


    // 执行次数， 周期性任务  与 有限次数任务
    // 任务成功与失败的定义， 粒度划分 某一个网页失败  算不算失败

    // 任务申请执行次数
    private int applyTimes;

    // 任务批准执行次数
    private int ratifyTimes;


    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getJobType() {
        return jobType;
    }

    public void setJobType(int jobType) {
        this.jobType = jobType;
    }

    public int getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(int scheduleType) {
        this.scheduleType = scheduleType;
    }

    public List<Pair<String, String>> getScheduleTimeList() {
        return scheduleTimeList;
    }

    public void setScheduleTimeList(List<Pair<String, String>> scheduleTimeList) {
        this.scheduleTimeList = scheduleTimeList;
    }

    public int getScheduleStatus() {
        return scheduleStatus;
    }

    public void setScheduleStatus(int scheduleStatus) {
        this.scheduleStatus = scheduleStatus;
    }

    public int getApplyTimes() {
        return applyTimes;
    }

    public void setApplyTimes(int applyTimes) {
        this.applyTimes = applyTimes;
    }

    public int getRatifyTimes() {
        return ratifyTimes;
    }

    public void setRatifyTimes(int ratifyTimes) {
        this.ratifyTimes = ratifyTimes;
    }
}