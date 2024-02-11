package finist.back.controllers;

import finist.back.exceptions.*;
import finist.back.model.dto.*;
import finist.back.services.BrideVoteService;
import finist.back.services.CompetitionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/competitions/")
public class CompetitionController {
    final CompetitionService competitionService;
    final BrideVoteService brideVoteService;

    public CompetitionController(CompetitionService competitionService, BrideVoteService brideVoteService) {
        this.competitionService = competitionService;
        this.brideVoteService = brideVoteService;
    }

    @GetMapping()
    public ResponseEntity<List<FullCompetitionDTO>> getAllCompetitions(){
        return competitionService.getAllCompetitions().map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping()
    public ResponseEntity<FullCompetitionDTO> addNewCompetition(@RequestBody NewCompetitionReqDTO newCompetitionReqDTO,
                                                                @AuthenticationPrincipal UserDetails userDetails) throws UserNotFoundException, LoveRequestNotFoundException, InvalidRequestParamsException {
        return competitionService.addCompetition(newCompetitionReqDTO, userDetails).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/{competitionId}/")
    public ResponseEntity<FullCompetitionDTO> getAllCompetitions(@PathVariable(name = "competitionId") Long competitionId){
        return competitionService.getCompetitionAsDTO(competitionId).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PatchMapping("/{competitionId}/")
    public ResponseEntity<FullCompetitionDTO> updateUser(@PathVariable(name = "competitionId") Long competitionId, @RequestBody FullCompetitionDTO fullCompetitionDTO){
        return competitionService.updateCompetition(competitionId,fullCompetitionDTO).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/{competitionId}/tasks/")
    public ResponseEntity<List<FullTaskDTO>> getAllTasksInCompetition(@PathVariable(name = "competitionId") Long competitionId) throws CompetitionNotFoundException {
        return new ResponseEntity<>(competitionService.getTasksByCompetition(competitionId), HttpStatus.OK);
    }

    @PostMapping("/{competitionId}/tasks/")
    public ResponseEntity<FullTaskDTO> addNewTask(@PathVariable(name = "competitionId") Long competitionId,
                                                  @RequestBody FullTaskDTO taskDTO) throws UserNotFoundException, CompetitionNotFoundException {
        return competitionService.addTask(competitionId, taskDTO).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/{competitionId}/members/")
    public ResponseEntity<List<FullUserDTO>> getCompetitionMembers(@PathVariable(name = "competitionId") Long competitionId) throws CompetitionNotFoundException {
        return competitionService.getMembersDTOByCompetition(competitionId).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/{competitionId}/tasks/{taskId}/")
    public ResponseEntity<FullTaskDTO> getCompetitionMembers(@PathVariable(name = "competitionId") Long competitionId,
                                                                   @PathVariable(name = "taskId") Long taskId) throws CompetitionNotFoundException {
        return competitionService.getTaskByCompIdAndTaskId(competitionId, taskId).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    @DeleteMapping("/{competitionId}/")
    public ResponseEntity<Void> deleteCompetition(@PathVariable(name = "competitionId") Long competitionId,
                                           @AuthenticationPrincipal UserDetails userDetails) throws ScenarioException {
        try {
            competitionService.deleteCompetition(competitionId, userDetails);
            return ResponseEntity.ok().build();
        } catch (UserNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{competitionId}/tasks/{taskId}/")
    public ResponseEntity<FullTaskDTO> updateTask(@PathVariable(name = "competitionId") Long competitionId,
                                                  @PathVariable(name = "taskId") Long taskId,
                                                  @RequestBody FullTaskDTO updatedTask) throws CompetitionNotFoundException, UserNotFoundException {
        return competitionService.updateTask(competitionId, taskId, updatedTask).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/{competitionId}/bride_vote/")
    public ResponseEntity<FullBrideVoteDTO> getBrideVoteByCompetition(@PathVariable(name = "competitionId") Long competitionId) throws CompetitionNotFoundException {
        return competitionService.getBrideVoteByCompetition(competitionId).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/{competitionId}/bride_vote/candidates/")
    public ResponseEntity<List<FullUserDTO>> getCandidatesFromBrideVote(@PathVariable(name = "competitionId") Long competitionId
                                                                  ) throws CompetitionNotFoundException, BrideVoteNotFoundException {
        return brideVoteService.getCandidatesFromBrideVote(competitionId).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    //todo переделать на PUT
    @PatchMapping("/{competitionId}/bride_vote/{candidateId}/vote/")
    public ResponseEntity<Void> makeVote(@PathVariable(name = "competitionId") Long competitionId,
                                         @PathVariable(name = "candidateId") Long candidateId,
                                         @AuthenticationPrincipal UserDetails userDetails) throws BrideVoteNotFoundException, CandidateNotFoundException, AlreadyMadeVoteException, CompetitionNotFoundException, UserNotFoundException {
        brideVoteService.makeVote(competitionId, candidateId, userDetails);
        return ResponseEntity.ok().build();
    }
}