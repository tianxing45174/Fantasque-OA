package com.fantasque;

import com.fantasque.auth.ServiceAuthApplication;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author LaFantasque
 * @version 1.0
 */
@SpringBootTest(classes = ServiceAuthApplication.class)
public class activitiTest {

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoryService historyService;

    String assignee = "zhangsan";

    /**
     * 挂起流程
     */
    @Test
    public void suspendProcessInstance() {
        ProcessDefinition qingjia = repositoryService.createProcessDefinitionQuery().processDefinitionKey("qingjia").singleResult();
        // 获取到当前流程定义是否为暂停状态 suspended方法为true是暂停的，suspended方法为false是运行的
        boolean suspended = qingjia.isSuspended();
        if (suspended) {
            // 暂定,那就可以激活
            // 参数1:流程定义的id  参数2:是否激活    参数3:时间点
            repositoryService.activateProcessDefinitionById(qingjia.getId(), true, null);
            System.out.println("流程定义:" + qingjia.getId() + "激活");
        } else {
            repositoryService.suspendProcessDefinitionById(qingjia.getId(), true, null);
            System.out.println("流程定义:" + qingjia.getId() + "挂起");
        }
    }
    /**
     * 挂起单个流程实例
     */
    @Test
    public void SingleSuspendProcessInstance() {
        String processInstanceId = "f12f09f3-7d3e-11ee-8877-00ff0f4de3a2";
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        //获取到当前流程定义是否为暂停状态   suspended方法为true代表为暂停   false就是运行的
        boolean suspended = processInstance.isSuspended();
        if (suspended) {
            runtimeService.activateProcessInstanceById(processInstanceId);
            System.out.println("流程实例:" + processInstanceId + "激活");
        } else {
            runtimeService.suspendProcessInstanceById(processInstanceId);
            System.out.println("流程实例:" + processInstanceId + "挂起");
        }
    }

    /**
     * 部署流程
     */
    @Test
    public void deployProcessTest() {
        // 流程部署
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("process/qingjia.bpmn20.xml")
                .name("请假申请流程")
                .deploy();
        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
        System.out.println(deploy);
    }

    /**
     * 创建流程实例
     */
    @Test
    public void startUpProcess() {
        ProcessInstance qingjia = runtimeService.startProcessInstanceByKey("qingjia");
        //输出实例的相关信息
        System.out.println("流程定义id：" + qingjia.getProcessDefinitionId());
        System.out.println("流程实例id：" + qingjia.getId());
        System.out.println("当前活动Id：" + qingjia.getActivityId());
    }

    /**
     * 查询未处理任务
     */
    @Test
    public void findPendingTaskList() {
        List<Task> list = taskService.createTaskQuery()
                .taskAssignee(assignee)//只查询该任务负责人的任务
                .list();
        for (Task task : list) {
            System.out.println("流程实例id：" + task.getProcessInstanceId());
            System.out.println("任务id：" + task.getId());
            System.out.println("任务负责人：" + task.getAssignee());
            System.out.println("任务名称：" + task.getName());
        }
    }

    /**
     * 处理任务
     */
    @Test
    public void completeTask(){
        Task task = taskService.createTaskQuery()
                .taskAssignee("lisi")  //要查询的负责人
                .singleResult();//返回一条

        //完成任务,参数：任务id
        taskService.complete(task.getId());
        System.out.println("流程实例id：" + task.getProcessInstanceId());
        System.out.println("任务id：" + task.getId());
        System.out.println("任务负责人：" + task.getAssignee());
        System.out.println("任务名称：" + task.getName());
    }

    /**
     * 查询已处理历史任务
     */
    @Test
    public void findCompleteProcessedTaskList() {
        //张三已处理过的历史任务
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().taskAssignee(assignee).finished().list();
        for (HistoricTaskInstance historicTaskInstance : list) {
            System.out.println("流程实例id：" + historicTaskInstance.getProcessInstanceId());
            System.out.println("任务id：" + historicTaskInstance.getId());
            System.out.println("任务负责人：" + historicTaskInstance.getAssignee());
            System.out.println("任务名称：" + historicTaskInstance.getName());
        }
    }

    @Test
    public void startUpProcessAddBusinessKey(){
        String businessKey = "11451";
        // 启动流程实例，指定业务标识businessKey，也就是请假申请单id
        ProcessInstance processInstance = runtimeService.
                startProcessInstanceByKey("qingjia",businessKey);
        // 输出
        System.out.println("业务id:"+processInstance.getBusinessKey());
    }
}
