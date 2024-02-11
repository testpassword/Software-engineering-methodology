package finist.back.controllers;

import finist.back.exceptions.CompetitionNotFoundException;
import finist.back.exceptions.LoveRequestNotFoundException;
import finist.back.exceptions.ScenarioException;
import finist.back.exceptions.UserNotFoundException;
import finist.back.model.dto.FullLoveRequestDTO;
import finist.back.model.dto.FullUserDTO;
import finist.back.services.CompetitionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LoveRequestController {
//    final LoveRequestService loveRequestService;

    final CompetitionService competitionService;

    public LoveRequestController(CompetitionService competitionService) {
        this.competitionService = competitionService;
//        this.loveRequestService = loveRequestService;
    }

    @PostMapping("/arrows/launch/")
    public ResponseEntity<FullLoveRequestDTO> launchArrow(@RequestBody FullLoveRequestDTO loveRequestDTO,
                                                          @AuthenticationPrincipal UserDetails userDetails) throws UserNotFoundException, CompetitionNotFoundException, ScenarioException {
        return competitionService.registerNewLoveRequest(loveRequestDTO, userDetails).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/lrequests/")
    ResponseEntity<List<FullLoveRequestDTO>> getAllLoveRequests(){
        return competitionService.getAllLoveRequests()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/lrequests/{requestId}")
    ResponseEntity<FullLoveRequestDTO> getAllLoveRequests(@PathVariable(name = "requestId") Long requestId) throws LoveRequestNotFoundException {
        return competitionService.getLoveRequestAsDTO(requestId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
    @GetMapping("/lrequests/unassigned")
    ResponseEntity<List<FullLoveRequestDTO>> getAllUnassignedLoveRequests(){
        return competitionService.getAllUnassignedLoveRequests()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PatchMapping("/lrequests/{requestId}/")
    public ResponseEntity<FullLoveRequestDTO> updateLoveRequest(@PathVariable(name = "requestId") Long requestId,
                                                  @RequestBody FullLoveRequestDTO loveRequestDTO,
                                                  @AuthenticationPrincipal UserDetails userDetails) throws UserNotFoundException, ScenarioException, CompetitionNotFoundException {
        return competitionService.updateLoveRequest(requestId, loveRequestDTO, userDetails).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


}
