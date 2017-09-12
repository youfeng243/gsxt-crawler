package com.haizhi;

import javafx.util.Pair;

import java.util.List;

/**
 * Created by youfeng on 2017/9/7.
 * 抓取任务调度类
 */
public class ScheduleJob {

    //任务名称
    private String jobName;

    /**
     * 任务类型
     */
    //系统级任务 走不通的任务运行通道 重要任务放入系统级
    public static final int SYSTEM_JOB = 0;

    //常规任务
    public static final int NORMAL_JOB = 1;

    private int jobType;


    /**
     * 任务优先级 根据任务重要性
     **/
    public static final int PRIORITY_LOW = 1;
    public static final int PRIORITY_NORMAL = 5;
    public static final int PRIORITY_HIGH = 9;
    public static final int PRIORITY_HIGHEST = 99;

    // 调度优先级
    private int priority;

//    /**
//     * 任务类型 按任务执行粒度设定
//     **/
//    //短周期任务 1小时以内
//    public static final int JOB_TYPE_SHORT = 1;
//
//    //中等周期任务 天级任务
//    public static final int JOB_TYPE_MIDDLE = 2;
//
//    //长周期任务 月级别 或者年级别 长期运行任务
//    public static final int JOB_TYPE_LONGEST = 3;
//
//    // 任务类型
//    private int jobType;

    /**
     * 调度权重，根据时间，优先级，延迟调度时间度量
     */
    private int weight;

    /**
     * 调度类型
     **/
    //固定周期调度方式 到执行时间点进行申请
    public static final int SCHEDULE_TYPE_NORMAL = 1;

    //弹性时间段调度方式 在指定时间段内才有调度申请机会
    public static final int SCHEDULE_TYPE_FLEX = 2;

    //资源闲时调度方式 检测到资源空闲时 扫描任务进行发起调度申请 资源闲时任务可以认为是实时任务
    public static final int SCHEDULE_TYPE_IDLE = 3;

    //手动调度方式 通过web界面配置进行启停 一般属于一次性采集脚本 暂搁置
    //public static final int SCHEDULE_TYPE_MANUAL = 4;

    //调度类型
    private int scheduleType;

    //调度时间 固定类型 需要设置的时间 crontab 方式  时间  月 年份 不同的调度情况
    private String scheduleCronTime;

    /**
     * 弹性时间调度需要的起始结束时间段 时间段跨度  日 月 年 固定 和弹性时间 使用时间戳确认时间段
     * 弹性调度周期， 每日弹性， 每月弹性， 每年弹性， 固定次数 时间戳方式
     */
    // 在每天的某些时间段进行调度
    public static final int PERIOD_DAILY = 0;

    // 按每月周期调度， 在每个月中 某个时间段进行调度
    public static final int PERIOD_MONTH = 1;

    // 按每年周期调度，在一年中 某个时间段需要被调度
    public static final int PERIOD_YEAR = 2;

    //固定次数 以时间戳方式表示时间段
    public static final int PERIOD_FINAL = 3;

    //弹性调度类型
    private int periodType;

    //任务发起申请时间间隔 在可申请时间段内 周期性发起调度申请，直到批准调度 该值可能为固定值比如: 2秒
    //根据发起申请周期可以计算出能够发起申请的次数
    // private int periodTime;

    //弹性调度时间段定义
    private List<Pair<String, String>> periodTimeList;


    /**
     * 当前任务调度状态
     **/

    // init 初始化状态
    public static final int STATUS_INIT = 0;

    //已准备好 加入待执行队列可运行状态
    public static final int STATUS_READY = 1;

    //正在执行状态
    public static final int STATUS_RUNNING = 2;

    //任务被暂停状态
    public static final int STATUS_PAUSE = 3;

    // 任务已经调度完成状态
    public static final int STATUS_FINISH = 4;

    // 任务被强制中止 任务结束
    public static final int STATUS_STOP = 5;

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


//    /**
//     * 任务并发数目, 控制任务执行窗口
//     */
//    private int threadNum;


    //任务当前执行进度 监控进度
    private String progress;

    // 执行次数， 周期性任务  与 有限次数任务
    // 任务成功与失败的定义， 粒度划分 某一个网页失败  算不算失败

    //任务可运行次数 0为无限制运行
    private int enableRunTimes;

    //任务已经运行次数
    private int curRunTimes;

//    //任务最大可申请次数 0 代表可以无限次申请， 其他代表可以申请的次数
//    private int maxApplyTimes;
//
//    // 任务申请执行次数
//    private int applyTimes;
//
//    // 最大可执行次数 0 代表可以无限次执行
//    private int maxRatifyTimes;
//
//    // 任务批准执行次数
//    private int ratifyTimes;

}
