package finist.back.services.impl;

import finist.back.exceptions.CompetitionNotFoundException;
import finist.back.exceptions.UserNotFoundException;
import finist.back.model.BrideVote;
import finist.back.model.Competition;
import finist.back.model.Task;
import finist.back.model.User;
import finist.back.model.dto.*;
import finist.back.model.enums.CompetitionStatus;
import finist.back.repositories.CompetitionRepository;
import finist.back.repositories.TaskRepository;
import finist.back.repositories.UserRepository;
import finist.back.services.BrideVoteService;
import finist.back.services.CompetitionService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompetitionServiceImpl implements CompetitionService {


    final TaskRepository taskRepository;
    final CompetitionRepository competitionRepository;
    final UserRepository userRepository;

    final BrideVoteService brideVoteService;


    public CompetitionServiceImpl(CompetitionRepository competitionRepository, UserRepository userRepository, TaskRepository taskRepository, BrideVoteService brideVoteService) {
        this.competitionRepository = competitionRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.brideVoteService = brideVoteService;
    }

    @Override
    public Optional<List<FullCompetitionDTO>> getAllCompetitions() {
        List<Competition> competitions = competitionRepository.findAll();
        return Optional.of(competitions.stream()
                .map(this::convertCompetitionToFullCompetitionDTO)
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<FullCompetitionDTO> addCompetition(NewCompetitionReqDTO newCompetitionReqDTO, UserDetails userDetails) throws UserNotFoundException { // не записывается creatorId, доделать при добавлении авторизации
        return Optional.ofNullable(userRepository.findByEmail(userDetails.getUsername()).map(user ->
                new FullCompetitionDTO(competitionRepository.save(new Competition(user, newCompetitionReqDTO.getName(), newCompetitionReqDTO.getCity())))).orElseThrow(() -> new UserNotFoundException(userDetails.getUsername())));
    }

    @Override
    public Optional<FullCompetitionDTO> getCompetition(Long competitionId) {
        return competitionRepository.findById(competitionId)
                .map(this::convertCompetitionToFullCompetitionDTO);
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
