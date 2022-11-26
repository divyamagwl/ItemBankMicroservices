package com.itembank.item.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itembank.item.Entity.QuestionVersion;
import com.itembank.item.Model.QuestionIdVersion;

public interface QuestionVersionRepo extends JpaRepository<QuestionVersion, QuestionIdVersion>{

}
