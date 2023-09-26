package finist.back.services.impl;

import finist.back.model.BrideVote;
import finist.back.model.Candidate;
import finist.back.model.User;
import finist.back.repositories.CandidateRepository;
import finist.back.services.CandidateService;
import org.springframework.stereotype.Service;

@Service
public class CandidateServiceImpl implements CandidateService {

    final CandidateRepository candidateRepository;

    public CandidateServiceImpl(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    @Override
    public void addNewCandidate(BrideVote brideVote, User user) {
        Candidate newCandidate = new Candidate();
        newCandidate.setBrideVote(brideVote);
        newCandidate.setBride(user);

        candidateRepository.save(newCandidate);
    }
}
