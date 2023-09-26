package finist.back.services;

import finist.back.exceptions.AlreadyMadeVoteException;
import finist.back.exceptions.BrideVoteNotFoundException;
import finist.back.exceptions.CandidateNotFoundException;
import finist.back.exceptions.CompetitionNotFoundException;
import finist.back.model.Competition;
import finist.back.model.User;
import finist.back.model.dto.FullUserDTO;

import java.util.List;
import java.util.Optional;

public interface BrideVoteService {
    void initBrideVoteForCompetition(Competition competition, List<User> competitionMembers) throws CompetitionNotFoundException;

    Optional<List<FullUserDTO>> getCandidatesFromBrideVote(Long competitionId, Long brideVoteId) throws CompetitionNotFoundException, BrideVoteNotFoundException;

    void makeVote(Long competitionId, Long candidateId) throws CompetitionNotFoundException, BrideVoteNotFoundException, CandidateNotFoundException, AlreadyMadeVoteException;
}
