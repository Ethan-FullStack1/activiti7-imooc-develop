package com.activiti7.activiti7imoocdevelop.listener;

import com.activiti7.activiti7imoocdevelop.mapper.ActivitiMapper;
import com.google.common.collect.Maps;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 多实例任务监听
 *
 * @author debao.yang
 * @since 2024/6/13 15:22
 */
@Component
public class MultiInstancesStartListener implements ExecutionListener {

    @Autowired
    private ActivitiMapper activitiMapper;

    @Override
    public void notify(DelegateExecution execution) {
        // 这里查询数据库里面所有的人
        List<HashMap<String, Object>> user = activitiMapper.selectUser();
        Map<String, Object> map = Maps.newHashMap();
        map.put("inscount", user.size());
        map.put("assigneeList", user.stream().flatMap(maps -> map.values()
                .stream()).collect(Collectors.toList()));
        execution.setVariables(map);
    }
}
