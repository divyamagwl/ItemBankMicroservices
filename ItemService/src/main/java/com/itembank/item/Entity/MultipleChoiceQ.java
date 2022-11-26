package com.itembank.item.Entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import com.itembank.item.Model.QuestionIdVersion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MultipleChoiceQ {

    @EmbeddedId
    private QuestionIdVersion quesIdVersion;
	
	@Column(nullable = false)	
	private String quesText;

	@Column(nullable = false)	
	private int optionNum;

	@Column(nullable = false)	
	private Boolean isCorrect;

	@Column(nullable = false)	
	private String optionText;

	public QuestionIdVersion getQuesIdVersion() {
		return quesIdVersion;
	}

	public void setQuesIdVersion(QuestionIdVersion quesIdVersion) {
		this.quesIdVersion = quesIdVersion;
	}

	public String getQuesText() {
		return quesText;
	}

	public void setQuesText(String quesText) {
		this.quesText = quesText;
	}

	public int getOptionNum() {
		return optionNum;
	}

	public void setOptionNum(int optionNum) {
		this.optionNum = optionNum;
	}

	public Boolean getIsCorrect() {
		return isCorrect;
	}

	public void setIsCorrect(Boolean isCorrect) {
		this.isCorrect = isCorrect;
	}

	public String getOptionText() {
		return optionText;
	}

	public void setOptionText(String optionText) {
		this.optionText = optionText;
	}

	public MultipleChoiceQ() {
		super();
	}

	public MultipleChoiceQ(QuestionIdVersion quesIdVersion, String quesText, int optionNum, Boolean isCorrect,
			String optionText) {
		super();
		this.quesIdVersion = quesIdVersion;
		this.quesText = quesText;
		this.optionNum = optionNum;
		this.isCorrect = isCorrect;
		this.optionText = optionText;
	}
}
