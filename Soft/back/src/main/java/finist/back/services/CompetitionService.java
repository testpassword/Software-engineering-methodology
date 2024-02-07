package finist.back.services;

import finist.back.exceptions.CompetitionNotFoundException;
import finist.back.exceptions.ScenarioException;
import finist.back.exceptions.UserNotFoundException;
import finist.back.model.User;
import finist.back.model.dto.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface CompetitionService {
    Optional<List<FullCompetitionDTO>> getAllCompetitions();

    Optional<FullCompetitionDTO> addCompetition(NewCompetitionReqDTO newCompetitionReqDTO, UserDetails userDetails) throws UserNotFoundException;

    Optional<FullCompetitionDTO> getCompetition(Long competitionId);

    Optional<FullCompetitionDTO> updateCompetition(Long competitionId, FullCompetitionDTO fullCompetitionDTO);

    List<FullTaskDTO> getTasksByCompetition(Long competitionId) throws CompetitionNotFoundException;

    Optional<FullTaskDTO> addTask(Long competitionId, FullTaskDTO taskDTO) throws UserNotFoundException, CompetitionNotFoundException;

    Optional<List<FullUserDTO>> getMembersDTOByCompetition(Long competitionId) throws CompetitionNotFoundException;

    List<User> getMembersByCompetition(Long competitionId) throws CompetitionNotFoundException;

    Optional<FullTaskDTO> getTaskByCompIdAndTaskId(Long competitionId, Long taskId);

    Optional<FullTaskDTO> updateTask(Long competitionId, Long taskId, FullTaskDTO updatedTask) throws CompetitionNotFoundException, UserNotFoundException;

    Optional<FullBrideVoteDTO> getBrideVoteByCompetition(Long competitionId) throws CompetitionNotFoundException;

    void deleteCompetition(Long competitionId, UserDetails userDetails) throws ScenarioException, UserNotFoundException;
}
