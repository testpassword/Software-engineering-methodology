//package finist.back.services.impl;
//
//import finist.back.exceptions.CompetitionNotFoundException;
//import finist.back.exceptions.LoveRequestNotFoundException;
//import finist.back.exceptions.ScenarioException;
//import finist.back.exceptions.UserNotFoundException;
//import finist.back.model.Competition;
//import finist.back.model.LoveRequest;
//import finist.back.model.User;
//import finist.back.model.dto.FullLoveRequestDTO;
//import finist.back.model.enums.CompetitionStatus;
//import finist.back.model.enums.UserRole;
//import finist.back.repositories.LoveRequestRepository;
//import finist.back.repositories.RoleRepository;
//import finist.back.repositories.UserRepository;
//import finist.back.services.CompetitionService;
//import finist.back.services.LoveRequestService;
//import finist.back.services.UserService;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.NoSuchElementException;
//import java.util.Objects;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//public class LoveRequestServiceImpl implements LoveRequestService {
//
//
//    final UserRepository userRepository;
//    final RoleRepository roleRepository;
//
//    final LoveRequestRepository loveRequestRepository;
//
//    final CompetitionService competitionService;
//
//    final UserService userService;
//
//
//    public LoveRequestServiceImpl(UserRepository userRepository, RoleRepository roleRepository, LoveRequestRepository loveRequestRepository, CompetitionService competitionService, UserService userService) {
//        this.userRepository = userRepository;
//        this.roleRepository = roleRepository;
//        this.loveRequestRepository = loveRequestRepository;
//        this.competitionService = competitionService;
//        this.userService = userService;
//    }
//
//
//    public Optional<FullLoveRequestDTO> registerNewLoveRequest(FullLoveRequestDTO loveRequestDTO, UserDetails userDetails) throws UserNotFoundException, ScenarioException {
//        User user = userRepository.findByEmail(userDetails.getUsername())
//                .orElseThrow(() -> new UserNotFoundException(userDetails.getUsername()));
//        if (!isAllCompCompleted(user)) {
//            throw new ScenarioException("У пользователя есть незавершенные состязания");
//        }
//        LoveRequest loveRequest = new LoveRequest().withGroom(user);
//        LoveRequest savedLoveRequest = loveRequestRepository.save(loveRequest);
//        return Optional.of(new FullLoveRequestDTO(savedLoveRequest));
//    }
//
//    private boolean isAllCompCompleted(User user) {
//        return user.getMyLoveRequests() == null || user.getMyLoveRequests().isEmpty() ||
//                user.getMyLoveRequests().stream()
//                        .map(LoveRequest::getCompetition)
//                        .filter(Objects::nonNull)
//                        .allMatch(competition -> competition.getStatus() == CompetitionStatus.COMPLETED);
//    }
//
//    @Override
//    public Optional<List<FullLoveRequestDTO>> getAllLoveRequests() {
//        List<LoveRequest> loveRequests = loveRequestRepository.findAll();
//        return Optional.of(loveRequests.stream()
//                .map(this::convertToFullLoveRequestDTO)
//                .collect(Collectors.toList()));
//    }
//
//    @Override
//    public Optional<List<FullLoveRequestDTO>> getAllUnassignedLoveRequests() {
//        List<LoveRequest> loveRequests = loveRequestRepository.findAllByIsAssignedFalse();
//        return Optional.of(loveRequests.stream()
//                .map(this::convertToFullLoveRequestDTO)
//                .collect(Collectors.toList()));
//    }
//
//
//    @Override
//    public Optional<FullLoveRequestDTO> getLoveRequestAsDTO(Long requestId) throws LoveRequestNotFoundException {
//        return Optional.ofNullable(loveRequestRepository.findById(requestId)
//                .map(this::convertToFullLoveRequestDTO)
//                .orElseThrow(() ->
//                        new LoveRequestNotFoundException(requestId)));
//    }
//
//    @Override
//    public LoveRequest getLoveRequest(Long requestId) throws LoveRequestNotFoundException {
//        return loveRequestRepository.findById(requestId)
//                .orElseThrow(() ->
//                        new LoveRequestNotFoundException(requestId));
//    }
//
//    @Override
//    public Optional<FullLoveRequestDTO> updateLoveRequest(Long requestId, FullLoveRequestDTO loveRequestDTO, UserDetails userDetails) throws UserNotFoundException, CompetitionNotFoundException, ScenarioException {
//        Optional<LoveRequest> wrappedRequest = loveRequestRepository.findById(requestId);
//        if (wrappedRequest.isEmpty()) throw new NoSuchElementException();
//        LoveRequest loveRequest = wrappedRequest.get();
//        updateLoveRequestAttributes(loveRequest, loveRequestDTO);
//        return Optional.of(convertToFullLoveRequestDTO(loveRequestRepository.save(loveRequest)));
//    }
//
//
//    private void updateLoveRequestAttributes(LoveRequest loveRequest, FullLoveRequestDTO loveRequestDTO) throws CompetitionNotFoundException, UserNotFoundException, ScenarioException {
//        if (loveRequestDTO.getCompetition_id() != null) {
//            Competition competition = competitionService.getCompetition(loveRequestDTO.getCompetition_id());
//            loveRequest.setCompetition(competition);
//        }
//        if (loveRequestDTO.getMatchmaker_id() != null){
//            User matchmaker = userService.getUser(loveRequestDTO.getMatchmaker_id());
//            if (!matchmaker.getRole().equals(UserRole.MATCHMAKER))
//                throw new ScenarioException("пользователь не является свахой");
//            loveRequest.setAssigned(true);
//            loveRequest.setMatchmaker(matchmaker);
//        }
//    }
//
//    private FullLoveRequestDTO convertToFullLoveRequestDTO(LoveRequest loveRequest) {
//        return new FullLoveRequestDTO(loveRequest);
//    }
//
//}
