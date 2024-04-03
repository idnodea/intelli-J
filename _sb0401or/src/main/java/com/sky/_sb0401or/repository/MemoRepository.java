package com.sky._sb0401or.repository;

import com.sky._sb0401or.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;

//엔티티명,Long(보통 원칙)
public interface MemoRepository extends JpaRepository<Memo, Long> {
}
