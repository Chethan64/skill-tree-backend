package com.RDS.skilltree.Endorsement;

import com.RDS.skilltree.Exceptions.NoEntityException;
import com.RDS.skilltree.Skill.SkillModel;
import com.RDS.skilltree.Skill.SkillRepository;
import com.RDS.skilltree.User.UserModel;
import com.RDS.skilltree.User.UserRepository;
import com.RDS.skilltree.utils.RQLParser;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EndorsementServiceImpl implements EndorsementService {
    private final EndorsementRepository endorsementRepository;
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;

    @Override
    public EndorsementDTO getEndorsementById(UUID id) throws IllegalStateException {
        Optional<EndorsementModel> endorsementModel = endorsementRepository.findById(id);
        return EndorsementDTO.toDto(endorsementModel
                .orElseThrow(() -> new EntityNotFoundException("No endorsement with the id " + id + " found")));
    }

    @Override
    public Page<EndorsementModel> getEndorsements(String queryString, PageRequest pageRequest) {
        try {
            if (queryString == null || queryString.isEmpty()) {
                return endorsementRepository.findAll(pageRequest);
            } else {
                Map<String, String> queryParams = RQLParser.parseQueryString(queryString);
                String skillId = queryParams.get("skillID");
                String userId = queryParams.get("userID");
                System.out.println(skillId);
                System.out.println(userId);

                // todo validate the UUIds and the keys against a set
                UUID skillIdUUID = null;
                UUID userIdUUID = null;
                if (skillId != null) {
                    try {
                        skillIdUUID = UUID.fromString(skillId);
                    } catch (IllegalArgumentException e) {
                        throw new IllegalArgumentException("Invalid skillID: " + skillId);
                    }
                }
                if (userId != null) {
                    try {
                        userIdUUID = UUID.fromString(userId);
                    } catch (IllegalArgumentException e) {
                        throw new IllegalArgumentException("Invalid userID: " + userId);
                    }
                }

                if (skillIdUUID != null && userIdUUID != null) {
                    return endorsementRepository.findBySkill_IdAndUser_Id(skillIdUUID, userIdUUID, pageRequest);
                } else if (skillIdUUID != null) {
                    return endorsementRepository.findBySkill_Id(skillIdUUID, pageRequest);
                } else if (userIdUUID != null) {
                    return endorsementRepository.findByUser_Id(userIdUUID, pageRequest);
                } else {
                    throw new IllegalArgumentException("At least one of skillID or userID must be provided.");
                }
            }
        } catch (IllegalArgumentException e) {

            e.printStackTrace();
            // todo Handle the validation error and return an appropriate response to the client
            throw new IllegalArgumentException("Invalid parameter(s) provided: " + e.getMessage());
        }
    }

    @Override
    public EndorsementModel createEndorsement(EndorsementDRO endorsementDRO) {
        UUID userId = endorsementDRO.getUserId();
        UUID skillId = endorsementDRO.getSkillId();
        Optional<UserModel> userOptional = userRepository.findById(userId);
        Optional<SkillModel> skillOptional = skillRepository.findById(skillId);
        if (userOptional.isPresent() && skillOptional.isPresent()) {
        EndorsementModel endorsementModel =  EndorsementModel.builder()
                .user(userOptional.get())
                .skill(skillOptional.get())
                .build();

            return endorsementRepository.save(endorsementModel);
        } else {
            if (userOptional.isEmpty())
                throw new NoEntityException("User with id:" + userId + " not found");
            throw new NoEntityException("Skill with id:" + skillId + " not found");
        }
    }
}