package finist.back.services.impl;

import finist.back.exceptions.AlreadyMadeVoteException;
import finist.back.exceptions.BrideVoteNotFoundException;
import finist.back.exceptions.CandidateNotFoundException;
import finist.back.exceptions.CompetitionNotFoundException;
import finist.back.model.*;
import finist.back.model.dto.FullUserDTO;
import finist.back.model.enums.UserRole;
import finist.back.repositories.*;
import finist.back.services.BrideVoteService;
import finist.back.services.CandidateService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BrideVoteServiceImpl implements BrideVoteService {

    final BrideVoteRepository brideVoteRepository;
    final CompetitionRepository competitionRepository;
    final UserRepository userRepository;
    final CandidateRepository candidateRepository;
    final UserVoteRepository userVoteRepository;
    final CandidateService candidateService;

    public BrideVoteServiceImpl(BrideVoteRepository brideVoteRepository, CandidateService candidateService, CompetitionRepository competitionRepository, UserRepository userRepository, CandidateRepository candidateRepository, UserVoteRepository userVoteRepository) {
        this.brideVoteRepository = brideVoteRepository;
        this.candidateService = candidateService;
        this.competitionRepository = competitionRepository;
        this.userRepository = userRepository;
        this.candidateRepository = candidateRepository;
        this.userVoteRepository = userVoteRepository;
    }

    @Override
    public void initBrideVoteForCompetition(Competition competition, List<User> competitionMembers) throws CompetitionNotFoundException {
        BrideVote brideVote = new BrideVote();

        List<User> candidates = competitionMembers.stream().filter(member -> member.getRole().equals(UserRole.BRIDE)).collect(Collectors.toList());
        brideVote.setCompetition(competition);
        brideVote.setEndTime(LocalDate.now().plusDays(3));
        BrideVote savedBrideVote = brideVoteRepository.save(brideVote);

        candidates.forEach(member -> candidateService.addNewCandidate(savedBrideVote, member));

    }


    @Override
    public Optional<List<FullUserDTO>> getCandidatesFromBrideVote(Long competitionId, Long brideVoteId) throws CompetitionNotFoundException, BrideVoteNotFoundException {
        Optional<Competition> wrappedCompetition = competitionRepository.findById(competitionId);
        if (wrappedCompetition.isEmpty()) throw new CompetitionNotFoundException(competitionId);
        else if (wrappedCompetition.get().getBrideVote() == null) throw new BrideVoteNotFoundException(brideVoteId);

        return Optional.ofNullable(competitionRepository.findById(competitionId)
                .map(competition -> competition.getBrideVote().getCandidates().stream()
                        .map(candidate -> convertUserToFullUserDTO(candidate.getBride()))
                        .collect(Collectors.toList())
                )
                .orElseThrow(() -> new CompetitionNotFoundException(competitionId)));

    }

    @Override
    public void makeVote(Long competitionId, Long candidateId) throws CompetitionNotFoundException, BrideVoteNotFoundException, CandidateNotFoundException, AlreadyMadeVoteException {
        Optional<Competition> wrappedCompetition = competitionRepository.findById(competitionId);
        if (wrappedCompetition.isEmpty()) throw new CompetitionNotFoundException(competitionId);
        else if (wrappedCompetition.get().getBrideVote() == null)
            throw new BrideVoteNotFoundException(0L);
        else if (wrappedCompetition.get().getBrideVote().getCandidates().stream().noneMatch(candidate -> candidate.getBride().getId().equals(candidateId)))
            throw new CandidateNotFoundException(candidateId);

        Long userId = 1L; // пока нет авторизации заменяет голосующего пользователя
        User voter = userRepository.findById(userId).get();

        if (!isAlreadyVote(userId, wrappedCompetition.get().getBrideVote().getId())) { //если еще не голосовал в этом соревновании
            Candidate candidate = wrappedCompetition.get().getBrideVote().getCandidates().stream()
                    .filter(cndt -> cndt.getBride().getId().equals(candidateId))
                    .findAny().get();


            candidate.addPoint(1);
            Candidate updatedCandidate = candidateRepository.save(candidate); // обновили кол-во очков в бд

            UserVote userVote = new UserVote();
            userVote.setBrideVote(wrappedCompetition.get().getBrideVote());
            userVote.setVoter(voter);
            userVote.setCandidate(updatedCandidate);

            userVoteRepository.save(userVote);

        }
        else throw new AlreadyMadeVoteException(userId, competitionId);
    }

    private boolean isAlreadyVote(Long voterId, Long brideVoteId) {
        return userVoteRepository.existsByVoterIdAndBrideVoteId(voterId,brideVoteId);
    }

    private FullUserDTO convertUserToFullUserDTO(User user) {
        return new FullUserDTO(user);
    }
}
