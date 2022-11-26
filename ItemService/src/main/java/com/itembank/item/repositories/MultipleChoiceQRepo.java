package com.itembank.item.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itembank.item.Entity.MultipleChoiceQ;
import com.itembank.item.Model.QuestionIdVersion;

public interface MultipleChoiceQRepo extends JpaRepository<MultipleChoiceQ, QuestionIdVersion>{

}
