package com.itembank.item.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itembank.item.Entity.IdVersionSequence;
import com.itembank.item.Entity.MultipleChoiceQ;
import com.itembank.item.Entity.QuestionBank;
import com.itembank.item.Entity.QuestionVersion;
import com.itembank.item.Entity.SubjectiveQ;
import com.itembank.item.Model.Item;
import com.itembank.item.Model.QuestionIdVersion;
import com.itembank.item.repositories.IdVersionSeqRepo;
import com.itembank.item.repositories.MultipleChoiceQRepo;
import com.itembank.item.repositories.QuestionBankRepo;
import com.itembank.item.repositories.QuestionVersionRepo;
import com.itembank.item.repositories.SubjectiveQRepo;

@RestController
@RequestMapping("/item")
public class ItemController {

	@Autowired
	private QuestionBankRepo quesBankRepo;
	@Autowired
	private QuestionVersionRepo quesVerRepo;
	@Autowired
	private IdVersionSeqRepo idVerSeqRepo;
	@Autowired
	private SubjectiveQRepo subqRepo;
	@Autowired
	private MultipleChoiceQRepo mcqRepo;

	@PostMapping(path = "/add")
	public ResponseEntity<Item> addItem(@RequestBody Item newItem) {

		Item it = new Item();
		
		QuestionBank quesToSave = new QuestionBank(newItem.getDomain(), newItem.getAuthorId(), newItem.getStatus());
		QuestionBank ques = quesBankRepo.save(quesToSave);

		IdVersionSequence idVerToSave = new IdVersionSequence(ques.getId(), 1);
		IdVersionSequence idVer = idVerSeqRepo.save(idVerToSave);

		QuestionVersion quesVerToAdd = new QuestionVersion(
				new QuestionIdVersion(ques.getId(), idVer.getVersion()),
				newItem.getType());
		QuestionVersion quesVer = quesVerRepo.save(quesVerToAdd);

		// Subjective
		if(quesVer.getType() == 1) {
			SubjectiveQ subqToSave = new SubjectiveQ(
					quesVer.getQuesIdVersion(), 
					newItem.getSubq().getQuesText(), 
					newItem.getSubq().getAns());
			SubjectiveQ subq = subqRepo.save(subqToSave);
			it.setSubq(subq);
		}
		// MCQ
		else if(quesVer.getType() == 2) {
			
			 List<MultipleChoiceQ> mcqList = new ArrayList<MultipleChoiceQ>();  
			 
			 for(MultipleChoiceQ mcqObj : newItem.getMcq()) {  
				 MultipleChoiceQ mcqToSave = new MultipleChoiceQ(
						quesVer.getQuesIdVersion(), 
						mcqObj.getQuesText(),
						mcqObj.getOptionNum(),
						mcqObj.getIsCorrect(),
						mcqObj.getOptionText()
					 );
				 MultipleChoiceQ mcq = mcqRepo.save(mcqToSave);
				 mcqList.add(mcq);
			}  
			
			it.setMcq(mcqList);
		}
		
		it.setId(ques.getId());
		it.setAuthorId(ques.getAuthorId());
		it.setDomain(ques.getDomain());
		it.setStatus(ques.getStatus());
		it.setVersion(quesVer.getQuesIdVersion().getVersion());
		it.setType(quesVer.getType());
		
		return new ResponseEntity<>(it, HttpStatus.OK);
	}

	
	@PostMapping(path ="/update")
	public ResponseEntity<Item> updateItem(@RequestBody Item newItem) {

		Optional<QuestionBank> ques = quesBankRepo.findById(newItem.getId());
		Optional<QuestionVersion> quesVer = quesVerRepo.findById(
				new QuestionIdVersion(newItem.getId(), newItem.getVersion()));
		Optional<IdVersionSequence> idVer = idVerSeqRepo.findById(newItem.getId());
				
		if(ques.isEmpty() || quesVer.isEmpty() || idVer.isEmpty()) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		
		if(ques.get().getStatus() != "ACTIVE") {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);			
		}

		Item it = new Item();

		IdVersionSequence idVerToUpdate = idVer.get(); 
		idVerToUpdate.setVersion(idVer.get().getVersion() + 1);
		IdVersionSequence idv = idVerSeqRepo.save(idVerToUpdate);

		QuestionBank quesToUpdate = ques.get();
		quesToUpdate.setAuthorId(newItem.getAuthorId());
		quesToUpdate.setDomain(newItem.getDomain());
		quesToUpdate.setStatus(newItem.getStatus());						
		QuestionBank q = quesBankRepo.save(quesToUpdate);
		
		QuestionVersion quesVerToSave = new QuestionVersion();			
		quesVerToSave.setQuesIdVersion(new QuestionIdVersion(q.getId(), idv.getVersion()));
		quesVerToSave.setType(newItem.getType());
		QuestionVersion qv = quesVerRepo.save(quesVerToSave);

		// Subjective
		if(qv.getType() == 1) {
			SubjectiveQ subqToSave = new SubjectiveQ(
					qv.getQuesIdVersion(), 
					newItem.getSubq().getQuesText(), 
					newItem.getSubq().getAns());
			SubjectiveQ subq = subqRepo.save(subqToSave);
			it.setSubq(subq);
		}
		// MCQ
		else if(qv.getType() == 2) {
			
			 List<MultipleChoiceQ> mcqList = new ArrayList<MultipleChoiceQ>();  
			 
			 for(MultipleChoiceQ mcqObj : newItem.getMcq()) {  
				 MultipleChoiceQ mcqToSave = new MultipleChoiceQ(
						qv.getQuesIdVersion(), 
						mcqObj.getQuesText(),
						mcqObj.getOptionNum(),
						mcqObj.getIsCorrect(),
						mcqObj.getOptionText()
					 );
				 MultipleChoiceQ mcq = mcqRepo.save(mcqToSave);
				 mcqList.add(mcq);
			}  
			
			it.setMcq(mcqList);
		}

		it.setId(q.getId());
		it.setAuthorId(q.getAuthorId());
		it.setDomain(q.getDomain());
		it.setStatus(q.getStatus());
		it.setVersion(qv.getQuesIdVersion().getVersion());
		it.setType(qv.getType());
				
		
		return new ResponseEntity<>(it, HttpStatus.OK);
	}

	@PostMapping(path ="/delete/{id}")
	public ResponseEntity<?> deleteItem(@PathVariable Integer id) {
		Optional<QuestionBank> ques = quesBankRepo.findById(id);

		if(ques.isEmpty()) {
            return new ResponseEntity<>( HttpStatus.NOT_FOUND );
		}
		
		QuestionBank quesToDelete = ques.get();
		quesToDelete.setStatus("DELETED");
		quesBankRepo.save(quesToDelete);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);		
	}
}
