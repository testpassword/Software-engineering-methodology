//package finist.back.services;
//
//import finist.back.exceptions.CompetitionNotFoundException;
//import finist.back.exceptions.LoveRequestNotFoundException;
//import finist.back.exceptions.ScenarioException;
//import finist.back.exceptions.UserNotFoundException;
//import finist.back.model.LoveRequest;
//import finist.back.model.dto.FullLoveRequestDTO;
//import finist.back.model.dto.FullUserDTO;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.List;
//import java.util.Optional;
//
//public interface LoveRequestService {
//    Optional<FullLoveRequestDTO> registerNewLoveRequest(FullLoveRequestDTO loveRequestDTO, UserDetails userDetails) throws UserNotFoundException, ScenarioException;
//    Optional<List<FullLoveRequestDTO>> getAllLoveRequests();
//    LoveRequest getLoveRequest(Long requestId) throws LoveRequestNotFoundException;
//    Optional<FullLoveRequestDTO> getLoveRequestAsDTO(Long requestId) throws LoveRequestNotFoundException;
//    Optional<FullLoveRequestDTO> updateLoveRequest(Long requestId, FullLoveRequestDTO loveRequestDTO, UserDetails userDetails) throws UserNotFoundException, CompetitionNotFoundException, ScenarioException;
//
//    Optional<List<FullLoveRequestDTO>> getAllUnassignedLoveRequests();
//}
