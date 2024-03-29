package com.example.MobilitySupport.repository;
import com.example.MobilitySupport.DTO.Map;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TestRepository extends JpaRepository<Map,Long> {
    Page<Map> findByAddrContaining(String addr, Pageable pageable);
    List<Map> findByAddrContaining(String addr);

    //https://zara49.tistory.com/130
    //https://devmoony.tistory.com/132
    //https://studyandwrite.tistory.com/495
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Map m SET m.stat = :stat where m.chgerId = :chgerId and   m.statId = :statid")
    void updateMapData(Long stat, String chgerId, String statid);
}
