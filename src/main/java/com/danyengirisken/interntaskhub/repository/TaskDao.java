package com.danyengirisken.interntaskhub.repository;

import com.danyengirisken.interntaskhub.entity.Task;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskDao extends JpaRepository<Task, Long> {

    List<Task> findAllByOrderByIdDesc();
}
