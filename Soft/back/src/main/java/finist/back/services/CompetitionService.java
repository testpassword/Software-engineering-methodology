package finist.back.services;

import finist.back.exceptions.*;
import finist.back.model.Competition;
import finist.back.model.LoveRequest;
import finist.back.model.User;
import finist.back.model.dto.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface CompetitionService {

    Optional<List<FullCompetitionDTO>> getAllCompetitions();

    Optional<FullCompetitionDTO> addCompetition(NewCompetitionReqDTO newCompetitionReqDTO, UserDetails userDetails) throws UserNotFoundException, InvalidRequestParamsException, LoveRequestNotFoundException;

    Competition getCompetition(Long competitionId) throws CompetitionNotFoundException;

    Optional<FullCompetitionDTO> getCompetitionAsDTO(Long competitionId);

    Optional<FullCompetitionDTO> updateCompetition(Long competitionId, FullCompetitionDTO fullCompetitionDTO);

    List<FullTaskDTO> getTasksByCompetition(Long competitionId) throws CompetitionNotFoundException;

    Optional<FullTaskDTO> addTask(Long competitionId, FullTaskDTO taskDTO) throws UserNotFoundException, CompetitionNotFoundException;

    Optional<List<FullUserDTO>> getMembersDTOByCompetition(Long competitionId) throws CompetitionNotFoundException;

    List<User> getMembersByCompetition(Long competitionId) throws CompetitionNotFoundException;

    Optional<FullTaskDTO> getTaskByCompIdAndTaskId(Long competitionId, Long taskId);

    Optional<FullTaskDTO> updateTask(Long competitionId, Long taskId, FullTaskDTO updatedTask) throws CompetitionNotFoundException, UserNotFoundException;

    Optional<FullBrideVoteDTO> getBrideVoteByCompetition(Long competitionId) throws CompetitionNotFoundException;

    void deleteCompetition(Long competitionId, UserDetails userDetails) throws ScenarioException, UserNotFoundException;

    Optional<FullLoveRequestDTO> registerNewLoveRequest(FullLoveRequestDTO loveRequestDTO, UserDetails userDetails) throws UserNotFoundException, ScenarioException;
    Optional<List<FullLoveRequestDTO>> getAllLoveRequests();
    LoveRequest getLoveRequest(Long requestId) throws LoveRequestNotFoundException;
    Optional<FullLoveRequestDTO> getLoveRequestAsDTO(Long requestId) throws LoveRequestNotFoundException;
    Optional<FullLoveRequestDTO> updateLoveRequest(Long requestId, FullLoveRequestDTO loveRequestDTO, UserDetails userDetails) throws UserNotFoundException, CompetitionNotFoundException, ScenarioException;

    Optional<List<FullLoveRequestDTO>> getAllUnassignedLoveRequests();
}
