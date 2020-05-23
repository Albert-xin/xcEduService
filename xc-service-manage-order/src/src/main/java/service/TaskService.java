package service;

import org.testng.annotations.Optional;

import java.awt.print.Pageable;
import java.util.Date;
import java.util.List;

/**
 * @author Albert
 * @date 2020/5/2 - 11:30 下午
 */

public class TaskService {
    public static void main(String[] args) {
    }

    @Autowired
    XcTaskRepository xcTaskRepository;

    RabbitTemplate rabbitTemplate;

    XcTaskHisRepositiry xcTaskHisRepositiry;

    //查询前n条任务
    public List<XcTask> findXcTaskList(Date updateTime,int size) {

        //设置分页参数
        Pageable pageable = new PageRequest(0,size);

        //查询前n条任务
        Page<XcTask> all = xcTaskRepository.findByupdateTimeBefore(pageable,updateTime);
        List<XcTask> list = all.getContent();

        return list;
    }
    //发送消息
    public void publish(XcTask xcTask,String ex,String routingKey) {
        Optional<XcTask> optional = xcTaskRepository.findById(xcTask.getId());

            //更新任务时间
            XcTask one = optional.get();
            one.setUpdateTime(new Date());
            xcTaskRepository.save(one);

            //获取任务

            //完成任务
            @Transaction    //事务控制
            public void finishTask(String taskId) { //传ID
                xcTashRepository.findById(taskId);
                if (optionalXcTask.isPresent) {

                    XcTask xcTask = optionalXcTask.get();

                    XcTaskHis xcTaskHis = new XcTashHis();
                    BeanUtils,copyProperties(xcTask,xcTaskHis);
                    xcTaskHisRepositiry.save(xcTaskHis);
                    xcTaskRepository.delete(xcTask);

                }
            }
        }

    }
}