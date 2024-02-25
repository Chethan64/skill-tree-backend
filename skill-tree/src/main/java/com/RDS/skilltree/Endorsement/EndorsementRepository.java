package com.RDS.skilltree.Endorsement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EndorsementRepository extends JpaRepository<EndorsementModel, UUID> {
    Page<EndorsementModel> findByUser_Id(UUID userId, PageRequest pageRequest);
    Page<EndorsementModel> findBySkill_Id(UUID skillId, PageRequest pageRequest);
    Page<EndorsementModel> findBySkill_IdAndUser_Id(UUID skillId, UUID userId, PageRequest pageRequest);
}
