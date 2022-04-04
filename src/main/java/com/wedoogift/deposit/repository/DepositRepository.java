package com.wedoogift.deposit.repository;

import com.wedoogift.deposit.entity.DepositEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepositRepository extends JpaRepository<DepositEntity, Integer> {
}
