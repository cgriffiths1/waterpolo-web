package com.example.waterpoloweb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Controller
public class HTMLController {
    private RestTemplate restTemplate;
    public static final String TEAMNAME_ATTRIBUTE = "teamName";
    public static final String NEWTEAM_ATTRIBUTE = "newTeam";
    public static final String TEAM_ATTRIBUTE = "team";

    @Autowired
    public HTMLController(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    @GetMapping("/landingPage")
    public String landingPage(){
        return "landingPage";
    }

    @GetMapping("/teams")
    public String allTeams(Model model){
        String teamURL = "http://localhost:8080/water-polo-api/teams";

        ResponseEntity<TeamModel[]> response =
                restTemplate.getForEntity(
                        teamURL,
                        TeamModel[].class);
        TeamModel[] teams = Objects.requireNonNull(response.getBody());
        model.addAttribute("teams", teams);
        return "teams";
    }

    @GetMapping("/selectTeam")
    public String selectTeam(){
        return "selectTeam";
    }

    @PostMapping("/team")
    public String sendTeam(@ModelAttribute(TEAMNAME_ATTRIBUTE) String nameAttribute){

        return "redirect:teams/" + nameAttribute;
    }

    @GetMapping("/teams/{name}")
    public String team(@PathVariable String name, Model model){

        String teamURL = "http://localhost:8080/water-polo-api/teams/" + name;

        ResponseEntity<TeamModel> response =
                restTemplate.getForEntity(
                        teamURL,
                        TeamModel.class);
        TeamModel team = Objects.requireNonNull(response.getBody());
        model.addAttribute("team", team);
        return "team";
    }

    @GetMapping("/newTeam")
    public String newTeam(){
        return "newTeam";
    }

    @PostMapping("/add")
    public String addTeam(@ModelAttribute(NEWTEAM_ATTRIBUTE) TeamModel newTeamAttribute){
        String teamURL = "http://localhost:8080/water-polo-api/teams";
        HttpEntity<TeamModel> request = new HttpEntity<>(newTeamAttribute);
        ResponseEntity<TeamModel> response = restTemplate.postForEntity(teamURL, request, TeamModel.class);
        return "redirect:/teams";
    }

    @PostMapping(value="/teams/modify", params="delete")
    public String deleteTeam(@ModelAttribute(TEAM_ATTRIBUTE) TeamModel modifyTeamAttribute){
        String teamName = modifyTeamAttribute.getTeamName();
        String teamURL = "http://localhost:8080/water-polo-api/teams/delete";
        HttpEntity<TeamModel> request = new HttpEntity<>(modifyTeamAttribute);
        ResponseEntity<TeamModel> response = restTemplate.postForEntity(teamURL, request, TeamModel.class);

        return "redirect:/teams";
    }

    @PostMapping(value="/teams/modify", params="update")
    public String updateTeam(@ModelAttribute(TEAM_ATTRIBUTE) TeamModel modifyTeamAttribute){
        String teamName = modifyTeamAttribute.getTeamName();
        String teamURL = "http://localhost:8080/water-polo-api/teams/update";
        HttpEntity<TeamModel> request = new HttpEntity<>(modifyTeamAttribute);
        ResponseEntity<TeamModel> response = restTemplate.postForEntity(teamURL, request, TeamModel.class);

        return "redirect:/teams";
    }

}
