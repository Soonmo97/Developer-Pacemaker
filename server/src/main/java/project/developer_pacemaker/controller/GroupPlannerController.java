package project.developer_pacemaker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.developer_pacemaker.service.GroupPlannerService;

@RestController
@RequestMapping("/api/group-planner")
public class GroupPlannerController {

    @Autowired
    GroupPlannerService groupPlannerService;

//    @PostMapping()
//    public ResponseEntity<?> getGroupPlanner(@AuthenticationPrincipal String uSeq){
//
//    }


}
