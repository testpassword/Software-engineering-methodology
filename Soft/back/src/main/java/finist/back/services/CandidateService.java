package finist.back.services;

import finist.back.model.BrideVote;
import finist.back.model.User;

public interface CandidateService {
    void addNewCandidate(BrideVote brideVote, User user);

}
