package project.developer_pacemaker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.developer_pacemaker.service.GroupPlannerService;

@RestController
@RequestMapping("/api/group-planner")
public class GroupPlannerController {

    @Autowired
    GroupPlannerService groupPlannerService;


}
