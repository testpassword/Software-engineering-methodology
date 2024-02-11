package finist.back.services.impl;

import finist.back.exceptions.*;
import finist.back.model.*;
import finist.back.model.dto.*;
import finist.back.model.enums.CompetitionStatus;
import finist.back.model.enums.UserRole;
import finist.back.repositories.*;
import finist.back.services.BrideVoteService;
import finist.back.services.CompetitionService;
import finist.back.services.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CompetitionServiceImpl implements CompetitionService {


    final TaskRepository taskRepository;
    final CompetitionRepository competitionRepository;
    final UserRepository userRepository;

    final BrideVoteService brideVoteService;

    final RoleRepository roleRepository;

    final LoveRequestRepository loveRequestRepository;
    final UserService userService;



    public CompetitionServiceImpl(CompetitionRepository competitionRepository, UserRepository userRepository, TaskRepository taskRepository, BrideVoteService brideVoteService, RoleRepository roleRepository, LoveRequestRepository loveRequestRepository, UserService userService) {
        this.competitionRepository = competitionRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.brideVoteService = brideVoteService;
        this.roleRepository = roleRepository;
        this.loveRequestRepository = loveRequestRepository;
        this.userService = userService;
    }

    @Override
    public Optional<List<FullCompetitionDTO>> getAllCompetitions() {
        List<Competition> competitions = competitionRepository.findAll();
        return Optional.of(competitions.stream()
                .map(this::convertCompetitionToFullCompetitionDTO)
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<FullCompetitionDTO> addCompetition(NewCompetitionReqDTO newCompetitionReqDTO, UserDetails userDetails) throws UserNotFoundException, InvalidRequestParamsException, LoveRequestNotFoundException { // не записывается creatorId, доделать при добавлении авторизации
        Long loveRequestId = newCompetitionReqDTO.getLoveRequestId();
        if (loveRequestId == null)
            throw new InvalidRequestParamsException("не указана заявка на поиск пары");
        User user = userService.getUserByEmail(userDetails.getUsername());
        LoveRequest loveRequest = getLoveRequest(loveRequestId);
        return Optional.of(new Competition(user, newCompetitionReqDTO.getName(), newCompetitionReqDTO.getCity(), loveRequest))
                .map(competition -> {
                 loveRequest.setCompetition(competition);
                 return competition;
                })
                .map(competitionRepository::save)
                .map(FullCompetitionDTO::new);
    }

    @Override
    public Optional<FullCompetitionDTO> getCompetitionAsDTO(Long competitionId) {
        return competitionRepository.findById(competitionId)
                .map(this::convertCompetitionToFullCompetitionDTO);
    }

    @Override
    public Competition getCompetition(Long competitionId) throws CompetitionNotFoundException {
        return competitionRepository.findById(competitionId)
                .orElseThrow(() ->
                        new CompetitionNotFoundException(competitionId));
    }

    @Override
    public Optional<FullCompetitionDTO> updateCompetition(Long competitionId, FullCompetitionDTO competitionDTO) {
        Optional<Competition> wrappedComp = competitionRepository.findById(competitionId);
        return wrappedComp.map(comp -> {
            Optional.ofNullable(competitionDTO.getName()).ifPresent(comp::setName);
            Optional.ofNullable(competitionDTO.getCity()).ifPresent(comp::setCity);
            Optional.ofNullable(competitionDTO.getStatus()).ifPresent(newStatus -> {
                comp.setStatus(CompetitionStatus.getByCode(newStatus));
                if (CompetitionStatus.getByCode(newStatus) == CompetitionStatus.VOTING){
                    try {
                        brideVoteService.initBrideVoteForCompetition(comp, this.getMembersByCompetition(comp.getId()));
                    } catch (CompetitionNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            if (competitionDTO.getTasksIds() != null && competitionDTO.getTasksIds().isEmpty()){
                comp.setTasks(new ArrayList<>());
            }
            if (competitionDTO.getFollowersAmount() != null && competitionDTO.getFollowersAmount() == 0){
                comp.setFollowers(new ArrayList<>());
            }

            return Optional.of(convertCompetitionToFullCompetitionDTO(competitionRepository.save(comp)));
        }).orElse(Optional.empty());
    }

    @Override
    public List<FullTaskDTO> getTasksByCompetition(Long competitionId) throws CompetitionNotFoundException {
        return competitionRepository.findById(competitionId)
                .map(competition -> {
                    List<Task> tasks = (List<Task>) competition.getTasks();
                    if (tasks != null) {
                        return tasks.stream()
                                .map(this::convertTaskToFullTaskDTO)
                                .collect(Collectors.toList());
                    } else {
                        return new ArrayList<FullTaskDTO>(); // Возвращаем пустой список
                    }
                })
                .orElseThrow(() -> new CompetitionNotFoundException(competitionId));
    }

    @Override
    public Optional<FullTaskDTO> addTask(Long competitionId, FullTaskDTO taskDTO) throws UserNotFoundException, CompetitionNotFoundException {
        Optional<User> executorWrapper = userRepository.findById(taskDTO.getExecutorId());
        if (executorWrapper.isEmpty())
            throw new UserNotFoundException(taskDTO.getExecutorId());



       return Optional.of(
               new FullTaskDTO(competitionRepository.findById(competitionId)
                       .map(competition -> taskRepository.save(new Task(taskDTO.getText(), taskDTO.getReport(), competition, executorWrapper.get())))
                       .orElseThrow(() -> new CompetitionNotFoundException(competitionId))));
    }


    public List<User> getMembersByCompetition(Long competitionId) throws CompetitionNotFoundException {
        return competitionRepository.findById(competitionId)
                .map(competition ->
                        competition.getTasks().stream()
                                .map(Task::getExecutor)
                                .distinct()
                                .collect(Collectors.toList())
                ).orElseThrow(() -> new CompetitionNotFoundException(competitionId));

    }

    @Override
    public Optional<List<FullUserDTO>> getMembersDTOByCompetition(Long competitionId) throws CompetitionNotFoundException {
        return Optional.of(this.getMembersByCompetition(competitionId)
                .stream()
                .map(this::convertUserToFullUserDTO)
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<FullTaskDTO> getTaskByCompIdAndTaskId(Long competitionId, Long taskId) {
        return taskRepository.findByParentCompetitionIdAndId(competitionId, taskId)
                .map(this::convertTaskToFullTaskDTO);
    }

    @Override
    public Optional<FullTaskDTO> updateTask(Long competitionId, Long taskId, FullTaskDTO updatedTask) throws CompetitionNotFoundException, UserNotFoundException {
        if(!competitionRepository.existsById(competitionId)) {
            throw new CompetitionNotFoundException(competitionId);
        }
        else if (!userRepository.existsById(updatedTask.getExecutorId())){
            throw new UserNotFoundException(updatedTask.getExecutorId());
        }
        else {
            Optional<Task> wrappedTask = taskRepository.findById(taskId);
            return wrappedTask.map(task -> {
                Optional.ofNullable(updatedTask.getReport()).ifPresent(task::setReport);
                Optional.ofNullable(updatedTask.getCompleted()).ifPresent(task::setCompleted);
                Optional.ofNullable(updatedTask.getText()).ifPresent(task::setText);
                Optional.ofNullable(updatedTask.getExecutorId()).ifPresent(updExecutorId -> {
                  User updExecutor = userRepository.findById(updExecutorId).get();
                  task.setExecutor(updExecutor);
                });

                return Optional.of(convertTaskToFullTaskDTO(taskRepository.save(task)));
            }).orElse(Optional.empty());
        }
    }

    //todo добавить проверку наличия голосования в соревновании
    @Override
    public Optional<FullBrideVoteDTO> getBrideVoteByCompetition(Long competitionId) throws CompetitionNotFoundException {
        return Optional.ofNullable(competitionRepository.findById(competitionId)
                .map(competition -> {
                    BrideVote brideVote = competition.getBrideVote();
                    return new FullBrideVoteDTO(brideVote);
                })
                .orElseThrow(() -> new CompetitionNotFoundException(competitionId)));
    }

    @Override
    public void deleteCompetition(Long competitionId, UserDetails userDetails) throws ScenarioException, UserNotFoundException {
        Optional<Competition> compOptional = competitionRepository.findById(competitionId);
        if (compOptional.isPresent()) {
            Competition competition = compOptional.get();
            competitionRepository.delete(competition);
        } else {
            throw new UserNotFoundException(competitionId);
        }
    }

////////////////////////////////////////////////////////////////

    public Optional<FullLoveRequestDTO> registerNewLoveRequest(FullLoveRequestDTO loveRequestDTO, UserDetails userDetails) throws UserNotFoundException, ScenarioException {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UserNotFoundException(userDetails.getUsername()));
        if (!isAllCompCompleted(user)) {
            throw new ScenarioException("У пользователя есть незавершенные состязания");
        }
        LoveRequest loveRequest = new LoveRequest().withGroom(user);
        user.addLoveRequests(loveRequest);
        LoveRequest savedLoveRequest = loveRequestRepository.save(loveRequest);
        return Optional.of(new FullLoveRequestDTO(savedLoveRequest));
    }

    private boolean isAllCompCompleted(User user) {
        return user.getMyLoveRequests() == null || user.getMyLoveRequests().isEmpty() ||
                user.getMyLoveRequests().stream()
                        .map(LoveRequest::getCompetition)
                        .filter(Objects::nonNull)
                        .allMatch(competition -> competition.getStatus() == CompetitionStatus.COMPLETED);
    }

    @Override
    public Optional<List<FullLoveRequestDTO>> getAllLoveRequests() {
        List<LoveRequest> loveRequests = loveRequestRepository.findAll();
        return Optional.of(loveRequests.stream()
                .map(this::convertToFullLoveRequestDTO)
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<List<FullLoveRequestDTO>> getAllUnassignedLoveRequests() {
        List<LoveRequest> loveRequests = loveRequestRepository.findAllByIsAssignedFalse();
        return Optional.of(loveRequests.stream()
                .map(this::convertToFullLoveRequestDTO)
                .collect(Collectors.toList()));
    }


    @Override
    public Optional<FullLoveRequestDTO> getLoveRequestAsDTO(Long requestId) throws LoveRequestNotFoundException {
        return Optional.ofNullable(loveRequestRepository.findById(requestId)
                .map(this::convertToFullLoveRequestDTO)
                .orElseThrow(() ->
                        new LoveRequestNotFoundException(requestId)));
    }

    @Override
    public LoveRequest getLoveRequest(Long requestId) throws LoveRequestNotFoundException {
        return loveRequestRepository.findById(requestId)
                .orElseThrow(() ->
                        new LoveRequestNotFoundException(requestId));
    }

    @Override
    public Optional<FullLoveRequestDTO> updateLoveRequest(Long requestId, FullLoveRequestDTO loveRequestDTO, UserDetails userDetails) throws UserNotFoundException, CompetitionNotFoundException, ScenarioException {
        Optional<LoveRequest> wrappedRequest = loveRequestRepository.findById(requestId);
        if (wrappedRequest.isEmpty()) throw new NoSuchElementException();
        LoveRequest loveRequest = wrappedRequest.get();
        updateLoveRequestAttributes(loveRequest, loveRequestDTO);
        return Optional.of(convertToFullLoveRequestDTO(loveRequestRepository.save(loveRequest)));
    }


    private void updateLoveRequestAttributes(LoveRequest loveRequest, FullLoveRequestDTO loveRequestDTO) throws CompetitionNotFoundException, UserNotFoundException, ScenarioException {
        if (loveRequestDTO.getCompetition_id() != null) {
            Competition competition = this.getCompetition(loveRequestDTO.getCompetition_id());
            loveRequest.setCompetition(competition);
        }
        if (loveRequestDTO.getMatchmaker_id() != null){
            User matchmaker = userService.getUser(loveRequestDTO.getMatchmaker_id());
            if (!matchmaker.getRole().equals(UserRole.MATCHMAKER))
                throw new ScenarioException("пользователь не является свахой");
            loveRequest.setAssigned(true);
            loveRequest.setMatchmaker(matchmaker);
        }
    }

    private FullLoveRequestDTO convertToFullLoveRequestDTO(LoveRequest loveRequest) {
        return new FullLoveRequestDTO(loveRequest);
    }

    private FullCompetitionDTO convertCompetitionToFullCompetitionDTO(Competition competition) {
        return new FullCompetitionDTO(competition);
    }

    private FullTaskDTO convertTaskToFullTaskDTO(Task task) {
        return new FullTaskDTO(task);
    }

    private FullUserDTO convertUserToFullUserDTO(User user) {
        return new FullUserDTO(user);
    }
}
