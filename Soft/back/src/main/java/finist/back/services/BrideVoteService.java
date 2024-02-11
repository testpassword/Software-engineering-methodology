package finist.back.services;

import finist.back.exceptions.*;
import finist.back.model.Competition;
import finist.back.model.User;
import finist.back.model.dto.FullUserDTO;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface BrideVoteService {
    void initBrideVoteForCompetition(Competition competition, List<User> competitionMembers) throws CompetitionNotFoundException;

    Optional<List<FullUserDTO>> getCandidatesFromBrideVote(Long competitionId) throws CompetitionNotFoundException, BrideVoteNotFoundException;

    void makeVote(Long competitionId, Long candidateId, UserDetails userDetails) throws CompetitionNotFoundException, BrideVoteNotFoundException, CandidateNotFoundException, AlreadyMadeVoteException, UserNotFoundException;
}
