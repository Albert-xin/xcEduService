package MQ;

/**
 * @author Albert
 * @date 2020/5/2 - 11:13 下午
 */

import config.RabbitMQConfig;
import service.Autowired;
import service.TaskService;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Logger;

/**
 * Choose Course Task 选择课程的任务
 * @schedule 定时器
 * (cron="0/3********")
 * 一个cron表达式有至少6个（也可能7个）有空格分隔的时间元素；秒 分 时 日 月 星期 年
 */
public class ChooseCourseTask {
    public static void main(String[] args) {
    }
    private static final Logger LOGGER = LoggerFactory.getLogger(ChooseCourseTask.class);

    @Autowired
    TaskService taskService;

    @RabbixListener(queues = RabbitMQConfig.EX_LEARNING_ADDCHOOSECOURSE)
    public void receiveFinishChoosecourseTask(Xctask xctask) {
        if (xctask != null && StringUtils.isNotEmpty(xctask.getId())) {
            taskService.finshTask();
        }
    }

    public <XcTask> void sendChoosecourseTask() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.set(GregorianCalendar.MINUTE,-1);
        Date time = calendar.getTime();
        List<XcTask> xcTaskList = taskService.findXcTaskList(time,100);
        System.out.println(xcTaskList);

        //调用service发布消息，将添加选课的任务发送给MQ
        for (XcTask xcTask:xcTaskList) {
            String ex = xcTask.getMqExchange(); //要发送的交换机
            String routingKey = xcTask.getMqRoutingkey();   //发送消息要带routingkey
            taskService.publish(xcTask,ex,routingKey);
        }
    }
}

