package com.itembank.author.Controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.itembank.author.Entity.Author;
import com.itembank.author.repositories.AuthorRepo;


@RestController
@RequestMapping("/author")
public class AuthorController {
	
	@Autowired
	private AuthorRepo repo; 

	@PostMapping(path ="/add")
	public ResponseEntity<Author> addAuthor(@RequestBody Author newAuthor) {
		
		Author author = repo.save(newAuthor);
		return new ResponseEntity<>(author, HttpStatus.OK);
	}
	
	@RequestMapping(path ="/uname/{uname}")
	public ResponseEntity<Author> getByUname(@PathVariable String uname) {
		
		Author author = repo.findByUsername(uname);
		return new ResponseEntity<>(author, HttpStatus.OK);
	}
		
}
